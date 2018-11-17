package com.sem.lamoot.elati.danstonplacard.danstonplacard.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Ingredient;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Recette;

import java.util.List;

@Dao
interface RecetteDao {

    @Insert
    void insert(Recette recette);

    @Query("SELECT * FROM recette")
    List<Ingredient> getAllRecettes();
}
