package com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RayonTest {

    Rayon rayon;

    @Before
    public void setUp() throws Exception {
        rayon = Rayon.DIVERS;
    }

    @Test
    public void testToString() {
        assertEquals("DIVERS", rayon.toString());
    }

    @Test
    public void testGetRayon() {
        assertEquals(Rayon.SURGELE, Rayon.getRayon("SURGELE"));
        assertEquals(Rayon.SURGELE, Rayon.getRayon("Surgelés"));
        assertEquals(Rayon.BIO, Rayon.getRayon("BIO"));
        assertEquals(Rayon.BIO, Rayon.getRayon("Bio"));
        assertEquals(Rayon.FRUITS_LEGUMES, Rayon.getRayon("FRUITS ET LEGUMES"));
        assertEquals(Rayon.FRUITS_LEGUMES, Rayon.getRayon("Fruits et Légumes"));
        assertEquals(Rayon.BOISSONS, Rayon.getRayon("BOISSONS"));
        assertEquals(Rayon.BOISSONS, Rayon.getRayon("Boissons"));
        assertEquals(Rayon.BOULANGERIE_PATISSERIE, Rayon.getRayon("BOULANGERIE ET PATISSERIE"));
        assertEquals(Rayon.BOULANGERIE_PATISSERIE, Rayon.getRayon("Boulangerie et Patisserie"));
        assertEquals(Rayon.CREMERIE, Rayon.getRayon("CREMERIE"));
        assertEquals(Rayon.CREMERIE, Rayon.getRayon("Crèmerie"));
        assertEquals(Rayon.YAOURTS_DESSERTS, Rayon.getRayon("YAOURTS ET DESSERTS"));
        assertEquals(Rayon.YAOURTS_DESSERTS, Rayon.getRayon("Yaourts et Desserts"));
        assertEquals(Rayon.FROMAGES, Rayon.getRayon("FROMAGES"));
        assertEquals(Rayon.FROMAGES, Rayon.getRayon("Fromages"));
        assertEquals(Rayon.VIANDES, Rayon.getRayon("VIANDES"));
        assertEquals(Rayon.VIANDES, Rayon.getRayon("Viandes"));
        assertEquals(Rayon.POISSONS_CRUSTACES, Rayon.getRayon("POISSONS ET CRUSTACES"));
        assertEquals(Rayon.POISSONS_CRUSTACES, Rayon.getRayon("Poissons et Crustaces"));
        assertEquals(Rayon.EPICERIE_SUCREE, Rayon.getRayon("EPICERIE SUCREE"));
        assertEquals(Rayon.EPICERIE_SUCREE, Rayon.getRayon("Épicerie sucrée"));
        assertEquals(Rayon.EPICERIE_SALEE, Rayon.getRayon("EPICERIE SALEE"));
        assertEquals(Rayon.EPICERIE_SALEE, Rayon.getRayon("Épicerie salée"));
        assertEquals(Rayon.HYGIENES_BEAUTES, Rayon.getRayon("HYGIENES ET BEAUTES"));
        assertEquals(Rayon.HYGIENES_BEAUTES, Rayon.getRayon("Hygiènes et beautés"));
        assertEquals(Rayon.DIVERS, Rayon.getRayon("DIVERS"));
        assertEquals(Rayon.DIVERS, Rayon.getRayon("Divers"));
    }
}