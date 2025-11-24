package at.ac.hcw.chess.scenes;

import at.ac.hcw.chess.gameutils.*;
import at.ac.hcw.chess.pieces.*;
import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

public class GameScreen {

    private Scene scene;
    private final SceneManager sceneManager;
    private final Game game;

    private GridPane squareGrid;
    private StackPane gameView;
    private StackPane gameOverOverlay = null;

    private double squareSize;
    private Piece selectedPiece = null;
    private List<Move> legalMovesForSelected = new ArrayList<>();
    private List<StackPane> blueHighlights = new ArrayList<>();
    private StackPane redKingSquare = null;

    private Clock clock;
    private Label whiteClockLabel;
    private Label blackClockLabel;

    public GameScreen(SceneManager sceneManager) {
        this.game = sceneManager.getGame();
        this.sceneManager = sceneManager;
        createScene();
    }

    private void createScene() {
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        Image boardImage = new Image("/board.png");
        ImageView boardView = new ImageView(boardImage);
        boardView.setPreserveRatio(true);
        boardView.setFitHeight(screenHeight*.9); // schöne Größe
        boardView.setSmooth(true);

        // --- GameView StackPane ---
        gameView = new StackPane();
        gameView.setAlignment(Pos.CENTER);
        gameView.getChildren().add(boardView);

        // --- GridPane für klickbare Squares ---
        squareGrid = new GridPane();
        gameView.getChildren().add(squareGrid);

        // --- Berechne Square-Größe basierend auf Board ---
        boardView.boundsInParentProperty().addListener((obs, oldBounds, newBounds) -> {
            if (newBounds.getWidth() == 0 || newBounds.getHeight() == 0) return;

            double borderRatio = 0.05;
            double totalSize = boardView.getFitHeight();
            double innerSize = totalSize * (1 - 2 * borderRatio);
            squareSize = innerSize / 8.0;

            // GridPane exakt auf Board
            squareGrid.setPrefSize(innerSize, innerSize);
            squareGrid.setMinSize(innerSize, innerSize);
            squareGrid.setMaxSize(innerSize, innerSize);

            // Squares nur initial erstellen
            if (squareGrid.getChildren().isEmpty()) {
                for (int x = 0; x < 8; x++) {
                    for (int y = 0; y < 8; y++) {
                        StackPane square = new StackPane();
                        square.setPrefSize(squareSize, squareSize);
                        square.setMinSize(squareSize, squareSize);
                        square.setMaxSize(squareSize, squareSize);
                        final int fx = x;
                        final int fy = y;
                        square.setOnMouseClicked(e -> handleSquareClick(fx, fy));
                        squareGrid.add(square, x, 7 - y); // invertierte Y-Achse
                    }
                }
            }

            updatePieces();
            highlightKingInCheck(game.getCurrentTurn());
        });

        // --- Sidebar & Buttons ---
        Button backButton = new Button("Back to Menu");
        backButton.setStyle("""
                -fx-background-color: #5b4636;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-background-radius: 10;
                -fx-padding: 6 12 6 12;
                """);

        VBox sidebar = new VBox(15);
        sidebar.setPadding(new Insets(30));
        sidebar.setAlignment(Pos.TOP_CENTER);
        sidebar.setPrefWidth(425);

        Button restartButton = new Button("Restart Game");
        restartButton.setStyle("""
                -fx-background-color: #5b4636;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-background-radius: 10;
                -fx-padding: 10 20 10 20;
                """);
        restartButton.setOnAction(e -> {
            sceneManager.restartGame();
        });

        Button settingsButton = new Button("Settings");
        settingsButton.setStyle("""
                -fx-background-color: #5b4636;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-background-radius: 10;
                -fx-padding: 10 20 10 20;
                """);

        Button resignButton = new Button("Resign");
        resignButton.setStyle("""
    -fx-background-color: #5b4636;
    -fx-text-fill: white;
    -fx-font-weight: bold;
    -fx-background-radius: 10;
    -fx-padding: 10 20 10 20;
""");

        resignButton.setOnAction(e -> onResign());

        sidebar.getChildren().addAll(restartButton, settingsButton, resignButton);

        clock = new Clock(10 * 60 * 1000); // 10 Minuten
        clock.start();

        whiteClockLabel = new Label("10:00");
        whiteClockLabel.setStyle("-fx-font-size: 40px; -fx-text-fill: white; -fx-font-weight: bold;");

        blackClockLabel = new Label("10:00");
        blackClockLabel.setStyle("-fx-font-size: 40px; -fx-text-fill: white; -fx-font-weight: bold;");

        sidebar.getChildren().addAll(whiteClockLabel, blackClockLabel);

        StackPane.setAlignment(backButton, Pos.TOP_RIGHT);
        StackPane.setMargin(backButton, new Insets(10, 10, 0, 0));

        HBox layout = new HBox(sidebar, gameView, backButton);
        layout.setStyle("""
        -fx-background-color: linear-gradient(to bottom, #c7a784, #8e735b);
        """);

        StackPane root = new StackPane();
        root.getChildren().add(layout);

        scene = new Scene(root, 1920, 1080);

        backButton.setOnAction(e -> sceneManager.showMainMenu());

        startClockUIUpdate();
    }

