package com.slideshow.patterns.factory;

import com.slideshow.patterns.factory.renderer.SimpleImageRenderer;
import com.slideshow.patterns.factory.statusbar.TextStatusBar;
import com.slideshow.patterns.factory.transition.InstantTransition;

public class BasicSlideFactory implements SlideComponentFactory {
    @Override
    public SlideRenderer createRenderer() {
        return new SimpleImageRenderer();
    }

    @Override
    public TransitionEffect createTransition() {
        return new InstantTransition();
    }

    @Override
    public StatusBarView createStatusBar() {
        return new TextStatusBar();
    }
}
