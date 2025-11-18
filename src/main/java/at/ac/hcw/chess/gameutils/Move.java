package at.ac.hcw.chess.gameutils;

import at.ac.hcw.chess.pieces.Piece;

public class Move {
    private final int fromX;
    private final int fromY;
    private final int toX;
    private final int toY;

    private final Piece movedPiece;
    private final Piece capturedPiece;   // null wenn kein Capture

    private final boolean isPromotion;
    private final Class<? extends Piece> promotionTarget; // z. B. Queen.class

    private final boolean isCastle;
    private boolean isEnPassant;
    private boolean isDoublePawnPush;

    public Move(int fromX, int fromY,
                int toX, int toY,
                Piece movedPiece,
                Piece capturedPiece,
                boolean isPromotion,
                Class<? extends Piece> promotionTarget,
                boolean isCastle,
                boolean isEnPassant,
                boolean isDoublePawnPush) {

        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
        this.movedPiece = movedPiece;
        this.capturedPiece = capturedPiece;

        this.isPromotion = isPromotion;
        this.promotionTarget = promotionTarget;
        this.isCastle = isCastle;
        this.isEnPassant = isEnPassant;
        this.isDoublePawnPush = isDoublePawnPush;
    }

    // ─────────────────────────────────────────────
    // ⬇ Getter
    // ─────────────────────────────────────────────
    public int getFromX() { return fromX; }
    public int getFromY() { return fromY; }
    public int getToX() { return toX; }
    public int getToY() { return toY; }

    public Piece getMovedPiece() { return movedPiece; }
    public Piece getCapturedPiece() { return capturedPiece; }

    public boolean isPromotion() { return isPromotion; }
    public Class<? extends Piece> getPromotionTarget() { return promotionTarget; }

    public boolean isCastle() { return isCastle; }
    public boolean isEnPassant() { return isEnPassant; }
    public boolean isDoublePawnPush() { return isDoublePawnPush; }

    public void setEnPassant(boolean v){
        this.isEnPassant = v;
    }

    public void setDoublePawnPush(boolean doublePawnPush) {
        isDoublePawnPush = doublePawnPush;
    }

    @Override
    public String toString() {
        return movedPiece.getClass().getSimpleName() +
                " from (" + fromX + "," + fromY + ") to (" + toX + "," + toY + ")" +
                (capturedPiece != null ? " capturing " + capturedPiece.getClass().getSimpleName() : "") +
                (isPromotion ? " promoting to " + promotionTarget.getSimpleName() : "") +
                (isCastle ? " castling" : "") +
                (isEnPassant ? " en passant" : "") +
                (isDoublePawnPush ? " double pawn push" : "");
    }
}
