package at.aau.se2.chessLogic.board;

import at.aau.se2.chessLogic.pieces.*;

public class ChessBoard {
    ChessPiece[][] gameBoard;

    public ChessBoard() {
        this.gameBoard = new ChessPiece[8][8];

        addPawns();
        addKnights();
        addBishops();
        addRooks();
        addQueens();
        addKings();
    }

    public void addPawns(){
        addBlackPawns();
        addWhitePawns();
    }

    public void addKnights(){
        addBlackKnights();
        addWhiteKnights();
    }

    public void addBishops(){
        addBlackBishops();
        addWhiteBishops();
    }

    public void addRooks(){
        addBlackRooks();
        addWhiteRooks();
    }

    public void addQueens(){
        addBlackQueen();
        addWhiteQueen();
    }

    public void addKings(){
        addBlackKing();
        addWhiteKing();
    }

    public void addBlackPawns(){
        for(int i=0; i<8; i++){
            Pawn blackPawn=new Pawn(PieceColour.BLACK);
            gameBoard[1][i]=blackPawn;
        }
    }

    public void addWhitePawns(){
        for(int i=0; i<8; i++){
            Pawn whitePawn=new Pawn(PieceColour.WHITE);
            gameBoard[6][i]=whitePawn;
        }
    }

    public void addBlackKnights(){
        Knight blackKnight1 = new Knight(PieceColour.BLACK);
        gameBoard[0][1]=blackKnight1;
        Knight blackKnight2 = new Knight(PieceColour.BLACK);
        gameBoard[0][6]=blackKnight2;
    }

    public void addWhiteKnights(){
        Knight whiteKnight1 = new Knight(PieceColour.WHITE);
        gameBoard[7][1]=whiteKnight1;
        Knight whiteKnight2 = new Knight(PieceColour.WHITE);
        gameBoard[7][6]=whiteKnight2;
    }

    public void addBlackBishops(){
        Bishop blackBishop1 = new Bishop(PieceColour.BLACK);
        gameBoard[0][2]=blackBishop1;
        Bishop blackBishop2 = new Bishop(PieceColour.BLACK);
        gameBoard[0][5]=blackBishop2;
    }

    public void addWhiteBishops(){
        Bishop whiteBishop1 = new Bishop(PieceColour.WHITE);
        gameBoard[7][2]=whiteBishop1;
        Bishop whiteBishop2 = new Bishop(PieceColour.WHITE);
        gameBoard[7][5]=whiteBishop2;
    }

    public void addBlackRooks(){
        Rook blackRook1 = new Rook(PieceColour.BLACK);
        gameBoard[0][0]=blackRook1;
        Rook blackRook2 = new Rook(PieceColour.BLACK);
        gameBoard[0][7]=blackRook2;
    }

    public void addWhiteRooks(){
        Rook whiteRook1 = new Rook(PieceColour.WHITE);
        gameBoard[7][0]=whiteRook1;
        Rook whiteRook2 = new Rook(PieceColour.WHITE);
        gameBoard[7][7]=whiteRook2;
    }

    public void addBlackQueen(){
        Queen blackQueen = new Queen (PieceColour.BLACK);
        gameBoard[0][3]=blackQueen;
    }

    public void addWhiteQueen(){
        Queen whiteQueen = new Queen (PieceColour.WHITE);
        gameBoard[7][3]=whiteQueen;
    }

    public void addBlackKing(){
        King blackKing = new King (PieceColour.BLACK);
        gameBoard[0][4]=blackKing;
    }

    public void addWhiteKing(){
        King whiteKing = new King (PieceColour.WHITE);
        gameBoard[7][3]=whiteKing;
    }

}
