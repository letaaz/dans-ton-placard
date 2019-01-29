package com.danstonplacard.view.fragment.ldc;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.danstonplacard.database.RoomDB;
import com.danstonplacard.database.dao.ListeCoursesDao;
import com.danstonplacard.database.model.ListeCourses;
import com.danstonplacard.database.model.Produit;
import com.danstonplacard.database.model.Rayon;
import com.danstonplacard.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapter that displays the products in the detail of a shopping list
 */
public class LDCProductAdapter extends RecyclerView.Adapter<LDCProductAdapter.LDCProductViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Produit> data;
    private int idLDC;
    private boolean aPrendre;

    public LDCProductAdapter(Context context, int idLDC, boolean aPrendre){
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.data = new ArrayList<>();
        this.idLDC = idLDC;
        this.aPrendre = aPrendre;
    }

    @NonNull
    @Override
    public LDCProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = mInflater.inflate(R.layout.ldc_product_item, viewGroup, false);
        return new LDCProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LDCProductViewHolder ldcProductViewHolder, int i) {
        ldcProductViewHolder.bind(data.get(i));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData (List<Produit> newData) {
        if(data != null)
        {
            LDCProductAdapterDiffCallback ldcProductDiffCallback = new LDCProductAdapterDiffCallback(data, newData);
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(ldcProductDiffCallback);

            data.clear();
            data.addAll(newData);
            diffResult.dispatchUpdatesTo(this);
        }
        else{
            data = newData;
        }
    }

    public class LDCProductViewHolder extends RecyclerView.ViewHolder {

        private TextView ldc_product_name, ldc_product_quantity, ldc_product_price, ldc_product_price_label, id_product_ldc_item, ldc_product_quantity_label;
        private CheckBox ldc_product_checked;
        private ImageView ldc_product_image;

        public LDCProductViewHolder(@NonNull View itemView) {
            super(itemView);

            // Get DAO
            ListeCoursesDao listeCoursesDao = RoomDB.getDatabase(mContext).listeCoursesDao();
            // Get LDC
            ListeCourses li = listeCoursesDao.getListeCoursesById(idLDC);

            // Get items from Layout
            ldc_product_name = itemView.findViewById(R.id.ldc_product_name);
            ldc_product_quantity = itemView.findViewById(R.id.ldc_product_quantity);
            ldc_product_price = itemView.findViewById(R.id.prix_produit);
            ldc_product_image = itemView.findViewById(R.id.ldc_product_image);
            ldc_product_price_label = itemView.findViewById(R.id.ldc_product_price_label);
            id_product_ldc_item = itemView.findViewById(R.id.id_product_ldc_item);
            ldc_product_checked = itemView.findViewById(R.id.ldc_product_checked);
            ldc_product_quantity_label = itemView.findViewById(R.id.ldc_product_quantity_label);

            // If LDC archived - Product can't be checked
            if(li.getEtat() == 1){
                ldc_product_checked.setEnabled(false);
            }

            setCheckedChangeListenerOnProduct();
        }

        private void setCheckedChangeListenerOnProduct() {
            ldc_product_checked.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // Get DAO
                ListeCoursesDao ldcDao = RoomDB.getDatabase(mContext).listeCoursesDao();

                // Get LDC
                ListeCourses ldc = ldcDao.getListeCoursesById(idLDC);
                // Gets Lists of Products (Take and Taken)
                List<Produit> produitsAprendre = ldc.getProduitsAPrendre();
                List<Produit> produitsPris = ldc.getProduitsPris();

                if(isChecked && aPrendre) { // Product will be move to "estPris"
                    for(Produit produit : produitsAprendre) {
                        if(produit.getId() == Integer.parseInt(id_product_ldc_item.getText().toString())) {
                            produitsAprendre.remove(produit);
                            produitsPris.add(produit);
                            ldc.setProduitsPris(produitsPris);
                            ldc.setProduitsAPrendre(produitsAprendre);
                            ldcDao.updateListe(ldc);
                            break;
                        }
                    }
                }
                else if(!isChecked && !aPrendre) { // Product will be move to "aPrendre"
                    for(Produit produit : produitsPris) {
                        if (produit.getId() == Integer.parseInt(id_product_ldc_item.getText().toString())) {
                            produitsPris.remove(produit);
                            produitsAprendre.add(produit);

                            ldc.setProduitsPris(produitsPris);
                            ldc.setProduitsAPrendre(produitsAprendre);

                            ldcDao.updateListe(ldc);
                            break;
                        }
                    }
                }
            });
        }

        public void bind(Produit product) {

            // Set background color - Represent color of Rayon
            itemView.setBackgroundColor(mContext.getResources().getColor(Rayon.getRayonColor(product.getRayon())));

            // Set Brand + name of product
            if(product.getMarque() != null) {
                ldc_product_name.setText(product.getNom()+ " - " +product.getMarque());
            }
            else {
                ldc_product_name.setText(product.getNom());
            }

            // Set quantity
            ldc_product_quantity.setText(product.getQuantite() + "");
            // Set price
            ldc_product_price.setText(product.getPrix() + " €");
            // Set id product
            id_product_ldc_item.setText(String.valueOf(product.getId()));


            if(aPrendre) { // If product in aPrendre - it can't be checked
                ldc_product_checked.setChecked(false);
            }
            else { // If product checked - Crossed product informations
                ldc_product_name.setPaintFlags(ldc_product_name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                ldc_product_price.setPaintFlags(ldc_product_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                ldc_product_price_label.setPaintFlags(ldc_product_price_label.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                ldc_product_quantity.setPaintFlags(ldc_product_quantity.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                ldc_product_quantity_label.setPaintFlags(ldc_product_quantity_label.getPaintFlags() |Paint.STRIKE_THRU_TEXT_FLAG);
                ldc_product_checked.setChecked(true);
            }

            // Set image of product
            setImageProduct(product);
        }

        /**
         * Method called to set image to product
         * @param product
         */
        private void setImageProduct(Produit product) {
            if(product.getUrlImage() != null) {
                if(product.getUrlImage().contains("http")) {
                    Glide.with(mContext).load(product.getUrlImage()).into(ldc_product_image);
                }
                else{
                    try {
                        InputStream is = mContext.getAssets().open("icons_products/"+product.getUrlImage());
                        Drawable draw = Drawable.createFromStream(is, null);
                        ldc_product_image.setImageDrawable(draw);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            else
                ldc_product_image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_barcode));
        }
    }

    private class LDCProductAdapterDiffCallback extends DiffUtil.Callback{
        private final List<Produit> oldLDC, newLDC;

        LDCProductAdapterDiffCallback(List<Produit> oldLDC, List<Produit> newLDC) {
            this.oldLDC = oldLDC;
            this.newLDC = newLDC;
        }

        @Override
        public int getOldListSize() {
            return oldLDC.size();
        }

        @Override
        public int getNewListSize() {
            return newLDC.size();
        }

        @Override
        public boolean areItemsTheSame(int i, int i1) {
            return oldLDC.get(i).getId() == newLDC.get(i1).getId();
        }

        @Override
        public boolean areContentsTheSame(int i, int i1) {
            return oldLDC.get(i).equals(newLDC.get(i1));
        }
    }

}
