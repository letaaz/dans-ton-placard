package com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.converter.DateTypeConverter;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.converter.PieceTypeConverter;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.converter.RayonTypeConverter;

import java.util.Date;

@Entity(tableName = "produit")
public class Produit {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    @NonNull
    private String nom;

    @NonNull
    private int quantite;

    private float poids;

    private String codeBarre;

    private String marque;

    private String urlImage;

    @Nullable
    @TypeConverters(DateTypeConverter.class)
    private Date dlc;

    @NonNull
    @TypeConverters(RayonTypeConverter.class)
    private Rayon rayon;

    private float prix;

    @NonNull
    @TypeConverters(PieceTypeConverter.class)
    private Piece piece;


    public Produit(@NonNull String nom, String codeBarre, String marque, String urlImage,@NonNull int quantite, float poids, Date dlc, @NonNull Rayon rayon, float prix, @NonNull Piece piece) {
        this.nom = nom;
        this.quantite = quantite;
        this.poids = poids;
        this.codeBarre = codeBarre;
        this.dlc = dlc;
        this.rayon = rayon;
        this.prix = prix;
        this.piece = piece;
        this.urlImage = urlImage;
        this.marque = marque;
    }

    @Ignore
    public Produit(String nom, int quantite, Rayon rayon, Piece piece)
    {
        this.nom = nom;
        this.quantite = quantite;
        this.rayon = rayon;
        this.piece = piece;
    }

    @Ignore
    public Produit(){}

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    @NonNull
    public String getNom() {
        return nom;
    }

    public void setNom(@NonNull String nom) {
        this.nom = nom;
    }

    @NonNull
    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(@NonNull int quantite) {
        this.quantite = quantite;
    }


    public String getCodeBarre() {
        return codeBarre;
    }

    public void setCodeBarre(String codeBarre) {
        this.codeBarre = codeBarre;
    }

    public float getPoids() {
        return poids;
    }

    public void setPoids(float poids) {
        this.poids = poids;
    }

    public Date getDlc() {
        return dlc;
    }

    public void setDlc(Date dlc) {
        this.dlc = dlc;
    }

    @NonNull
    public Rayon getRayon() {
        return rayon;
    }

    public void setRayon(@NonNull Rayon rayon) {
        this.rayon = rayon;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    @NonNull
    public Piece getPiece() {
        return piece;
    }

    public void setPiece(@NonNull Piece piece) {
        this.piece = piece;
    }
  
    @NonNull
    @Override
    public String toString() {
        return "Produit : { " + nom + " ; qt = " + quantite + " ; poids = "
            + poids + " ; dlc = " + dlc +" ; rayon = " + rayon + " ; piece = " + piece
            + " ; prix = " + prix + " }";
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}
