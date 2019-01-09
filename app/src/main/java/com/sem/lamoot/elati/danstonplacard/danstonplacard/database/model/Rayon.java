package com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model;

public enum Rayon {
    SURGELE, BIO, FRUITS, LEGUMES;

    @Override
    public String toString() {
        return super.toString();
    }

    public static Rayon getRayon(String rayon) {
        if (rayon.equals("SURGELE") || rayon.equals("Surgelés"))
            return SURGELE;
        if (rayon.equals("BIO") || rayon.equals("Bio"))
            return BIO;
        if (rayon.equals("FRUITS") || rayon.equals("Fruits"))
            return FRUITS;
        if (rayon.equals("LEGUMES") | rayon.equals("Légumes"))
            return LEGUMES;
        else
            return null;
    }
}
