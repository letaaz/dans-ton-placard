package com.sem.lamoot.elati.danstonplacard.danstonplacard.database.converter;

import android.arch.persistence.room.TypeConverter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTypeConverter {

    public static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd/MM/yyy");

    @TypeConverter
    public static Date toDate(Long date){
        return date == null ? null : new Date(date);
    }

    @TypeConverter
    public static Long fromDate(Date date){
        return (date == null) ? null : date.getTime();
    }
}
