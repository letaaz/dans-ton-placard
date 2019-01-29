package com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model;

import com.sem.lamoot.elati.danstonplacard.danstonplacard.R;

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
            case "HYGIENES ET BEAUTES": case "Hygiènes et beautés":
                return HYGIENES_BEAUTES;
            case "ANIMAUX": case "Animaux":
                return ANIMAUX;
            default:
                return DIVERS;
        }

    }

    public static int getRayonColor(Rayon rayon) {
        switch (rayon) {
            case SURGELE:
                return R.color.rayon_surgeles;
            case BIO:
                return R.color.rayon_bio;
            case FRUITS_LEGUMES:
                return R.color.rayon_fruits_legumes;
            case BOISSONS:
                return R.color.rayon_boissons;
            case BOULANGERIE_PATISSERIE:
                return R.color.rayon_boulangeries_patisseries;
            case CREMERIE:
                return R.color.cremerie;
            case YAOURTS_DESSERTS:
                return R.color.yaourts_desserts;
            case FROMAGES:
                return R.color.fromages;
            case VIANDES:
                return R.color.rayon_viandes;
            case POISSONS_CRUSTACES:
                return R.color.rayon_poissons_crustaces;
            case EPICERIE_SUCREE:
                return R.color.epicerie_sucree;
            case EPICERIE_SALEE:
                return R.color.epicerie_salee;
            case HYGIENES_BEAUTES:
                return R.color.epicerie_hygiene_beaute;
            default:
                return R.color.divers;
        }
    }
}
