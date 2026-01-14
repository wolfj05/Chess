package at.ac.hcw.chess.pieces;

import at.ac.hcw.chess.gameutils.Board;
import at.ac.hcw.chess.gameutils.Move;
import at.ac.hcw.chess.gameutils.Player;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece{
    public Queen(Player player, String src) {
        super(player, src);
    }

    public Queen(Player player) {
        super(player, "/" + (player.getColor().equals("White") ? "white_queen.png" : "black_queen.png"));
    }

    @Override
    public List<Move> calcValidMoves(Board board, int x, int y) {
        List<Move> moves = new ArrayList<>();

        moves.addAll(calcRookMoves(board, x, y));
        moves.addAll(calcBishopMoves(board, x, y));

        return moves;
    }

    private List<Move> calcRookMoves(Board board, int x, int y) {
        List<Move> moves = new ArrayList<>();
        int[] dRow = {-1, 1, 0, 0};
        int[] dCol = {0, 0, -1, 1};

        for (int i = 0; i < 4; i++) {
            int r = x + dRow[i];
            int c = y + dCol[i];
            while (board.isValid(r, c)) {
                if (board.getSquare(r, c).getPiece() == null) {
                    moves.add(new Move(x, y, r, c, this, null,
                            false, null, false, false, false));
                } else {
                    if (board.getSquare(r, c).getPiece().getPlayer() != this.getPlayer())
                        moves.add(new Move(x, y, r, c, this, board.getSquare(r, c).getPiece(),
                                false, null, false, false, false));
                    break; // Blockiert
                }
                r += dRow[i];
                c += dCol[i];
            }
        }
        return moves;
    }

    private List<Move> calcBishopMoves(Board board, int x, int y) {
        List<Move> moves = new ArrayList<>();
        int[] dRow = {-1, -1, 1, 1};
        int[] dCol = {-1, 1, -1, 1};

        for (int i = 0; i < 4; i++) {
            int r = x + dRow[i];
            int c = y + dCol[i];
            while (board.isValid(r, c)) {
                if (board.getSquare(r, c).getPiece() == null) {
                    moves.add(new Move(x, y, r, c, this, null,
                            false, null, false, false, false));
                } else {
                    if (board.getSquare(r, c).getPiece().getPlayer() != this.getPlayer())
                        moves.add(new Move(x, y, r, c, this, board.getSquare(r, c).getPiece(),
                                false, null, false, false, false));
                    break;
                }
                r += dRow[i];
                c += dCol[i];
            }
        }
        return moves;
    }
}
