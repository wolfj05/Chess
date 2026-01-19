package at.ac.hcw.chess.scenes;

import at.ac.hcw.chess.gameutils.*;
import at.ac.hcw.chess.pieces.*;
import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;

import java.util.*;

public class GameScreen {

    private Scene scene;
    private final SceneManager sceneManager;
    private final Game game;
    private String whiteName;
    private String blackName;


    // BOARD
    private GridPane squareGrid;
    private StackPane gameView;
    private StackPane gameOverOverlay = null;

    private double squareSize;

    // PANELS
    private BorderPane leftPanel;
    private VBox moveListPane;
    private VBox moveListBox;

    // CLOCK LABELS (top = black, bottom = white)
    private Label blackClockLabel;
    private Label whiteClockLabel;

    // CAPTURED PIECES
    private HBox blackCapturedRow;
    private HBox whiteCapturedRow;

    // MATERIAL LABELS
    private Label blackMaterialLabel;
    private Label whiteMaterialLabel;

    // MOVE LOGIC
    private Piece selectedPiece = null;
    private final List<Move> legalMovesForSelected = new ArrayList<>();
    private final List<StackPane> blueHighlights = new ArrayList<>();
    private StackPane redKingSquare = null;

    // SOUNDS
    private AudioClip moveSound;
    private AudioClip captureSound;
    private AudioClip checkSound;
    private AudioClip endSound;
    private AudioClip pattSound;

    public GameScreen(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        this.game = sceneManager.getGame();
        this.whiteName = sceneManager.getWhitePlayerName();
        this.blackName = sceneManager.getBlackPlayerName();

        createScene();
    }

