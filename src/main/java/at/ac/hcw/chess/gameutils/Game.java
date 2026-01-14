package at.ac.hcw.chess.gameutils;

import at.ac.hcw.chess.pieces.Pawn;
import at.ac.hcw.chess.pieces.Piece;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private String whitePlayerName; // Name User1
    private String blackPlayerName; //  NAme User2
    private Board board;
    private Player[] players = new Player[2];
    private Player currentTurn;
    private List<Move> moveHistory = new ArrayList<>();
    private final List<Piece> capturedWhite = new ArrayList<>();
    private final List<Piece> capturedBlack = new ArrayList<>();
    private Clock clock;
    private int startTime = 10 * 60 * 1000;
    private boolean mute;

    public Game() {
        this.players[0] = new Player("White", "/man.png");
        this.players[1] = new Player("Black", "/woman.png");
        currentTurn = this.players[0];

        this.clock = new Clock(startTime);

        this.board = new Board(this.players, false, this);
    }

    public Player[] getPlayers() {
        return players;
    }

    public Player getCurrentTurn() {
        return currentTurn;
    }

    public Player getNotCurrentTurn() {
        return currentTurn.equals(players[0]) ? players[1] : players[0];
    }

    public List<Move> getMoveHistory() {
        return moveHistory;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime * 60 * 1000;
        this.clock = new Clock(this.startTime);
    }

    public boolean isMute() {
        return mute;
    }

    public void setMute(boolean mute) {
        this.mute = mute;
    }

    public Board getBoard() {
        return board;
    }

    public List<Piece> getCapturedWhite() { return capturedWhite; }

    public List<Piece> getCapturedBlack() { return capturedBlack; }

    public Clock getClock() {
        return clock;
    }

    public void setClock(Clock clock) {
        this.clock = clock;
    }

    public void setCurrentTurn(Player currentTurn) {
        this.currentTurn = currentTurn;
    }

    public void switchTurn(){
        if (currentTurn == players[0]){
            currentTurn = players[1];
        } else {
            currentTurn = players[0];
        }
    }

    public void startGame() {
        this.board = new Board(players, false, this);

        this.moveHistory.clear();

        this.capturedWhite.clear();
        this.capturedBlack.clear();

        this.currentTurn = players[0];

        this.clock.reset();
    }

    public void restartGame() {
        startGame();
    }

    public void addMoveToHistory(Move move) {
        moveHistory.add(move);
    }

    public void addCapturedPiece(Piece p) {
        // tiefkopierte Boards dürfen NICHT capturen
        if (this.board.isSimulation) return;

        if (p == null) return;

        if (p.getPlayer().getColor().equals("White")) {
            if (!capturedWhite.contains(p))
                capturedWhite.add(p);
        } else {
            if (!capturedBlack.contains(p))
                capturedBlack.add(p);
        }
    }

    public void undoLastMove() {
        if (moveHistory.isEmpty()) return;

        Move m = moveHistory.removeLast();

        // Figuren zurücksetzen
        Square from = board.getSquare(m.getFromX(), m.getFromY());
        Square to = board.getSquare(m.getToX(), m.getToY());

        if(m.isPromotion()){
            to.setPiece(null);
            Pawn p = new Pawn(getCurrentTurn(), "/" + getCurrentTurn().getColor().toLowerCase() + "_pawn.png");
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
            board.getSquare(to.getRow(), from.getCol()).setPiece(m.getCapturedPiece());
            m.getCapturedPiece().setSquare(board.getSquare(to.getRow(), from.getCol()));
        }

        if(m.isCastle()){
            Piece r;
            if(m.getToX() == 6){
                r = board.getSquare(5, m.getFromY()).getPiece();
                r.setSquare(board.getSquare(7, m.getFromY()));

                board.getSquare(5, m.getFromY()).setPiece(null);
                board.getSquare(7, m.getFromY()).setPiece(r);
            } else {
                r = board.getSquare(3, m.getFromY()).getPiece();
                r.setSquare(board.getSquare(0, m.getFromY()));

                board.getSquare(3, m.getFromY()).setPiece(null);
                board.getSquare(0, m.getFromY()).setPiece(r);
            }
            r.setHasMoved(false);
            m.getMovedPiece().setHasMoved(false);
        }

        // Zugrecht zurück
        setCurrentTurn(getNotCurrentTurn());

        // Uhr zurücksetzen / stoppen
        clock.switchTurn();
    }
}
