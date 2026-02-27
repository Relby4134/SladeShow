package com.slideshow.patterns.factory.renderer;

import com.slideshow.domain.Slide;
import com.slideshow.patterns.factory.SlideRenderer;
import javafx.animation.*;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class AnimatedRenderer implements SlideRenderer {
    private final SimpleImageRenderer base = new SimpleImageRenderer();

    @Override
    public void render(Slide slide, Pane c) {
        base.render(slide, c);
        switch (slide.getAnimation().getType()) {
            case FADE_IN -> {
                FadeTransition ft = new FadeTransition(Duration.millis(slide.getAnimation().getDurationMs()), c);
                ft.setFromValue(0);
                ft.setToValue(1);
                ft.play();
            }
            case SLIDE_IN -> {
                c.setTranslateX(-350);
                TranslateTransition tt = new TranslateTransition(Duration.millis(slide.getAnimation().getDurationMs()), c);
                tt.setFromX(-350);
                tt.setToX(0);
                tt.play();
            }
            case ZOOM_IN -> {
                ScaleTransition st = new ScaleTransition(Duration.millis(slide.getAnimation().getDurationMs()), c);
                st.setFromX(0.3);
                st.setFromY(0.3);
                st.setToX(1);
                st.setToY(1);
                st.play();
            }
            default -> {
            }
        }
    }
}
