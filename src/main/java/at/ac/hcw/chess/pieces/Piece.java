package at.ac.hcw.chess.pieces;

import at.ac.hcw.chess.gameutils.Board;
import at.ac.hcw.chess.gameutils.Move;
import at.ac.hcw.chess.gameutils.Player;
import at.ac.hcw.chess.gameutils.Square;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;

public abstract class Piece {
    Player player;
    Square square;
    boolean movable;
    String src;
    boolean hasMoved;
    ImageView imageView;

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

    public boolean isMovable() {
        return movable;
    }

    public String getSrc() {
        return src;
    }

    public void setSquare(Square square) {
        this.square = square;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public abstract List<Move> calcValidMoves(Board board, int x, int y); //CALC ist btw Slang für Calculate

}
