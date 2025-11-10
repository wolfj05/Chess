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

    Game game = new Game();
    String currentScene = "main";

    @Override
    public void start(Stage primaryStage) {
        SceneManager sceneManager = new SceneManager(primaryStage);

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.0 / 60), e -> {
            update();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void update() {
        /*
        switch(currentScene){
            case "main": mainMenu.show(); break;
            case "game": gameScreen.show(); break;
        }

         */
    }

    public static void main(String[] args) {
        launch(args);
    }
}