package com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RayonCategoriesTest {

    private RayonCategories rayonCategories;



    @Before
    public void setUp() throws Exception {
        this.rayonCategories = RayonCategories.getInstance();
    }

    @Test
    public void testGetInstance() {
        assertNotNull(rayonCategories);
        assertNotNull(rayonCategories.getListRayonCategories());
        assertEquals(12, rayonCategories.getListRayonCategories().size());
    }

    @Test
    public void testGetKeys(){
        assertNotNull(rayonCategories);
        assertFalse(rayonCategories.getKeys().isEmpty());
        assertEquals(12, rayonCategories.getKeys().size());
    }

/*    @Test
    public void testFindRayonByCategory(){

        // categories for Foods
        String[] categoriesOne = new String[3];
        categoriesOne[0] = "Surgelés";
        assertNotNull(rayonCategories);
        assertEquals(Rayon.SURGELE, rayonCategories.findRayonByCategory(categoriesOne));

        // categories for BEAUTY
        String[] categoriesTwo = new String[3];
        categoriesTwo[0] = "Hygiène";
        categoriesTwo[1] = "Cheveux";
        assertEquals(Rayon.HYGIENES_BEAUTES, rayonCategories.findRayonByCategory(categoriesTwo));

        // categories are empty
        String[] categoriesThree = new String[3];
        assertEquals(Rayon.DIVERS, rayonCategories.findRayonByCategory(categoriesThree));

    }*/


}