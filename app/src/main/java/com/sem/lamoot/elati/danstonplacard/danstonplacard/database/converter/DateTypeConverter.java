package com.sem.lamoot.elati.danstonplacard.danstonplacard.database.converter;

import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;

public class DateTypeConverter {

    @TypeConverter
    public static Date toDate(Long date){
        return date == null ? null : new Date(date);
    }

    @TypeConverter
    public static long fromDate(Date date){
        //return (date == null) ? null : date.getTime(); // TODO - Revoir cette méthode car NullPointerException lancée
        return 0;
    }
}
