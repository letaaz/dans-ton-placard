package com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model;

public enum Rayon {


    SURGELE, BIO, FRUITS_LEGUMES, BOISSONS, BOULANGERIE_PATISSERIE,
    CREMERIE, YAOURTS_DESSERTS, FROMAGES, VIANDES, POISSONS_CRUSTACES, EPICERIE_SUCREE, EPICERIE_SALEE,
    BEAUTE, DIVERS;


    @Override
    public String toString() {
        return super.toString();
    }

    public static Rayon getRayon(String rayon) {
        if (rayon.equals("SURGELE") || rayon.equals("Surgelés"))
            return SURGELE;
        if (rayon.equals("BIO") || rayon.equals("Bio"))
            return BIO;
        if (rayon.equals("FRUITS_LEGUMES") || rayon.equals("Fruits et Légumes"))
            return FRUITS_LEGUMES;
        if (rayon.equals("BOISSONS") || rayon.equals("Boissons"))
            return BOISSONS;
        if (rayon.equals("BOULANGERIE_PATISSERIE") || rayon.equals("Boulangerie et Patisserie"))
            return BOULANGERIE_PATISSERIE;
        if (rayon.equals("CREMERIE") || rayon.equals("Fruits ou Crèmerie"))
            return CREMERIE;
        if (rayon.equals("YAOURTS_DESSERTS") || rayon.equals("Yaourts et Desserts"))
            return YAOURTS_DESSERTS;
        if (rayon.equals("FROMAGES") || rayon.equals("Fromages"))
            return FROMAGES;
        if (rayon.equals("VIANDES") || rayon.equals("Viandes"))
            return VIANDES;
        if (rayon.equals("POISSONS_CRUSTACES") || rayon.equals("Poissons et Crustaces"))
            return POISSONS_CRUSTACES;
        if (rayon.equals("EPICERIE_SUCREE") || rayon.equals("Épicerie sucrée"))
            return EPICERIE_SUCREE;
        if (rayon.equals("EPICERIE_SALEE") || rayon.equals("Épicerie salée"))
            return EPICERIE_SALEE;
        if (rayon.equals("BEAUTE") || rayon.equals("Beauté"))
            return BEAUTE;
        if (rayon.equals("DIVERS") || rayon.equals("Divers"))
            return DIVERS;
        else
            return null;
    }


}
