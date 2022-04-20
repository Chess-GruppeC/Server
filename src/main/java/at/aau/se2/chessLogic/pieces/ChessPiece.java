package at.aau.se2.chessLogic.pieces;

import at.aau.se2.chessLogic.board.ChessBoard;
import at.aau.se2.chessLogic.board.Move;

import java.lang.reflect.Array;
import java.util.ArrayList;

public abstract class ChessPiece {
    int pieceValue;
    boolean moved=false;
    PieceColour colour;

    abstract public ArrayList<Move> getLegalMoves(ChessBoard board);

}
