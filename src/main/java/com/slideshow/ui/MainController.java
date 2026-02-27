package com.slideshow.ui;

import com.slideshow.controller.SlideController;
import com.slideshow.domain.*;
import com.slideshow.infrastructure.FileManager;
import com.slideshow.patterns.factory.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.Duration;

import java.io.File;
import java.util.List;

public class MainController {

    @FXML private ComboBox<String> modeCombo;
    @FXML private StackPane        slidePane;
    @FXML private HBox             statusBarContainer;
    @FXML private Label            defaultStatusLabel;
    @FXML private TextArea         notesArea;
    @FXML private ListView<String> slideListView;
    @FXML private Label            emptyLabel;
    @FXML private Button           btnFirst, btnPrev, btnNext, btnLast;
    @FXML private Label            audioLabel;

    private SlideController controller;
    private SlideShow       slideShow;
    private FileManager     fm = new FileManager();
    private String          currentAudioPath = null;
    private Timeline autoPlay;

    @FXML
    public void initialize() {
        slideShow  = new SlideShow();
        controller = new SlideController(slideShow, new BasicSlideFactory());
        controller.setOnStatusBarUpdate(node -> {
            statusBarContainer.getChildren().clear();
            statusBarContainer.getChildren().add(node);
        });

        modeCombo.getItems().addAll("Простой", "Анимированный", "Полный");
        modeCombo.getSelectionModel().selectFirst();
        refreshNav();
    }


    @FXML
    private void onModeChanged() {
        String mode = modeCombo.getValue();
        if (mode.equals("Анимированный")) {
            stopAutoPlay();
            controller.setFactory(new AnimatedSlideFactory());
        } else if (mode.equals("Полный")) {
            controller.setFactory(new RichSlideFactory());
            startAutoPlay();
        } else {
            stopAutoPlay();
            controller.setFactory(new BasicSlideFactory());
        }
        controller.refreshCurrent(slidePane);
    }
    private void startAutoPlay() {
        stopAutoPlay();

        autoPlay = new Timeline(
                new KeyFrame(Duration.seconds(3), e -> {
                    if (controller.hasNext()) {
                        controller.nextSlide(slidePane);
                    } else {
                        autoPlay.stop();
                    }
                })
        );

        autoPlay.setCycleCount(Animation.INDEFINITE);
        autoPlay.play();
    }
    private void stopAutoPlay() {
        if (autoPlay != null) {
            autoPlay.stop();
        }
    }


