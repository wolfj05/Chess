package at.ac.hcw.chess.pieces;

import at.ac.hcw.chess.gameutils.Player;
import at.ac.hcw.chess.gameutils.Square;

public abstract class Piece {
    Player player;
    Square square;
    boolean movable;
    String src;

    public Piece(Player player, String src) {
        this.player = player;
        this.src = src;
    }

    public Player getPlayer() {
        return player;
    }

    public Square getSquare() {
        return square;
    }

    public boolean isMovable() {
        return movable;
    }

    public String getSrc() {
        return src;
    }

    abstract void calcValidMoves();  //calc ist SLANG für calculate (Das wissen viele nicht)

}
