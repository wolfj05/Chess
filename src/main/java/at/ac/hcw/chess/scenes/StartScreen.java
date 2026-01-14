package at.ac.hcw.chess.scenes;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.Objects;
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

            ImageView player1Icon = (ImageView) root.lookup("#whitePieceIcon");
            ImageView player2Icon = (ImageView) root.lookup("#blackPieceIcon");

            Button switchButton = (Button) root.lookup("#switchButton");
            Button randomButton = (Button) root.lookup("#randomButton");
            Button rulesButton = (Button) root.lookup("#rulesButton");
            ToggleButton muteToggle = (ToggleButton) root.lookup("#muteToggle");

            Button startButton = (Button) root.lookup("#startButton");
            Button exitButton = (Button) root.lookup("#exitButton");

            Button tenMin = (Button) root.lookup("#tenMin");
            Button fiveMin = (Button) root.lookup("#fiveMin");
            Button threeMin = (Button) root.lookup("#threeMin");

            if (p1Field == null || p2Field == null
                    || randomButton == null || muteToggle == null
                    || startButton == null || exitButton == null) {
                throw new RuntimeException("StartScreen: FXML Elemente fehlen (fx:id stimmt nicht).");
            }

            // Icons laden (falls vorhanden)
            if (player1Icon != null) {
                player1Icon.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/white_pawn.png"))));
            }
            if (player2Icon != null) {
                player2Icon.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/black_pawn.png"))));
            }

            randomButton.setOnAction(e -> {
                player1IsWhite = new Random().nextBoolean();
                assert player1Icon != null;
                if(player1IsWhite){
                    player1Icon.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/white_pawn.png"))));
                    assert player2Icon != null;
                    player2Icon.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/black_pawn.png"))));
                } else {
                    player1Icon.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/black_pawn.png"))));
                    assert player2Icon != null;
                    player2Icon.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/white_pawn.png"))));
                }
            });

            switchButton.setOnAction(e -> {
                player1IsWhite = !player1IsWhite;
                assert player1Icon != null;
                if(player1IsWhite){
                    player1Icon.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/white_pawn.png"))));
                    assert player2Icon != null;
                    player2Icon.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/black_pawn.png"))));
                } else {
                    player1Icon.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/black_pawn.png"))));
                    assert player2Icon != null;
                    player2Icon.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/white_pawn.png"))));
                }
            });

            rulesButton.setOnAction(e -> sceneManager.showRules());

            muteToggle.setOnAction(e -> {
                sceneManager.getGame().setMute(muteToggle.isSelected());
                if(muteToggle.isSelected()){
                    muteToggle.setText("Unmute");
                } else {
                    muteToggle.setText("Mute");
                }
            });

            tenMin.setOnAction(e -> {
                sceneManager.getGame().setStartTime(10);
                threeMin.getStyleClass().remove("selected");
                fiveMin.getStyleClass().remove("selected");
                tenMin.getStyleClass().add("selected");
            });
            fiveMin.setOnAction(e -> {
                sceneManager.getGame().setStartTime(5);
                threeMin.getStyleClass().remove("selected");
                fiveMin.getStyleClass().add("selected");
                tenMin.getStyleClass().remove("selected");
            });
            threeMin.setOnAction(e -> {
                sceneManager.getGame().setStartTime(3);
                threeMin.getStyleClass().add("selected");
                fiveMin.getStyleClass().remove("selected");
                tenMin.getStyleClass().remove("selected");
            });

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

                // Namen wirklich speichern
                sceneManager.setPlayerNames(white, black);

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