    /**
     * Create the Scene
     */
    private void createScene() {
        // LOAD SOUNDS
        moveSound    = new AudioClip(Objects.requireNonNull(getClass().getResource("/move.wav")).toString());
        captureSound = new AudioClip(Objects.requireNonNull(getClass().getResource("/capture.wav")).toString());
        checkSound   = new AudioClip(Objects.requireNonNull(getClass().getResource("/check.mp3")).toString());
        endSound   = new AudioClip(Objects.requireNonNull(getClass().getResource("/end.mp3")).toString());
        pattSound   = new AudioClip(Objects.requireNonNull(getClass().getResource("/patt.mp3")).toString());

        // BOARD IMAGE
        ImageView boardView = new ImageView(new Image("/board.png"));

        boardView.setPreserveRatio(true);
        boardView.setFitHeight(Screen.getPrimary().getBounds().getHeight() * 0.9);
        boardView.setSmooth(true);

        gameView = new StackPane();
        gameView.setAlignment(Pos.CENTER);
        gameView.getChildren().add(boardView);

        squareGrid = new GridPane();
        gameView.getChildren().add(squareGrid);

        squareGrid.boundsInParentProperty().addListener((obs, o, n) -> {
            double w = boardView.getBoundsInParent().getWidth();
            double h = boardView.getBoundsInParent().getHeight();
            if (w == 0 || h == 0) return;

            double borderRatio = 0.048; // 5% außen ringsum

            double innerSize = h * (1 - 2 * borderRatio);
            squareSize = innerSize / 8.0;

            squareGrid.setPrefSize(innerSize, innerSize);
            squareGrid.setMinSize(innerSize, innerSize);
            squareGrid.setMaxSize(innerSize, innerSize);

            // Grid-Erstellung falls noch nicht vorhanden
            if (squareGrid.getChildren().isEmpty()) {
                for (int x = 0; x < 8; x++) {
                    for (int y = 0; y < 8; y++) {
                        StackPane square = new StackPane();
                        square.setPrefSize(squareSize, squareSize);
                        square.setMinSize(squareSize, squareSize);
                        square.setMaxSize(squareSize, squareSize);

                        final int fx = x, fy = y;

                        square.setOnMouseClicked(e -> onSquareClick(fx, fy));

                        squareGrid.add(square, x, 7 - y);
                    }
                }
            }

            Platform.runLater(() -> {
                updatePieces();
                highlightKingCheck(game.getCurrentTurn());
            });
        });

        // Left Panel (22% WIDTH)
        leftPanel = new BorderPane();
        leftPanel.setPadding(new Insets(20));
        leftPanel.prefWidthProperty().bind(sceneManager.getStage().widthProperty().multiply(0.22));
        leftPanel.setStyle("-fx-background-color: transparent;");

        buildLeftPanelContent();

        // MOVE LIST RIGHT
        moveListBox = new VBox(8);
        moveListBox.setPadding(new Insets(20));
        moveListBox.prefWidthProperty().bind(sceneManager.getStage().widthProperty().multiply(0.22));
        ScrollPane scroll = new ScrollPane(moveListBox);
        scroll.setFitToWidth(true);
        scroll.setStyle("""
       -fx-background-color: transparent;
       -fx-background: transparent;
       -fx-control-inner-background: transparent;
       -fx-padding: 0;
""");

        moveListPane = new VBox(scroll);
        moveListPane.setPadding(new Insets(20));
        moveListPane.setStyle("-fx-background-color: rgba(40,40,40,0.45); -fx-background-radius: 20;");

        HBox layout = new HBox(30, leftPanel, gameView, moveListPane);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: linear-gradient(to bottom, #c7a784, #8e735b);");

        scene = new Scene(new StackPane(layout));
        sceneManager.getStage().setScene(scene);

        game.setClock(new Clock(game.getStartTime()));
        game.getClock().start();
        startClockUpdater();
    }

    /**
     * Build the left Panel (Player names, clock timers, captured pieces and menu buttons)
     */
    private void buildLeftPanelContent() {

        //Black
        HBox blackHeader = new HBox();
        blackHeader.setAlignment(Pos.CENTER_LEFT);

        ImageView blackAvatar = new ImageView(new Image(Objects.requireNonNull(getClass().getResource(game.getPlayers()[1].getAvatarSrc())).toExternalForm()));
        blackAvatar.setPreserveRatio(true);
        blackAvatar.setFitHeight(sceneManager.getStage().widthProperty().get() * 0.04);
        blackAvatar.setFitWidth(sceneManager.getStage().widthProperty().get() * 0.04);

        Label blackName = new Label(sceneManager.getBlackPlayerName());
        blackName.setStyle("-fx-font-size: 26px; -fx-font-weight: bold;");

        HBox blackNameBox = new HBox(12, blackAvatar, blackName);
        blackNameBox.setAlignment(Pos.CENTER_LEFT);

        blackClockLabel = new Label(game.getClock().format(game.getClock().getBlackTime()));
        blackClockLabel.setStyle("-fx-font-size: 26px; -fx-font-weight: bold;");

        Region spacer1 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);

        blackHeader.getChildren().addAll(blackNameBox, spacer1, blackClockLabel);

        // CAPTURED BLACK – OVERLAP 30%
        blackCapturedRow = new HBox();
        blackCapturedRow.setSpacing(-squareSize * 0.20);

        blackMaterialLabel = new Label();
        blackMaterialLabel.setStyle("-fx-font-size: 18px;");

        VBox blackSection = new VBox(8, blackHeader, blackCapturedRow, blackMaterialLabel);

        //Menu
        VBox menuBox = getMenuButtons();

        // White
        HBox whiteHeader = new HBox();
        whiteHeader.setAlignment(Pos.CENTER_LEFT);

        ImageView whiteAvatar = new ImageView(new Image(Objects.requireNonNull(getClass().getResource(game.getPlayers()[0].getAvatarSrc())).toExternalForm()));
        whiteAvatar.setPreserveRatio(true);
        whiteAvatar.setFitHeight(sceneManager.getStage().widthProperty().get() * 0.04);
        whiteAvatar.setFitWidth(sceneManager.getStage().widthProperty().get() * 0.04);

        Label whiteName = new Label(sceneManager.getWhitePlayerName());
        whiteName.setStyle("-fx-font-size: 26px; -fx-font-weight: bold;");

        HBox whiteNameBox = new HBox(12, whiteAvatar, whiteName);
        whiteNameBox.setAlignment(Pos.CENTER_LEFT);

        whiteClockLabel = new Label(game.getClock().format(game.getClock().getWhiteTime()));
        whiteClockLabel.setStyle("-fx-font-size: 26px; -fx-font-weight: bold;");

        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);

        whiteHeader.getChildren().addAll(whiteNameBox, spacer2, whiteClockLabel);

        whiteCapturedRow = new HBox();
        whiteCapturedRow.setSpacing(-squareSize * 0.20);

        whiteMaterialLabel = new Label();
        whiteMaterialLabel.setStyle("-fx-font-size: 18px;");

