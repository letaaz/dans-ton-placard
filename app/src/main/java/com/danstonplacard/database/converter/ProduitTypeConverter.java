package com.danstonplacard.database.converter;

import android.arch.persistence.room.TypeConverter;
import android.util.Log;

import com.danstonplacard.database.model.Produit;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ProduitTypeConverter {


    @TypeConverter
    public static List<Produit> toProduitList(String produitList){
//        Gson gson = new Gson();
//        Type type = new TypeToken<List<Produit>>() {}.getType();
//        return gson.fromJson(produitList, type);

        Gson gson = new Gson();
        Type listType = new TypeToken<List<Produit>>() {}.getType();
        return gson.fromJson(produitList, listType);
    }

    @TypeConverter
    public static String fromProduitList(List<Produit> produitList){
//        Gson gson = new Gson();
//        Type type = new TypeToken<List<Produit>>() {}.getType();
//        return gson.toJson(produitList, type);

        Gson gson = new Gson();
        System.out.println(gson.toJson(produitList));
        return gson.toJson(produitList);

    }
}