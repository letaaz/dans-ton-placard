package com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model;

public enum Piece {
    CUISINE, SALLE_DE_BAIN, CAVE, GARAGE, SALLE_A_MANGER;

    public static Piece getPiece(String piece) {
        switch (piece) {
            case "CUISINE": case "Cuisine":
                return CUISINE;
            case "SALLE_DE_BAIN": case "Salle_de_bain": case "Salle de bain" :
                return SALLE_DE_BAIN;
            case "CAVE": case "Cave":
                return CAVE;
            case "SALLE_A_MANGER": case "Salle_a_manger": case "Salle à manger": case "Salle a manger" :
                return SALLE_A_MANGER;
            default:
                return CUISINE;
        }
    }
}