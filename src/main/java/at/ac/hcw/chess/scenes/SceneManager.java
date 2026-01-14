package at.ac.hcw.chess.scenes;

import at.ac.hcw.chess.gameutils.Game;
import javafx.stage.Stage;

public class SceneManager {
    private final Stage stage;
    private GameScreen gameScreen;
    private final MainMenu mainMenu;
    private final Game game;
    private final StartScreen startScreen;

    private String whitePlayerName = "White";       // Spieler-Namen (werden im StartScreen gesetzt)
    private String blackPlayerName = "Black";

    public SceneManager(Stage stage, Game game) {
        this.stage = stage;
        this.game = game;
        startScreen = new StartScreen(this);
        gameScreen = new GameScreen(this);
        mainMenu = new MainMenu(this);

    }

    public void showStartScreen() {
        startScreen.show();
    }


    public void showMainMenu() {
        mainMenu.show();
    }

    public void continueGame() {
        if (gameScreen != null) {
            gameScreen.show();
        } else {
            // Falls noch kein Spiel existiert: zurück zum StartScreen
            showStartScreen();
        }
    }


    public void startGame() {
        System.out.println("SceneManager.startGame() using -> WHITE=" + whitePlayerName + " BLACK=" + blackPlayerName);

        game.startGame();
        GameScreen gs = new GameScreen(this);
        this.gameScreen = gs;
        gs.show();
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
        GameScreen gs = new GameScreen(this);
        this.gameScreen = gs;
        gs.show();
    }
    // Restart = zurück zum StartScreen (Spieler neu bestimmen)
    public void restartToStartScreen() {
        game.restartGame();     // optional aber sauber: board resetten
        showStartScreen();      // jetzt wieder Namen eingeben
    }

    // Revenge = Partie reset, aber Namen bleiben gleich (direkt ins Spiel)
    public void revengeSamePlayers() {
        game.restartGame();
        GameScreen gs = new GameScreen(this);
        this.gameScreen = gs;
        gs.show();
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

//    // Restart = zurück zum StartScreen (Spieler neu bestimmen)
//    public void restartToStartScreen() {
//        showStartScreen();
//    }
//
//    // Revenge = Partie reset, aber Namen bleiben gleich
//    public void revengeSamePlayers() {
//        game.restartGame();
//        GameScreen gs = new GameScreen(this);
//        this.gameScreen = gs;
//        gs.show();
//    }
}

