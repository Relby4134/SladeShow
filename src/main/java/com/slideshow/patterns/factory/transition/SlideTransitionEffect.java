package com.slideshow.patterns.factory.transition;

import com.slideshow.patterns.factory.TransitionEffect;
import javafx.animation.*;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class SlideTransitionEffect implements TransitionEffect {
    @Override
    public void apply(Pane from, Pane to) {
        if (to == null) return;
        Duration dur = Duration.millis(480);
        to.setTranslateX(420);
        TranslateTransition tt = new TranslateTransition(dur, to);
        tt.setFromX(420);
        tt.setToX(0);
        FadeTransition ft = new FadeTransition(dur, to);
        ft.setFromValue(0.2);
        ft.setToValue(1.0);
        new ParallelTransition(tt, ft).play();
    }
}
