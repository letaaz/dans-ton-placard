package com.sem.lamoot.elati.danstonplacard.danstonplacard.view.fragment;

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
import android.widget.Toast;

import com.sem.lamoot.elati.danstonplacard.danstonplacard.ProduitAdapter;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.R;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Produit;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.viewmodel.ProduitViewModel;

public class PieceFragment extends Fragment implements ProduitAdapter.OnMinusImageViewClickListener, ProduitAdapter.OnAddImageViewClickListener {

    public static String ARG_PIECE = "";
    private Context mContext = null;
    private String mParam = null;

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
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.piece_fragment, container, false);
        Toast.makeText(this.getContext(), "mParam = " +mParam, Toast.LENGTH_SHORT).show();


        // Récupération des produits disponibles
        produitViewModel = ViewModelProviders.of(this).get(ProduitViewModel.class);
        produitViewModel.getProduitsDisponiblesParPiece(mParam).observe(this, produits -> produitsDisponiblesAdapter.setData(produits));
        produitViewModel.getProduitsIndisponiblesParPiece(mParam).observe(this, produits_indispos -> produitsIndisponiblesAdapter.setData(produits_indispos));

        // Set recyclerView + Adapter - Produits disponibles
        produitsDisponiblesAdapter = new ProduitAdapter(this.mContext, this, this);
        produitsDisponiblesRecyclerView = (RecyclerView) view.findViewById(R.id.inventaireDispo_recyclerview);
        produitsDisponiblesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        produitsDisponiblesRecyclerView.setAdapter(produitsDisponiblesAdapter);

        produitsDisponiblesLayoutManager = new LinearLayoutManager(getActivity());
        produitsDisponiblesRecyclerView.setLayoutManager(produitsDisponiblesLayoutManager);
        produitsDisponiblesRecyclerView.setNestedScrollingEnabled(false);

        // Set recyclerView + Adapter - Produits indisponibles
        produitsIndisponiblesAdapter = new ProduitAdapter(this.mContext, this, this);
        produitsIndisponiblesRecyclerView = (RecyclerView) view.findViewById(R.id.inventaireIndispo_recyclerview);
        produitsIndisponiblesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        produitsIndisponiblesRecyclerView.setAdapter(produitsIndisponiblesAdapter);

        produitsIndisponiblesLayoutManager = new LinearLayoutManager(getActivity());
        produitsIndisponiblesRecyclerView.setLayoutManager(produitsIndisponiblesLayoutManager);
        produitsIndisponiblesRecyclerView.setNestedScrollingEnabled(false);


        TextView btn = (TextView) view.findViewById(R.id.section_show_all_button_dispo);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int visibilite = ((produitsDisponiblesRecyclerView.getVisibility() == View.INVISIBLE) ? View.VISIBLE : View.INVISIBLE);
                produitsDisponiblesRecyclerView.setVisibility(visibilite);
            }
        });


        //TODO onClickListener for the recyclerView item
        // Launch a detailed view of the clicked product

        // Listener for the FAB
        FloatingActionButton add_fab = view.findViewById(R.id.ajout_produit_fab);
        add_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch the view for adding a product to the current piece
                Toast.makeText(mContext, "Open add product form", Toast.LENGTH_SHORT).show();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.root_frame, new AjouterProduitFragment());
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
}
