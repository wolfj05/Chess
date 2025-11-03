package at.ac.hcw.chess;

import at.ac.hcw.chess.scenes.MainMenu;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        primaryStage.setTitle("Schach");
        showStartScreen();
        primaryStage.show();
    }

    public void showStartScreen() {
        MainMenu startScreen = new MainMenu(this);
        Scene scene = new Scene(startScreen.getView(), 800, 600);
        primaryStage.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}