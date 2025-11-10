package at.ac.hcw.chess.scenes;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GameScreen {

    private final Stage stage;

    public GameScreen(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        // === Menü-Buttons ===
        Button startButton = new Button("Game Screen");
        Button settingsButton = new Button("Settings");
        Button exitButton = new Button("Exit");

        // === Layout ===
        VBox layout = new VBox(10, startButton, settingsButton, exitButton);
        layout.setStyle("-fx-alignment: center; -fx-padding: 30;");

        // === Scene ===
        Scene scene = new Scene(layout, 400, 300);
        stage.setFullScreen(true);
        stage.setTitle("Main Menu");
        stage.setScene(scene);
        stage.show();

        // === Aktionen ===
        exitButton.setOnAction(e -> stage.close());
    }
}