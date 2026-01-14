package at.ac.hcw.chess.scenes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.Random;

public class StartScreen {

    private final SceneManager sceneManager;
    private Scene scene;

    private boolean player1IsWhite = true;

    public StartScreen(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        createScene();
    }

    private void createScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("start_screen.fxml"));
            Parent root = loader.load();

            TextField p1Field = (TextField) root.lookup("#player1Field");
            TextField p2Field = (TextField) root.lookup("#player2Field");

            ImageView whiteIcon = (ImageView) root.lookup("#whitePieceIcon");
            ImageView blackIcon = (ImageView) root.lookup("#blackPieceIcon");

            Button randomButton = (Button) root.lookup("#randomButton");
            ToggleButton muteToggle = (ToggleButton) root.lookup("#muteToggle");

            Button startButton = (Button) root.lookup("#startButton");
            Button exitButton = (Button) root.lookup("#exitButton");

            if (p1Field == null || p2Field == null
                    || randomButton == null || muteToggle == null
                    || startButton == null || exitButton == null) {
                throw new RuntimeException("StartScreen: FXML Elemente fehlen (fx:id stimmt nicht).");
            }

            // Icons laden (falls vorhanden)
            if (whiteIcon != null) {
                whiteIcon.setImage(new Image(getClass().getResourceAsStream("/white_pawn.png")));
            }
            if (blackIcon != null) {
                blackIcon.setImage(new Image(getClass().getResourceAsStream("/black_pawn.png")));
            }

            randomButton.setOnAction(e -> {
                player1IsWhite = new Random().nextBoolean();
                System.out.println("Random colors -> player1IsWhite = " + player1IsWhite);
            });

            muteToggle.setOnAction(e -> System.out.println("Mute: " + muteToggle.isSelected()));

            startButton.setOnAction(e -> {
                String name1 = p1Field.getText().trim();
                String name2 = p2Field.getText().trim();

                if (name1.isEmpty() || name2.isEmpty()) {
                    showAlert("Bitte beide Namen eingeben.");
                    return;
                }
                if (name1.equalsIgnoreCase(name2)) {
                    showAlert("Bitte zwei unterschiedliche Namen verwenden.");
                    return;
                }

                String white = player1IsWhite ? name1 : name2;
                String black = player1IsWhite ? name2 : name1;

                System.out.println("StartScreen computed -> WHITE=" + white + " BLACK=" + black);

                // Namen wirklich speichern
                sceneManager.setPlayerNames(white, black);

                // Kontroll-Log direkt danach
                System.out.println("SceneManager now has -> WHITE=" + sceneManager.getWhitePlayerName()
                        + " BLACK=" + sceneManager.getBlackPlayerName());

                sceneManager.startGame();
            });


            exitButton.setOnAction(e -> sceneManager.exitGame());

            scene = new Scene(root, 1200, 720);

        } catch (IOException | NullPointerException e) {
            throw new RuntimeException("Could not load start_screen.fxml", e);
        }
    }

    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    public void show() {
        sceneManager.getStage().setScene(scene);
        sceneManager.getStage().setFullScreenExitHint("");
        sceneManager.getStage().setFullScreenExitKeyCombination(javafx.scene.input.KeyCombination.NO_MATCH);
        sceneManager.getStage().setFullScreen(true);
        sceneManager.getStage().setResizable(false);
    }
}
