package at.ac.hcw.chess.gameutils;

import at.ac.hcw.chess.pieces.*;

import java.util.List;

public class Board {
    Square[][] board = new Square[8][8];
    Player[] players;
    private Move lastMove;

    public Board(Player[] players, boolean empty){
        this.players = players;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = new Square(i, j);
            }
        }

        if(!empty) {
            for (Player player : players) {
                if (player.getColor().equals("White")) {
                    board[0][0].setPiece(player.getPieces()[8]);
                    board[1][0].setPiece(player.getPieces()[9]);
                    board[2][0].setPiece(player.getPieces()[10]);
                    board[3][0].setPiece(player.getPieces()[12]);
                    board[4][0].setPiece(player.getPieces()[11]);
                    board[5][0].setPiece(player.getPieces()[13]);
                    board[6][0].setPiece(player.getPieces()[14]);
                    board[7][0].setPiece(player.getPieces()[15]);

                    for (int x = 0; x < 8; x++) {
                        board[x][1].setPiece(player.getPieces()[x]);
                    }
                } else {

                    board[0][7].setPiece(player.getPieces()[8]);
                    board[1][7].setPiece(player.getPieces()[9]);
                    board[2][7].setPiece(player.getPieces()[10]);
                    board[3][7].setPiece(player.getPieces()[12]);
                    board[4][7].setPiece(player.getPieces()[11]);
                    board[5][7].setPiece(player.getPieces()[13]);
                    board[6][7].setPiece(player.getPieces()[14]);
                    board[7][7].setPiece(player.getPieces()[15]);

                    for (int x = 0; x < 8; x++) {
                        board[x][6].setPiece(player.getPieces()[x]);
                    }
                }
            }
        }
    }

    public Square[][] getBoard() {
        return board;
    }

    public Square getSquare(int row, int col){
        if (row < 0 || row >= 8 || col < 0 || col >= 8) return null;
        return board[row][col];
    }

    public boolean isInside(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }

    public Piece getPiece(int x, int y) {
        Square sq = getSquare(x, y);
        return sq != null ? sq.getPiece() : null;
    }

    public Move getLastMove() {
        return lastMove;
    }

    public void makeMove(Move move) {

        int x1 = move.getFromX();
        int y1 = move.getFromY();
        int x2 = move.getToX();
        int y2 = move.getToY();

        Piece piece = move.getMovedPiece();

        Square from = getSquare(x1, y1);
        Square to = getSquare(x2, y2);

        if (move.getCapturedPiece() != null) {
            to.setPiece(null);
        }

        if (move.isEnPassant()) {
            int dir = piece.getPlayer().getColor().equals("White") ? -1 : 1;

            Square pawnSquare = getSquare(move.getToX(), move.getToY() + dir);
            pawnSquare.setPiece(null);   // gegnerischen Bauern entfernen
        }

        from.setPiece(null);
        to.setPiece(piece);

        if (move.isPromotion() && move.getPromotionTarget() != null) {
            try {
                Piece promotedPiece = move.getPromotionTarget()
                        .getDeclaredConstructor(Player.class)
                        .newInstance(piece.getPlayer());
                to.setPiece(promotedPiece);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Promotion failed");
            }
        }

        if (move.isCastle()) {
            if (move.getToX() == 6) {  // short castle (king side)
                Square rookFrom = getSquare(7, y1);
                Square rookTo = getSquare(5, y1);

                Piece rook = rookFrom.getPiece();
                rookFrom.setPiece(null);
                rookTo.setPiece(rook);
            } else if (move.getToX() == 2) {   // long castle (queen side)
                Square rookFrom = getSquare(0, y1);
                Square rookTo = getSquare(3, y1);

                Piece rook = rookFrom.getPiece();
                rookFrom.setPiece(null);
                rookTo.setPiece(rook);
            }
        }

        if (piece instanceof Pawn && Math.abs(y2 - y1) == 2) {
            move.setDoublePawnPush(true);
        }

        // 6) Double pawn push (für späteres En Passant Tracking)
        this.lastMove = move;

        piece.setHasMoved(true);
    }

    public boolean isValid(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    public boolean isInCheck(Player player) {
        Square kingSquare = findKing(player);
        if (kingSquare == null) return false; // sollte nie passieren

        Player opponent = (player.getColor().equals("White")) ? players[1] : players[0];

        // Gehe alle gegnerischen Figuren durch
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Piece p = board[x][y].getPiece();
                if (p != null && p.getPlayer() == opponent) {
                    List<Move> opponentMoves = p.calcValidMoves(this, x, y);
                    for (Move m : opponentMoves) {
                        if (m.getToX() == kingSquare.getRow() && m.getToY() == kingSquare.getCol())
                            return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isCheckmate(Player player) {
        if (!isInCheck(player)) return false;

        // Prüfe, ob der Spieler noch einen legalen Zug hat
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Piece p = board[x][y].getPiece();
                if (p != null && p.getPlayer() == player) {
                    List<Move> moves = p.calcValidMoves(this, x, y);
                    for (Move m : moves) {
                        Board copy = this.deepCopy();  // Erstelle Kopie
                        copy.makeMove(m);             // Simuliere Zug
                        if (!copy.isInCheck(player)) {
                            return false;             // legaler Zug gefunden
                        }
                    }
                }
            }
        }
        return true; // keine legalen Züge → Schachmatt
    }

    public Square findKing(Player player) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Piece p = board[x][y].getPiece();
                if (p != null && p.getPlayer() == player && p instanceof King)
                    return board[x][y];
            }
        }
        return null;
    }

    public Board deepCopy() {
        Board copy = new Board(players, true);
        copy.lastMove = this.lastMove;

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Square original = this.board[x][y];
                Piece p = original.getPiece();
                if (p != null) {
                    // Kopie des Pieces erstellen, abhängig vom Typ
                    Piece newPiece = getNewPiece(p);
                    newPiece.setSquare(copy.board[x][y]); // neues Square setzen
                    copy.board[x][y].setPiece(newPiece);
                }
            }
        }

        return copy;
    }

    private static Piece getNewPiece(Piece p) {
        Piece newPiece = null;
        switch (p) {
            case Pawn pawn -> newPiece = new Pawn(p.getPlayer(), p.getSrc());
            case Rook rook -> newPiece = new Rook(p.getPlayer(), p.getSrc());
            case Knight knight -> newPiece = new Knight(p.getPlayer(), p.getSrc());
            case Bishop bishop -> newPiece = new Bishop(p.getPlayer(), p.getSrc());
            case Queen queen -> newPiece = new Queen(p.getPlayer(), p.getSrc());
            case King king -> newPiece = new King(p.getPlayer(), p.getSrc());
            default -> {
            }
        }

        assert newPiece != null;
        return newPiece;
    }

    public boolean isStalemate(Player player) {
        if (isInCheck(player)) return false; // Spieler steht nicht im Schach

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Piece p = board[x][y].getPiece();
                if (p != null && p.getPlayer() == player) {
                    List<Move> moves = p.calcValidMoves(this, x, y);
                    for (Move m : moves) {
                        Board copy = this.deepCopy();
                        copy.makeMove(m);
                        if (!copy.isInCheck(player)) {
                            return false; // legaler Zug gefunden → kein Patt
                        }
                    }
                }
            }
        }
        return true; // keine legalen Züge und nicht im Schach → Patt
    }
}
