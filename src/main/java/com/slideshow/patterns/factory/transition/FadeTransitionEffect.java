package com.slideshow.patterns.factory.transition;

import com.slideshow.patterns.factory.TransitionEffect;
import javafx.animation.FadeTransition;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class FadeTransitionEffect implements TransitionEffect {
    @Override
    public void apply(Pane from, Pane to) {
        if (to == null) return;
        to.setOpacity(0);
        FadeTransition ft = new FadeTransition(Duration.millis(600), to);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }
}
