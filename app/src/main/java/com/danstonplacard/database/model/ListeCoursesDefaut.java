package com.danstonplacard.database.model;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ListeCoursesDefaut {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;

    private String nom;

    private int etat; // 1 pour archivé, 0 pour en cours

    private Date dateArchive;

    private Date dateCreation;

    private List<Produit> produitsPris;

    private List<Produit> produitsAPrendre;

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

    public ListeCoursesDefaut(String name) {
        this.nom = name;
        this.id = 1;
        this.etat = 0;
        this.dateArchive = null;
        this.dateCreation = new Date();
        this.produitsAPrendre = Arrays.asList();
        this.produitsPris = Arrays.asList();
    }

}
