package at.ac.hcw.chess.pieces;

import at.ac.hcw.chess.gameutils.Board;
import at.ac.hcw.chess.gameutils.Move;
import at.ac.hcw.chess.gameutils.Player;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece{
    public Rook(Player player, String src) {
        super(player, src);
    }

    public Rook(Player player) {
        super(player);
    }

    @Override
    public List<Move> calcValidMoves(Board board, int x, int y) {
        List<Move> moves = new ArrayList<>();

        int[][] dirs = {
                {1, 0}, {-1, 0}, {0, 1}, {0, -1}
        };

        for (int[] d : dirs) {
            int nx = x + d[0];
            int ny = y + d[1];

            while (board.isInside(nx, ny)) {
                Piece target = board.getPiece(nx, ny);

                if (target == null) {
                    moves.add(new Move(x, y, nx, ny, this, null,
                            false, null, false, false, false));
                } else {
                    if (target.player != this.player) {
                        moves.add(new Move(x, y, nx, ny, this, target,
                                false, null, false, false, false));
                    }
                    break;
                }

                nx += d[0];
                ny += d[1];
            }
        }

        return moves;
    }
}
