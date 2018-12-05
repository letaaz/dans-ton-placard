package com.sem.lamoot.elati.danstonplacard.danstonplacard.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.sem.lamoot.elati.danstonplacard.danstonplacard.R;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Produit;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.viewmodel.DetailProduitViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import io.reactivex.annotations.NonNull;

public class DetailProduitFragment extends Fragment {

    final static String ARG_PROD = "";
    private DetailProduitViewModel detailProduitViewModel = null;

    Context mContext;
    private String mParam = null;
  //  private String mPiece = null;

    // View
    TextView produitNom, produitQuantite, produitPrix, produitDlc;
    Spinner produitPiece;
    Button saveBtn;

    public static Fragment newInstance(String param){
        Bundle args = new Bundle();
        args.putString(ARG_PROD, param);
        DetailProduitFragment detailProduitFragment = new DetailProduitFragment();
        detailProduitFragment.setArguments(args);
        return detailProduitFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mContext = this.getContext();
        if (getArguments() != null) {
            mParam = getArguments().getString(ARG_PROD);
      //      mPiece = getArguments().getString("PIECE");
        }
        detailProduitViewModel = ViewModelProviders.of(this).get(DetailProduitViewModel.class);
        detailProduitViewModel.getProduit(Integer.parseInt(mParam))
                .observe(this, this::updateFields);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.detail_produit_fragment, container, false);

        produitNom = view.findViewById(R.id.produit_nom);
        produitPiece = view.findViewById(R.id.produit_piece);
        produitQuantite = view.findViewById(R.id.produit_quantité);
        produitPrix = view.findViewById(R.id.produit_prix);
        produitDlc = view.findViewById(R.id.produit_dlc);

        saveBtn = view.findViewById(R.id.sauvegarde_product_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "A venir...", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void updateFields(Produit produit){
        produitNom.setText(produit.getNom());
        int piece = getItemPositionInSpinner(produit.getPiece().name());
        produitPiece.setSelection(piece);
        produitQuantite.setText(produit.getQuantite() + "");
        produitPrix.setText(produit.getPrix() + " €");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy");
        produitDlc.setText(dateFormat.format(produit.getDlc()));
    }

    private int getItemPositionInSpinner(String item){
        SpinnerAdapter mSpinnerAdapter = produitPiece.getAdapter();
        for (int i=0; i < mSpinnerAdapter.getCount(); i++) {
            if (mSpinnerAdapter.getItem(i).equals(item)) {
                return i;
            }
        }
        return 0;
    }
}
