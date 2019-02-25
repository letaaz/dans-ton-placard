package com.danstonplacard.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.db.SimpleSQLiteQuery;
import android.support.annotation.NonNull;
import android.util.Log;

import com.danstonplacard.database.RoomDB;
import com.danstonplacard.database.dao.ProduitDao;
import com.danstonplacard.database.model.Piece;
import com.danstonplacard.database.model.Produit;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProduitViewModel extends AndroidViewModel {

    private ProduitDao produitDao;
    private ExecutorService executorService;

    public ProduitViewModel(@NonNull Application application) {
        super(application);
        this.produitDao = RoomDB.getDatabase(application.getApplicationContext()).produitDao();
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Produit>> getProduitsDisponiblesTrierPar(String piece, String colonne, String trierPar, String nom){
        String query = "SELECT * from produit WHERE nom LIKE '%"+ nom +"%' AND piece = '" + Piece.valueOf(piece) + "' AND quantite > 0 ORDER BY " + colonne + " " + trierPar;
        return produitDao.runtimeQuery(new SimpleSQLiteQuery(query));
    }

    public LiveData<List<Produit>> getProduitsIndisponiblesTrierPar(String piece, String colonne, String trierPar, String nom){
        String query = "SELECT * from produit WHERE nom LIKE '%"+ nom +"%' AND piece = '" + Piece.valueOf(piece) + "' AND quantite = 0 ORDER BY " + colonne + " " + trierPar;
        return produitDao.runtimeQuery(new SimpleSQLiteQuery(query));
    }

    public LiveData<List<Produit>> getAllProduits(String piece) {
        return produitDao.findProductsByPiece(piece);
    }

    public LiveData<List<Produit>> getAllProduits()
    {
        return produitDao.findAll();
    }


    public void updateProduit(int id, int quantite){
        executorService.execute(() -> produitDao.updateQuantityById(id, quantite));
    }


}
