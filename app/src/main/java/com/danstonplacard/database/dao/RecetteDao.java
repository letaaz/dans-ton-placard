package com.danstonplacard.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.danstonplacard.database.model.Ingredient;
import com.danstonplacard.database.model.Recette;

import java.util.List;

@Dao
interface RecetteDao {

    @Insert
    void insert(Recette recette);

    @Query("SELECT * FROM recette")
    List<Ingredient> getAllRecettes();
}
