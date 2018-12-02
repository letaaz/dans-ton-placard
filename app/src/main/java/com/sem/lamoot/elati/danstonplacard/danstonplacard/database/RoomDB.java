package com.sem.lamoot.elati.danstonplacard.danstonplacard.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.converter.DateTypeConverter;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.converter.IngredientTypeConverter;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.converter.PieceTypeConverter;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.converter.RayonTypeConverter;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.dao.ProduitDao;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Ingredient;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.ListeCourses;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Piece;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Produit;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Rayon;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Recette;

@Database(entities = {Produit.class, Ingredient.class, Recette.class, ListeCourses.class}, version = 1, exportSchema = false)
@TypeConverters({RayonTypeConverter.class, PieceTypeConverter.class, DateTypeConverter.class, IngredientTypeConverter.class})
public abstract class RoomDB extends RoomDatabase {

    public abstract ProduitDao produitDao();

    private static volatile RoomDB INSTANCE;
    private static String DB_NAME = "danstonplacard_db";

    public static RoomDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RoomDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RoomDB.class, DB_NAME).allowMainThreadQueries().build(); // TODO /!\ Remove allowMainThreadQueries only for test - can block UI for a long periode time
                            createFakeProduct();
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    public static void createFakeProduct(){

        INSTANCE.produitDao().deleteAll();

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
}
