package at.aau.se2.chessLogic.pieces;

import at.aau.se2.chessLogic.board.ChessBoard;
import at.aau.se2.chessLogic.board.Location;
import at.aau.se2.chessLogic.board.Move;

import java.lang.reflect.Array;
import java.util.ArrayList;

public abstract class ChessPiece {
    int pieceValue;
    boolean moved=false;
    PieceColour colour;

    abstract public ArrayList<Location> getLegalMoves(ChessBoard board);

    public int getPieceValue() {
        return pieceValue;
    }

    public void setPieceValue(int pieceValue) {
        this.pieceValue = pieceValue;
    }

    public boolean isMoved() {
        return moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    public PieceColour getColour() {
        return colour;
    }

    public void setColour(PieceColour colour) {
        this.colour = colour;
    }
}
