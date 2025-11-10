package at.ac.hcw.chess.scenes;

import at.ac.hcw.chess.gameutils.Game;
import javafx.stage.Stage;

public class SceneManager {
    private final Stage stage;
    private final GameScreen gameScreen;
    private final MainMenu mainMenu;

    public SceneManager(Stage stage) {
        this.stage = stage;
        gameScreen = new GameScreen(this);
        mainMenu = new MainMenu(this);
    }

    public void showMainMenu() {
        gameScreen.stopTimeline();  // stop updates if leaving game
        mainMenu.show();
    }

    public void startGame() {
        Game game = new Game();
        gameScreen.show(game);       // starts timeline automatically
    }

    public Stage getStage() {
        return stage;
    }
}
