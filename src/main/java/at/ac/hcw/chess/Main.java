package at.ac.hcw.chess;

import at.ac.hcw.chess.gameutils.Game;
import at.ac.hcw.chess.scenes.SceneManager;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.InputStream;

import java.util.Objects;

public class Main extends Application {

    /** 
     * start method javafx
     * @param primaryStage primary Stage
     */
    @Override
    public void start(Stage primaryStage) {
        Game game = new Game();
        SceneManager sceneManager = new SceneManager(primaryStage, game);

        sceneManager.showStartScreen();

        primaryStage.setTitle("Chess");

        //  Taskleisten- & Fenster-Icon setzen
        setAppIcon(primaryStage);

        primaryStage.show();
    }

    /** 
     * set the App icon to king piece
     * @param stage
     */
    private void setAppIcon(Stage stage) {
        String iconPath = "/at/ac/hcw/chess/scenes/icon/black_king.png";

        InputStream iconStream = getClass().getResourceAsStream(iconPath);

        // falls ich den Error gleich verstehen möchte. Netter Vorschlag aber nicht nötig! Nur wenn ich den Pfad nicht richtig gesetzt habe!
//        if (iconStream == null) {
//            System.err.println("Icon nicht gefunden: " + iconPath);
//            return;
//        }

        assert iconStream != null;
        stage.getIcons().add(new Image(iconStream));
    }

    /** 
     * main
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
