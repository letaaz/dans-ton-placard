package com.sem.lamoot.elati.danstonplacard.danstonplacard.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.sem.lamoot.elati.danstonplacard.danstonplacard.ProduitAdapter;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.R;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Piece;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Produit;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Rayon;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.viewmodel.DetailProduitViewModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;

import io.reactivex.annotations.NonNull;

public class DetailProduitFragment extends Fragment {

    final static String ARG_PROD = "";
    public final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyy");
    private DetailProduitViewModel detailProduitViewModel = null;

    Context mContext;
    private String mParam = null;
    private Produit mProduct;
    private String mPiece = null;

    // View
    ImageView produitImage;
    TextView produitNom, produitPoids;
    EditText produitQuantite, produitPrix, produitDlc;
    Spinner produitPiece, produitRayon;
    Button saveBtn;

    public static Fragment newInstance(String ...params){
        Bundle args = new Bundle();
        args.putStringArray(ARG_PROD, params);
        DetailProduitFragment detailProduitFragment = new DetailProduitFragment();
        detailProduitFragment.setArguments(args);
        return detailProduitFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mContext = this.getContext();
        if (getArguments() != null) {
            String[] args =  getArguments().getStringArray(ARG_PROD);
            mParam = args[0];
            mPiece = args[1];
        }
        detailProduitViewModel = ViewModelProviders.of(this).get(DetailProduitViewModel.class);
        detailProduitViewModel.getProduit(Integer.parseInt(mParam))
                .observe(this, result -> {
                    if (result != null) {
                        mProduct = result;
                        updateFields(result);
                    } else {
                        // Handle exception here
                    }
                });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.detail_produit_fragment, container, false);

        produitImage = view.findViewById(R.id.produit_image);
        produitNom = view.findViewById(R.id.produit_nom);
        produitPoids = view.findViewById(R.id.produit_poids);
        produitPiece = view.findViewById(R.id.produit_piece);
        produitRayon = view.findViewById(R.id.produit_rayon);
        produitQuantite = view.findViewById(R.id.produit_quantité);
        produitPrix = view.findViewById(R.id.produit_prix);
        produitDlc = view.findViewById(R.id.produit_dlc);

        saveBtn = view.findViewById(R.id.sauvegarde_product_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pieceItemName = getResources().getStringArray(R.array.pieces)[produitPiece.getSelectedItemPosition()];
                Piece piece = Piece.getPiece(pieceItemName);
                String rayonItemName = getResources().getStringArray(R.array.rayons)[produitRayon.getSelectedItemPosition()];
                Rayon rayon = Rayon.getRayon(rayonItemName);
                if (detailProduitViewModel.validateForm(produitQuantite.getText().toString(), produitPrix.getText().toString(),
                        produitDlc.getText().toString())) {
                    mProduct.setPiece(piece);
                    mProduct.setRayon(rayon);
                    mProduct.setQuantite(Integer.parseInt(produitQuantite.getText().toString()));
                    mProduct.setPrix(Float.parseFloat(produitPrix.getText().toString()));
                    try {
                        mProduct.setDlc(DATE_FORMAT.parse(produitDlc.getText().toString()));
                    } catch (ParseException e) {
                        // Should never be reached
                    }
                    detailProduitViewModel.updateProduct(mProduct);
                    getFragmentManager().popBackStack();
                }
                else
                    Toast.makeText(mContext, "Formulaire incorrect", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void updateFields(Produit produit){
        new ProduitAdapter.AsyncTaskLoadImage(produitImage).execute(produit.getUrlImage());
        produitNom.setText(produit.getNom());
        produitPoids.setText(produit.getPoids() + " g");
        int piece = produit.getPiece().ordinal();
        produitPiece.setSelection(piece);
        int rayon = produit.getRayon().ordinal();
        produitRayon.setSelection(rayon);
        produitQuantite.setText(produit.getQuantite() + "");
        produitPrix.setText(produit.getPrix() + "");
        produitDlc.setText(DATE_FORMAT.format(produit.getDlc()));
    }

}
