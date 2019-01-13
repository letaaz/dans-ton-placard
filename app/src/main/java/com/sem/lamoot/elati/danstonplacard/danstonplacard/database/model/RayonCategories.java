package com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class RayonCategories {


    private static final HashMap<List<String>, Rayon> listRayonCategories = new HashMap<List<String>, Rayon>();

    private static RayonCategories INSTANCE = new RayonCategories();


    private RayonCategories() {

        // BIO
        List<String> categoriesBio = new ArrayList<String>();
        categoriesBio.add("Bières bio");
        categoriesBio.add("Oeufs-labellises-biologiques");
        categoriesBio.add("Laits demi-écrémés biologiques");
        categoriesBio.add("Tisanes-bio");
        categoriesBio.add("Bio");
        categoriesBio.add("Infusion-bio");
        categoriesBio.add("Legumes-bio");
        categoriesBio.add("Vin rouge bio");
        categoriesBio.add("Fromages-bio");
        categoriesBio.add("Poissons-bio");
        categoriesBio.add("Saumons-fumes-bio");
        categoriesBio.add("Riz-bio");
        categoriesBio.add("Boulghours-bio");
        categoriesBio.add("Boulgour-bio");

        this.listRayonCategories.put(categoriesBio, Rayon.BIO);


        // SURGELE
        List<String> categoriesSurgele = new ArrayList<String>();
        categoriesSurgele.add("Surgelés");
        categoriesSurgele.add("Plats préparés surgelés");
        categoriesSurgele.add("Aliments à base de plantes surgelés");
        categoriesSurgele.add("Légumes surgelés");
        categoriesSurgele.add("Gâteaux et pâtisseries surgelés");
        categoriesSurgele.add("Poissons surgelés");
        categoriesSurgele.add("Steaks surgelés");
        categoriesSurgele.add("Steaks hachés de bœuf surgelés");
        categoriesSurgele.add("Entrées et snacks surgelés");
        categoriesSurgele.add("Mélanges d'aliments à base de plantes surgelés");
        categoriesSurgele.add("Fruits surgelés");
        categoriesSurgele.add("Mélanges de légumes surgelés");
        categoriesSurgele.add("Ravioli surgelés");
        categoriesSurgele.add("Brocolis en fleurettes surgelés");

        this.listRayonCategories.put(categoriesSurgele, Rayon.SURGELE);


        // FRUITS_LEGUMES
        List<String> categoriesFruitsLegumes = new ArrayList<String>();
        categoriesFruitsLegumes.add("Fruits");
        categoriesFruitsLegumes.add("Fruits et produits dérivés");
        categoriesFruitsLegumes.add("Légumes et dérivés");

        this.listRayonCategories.put(categoriesFruitsLegumes, Rayon.FRUITS_LEGUMES);


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
        List<String> categoriesBoissons = new ArrayList<String>();
        categoriesBoissons.add("Boissons");
        categoriesBoissons.add("Eaux");

        this.listRayonCategories.put(categoriesBoissons, Rayon.BOISSONS);


        //BOULANGERIE_PATISSERIE
        List<String> categoriesBoulangeriePatisserie = new ArrayList<String>();
        categoriesBoulangeriePatisserie.add("Boulangerie");
        categoriesBoulangeriePatisserie.add("Pains");
        categoriesBoulangeriePatisserie.add("Viennoiseries");
        categoriesBoulangeriePatisserie.add("Pains de mie");
        categoriesBoulangeriePatisserie.add("Viennoiseries");
        categoriesBoulangeriePatisserie.add("Pains précuits");
        categoriesBoulangeriePatisserie.add("Viennoiseries-et-brioches");
        categoriesBoulangeriePatisserie.add("Gâteaux");
        categoriesBoulangeriePatisserie.add("Pains au lait");
        categoriesBoulangeriePatisserie.add("Galettes");


        this.listRayonCategories.put(categoriesBoulangeriePatisserie, Rayon.BOULANGERIE_PATISSERIE);


        //VIANDES
        List<String> categoriesViandes = new ArrayList<String>();
        categoriesViandes.add("Viandes");
        categoriesViandes.add("Boucherie");
        categoriesViandes.add("Saucisses");
        categoriesViandes.add("Préparations de viande hachées fraîches");
        categoriesViandes.add("Farces");
        categoriesViandes.add("Viande-grille");
        categoriesViandes.add("Volailles");
        categoriesViandes.add("Farces");
        categoriesViandes.add("Viande-grille");

        this.listRayonCategories.put(categoriesViandes, Rayon.VIANDES);


        //POISSONS_CRUSTACES
        List<String> categoriesPoissons = new ArrayList<String>();
        categoriesPoissons.add("Poissons");
        categoriesPoissons.add("Crevettes");
        categoriesPoissons.add("Moules");
        categoriesPoissons.add("Coquillages");
        categoriesPoissons.add("Crustaces-en-conserve");
        categoriesPoissons.add("Plats-a-base-de-crustaces");
        categoriesPoissons.add("Soupes de poissons");
        categoriesPoissons.add("Sushi");
        categoriesPoissons.add("Sushi and Maki");

        this.listRayonCategories.put(categoriesPoissons, Rayon.POISSONS_CRUSTACES);


        //CREMERIE
        List<String> categoriesCremerie = new ArrayList<String>();
        categoriesCremerie.add("Laits laitiers");
        categoriesCremerie.add("Produits laitiers");
        categoriesCremerie.add("Boissons lactées laitiers fermentés");
        categoriesCremerie.add("Laits");
        categoriesCremerie.add("Oeufs-labellises-biologiques");
        categoriesCremerie.add("Oeufs-labellises");
        categoriesCremerie.add("Cremes-aux-oeufs");
        categoriesCremerie.add("Beurres");
        categoriesCremerie.add("Margarines");
        categoriesCremerie.add("Boissons végétales");

        this.listRayonCategories.put(categoriesCremerie, Rayon.CREMERIE);


        //YAOURTS_DESSERTS
        List<String> categoriesYaourtsDesserts = new ArrayList<String>();
        categoriesYaourtsDesserts.add("Yaourts");
        categoriesYaourtsDesserts.add("Gateaux-de-riz-sur-lit-de-caramel");
        categoriesYaourtsDesserts.add("Yaourts au lait de chèvre");
        categoriesYaourtsDesserts.add("Crèmes dessert");
        categoriesYaourtsDesserts.add("Oeufs-labellises");
        categoriesYaourtsDesserts.add("Cremes-aux-oeufs");
        categoriesYaourtsDesserts.add("Beurres");
        categoriesYaourtsDesserts.add("Margarines");
        categoriesYaourtsDesserts.add("Boissons végétales");
        categoriesYaourtsDesserts.add("Compotes");

        this.listRayonCategories.put(categoriesYaourtsDesserts, Rayon.YAOURTS_DESSERTS);


        //EPICERIE_SUCREE
        List<String> categoriesEpicerieSucree = new ArrayList<String>();
        categoriesEpicerieSucree.add("Cafés");
        categoriesEpicerieSucree.add("Thés");
        categoriesEpicerieSucree.add("Infusions");
        categoriesEpicerieSucree.add("Boissons chaudes");
        categoriesEpicerieSucree.add("Boissons végétales");
        categoriesEpicerieSucree.add("Laits en poudre");
        categoriesEpicerieSucree.add("Laits concentrés");
        categoriesEpicerieSucree.add("Céréales et dérivés");
        categoriesEpicerieSucree.add("Confitures et marmelades");
        categoriesEpicerieSucree.add("Miels");
        categoriesEpicerieSucree.add("Pâtes à tartiner");
        categoriesEpicerieSucree.add("Biscuits et gâteaux");
        categoriesEpicerieSucree.add("Confiseries");
        categoriesEpicerieSucree.add("Snacks sucrés");

        this.listRayonCategories.put(categoriesEpicerieSucree, Rayon.EPICERIE_SUCREE);


        //EPICERIE_SALEE
        List<String> categoriesEpicerieSalee = new ArrayList<String>();
        categoriesEpicerieSalee.add("Fruits secs");
        categoriesEpicerieSalee.add("Légumes secs");
        categoriesEpicerieSalee.add("Mélanges de fruits secs");
        categoriesEpicerieSalee.add("Cacahuètes");
        categoriesEpicerieSalee.add("Apéritif");
        categoriesEpicerieSalee.add("Saucissons");
        categoriesEpicerieSalee.add("Conserves");
        categoriesEpicerieSalee.add("Plats préparés");
        categoriesEpicerieSalee.add("Foies gras");
        categoriesEpicerieSalee.add("Soupes");
        categoriesEpicerieSalee.add("Pâtes alimentaires");
        categoriesEpicerieSalee.add("Riz");
        categoriesEpicerieSalee.add("Purées");
        categoriesEpicerieSalee.add("Couscous");
        categoriesEpicerieSalee.add("Semoules de blé");
        categoriesEpicerieSalee.add("Semoules de céréales");
        categoriesEpicerieSalee.add("Blés");
        categoriesEpicerieSalee.add("Légumes secs");
        categoriesEpicerieSalee.add("Sauces pour pâtes");
        categoriesEpicerieSalee.add("Huiles");
        categoriesEpicerieSalee.add("Vinaigres");
        categoriesEpicerieSalee.add("Sauces crudités");
        categoriesEpicerieSalee.add("Sels");
        categoriesEpicerieSalee.add("Poivres");
        categoriesEpicerieSalee.add("Herbes aromatiques");
        categoriesEpicerieSalee.add("Mélanges d'épices");
        categoriesEpicerieSalee.add("Epices");
        categoriesEpicerieSalee.add("Cornichons");
        categoriesEpicerieSalee.add("Olives");
        categoriesEpicerieSalee.add("Condiments");
        categoriesEpicerieSalee.add("Ketchup");
        categoriesEpicerieSalee.add("Mayonnaises");
        categoriesEpicerieSalee.add("Sauces tomate");
        categoriesEpicerieSalee.add("Aides culinaires");
        categoriesEpicerieSalee.add("Bouillons");

        this.listRayonCategories.put(categoriesEpicerieSalee, Rayon.EPICERIE_SALEE);
    }


    public static RayonCategories getInstance() {

        return INSTANCE;
    }


    public Rayon findRayonByCategory(String[] categories) {
        Rayon rayon = Rayon.DIVERS;

        Log.d("dtp", "Length of categories : "+categories.length);
        for (String categorie : categories) {
            Log.d("dtp", "Categorie : "+categorie);
            Log.d("dtp", "this.getKeys() : "+this.getKeys().size());
            for (List<String> cats : this.getKeys()) {
                Log.d("dtp", "Length of cats : "+cats.size());
                Log.d("dtp", "cats.contains(categorie) ? "+categorie+" : "+cats.contains(categorie));

                if (cats.contains(categorie)) {
                    Log.d("dtp", "Rayon trouvé !");
                    rayon = this.listRayonCategories.get(cats);
                    Log.d("dtp", "Rayon of product is : "+rayon.toString());
                    return rayon;
                }
            }
        }

        return rayon;
    }

    private List<List<String>> getKeys () {
        List<List<String>> keys = new ArrayList<List<String>>();
        Log.d("dtp", "Length of listRayonCategories : "+this.listRayonCategories.size());
        for (List<String> key : this.listRayonCategories.keySet()) {
            Log.d("dtp", "list of categories : "+key.toString());
            keys.add(key);
        }
        return keys;
    }

    @Override
    public String toString () {
        return super.toString();
    }
}
