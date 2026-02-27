package com.slideshow.patterns.factory;

import com.slideshow.patterns.factory.renderer.RichRenderer;
import com.slideshow.patterns.factory.statusbar.RichStatusBar;
import com.slideshow.patterns.factory.transition.SlideTransitionEffect;

public class RichSlideFactory implements SlideComponentFactory {
    @Override
    public SlideRenderer createRenderer() {
        return new RichRenderer();
    }

    @Override
    public TransitionEffect createTransition() {
        return new SlideTransitionEffect();
    }

    @Override
    public StatusBarView createStatusBar() {
        return new RichStatusBar();
    }
}
