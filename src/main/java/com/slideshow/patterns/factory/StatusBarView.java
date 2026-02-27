package com.slideshow.patterns.factory;

import javafx.scene.Node;

public interface StatusBarView {
    Node buildStatusBar(int current, int total);
}
