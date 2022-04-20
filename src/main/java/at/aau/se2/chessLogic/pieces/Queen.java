package at.aau.se2.chessLogic.pieces;

import at.aau.se2.chessLogic.board.ChessBoard;
import at.aau.se2.chessLogic.board.Location;

import java.util.ArrayList;

public class Queen extends Bishop{

    public Queen(PieceColour colour) {
        super(colour);
        this.pieceValue=9;
    }

    @Override
    public ArrayList<Location> getLegalMoves(ChessBoard board) {
        return null;
    }
}
