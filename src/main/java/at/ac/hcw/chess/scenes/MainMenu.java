package at.ac.hcw.chess.scenes;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainMenu {

    private final Stage stage;

    public MainMenu(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        // === Menü-Buttons ===
        Button startButton = new Button("Start Game");
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
        startButton.setOnAction(e -> openGameScene());
        settingsButton.setOnAction(e -> openSettingsScene());
        exitButton.setOnAction(e -> stage.close());
    }

    private void startGame() {
        show();
        /*VBox layout = new VBox(new Button("Zurück zum Menü"));
        layout.setStyle("-fx-alignment: center; -fx-padding: 30;");
        Scene gameScene = new Scene(layout, 400, 300);
        stage.setScene(gameScene);

         */
    }

    private void openSettingsScene() {
        VBox layout = new VBox(new Button("Settings Screen"));
        layout.setStyle("-fx-alignment: center; -fx-padding: 30;");
        Scene settingsScene = new Scene(layout, 400, 300);
        stage.setScene(settingsScene);
    }
}
