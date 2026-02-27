package com.slideshow.controller;

import com.slideshow.domain.Slide;
import com.slideshow.domain.SlideShow;
import com.slideshow.infrastructure.AudioPlayer;
import com.slideshow.infrastructure.ConfigManager;
import com.slideshow.infrastructure.FileManager;
import com.slideshow.patterns.factory.SlideComponentFactory;
import com.slideshow.patterns.iterator.SlideIterator;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.function.Consumer;

public class SlideController {

    private final SlideShow slideShow;
    private SlideComponentFactory factory;
    private final FileManager fileManager;
    private final AudioPlayer audioPlayer;
    private final ConfigManager configManager;

    private SlideIterator iterator;
    private Consumer<Node> onStatusBarUpdate;

    public SlideController(SlideShow slideShow, SlideComponentFactory factory) {
        this.slideShow = slideShow;
        this.factory = factory;
        this.fileManager = new FileManager();
        this.audioPlayer = new AudioPlayer();
        this.configManager = new ConfigManager();
        this.iterator = slideShow.iterator();
    }

    public void setFactory(SlideComponentFactory f) {
        this.factory = f;
    }

    public void setOnStatusBarUpdate(Consumer<Node> cb) {
        this.onStatusBarUpdate = cb;
    }


    public void nextSlide(Pane c) {
        if (slideShow.size() > 0 && iterator.hasNext()) render(iterator.next(), c);
    }

    public void prevSlide(Pane c) {
        if (slideShow.size() > 0 && iterator.hasPrev()) render(iterator.prev(), c);
    }

    public void firstSlide(Pane c) {
        if (slideShow.size() > 0) render(iterator.first(), c);
    }

    public void lastSlide(Pane c) {
        if (slideShow.size() > 0) render(iterator.last(), c);
    }

    public void goToSlide(int index, Pane c) {
        if (index < 0 || index >= slideShow.size()) return;
        iterator = slideShow.iterator();
        Slide t = iterator.first();
        for (int i = 0; i < index; i++) t = iterator.next();
        render(t, c);
    }


    private void render(Slide slide, Pane c) {
        if (slide == null) return;
        factory.createRenderer().render(slide, c);
        factory.createTransition().apply(null, c);
        audioPlayer.stop();
        if (slide.getAudio() != null) audioPlayer.play(slide.getAudio().getPath());
        if (onStatusBarUpdate != null)
            onStatusBarUpdate.accept(
                    factory.createStatusBar().buildStatusBar(iterator.currentIndex() + 1, slideShow.size()));
    }


    public void addSlide(Slide s) {
        slideShow.addSlide(s);
        iterator = slideShow.iterator();
    }

    public void removeCurrentSlide(Pane c) {
        if (slideShow.size() == 0) return;
        int idx = iterator.currentIndex();
        slideShow.removeSlide(idx);
        iterator = slideShow.iterator();
        if (slideShow.size() > 0) goToSlide(Math.min(idx, slideShow.size() - 1), c);
        else c.getChildren().clear();
    }

    public void reorder(int from, int to, Pane c) {
        slideShow.reorder(from, to);
        iterator = slideShow.iterator();
        goToSlide(to, c);
    }


    public void save(String path) throws IOException {
        configManager.save(slideShow, path);
    }

    public SlideShow load(String path) throws IOException {
        return configManager.load(path);
    }

    public SlideShow getSlideShow() {
        return slideShow;
    }

    public SlideIterator getIterator() {
        return iterator;
    }

    public AudioPlayer getAudioPlayer() {
        return audioPlayer;
    }

    public boolean hasNext() {
        return iterator.hasNext();
    }

    public boolean hasPrev() {
        return iterator.hasPrev();
    }

    public int currentIndex() {
        return iterator.currentIndex();
    }

    public int totalSlides() {
        return slideShow.size();
    }

    public void refreshCurrent(Pane c) {
        if (slideShow.size() > 0) goToSlide(iterator.currentIndex(), c);
    }
}
