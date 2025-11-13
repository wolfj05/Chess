package at.ac.hcw.chess.gameutils;

import at.ac.hcw.chess.pieces.Piece;

public class Square {
    private int row;
    private int col;
    private Piece p;
    private boolean isValidMove;

    public Square(Piece p,int row, int col, boolean isValidMove){
        this.p = p;
        this.row = row;
        this.col = col;
        this.isValidMove = isValidMove;
    }

    public Square(int row, int col, boolean isValidMove){
        this.row = row;
        this.col = col;
        this.isValidMove = isValidMove;
    }

    public Square(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public Piece getPiece() {
        return p;
    }

    public void setPiece(Piece p) {
        this.p = p;
    }

    public boolean isValidMove() {
        return isValidMove;
    }

    public void setValidMove(boolean validMove) {
        isValidMove = validMove;
    }
}
