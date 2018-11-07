package com.sem.lamoot.elati.danstonplacard.danstonplacard.database.converter;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Ingredient;

import java.lang.reflect.Type;
import java.util.List;

public class IngredientTypeConverter {

    @TypeConverter
    public static List<Ingredient> toIngredientList(String ingredientList){
        Gson gson = new Gson();
        Type type = new TypeToken<List<Ingredient>>() {}.getType();
        List<Ingredient> json = gson.fromJson(ingredientList, type);
        return json;
    }

    @TypeConverter
    public static String fromIngredientList(List<Ingredient> ingredientList){
        Gson gson = new Gson();
        Type type = new TypeToken<List<Ingredient>>() {}.getType();
        String json = gson.toJson(ingredientList, type);
        return json;
    }
}
