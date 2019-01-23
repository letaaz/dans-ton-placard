package com.sem.lamoot.elati.danstonplacard.danstonplacard.view.fragment.ldc;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sem.lamoot.elati.danstonplacard.danstonplacard.R;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.RoomDB;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.dao.ListeCoursesDao;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.dao.ProduitDao;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.dao.ProduitDao_Impl;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.ListeCourses;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Produit;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.viewmodel.ListeCoursesViewModel;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.viewmodel.ProduitViewModel;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.reactivex.annotations.NonNull;

public class DetailLDCFragment extends Fragment {

    final static String ARG_LDC = "ARG_LDC";
    private int idLDC;
    private Context mContext;

    private RecyclerView ldcProductRecyclerview, historyListRecyclerView;
    private ImageButton btnEditLdc, btnRecycleLdc;
    private RelativeLayout ldcLabel;

    private ListeCoursesDao listeCoursesDao;
    private ProduitDao produitDao;

    public static Fragment newInstance(int id) {
        Bundle args = new Bundle();
        args.putInt(ARG_LDC, id);
        DetailLDCFragment detailLdcFragment = new DetailLDCFragment();
        detailLdcFragment.setArguments(args);
        return detailLdcFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mContext = this.getContext();
        if (getArguments() != null) {
            idLDC = getArguments().getInt(ARG_LDC);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_ldc_fragment, container, false);


        ListeCoursesViewModel listeCoursesViewModel = ViewModelProviders.of(this).get(ListeCoursesViewModel.class);

        this.listeCoursesDao = RoomDB.getDatabase(mContext).listeCoursesDao();
        this.produitDao = RoomDB.getDatabase(mContext).produitDao();


        ListeCourses listeCourses = listeCoursesDao.getListeCoursesById(idLDC);
        float prix_total = 0;
        for(Produit produit : listeCourses.getProduitsPris())
        {
            prix_total += produit.getPrix();
        }
        for(Produit produit : listeCourses.getProduitsAPrendre())
        {
            prix_total += produit.getPrix();
        }

        ldcLabel = view.findViewById(R.id.ldc_label);
        TextView ldc_name_detail = view.findViewById(R.id.ldc_name_detail);
        ldc_name_detail.setText(listeCourses.getNom());

        TextView price_product_right = view.findViewById(R.id.price_product_right);
        price_product_right.setText(Float.toString(prix_total) + " €");


        ldcProductRecyclerview = view.findViewById(R.id.ldc_product_list_recyclerview);
        LDCProductAdapter ldcProductAdapter = new LDCProductAdapter(mContext, idLDC, true);

        listeCoursesViewModel.getListeCoursesByIdLD(idLDC).observe(this, listeCourses1 -> ldcProductAdapter.setData(listeCourses1.getProduitsAPrendre()));


        ldcProductRecyclerview.setAdapter(ldcProductAdapter);
        RecyclerView.LayoutManager ldcProductLayoutManager = new LinearLayoutManager(mContext);
        ldcProductRecyclerview.setLayoutManager(ldcProductLayoutManager);

        btnEditLdc = view.findViewById(R.id.btn_edit_ldc);
        btnRecycleLdc = view.findViewById(R.id.btn_archive_ldc);

        if(idLDC == 1){
            btnEditLdc.setVisibility(View.INVISIBLE);
        }
        if(listeCourses.getEtat() == 1)
        {
            btnEditLdc.setVisibility(View.INVISIBLE);
            btnRecycleLdc.setVisibility(View.INVISIBLE);
        }

        btnEditLdc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.root_ldc_frame, LDCEditFragment.newInstance(idLDC));
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        btnRecycleLdc.setOnClickListener(new View.OnClickListener() { // Archiver liste de courses
            @Override
            public void onClick(View v) {
                // Set archive field to 1

                if(listeCourses.getId() == 1)
                {
                    ListeCourses upListeCourse = listeCoursesDao.getListeCoursesById(1);
                    ListeCourses li = new ListeCourses(upListeCourse);
                    li.setEtat(1);

                    Date date = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    String strDate = formatter.format(date);

                    li.setDateArchive(date);
                    li.setNom("Liste des produits manquants du " + strDate);

                    listeCoursesDao.insert(li);

                    for(Produit produit : li.getProduitsPris())
                    {
                        produitDao.updateQuantityById(produit.getId(), 1);
                    }
                }
                else {
                    ListeCourses li = listeCoursesDao.getListeCoursesById(listeCourses.getId());

                    li.setEtat(1);
                    li.setDateArchive(new Date());
                    listeCoursesDao.updateListe(li);

                    for (Produit produit : li.getProduitsPris()) {
                        if(produit.getQuantite() != 0)
                            produitDao.updateQuantityById(produit.getId(), produit.getQuantite());
                        else
                            produitDao.updateQuantityById(produit.getId(), 1);

                    }
                }
                getFragmentManager().popBackStack();

            }
        });

        LinearLayout ldcBottomControl = view.findViewById(R.id.bottom_sheet);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(ldcBottomControl);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@android.support.annotation.NonNull View view, int newState) {
                ImageView arrow_up = ldcBottomControl.findViewById(R.id.view_show_history);
                ImageView arrow_down = ldcBottomControl.findViewById(R.id.view_show_ldc_list);
                ConstraintLayout peekLayout = ldcBottomControl.findViewById(R.id.bottom_sheet_control);
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    arrow_down.setVisibility(View.VISIBLE);
                    //peekLayout.setBackgroundColor(Color.DKGRAY);
                    peekLayout.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
                    ldcProductRecyclerview.setVisibility(View.GONE);
                    arrow_up.setVisibility(View.GONE);
                    ldcLabel.setVisibility(View.GONE);
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    arrow_up.setVisibility(View.VISIBLE);
                    ldcLabel.setVisibility(View.VISIBLE);
                    peekLayout.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
                    ldcProductRecyclerview.setVisibility(View.VISIBLE);
                    arrow_down.setVisibility(View.GONE);
                }
            }

            @Override
            public void onSlide(@android.support.annotation.NonNull View view, float v) { }
        });

        historyListRecyclerView = ldcBottomControl.findViewById(R.id.bottom_sheet_content_ldc_history_recyclerview);
        LDCProductAdapter historyAdapter = new LDCProductAdapter(mContext, idLDC, false);
        //historyAdapter.setData(listeCourses.getProduitsPris());
        listeCoursesViewModel.getListeCoursesByIdLD(idLDC).observe(this, listeCourses1 -> {
            List<Produit> estPris = listeCourses1.getProduitsPris();
            historyAdapter.setData(estPris);
            float prix_estPris = 0;
            for(Produit produit : estPris)
            {
                prix_estPris += produit.getPrix();
            }
            TextView price_caddie = view.findViewById(R.id.price_product_caddie);
            price_caddie.setText(Float.toString(prix_estPris));
        });

        historyListRecyclerView.setAdapter(historyAdapter);
        RecyclerView.LayoutManager historyLayoutManager = new LinearLayoutManager(mContext);
        historyListRecyclerView.setLayoutManager(historyLayoutManager);

        return view;
    }
}