    private void updatePieces() {
        // Alle bisherigen Pieces aus Squares entfernen
        for (Node n : squareGrid.getChildren()) {
            StackPane square = (StackPane) n;
            square.getChildren().removeIf(node -> node instanceof ImageView);
        }

        // Pieces in Squares einfügen und zentrieren
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Square sq = game.getBoard().getSquare(x, y);
                Piece p = sq.getPiece();
                if (p != null) {
                    p.setSquare(sq);
                    ImageView pieceView = new ImageView(new Image(p.getSrc()));
                    pieceView.setFitWidth(squareSize);
                    pieceView.setFitHeight(squareSize);

                    StackPane square = getSquareNode(x, y);
                    if (square != null) {
                        square.getChildren().add(pieceView);
                        StackPane.setAlignment(pieceView, Pos.CENTER); // zentrieren
                        p.setImageView(pieceView);
                    }
                }
            }
        }
    }

    private void handleSquareClick(int x, int y) {
        Square square = game.getBoard().getSquare(x, y);
        Piece piece = square.getPiece();

        // Auswahl einer Figur
        if (selectedPiece == null) {
            if (piece != null && piece.getPlayer() == game.getCurrentTurn()) {
                selectPiece(piece);
            }
            return;
        }

        // Move ausführen
        boolean isValidMove = false;
        for (Move m : legalMovesForSelected) {
            if (m.getToX() == x && m.getToY() == y) {
                if(m.isPromotion()){
                    showPromotionPopup(selectedPiece, m);
                } else {
                    game.getBoard().makeMove(m);
                    updatePieces();
                    clearBlueHighlights();
                    selectedPiece = null;
                    game.switchTurn();
                    clock.switchTurn();
                    highlightKingInCheck(game.getCurrentTurn());
                    isValidMove = true;
                }

                if (game.getBoard().isCheckmate(game.getCurrentTurn())) {
                    String winner = (game.getCurrentTurn() == game.getPlayers()[0])
                            ? "Black wins!"
                            : "White wins!";
                    showGameOverOverlay("Checkmate!\n" + winner);
                } else if (game.getBoard().isStalemate(game.getCurrentTurn())) {
                    showGameOverOverlay("Stalemate!\nDraw");
                }
                break;
            }
        }

        // Neue Auswahl eigener Figur
        if (!isValidMove && piece != null && piece.getPlayer() == game.getCurrentTurn()) {
            clearBlueHighlights();
            selectPiece(piece);
        }
    }

    private void selectPiece(Piece piece) {
        selectedPiece = piece;

        // Alle möglichen Moves berechnen
        List<Move> moves = piece.calcValidMoves(
                game.getBoard(),
                piece.getSquare().getRow(),
                piece.getSquare().getCol()
        );

        // Moves filtern: König darf nicht ins Schach gesetzt werden
        legalMovesForSelected.clear();
        for (Move m : moves) {
            Board copy = game.getBoard().deepCopy();
            copy.makeMove(m);
            if (!copy.isInCheck(piece.getPlayer())) {
                legalMovesForSelected.add(m);
            }
        }

        highlightLegalMoves();
        highlightKingInCheck(game.getCurrentTurn());
    }

    private void highlightLegalMoves() {
        clearBlueHighlights();
        for (Move move : legalMovesForSelected) {
            StackPane square = getSquareNode(move.getToX(), move.getToY());
            if (square != null) {
                square.setBackground(new Background(
                        new BackgroundFill(Color.rgb(0, 0, 255, 0.35), null, null)
                ));
                blueHighlights.add(square);
            }
        }
    }

    private void highlightKingInCheck(Player player) {
        // alten roten Rahmen entfernen
        if (redKingSquare != null) {
            redKingSquare.setBorder(null);
            redKingSquare = null;
        }

        if (game.getBoard().isInCheck(player)) {
            Square kingSquare = game.getBoard().findKing(player);
            if (kingSquare != null) {
                StackPane square = getSquareNode(kingSquare.getRow(), kingSquare.getCol());
                if (square != null) {
                    square.setBorder(new Border(new BorderStroke(
                            Color.RED,
                            BorderStrokeStyle.SOLID,
                            CornerRadii.EMPTY,
                            new BorderWidths(3)
                    )));
                    redKingSquare = square;
                }
            }
        }
    }

    private void clearBlueHighlights() {
        for (StackPane sq : blueHighlights) {
            sq.setBackground(null);
        }
        blueHighlights.clear();
    }

    private StackPane getSquareNode(int x, int y) {
        for (Node n : squareGrid.getChildren()) {
            Integer col = GridPane.getColumnIndex(n);
            Integer row = GridPane.getRowIndex(n);
            if (col != null && row != null && col == x && row == (7 - y)) {
                return (StackPane) n;
            }
        }
        return null;
    }

    public void show() {
        sceneManager.getStage().setScene(scene);
        sceneManager.getStage().setFullScreenExitHint("");
        sceneManager.getStage().setFullScreenExitKeyCombination(javafx.scene.input.KeyCombination.NO_MATCH);
        sceneManager.getStage().setFullScreen(true);
        sceneManager.getStage().setResizable(false);
        sceneManager.getStage().setTitle("Chess");
    }

    private void showGameOverOverlay(String message) {
        clock.stop();
        if (gameOverOverlay != null) return;

        gameOverOverlay = new StackPane();
        gameOverOverlay.setStyle("""
        -fx-background-color: rgba(0, 0, 0, 0.65);
    """);

        gameOverOverlay.setPrefSize(
                scene.getWidth(),
                scene.getHeight()
        );

        VBox box = new VBox(20);
        box.setAlignment(Pos.CENTER);

        javafx.scene.control.Label label = new javafx.scene.control.Label(message);
        label.setStyle("""
        -fx-text-fill: white;
        -fx-font-size: 64px;
        -fx-font-weight: bold;
    """);

        Button restart = new Button("Restart Game");
        restart.setStyle("""
        -fx-background-color: #ffffff;
        -fx-text-fill: black;
        -fx-font-size: 24px;
        -fx-padding: 12 24 12 24;
        -fx-background-radius: 12;
    """);
        restart.setOnAction(e -> {
            sceneManager.restartGame();
        });

        Button back = new Button("Back to Menu");
        back.setStyle("""
        -fx-background-color: #ffffff;
        -fx-text-fill: black;
        -fx-font-size: 24px;
        -fx-padding: 12 24 12 24;
        -fx-background-radius: 12;
    """);
        back.setOnAction(e -> sceneManager.showMainMenu());

        box.getChildren().addAll(label, restart, back);

        gameOverOverlay.getChildren().add(box);

        // 🎯 Auf oberster Ebene einfügen
        StackPane root = (StackPane) scene.getRoot();
        root.getChildren().add(gameOverOverlay);
    }

    private void hideGameOverOverlay() {
        if (gameOverOverlay != null) {
            gameView.getChildren().remove(gameOverOverlay);
            gameOverOverlay = null;
        }
    }

    private void startClockUIUpdate() {
        AnimationTimer uiTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                whiteClockLabel.setText(clock.format(clock.getWhiteTime()));
                blackClockLabel.setText(clock.format(clock.getBlackTime()));

                if (clock.getWhiteTime() <= 0) {
                    showGameOverOverlay("Black wins (White ran out of time)");
                    stop();
                }

                if (clock.getBlackTime() <= 0) {
                    showGameOverOverlay("White wins (Black ran out of time)");
                    stop();
                }
            }
        };
        uiTimer.start();
    }

    private void showPromotionPopup(Piece pawn, Move move) {
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.6);");

        VBox box = new VBox(20);
        box.setAlignment(Pos.CENTER);
        box.setStyle("""
        -fx-background-color: #f0e6d2;
        -fx-padding: 20;
        -fx-background-radius: 20;
        -fx-border-radius: 20;
        -fx-border-color: #5b4636;
        -fx-border-width: 4;
    """);

        Label text = new Label("Choose promotion piece:");
        text.setStyle("-fx-font-size: 28px; -fx-text-fill: black; -fx-font-weight: bold;");

        HBox buttons = new HBox(20);
        buttons.setAlignment(Pos.CENTER);

        Button queen = new Button("Queen");
        Button rook = new Button("Rook");
        Button bishop = new Button("Bishop");
        Button knight = new Button("Knight");

        queen.setOnAction(e -> finishPromotion(move, "queen"));
        rook.setOnAction(e -> finishPromotion(move, "rook"));
        bishop.setOnAction(e -> finishPromotion(move, "bishop"));
        knight.setOnAction(e -> finishPromotion(move, "knight"));

        buttons.getChildren().addAll(queen, rook, bishop, knight);
        box.getChildren().addAll(text, buttons);

        overlay.getChildren().add(box);

        gameView.getChildren().add(overlay);
    }

    private void finishPromotion(Move move, String type) {
        Board board = game.getBoard();

        // Temporär Zug ausführen
        board.makeMove(move);

        // Figur ersetzen
        Player player = move.getMovedPiece().getPlayer();
        int x = move.getToX();
        int y = move.getToY();

        switch (type) {
            case "queen" -> board.getSquare(x, y).setPiece(new Queen(player, player.getColor().equals("White") ? "/white_queen.png" : "/black_queen.png"));
            case "rook" -> board.getSquare(x, y).setPiece(new Rook(player, player.getColor().equals("White") ? "/white_rook.png" : "/black_rook.png"));
            case "bishop" -> board.getSquare(x, y).setPiece(new Bishop(player, player.getColor().equals("White") ? "/white_bishop.png" : "/black_bishop.png"));
            case "knight" -> board.getSquare(x, y).setPiece(new Knight(player, player.getColor().equals("White") ? "/white_knight.png" : "/black_knight.png"));
        }

        updatePieces();

        // Overlay entfernen
        gameView.getChildren().remove(gameView.getChildren().size() - 1);

        // Restliches Spiel fortsetzen
        selectedPiece = null;
        clearBlueHighlights();
        game.switchTurn();
        clock.switchTurn();
        highlightKingInCheck(game.getCurrentTurn());
    }

    private void onResign() {
        Player loser = game.getCurrentTurn();
        Player winner = (game.getPlayers()[0] == loser
                ? game.getPlayers()[1]
                : game.getPlayers()[0]);

        showGameOverOverlay(winner.getColor() + " wins by resignation!");
    }
}
