package com.slideshow.patterns.factory.renderer;

import com.slideshow.domain.GraphicObject;
import com.slideshow.domain.Slide;
import com.slideshow.patterns.factory.SlideRenderer;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.*;

public class SimpleImageRenderer implements SlideRenderer {

    @Override
    public void render(Slide slide, Pane c) {
        c.getChildren().clear();

        // фоновое изображение
        if (slide.getImage() != null) {
            ImageView iv = new ImageView(slide.getImage());
            iv.setPreserveRatio(true);
            iv.fitWidthProperty().bind(c.widthProperty());
            iv.fitHeightProperty().bind(c.heightProperty());
            c.getChildren().add(iv);
        }

        Pane overlay = new Pane();
        overlay.prefWidthProperty().bind(c.widthProperty());
        overlay.prefHeightProperty().bind(c.heightProperty());
        overlay.setPickOnBounds(false);

        // заголовок в левом верхнем углу
        if (!slide.getTitle().isBlank() && !slide.getTitle().equals("Без названия")) {
            Label t = new Label(slide.getTitle());
            t.setFont(Font.font("Arial", FontWeight.BOLD, 22));
            t.setTextFill(Color.WHITE);
            t.setStyle("-fx-background-color: rgba(0,0,0,0.55); -fx-padding: 6 14;");
            t.setLayoutX(12);
            t.setLayoutY(12);
            overlay.getChildren().add(t);
        }

//        for (GraphicObject g : slide.getGraphics()) {
//            renderGraphic(g, overlay);
//        }

        c.getChildren().add(overlay);
    }

//    protected void renderGraphic(GraphicObject g, Pane overlay) {
//        switch (g.getType()) {
//            case TEXT -> {
//                Label l = new Label(g.getContent());
//                l.setFont(Font.font("Arial", 16));
//                l.setTextFill(Color.web(g.getColor()));
//                l.setLayoutX(g.getX());
//                l.setLayoutY(g.getY());
//                overlay.getChildren().add(l);
//            }
//            case RECTANGLE -> {
//                Rectangle r = new Rectangle(g.getX(), g.getY(), g.getWidth(), g.getHeight());
//                r.setFill(Color.TRANSPARENT);
//                r.setStroke(Color.web(g.getColor()));
//                r.setStrokeWidth(2);
//                overlay.getChildren().add(r);
//            }
//            case CIRCLE -> {
//                double cx = g.getX() + g.getWidth() / 2;
//                double cy = g.getY() + g.getHeight() / 2;
//                Circle ci = new Circle(cx, cy, Math.min(g.getWidth(), g.getHeight()) / 2);
//                ci.setFill(Color.TRANSPARENT);
//                ci.setStroke(Color.web(g.getColor()));
//                ci.setStrokeWidth(2);
//                overlay.getChildren().add(ci);
//            }
//            case ARROW -> {
//                Line ln = new Line(g.getX(), g.getY(), g.getX() + g.getWidth(), g.getY() + g.getHeight());
//                ln.setStroke(Color.web(g.getColor()));
//                ln.setStrokeWidth(2);
//                overlay.getChildren().add(ln);
//            }
//        }
//    }
}
