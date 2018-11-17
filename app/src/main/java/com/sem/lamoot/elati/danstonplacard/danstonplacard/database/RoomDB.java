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
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Produit;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Recette;

@Database(entities = {Produit.class, Ingredient.class, Recette.class, ListeCourses.class}, version = 1, exportSchema = false)
@TypeConverters({RayonTypeConverter.class, PieceTypeConverter.class, DateTypeConverter.class, IngredientTypeConverter.class})
public abstract class RoomDB extends RoomDatabase {

    public abstract ProduitDao produitDao();

    private static volatile RoomDB INSTANCE;
    private static String DB_NAME = "danstonplacard_db";

    static RoomDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RoomDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RoomDB.class, DB_NAME).build();
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
