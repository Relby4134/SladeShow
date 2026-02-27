package com.slideshow.patterns.factory.transition;

import com.slideshow.patterns.factory.TransitionEffect;
import javafx.scene.layout.Pane;

public class InstantTransition implements TransitionEffect {
    @Override
    public void apply(Pane from, Pane to) {
        if (to != null) to.setOpacity(1.0);
    }
}
