package at.ac.hcw.chess.scenes;

import at.ac.hcw.chess.gameutils.Game;
import javafx.stage.Stage;

public class SceneManager {
    private final Stage stage;
    private GameScreen gameScreen;
    private final PauseScreen pauseScreen;
    private final RulesScreen rules;
    private final Game game;
    private final StartScreen startScreen;

    private String whitePlayerName = "White";       // Spieler-Namen (werden im StartScreen gesetzt)
    private String blackPlayerName = "Black";

    public SceneManager(Stage stage, Game game) {
        this.stage = stage;
        this.game = game;
        startScreen = new StartScreen(this);
        gameScreen = new GameScreen(this);
        pauseScreen = new PauseScreen(this);
        rules = new RulesScreen(this);
    }

    public void showStartScreen() {
        startScreen.show();
    }

    public void pauseGame() {
        game.getClock().stop();
        pauseScreen.show();
    }

    public void showRules(){
        rules.show();
    }

    public void continueGame() {
        if (gameScreen != null) {
            game.getClock().start();
            gameScreen.show();
        } else {
            showStartScreen();
        }
    }

    public void startGame() {
        game.startGame();
        this.gameScreen = new GameScreen(this);
        gameScreen.show();
    }

    public void exitGame() {
        javafx.application.Platform.exit();
        System.exit(0);
    }

    public Stage getStage() {
        return stage;
    }

    public Game getGame() {
        return game;
    }

    public void restartGame() {
        game.restartGame();
        this.gameScreen = new GameScreen(this);
        gameScreen.show();
    }

    // Setter: StartScreen ruft das auf
    public void setPlayerNames(String whiteName, String blackName) {
        this.whitePlayerName = whiteName;
        this.blackPlayerName = blackName;
    }

    // Getter: GameScreen nutzt das
    public String getWhitePlayerName() {
        return whitePlayerName;
    }

    public String getBlackPlayerName() {
        return blackPlayerName;
    }
}

