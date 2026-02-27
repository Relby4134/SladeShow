package com.slideshow.patterns.factory;

import com.slideshow.domain.Slide;
import javafx.scene.layout.Pane;

public interface SlideRenderer {
    void render(Slide slide, Pane container);
}
