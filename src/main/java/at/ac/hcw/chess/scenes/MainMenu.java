package at.ac.hcw.chess.scenes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.IOException;

public class MainMenu {

    private final SceneManager sceneManager;
    private Scene scene;

    public MainMenu(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        createScene();
    }

    private void createScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("main_menu.fxml"));
            Parent root = loader.load();

            Button continueButton = (Button) root.lookup("#continueButton");
            Button exitButton = (Button) root.lookup("#exitButton");
            Button restartButton = (Button) root.lookup("#restartButton");
            Button revangeButton = (Button) root.lookup("#revangeButton");

            if (continueButton == null || exitButton == null || restartButton == null || revangeButton == null) {
                throw new RuntimeException("MainMenu: FXML Elemente fehlen (fx:id stimmt nicht).");
            }

            // CONTINUE = zurück ins aktuelle Spiel (ohne Reset)
            continueButton.setOnAction(e -> sceneManager.continueGame());

            // EXIT = Programm schließen
            exitButton.setOnAction(e -> sceneManager.exitGame());

            // RESTART = zurück zum StartScreen (Setup neu)
            restartButton.setOnAction(e -> sceneManager.restartToStartScreen());

            // REVANGE = neue Runde direkt (ohne Setup)
            revangeButton.setOnAction(e -> sceneManager.revengeSamePlayers());

            scene = new Scene(root, 700, 450);

        } catch (IOException | NullPointerException e) {
            throw new RuntimeException("Could not load main_menu.fxml", e);
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
