package at.ac.hcw.chess.scenes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class RulesScreen {

    private final SceneManager sceneManager;
    private Scene scene;

    public RulesScreen(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        createScene();
    }

    /**
     * Create the Scene
     */
    private void createScene() {

        // ---------- TITLE ----------
        Label title = new Label("Chess Rules");
        title.setStyle("""
            -fx-font-size: 42px;
            -fx-font-weight: bold;
            -fx-text-fill: #2b1d0e;
        """);

        // ---------- RULE TEXT ----------
        VBox rulesBox = new VBox(18);
        rulesBox.setPadding(new Insets(30));
        rulesBox.setMaxWidth(900);

        rulesBox.getChildren().addAll(
                rule("Objective",
                        "The goal of chess is to checkmate your opponent’s king. "
                                + "Checkmate occurs when the king is in check and cannot escape."),

                rule("Basic Moves",
                        """
                        Pawn: Moves forward one square (two on first move), captures diagonally.
                        Rook: Moves any number of squares horizontally or vertically.
                        Knight: Moves in an L-shape (2+1).
                        Bishop: Moves diagonally.
                        Queen: Moves like rook and bishop combined.
                        King: Moves one square in any direction.
                        """),

                rule("Check",
                        "A king is in check when it is attacked by an opponent’s piece. "
                                + "The player must remove the check immediately."),

                rule("Checkmate",
                        "Checkmate occurs when the king is in check and no legal move can remove it. "
                                + "The game ends immediately."),

                rule("Stalemate",
                        "Stalemate occurs when the current player has no legal moves but is not in check. "
                                + "The game ends in a draw."),

                rule("Castling",
                        """
                        - King and rook must not have moved before
                        - No pieces between them
                        - King may not castle out of, through, or into check
                        """),

                rule("En Passant",
                        "A pawn that advances two squares can be captured as if it had moved one square, "
                                + "but only immediately on the next move."),

                rule("Promotion",
                        "When a pawn reaches the last rank, it must be promoted to a Queen, Rook, Bishop, or Knight."),

                rule("Illegal Moves",
                        "You may not make a move that leaves your own king in check."),

                rule("Resignation & Time",
                        """
                        A player may resign at any time.
                        If a player's clock reaches zero, that player loses the game.
                        """)
        );

        // ---------- SCROLL ----------
        VBox layout = getRules(rulesBox, title);

        StackPane root = new StackPane(layout);
        root.setStyle("""
            -fx-background-color: linear-gradient(to bottom, #c7a784, #8e735b);
        """);

        scene = new Scene(root);
    }

    /** 
     * Formatting for the rule box
     * @param rulesBox rule box
     * @param title title
     * @return VBox
     */
    private VBox getRules(VBox rulesBox, Label title) {
        ScrollPane scrollPane = new ScrollPane(rulesBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("""
            -fx-background: transparent;
            -fx-background-color: transparent;
        """);

        // ---------- BACK BUTTON ----------
        Button back = new Button("Back");
        back.setStyle("""
            -fx-background-color: #5b4636;
            -fx-text-fill: white;
            -fx-font-size: 18px;
            -fx-padding: 10 18;
            -fx-background-radius: 10;
        """);
        back.setOnAction(e -> sceneManager.showStartScreen());

        // ---------- MAIN LAYOUT ----------
        VBox layout = new VBox(25, title, scrollPane, back);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(30));
        return layout;
    }

    /** 
     * Formatting of one rule
     * @param title Title
     * @param text Text
     * @return VBox
     */
    private VBox rule(String title, String text) {
        Label t = new Label(title);
        t.setStyle("""
            -fx-font-size: 26px;
            -fx-font-weight: bold;
            -fx-text-fill: #2b1d0e;
        """);

        Label body = new Label(text);
        body.setWrapText(true);
        body.setStyle("""
            -fx-font-size: 18px;
            -fx-text-fill: #1e1e1e;
        """);

        return new VBox(6, t, body);
    }

    /**
     * show method
     */
    public void show() {
        sceneManager.getStage().setScene(scene);
        sceneManager.getStage().setTitle("Chess Rules");
        sceneManager.getStage().setFullScreenExitKeyCombination(javafx.scene.input.KeyCombination.NO_MATCH);
        sceneManager.getStage().setFullScreen(true);
        sceneManager.getStage().setResizable(false);
    }
}
