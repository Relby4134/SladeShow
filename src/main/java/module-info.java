module com.slideshow {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires com.google.gson;
    requires java.desktop;

    opens com.slideshow         to javafx.graphics;
    opens com.slideshow.ui      to javafx.fxml;
    opens com.slideshow.domain  to com.google.gson;
    exports com.slideshow;
}
