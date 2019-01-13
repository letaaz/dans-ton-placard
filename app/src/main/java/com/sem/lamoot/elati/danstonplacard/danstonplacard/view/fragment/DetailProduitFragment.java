package com.sem.lamoot.elati.danstonplacard.danstonplacard.view.fragment;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sem.lamoot.elati.danstonplacard.danstonplacard.AsyncTaskLoadImage;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.R;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.converter.DateTypeConverter;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Piece;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Produit;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Rayon;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.viewmodel.DetailProduitViewModel;
import com.travijuu.numberpicker.library.NumberPicker;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import io.reactivex.annotations.NonNull;

public class DetailProduitFragment extends Fragment {

    final static String ARG_PROD = "";
    public final static int DATE_PICKER_REQUEST_CODE = 1;
    private DetailProduitViewModel detailProduitViewModel = null;

    Context mContext;
    private String mParam = null;
    private Produit mProduct;
    private Date dlc;
    private String mPiece = null;

    // View
    ImageView produitImage;
    TextView produitNom, produitPoids;
    EditText produitPrix;
    NumberPicker produitQuantite;
    Spinner produitPiece, produitRayon;
    Button saveBtn, produitDlc;

    ArrayAdapter piecesAdapter, rayonsAdapter;

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
                        Log.d("DETAIL_PRODUCT", "ERROR WHILE RETRIEVING PRODUCT FROM DATABASE ID := " + mParam);
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
        piecesAdapter = ArrayAdapter.createFromResource(mContext, R.array.pieces, R.layout.spinner_item);
        piecesAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        produitPiece.setAdapter(piecesAdapter);
        produitRayon = view.findViewById(R.id.produit_rayon);
        rayonsAdapter = ArrayAdapter.createFromResource(mContext, R.array.rayons, R.layout.spinner_item);
        rayonsAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        produitRayon.setAdapter(rayonsAdapter);
        produitQuantite = view.findViewById(R.id.produit_quantité);
        produitPrix = view.findViewById(R.id.produit_prix);
        produitDlc = view.findViewById(R.id.produit_dlc);
        produitDlc.setOnClickListener(this::showDatePicker);

        saveBtn = view.findViewById(R.id.sauvegarde_product_btn);
        saveBtn.setOnClickListener(view1 -> {
            String pieceItemName = getResources().getStringArray(R.array.pieces)[produitPiece.getSelectedItemPosition()];
            Piece piece = Piece.getPiece(pieceItemName);
            String rayonItemName = getResources().getStringArray(R.array.rayons)[produitRayon.getSelectedItemPosition()];
            Rayon rayon = Rayon.getRayon(rayonItemName);
            // Form controls ?
            mProduct.setPiece(piece);
            mProduct.setRayon(rayon);
            mProduct.setQuantite(produitQuantite.getValue());
            mProduct.setPrix(Float.parseFloat(produitPrix.getText().toString()));
            mProduct.setDlc(dlc);
            Log.d("DETAIL_PRODUCT", "update product's dlc with := " + dlc);
            detailProduitViewModel.updateProduct(mProduct);
            getFragmentManager().popBackStack();
        });

        return view;
    }

    private void updateFields(Produit produit){

        if(produit.getUrlImage().isEmpty()){
            produitImage.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_barcode));
        }
        else{
            if(produit.getUrlImage().contains("http"))
            {
                try {
                    Bitmap bitmap = new AsyncTaskLoadImage().execute(produit.getUrlImage()).get();
                    produitImage.setImageBitmap(bitmap);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                InputStream is;
                try {
                    is = getContext().getAssets().open("icons_products/"+produit.getUrlImage());
                    Drawable draw = Drawable.createFromStream(is, null);
                    produitImage.setImageDrawable(draw);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        produitNom.setText(produit.getNom());
        produitPoids.setText(produit.getPoids() + " g");
        int piece = produit.getPiece().ordinal();
        produitPiece.setSelection(piece);
        int rayon = produit.getRayon().ordinal();
        produitRayon.setSelection(rayon);
        produitQuantite.setValue(produit.getQuantite());
        produitPrix.setText(produit.getPrix() + "");
        dlc = produit.getDlc();
        produitDlc.setText(DateTypeConverter.DATE_FORMATTER.format(produit.getDlc()));
    }

    public void showDatePicker(View v) {
        DialogFragment newFragment = new MyDatePickerFragment();
        newFragment.setTargetFragment(this, DATE_PICKER_REQUEST_CODE);
        newFragment.show(getFragmentManager(), "date picker");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == DATE_PICKER_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK)
                if (data.getExtras().containsKey(MyDatePickerFragment.SELECTED_DATE)) {
                    try {
                        Log.d("DETAIL_PRODUCT", "data retrieved := " + data.getExtras().getString(MyDatePickerFragment.SELECTED_DATE));
                        String picked = data.getExtras().getString(MyDatePickerFragment.SELECTED_DATE);
                        dlc = DateTypeConverter.DATE_FORMATTER.parse(picked);
                        Log.d("DETAIL_PRODUCT", "date parsing worked := " + dlc.toString());
                        produitDlc.setText(DateTypeConverter.DATE_FORMATTER.format(dlc));
                    } catch (ParseException e) {
                        Toast.makeText(getActivity(), "Impossible de convertir la date", Toast.LENGTH_SHORT).show();
                    }
                }
        }
    }

}
