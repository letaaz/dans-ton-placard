package com.sem.lamoot.elati.danstonplacard.danstonplacard.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.widget.EditText;

import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.RoomDB;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.dao.ProduitDao;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Produit;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.view.fragment.DetailProduitFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    /**
     * This methods is an input controls
     * It verifies the quantity and price fields of the detail view form
     * @param produitQuantite - should be an positive integer [0..[
     * @param produitPrix - should be a positive decimal [0.0..[
     * @return
     */
    public boolean validateForm(String produitQuantite, String produitPrix, String produitDlc) {
            int quantiteVal = Integer.parseInt(produitQuantite);
            float produitVal = Float.parseFloat(produitPrix);
            Date date;
            try {
                SimpleDateFormat sdf = DetailProduitFragment.DATE_FORMAT;
                date = sdf.parse(produitDlc);
                if (!produitDlc.equals((sdf.format(date))))
                    date = null;
            } catch (ParseException e) {
                return false;
            }
            return (quantiteVal > 0 && produitVal > 0 && date != null);
    }

    public void updateProduct(Produit product) {
        this.executorService.execute(() -> produitDao.updateProduct(product));
    }
}
