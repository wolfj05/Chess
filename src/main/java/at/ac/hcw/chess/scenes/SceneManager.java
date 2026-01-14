package at.ac.hcw.chess.scenes;

import at.ac.hcw.chess.gameutils.Game;
import javafx.stage.Stage;

public class SceneManager {
    private final Stage stage;
    private GameScreen gameScreen;
    private final MainMenu mainMenu;
    private final RulesScreen rules;
    private final Game game;

    public SceneManager(Stage stage, Game game) {
        this.stage = stage;
        this.game = game;
        gameScreen = new GameScreen(this);
        mainMenu = new MainMenu(this);
        rules = new RulesScreen(this);
    }

    public void showMainMenu() {
        mainMenu.show();
    }

    public void showRules(){
        rules.show();
    }

    public void startGame() {
        game.startGame();
        this.gameScreen = new GameScreen(this);
        gameScreen.show();
    }

    public void exitGame(){
        javafx.application.Platform.exit();
        System.exit(0);
    };

    public Stage getStage() {
        return stage;
    }

    public Game getGame() {
        return game;
    }

    public void restartGame(){
        game.restartGame();
        this.gameScreen = new GameScreen(this);
        gameScreen.show();
    }
}
