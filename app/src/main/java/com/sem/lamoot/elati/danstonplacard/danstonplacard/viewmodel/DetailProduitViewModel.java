package com.sem.lamoot.elati.danstonplacard.danstonplacard.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;
import android.widget.EditText;

import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.RoomDB;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.dao.ProduitDao;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Produit;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.view.fragment.DetailProduitFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.annotations.NonNull;

public class DetailProduitViewModel extends AndroidViewModel {

    private ProduitDao produitDao;
    private ExecutorService executorService;

    public DetailProduitViewModel(@NonNull Application application) {
        super(application);
        this.produitDao = RoomDB.getDatabase(application.getApplicationContext()).produitDao();
        this.executorService = Executors.newSingleThreadExecutor();
    }

    /**
     * Retrieve a product from the DB via its id field
     * @param produitId
     */
    public LiveData<Produit> getProduit(int produitId) {
        return produitDao.findProduct(produitId);
    }

    public void updateProduct(Produit product) {
        this.executorService.execute(() -> produitDao.updateProduct(product));
    }

}
