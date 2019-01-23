package com.sem.lamoot.elati.danstonplacard.danstonplacard.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.sem.lamoot.elati.danstonplacard.danstonplacard.R;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.converter.DateTypeConverter;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.converter.IngredientTypeConverter;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.converter.PieceTypeConverter;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.converter.RayonTypeConverter;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.dao.ListeCoursesDao;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.dao.ProduitDao;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Ingredient;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.ListeCourses;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Piece;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Produit;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Rayon;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Recette;

import java.util.ArrayList;
import java.util.concurrent.Executors;

@Database(entities = {Produit.class, Ingredient.class, Recette.class, ListeCourses.class}, version = 1, exportSchema = false)
@TypeConverters({RayonTypeConverter.class, PieceTypeConverter.class, DateTypeConverter.class, IngredientTypeConverter.class})
public abstract class RoomDB extends RoomDatabase {

    public abstract ProduitDao produitDao();
    public abstract ListeCoursesDao listeCoursesDao();

    private static volatile RoomDB INSTANCE;
    private static String DB_NAME = "danstonplacard_db";


    public static RoomDB getDatabase(final Context context) {
        if (INSTANCE == null) {

            context.deleteDatabase(DB_NAME);

            synchronized (RoomDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RoomDB.class, DB_NAME).allowMainThreadQueries().addCallback(new RoomDatabase.Callback(){
                        public void onCreate (SupportSQLiteDatabase db){
                            Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                                @Override
                                public void run() {
                                    ListeCourses listeCourses = new ListeCourses(context.getResources().getString(R.string.label_titre_listecourse_auto_generee_automatique));
                                    getDatabase(context).listeCoursesDao().insert(listeCourses);
                                    Log.i("dtp", "Liste automatique ajouté en BDD");
                                }
                            });
                        }
                        public void onOpen (SupportSQLiteDatabase db){
                            Log.i("dtp", "openDB");
                        }
                    }).build(); // TODO /!\ Remove allowMainThreadQueries only for test - can block UI for a long periode time
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {

        INSTANCE = null;
    }

    private static void createFakeProduct(){

        Produit p1 = new Produit("pizza", 1, Rayon.SURGELE, Piece.CUISINE);
        Produit p2 = new Produit("oeufs", 1, Rayon.BIO, Piece.CUISINE);
        Produit p3 = new Produit("pates", 1, Rayon.BIO, Piece.CUISINE);
        Produit p4 = new Produit("gel douche", 3, Rayon.BIO, Piece.SALLE_DE_BAIN);
        Produit p5 = new Produit("dentifrice", 0, Rayon.BIO, Piece.SALLE_DE_BAIN);
        Produit p6 = new Produit("déodorant", 1, Rayon.BIO, Piece.SALLE_DE_BAIN);

        INSTANCE.produitDao().insert(p1);
        INSTANCE.produitDao().insert(p2);
        INSTANCE.produitDao().insert(p3);
        INSTANCE.produitDao().insert(p4);
        INSTANCE.produitDao().insert(p5);
        INSTANCE.produitDao().insert(p6);
    }

    private static void createFakeLDC() {

        Produit p1 = new Produit("pizza", 1, Rayon.SURGELE, Piece.CUISINE);
        Produit p2 = new Produit("oeufs", 1, Rayon.BIO, Piece.CUISINE);
        Produit p3 = new Produit("pates", 1, Rayon.BIO, Piece.CUISINE);
        ArrayList<Produit> aPrendre = new ArrayList<>();
        aPrendre.add(p1);
        aPrendre.add(p2);
        aPrendre.add(p3);

        Produit p4 = new Produit("gel douche", 3, Rayon.BIO, Piece.SALLE_DE_BAIN);
        Produit p5 = new Produit("dentifrice", 0, Rayon.BIO, Piece.SALLE_DE_BAIN);

        ArrayList<Produit> aPrendre2 = new ArrayList<>();
        aPrendre2.add(p4);
        aPrendre2.add(p5);


        ListeCourses l1 = new ListeCourses("liste1");
        l1.setProduitsAPrendre(aPrendre);
        l1.setProduitsPris(aPrendre2);
        ListeCourses l2 = new ListeCourses("liste2");
        l2.setProduitsAPrendre(aPrendre2);
        l2.setProduitsPris(aPrendre);

        ListeCourses l3 = new ListeCourses("liste3");
        ListeCourses l4 = new ListeCourses("liste4");

        INSTANCE.listeCoursesDao().insert(l1);
        INSTANCE.listeCoursesDao().insert(l2);
        INSTANCE.listeCoursesDao().insert(l3);
        INSTANCE.listeCoursesDao().insert(l4);
    }
}
