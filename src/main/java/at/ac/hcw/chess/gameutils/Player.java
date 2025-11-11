package at.ac.hcw.chess.gameutils;

import at.ac.hcw.chess.pieces.*;

import java.util.Objects;

public class Player {
    String color;
    Piece[] pieces = new Piece[16];

    public Player(String color) {
        this.color = color;

        if(color.equals("White")) {
            for (int i = 0; i < 8; i++) {
                pieces[i] = new Pawn(this, "/white_pawn.png");
            }
            pieces[8] = new Rook(this, "/white_rook.png");
            pieces[9] = new Knight(this, "/white_knight.png");
            pieces[10] = new Bishop(this, "/white_bishop.png");
            pieces[11] = new King(this, "/white_king.png");
            pieces[12] = new Queen(this, "/white_queen.png");
            pieces[13] = new Bishop(this, "/white_bishop.png");
            pieces[14] = new Knight(this, "/white_knight.png");
            pieces[15] = new Rook(this, "/white_rook.png");
        }else{
            for (int i = 0; i < 8; i++) {
                pieces[i] = new Pawn(this, "/black_pawn.png");
            }
            pieces[8] = new Rook(this, "/black_rook.png");
            pieces[9] = new Knight(this, "/black_knight.png");
            pieces[10] = new Bishop(this, "/black_bishop.png");
            pieces[11] = new King(this, "/black_king.png");
            pieces[12] = new Queen(this, "/black_queen.png");
            pieces[13] = new Bishop(this, "/black_bishop.png");
            pieces[14] = new Knight(this, "/black_knight.png");
            pieces[15] = new Rook(this, "/black_rook.png");
        }
    }

    public String getColor() {
        return color;
    }

    public Piece[] getPieces() {
        return pieces;
    }

    public void setPieces(Piece[] pieces) {
        this.pieces = pieces;
    }
}
