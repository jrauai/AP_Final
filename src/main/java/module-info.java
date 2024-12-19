module AP {
    requires com.google.zxing;
    requires com.google.zxing.javase;
    requires googleauth;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;

    exports Assignment to javafx.graphics, javafx.fxml;
    opens Assignment to javafx.fxml;
}