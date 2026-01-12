package at.ac.hcw.chess.scenes;

import at.ac.hcw.chess.gameutils.*;
import at.ac.hcw.chess.pieces.Piece;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.fxml.FXMLLoader;


import java.util.ArrayList;
import java.util.List;

public class GameScreen {

    private Scene scene;
    private final SceneManager sceneManager;
    private final Game game;
    private String whiteName;
    private String blackName;


    private GridPane squareGrid;
    private StackPane gameView;
    private StackPane gameOverOverlay = null;

    private double squareSize;
    private Piece selectedPiece = null;
    private final List<Move> legalMovesForSelected = new ArrayList<>();
    private final List<StackPane> blueHighlights = new ArrayList<>();
    private StackPane redKingSquare = null;

    public GameScreen(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        this.game = sceneManager.getGame();
        this.whiteName = sceneManager.getWhitePlayerName();
        this.blackName = sceneManager.getBlackPlayerName();

        createScene();
    }

    private void createScene() {

        String whiteName = sceneManager.getWhitePlayerName();                // Namen aus SceneManager holen
        String blackName = sceneManager.getBlackPlayerName();

        Label blackPlayerLabel = new Label(blackName);                       // Labels (BLACK oben links im freien Bereich, WHITE unten links)
        blackPlayerLabel.setStyle("""
                -fx-font-size: 26px;
                -fx-font-weight: bold;
                -fx-text-fill: #1a1a1a;
                """);

        Label whitePlayerLabel = new Label(whiteName);
        whitePlayerLabel.setStyle("""
                -fx-font-size: 26px;
                -fx-font-weight: bold;
                -fx-text-fill: #ffffff;
                """);

        double screenHeight = Screen.getPrimary().getBounds().getHeight();

        Image boardImage = new Image("/board.png");
        ImageView boardView = new ImageView(boardImage);
        boardView.setPreserveRatio(true);
        boardView.setFitHeight(screenHeight * .9);
        boardView.setSmooth(true);

        // --- GameView StackPane ---
        gameView = new StackPane();
        gameView.setAlignment(Pos.CENTER);
        gameView.getChildren().add(boardView);

        // --- GridPane für klickbare Squares ---
        squareGrid = new GridPane();
        gameView.getChildren().add(squareGrid);

        // --- Sidebar links: oben BLACK, unten WHITE ---
        VBox sidebar = new VBox();
        sidebar.setPadding(new Insets(12, 12, 12, 12));      // weniger Abstand
        sidebar.setAlignment(Pos.TOP_RIGHT);                                // Inhalt rechtsbündig Richtung Brett
        sidebar.setMinWidth(180);
        sidebar.setMaxWidth(180);
        sidebar.setPrefWidth(180);

// VBox darf NICHT die ganze StackPane-Breite nehmen
        sidebar.setFillWidth(false);


        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        sidebar.getChildren().addAll(
                blackPlayerLabel,
                spacer,
                whitePlayerLabel
        );


        // Berechne Square-Größe basierend auf Board
        boardView.boundsInParentProperty().addListener((obs, oldBounds, newBounds) -> {
            if (newBounds.getWidth() == 0 || newBounds.getHeight() == 0) return;

            double borderRatio = 0.05;
            double totalSize = boardView.getFitHeight();
            double innerSize = totalSize * (1 - 2 * borderRatio);
            squareSize = innerSize / 8.0;

            squareGrid.setPrefSize(innerSize, innerSize);
            squareGrid.setMinSize(innerSize, innerSize);
            squareGrid.setMaxSize(innerSize, innerSize);

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
                        squareGrid.add(square, x, 7 - y);
                    }
                }
            }

            updatePieces();
            highlightKingInCheck(game.getCurrentTurn());
            // Sidebar links neben das Brett setzen (z.B. 30px Abstand)

            double gap = 30;
            double boardHalf = newBounds.getWidth() / 2.0;
                                                                                        // Sidebar soll links NEBEN dem Brett stehen (vom Zentrum aus):
            sidebar.setTranslateX(-(boardHalf + gap + sidebar.getPrefWidth() / 2.0));   // Sidebar links neben Board (vom Zentrum aus gerechnet)

        });

        // --- Back Button ---
        Button backButton = new Button("Back to Menu");
        backButton.setStyle("""
                -fx-background-color: #5b4636;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-background-radius: 10;
                -fx-padding: 6 12 6 12;
                """);
        backButton.setOnAction(e -> sceneManager.showMainMenu());

        // --- Layout ---
        StackPane.setAlignment(backButton, Pos.TOP_RIGHT);
        StackPane.setMargin(backButton, new Insets(10, 10, 0, 0));

        StackPane root = new StackPane();
        root.setStyle("""
                -fx-background-color: linear-gradient(to bottom, #c7a784, #8e735b);
                """);


        root.getChildren().add(gameView);                               // Board immer exakt zentriert

        StackPane.setAlignment(sidebar, Pos.CENTER);
        root.getChildren().add(sidebar);


        StackPane.setAlignment(backButton, Pos.TOP_RIGHT);              // Back Button oben rechts
        StackPane.setMargin(backButton, new Insets(20));
        root.getChildren().add(backButton);

        scene = new Scene(root, 1920, 1080);

    }

    private void updatePieces() {
        for (Node n : squareGrid.getChildren()) {
            StackPane square = (StackPane) n;
            square.getChildren().removeIf(node -> node instanceof ImageView);
        }

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
                        StackPane.setAlignment(pieceView, Pos.CENTER);
                        p.setImageView(pieceView);
                    }
                }
            }
        }
    }

    private void handleSquareClick(int x, int y) {
        Square square = game.getBoard().getSquare(x, y);
        Piece piece = square.getPiece();

        if (selectedPiece == null) {
            if (piece != null && piece.getPlayer() == game.getCurrentTurn()) {
                selectPiece(piece);
            }
            return;
        }

        boolean isLegalMove = false;
        for (Move m : legalMovesForSelected) {
            if (m.getToX() == x && m.getToY() == y) {
                game.getBoard().makeMove(m);
                updatePieces();
                clearBlueHighlights();
                selectedPiece = null;
                game.switchTurn();
                highlightKingInCheck(game.getCurrentTurn());
                isLegalMove = true;

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

        if (!isLegalMove && piece != null && piece.getPlayer() == game.getCurrentTurn()) {
            clearBlueHighlights();
            selectPiece(piece);
        }
    }

    private void selectPiece(Piece piece) {
        selectedPiece = piece;

        List<Move> moves = piece.calcValidMoves(
                game.getBoard(),
                piece.getSquare().getRow(),
                piece.getSquare().getCol()
        );

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
        if (gameOverOverlay != null) return;

        gameOverOverlay = new StackPane();
        gameOverOverlay.setStyle("""
                -fx-background-color: rgba(0, 0, 0, 0.65);
                """);

        gameOverOverlay.setPrefSize(scene.getWidth(), scene.getHeight());

        VBox box = new VBox(20);
        box.setAlignment(Pos.CENTER);

        Label label = new Label(message);
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
        restart.setOnAction(e -> sceneManager.restartGame());

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

        StackPane root = (StackPane) scene.getRoot();
        root.getChildren().add(gameOverOverlay);
    }
}
