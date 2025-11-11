package at.ac.hcw.chess;

import at.ac.hcw.chess.gameutils.Game;
import at.ac.hcw.chess.scenes.GameScreen;
import at.ac.hcw.chess.scenes.MainMenu;
import at.ac.hcw.chess.scenes.SceneManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        Game game = new Game();
        SceneManager sceneManager = new SceneManager(primaryStage, game);
        sceneManager.showMainMenu(); // start with the main menu
        primaryStage.setTitle("Chess");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}