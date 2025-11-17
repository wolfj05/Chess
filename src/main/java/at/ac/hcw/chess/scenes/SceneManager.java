package at.ac.hcw.chess.scenes;

import at.ac.hcw.chess.gameutils.Game;
import javafx.stage.Stage;

public class SceneManager {
    private final Stage stage;
    private final GameScreen gameScreen;
    private final MainMenu mainMenu;
    private final Game game;

    public SceneManager(Stage stage, Game game) {
        this.stage = stage;
        this.game = game;
        gameScreen = new GameScreen(this);
        mainMenu = new MainMenu(this);
    }

    public void showMainMenu() {
        mainMenu.show();
    }

    public void startGame() {
        gameScreen.show();
    }

    public Stage getStage() {
        return stage;
    }

    public Game getGame() {
        return game;
    }
}
