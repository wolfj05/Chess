package at.ac.hcw.chess.scenes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.IOException;

public class PauseScreen {

    private final SceneManager sceneManager;
    private Scene scene;

    public PauseScreen(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        createScene();
    }

    private void createScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("pause.fxml"));
            Parent root = loader.load();

            Button continueButton = (Button) root.lookup("#continueButton");
            Button menuButton = (Button) root.lookup("#menuButton");
            Button restartButton = (Button) root.lookup("#restartButton");

            if (continueButton == null || menuButton == null || restartButton == null) {
                throw new RuntimeException("MainMenu: FXML Elemente fehlen (fx:id stimmt nicht).");
            }

            // CONTINUE = zurück ins aktuelle Spiel (ohne Reset)
            continueButton.setOnAction(e -> sceneManager.continueGame());

            // EXIT = Programm schließen
            menuButton.setOnAction(e -> sceneManager.showStartScreen());

            // RESTART = zurück zum StartScreen (Setup neu)
            restartButton.setOnAction(e -> sceneManager.restartGame());

            scene = new Scene(root, 700, 450);

        } catch (IOException | NullPointerException e) {
            throw new RuntimeException("Could not load pause.fxml", e);
        }
    }

    public void show() {
        sceneManager.getStage().setScene(scene);
        sceneManager.getStage().setFullScreenExitHint("");
        sceneManager.getStage().setFullScreenExitKeyCombination(javafx.scene.input.KeyCombination.NO_MATCH);
        sceneManager.getStage().setFullScreen(true);
        sceneManager.getStage().setResizable(false);
    }
}
