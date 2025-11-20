package at.ac.hcw.chess.scenes;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainMenu {

    private final SceneManager sceneManager;
    private Scene scene;

    public MainMenu(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        createScene();
    }

    private void createScene() {
        Button startButton = new Button("Start Game");
        Button exitButton = new Button("End Game");
        VBox layout = new VBox(10, startButton, exitButton);
        layout.setStyle("-fx-alignment: center; -fx-padding: 30;");
        scene = new Scene(layout, 400, 300);

        startButton.setOnAction(e -> sceneManager.startGame());
        exitButton.setOnAction(e -> sceneManager.exitGame());
    }

    public void show() {
        sceneManager.getStage().setScene(scene);
        sceneManager.getStage().setFullScreenExitHint("");
        sceneManager.getStage().setFullScreenExitKeyCombination(javafx.scene.input.KeyCombination.NO_MATCH);
        sceneManager.getStage().setFullScreen(true);
        sceneManager.getStage().setResizable(false);
    }
}
