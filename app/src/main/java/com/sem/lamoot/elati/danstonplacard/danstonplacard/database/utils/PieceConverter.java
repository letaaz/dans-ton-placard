package com.sem.lamoot.elati.danstonplacard.danstonplacard.database.utils;

import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Piece;

public class PieceConverter {

    PieceConverter()
    {

    }

    public static Piece stringToPiece(String mPiece) {
        Piece piece = Piece.CUISINE;
        switch (mPiece) {
            case "CUISINE":
                piece = Piece.CUISINE;
                break;
            case "SALLE_DE_BAIN":
                piece = Piece.SALLE_DE_BAIN;
                break;
            case "CAVE":
                piece = Piece.CAVE;
                break;
            case "SALLE_A_MANGER":
                piece = Piece.SALLE_A_MANGER;
                break;
        }
        return piece;
    }
}
