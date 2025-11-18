package at.ac.hcw.chess.gameutils;

import at.ac.hcw.chess.pieces.Piece;

import java.util.ArrayList;
import java.util.List;

public class Game {
    Board board;
    Player[] players = new Player[2];
    Player currentTurn;
    List<String> moveHistory = new ArrayList<>();

    public Game() {
        this.players[0] = new Player("White");
        this.players[1] = new Player("Black");
        currentTurn = this.players[0];

        this.board = new Board(this.players, false);
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

    public void switchTurn(){
        if (currentTurn == players[0]){
            currentTurn = players[1];
        } else {
            currentTurn = players[0];
        }
    }

    public void startGame() {
        this.board = new Board(players, false);

        currentTurn = players[0];
    }

    public void restartGame() {
        startGame();
    }
}
