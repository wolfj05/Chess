package at.ac.hcw.chess.scenes;

import at.ac.hcw.chess.Main;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainMenu {
    private VBox root;
    private Main mainApp;

    public MainMenu(Main mainApp){
        this.mainApp = mainApp;
        createLayout();
    }

    private void createLayout() {
        Label title = new Label("Schach");
        Button startButton = new Button("Spiel starten");

        //startButton.setOnAction(e -> mainApp.showGameScreen());

        root = new VBox(20, title, startButton);
        root.setAlignment(Pos.CENTER);
    }

    public Parent getView(){
        return this.root;
    }

}
