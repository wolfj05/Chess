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

    /**
     * show method
     */
    public void showStartScreen() {
        startScreen.show();
    }

    /**
     * pause the current game (clock timers) and show pause menu
     */
    public void pauseGame() {
        game.getClock().stop();
        pauseScreen.show();
    }

    /**
     * show the rules screen
     */
    public void showRules(){
        rules.show();
    }

    /**
     * continue the game after pause
     */
    public void continueGame() {
        if (gameScreen != null) {
            game.getClock().start();
            gameScreen.show();
        } else {
            showStartScreen();
        }
    }

    /**
     * start a new game
     */
    public void startGame() {
        game.startGame();
        this.gameScreen = new GameScreen(this);
        gameScreen.show();
    }

    /**
     * exit the game and close application
     */
    public void exitGame() {
        javafx.application.Platform.exit();
        System.exit(0);
    }

    /** 
     * @return current Stage
     */
    public Stage getStage() {
        return stage;
    }

    /** 
     * @return current Game
     */
    public Game getGame() {
        return game;
    }

    /**
     * restart the game
     */
    public void restartGame() {
        game.restartGame();
        this.gameScreen = new GameScreen(this);
        gameScreen.show();
    }

    /** 
     * set the names of each player
     * @param whiteName
     * @param blackName
     */
    // Setter: StartScreen ruft das auf
    public void setPlayerNames(String whiteName, String blackName) {
        this.whitePlayerName = whiteName;
        this.blackPlayerName = blackName;
    }

    /** 
     * @return name of white player
     */
    public String getWhitePlayerName() {
        return whitePlayerName;
    }

    /** 
     * @return name of black player
     */
    public String getBlackPlayerName() {
        return blackPlayerName;
    }
}

