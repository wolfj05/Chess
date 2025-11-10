package at.ac.hcw.chess.gameutils;

import java.util.ArrayList;
import java.util.List;

public class Game {
    Board board;
    Player[] players = new Player[2];
    Player currentTurn;
    List<String> moveHistory = new ArrayList<>();

    public Game() {
        this.board = new Board();
        this.players[0] = new Player();
        this.players[1] = new Player();
    }

    public Board getBoard() {
        return board;
    }
}
