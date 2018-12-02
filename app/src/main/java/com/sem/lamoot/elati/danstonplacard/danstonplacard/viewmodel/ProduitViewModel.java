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

    public LiveData<List<Produit>> getProduitsDisponiblesParPiece(String piece)
    {
        return produitDao.getProduitsDisponiblesParPiece(piece);
    }
    public LiveData<List<Produit>> getProduitsIndisponiblesParPiece(String piece)
    {
        return produitDao.getProduitsIndisponiblesParPiece(piece);
    }

    public void updateProduit(int id, int quantite){
        executorService.execute(() -> produitDao.updateQuantityById(id, quantite));
    }


}
