package com.sem.lamoot.elati.danstonplacard.danstonplacard.database.converter;

import android.arch.persistence.room.TypeConverter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTypeConverter {

    public static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd/MM/yyy");

    /**
     * Returns the date in Date format from a date in Long format (in milliseconds)
     * @param date date (in millis) to convert in Date
     * @return Converted date in Date
     */
    @TypeConverter
    public static Date toDate(Long date){
        return date == null ? null : new Date(date);
    }

    /**
     * Returns the date in Long format (in milliseconds) from a date in Date format
     * @param date date (format Data) to convert in Long
     * @return Converted date in Long
     */
    @TypeConverter
    public static Long fromDate(Date date){
        return (date == null) ? null : date.getTime();
    }
}
