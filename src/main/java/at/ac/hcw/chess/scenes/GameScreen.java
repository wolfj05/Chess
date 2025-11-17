package at.ac.hcw.chess.scenes;

import at.ac.hcw.chess.gameutils.*;
import at.ac.hcw.chess.pieces.Piece;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.List;

public class GameScreen {

    private Scene scene;
    private final SceneManager sceneManager;
    private final Game game;

    private GridPane squareGrid;
    private StackPane gameView;

    private double squareSize;
    private Piece selectedPiece = null;
    private List<Move> legalMovesForSelected = new ArrayList<>();
    private List<StackPane> blueHighlights = new ArrayList<>();
    private StackPane redKingSquare = null;

    public GameScreen(SceneManager sceneManager) {
        this.game = sceneManager.getGame();
        this.sceneManager = sceneManager;
        createScene();
    }

    private void createScene() {
        // --- Board Image ---
        ImageView boardView = new ImageView(new Image("/board.png"));
        boardView.setPreserveRatio(true);
        boardView.setFitHeight(1000);
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

        Button settingsButton = new Button("Settings");
        settingsButton.setStyle("""
                -fx-background-color: #5b4636;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-background-radius: 10;
                -fx-padding: 10 20 10 20;
                """);

        sidebar.getChildren().addAll(restartButton, settingsButton);
        StackPane.setAlignment(backButton, Pos.TOP_RIGHT);
        StackPane.setMargin(backButton, new Insets(10, 10, 0, 0));

        HBox layout = new HBox(sidebar, gameView, backButton);
        layout.setStyle("""
                -fx-background-color: linear-gradient(to bottom, #c7a784, #8e735b);
                """);

        scene = new Scene(layout, 1920, 1080);

        backButton.setOnAction(e -> sceneManager.showMainMenu());
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
                    System.out.println("Checkmate! " +
                            (game.getCurrentTurn() == game.getPlayers()[0] ? "Black wins" : "White wins"));
                } else if (game.getBoard().isStalemate(game.getCurrentTurn())) {
                    System.out.println("Stalemate! Draw!");
                }
                break;
            }
        }

        // Neue Auswahl eigener Figur
        if (!isLegalMove && piece != null && piece.getPlayer() == game.getCurrentTurn()) {
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
        sceneManager.getStage().setFullScreen(true);
        sceneManager.getStage().setTitle("Chess");
    }
}
