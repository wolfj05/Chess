package at.ac.hcw.chess.pieces;

import at.ac.hcw.chess.gameutils.Board;
import at.ac.hcw.chess.gameutils.Move;
import at.ac.hcw.chess.gameutils.Player;
import at.ac.hcw.chess.gameutils.Square;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;

public abstract class Piece {
    private Player player;
    private Square square;
    private boolean movable;
    private String src;
    private boolean hasMoved;
    private ImageView imageView;

    public Piece(Player player, String src) {
        this.player = player;
        this.src = src;
        hasMoved = false;
    }

    public Piece(Player player) {
        this.player = player;
        hasMoved = false;
    }

    /** 
     * @return current Player
     */
    public Player getPlayer() {
        return player;
    }

    /** 
     * @return current Square
     */
    public Square getSquare() {
        return square;
    }

    /** 
     * @return ImageView
     */
    public ImageView getImageView() {
        return imageView;
    }

    /** 
     * @return source for image
     */
    public String getSrc() {
        return src;
    }

    /** 
     * set square
     * @param square
     */
    public void setSquare(Square square) {
        this.square = square;
    }

    /** 
     * set player
     * @param player
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /** 
     * @return if piece has moved
     */
    public boolean hasMoved() {
        return hasMoved;
    }

    /** 
     * set wether piece has moved
     * @param hasMoved
     */
    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    /** 
     * set the imageview
     * @param imageView
     */
    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    /**
     * function to calculate all possible moves a piece can make
     * @param board current board
     * @param x coordinate
     * @param y coordinate
     * @return list of all possible moves
     */
    public abstract List<Move> calcValidMoves(Board board, int x, int y); //CALC ist btw Slang für Calculate

    /** 
     * get a value based on which type of piece it is
     * @return int
     */
    public int getValue() {
        return switch (this.getClass().getSimpleName()) {
            case "Pawn" -> 1;
            case "Knight", "Bishop" -> 3;
            case "Rook" -> 5;
            case "Queen" -> 9;
            default -> 0; // König
        };
    }
}
