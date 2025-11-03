package at.ac.hcw.chess.scenes;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GameScreen {
    public void initGame(Stage primaryStage){
        StackPane root = new StackPane(); // Einfacher Container
        Scene scene = new Scene(root, 800, 600); // Standardgröße, wird Fullscreen überschrieben

        primaryStage.setTitle("GameScreen");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true); // Fullscreen aktivieren
        primaryStage.show();
    }
}
