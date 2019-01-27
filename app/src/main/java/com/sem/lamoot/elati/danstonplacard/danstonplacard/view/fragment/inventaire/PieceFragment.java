package com.sem.lamoot.elati.danstonplacard.danstonplacard.view.fragment.inventaire;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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


    private View view;

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

        this.view = view;

        produitViewModel = ViewModelProviders.of(this).get(ProduitViewModel.class);
        setProduitsDisponibles(produitViewModel, "Nom");
        setProduitsIndisponibles(produitViewModel, "Nom");

        TextView btn = (TextView) view.findViewById(R.id.section_show_all_button_dispo);
        btn.setOnClickListener(new View.OnClickListener() {
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

        // Sortting by something
        String[] listSortingBy = getResources().getStringArray(R.array.sort_by);

        TextView btn_sort_by = view.findViewById(R.id.sort_by_btn);
        btn_sort_by.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                mBuilder.setTitle("Trier par");

                mBuilder.setSingleChoiceItems(listSortingBy, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("dtp", "trier par : "+listSortingBy[i]);
                        setProduitsDisponibles(produitViewModel, listSortingBy[i]);
                        setProduitsIndisponibles(produitViewModel, listSortingBy[i]);
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
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


    private void setProduitsDisponibles(ProduitViewModel produitViewModel, String trierPar) {

        switch(trierPar){
            case "Nom" :
                produitViewModel.getProduitsDisponiblesParPieceTrierParNom(mParam).observe(this, produits -> produitsDisponiblesAdapter.setData(produits));
                break;

            case"Rayon":
                produitViewModel.getProduitsDisponiblesParPieceTrierParRayon(mParam).observe(this, produits -> produitsDisponiblesAdapter.setData(produits));
                break;

            case"Date":
                produitViewModel.getProduitsDisponiblesParPieceTrierParDate(mParam).observe(this, produits -> produitsDisponiblesAdapter.setData(produits));
                break;

            case"Prix":
                produitViewModel.getProduitsDisponiblesParPieceTrierParPrix(mParam).observe(this, produits -> produitsDisponiblesAdapter.setData(produits));
                break;

            default:
                break;
        }
        setRecyclerViewProduitsDisponibles();
    }

    private void setRecyclerViewProduitsDisponibles() {
        // Set recyclerView + Adapter - Produits disponibles
        produitsDisponiblesAdapter = new ProduitAdapter(this.mContext, this, this);
        produitsDisponiblesAdapter.setOnProductItemClickListener(this::onProductItemClickListener);
        produitsDisponiblesRecyclerView = (RecyclerView) view.findViewById(R.id.inventaireDispo_recyclerview);
        produitsDisponiblesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        produitsDisponiblesRecyclerView.setAdapter(produitsDisponiblesAdapter);

        produitsDisponiblesLayoutManager = new LinearLayoutManager(getActivity());
        produitsDisponiblesRecyclerView.setLayoutManager(produitsDisponiblesLayoutManager);
        produitsDisponiblesRecyclerView.setNestedScrollingEnabled(false);
    }

    private void setProduitsIndisponibles(ProduitViewModel produitViewModel, String trierPar) {
        switch(trierPar){
            case "Nom" :
                produitViewModel.getProduitsIndisponiblesParPieceTrierParNom(mParam).observe(this, produits -> produitsDisponiblesAdapter.setData(produits));
                break;

            case"Rayon":
                produitViewModel.getProduitsIndisponiblesParPieceTrierParRayon(mParam).observe(this, produits_indispos -> produitsIndisponiblesAdapter.setData(produits_indispos));
                break;

            case"Date":
                produitViewModel.getProduitsIndisponiblesParPieceTrierParDate(mParam).observe(this, produits_indispos -> produitsIndisponiblesAdapter.setData(produits_indispos));
                break;

            case"Prix":
                produitViewModel.getProduitsIndisponiblesParPieceTrierParPrix(mParam).observe(this, produits_indispos -> produitsIndisponiblesAdapter.setData(produits_indispos));

                break;

            default:
                break;
        }
        setRecyclerViewProduitsIndisponibles();
    }

    private void setRecyclerViewProduitsIndisponibles() {
        // Set recyclerView + Adapter - Produits indisponibles
        produitsIndisponiblesAdapter = new ProduitAdapter(this.mContext, this, this);
        produitsIndisponiblesAdapter.setOnProductItemClickListener(this::onProductItemClickListener);
        produitsIndisponiblesRecyclerView = (RecyclerView) view.findViewById(R.id.inventaireIndispo_recyclerview);
        produitsIndisponiblesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        produitsIndisponiblesRecyclerView.setAdapter(produitsIndisponiblesAdapter);

        produitsIndisponiblesLayoutManager = new LinearLayoutManager(getActivity());
        produitsIndisponiblesRecyclerView.setLayoutManager(produitsIndisponiblesLayoutManager);
        produitsIndisponiblesRecyclerView.setNestedScrollingEnabled(false);
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
