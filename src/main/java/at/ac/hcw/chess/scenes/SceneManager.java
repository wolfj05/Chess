package at.ac.hcw.chess.scenes;

import javafx.stage.Stage;

public class SceneManager {
    private final Stage stage;
    private GameScreen gameScreen;
    private MainMenu mainMenu;

    public SceneManager(Stage stage) {
        this.stage = stage;
        gameScreen = new GameScreen(stage);
        mainMenu = new MainMenu(stage);
    }

    public void showMainMenu() {
        gameScreen.stopTimeline();
        mainMenu.show();
    }

    public void showGame() {
        gameScreen.show(); // timeline starts automatically
    }
}
