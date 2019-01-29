package com.danstonplacard.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.danstonplacard.database.model.Ingredient;

import java.util.List;

@Dao
interface IngredientDao {

    @Insert
    void insert(Ingredient ingredient);

    @Query("SELECT * FROM ingredient")
    List<Ingredient> getAllIngredient();
}
