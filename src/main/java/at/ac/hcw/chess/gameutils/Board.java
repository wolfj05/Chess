package at.ac.hcw.chess.gameutils;

import at.ac.hcw.chess.pieces.Pawn;
import at.ac.hcw.chess.pieces.Piece;

public class Board {
    Square[][] board = new Square[8][8];

    public Board(Player[] players){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = new Square(i, j);
            }
        }

        for (Player player: players){
            if(player.getColor().equals("White")){
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
            }else {

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

    public Square[][] getBoard() {
        return board;
    }

    public Square getSquare(int row, int col){
        return board[row][col];
    }
}
