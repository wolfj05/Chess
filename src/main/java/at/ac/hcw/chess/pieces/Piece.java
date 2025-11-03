package at.ac.hcw.chess.pieces;

import at.ac.hcw.chess.gameutils.Player;
import at.ac.hcw.chess.gameutils.Square;

public abstract class Piece {
    Player player;
    Square square;
    boolean movable;

    abstract void calcValidMoves();  //calc ist SLANG für calculate (Das wissen viele nicht)

}
