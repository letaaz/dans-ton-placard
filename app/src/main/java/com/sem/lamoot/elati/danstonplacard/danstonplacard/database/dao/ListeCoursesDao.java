package com.sem.lamoot.elati.danstonplacard.danstonplacard.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Ingredient;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.ListeCourses;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Produit;

import java.util.List;

@Dao
public interface ListeCoursesDao {

    @Insert
    long insert(ListeCourses listeCourses);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateListe(ListeCourses listeCourses);

    @Query("DELETE from liste_de_courses WHERE id = :id")
    void deleteListeCoursesById(int id);

    @Query("SELECT * FROM liste_de_courses")
    List<ListeCourses> getAllListeCourses();

//    @Query("SELECT * FROM liste_de_courses WHERE etat = 1")
//    List<ListeCourses> getAllListesCoursesArchivees();
//
//    @Query("SELECT * FROM liste_de_courses WHERE etat = 0")
//    List<ListeCourses> getAllListesCoursesDisponibles();

    @Query("SELECT * FROM liste_de_courses WHERE id = :id")
    ListeCourses getListeCoursesById(int id);


    @Query("SELECT * FROM liste_de_courses WHERE etat = 1")
    LiveData<List<ListeCourses>> getAllListesCoursesArchivees();

    @Query("SELECT * FROM liste_de_courses WHERE etat = 0")
    LiveData<List<ListeCourses>> getAllListesCoursesDisponibles();

    @Query("SELECT * FROM liste_de_courses WHERE id = :idLDC")
    LiveData<ListeCourses> getListeCoursesByIdLD(int idLDC);


//
//    @Query("SELECT produitsAPrendre FROM liste_de_courses WHERE id = :idLDC")
//    LiveData<List<Produit>> getProduitsAPrendreByIdLDC(int idLDC);


}
