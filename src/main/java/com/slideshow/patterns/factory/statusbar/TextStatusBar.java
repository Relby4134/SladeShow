package com.slideshow.patterns.factory.statusbar;

import com.slideshow.patterns.factory.StatusBarView;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.*;

public class TextStatusBar implements StatusBarView {
    @Override
    public Node buildStatusBar(int cur, int total) {
        Label l = new Label("\u0421\u043b\u0430\u0439\u0434 " + cur + " \u0438\u0437 " + total);
        l.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        l.setTextFill(Color.DARKSLATEGRAY);
        return l;
    }
}
