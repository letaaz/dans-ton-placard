package com.danstonplacard.database.converter;

import com.danstonplacard.database.model.Piece;
import com.danstonplacard.database.model.Produit;
import com.danstonplacard.database.model.Rayon;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
