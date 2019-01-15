package com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model;

public class LdcProduitDefaut {

    private String nom;
    private float prix;
    private int quantite;

    public LdcProduitDefaut(String nom, float prix, int quantite) {
        this.nom = nom;
        this.prix = prix;
        this.quantite = quantite;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }
}
