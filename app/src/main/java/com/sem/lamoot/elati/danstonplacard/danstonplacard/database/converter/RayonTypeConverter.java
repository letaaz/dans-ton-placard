package com.sem.lamoot.elati.danstonplacard.danstonplacard.database.converter;

import android.arch.persistence.room.TypeConverter;

import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Rayon;

public class RayonTypeConverter {

    @TypeConverter
    public static Rayon toRayon(String rayon){
        return rayon == null ? null : Rayon.valueOf(rayon);
    }

    @TypeConverter
    public static String fromRayon(Rayon rayon){
        return rayon == null ? null : rayon.toString();
    }
}
