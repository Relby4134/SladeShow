package com.slideshow.patterns.factory;

import javafx.scene.layout.Pane;

public interface TransitionEffect {
    void apply(Pane from, Pane to);
}
