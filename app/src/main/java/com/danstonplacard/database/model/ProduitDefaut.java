package com.danstonplacard.database.model;

public class ProduitDefaut
{
    private String nom;
    private String rayon;
    private String url_image;

    public ProduitDefaut(String nom, String rayon, String url_image) {
        this.nom = nom;
        this.rayon = rayon;
        this.url_image = url_image;
    }


    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getRayon() {
        return rayon;
    }

    public void setRayon(String rayon) {
        this.rayon = rayon;
    }

    public String getUrl_image() {
        return url_image;
    }

    public void setUrl_image(String url_image) {
        this.url_image = url_image;
    }

    public String toString()
    {
        return this.nom.substring(0,1).toUpperCase() + this.nom.substring(1);
    }
}