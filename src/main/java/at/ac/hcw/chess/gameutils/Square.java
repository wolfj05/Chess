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

    /** 
     * @return row
     */
    public int getRow() {
        return row;
    }

    /** 
     * set row
     * @param row
     */
    public void setRow(int row) {
        this.row = row;
    }

    /** 
     * @return column
     */
    public int getCol() {
        return col;
    }

    /** 
     * set column
     * @param col
     */
    public void setCol(int col) {
        this.col = col;
    }

    /** 
     * @return Piece
     */
    public Piece getPiece() {
        return p;
    }

    /** 
     * set piece
     * @param p Piece
     */
    public void setPiece(Piece p) {
        // Falls vorher eine Figur hier stand: Square-Referenz löschen
        if (this.p != null && this.p.getSquare() == this) {
            this.p.setSquare(null);
        }

        this.p = p;

        // Falls neue Figur: Square zuweisen
        if (p != null && p.getSquare() != this) {
            p.setSquare(this);
        }
    }

    /** 
     * @return if move is valid
     */
    public boolean isValidMove() {
        return isValidMove;
    }

    /** 
     * set if move is valid
     * @param validMove
     */
    public void setValidMove(boolean validMove) {
        isValidMove = validMove;
    }
}
