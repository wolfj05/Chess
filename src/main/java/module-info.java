module at.ac.hcw.chess {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.compiler;
    requires javafx.media;

    opens at.ac.hcw.chess to javafx.fxml;
    exports at.ac.hcw.chess;
}