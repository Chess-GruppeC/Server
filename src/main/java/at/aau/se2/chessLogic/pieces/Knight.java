package at.aau.se2.chessLogic.pieces;

import at.aau.se2.chessLogic.board.ChessBoard;
import at.aau.se2.chessLogic.board.Move;

import java.util.ArrayList;

public class Knight extends ChessPiece{

    public Knight(PieceColour colour) {
        this.pieceValue=3;
        this.colour=colour;
    }

    @Override
    public ArrayList<Move> getLegalMoves(ChessBoard board) {
        return null;
    }
}