    @FXML
    private void onLoadImage() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Выбери картинку");
        fc.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Картинки", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
        );
        File file = fc.showOpenDialog(stage());
        if (file == null) return;
        addImageFile(file);
    }

    @FXML
    private void onLoadFolder() {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("Выбери папку с картинками");
        File dir = dc.showDialog(stage());
        if (dir == null) return;

        List<File> images = fm.listImages(dir.getAbsolutePath());
        if (images.isEmpty()) {
            showAlert("Пусто", "В папке нет картинок (png, jpg, gif, bmp)");
            return;
        }

        for (File f : images) {
            addImageFile(f);
        }
        controller.goToSlide(slideShow.size() - 1, slidePane);
        emptyLabel.setVisible(false);
        refreshNav();
    }

    private void addImageFile(File file) {
        var img  = fm.loadImage(file.getAbsolutePath());
        String name = file.getName().replaceAll("\\.[^.]+$", "");
        Slide slide = new Slide.Builder(img, file.getAbsolutePath())
            .title(name)
            .build();
        controller.addSlide(slide);
        refreshList();
        emptyLabel.setVisible(false);
    }

    @FXML
    private void onSaveShow() {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("Куда сохранить показ?");
        File dir = dc.showDialog(stage());
        if (dir == null) return;
        try {
            controller.save(dir.getAbsolutePath());
            showAlert("Готово", "Сохранено в: " + dir.getAbsolutePath());
        } catch (Exception e) {
            showAlert("Ошибка", "Не сохранилось: " + e.getMessage());
        }
    }

    @FXML
    private void onLoadShow() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Открыть config.json");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Конфиг слайдшоу", "*.json"));
        File file = fc.showOpenDialog(stage());
        if (file == null) return;

        String dirPath = file.getParent();

        try {
            SlideShow loaded = controller.load(dirPath);
            slideShow.getSlides().clear();
            for (Slide s : loaded.getSlides()) {
                slideShow.addSlide(s);
            }
            slideShow.setName(loaded.getName());

            controller = new SlideController(slideShow, currentFactory());
            controller.setOnStatusBarUpdate(node -> {
                statusBarContainer.getChildren().clear();
                statusBarContainer.getChildren().add(node);
            });

            refreshList();
            if (slideShow.size() > 0) {
                controller.firstSlide(slidePane);
                emptyLabel.setVisible(false);
            }
            refreshNav();
        } catch (Exception e) {
            showAlert("Ошибка", "Не открылось: " + e.getMessage());
        }
    }

    @FXML
    private void onExit() {
        stage().close();
    }


    @FXML
    private void onAddText() {
        if (slideShow.size() == 0) {
            showAlert("Ой", "Сначала добавь картинку!");
            return;
        }
        TextInputDialog d = new TextInputDialog("Текст");
        d.setTitle("Добавить текст");
        d.setHeaderText("Что написать на слайде?");
        d.showAndWait().ifPresent(text -> {
            if (text.isBlank()) return;
            int i = controller.currentIndex();
            Slide cur = slideShow.getSlides().get(i);

            GraphicObject graphic = new GraphicObject(
                GraphicObject.Type.TEXT, text, 50, 50, 200, 40, "#ffffff"
            );
            slideShow.getSlides().set(i, rebuildWithGraphic(cur, graphic));
            controller.refreshCurrent(slidePane);
        });
    }

    @FXML
    private void onSetAnimation() {
        if (slideShow.size() == 0) return;
        ChoiceDialog<String> d = new ChoiceDialog<>("NONE", "NONE", "FADE_IN", "SLIDE_IN", "ZOOM_IN");
        d.setTitle("Анимация");
        d.setHeaderText("Выбери тип анимации:");
        d.showAndWait().ifPresent(type -> {
            int i = controller.currentIndex();
            Slide cur = slideShow.getSlides().get(i);

            AnimationSettings anim;
            switch (type) {
                case "FADE_IN":  anim = AnimationSettings.fadeIn(800);  break;
                case "SLIDE_IN": anim = AnimationSettings.slideIn(600); break;
                case "ZOOM_IN":  anim = AnimationSettings.zoomIn(700);  break;
                default:         anim = AnimationSettings.none();
            }
            slideShow.getSlides().set(i, rebuildWithAnimation(cur, anim));
            controller.refreshCurrent(slidePane);
        });
    }

    @FXML
    private void onAddAudio() {
        if (slideShow.size() == 0) return;
        FileChooser fc = new FileChooser();
        fc.setTitle("Выбери аудиофайл");
        fc.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Аудио", "*.mp3", "*.wav", "*.m4a", "*.ogg")
        );
        File file = fc.showOpenDialog(stage());
        if (file == null) return;

        currentAudioPath = file.getAbsolutePath();
        audioLabel.setText(file.getName());

        int i = controller.currentIndex();
        Slide cur = slideShow.getSlides().get(i);
        AudioTrack track = new AudioTrack(file.getAbsolutePath(), file.getName());
        slideShow.getSlides().set(i, rebuildWithAudio(cur, track));
        // do NOT play — user clicks ▶ manually
    }


    @FXML
    private void onAudioPlay() {
        if (slideShow.size() > 0) {
            Slide cur = slideShow.getSlides().get(controller.currentIndex());
            if (cur.getAudio() != null) {
                currentAudioPath = cur.getAudio().getPath();
                audioLabel.setText(cur.getAudio().getName());
            }
        }
        if (currentAudioPath != null) {
            controller.getAudioPlayer().play(currentAudioPath);
        }
    }

    @FXML
    private void onAudioPause() {
        controller.getAudioPlayer().pause();
    }

    @FXML
    private void onAudioStop() {
        controller.getAudioPlayer().stop();
    }


    @FXML
    private void onSaveNote() {
        int i = controller.currentIndex();
        if (i < 0 || i >= slideShow.size()) return;

        Slide cur = slideShow.getSlides().get(i);
        String noteText = notesArea.getText();

        // Slide is immutable — rebuild via Builder
        Slide.Builder b = new Slide.Builder(cur.getImage(), cur.getImagePath())
            .title(cur.getTitle())
            .notes(noteText)
            .animation(cur.getAnimation());
        if (cur.getAudio() != null) b.audio(cur.getAudio());
        for (GraphicObject g : cur.getGraphics()) b.addGraphic(g);

        slideShow.getSlides().set(i, b.build());
        System.out.println("Note saved for slide " + (i + 1));
    }


    @FXML private void onFirst() { controller.firstSlide(slidePane); syncNotes(); refreshNav(); }
    @FXML private void onPrev()  { controller.prevSlide(slidePane);  syncNotes(); refreshNav(); }
    @FXML private void onNext()  { controller.nextSlide(slidePane);  syncNotes(); refreshNav(); }
    @FXML private void onLast()  { controller.lastSlide(slidePane);  syncNotes(); refreshNav(); }

    @FXML
    private void onSlideSelected() {
        int i = slideListView.getSelectionModel().getSelectedIndex();
        if (i < 0) return;
        controller.goToSlide(i, slidePane);
        syncNotes();
        refreshNav();
    }

    @FXML
    private void onMoveUp() {
        int i = controller.currentIndex();
        if (i > 0) {
            controller.reorder(i, i - 1, slidePane);
            refreshList();
            slideListView.getSelectionModel().select(i - 1);
        }
    }

    @FXML
    private void onMoveDown() {
        int i = controller.currentIndex();
        if (i < slideShow.size() - 1) {
            controller.reorder(i, i + 1, slidePane);
            refreshList();
            slideListView.getSelectionModel().select(i + 1);
        }
    }

    @FXML
    private void onDeleteSlide() {
        if (slideShow.size() == 0) return;
        controller.removeCurrentSlide(slidePane);
        refreshList();
        refreshNav();
        if (slideShow.size() == 0) {
            emptyLabel.setVisible(true);
            statusBarContainer.getChildren().setAll(defaultStatusLabel);
        }
    }


    private void refreshList() {
        slideListView.getItems().clear();
        for (int i = 0; i < slideShow.size(); i++) {
            slideListView.getItems().add((i + 1) + ". " + slideShow.getSlides().get(i).getTitle());
        }
    }

    private void syncNotes() {
        int i = controller.currentIndex();
        if (i < slideShow.size()) {
            Slide cur = slideShow.getSlides().get(i);
            notesArea.setText(cur.getNotes());
            slideListView.getSelectionModel().select(i);
            if (cur.getAudio() != null) {
                currentAudioPath = cur.getAudio().getPath();
                audioLabel.setText(cur.getAudio().getName());
            } else {
                audioLabel.setText("Нет аудио");
            }
        }
    }

    private void refreshNav() {
        btnFirst.setDisable(!controller.hasPrev());
        btnPrev.setDisable(!controller.hasPrev());
        btnNext.setDisable(!controller.hasNext());
        btnLast.setDisable(!controller.hasNext());
    }

    private SlideComponentFactory currentFactory() {
        if (modeCombo.getValue() == null) return new BasicSlideFactory();
        if (modeCombo.getValue().equals("Анимированный")) return new AnimatedSlideFactory();
        if (modeCombo.getValue().equals("Полный")) return new RichSlideFactory();
        return new BasicSlideFactory();
    }

    private Stage stage() {
        return (Stage) slidePane.getScene().getWindow();
    }

    private void showAlert(String title, String message) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(message);
        a.showAndWait();
    }


    private Slide rebuildWithGraphic(Slide cur, GraphicObject go) {
        Slide.Builder b = new Slide.Builder(cur.getImage(), cur.getImagePath())
            .title(cur.getTitle()).notes(cur.getNotes()).animation(cur.getAnimation());
        if (cur.getAudio() != null) b.audio(cur.getAudio());
        for (GraphicObject g : cur.getGraphics()) b.addGraphic(g);
        b.addGraphic(go);
        return b.build();
    }

    private Slide rebuildWithAnimation(Slide cur, AnimationSettings anim) {
        Slide.Builder b = new Slide.Builder(cur.getImage(), cur.getImagePath())
            .title(cur.getTitle()).notes(cur.getNotes()).animation(anim);
        if (cur.getAudio() != null) b.audio(cur.getAudio());
        for (GraphicObject g : cur.getGraphics()) b.addGraphic(g);
        return b.build();
    }

    private Slide rebuildWithAudio(Slide cur, AudioTrack track) {
        Slide.Builder b = new Slide.Builder(cur.getImage(), cur.getImagePath())
            .title(cur.getTitle()).notes(cur.getNotes()).animation(cur.getAnimation()).audio(track);
        for (GraphicObject g : cur.getGraphics()) b.addGraphic(g);
        return b.build();
    }
}
