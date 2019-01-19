package com.sem.lamoot.elati.danstonplacard.danstonplacard.view.fragment.ldc;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sem.lamoot.elati.danstonplacard.danstonplacard.AsyncTaskLoadImage;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.R;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.RoomDB;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.dao.ListeCoursesDao;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.ListeCourses;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Produit;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

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

        private TextView ldc_product_name, ldc_product_quantity, ldc_product_price, ldc_product_price_label, id_product_ldc_item;
        private CheckBox ldc_product_checked;
        private ImageView ldc_product_image;

        public LDCProductViewHolder(@NonNull View itemView) {
            super(itemView);

            ldc_product_name = itemView.findViewById(R.id.ldc_product_name);
            ldc_product_quantity = itemView.findViewById(R.id.ldc_product_quantity);
            ldc_product_price = itemView.findViewById(R.id.ldc_product_price);
            ldc_product_image = itemView.findViewById(R.id.ldc_product_image);
            ldc_product_price_label = itemView.findViewById(R.id.ldc_product_price_label);
            id_product_ldc_item = itemView.findViewById(R.id.id_product_ldc_item);
            ldc_product_checked = itemView.findViewById(R.id.ldc_product_checked);

            ldc_product_checked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    ListeCoursesDao ldcDao = RoomDB.getDatabase(mContext).listeCoursesDao();
                    ListeCourses ldc = ldcDao.getListeCoursesById(idLDC);
                    List<Produit> produitsAprendre = ldc.getProduitsAPrendre();
                    List<Produit> produitsPris = ldc.getProduitsPris();

                    if(isChecked && aPrendre)
                    {
                        Toast.makeText(mContext, "Produit coché", Toast.LENGTH_SHORT).show();
                        for(Produit produit : produitsAprendre) {
                            if(produit.getId() == Integer.parseInt(id_product_ldc_item.getText().toString())) {
                                Toast.makeText(mContext, "inside", Toast.LENGTH_SHORT).show();
                                produitsAprendre.remove(produit);
                                produitsPris.add(produit);
                                ldc.setProduitsPris(produitsPris);
                                ldc.setProduitsAPrendre(produitsAprendre);
                                ldcDao.updateListe(ldc);
                                break;
                            }
                        }
                    }
                    else if(!isChecked && !aPrendre)
                    {
                        Toast.makeText(mContext, "Produit décoché", Toast.LENGTH_SHORT).show();
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
                }
            });
        }

        public void bind(Produit product) {
            ldc_product_name.setText(product.getNom());
            ldc_product_quantity.setText(product.getQuantite() + "");
            ldc_product_price.setText(product.getPrix() + " €");
            id_product_ldc_item.setText(String.valueOf(product.getId()));


            if(aPrendre)
            {
                ldc_product_checked.setChecked(false);
            }
            else
            {
                ldc_product_name.setPaintFlags(ldc_product_name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                ldc_product_price.setPaintFlags(ldc_product_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                ldc_product_price_label.setPaintFlags(ldc_product_price_label.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                ldc_product_checked.setChecked(true);
            }

            if(product.getUrlImage() != null) {
                if(product.getUrlImage().contains("http")) {
                    try {
                        Bitmap bitmap = new AsyncTaskLoadImage().execute(product.getUrlImage()).get();
                        ldc_product_image.setImageBitmap(bitmap);
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                        ldc_product_image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_barcode));
                    }
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
