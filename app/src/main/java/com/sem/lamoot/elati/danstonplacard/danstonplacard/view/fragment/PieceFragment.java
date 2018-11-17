package com.sem.lamoot.elati.danstonplacard.danstonplacard.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sem.lamoot.elati.danstonplacard.danstonplacard.R;

public class PieceFragment extends Fragment {

    public static String ARG_PIECE = "ARG_PIECE_INVENTAIRE";
    private Context mContext = null;
    private String mParam = null;

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
        View view = inflater.inflate(R.layout.piece_fragment, container, false);

        // Set recyclerView
        RecyclerView produits_recycleView = view.findViewById(R.id.produits_recyclerview);
        // Retrieve list of products from ProduitDao and populate recyclerViews adapter
        switch (mParam) {
            case "CUISINE":
                // ProduitDAO.getAllKitchenProduct()
                break;
            case "SALLE_DE_BAIN":
                // ProduitDAO.getAllBathroomProduct()
                break;
            case "SALLE_A_MANGER":
                // ProduitDAO.getAllDiningRoomProduct()
                break;
            case "CAVE":
                // ProduitDAO.getAllBasementProduct()
                break;
        }
            // onClickListener for the recyclerView item
                // Launch a detailed view of the clicked product

        // Listener for the FAB
        FloatingActionButton add_fab = view.findViewById(R.id.ajout_produit_fab);
        add_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch the view for adding a product to the current piece
                Toast.makeText(mContext, "Open add product form", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
