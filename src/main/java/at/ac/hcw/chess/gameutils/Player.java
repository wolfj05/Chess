package at.ac.hcw.chess.gameutils;

import at.ac.hcw.chess.pieces.*;

public class Player {
    String color;
    Piece[] pieces = new Piece[16];

    public Player(String color) {
        this.color = color;
        for (int i = 0; i < 8; i++) {
            pieces[i] = new Pawn(this);
        }
        pieces[8] = new Rook(this);
        pieces[9] = new Knight(this);
        pieces[10] = new Bishop(this);
        pieces[11] = new King(this);
        pieces[12] = new Queen(this);
        pieces[13] = new Bishop(this);
        pieces[14] = new Knight(this);
        pieces[15] = new Rook(this);
    }
}
