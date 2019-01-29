package com.danstonplacard.database.converter;

import android.arch.persistence.room.TypeConverter;

import com.danstonplacard.database.model.Ingredient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class IngredientTypeConverter {

    /**
     * Return a list of ingredients from a list of ingredients in JSON format
     * @param ingredientList List of ingredients (JSON Format)
     * @return Converted ingredient List in List<T> Format
     */
    @TypeConverter
    public static List<Ingredient> toIngredientList(String ingredientList){
        Gson gson = new Gson();
        Type type = new TypeToken<List<Ingredient>>() {}.getType();
        List<Ingredient> json = gson.fromJson(ingredientList, type);
        return json;
    }

    /**
     * Return a list of ingredients in JSON format from a list of ingredients in List <Ingredients> format
     * @param ingredientList List of Ingredients
     * @return Converted ingredient list in JSON Format
     */
    @TypeConverter
    public static String fromIngredientList(List<Ingredient> ingredientList){
        Gson gson = new Gson();
        Type type = new TypeToken<List<Ingredient>>() {}.getType();
        String json = gson.toJson(ingredientList, type);
        return json;
    }
}
