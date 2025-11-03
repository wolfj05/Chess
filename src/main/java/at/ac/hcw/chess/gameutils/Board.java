package at.ac.hcw.chess.gameutils;

import at.ac.hcw.chess.pieces.Pawn;

public class Board {
    Square[][] board;

    public Board(){
        this.board = new Square[8][8];
        board[0][0].setP(new Pawn());
    }
}
