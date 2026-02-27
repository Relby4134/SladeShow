package com.slideshow.patterns.factory;

import com.slideshow.patterns.factory.renderer.AnimatedRenderer;
import com.slideshow.patterns.factory.statusbar.AnimatedStatusBar;
import com.slideshow.patterns.factory.transition.FadeTransitionEffect;

public class AnimatedSlideFactory implements SlideComponentFactory {
    @Override
    public SlideRenderer createRenderer() {
        return new AnimatedRenderer();
    }

    @Override
    public TransitionEffect createTransition() {
        return new FadeTransitionEffect();
    }

    @Override
    public StatusBarView createStatusBar() {
        return new AnimatedStatusBar();
    }
}
