package at.ac.hcw.chess.scenes;

import at.ac.hcw.chess.gameutils.Board;
import at.ac.hcw.chess.gameutils.Game;
import at.ac.hcw.chess.gameutils.Square;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.Objects;

public class GameScreen {

    private Scene scene;
    private final SceneManager sceneManager;
    private Timeline timeline;
    private Game game;

    public GameScreen(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        createScene();
        createTimeline();
    }

    private void createScene() {
        ImageView boardImageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/java/at/ac/hcw/chess/resources/board.png"), "Cannot find chess_board.png in /images/").toExternalForm()));
        boardImageView.setPreserveRatio(true);
        boardImageView.setFitWidth(640);  // adjust to your image
        boardImageView.setFitHeight(640);

        Pane pieceLayer = new Pane();

        StackPane root = new StackPane();
        root.getChildren().addAll(boardImageView, pieceLayer);
        Button backButton = new Button("Back to Menu");
        HBox layout = new HBox(backButton);
        scene = new Scene(layout, 400, 300);

        backButton.setOnAction(e -> sceneManager.showMainMenu());
    }

    private void createTimeline() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1.0 / 30), e -> update()));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    private void update() {

    }

    public void show(Game game) {
        sceneManager.getStage().setScene(scene);
        sceneManager.getStage().setFullScreen(true);
        this.game = game;
        timeline.play();
    }

    public void stopTimeline() {
        timeline.stop();
    }
}