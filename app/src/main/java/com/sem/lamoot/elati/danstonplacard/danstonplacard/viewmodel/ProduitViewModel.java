package com.sem.lamoot.elati.danstonplacard.danstonplacard.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.RoomDB;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.dao.ProduitDao;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Produit;

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

    public LiveData<List<Produit>> getAllProduits(String piece) {
        return produitDao.findProductsByPiece(piece);
    }

    public LiveData<List<Produit>> getAllProduits()
    {
        return produitDao.findAll();
    }

    public LiveData<List<Produit>> getProduitsDisponiblesParPieceTrierParNom(String piece)
    {
        return produitDao.getProduitsDisponiblesParPieceTrierParNom(piece);
    }

    public LiveData<List<Produit>> getProduitsDisponiblesParPieceTrierParRayon(String piece)
    {
        return produitDao.getProduitsDisponiblesParPieceTrierParRayon(piece);
    }


    public LiveData<List<Produit>> getProduitsDisponiblesParPieceTrierParDate(String piece)
    {
        return produitDao.getProduitsDisponiblesParPieceTrierParDate(piece);
    }


    public LiveData<List<Produit>> getProduitsDisponiblesParPieceTrierParPrix(String piece)
    {
        return produitDao.getProduitsDisponiblesParPieceTrierParPrix(piece);
    }


    public LiveData<List<Produit>> getProduitsIndisponiblesParPieceTrierParNom(String piece)
    {
        return produitDao.getProduitsIndisponiblesParPieceTrierParNom(piece);
    }

    public LiveData<List<Produit>> getProduitsIndisponiblesParPieceTrierParRayon(String piece)
    {
        return produitDao.getProduitsIndisponiblesParPieceTrierParRayon(piece);
    }

    public LiveData<List<Produit>> getProduitsIndisponiblesParPieceTrierParDate(String piece)
    {
        return produitDao.getProduitsIndisponiblesParPieceTrierParDate(piece);
    }

    public LiveData<List<Produit>> getProduitsIndisponiblesParPieceTrierParPrix(String piece)
    {
        return produitDao.getProduitsIndisponiblesParPieceTrierParPrix(piece);
    }

    public void updateProduit(int id, int quantite){
        executorService.execute(() -> produitDao.updateQuantityById(id, quantite));
    }


}