        VBox whiteSection = new VBox(8, whiteMaterialLabel, whiteCapturedRow, whiteHeader);


        // BUILD LEFT PANEL
        leftPanel.getChildren().clear();
        leftPanel.setTop(blackSection);
        leftPanel.setCenter(menuBox);
        leftPanel.setBottom(whiteSection);

    }

    /** 
     * @return all the menu buttons for the left panel
     */
    private VBox getMenuButtons() {
        Button undo = new Button("Undo");
        undo.setOnAction(e -> {game.undoLastMove(); updatePieces();});

        Button resign = new Button("Resign");
        resign.setOnAction(e -> onResign());

        Button back = new Button("Pause");
        back.setOnAction(e -> sceneManager.pauseGame());

        undo.setStyle(btn());
        resign.setStyle(btn());
        back.setStyle(btn());

        VBox menuBox = new VBox(12, undo, resign, back);
        menuBox.setAlignment(Pos.CENTER);
        return menuBox;
    }

    /** 
     * @return menu button styling 
     */
    private String btn() {
        return """
            -fx-background-color: #5b4636;
            -fx-text-fill: white;
            -fx-font-size: 18px;
            -fx-padding: 10 18;
            -fx-background-radius: 10;
        """;
    }

    /**
     * Remove old pieces and place the pieces on their new positions
     */
    private void updatePieces() {
        // Remove old piece images
        if (squareGrid == null) return;
        for (Node n : squareGrid.getChildren()) {
            StackPane square = (StackPane) n;
            square.getChildren().removeIf(node -> node instanceof ImageView);
        }

        // Place pieces
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Square sq = game.getBoard().getSquare(x, y);
                Piece p = sq.getPiece();
                if (p != null) {
                    p.setSquare(sq);
                    ImageView iv = new ImageView(new Image(p.getSrc()));
                    iv.setFitWidth(squareSize);
                    iv.setFitHeight(squareSize);
                    StackPane squareNode = getSquareNode(x, y);
                    if (squareNode != null) {
                        squareNode.getChildren().add(iv);
                        StackPane.setAlignment(iv, Pos.CENTER);
                        p.setImageView(iv);
                    }
                }
            }
        }

        // after pieces placed, ensure captured rows spacing adapts if squareSize changed
        double overlap = (squareSize > 0) ? -squareSize * 0.25 : -24;
        blackCapturedRow.setSpacing(overlap);
        whiteCapturedRow.setSpacing(overlap);
    }

    /** 
     * Funtion to execute an action if a square was clicked
     * @param x coordinate
     * @param y coordinate
     */
    private void onSquareClick(int x, int y) {
        Square square = game.getBoard().getSquare(x, y);
        Piece piece = square.getPiece();
        // Auswahl einer Figur
        if (piece != null && selectedPiece == null) {
            if (piece.getPlayer() == game.getCurrentTurn()) {
                selectPiece(piece);
            }
            return;
        } // Move ausführen
        boolean isValidMove = false;
        for (Move m : legalMovesForSelected) {
            if (m.getToX() == x && m.getToY() == y) {
                if(m.isPromotion()){
                    showPromotionPopup(selectedPiece, m);
                } else {
                    game.getBoard().makeMove(m);
                    if (m.getCapturedPiece() != null) {
                        if(!game.isMute()) {
                            captureSound.play();
                        }
                    } else {
                        if(!game.isMute()) {
                            moveSound.play();
                        }
                    }
                    game.addMoveToHistory(m);
                    updateMoveList();
                    Piece movedPiece = selectedPiece;
                    ImageView iv = movedPiece.getImageView();

                    int fromRow = m.getFromX();
                    int fromCol = m.getFromY();
                    int toRow = m.getToX();
                    int toCol = m.getToY();
                    animateMove(iv, fromRow, fromCol, toRow, toCol, this::updatePieces);

                    updatePlayerPanels();
                    clearBlueHighlights();
                    selectedPiece = null;
                    game.switchTurn();
                    game.getClock().switchTurn();
                    highlightKingCheck(game.getCurrentTurn());
                    isValidMove = true;
                    if (game.getBoard().isInCheck(game.getCurrentTurn())) {
                        if(!game.isMute()) {
                            checkSound.play();
                        }
                    }
                    if (game.getBoard().isCheckmate(game.getCurrentTurn())) {
                        String winner = (game.getCurrentTurn() == game.getPlayers()[0]) ? "Black wins!" : "White wins!";
                        showGameOverOverlay("Checkmate!\n" + winner);
                        if(!game.isMute()) {
                            endSound.play();
                        }
                    } else if (game.getBoard().isStalemate(game.getCurrentTurn())
                            || game.getBoard().hasInsufficientMaterial()
                            || game.getBoard().isFiftyMoveRule()
                            || game.getBoard().isThreefoldRepetition()) {
                        showGameOverOverlay("Stalemate! Draw");
                        if(!game.isMute()) {
                            pattSound.play();
                        }
                    }
                }
                break;
            }
        }

        if (piece != null && !isValidMove && piece.getPlayer() == game.getCurrentTurn()) {
            clearBlueHighlights();
            selectPiece(piece);
        }
    }

    /** 
     * Select a piece after it got clicked and visualize possible moves
     * @param piece
     */
    private void selectPiece(Piece piece) {
        if (piece == null || piece.getSquare() == null) return;
        selectedPiece = piece;

        // calc moves and filter out illegal ones that leave king in check
        List<Move> candidates = piece.calcValidMoves(game.getBoard(), piece.getSquare().getRow(), piece.getSquare().getCol());
        legalMovesForSelected.clear();
        for (Move m : candidates) {
            Board copy = game.getBoard().deepCopy();
            copy.makeMove(m);
            if (!copy.isInCheck(piece.getPlayer())) {
                legalMovesForSelected.add(m);
            }
        }

        highlightLegalMoves();
        highlightKingCheck(game.getCurrentTurn());
    }

    /**
     * visualize possible moves for selected piece
     */
    private void highlightLegalMoves() {
        clearBlueHighlights();
        for (Move mv : List.copyOf(legalMovesForSelected)) {
            StackPane node = getSquareNode(mv.getToX(), mv.getToY());
            if (node != null) {
                javafx.scene.shape.Circle dot = new javafx.scene.shape.Circle();

                dot.setRadius(squareSize * 0.15);     // Größe des Punktes
                dot.setFill(Color.rgb(80, 80, 80, 0.55)); // halbtransparentes Grau

                node.getChildren().add(dot);
                StackPane.setAlignment(dot, Pos.CENTER);
                blueHighlights.add(node);
            }
        }
    }

    /** 
     * highlight if a king is in check of a player
     * @param player the current Player
     */
    private void highlightKingCheck(Player player) {
        // clear previous
        if (redKingSquare != null) {
            redKingSquare.setBorder(null);
            redKingSquare = null;
        }

        if (game.getBoard().isInCheck(player)) {
            Square kingSq = game.getBoard().findKing(player);
            if (kingSq != null) {
                StackPane node = getSquareNode(kingSq.getRow(), kingSq.getCol());
                if (node != null) {
                    node.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
                    redKingSquare = node;
                }
            }
        }
    }

    /**
     * clear the visualizing of possible moves
     */
    private void clearBlueHighlights() {
        for (StackPane s : List.copyOf(blueHighlights)) {
            s.getChildren().removeIf(n -> n instanceof javafx.scene.shape.Circle);
        }
        blueHighlights.clear();
    }

    /** 
     * find the stackpane of a square at x,y
     * @param x coordinate
     * @param y coordinate
     * @return StackPane
     */
    private StackPane getSquareNode(int x, int y) {
        for (Node n : squareGrid.getChildren()) {
            Integer col = GridPane.getColumnIndex(n);
            Integer row = GridPane.getRowIndex(n);
            if (col == null || row == null) continue;
            if (col == x && row == (7 - y)) return (StackPane) n;
        }
        return null;
    }

    /**
     * show method
     */
    public void show() {
        sceneManager.getStage().setScene(scene);
        sceneManager.getStage().setFullScreenExitHint("");
        sceneManager.getStage().setFullScreenExitKeyCombination(javafx.scene.input.KeyCombination.NO_MATCH);
        sceneManager.getStage().setFullScreen(true);
        sceneManager.getStage().setResizable(false);
        sceneManager.getStage().setTitle("Chess");
    }

    /** 
     * visualize Overlay after game has concluded
     * @param message Message
     */
    private void showGameOverOverlay(String message) {
        if (gameOverOverlay != null) return;
        if (game.getClock() != null) game.getClock().stop();

        gameOverOverlay = new StackPane();
        gameOverOverlay.setStyle("-fx-background-color: rgba(0,0,0,0.65);");
        gameOverOverlay.setPrefSize(scene.getWidth(), scene.getHeight());

        VBox box = new VBox(16);
        box.setAlignment(Pos.CENTER);
        Label msg = new Label(message);
        msg.setStyle("-fx-font-size: 48px; -fx-text-fill: white; -fx-font-weight: bold;");

        Button r = new Button("Restart");
        Button b = new Button("Back to Menu");
        r.setOnAction(e -> sceneManager.restartGame());
        b.setOnAction(e -> sceneManager.showStartScreen());
        r.setStyle(btn());
        b.setStyle(btn());

        box.getChildren().addAll(msg, r, b);
        gameOverOverlay.getChildren().add(box);

        StackPane root = (StackPane) scene.getRoot();
        root.getChildren().add(gameOverOverlay);
    }

    /**
     * hide Overlay of concluded game
     */
    private void hideGameOverOverlay() {
        if (gameOverOverlay != null) {
            StackPane root = (StackPane) scene.getRoot();
            root.getChildren().remove(gameOverOverlay);
            gameOverOverlay = null;
        }
    }

    // ---------- CLOCK UPDATER ----------
    private void startClockUpdater() {
        AnimationTimer t = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (game.getClock() == null) return;

                try {
                    blackClockLabel.setText(game.getClock().format(game.getClock().getBlackTime()));
                    whiteClockLabel.setText(game.getClock().format(game.getClock().getWhiteTime()));
                } catch (Exception ignored) {
                }

                if (game.getClock().getWhiteTime() <= 0) {
                    showGameOverOverlay("Black wins (White ran out of time)");
                    stop();
                } else if (game.getClock().getBlackTime() <= 0) {
                    showGameOverOverlay("White wins (Black ran out of time)");
                    stop();
                }
            }
        };
        t.start();
    }

    /** 
     * Show Promotion UI
     * @param pawn Pawn to be promoted
     * @param move last Move
     */
    private void showPromotionPopup(Piece pawn, Move move) {
        if (pawn == null || move == null) return;

        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.6);");
        overlay.setPrefSize(scene.getWidth(), scene.getHeight());

        VBox box = new VBox(16);
        box.setAlignment(Pos.CENTER);
        box.setStyle("""
            -fx-background-color: #f0e6d2;
            -fx-padding: 18;
            -fx-background-radius: 16;
            -fx-border-radius: 16;
            -fx-border-color: #5b4636;
            -fx-border-width: 3;
        """);

        Label txt = new Label("Choose promotion piece:");
        txt.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        HBox buttons = new HBox(12);
        buttons.setAlignment(Pos.CENTER);

        Button q = new Button("Queen");
        Button r = new Button("Rook");
        Button b = new Button("Bishop");
        Button n = new Button("Knight");

        q.setStyle(btn());
        r.setStyle(btn());
        b.setStyle(btn());
        n.setStyle(btn());

        q.setOnAction(e -> {
            finishPromotion(move, "queen");
            ((StackPane) overlay.getParent()).getChildren().remove(overlay);
        });
        r.setOnAction(e -> {
            finishPromotion(move, "rook");
            ((StackPane) overlay.getParent()).getChildren().remove(overlay);
        });
        b.setOnAction(e -> {
            finishPromotion(move, "bishop");
            ((StackPane) overlay.getParent()).getChildren().remove(overlay);
        });
        n.setOnAction(e -> {
            finishPromotion(move, "knight");
            ((StackPane) overlay.getParent()).getChildren().remove(overlay);
        });

        buttons.getChildren().addAll(q, r, b, n);
        box.getChildren().addAll(txt, buttons);
        overlay.getChildren().add(box);

        // füge Overlay zur obersten Ebene hinzu
        StackPane root = (StackPane) scene.getRoot();
        root.getChildren().add(overlay);
    }

    /** 
     * finish the promotion process, after promotion target was chosen
     * @param move last Move
     * @param type promotion target
     */
    private void finishPromotion(Move move, String type) {
        if (move == null || type == null) return;

        Board board = game.getBoard();
        // make the move first (captures handled inside)
        board.makeMove(move);

        Player player = move.getMovedPiece().getPlayer();
        int x = move.getToX();
        int y = move.getToY();

        // replace pawn with chosen piece (use correct src)
        switch (type.toLowerCase()) {
            case "rook"  -> board.getSquare(x, y).setPiece(new Rook(player, player.getColor().equals("White") ? "/white_rook.png"  : "/black_rook.png"));
            case "bishop"-> board.getSquare(x, y).setPiece(new Bishop(player, player.getColor().equals("White") ? "/white_bishop.png": "/black_bishop.png"));
            case "knight"-> board.getSquare(x, y).setPiece(new Knight(player, player.getColor().equals("White") ? "/white_knight.png": "/black_knight.png"));
            default -> board.getSquare(x, y).setPiece(new Queen(player, player.getColor().equals("White") ? "/white_queen.png" : "/black_queen.png"));
        }

        // UI updates after promotion
        updatePieces();
        updatePlayerPanels();
        clearBlueHighlights();
        selectedPiece = null;

        // switch turn and clocks
        game.switchTurn();
        if (game.getClock() != null) game.getClock().switchTurn();

        highlightKingCheck(game.getCurrentTurn());
        if (board.isInCheck(game.getCurrentTurn()) && checkSound != null && !game.isMute()) checkSound.play();

        // record promotion move in history (append promotion piece letter)
        String base = move.getMoveNotation();
        String promoSuffix = switch (type.toLowerCase()) {
            case "rook" -> "=R";
            case "bishop" -> "=B";
            case "knight" -> "=N";
            default -> "=Q";
        };
        game.addMoveToHistory(move);
        updateMoveList();

        // check for mate/stalemate
        if (board.isCheckmate(game.getCurrentTurn())) {
            Player winner = (game.getCurrentTurn() == game.getPlayers()[0]) ? game.getPlayers()[1] : game.getPlayers()[0];
            showGameOverOverlay("Checkmate!\n" + winner.getColor() + " wins!");
        } else if (board.isStalemate(game.getCurrentTurn())) {
            showGameOverOverlay("Stalemate!\nDraw");
        }
    }

    /**
     * list of all past moves
     */
    private void updateMoveList() {
        moveListBox.getChildren().clear();
        List<Move> history = List.copyOf(game.getMoveHistory());
        for (int i = 0; i < history.size(); i++) {
            String txt = history.get(i).getMoveNotation();
            Label lbl = new Label((i + 1) + ". " + txt);
            lbl.setStyle("-fx-font-size: 16px; -fx-text-fill: black;");
            moveListBox.getChildren().add(lbl);
        }
    }

    /**
     * update player panels (captured + material)
     */
    private void updatePlayerPanels() {
        // compute scores
        int whiteScore = 0;
        int blackScore = 0;

        // White captured: pieces that Black has taken (display on whiteCapturedRow)
        List<Piece> capturedByWhite = new ArrayList<>(List.copyOf(game.getCapturedBlack()));
        capturedByWhite.sort(Comparator.comparingInt(Piece::getValue).reversed());

        whiteCapturedRow.getChildren().clear();
        for (Piece p : capturedByWhite) {
            ImageView iv = new ImageView(new Image(p.getSrc()));
            iv.setFitWidth(squareSize * 0.4);
            iv.setFitHeight(squareSize * 0.4);
            whiteCapturedRow.getChildren().add(iv);
            whiteScore += p.getValue();
        }

        // Black captured: pieces that White has taken (display on blackCapturedRow)
        List<Piece> capturedByBlack = new ArrayList<>(List.copyOf(game.getCapturedWhite()));
        capturedByBlack.sort(Comparator.comparingInt(Piece::getValue).reversed());

        blackCapturedRow.getChildren().clear();
        for (Piece p : capturedByBlack) {
            ImageView iv = new ImageView(new Image(p.getSrc()));
            iv.setFitWidth(squareSize * 0.4);
            iv.setFitHeight(squareSize * 0.4);
            blackCapturedRow.getChildren().add(iv);
            blackScore += p.getValue();
        }

        // material difference: only show if positive, smaller text, no color
        int diffBlack = blackScore - whiteScore;
        int diffWhite = whiteScore - blackScore;

        if (diffBlack > 0) {
            blackMaterialLabel.setText("+" + diffBlack);
            blackMaterialLabel.setStyle("-fx-font-size: 14px;");
        } else {
            blackMaterialLabel.setText("");
        }

        if (diffWhite > 0) {
            whiteMaterialLabel.setText("+" + diffWhite);
            whiteMaterialLabel.setStyle("-fx-font-size: 14px;");
        } else {
            whiteMaterialLabel.setText("");
        }
    }

    /**
     * Handles a players resignation
     */
    private void onResign() {
        Player loser = game.getCurrentTurn();
        Player winner = (game.getPlayers()[0] == loser ? game.getPlayers()[1] : game.getPlayers()[0]);
        showGameOverOverlay(winner.getColor() + " wins by resignation!");
        if(!game.isMute()) {
            endSound.play();
        }
    }

    /** 
     * Animate the move of moving a piece
     * @param pieceView piece image
     * @param fromRow "from" - y coordinate
     * @param fromCol "from" - x coordinate
     * @param toRow "to" - y coordinate
     * @param toCol "to" - x coordinate
     * @param onFinished function that is the cause for an animation
     */
    private void animateMove(
            ImageView pieceView,
            int fromRow, int fromCol,
            int toRow, int toCol,
            Runnable onFinished
    ) {
        StackPane fromSquare = getSquareNode(fromRow, fromCol);
        StackPane toSquare = getSquareNode(toRow, toCol);

        if (fromSquare == null || toSquare == null) {
            onFinished.run();
            return;
        }

        // Start- und Zielposition relativ zum Grid
        Bounds start = fromSquare.localToScene(fromSquare.getBoundsInLocal());
        Bounds end = toSquare.localToScene(toSquare.getBoundsInLocal());

        double dx = end.getMinX() - start.getMinX();
        double dy = end.getMinY() - start.getMinY();

        TranslateTransition tt = new TranslateTransition(Duration.millis(180), pieceView);
        tt.setByX(dx);
        tt.setByY(dy);
        tt.setInterpolator(Interpolator.EASE_OUT);

        tt.setOnFinished(e -> {
            pieceView.setTranslateX(0);
            pieceView.setTranslateY(0);
            onFinished.run();
        });

        tt.play();
    }

    /**
     * Undo the last move
     */
    private void undoLastMove() {
        if (game.getMoveHistory().isEmpty()) return;

        Move m = game.getMoveHistory().removeLast();

        // Figuren zurücksetzen
        Square from = game.getBoard().getSquare(m.getFromX(), m.getFromY());
        Square to = game.getBoard().getSquare(m.getToX(), m.getToY());

        if(m.isPromotion()){
            to.setPiece(null);
            Pawn p = new Pawn(game.getCurrentTurn(), "/" + game.getCurrentTurn().getColor().toLowerCase() + "_pawn.png");
            from.setPiece(p);
            p.setSquare(from);
        } else {
            to.setPiece(null);
            from.setPiece(m.getMovedPiece());
            m.getMovedPiece().setSquare(from);
        }

        // Capture rückgängig machen
        if (m.getCapturedPiece() != null && !m.isEnPassant()) {
            to.setPiece(m.getCapturedPiece());
            m.getCapturedPiece().setSquare(to);
        }

        if(m.isEnPassant()){
            game.getBoard().getSquare(to.getRow(), from.getCol()).setPiece(m.getCapturedPiece());
            m.getCapturedPiece().setSquare(game.getBoard().getSquare(to.getRow(), from.getCol()));
        }

        if(m.isCastle()){
            Piece r;
            if(m.getToX() == 6){
                r = game.getBoard().getSquare(5, m.getFromY()).getPiece();
                r.setSquare(game.getBoard().getSquare(7, m.getFromY()));

                game.getBoard().getSquare(5, m.getFromY()).setPiece(null);
                game.getBoard().getSquare(7, m.getFromY()).setPiece(r);
            } else {
                r = game.getBoard().getSquare(3, m.getFromY()).getPiece();
                r.setSquare(game.getBoard().getSquare(0, m.getFromY()));

                game.getBoard().getSquare(3, m.getFromY()).setPiece(null);
                game.getBoard().getSquare(0, m.getFromY()).setPiece(r);
            }
            r.setHasMoved(false);
            m.getMovedPiece().setHasMoved(false);
        }

        // Zugrecht zurück
        game.setCurrentTurn(game.getNotCurrentTurn());

        // Uhr zurücksetzen / stoppen
        game.getClock().switchTurn();

        updatePieces();
    }
}
