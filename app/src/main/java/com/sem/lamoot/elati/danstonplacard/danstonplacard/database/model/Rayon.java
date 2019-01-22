package com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model;

public enum Rayon {


    SURGELE, BIO, FRUITS_LEGUMES, BOISSONS, BOULANGERIE_PATISSERIE,
    CREMERIE, YAOURTS_DESSERTS, FROMAGES, VIANDES, POISSONS_CRUSTACES, EPICERIE_SUCREE, EPICERIE_SALEE,
    HYGIENES_BEAUTES, ANIMAUX, DIVERS;


    @Override
    public String toString() {
        return super.toString();
    }

    public static Rayon getRayon(String rayon) {
        switch(rayon){
            case "SURGELE": case "Surgelés":
                return SURGELE;
            case "BIO": case "Bio":
                return BIO;
            case "FRUITS ET LEGUMES": case "Fruits et Légumes":
                return FRUITS_LEGUMES;
            case "BOISSONS": case "Boissons":
                return BOISSONS;
            case "BOULANGERIE ET PATISSERIE": case "Boulangerie et Patisserie":
                return BOULANGERIE_PATISSERIE;
            case "CREMERIE" : case "Crèmerie":
                return CREMERIE;
            case "YAOURTS ET DESSERTS": case "Yaourts et Desserts":
                return YAOURTS_DESSERTS;
            case "FROMAGES": case "Fromages":
                return FROMAGES;
            case "VIANDES": case "Viandes":
                return VIANDES;
            case "POISSONS ET CRUSTACES": case "Poissons et Crustaces":
                return POISSONS_CRUSTACES;
            case "EPICERIE SUCREE": case "Épicerie sucrée":
                return EPICERIE_SUCREE;
            case "EPICERIE SALEE": case "Épicerie salée":
                return EPICERIE_SALEE;
            case "HYGIENES_BEAUTES": case "Hygiènes et beautés":
                return HYGIENES_BEAUTES;
            case "ANIMAUX": case "Animaux":
                return ANIMAUX;
            default:
                return DIVERS;
        }

    }


}
