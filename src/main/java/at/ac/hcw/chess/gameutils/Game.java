package at.ac.hcw.chess.gameutils;

import at.ac.hcw.chess.pieces.Piece;

import java.util.ArrayList;
import java.util.List;

public class Game {
    Board board;
    Player[] players = new Player[2];
    Player currentTurn;
    List<String> moveHistory = new ArrayList<>();
    private final List<Piece> capturedWhite = new ArrayList<>();
    private final List<Piece> capturedBlack = new ArrayList<>();
    private Clock clock;

    public Game() {
        this.players[0] = new Player("White");
        this.players[1] = new Player("Black");
        currentTurn = this.players[0];

        this.board = new Board(this.players, false, this);
    }

    public Player[] getPlayers() {
        return players;
    }

    public Player getCurrentTurn() {
        return currentTurn;
    }

    public List<String> getMoveHistory() {
        return moveHistory;
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

    public void addMoveToHistory(String notation) {
        moveHistory.add(notation);
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
}
