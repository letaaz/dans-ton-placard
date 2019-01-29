package com.danstonplacard.database.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.danstonplacard.database.converter.DateTypeConverter;
import com.danstonplacard.database.converter.ProduitTypeConverter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.annotations.NonNull;

@Entity(tableName = "liste_de_courses")
public class ListeCourses {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    private String nom;

    private int etat; // 1 pour archivé, 0 pour en cours

    @ColumnInfo(name = "date_archive")
    @TypeConverters(DateTypeConverter.class)
    private Date dateArchive;

    @ColumnInfo(name = "date_creation")
    @TypeConverters(DateTypeConverter.class)
    private Date dateCreation;

    @TypeConverters(ProduitTypeConverter.class)
    private List<Produit> produitsPris;

    @TypeConverters(ProduitTypeConverter.class)
    private List<Produit> produitsAPrendre;

    public ListeCourses(String nom) {
        this.nom = nom;
        this.dateCreation = new Date();
        this.etat = 0;
        this.produitsAPrendre = new ArrayList<Produit>();
        this.produitsPris = new ArrayList<Produit>();
    }

    public ListeCourses(ListeCourses li)
    {
        this.nom = li.nom;
        this.etat = li.etat;
        this.dateArchive = li.dateArchive;
        this.dateCreation = li.dateCreation;
        this.produitsPris = li.produitsPris;
        this.produitsAPrendre = li.produitsAPrendre;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

    public Date getDateArchive() {
        return dateArchive;
    }

    public void setDateArchive(Date dateArchive) {
        this.dateArchive = dateArchive;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public List<Produit> getProduitsPris() {
        return produitsPris;
    }

    public void setProduitsPris(List<Produit> produitsPris) {
        this.produitsPris = produitsPris;
    }

    public List<Produit> getProduitsAPrendre() {
        return produitsAPrendre;
    }

    public void setProduitsAPrendre(List<Produit> produitsAPrendre) {
        this.produitsAPrendre = produitsAPrendre;
    }

    public void setId(int id) {
        this.id = id;
    }
}
