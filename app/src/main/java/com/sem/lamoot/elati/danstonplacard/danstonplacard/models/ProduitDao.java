//package com.sem.lamoot.elati.danstonplacard.danstonplacard.models;
//
//import android.arch.persistence.room.Dao;
//import android.arch.persistence.room.Insert;
//import android.arch.persistence.room.Query;
//
//import java.util.List;
//
//@Dao
//public interface ProduitDao {
//
//    @Insert
//    void insert(Produit produit);
//
//    @Query("DELETE FROM produit")
//    void deleteAll();
//
//    @Query("SELECT * from produit ORDER BY nom ASC")
//    List<Produit> getAllProduits();
//
//    @Query("SELECT COUNT(*) from produit")
//    int countProduits();
//}
