package at.ac.hcw.chess.gameutils;

import at.ac.hcw.chess.pieces.*;

import java.util.Objects;

public class Player {
    private String color;
    private Piece[] pieces = new Piece[16];
    private String avatarSrc;

    public Player(String color, String avatarSrc) {
        this.color = color;
        this.avatarSrc = avatarSrc;

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

    /** 
     * get current color
     * @return String
     */
    public String getColor() {
        return color;
    }

    /** 
     * get all pieces
     * @return Pieces
     */
    public Piece[] getPieces() {
        return pieces;
    }

    /** 
     * set all pieces
     * @param pieces
     */
    public void setPieces(Piece[] pieces) {
        this.pieces = pieces;
    }

    /** 
     * get avatar source
     * @return String
     */
    public String getAvatarSrc() {
        return avatarSrc;
    }

    /** 
     * set avatar source
     * @param avatarSrc
     */
    public void setAvatarSrc(String avatarSrc) {
        this.avatarSrc = avatarSrc;
    }
}
