package com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model;

public enum Piece {
    CUISINE, SALLE_DE_BAIN, CAVE, GARAGE, SALLE_A_MANGER, CHAMBRE, DIVERS;

    public static Piece getPiece(String piece) {
        switch (piece) {
            case "CUISINE": case "Cuisine": case "cuisine" :
                return CUISINE;
            case "SALLE_DE_BAIN": case "Salle_de_bain": case "Salle de bain" : case "salle de bain" :
                return SALLE_DE_BAIN;
            case "CAVE": case "Cave": case "cave" :
                return CAVE;
            case "SALLE_A_MANGER": case "Salle_a_manger": case "Salle à manger": case "Salle a manger" : case "salle a manger" : case "salle à manger" : case "sejour" : case "Séjour" : case "séjour" : case "Sejour" :
                return SALLE_A_MANGER;
            case "CHAMBRE" : case "chambre": case "Chambre" :
                return CHAMBRE;
            case "GARAGE" : case "garage" : case "Garage" :
                return GARAGE;
            case "DIVERS" : case "divers" : case "Divers" :
                return DIVERS;
            default:
                return DIVERS;
        }
    }

    public static String pieceToString(Piece piece)
    {
        switch(piece){
            case CUISINE:
                return "CUISINE";
            case SALLE_DE_BAIN:
                return "SALLE_DE_BAIN";
            case CAVE:
                return "CAVE";
            case GARAGE:
                return "GARAGE";
            case SALLE_A_MANGER:
                return "SALLE_A_MANGER";
            case CHAMBRE:
                return "CHAMBRE";
            case DIVERS:
                return "DIVERS";
            default:
                return "DIVERS";
        }
    }
}