package com.slideshow.patterns.factory;

public interface SlideComponentFactory {
    SlideRenderer createRenderer();

    TransitionEffect createTransition();

    StatusBarView createStatusBar();
}
