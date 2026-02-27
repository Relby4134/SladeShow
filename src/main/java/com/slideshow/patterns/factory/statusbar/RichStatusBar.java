package com.slideshow.patterns.factory.statusbar;

import com.slideshow.patterns.factory.StatusBarView;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.util.Duration;

public class RichStatusBar implements StatusBarView {
    @Override public Node buildStatusBar(int cur, int total) {
        Label l = new Label("\u0421\u043b\u0430\u0439\u0434 " + cur + " / " + total);
        l.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        l.setTextFill(Color.WHITE);

        ProgressBar pb = new ProgressBar(total > 0 ? (double) cur / total : 0);
        pb.setPrefWidth(200);
        pb.setStyle("-fx-accent: #4CAF50;");

        HBox dots = new HBox(4);
        for (int i = 1; i <= Math.min(total, 12); i++) {
            Label d = new Label("\u25cf");
            d.setTextFill(i == cur ? Color.WHITE : Color.GRAY);
            d.setFont(Font.font(9));
            dots.getChildren().add(d);
        }
        if (total > 12) dots.getChildren().add(new Label("\u2026"));

        VBox box = new VBox(4, l, pb, dots);
        box.setPadding(new Insets(6));
        box.setStyle("-fx-background-color: rgba(30,30,30,0.85); -fx-background-radius: 6;");
        FadeTransition ft = new FadeTransition(Duration.millis(300), box);
        ft.setFromValue(0); ft.setToValue(1); ft.play();
        return box;
    }
}
