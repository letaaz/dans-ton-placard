package com.sem.lamoot.elati.danstonplacard.danstonplacard.database.converter;

import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Piece;

import org.junit.Test;

import static org.junit.Assert.*;

public class PieceTypeConverterTest {

    @Test
    public void testToPiece() {

        assertEquals(Piece.CUISINE, PieceTypeConverter.toPiece("CUISINE"));

        assertEquals(Piece.SALLE_DE_BAIN, PieceTypeConverter.toPiece("SALLE_DE_BAIN"));

        assertEquals(Piece.CAVE, PieceTypeConverter.toPiece("CAVE"));

        assertEquals(Piece.SALLE_A_MANGER, PieceTypeConverter.toPiece("SALLE_A_MANGER"));

        assertEquals(Piece.CHAMBRE, PieceTypeConverter.toPiece("CHAMBRE"));

        assertEquals(Piece.GARAGE, PieceTypeConverter.toPiece("GARAGE"));

        assertEquals(Piece.DIVERS, PieceTypeConverter.toPiece("DIVERS"));





    }

    @Test
    public void testFromPiece() {
        assertEquals("CUISINE", PieceTypeConverter.fromPiece(Piece.CUISINE));

        assertEquals("SALLE_DE_BAIN", PieceTypeConverter.fromPiece(Piece.SALLE_DE_BAIN));

        assertEquals("CAVE", PieceTypeConverter.fromPiece(Piece.CAVE));

        assertEquals("SALLE_A_MANGER", PieceTypeConverter.fromPiece(Piece.SALLE_A_MANGER));

        assertEquals("CHAMBRE", PieceTypeConverter.fromPiece(Piece.CHAMBRE));

        assertEquals("GARAGE", PieceTypeConverter.fromPiece(Piece.GARAGE));

        assertEquals("DIVERS", PieceTypeConverter.fromPiece(Piece.DIVERS));
    }
}