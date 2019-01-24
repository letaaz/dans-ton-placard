package com.sem.lamoot.elati.danstonplacard.danstonplacard.database.dao;


import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.RoomDB;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Piece;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Produit;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Rayon;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;


public class ProductDaoTest {


    private Produit produit;
    private ProduitDao produitDao;
    private RoomDB roomDB;

/*
    @Before
    public void initDb(){
        roomDB = RoomDB.create(InstrumentationRegistry.getTargetContext(), true);

        produitDao = roomDB.produitDao();


        produit = new Produit("Bonbon", "123321123321", "Haribo", "wwww.abcd.com/image.png",2, 500, new Date(), Rayon.EPICERIE_SUCREE, 1, Piece.CUISINE);
    }

    @After
    public void closeDb() throws Exception{
        roomDB.close();
    }
*/




    @Test
    public void onFetchingProduits_shouldGetEmptyList_IfTable_IsEmpty() throws InterruptedException {
        //List< Produit > produitList = LiveDataTestUtil.getValue(produitDao.getProduitsDisponiblesParPiece("Cuisine"));
        //assertTrue(produitList.isEmpty());
    }



/*    @Test
    public void testDBIsEmpty(){

        List<Produit> produitList = new ArrayList<Produit>();

        LiveData<List<Produit>> listLiveData = new LiveData<List<Produit>>() {
            @Override
            protected void setValue(List<Produit> value) {
                super.setValue(value);
            }

            @Nullable
            @Override
            public List<Produit> getValue() {
                return super.getValue();
            }
        };
/*
        //listLiveData.setValue(produitList);

        Mockito.when(produitDao.getProduitsDisponiblesParPiece("Cuisine")).thenReturn(listLiveData);
        Mockito.when(produitDao.getProduitsIndisponiblesParPiece("Cuisine")).thenReturn(listLiveData);

        assertTrue(produitDao.getProduitsDisponiblesParPiece("Cuisine").getValue().isEmpty());
        assertTrue(produitDao.getProduitsIndisponiblesParPiece("Cuisine").getValue().isEmpty());
    }

    @Test
    public void testAddProduct(){
        List<Produit> produitList = new ArrayList<>();
        produitList.add(produit);

    }*/
}
