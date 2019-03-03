package com.danstonplacard.view.fragment;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

import com.bumptech.glide.Glide;
import com.danstonplacard.R;
import com.danstonplacard.database.RoomDB;
import com.danstonplacard.database.converter.DateTypeConverter;
import com.danstonplacard.database.dao.ListeCoursesDao;
import com.danstonplacard.database.dao.ProduitDao;
import com.danstonplacard.database.model.ListeCourses;
import com.danstonplacard.database.model.Piece;
import com.danstonplacard.database.model.Produit;
import com.danstonplacard.database.model.Rayon;
import com.danstonplacard.viewmodel.DetailProduitViewModel;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.travijuu.numberpicker.library.NumberPicker;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * Fragment that displays the detail of a product
 */
public class DetailProduitFragment extends Fragment {

    final static String ARG_PROD = "";
    public final static int DATE_PICKER_REQUEST_CODE = 1;
    private DetailProduitViewModel detailProduitViewModel = null;
    private ListeCoursesDao listeCoursesDao;

    Context mContext;
    private String mParam = null;
    private Produit mProduct;
    private Date dlc;
    private int idLDC = -1;
    private int idxProdInAPrendre = -1;

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
            idLDC = Integer.valueOf(args[1]);
        }

        // Get DAO
        listeCoursesDao = RoomDB.getDatabase(mContext).listeCoursesDao();
        detailProduitViewModel = ViewModelProviders.of(this).get(DetailProduitViewModel.class);

        if(idLDC != -1) { // Called in LDC -
            ListeCourses li = listeCoursesDao.getListeCoursesById(idLDC);
            mProduct = foundProduitInListeAPrendre(li.getProduitsAPrendre(), Integer.parseInt(mParam));

        } else { // Called in Inventaire
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

    }

    @Override
    public void onResume() {
        super.onResume();
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(mContext);
        firebaseAnalytics.setCurrentScreen(this.getActivity(), this.getClass().getSimpleName(), this.getClass().getSimpleName());
    }

    /**
     * Method called to retrieve a product from the list of products to take from a shopping list
     * @param produitsAPrendre List of products to take
     * @param idProduit if of product to found
     * @return Product if product found - null otherwise
     */
    private Produit foundProduitInListeAPrendre(List<Produit> produitsAPrendre, int idProduit) {
        for(int i = 0; i < produitsAPrendre.size(); i++) {
            if(produitsAPrendre.get(i).getId() == idProduit){
                idxProdInAPrendre = i;
                return produitsAPrendre.get(i);}
        }
        return null;
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

        if(idLDC != -1) {
            updateFields(mProduct);
            OnClickListenerUpdateProductInLDC();
        } else {
            OnClickListenerUpdateProductInInventory();
        }
        return view;
    }

    /**
     * Method called when the user clicks the Save button from LDC
     */
    private void OnClickListenerUpdateProductInLDC() {
        saveBtn.setOnClickListener(view1 -> {
            String pieceItemName = getResources().getStringArray(R.array.pieces)[produitPiece.getSelectedItemPosition()];
            String rayonItemName = getResources().getStringArray(R.array.rayons)[produitRayon.getSelectedItemPosition()];

            // Get DAO
            ProduitDao produitDao = RoomDB.getDatabase(mContext).produitDao();

            // Get piece
            Piece piece = Piece.getPiece(pieceItemName);
            // Get Rayon
            Rayon rayon = Rayon.getRayon(rayonItemName);

            // Form controls ?
            // Change information of the product
            mProduct.setPiece(piece);
            mProduct.setRayon(rayon);
            mProduct.setQuantite(produitQuantite.getValue());
            mProduct.setPrix(Float.parseFloat(produitPrix.getText().toString()));
            mProduct.setDlc(dlc);

            /* Update product in LDC */
            ListeCourses li = listeCoursesDao.getListeCoursesById(idLDC);
            List<Produit> aPrendre = li.getProduitsAPrendre();
            aPrendre.remove(idxProdInAPrendre);
            aPrendre.add(mProduct);
            li.setProduitsAPrendre(aPrendre);
            listeCoursesDao.updateListe(li);

            /* Update product in inventory */
            Produit productFromInventaire = produitDao.findProductById(mProduct.getId());
            productFromInventaire.setPiece(mProduct.getPiece());
            productFromInventaire.setPrix(mProduct.getPrix());
            productFromInventaire.setRayon(mProduct.getRayon());
            productFromInventaire.setDlc(mProduct.getDlc());
            produitDao.updateProduct(productFromInventaire);


            getFragmentManager().popBackStack();
        });
    }

    /**
     * This method will update the product data that has been changed in the inventory.
     */
    private void OnClickListenerUpdateProductInInventory() {
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

            /* Update product in inventory */
            detailProduitViewModel.updateProduct(mProduct);
            /* Update product in LDC */
            updateProductInLDCAfterInventory();


            getFragmentManager().popBackStack();
        });
    }

    private void updateFields(Produit produit){

        int piece = produit.getPiece().ordinal();
        int rayon = produit.getRayon().ordinal();
        dlc = produit.getDlc();


        // Set image of product
        setImageProduct(produit);

        // Set brand - name of product
        if(produit.getMarque() != null) {
            produitNom.setText(produit.getNom()+ " - " +produit.getMarque());
        }
        else{
            produitNom.setText(produit.getNom());
        }

        // Set poids of product
        produitPoids.setText(produit.getPoids() + " g");
        // Set Piece of product
        produitPiece.setSelection(piece);
        // Set Rayon of product
        produitRayon.setSelection(rayon);
        // Set quantity of product
        produitQuantite.setValue(produit.getQuantite());
        // Set price of product
        produitPrix.setText(produit.getPrix() + "");
        // set DLC of product
        produitDlc.setText(DateTypeConverter.DATE_FORMATTER.format(produit.getDlc()));
    }

    /**
     * Called method to display the product image
     * @param produit
     */
    private void setImageProduct(Produit produit) {
        if(produit.getUrlImage() == null || produit.getUrlImage().isEmpty()){
            produitImage.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_barcode));
        }
        else{
            if(produit.getUrlImage().contains("http")) {
                Glide.with(mContext).load(produit.getUrlImage()).into(produitImage);
            }
            else {
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
    }

    /**
     * Method used to open a new fragment that allows you to select the product's expiration date
     * @param v
     */
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
                        String picked = data.getExtras().getString(MyDatePickerFragment.SELECTED_DATE);
                        dlc = DateTypeConverter.DATE_FORMATTER.parse(picked);
                        produitDlc.setText(DateTypeConverter.DATE_FORMATTER.format(dlc));
                    } catch (ParseException e) {
                        Toast.makeText(getActivity(), "Impossible de convertir la date", Toast.LENGTH_SHORT).show();
                    }
                }
        }
    }

    /**
     * After editing the information of a product in the inventory,
     * this method will also go to modify the corresponding product in the shopping list.
     */
    private void updateProductInLDCAfterInventory() {
        for(ListeCourses ldc : listeCoursesDao.getAllListeCourses()) {
            List<Produit> aPrendre = ldc.getProduitsAPrendre();
            List<Produit> estPris = ldc.getProduitsPris();
            for(int i = 0; i < aPrendre.size(); i++) {
                if(aPrendre.get(i).getId() == mProduct.getId()) {
                    aPrendre.remove(i);
                    aPrendre.add(mProduct);
                    ldc.setProduitsAPrendre(aPrendre);
                    listeCoursesDao.updateListe(ldc);
                }
            }
            for(int i = 0; i < estPris.size(); i++) {
                if(estPris.get(i).getId() == mProduct.getId()) {
                    estPris.remove(i);
                    estPris.add(mProduct);
                    ldc.setProduitsPris(estPris);
                    listeCoursesDao.updateListe(ldc);
                }
            }
        }
    }
}
