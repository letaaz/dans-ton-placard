package com.sem.lamoot.elati.danstonplacard.danstonplacard.database.converter;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Produit;

import java.lang.reflect.Type;
import java.util.List;

public class ProduitTypeConverter {

    @TypeConverter
    public static List<Produit> toProduitList(String produitList){
        Gson gson = new Gson();
        Type type = new TypeToken<List<Produit>>() {}.getType();
        return gson.fromJson(produitList, type);
    }

    @TypeConverter
    public static String fromProduitList(List<Produit> produitList){
        Gson gson = new Gson();
        Type type = new TypeToken<List<Produit>>() {}.getType();
        return gson.toJson(produitList, type);
    }
}
