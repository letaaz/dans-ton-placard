package com.sem.lamoot.elati.danstonplacard.danstonplacard.view.fragment.ldc;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.sem.lamoot.elati.danstonplacard.danstonplacard.R;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Produit;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.view.fragment.AjouterProduitFragment;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.view.fragment.inventaire.ProduitAdapter;

public class LDCEditFragment extends Fragment
                implements ProduitAdapter.OnMinusImageViewClickListener, ProduitAdapter.OnAddImageViewClickListener {

    public static String ARG_LDC = "";
    private Context mContext;
    private Integer mLdc;

    private EditText ldcNameEdit;
    private ImageButton ldcSaveEdit;
    private RecyclerView ldcEditProductRecyclerView;
    private FloatingActionButton ldcEditAddProduct;

    public static Fragment newInstance(Integer param){
        Bundle args = new Bundle();
        args.putInt(ARG_LDC, param);
        LDCEditFragment ldcEditFragment = new LDCEditFragment();
        ldcEditFragment.setArguments(args);
        return ldcEditFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getContext();
        if (getArguments() != null) {
            mLdc = getArguments().getInt(ARG_LDC);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ldc_edit_fragment, container, false);

        if (mLdc.equals(LDCFragment.NEW_LDC))
            // Create empty ldc with no product and a default title

        ldcNameEdit = view.findViewById(R.id.ldc_edit_name);

        ldcSaveEdit = view.findViewById(R.id.btn_save_ldc_edit);
        ldcSaveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.root_ldc_frame, DetailLDCFragment.newInstance(mLdc));
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        ldcEditAddProduct = view.findViewById(R.id.ldc_edit_ajout_produit_fab);
        ldcEditAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the view for adding a product to the current piece
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.root_ldc_frame, AjouterProduitLDCFragment.newInstance(mLdc));
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        ldcEditProductRecyclerView = view.findViewById(R.id.ldc_product_edit_recyclerview);
        ProduitAdapter produitAdapter = new ProduitAdapter(mContext, this, this);
        ldcEditProductRecyclerView.setItemAnimator(new DefaultItemAnimator());
        ldcEditProductRecyclerView.setAdapter(produitAdapter);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(mContext);
        ldcEditProductRecyclerView.setLayoutManager(manager);
        ldcEditProductRecyclerView.setNestedScrollingEnabled(false);

        return view;
    }

    @Override
    public void onMinusImageViewClickListener(Produit produit) {
        // update view
    }

    @Override
    public void onAddImageViewClickListener(Produit produit) {
        // update view
    }
}
