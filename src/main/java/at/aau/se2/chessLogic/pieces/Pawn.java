package at.aau.se2.chessLogic.pieces;

import at.aau.se2.chessLogic.board.ChessBoard;
import at.aau.se2.chessLogic.board.Location;

import java.util.ArrayList;

public class Pawn extends ChessPiece {

    public Pawn(PieceColour colour) {
        this.pieceValue=1;
        this.colour=colour;
    }

    @Override
    public ArrayList<Location> getLegalMoves(ChessBoard board) {

        if(this.colour==PieceColour.WHITE){
            return getLegalMovesWhitePawn(board);
        }
        return getLegalMovesBlackPawn(board);
    }

    private ArrayList<Location> getLegalMovesBlackPawn(ChessBoard board) {
        ArrayList<Location> legalMoveTargetList = new ArrayList<>();

        Location pieceLocation=board.getLocationOf(this);
        int row=pieceLocation.getRow();
        int column=pieceLocation.getColumn();
        int i;
        int j;

        //move forward
        i=row;
        j=column+1;
        if(board.isWithinBounds(row,j)){
            if(board.getPieceAtLocation(new Location(i, j))==null) {    //if empty space, add move option to legal moves
                legalMoveTargetList.add(new Location(i, j));

                if(this.moved==false){                                  //if empty space and not moved
                    j++;                                                //check also if next tile is free
                    if(board.isWithinBounds(row,j)) {
                        if (board.getPieceAtLocation(new Location(i, j)) == null) {    //if empty space, add move option to legal moves
                            legalMoveTargetList.add(new Location(i, j));
                        }
                    }
                }

            }
        }

        //check for taking left and right
        i=row-1;
        j=column+1;
        if(board.isWithinBounds(row,j)){
            if(board.getPieceAtLocation(new Location(i, j)).getColour()!=this.getColour()){
                legalMoveTargetList.add(new Location(i,j));
                //if space is occupied from enemy piece, give option to take
            }
        }
        i=row+1;
        j=column+1;
        if(board.isWithinBounds(row,j)){
            if(board.getPieceAtLocation(new Location(i, j)).getColour()!=this.getColour()){
                legalMoveTargetList.add(new Location(i,j));
                //if space is occupied from enemy piece, give option to take
            }
        }

        return legalMoveTargetList;
    }

    public ArrayList<Location> getLegalMovesWhitePawn(ChessBoard board){
        ArrayList<Location> legalMoveTargetList = new ArrayList<>();

        Location pieceLocation=board.getLocationOf(this);
        int row=pieceLocation.getRow();
        int column=pieceLocation.getColumn();
        int i;
        int j;

        //move forward
        i=row;
        j=column-1;
        if(board.isWithinBounds(row,j)){
            if(board.getPieceAtLocation(new Location(i, j))==null) {    //if empty space, add move option to legal moves
                legalMoveTargetList.add(new Location(i, j));

                if(this.moved==false){                                  //if empty space and not moved
                    j--;                                                //check also if next tile is free
                    if(board.isWithinBounds(row,j)) {
                        if (board.getPieceAtLocation(new Location(i, j)) == null) {    //if empty space, add move option to legal moves
                            legalMoveTargetList.add(new Location(i, j));
                        }
                    }
                }

            }
        }

        //check for taking left and right
        i=row-1;
        j=column-1;
        if(board.isWithinBounds(row,j)){
            if(board.getPieceAtLocation(new Location(i, j)).getColour()!=this.getColour()){
                legalMoveTargetList.add(new Location(i,j));
                //if space is occupied from enemy piece, give option to take
            }
        }
        i=row+1;
        j=column-1;
        if(board.isWithinBounds(row,j)){
            if(board.getPieceAtLocation(new Location(i, j)).getColour()!=this.getColour()){
                legalMoveTargetList.add(new Location(i,j));
                //if space is occupied from enemy piece, give option to take
            }
        }

        return legalMoveTargetList;
    }
}
