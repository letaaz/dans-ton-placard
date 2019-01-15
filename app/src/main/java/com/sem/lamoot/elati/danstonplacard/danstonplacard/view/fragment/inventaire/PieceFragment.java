package com.sem.lamoot.elati.danstonplacard.danstonplacard.view.fragment.inventaire;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sem.lamoot.elati.danstonplacard.danstonplacard.view.ProduitAdapter;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.R;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Produit;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.view.fragment.AjouterProduitFragment;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.view.fragment.DetailProduitFragment;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.viewmodel.ProduitViewModel;

public class PieceFragment extends Fragment
        implements ProduitAdapter.OnMinusImageViewClickListener, ProduitAdapter.OnAddImageViewClickListener,
        ProduitAdapter.OnProductItemClickListener {

    public static String ARG_PIECE = "";
    private Context mContext = null;
    private String mParam = null;
    private String mPiece = null;

    // RecyclerView + Adapter - produits disponibles
    private RecyclerView produitsDisponiblesRecyclerView;
    private ProduitAdapter produitsDisponiblesAdapter;
    private RecyclerView.LayoutManager produitsDisponiblesLayoutManager;


    // RecyclerView + Adapter - produits indisponibles
    private RecyclerView produitsIndisponiblesRecyclerView;
    private ProduitAdapter produitsIndisponiblesAdapter;
    private RecyclerView.LayoutManager produitsIndisponiblesLayoutManager;
    private ProduitViewModel produitViewModel;
    private ProduitViewModel produitViewModel2;


    public static Fragment newInstance(String param){
        Bundle args = new Bundle();
        args.putString(ARG_PIECE, param);
        args.putString("PIECE", param);
        PieceFragment pieceFragment = new PieceFragment();
        pieceFragment.setArguments(args);
        return pieceFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getContext();
        if (getArguments() != null) {
            mParam = getArguments().getString(ARG_PIECE);
            mPiece = getArguments().getString("PIECE");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.piece_fragment, container, false);

        // Récupération des produits disponibles
        produitViewModel = ViewModelProviders.of(this).get(ProduitViewModel.class);
        produitViewModel.getProduitsDisponiblesParPiece(mParam).observe(this, produits -> produitsDisponiblesAdapter.setData(produits));
        produitViewModel.getProduitsIndisponiblesParPiece(mParam).observe(this, produits_indispos -> produitsIndisponiblesAdapter.setData(produits_indispos));

        // Set recyclerView + Adapter - Produits disponibles
        produitsDisponiblesAdapter = new ProduitAdapter(this.mContext, this, this, this);
        produitsDisponiblesRecyclerView = (RecyclerView) view.findViewById(R.id.inventaireDispo_recyclerview);
        produitsDisponiblesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        produitsDisponiblesRecyclerView.setAdapter(produitsDisponiblesAdapter);

        produitsDisponiblesLayoutManager = new LinearLayoutManager(getActivity());
        produitsDisponiblesRecyclerView.setLayoutManager(produitsDisponiblesLayoutManager);
        produitsDisponiblesRecyclerView.setNestedScrollingEnabled(false);

        // Set recyclerView + Adapter - Produits indisponibles
        produitsIndisponiblesAdapter = new ProduitAdapter(this.mContext, this, this, this);
        produitsIndisponiblesRecyclerView = (RecyclerView) view.findViewById(R.id.inventaireIndispo_recyclerview);
        produitsIndisponiblesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        produitsIndisponiblesRecyclerView.setAdapter(produitsIndisponiblesAdapter);

        produitsIndisponiblesLayoutManager = new LinearLayoutManager(getActivity());
        produitsIndisponiblesRecyclerView.setLayoutManager(produitsIndisponiblesLayoutManager);
        produitsIndisponiblesRecyclerView.setNestedScrollingEnabled(false);


        TextView btn_hide_show_available_product = (TextView) view.findViewById(R.id.section_show_all_button_dispo);
        btn_hide_show_available_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int visibilite = ((produitsDisponiblesRecyclerView.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE);
                produitsDisponiblesRecyclerView.setVisibility(visibilite);
            }
        });

        TextView btn_hide_show_unavailable_product = view.findViewById(R.id.section_show_all_button_indispo);
        btn_hide_show_unavailable_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int visibilite = ((produitsIndisponiblesRecyclerView.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE);
                produitsIndisponiblesRecyclerView.setVisibility(visibilite);
            }
        });

        // Listener for the FAB
        FloatingActionButton add_fab = view.findViewById(R.id.ajout_produit_fab);
        add_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch the view for adding a product to the current piece
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.root_inventaire_frame, AjouterProduitFragment.newInstance(mPiece));
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }

    @Override
    public void onMinusImageViewClickListener(Produit produit) {
        if(produit.getQuantite() > 0)
            produitViewModel.updateProduit(produit.getId(), produit.getQuantite() - 1);
    }

    @Override
    public void onAddImageViewClickListener(Produit produit) {
        produitViewModel.updateProduit(produit.getId(), produit.getQuantite() + 1);
    }

    @Override
    public void onProductItemClickListener(Produit produit) {
        // Launch the view for product's detail
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        String[] params = new String[]{produit.getId()+"", mParam};
        transaction.replace(R.id.root_inventaire_frame, DetailProduitFragment.newInstance(params));
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
