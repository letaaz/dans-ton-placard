package com.sem.lamoot.elati.danstonplacard.danstonplacard.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

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

    // TODO : Delete a particular product

    // TODO : Update a product attributes
    @Query("UPDATE produit SET quantite = :quantite WHERE id = :id")
    void updateQuantityById(int id, int quantite);

    @Query("SELECT * from produit ORDER BY nom ASC")
    LiveData<List<Produit>> findAll();

    @Query("SELECT * from produit WHERE piece = :piece")
    LiveData<List<Produit>> findProductsByPiece(String piece);
}
