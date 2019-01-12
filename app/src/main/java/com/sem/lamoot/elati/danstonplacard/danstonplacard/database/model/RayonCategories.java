package com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class RayonCategories {


    private static final HashMap<List<String>, Rayon> listRayonCategories = new HashMap<List<String>, Rayon>();

    private static RayonCategories INSTANCE = new RayonCategories();


    private RayonCategories() {
        List<String> categories = new ArrayList<String>();

        // BIO
        categories.add("Bières bio");
        categories.add("Oeufs-labellises-biologiques");
        categories.add("Laits demi-écrémés biologiques");
        categories.add("Tisanes-bio");
        categories.add("Bio");
        categories.add("Aliments d'origine végétale");
        categories.add("Infusion-bio");
        categories.add("Legumes-bio");
        categories.add("Vin rouge bio");
        categories.add("Fromages-bio");
        categories.add("Poissons-bio");
        categories.add("Saumons-fumes-bio");
        categories.add("Riz-bio");
        categories.add("Boulghours-bio");
        categories.add("Boulgour-bio");

        listRayonCategories.put(categories, Rayon.BIO);
        listRayonCategories.clear();


        // SURGELE
        categories.add("Surgelés");
        categories.add("Plats préparés surgelés");
        categories.add("Aliments à base de plantes surgelés");
        categories.add("Légumes surgelés");
        categories.add("Gâteaux et pâtisseries surgelés");
        categories.add("Poissons surgelés");
        categories.add("Steaks surgelés");
        categories.add("Steaks hachés de bœuf surgelés");
        categories.add("Entrées et snacks surgelés");
        categories.add("Mélanges d'aliments à base de plantes surgelés");
        categories.add("Fruits surgelés");
        categories.add("Mélanges de légumes surgelés");
        categories.add("Ravioli surgelés");
        categories.add("Brocolis en fleurettes surgelés");

        listRayonCategories.put(categories, Rayon.SURGELE);
        listRayonCategories.clear();


        // FRUITS_LEGUMES
        categories.add("Fruits");
        categories.add("Fruits et produits dérivés");
        categories.add("Légumes et dérivés");

        listRayonCategories.put(categories, Rayon.FRUITS_LEGUMES);
        listRayonCategories.clear();


/*
        //FRUITS_LEGUMES_SECS
        categories.add("Fruits secs");
        categories.add("Légumes secs");
        categories.add("Mélanges de fruits secs");
        categories.add("Cacahuètes");
        listRayonCategories.put(categories, Rayon.FRUITS_LEGUMES_SECS);
        listRayonCategories.clear();
*/


        //BOISSONS
        categories.add("Boissons");
        categories.add("");

        listRayonCategories.put(categories, Rayon.BOISSONS);
        listRayonCategories.clear();


        //BOULANGERIE_PATISSERIE
        categories.add("Boulangerie");
        categories.add("Pains");
        categories.add("Viennoiseries");
        categories.add("Pains de mie");
        categories.add("Viennoiseries");
        categories.add("Pains précuits");
        categories.add("Viennoiseries-et-brioches");
        categories.add("Gâteaux");
        categories.add("Pains au lait");
        categories.add("Galettes");


        listRayonCategories.put(categories, Rayon.BOULANGERIE_PATISSERIE);
        listRayonCategories.clear();


        //VIANDES
        categories.add("Viandes");
        categories.add("Boucherie");
        categories.add("Saucisses");
        categories.add("Préparations de viande hachées fraîches");
        categories.add("Farces");
        categories.add("Viande-grille");
        categories.add("Volailles");
        categories.add("Farces");
        categories.add("Viande-grille");

        listRayonCategories.put(categories, Rayon.VIANDES);
        listRayonCategories.clear();

        //POISSONS_CRUSTACES
        categories.add("Poissons");
        categories.add("Crevettes");
        categories.add("Moules");
        categories.add("Coquillages");
        categories.add("Crustaces-en-conserve");
        categories.add("Plats-a-base-de-crustaces");
        categories.add("Soupes de poissons");
        categories.add("Sushi");
        categories.add("Sushi and Maki");

        listRayonCategories.put(categories, Rayon.POISSONS_CRUSTACES);
        listRayonCategories.clear();


        //CREMERIE
        categories.add("Laits laitiers");
        categories.add("Boissons lactées laitiers fermentés");
        categories.add("Laits");
        categories.add("Oeufs-labellises-biologiques");
        categories.add("Oeufs-labellises");
        categories.add("Cremes-aux-oeufs");
        categories.add("Beurres");
        categories.add("Margarines");
        categories.add("Boissons végétales");

        listRayonCategories.put(categories, Rayon.CREMERIE);
        listRayonCategories.clear();


        //YAOURTS_DESSERTS
        categories.add("Yaourts");
        categories.add("Gateaux-de-riz-sur-lit-de-caramel");
        categories.add("Yaourts au lait de chèvre");
        categories.add("Crèmes dessert");
        categories.add("Oeufs-labellises");
        categories.add("Cremes-aux-oeufs");
        categories.add("Beurres");
        categories.add("Margarines");
        categories.add("Boissons végétales");
        categories.add("Compotes");

        listRayonCategories.put(categories, Rayon.YAOURTS_DESSERTS);
        listRayonCategories.clear();


        //EPICERIE_SUCREE
        categories.add("Cafés");
        categories.add("Thés");
        categories.add("Infusions");
        categories.add("Boissons chaudes");
        categories.add("Boissons végétales");
        categories.add("Laits en poudre");
        categories.add("Laits concentrés");
        categories.add("Céréales et dérivés");
        categories.add("Confitures et marmelades");
        categories.add("Miels");
        categories.add("Pâtes à tartiner");
        categories.add("Biscuits et gâteaux");
        categories.add("Confiseries");
        categories.add("Snacks sucrés");

        listRayonCategories.put(categories, Rayon.EPICERIE_SUCREE);
        listRayonCategories.clear();


        //EPICERIE_SALEE
        categories.add("Fruits secs");
        categories.add("Légumes secs");
        categories.add("Mélanges de fruits secs");
        categories.add("Cacahuètes");
        categories.add("Apéritif");
        categories.add("Saucissons");
        categories.add("Conserves");
        categories.add("Plats préparés");
        categories.add("Foies gras");
        categories.add("Soupes");
        categories.add("Pâtes alimentaires");
        categories.add("Riz");
        categories.add("Purées");
        categories.add("Couscous");
        categories.add("Semoules de blé");
        categories.add("Semoules de céréales");
        categories.add("Blés");
        categories.add("Légumes secs");
        categories.add("Sauces pour pâtes");
        categories.add("Huiles");
        categories.add("Vinaigres");
        categories.add("Sauces crudités");
        categories.add("Sels");
        categories.add("Poivres");
        categories.add("Herbes aromatiques");
        categories.add("Mélanges d'épices");
        categories.add("Epices");
        categories.add("Cornichons");
        categories.add("Olives");
        categories.add("Condiments");
        categories.add("Ketchup");
        categories.add("Mayonnaises");
        categories.add("Sauces tomate");
        categories.add("Aides culinaires");
        categories.add("Bouillons");

        listRayonCategories.put(categories, Rayon.EPICERIE_SALEE);
        listRayonCategories.clear();
    }


    public static RayonCategories getInstance() {
        return INSTANCE;
    }


    public Rayon findRayonByCategory(List<String> categories) {
        for (String categorie : categories) {
            for (List<String> cats : this.getKeys()) {
                if (cats.contains(categorie)) {
                    return this.listRayonCategories.get(cats);
                }
            }
        }
        return Rayon.DIVERS;
    }

    private List<List<String>> getKeys () {
        List<List<String>> keys = new ArrayList<List<String>>();
        for (List<String> key : this.listRayonCategories.keySet()) {
            keys.add(key);
        }
        return keys;
    }

    @Override
    public String toString () {
        return super.toString();
    }
}
