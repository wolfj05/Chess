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

    public Player getPlayer() {
        return player;
    }

    public Square getSquare() {
        return square;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public String getSrc() {
        return src;
    }

    public void setSquare(Square square) {
        this.square = square;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public abstract List<Move> calcValidMoves(Board board, int x, int y); //CALC ist btw Slang für Calculate

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
