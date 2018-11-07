package com.sem.lamoot.elati.danstonplacard.danstonplacard.models;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Produit.class}, version = 1)
public abstract class ProduitRoomDB extends RoomDatabase {

    public abstract ProduitDao produitDao();

    private static volatile ProduitRoomDB INSTANCE;

    static ProduitRoomDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ProduitRoomDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ProduitRoomDB.class, "produit_db").build();
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
