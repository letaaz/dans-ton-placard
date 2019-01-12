package com.sem.lamoot.elati.danstonplacard.danstonplacard.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.sem.lamoot.elati.danstonplacard.danstonplacard.ProduitAdapter;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Produit;

import java.util.List;

@Dao
public interface ProduitDao {

    @Insert
    void insert(Produit produit);

    @Query("DELETE FROM produit")
    void deleteAll();

    @Query("SELECT COUNT(*) from produit")
    int countProduits();

    @Query("UPDATE produit SET quantite = :quantite WHERE id = :id")
    void updateQuantityById(int id, int quantite);


    // TODO : Find a product by barcode product
    @Query("SELECT * from produit WHERE codeBarre = :barcode and piece = :piece")
    List<Produit> findProductByBarcode(String barcode, String piece);


    @Query("SELECT * from produit ORDER BY nom ASC")
    LiveData<List<Produit>> findAll();

    @Query("SELECT * from produit WHERE piece = :piece")
    LiveData<List<Produit>> findProductsByPiece(String piece);

    @Query("SELECT * from produit WHERE piece = :piece AND quantite > 0")
    LiveData<List<Produit>> getProduitsDisponiblesParPiece(String piece);

    @Query("SELECT * from produit WHERE piece = :piece AND quantite = 0")
    LiveData<List<Produit>> getProduitsIndisponiblesParPiece(String piece);

    @Query("SELECT * from produit WHERE id = :produitId")
    LiveData<Produit> findProduct(int produitId);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateProduct(Produit produit);

    @Query("DELETE from produit WHERE id = :id")
    void deleteProductById(int id);
}
