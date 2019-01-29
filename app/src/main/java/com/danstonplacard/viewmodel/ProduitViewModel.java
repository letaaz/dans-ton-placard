package com.danstonplacard.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.danstonplacard.database.RoomDB;
import com.danstonplacard.database.dao.ProduitDao;
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

    public LiveData<List<Produit>> getProduitsDisponiblesTrierPar(String piece, String colonne, String trierPar){
        switch(colonne){
            case "Nom" :
                if(trierPar.equals("[A-Z]"))
                    return getProduitsDisponiblesParPieceTrierParNomASC(piece);
                else
                    return getProduitsDisponiblesParPieceTrierParNomDESC(piece);

            case"Rayon":
                if(trierPar.equals("[A-Z]"))
                    return getProduitsDisponiblesParPieceTrierParRayonASC(piece);
                else
                    return getProduitsDisponiblesParPieceTrierParRayonDESC(piece);

            case"Date":
                if(trierPar.equals("[+ récent]"))
                    return getProduitsDisponiblesParPieceTrierParDateASC(piece);
                else
                    return getProduitsDisponiblesParPieceTrierParDateDESC(piece);

            case"Prix":
                if(trierPar.equals("[croissant]"))
                    return getProduitsDisponiblesParPieceTrierParPrixASC(piece);
                else
                    return getProduitsDisponiblesParPieceTrierParPrixDESC(piece);


            default:
                return null;
        }
    }

    public LiveData<List<Produit>> getProduitsIndisponiblesTrierPar(String piece, String colonne, String trierPar){
        switch(colonne){
            case "Nom" :
                if(trierPar.equals("[A-Z]"))
                    return getProduitsIndisponiblesParPieceTrierParNomASC(piece);
                else
                    return getProduitsIndisponiblesParPieceTrierParNomDESC(piece);

            case"Rayon":
                if(trierPar.equals("[A-Z]"))
                    return getProduitsIndisponiblesParPieceTrierParRayonASC(piece);
                else
                    return getProduitsIndisponiblesParPieceTrierParRayonDESC(piece);

            case"Date":
                if(trierPar.equals("[+ récent]"))
                    return getProduitsIndisponiblesParPieceTrierParDateASC(piece);
                else
                    return getProduitsIndisponiblesParPieceTrierParDateDESC(piece);

            case"Prix":
                if(trierPar.equals("[croissant]"))
                    return getProduitsIndisponiblesParPieceTrierParPrixASC(piece);
                else
                    return getProduitsIndisponiblesParPieceTrierParPrixDESC(piece);

            default:
                return null;
        }
    }



    public LiveData<List<Produit>> getAllProduits(String piece) {
        return produitDao.findProductsByPiece(piece);
    }

    public LiveData<List<Produit>> getAllProduits()
    {
        return produitDao.findAll();
    }

    public LiveData<List<Produit>> getProduitsDisponiblesParPieceTrierParNomASC(String piece)
    {
        return produitDao.getProduitsDisponiblesParPieceTrierParNomASC(piece);
    }

    public LiveData<List<Produit>> getProduitsDisponiblesParPieceTrierParNomDESC(String piece)
    {
        return produitDao.getProduitsDisponiblesParPieceTrierParNomDESC(piece);
    }

    public LiveData<List<Produit>> getProduitsDisponiblesParPieceTrierParRayonASC(String piece)
    {
        return produitDao.getProduitsDisponiblesParPieceTrierParRayonASC(piece);
    }

    public LiveData<List<Produit>> getProduitsDisponiblesParPieceTrierParRayonDESC(String piece)
    {
        return produitDao.getProduitsDisponiblesParPieceTrierParRayonDESC(piece);
    }


    public LiveData<List<Produit>> getProduitsDisponiblesParPieceTrierParDateASC(String piece)
    {
        return produitDao.getProduitsDisponiblesParPieceTrierParDateASC(piece);
    }

    public LiveData<List<Produit>> getProduitsDisponiblesParPieceTrierParDateDESC(String piece)
    {
        return produitDao.getProduitsDisponiblesParPieceTrierParDateDESC(piece);
    }


    public LiveData<List<Produit>> getProduitsDisponiblesParPieceTrierParPrixASC(String piece)
    {
        return produitDao.getProduitsDisponiblesParPieceTrierParPrixASC(piece);
    }

    public LiveData<List<Produit>> getProduitsDisponiblesParPieceTrierParPrixDESC(String piece)
    {
        return produitDao.getProduitsDisponiblesParPieceTrierParPrixDESC(piece);
    }


    public LiveData<List<Produit>> getProduitsIndisponiblesParPieceTrierParNomASC(String piece)
    {
        return produitDao.getProduitsIndisponiblesParPieceTrierParNomASC(piece);
    }

    public LiveData<List<Produit>> getProduitsIndisponiblesParPieceTrierParNomDESC(String piece)
    {
        return produitDao.getProduitsIndisponiblesParPieceTrierParNomDESC(piece);
    }


    public LiveData<List<Produit>> getProduitsIndisponiblesParPieceTrierParRayonASC(String piece)
    {
        return produitDao.getProduitsIndisponiblesParPieceTrierParRayonASC(piece);
    }

    public LiveData<List<Produit>> getProduitsIndisponiblesParPieceTrierParRayonDESC(String piece)
    {
        return produitDao.getProduitsIndisponiblesParPieceTrierParRayonDESC(piece);
    }


    public LiveData<List<Produit>> getProduitsIndisponiblesParPieceTrierParDateASC(String piece)
    {
        return produitDao.getProduitsIndisponiblesParPieceTrierParDateASC(piece);
    }

    public LiveData<List<Produit>> getProduitsIndisponiblesParPieceTrierParDateDESC(String piece)
    {
        return produitDao.getProduitsIndisponiblesParPieceTrierParDateDESC(piece);
    }


    public LiveData<List<Produit>> getProduitsIndisponiblesParPieceTrierParPrixASC(String piece)
    {
        return produitDao.getProduitsIndisponiblesParPieceTrierParPrixASC(piece);
    }

    public LiveData<List<Produit>> getProduitsIndisponiblesParPieceTrierParPrixDESC(String piece)
    {
        return produitDao.getProduitsIndisponiblesParPieceTrierParPrixDESC(piece);
    }



    public void updateProduit(int id, int quantite){
        executorService.execute(() -> produitDao.updateQuantityById(id, quantite));
    }


}
