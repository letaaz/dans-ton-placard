package com.sem.lamoot.elati.danstonplacard.danstonplacard.database.converter;

import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Piece;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Produit;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Rayon;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class ProduitTypeConverterTest {

    @Test
    public void testToProduitList() {

    }

    @Test
    public void testFromProduitList() {
        List<Produit> produits = new ArrayList<Produit>();
        Produit produit = new Produit("Bonbon", "123321123321", "Haribo", "wwww.abcd.com/image.png", 2, 500, new Date(), Rayon.EPICERIE_SUCREE, 1, Piece.CUISINE);
        produits.add(produit);

        ProduitTypeConverter.fromProduitList(produits);
    }
}
