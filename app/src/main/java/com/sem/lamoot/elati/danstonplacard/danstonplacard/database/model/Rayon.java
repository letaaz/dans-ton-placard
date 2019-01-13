package com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model;

public enum Rayon {
    SURGELE, BIO, FRUITS, LEGUMES, AUTRES;

    public static Rayon getRayon(String rayon) {
        switch (rayon) {
            case "SURGELE": case "Surgelés":
                return SURGELE;
            case "BIO": case "Bio":
                return BIO;
            case "FRUITS": case "Fruits":
                return FRUITS;
            case "LEGUMES": case "Légumes":
                return LEGUMES;
            default:
                return BIO;
        }
    }
}
