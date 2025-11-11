package at.ac.hcw.chess.scenes;

import at.ac.hcw.chess.gameutils.Board;
import at.ac.hcw.chess.gameutils.Game;
import at.ac.hcw.chess.gameutils.Player;
import at.ac.hcw.chess.gameutils.Square;
import at.ac.hcw.chess.pieces.Piece;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import java.util.Objects;

public class GameScreen {

    private Scene scene;
    private final SceneManager sceneManager;
    private Timeline timeline;
    private Game game;

    public GameScreen(SceneManager sceneManager) {
        this.game = sceneManager.getGame();
        this.sceneManager = sceneManager;
        createScene();
        createTimeline();
    }

    private void createScene() {
        Image boardImage = new Image("/board.png");
        ImageView boardView = new ImageView(boardImage);
        boardView.setPreserveRatio(true);
        boardView.setFitHeight(1000); // schöne Größe
        boardView.setSmooth(true);
        // z. B. 512 px
        double totalSize = boardView.getFitHeight();

// 🟤 geschätzte Rahmenbreite (z. B. 8 % des Bildes)
        double borderRatio = 0.05;
        double border = totalSize * borderRatio;

// 🟩 effektive Spielfläche (8×8 Felder)
        double innerSize = totalSize - 2 * border;
        double squareSize = innerSize / 8.0;          // 64 px bei 512x512

        Pane pieceLayer = new Pane();
        pieceLayer.setPickOnBounds(false);

// über alle Felder gehen
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Square square = game.getBoard().getSquare(x, y);
                Piece piece = square.getPiece();
                if (piece != null) {
                    ImageView pieceView = new ImageView(new Image(piece.getSrc()));
                    pieceView.setFitWidth(squareSize);
                    pieceView.setFitHeight(squareSize);

                    // Position setzen: x = Spalte, y = Reihe (unten = 0)
                    pieceView.setLayoutX(border + x * squareSize);
                    pieceView.setLayoutY(border + (7 - y) * squareSize);

                    pieceLayer.getChildren().add(pieceView);
                }
            }
        }

        StackPane gameView = new StackPane(boardView, pieceLayer);
        gameView.setPadding(new Insets(45));
        gameView.setAlignment(Pos.CENTER);

        Button backButton = new Button("Back to Menu");
        backButton.setStyle("""
        -fx-background-color: #5b4636;
        -fx-text-fill: white;
        -fx-font-weight: bold;
        -fx-background-radius: 10;
        -fx-padding: 6 12 6 12;
        """);

        VBox sidebar = new VBox(15);
        sidebar.setPadding(new Insets(30));
        sidebar.setAlignment(Pos.TOP_CENTER);
        sidebar.setPrefWidth(425);

        Button restartButton = new Button("Restart Game");
        restartButton.setStyle("""
            -fx-background-color: #5b4636;
            -fx-text-fill: white;
            -fx-font-weight: bold;
            -fx-background-radius: 10;
            -fx-padding: 10 20 10 20;
        """);

        Button settingsButton = new Button("Settings");
        settingsButton.setStyle("""
            -fx-background-color: #5b4636;
            -fx-text-fill: white;
            -fx-font-weight: bold;
            -fx-background-radius: 10;
            -fx-padding: 10 20 10 20;
        """);

        sidebar.getChildren().addAll(restartButton, settingsButton);

        StackPane.setAlignment(backButton, Pos.TOP_RIGHT);
        StackPane.setMargin(backButton, new Insets(10, 10, 0, 0));

        HBox layout = new HBox(sidebar, gameView, backButton);
        layout.setStyle("""
            -fx-background-color: linear-gradient(to bottom, #c7a784, #8e735b);
        """);
        scene = new Scene(layout, 1920, 1080);

        backButton.setOnAction(e -> sceneManager.showMainMenu());
    }

    private void createTimeline() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1.0 / 60), e -> update()));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    private void update() {

    }

    public void show() {
        sceneManager.getStage().setScene(scene);
        sceneManager.getStage().setFullScreen(true);
        sceneManager.getStage().setTitle("Chess");
        timeline.play();
    }

    public void stopTimeline() {
        timeline.stop();
    }
}