package com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model;

public enum Piece {
    CUISINE, SALLE_DE_BAIN, CAVE, GARAGE, SALLE_A_MANGER, CHAMBRE, DIVERS;

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
            case "CHAMBRE" : case "chambre" :
                return CHAMBRE;
            case "GARAGE" : case "garage" :
                return GARAGE;
            case "DIVERS" : case "divers" :
                return DIVERS;
            default:
                return CUISINE;
        }
    }

    public static String pieceToString(Piece piece)
    {
        switch(piece){
            case CUISINE:
                return "cuisine";
            case SALLE_DE_BAIN:
                return "Salle de bain";
            case CAVE:
                return "cave";
            case GARAGE:
                return "garage";
            case SALLE_A_MANGER:
                return "Salle a manger";
            case CHAMBRE:
                return "chambre";
            case DIVERS:
                return "divers";
            default:
                return "divers";
        }
    }
}