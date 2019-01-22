package com.sem.lamoot.elati.danstonplacard.danstonplacard.database.converter;

import android.arch.persistence.room.TypeConverter;

import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Piece;

public class PieceTypeConverter {

    @TypeConverter
    public static Piece toPiece(String piece){
        return piece == null ? null : Piece.valueOf(piece);
    }

    @TypeConverter
    public static String d fromPiece(Piece piece){
        return piece == null ? null : piece.toString();
    }
}
