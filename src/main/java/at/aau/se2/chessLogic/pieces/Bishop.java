package at.aau.se2.chessLogic.pieces;

import at.aau.se2.chessLogic.board.ChessBoard;
import at.aau.se2.chessLogic.board.Location;

import java.util.ArrayList;

public class Bishop extends ChessPiece {

    public Bishop(PieceColour colour) {
        this.pieceValue=3;
        this.colour=colour;
    }

    @Override
    public ArrayList<Location> getLegalMoves(ChessBoard board) {
        ArrayList<Location> legalMoveTargetList = new ArrayList<>();

        Location pieceLocation=board.getLocationOf(this);

        //check all 4 directions

        //check up left
        for(int i=pieceLocation.getRow()-1, j=pieceLocation.getColumn()-1; i>=0 && j>=0; i--,j--){

            if(board.getPieceAtLocation(new Location(i, j))==null) {    //if empty space, add move option to legal moves
                legalMoveTargetList.add(new Location(i, j));
            }else
            if(board.getPieceAtLocation(new Location(i, j)).getColour()==this.getColour()){
                break;                                                 //if space is occupied from friendly piece, break
            }else
            if(board.getPieceAtLocation(new Location(i, j)).getColour()!=this.getColour()){
                legalMoveTargetList.add(new Location(i,j));
                break;                                                 //if space is occupied from enemy piece, give option to take and break
            }
        }

        //check up right
        for(int i=pieceLocation.getRow()-1, j=pieceLocation.getColumn()+1; i>=0 && j<8; i--,j++){

            if(board.getPieceAtLocation(new Location(i, j))==null) {    //if empty space, add move option to legal moves
                legalMoveTargetList.add(new Location(i, j));
            }else
            if(board.getPieceAtLocation(new Location(i, j)).getColour()==this.getColour()){
                break;                                                 //if space is occupied from friendly piece, break
            }else
            if(board.getPieceAtLocation(new Location(i, j)).getColour()!=this.getColour()){
                legalMoveTargetList.add(new Location(i,j));
                break;                                                 //if space is occupied from enemy piece, give option to take and break
            }
        }

        //check down right
        for(int i=pieceLocation.getRow()+1, j=pieceLocation.getColumn()+1; i<8 && j<8; i++,j++){

            if(board.getPieceAtLocation(new Location(i, j))==null) {    //if empty space, add move option to legal moves
                legalMoveTargetList.add(new Location(i, j));
            }else
            if(board.getPieceAtLocation(new Location(i, j)).getColour()==this.getColour()){
                break;                                                 //if space is occupied from friendly piece, break
            }else
            if(board.getPieceAtLocation(new Location(i, j)).getColour()!=this.getColour()){
                legalMoveTargetList.add(new Location(i,j));
                break;                                                 //if space is occupied from enemy piece, give option to take and break
            }
        }

        //check down left
        for(int i=pieceLocation.getRow()+1, j=pieceLocation.getColumn()-1; i<8 && j>=0; i++,j--){

            if(board.getPieceAtLocation(new Location(i, j))==null) {    //if empty space, add move option to legal moves
                legalMoveTargetList.add(new Location(i, j));
            }else
            if(board.getPieceAtLocation(new Location(i, j)).getColour()==this.getColour()){
                break;                                                 //if space is occupied from friendly piece, break
            }else
            if(board.getPieceAtLocation(new Location(i, j)).getColour()!=this.getColour()){
                legalMoveTargetList.add(new Location(i,j));
                break;                                                 //if space is occupied from enemy piece, give option to take and break
            }
        }


        return legalMoveTargetList;
    }
}
