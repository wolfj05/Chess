package at.ac.hcw.chess.pieces;

import at.ac.hcw.chess.gameutils.Board;
import at.ac.hcw.chess.gameutils.Move;
import at.ac.hcw.chess.gameutils.Player;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece{
    public Knight(Player player, String src) {
        super(player, src);
    }

    @Override
    public List<Move> calcValidMoves(Board board, int x, int y) {
        List<Move> moves = new ArrayList<>();

        int[][] deltas = {
                {1, 2}, {2, 1}, {2, -1}, {1, -2},
                {-1, -2}, {-2, -1}, {-2, 1}, {-1, 2}
        };

        for (int[] d : deltas) {
            int nx = x + d[0];
            int ny = y + d[1];

            if (!board.isInside(nx, ny)) continue;

            Piece target = board.getPiece(nx, ny);

            if (target == null || target.getPlayer() != this.getPlayer()) {
                moves.add(new Move(
                        x, y, nx, ny,
                        this, target,
                        false, null,
                        false, false, false
                ));
            }
        }

        return moves;
    }
}
