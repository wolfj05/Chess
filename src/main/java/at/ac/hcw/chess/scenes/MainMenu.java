package at.ac.hcw.chess.scenes;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class MainMenu {

    private final SceneManager sceneManager;
    private Scene scene;

    public MainMenu(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        createScene();
    }

    private void createScene() {
        Button startButton = new Button("Start Game");
        VBox layout = new VBox(10, startButton);
        layout.setStyle("-fx-alignment: center; -fx-padding: 30;");
        scene = new Scene(layout, 400, 300);

        startButton.setOnAction(e -> sceneManager.startGame());
    }

    public void show() {
        sceneManager.getStage().setScene(scene);
        sceneManager.getStage().setFullScreen(true);
    }
}
