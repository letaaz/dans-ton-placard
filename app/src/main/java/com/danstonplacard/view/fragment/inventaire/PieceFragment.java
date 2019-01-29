package com.danstonplacard.view.fragment.inventaire;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.danstonplacard.database.model.Produit;
import com.danstonplacard.view.fragment.AjouterProduitFragment;
import com.danstonplacard.view.fragment.DetailProduitFragment;
import com.danstonplacard.viewmodel.ProduitViewModel;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.danstonplacard.R;

/**
 * Fragment that contains the available and unavailable products depending on the selected room
 */
public class PieceFragment extends Fragment
        implements ProduitAdapter.OnMinusImageViewClickListener, ProduitAdapter.OnAddImageViewClickListener,
        ProduitAdapter.OnProductItemClickListener {

    public static String ARG_PIECE = "";
    private Context mContext = null;
    private String mParam = null;
    private String mPiece = null;
    private String colonneTri = "Nom";
    private String trierPar = "[A-Z]";
    private int checkedItemSort = 0;

    // RecyclerView + Adapter - produits disponibles
    private RecyclerView produitsDisponiblesRecyclerView;
    private ProduitAdapter produitsDisponiblesAdapter;


    // RecyclerView + Adapter - produits indisponibles
    private RecyclerView produitsIndisponiblesRecyclerView;
    private ProduitAdapter produitsIndisponiblesAdapter;
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
    public void onResume() {
        super.onResume();
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(mContext);
        firebaseAnalytics.setCurrentScreen(this.getActivity(), this.getClass().getSimpleName(), this.getClass().getSimpleName());

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.piece_fragment, container, false);
        this.view = view;

        // Set RecyclerView + Set Datas (Products Availables and unavailables)
        produitViewModel = ViewModelProviders.of(this).get(ProduitViewModel.class);
        setProduitsDisponibles(produitViewModel, colonneTri, trierPar);
        setProduitsIndisponibles(produitViewModel, colonneTri, trierPar);

        TextView textViewPiece = view.findViewById(R.id.piece_name_piecefragment);
        textViewPiece.setText("" + getPiece(mPiece));

        RelativeLayout section_dispo = view.findViewById(R.id.section_produits_dispo);
        ImageView btn_hide_show_available_product = view.findViewById(R.id.section_show_all_button_dispo);
        RelativeLayout section_indispo = view.findViewById(R.id.section_produits_indispo);
        ImageView btn_hide_show_unavailable_product = view.findViewById(R.id.section_show_all_button_indispo);
        NestedScrollView nestedScrollView = view.findViewById(R.id.nestedScrollView);
        FloatingActionButton add_fab = view.findViewById(R.id.ajout_produit_fab);

        // Sortting products by something (name, ray, price or date)
        String[] listSortingBy = getResources().getStringArray(R.array.sort_by);

        TextView btn_sort_by = view.findViewById(R.id.sort_by_btn);
        btn_sort_by.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                mBuilder.setTitle("Trier par");

                mBuilder.setSingleChoiceItems(listSortingBy, checkedItemSort, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("dtp", "trier par : "+listSortingBy[i]);
                        checkedItemSort = i;
                        String[] parts = listSortingBy[checkedItemSort].split(" ");
                        Log.d("dtp", "Tri par "+colonneTri+" "+trierPar);
                        colonneTri = parts[0];
                        trierPar = parts[1];
                        setProduitsDisponibles(produitViewModel, colonneTri, trierPar);
                        setProduitsIndisponibles(produitViewModel, colonneTri, trierPar);
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

        // Implementation of the behavior to close down / reduce the list of available products
        setOnClickOnBtnShowAllProduitsDispos(section_dispo, btn_hide_show_available_product);

        // Setting up the behavior to close down / reduce the list of unavailable products
        setOnClickOnBtnShowAllProduitsIndispos(section_indispo, btn_hide_show_unavailable_product);

        // Setting behavior to floating button
        setOnClickToFloatingButton(add_fab);


        // Hide floating button when scroll in list of product
        hideFloatingButton(nestedScrollView, add_fab);

        return view;
    }

    private String getPiece(String mPiece) {
            switch(mPiece){
                case "CUISINE":
                    return "Cuisine";
                case "SALLE_DE_BAIN":
                    return "Salle de bain";
                case "CAVE":
                    return "Cave";
                case "GARAGE":
                    return "Garage";
                case "SALLE_A_MANGER":
                    return "Séjour";
                case "CHAMBRE":
                    return "Chambre";
                case "DIVERS":
                    return "Divers";
                default:
                    return "Divers";
            }
    }

    /**
     * Hides the floating button when the user moves down the list -
     * prevents the button from being above the (-) and (+) buttons of a product
     * @param nestedScrollView
     * @param add_fab
     */
    private void hideFloatingButton(NestedScrollView nestedScrollView, FloatingActionButton add_fab){
        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY > oldScrollY) {
                add_fab.hide();
            } else {
                add_fab.show();
            }
        });
    }







    /**
     * Set onClick Behavor for Floating Button
     * Open new Fragment - AjouterProduitFragment to add new Product
     * @param add_fab
     */
    private void setOnClickToFloatingButton(FloatingActionButton add_fab) {
        add_fab.setOnClickListener(view1 -> {
            // Launch the view for adding a product to the current piece
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.root_inventaire_frame, AjouterProduitFragment.newInstance(mPiece));
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.addToBackStack(null);
            transaction.commit();
        });
    }

    /**
     *
     * @param section_indispo
     * @param btn_hide_show_unavailable_product
     */
    private void setOnClickOnBtnShowAllProduitsIndispos(RelativeLayout section_indispo, ImageView btn_hide_show_unavailable_product) {
        section_indispo.setOnClickListener(v -> {
            if (produitsIndisponiblesRecyclerView.getVisibility() == View.GONE) {
                btn_hide_show_unavailable_product.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.drag_list_down_dark));
                produitsIndisponiblesRecyclerView.setVisibility(View.VISIBLE);
            } else {
                btn_hide_show_unavailable_product.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.drag_list_up_dark));
                produitsIndisponiblesRecyclerView.setVisibility(View.GONE);
            }
        });

    }

    /**
     *
     * @param section_dispo
     * @param btn_hide_show_available_product
     */
    private void setOnClickOnBtnShowAllProduitsDispos(RelativeLayout section_dispo, ImageView btn_hide_show_available_product) {
        section_dispo.setOnClickListener(v -> {
            if (produitsDisponiblesRecyclerView.getVisibility() == View.GONE) {
                btn_hide_show_available_product.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.drag_list_down_dark));
                produitsDisponiblesRecyclerView.setVisibility(View.VISIBLE);
            } else {
                btn_hide_show_available_product.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.drag_list_up_dark));
                produitsDisponiblesRecyclerView.setVisibility(View.GONE);
            }
        });
    }



    /**
     * Set Produits Disponibles in RecyclerView
     * @param produitViewModel
     */
    private void setProduitsDisponibles(ProduitViewModel produitViewModel, String colonne, String trierPar) {
        produitViewModel.getProduitsDisponiblesTrierPar(mParam, colonne, trierPar).observe(this, produits -> produitsDisponiblesAdapter.setData(produits));
        setRecyclerViewProduitsDisponibles();
    }

    /**
     * Set RecyclerView of Produits Disponibles
     */
    private void setRecyclerViewProduitsDisponibles() {
        // Set recyclerView + Adapter - Produits disponibles
        produitsDisponiblesAdapter = new ProduitAdapter(this.mContext, this, this, -1);
        produitsDisponiblesAdapter.setOnProductItemClickListener(this::onProductItemClickListener);
        produitsDisponiblesRecyclerView = (RecyclerView) view.findViewById(R.id.inventaireDispo_recyclerview);
        produitsDisponiblesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        produitsDisponiblesRecyclerView.setAdapter(produitsDisponiblesAdapter);

        RecyclerView.LayoutManager produitsDisponiblesLayoutManager = new LinearLayoutManager(getActivity());
        produitsDisponiblesRecyclerView.setLayoutManager(produitsDisponiblesLayoutManager);
        produitsDisponiblesRecyclerView.setNestedScrollingEnabled(false);
    }


    /**
     * Set Products Indisponibles in RecyclerView
     * @param produitViewModel
     */
    private void setProduitsIndisponibles(ProduitViewModel produitViewModel, String colonne, String trierPar) {
        produitViewModel.getProduitsIndisponiblesTrierPar(mParam, colonne, trierPar).observe(this, produits_indispos -> produitsIndisponiblesAdapter.setData(produits_indispos));
        setRecyclerViewProduitsIndisponibles();
    }

    /**
     * Set RecyclerView of Produits Indisponibles
     */
    private void setRecyclerViewProduitsIndisponibles() {
        // Set recyclerView + Adapter - Produits indisponibles
        produitsIndisponiblesAdapter = new ProduitAdapter(this.mContext, this, this, -1);
        produitsIndisponiblesAdapter.setOnProductItemClickListener(this::onProductItemClickListener);
        produitsIndisponiblesRecyclerView = (RecyclerView) view.findViewById(R.id.inventaireIndispo_recyclerview);
        produitsIndisponiblesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        produitsIndisponiblesRecyclerView.setAdapter(produitsIndisponiblesAdapter);

        RecyclerView.LayoutManager produitsIndisponiblesLayoutManager = new LinearLayoutManager(getActivity());
        produitsIndisponiblesRecyclerView.setLayoutManager(produitsIndisponiblesLayoutManager);
        produitsIndisponiblesRecyclerView.setNestedScrollingEnabled(false);
    }


    /**
     * Method called when (-) button on a product is clicked
     * @param produit
     */
    @Override
    public void onMinusImageViewClickListener(Produit produit) {
        if(produit.getQuantite() > 0)
            produitViewModel.updateProduit(produit.getId(), produit.getQuantite() - 1);
            setProduitsDisponibles(produitViewModel, colonneTri, trierPar);
            setProduitsIndisponibles(produitViewModel, colonneTri, trierPar);

    }

    /**
     * Method called when (+) button on a product is clicked
     * @param produit
     */
    @Override
    public void onAddImageViewClickListener(Produit produit) {
        produitViewModel.updateProduit(produit.getId(), produit.getQuantite() + 1);
        setProduitsDisponibles(produitViewModel, colonneTri, trierPar);
        setProduitsIndisponibles(produitViewModel, colonneTri, trierPar);
    }

    /**
     * Method called when clicking on a product - Opens DetailProduitFragment
     * @param produit
     */
    @Override
    public void onProductItemClickListener(Produit produit) {
        // Launch the view for product's detail
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        String[] params = new String[]{produit.getId()+"", "-1"};
        transaction.replace(R.id.root_inventaire_frame, DetailProduitFragment.newInstance(params));
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
