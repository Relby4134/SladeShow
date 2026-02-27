package com.slideshow.patterns.factory.renderer;

import com.slideshow.domain.Slide;
import com.slideshow.patterns.factory.SlideRenderer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class RichRenderer implements SlideRenderer {
    private final AnimatedRenderer base = new AnimatedRenderer();

    @Override
    public void render(Slide slide, Pane c) {
        base.render(slide, c);

        VBox bottomBox = new VBox(4);

        if (slide.getAudio() != null) {
            Label al = new Label("♪ " + slide.getAudio().getName());
            al.setFont(Font.font("Arial", 12));
            al.setTextFill(Color.LIGHTGREEN);
            al.setStyle("-fx-background-color: rgba(0,0,0,0.5); -fx-padding: 3 8;");
            bottomBox.getChildren().add(al);
        }

        if (!slide.getNotes().isBlank()) {
            Label nl = new Label("ℹ " + slide.getNotes());
            nl.setFont(Font.font("Arial", 13));
            nl.setTextFill(Color.LIGHTYELLOW);
            nl.setStyle("-fx-background-color: rgba(0,0,0,0.65); -fx-padding: 5 12; -fx-background-radius: 4;");
            nl.setWrapText(true);
            nl.setMaxWidth(500);
            bottomBox.getChildren().add(nl);
        }

        if (!bottomBox.getChildren().isEmpty()) {
            StackPane.setAlignment(bottomBox, Pos.BOTTOM_LEFT);
            StackPane.setMargin(bottomBox, new Insets(0, 0, 10, 12));
            c.getChildren().add(bottomBox);
        }
    }
}
