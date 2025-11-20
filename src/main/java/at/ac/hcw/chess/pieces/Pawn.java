package at.ac.hcw.chess.pieces;

import at.ac.hcw.chess.gameutils.Board;
import at.ac.hcw.chess.gameutils.Move;
import at.ac.hcw.chess.gameutils.Player;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece{
    public Pawn(Player player, String src) {
        super(player, src);
    }

    @Override
    public List<Move> calcValidMoves(Board board, int x, int y) {
        List<Move> moves = new ArrayList<>();

        int dir = (player.getColor().equals("White")) ? 1 : -1;
        int startRow = (player.getColor().equals("White")) ? 1 : 6;
        int promotionRow = (player.getColor().equals("White")) ? 7 : 0;

        // 1-square forward
        int ny = y + dir;
        if (board.getPiece(x, ny) == null) {
            boolean isPromotion = ny == promotionRow;

            moves.add(new Move(
                    x, y, x, ny,
                    this, null,
                    isPromotion,
                    isPromotion ? Queen.class : null,
                    false, false, false
            ));
        }

        // 2-square forward
        if (y == startRow && board.getPiece(x, ny) == null &&
                board.getPiece(x, ny + dir) == null) {

            moves.add(new Move(
                    x, y, x, ny + dir,
                    this, null,
                    false, null,
                    false, false, true    // double pawn push
            ));
        }

        // captures
        for (int dx : new int[]{-1, 1}) {
            int nx = x + dx;

            if (!board.isInside(nx, ny)) continue;

            Piece enemy = board.getPiece(nx, ny);
            if (enemy != null && enemy.getPlayer() != this.getPlayer()) {
                boolean isPromotion = ny == promotionRow;

                moves.add(new Move(
                        x, y, nx, ny,
                        this, enemy,
                        isPromotion,
                        isPromotion ? Queen.class : null,
                        false, false, false
                ));
            }
        }

        Move last = board.getLastMove();
        if (last != null && last.isDoublePawnPush()) {

            Piece lp = last.getMovedPiece();
            if (lp instanceof Pawn && lp.getPlayer() != this.getPlayer()) {

                int enemyX = last.getToX();
                int enemyY = last.getToY();

                // steht der gegnerische Bauer direkt links oder rechts?
                if (Math.abs(enemyX - x) == 1 && enemyY == y) {

                    int targetY = y + dir; // wo dein Bauer hinzieht

                    if (board.isInside(enemyX, targetY) && board.getPiece(enemyX, targetY) == null) {

                        Move enPassantMove = new Move(x, y, enemyX, targetY, this, lp, false, null, false, true, false);
                        enPassantMove.setEnPassant(true);
                        moves.add(enPassantMove);
                    }
                }
            }
        }

        return moves;
    }
}
