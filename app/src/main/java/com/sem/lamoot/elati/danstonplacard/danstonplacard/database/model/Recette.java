package com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.converter.DateTypeConverter;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.converter.IngredientTypeConverter;

import java.util.Date;
import java.util.List;

import io.reactivex.annotations.NonNull;

@Entity(tableName = "recette")
public class Recette {

    @PrimaryKey
    @NonNull
    private int id;

    private String nom;

    private int etat; // 1 pour archivé, 0 pour suggéré

    @ColumnInfo(name = "date_creation")
    @TypeConverters(DateTypeConverter.class)
    private Date dateCreation;

    @TypeConverters(IngredientTypeConverter.class)
    private List<Ingredient> ingredients;

    private String preparation;

    private int note; // de 5 à 1

    private String image_url;

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

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public String getPreparation() {
        return preparation;
    }

    public void setPreparation(String preparation) {
        this.preparation = preparation;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setId(int id) {
        this.id = id;
    }
}
