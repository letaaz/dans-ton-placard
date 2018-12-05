package com.sem.lamoot.elati.danstonplacard.danstonplacard.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.RoomDB;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.dao.ProduitDao;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Produit;

import io.reactivex.annotations.NonNull;

public class DetailProduitViewModel extends AndroidViewModel {

    private ProduitDao produitDao;

    public DetailProduitViewModel(@NonNull Application application) {
        super(application);
        this.produitDao = RoomDB.getDatabase(application.getApplicationContext()).produitDao();
    }

    /**
     * Retrieve a product from the DB via its id field
     * @param produitId
     */
    public LiveData<Produit> getProduit(int produitId) {
        return produitDao.findProduct(produitId);
    }
}
