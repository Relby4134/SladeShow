package com.slideshow.patterns.factory.statusbar;

import com.slideshow.patterns.factory.StatusBarView;
import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.util.Duration;

public class AnimatedStatusBar implements StatusBarView {
    @Override
    public Node buildStatusBar(int cur, int total) {
        Label l = new Label("\u0421\u043b\u0430\u0439\u0434 " + cur + " \u0438\u0437 " + total);
        l.setFont(Font.font("Arial", FontWeight.BOLD, 9));
        l.setTextFill(Color.STEELBLUE);
        ProgressBar pb = new ProgressBar(total > 0 ? (double) cur / total : 0);
        pb.setPrefWidth(160);
        HBox box = new HBox(10, l, pb);
        box.setStyle("-fx-padding: 4;");
        FadeTransition ft = new FadeTransition(Duration.millis(400), box);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
        return box;
    }
}
