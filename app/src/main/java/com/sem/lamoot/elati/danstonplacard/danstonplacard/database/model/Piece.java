package com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model;

public enum Piece {
    CUISINE, SALLE_DE_BAIN, CAVE, GARAGE, SALLE_A_MANGER;

    public static Piece getPiece(String piece) {
        if (piece.equals("CUISINE") || piece.equals("Cuisine"))
            return CUISINE;
        if (piece.equals("SALLE_DE_BAIN") || piece.equals("Salle de bain"))
            return SALLE_DE_BAIN;
        if (piece.equals("CAVE") || piece.equals("Cave"))
            return CAVE;
        if (piece.equals("GARAGE") || piece.equals("Garage"))
            return GARAGE;
        if (piece.equals("SALLE_A_MANGER") || piece.equals("Salle à manger"))
            return SALLE_A_MANGER;
        else
            return null;
    }
}