package at.ac.hcw.chess;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane(); // Einfacher Container
        Scene scene = new Scene(root, 800, 600); // Standardgröße, wird Fullscreen überschrieben

        primaryStage.setTitle("Test Fullscreen Window");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true); // Fullscreen aktivieren
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}