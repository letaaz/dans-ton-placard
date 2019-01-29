package com.danstonplacard.database.converter;

import com.danstonplacard.database.model.Rayon;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RayonTypeConverterTest {

    @Test
    public void testToRayon() {

        assertEquals(Rayon.BIO, RayonTypeConverter.toRayon("BIO"));
        assertEquals(Rayon.BOISSONS, RayonTypeConverter.toRayon("BOISSONS"));
        assertEquals(Rayon.BOULANGERIE_PATISSERIE, RayonTypeConverter.toRayon("BOULANGERIE_PATISSERIE"));
        assertEquals(Rayon.CREMERIE, RayonTypeConverter.toRayon("CREMERIE"));
        assertEquals(Rayon.DIVERS, RayonTypeConverter.toRayon("DIVERS"));
        assertEquals(Rayon.EPICERIE_SALEE, RayonTypeConverter.toRayon("EPICERIE_SALEE"));
        assertEquals(Rayon.EPICERIE_SUCREE, RayonTypeConverter.toRayon("EPICERIE_SUCREE"));
        assertEquals(Rayon.FROMAGES, RayonTypeConverter.toRayon("FROMAGES"));
        assertEquals(Rayon.FRUITS_LEGUMES, RayonTypeConverter.toRayon("FRUITS_LEGUMES"));
        assertEquals(Rayon.HYGIENES_BEAUTES, RayonTypeConverter.toRayon("HYGIENES_BEAUTES"));
        assertEquals(Rayon.POISSONS_CRUSTACES, RayonTypeConverter.toRayon("POISSONS_CRUSTACES"));
        assertEquals(Rayon.VIANDES, RayonTypeConverter.toRayon("VIANDES"));
        assertEquals(Rayon.YAOURTS_DESSERTS, RayonTypeConverter.toRayon("YAOURTS_DESSERTS"));
    }



    @Test
    public void testFromRayon() {
        assertEquals("BIO", RayonTypeConverter.fromRayon(Rayon.BIO));
        assertEquals("BOISSONS", RayonTypeConverter.fromRayon(Rayon.BOISSONS));
        assertEquals("BOULANGERIE_PATISSERIE", RayonTypeConverter.fromRayon(Rayon.BOULANGERIE_PATISSERIE));
        assertEquals("CREMERIE", RayonTypeConverter.fromRayon(Rayon.CREMERIE));
        assertEquals("DIVERS", RayonTypeConverter.fromRayon(Rayon.DIVERS));
        assertEquals("EPICERIE_SALEE", RayonTypeConverter.fromRayon(Rayon.EPICERIE_SALEE));
        assertEquals("EPICERIE_SUCREE", RayonTypeConverter.fromRayon(Rayon.EPICERIE_SUCREE));
        assertEquals("FROMAGES", RayonTypeConverter.fromRayon(Rayon.FROMAGES));
        assertEquals("FRUITS_LEGUMES", RayonTypeConverter.fromRayon(Rayon.FRUITS_LEGUMES));
        assertEquals("HYGIENES_BEAUTES", RayonTypeConverter.fromRayon(Rayon.HYGIENES_BEAUTES));
        assertEquals("POISSONS_CRUSTACES", RayonTypeConverter.fromRayon(Rayon.POISSONS_CRUSTACES));
        assertEquals("VIANDES", RayonTypeConverter.fromRayon(Rayon.VIANDES));
        assertEquals("YAOURTS_DESSERTS", RayonTypeConverter.fromRayon(Rayon.YAOURTS_DESSERTS));

    }
}