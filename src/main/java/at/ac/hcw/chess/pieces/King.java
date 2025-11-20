package at.ac.hcw.chess.pieces;

import at.ac.hcw.chess.gameutils.Board;
import at.ac.hcw.chess.gameutils.Move;
import at.ac.hcw.chess.gameutils.Player;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece{

    public King(Player player, String src) {
        super(player, src);
    }

    @Override
    public List<Move> calcValidMoves(Board board, int x, int y) {
        List<Move> moves = new ArrayList<>();

        int[][] deltas = {
                {1, 0}, {1, 1}, {0, 1}, {-1, 1},
                {-1, 0}, {-1, -1}, {0, -1}, {1, -1}
        };

        // normal moves
        for (int[] d : deltas) {
            int nx = x + d[0];
            int ny = y + d[1];

            if (!board.isInside(nx, ny)) continue;

            Piece target = board.getPiece(nx, ny);

            if (target == null || target.player != this.player) {
                moves.add(new Move(
                        x, y, nx, ny,
                        this, target,
                        false, null,
                        false, false, false
                ));
            }
        }

        // castling (vereinfachte Version)
        if (!hasMoved) {
            // king side
            Piece rook = board.getPiece(7, y);
            if (rook instanceof Rook && !((Rook) rook).hasMoved) {
                if (board.getPiece(5, y) == null && board.getPiece(6, y) == null) {
                    moves.add(new Move(
                            x, y, 6, y,
                            this, null,
                            false, null,
                            true, false, false      // castle flag
                    ));
                }
            }
            // queen side
            rook = board.getPiece(0, y);
            if (rook instanceof Rook && !((Rook) rook).hasMoved) {
                if (board.getPiece(1, y) == null &&
                        board.getPiece(2, y) == null &&
                        board.getPiece(3, y) == null) {

                    moves.add(new Move(
                            x, y, 2, y,
                            this, null,
                            false, null,
                            true, false, false
                    ));
                }
            }
        }

        return moves;
    }
}
