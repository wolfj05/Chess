package at.ac.hcw.chess.gameutils;

import at.ac.hcw.chess.pieces.Pawn;

public class Board {
    Square[][] board = new Square[8][8];

    public Board(){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = new Square(i, j);
            }
        }
    }

    public Square[][] getBoard() {
        return board;
    }
}
