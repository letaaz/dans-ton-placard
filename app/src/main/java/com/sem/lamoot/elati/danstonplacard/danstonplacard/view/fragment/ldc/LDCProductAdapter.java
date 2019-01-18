package com.sem.lamoot.elati.danstonplacard.danstonplacard.view.fragment.ldc;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.sem.lamoot.elati.danstonplacard.danstonplacard.AsyncTaskLoadImage;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.R;
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

    public LDCProductAdapter(Context context){
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.data = new ArrayList<>();
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

    public void setData (List<Produit> newData) { this.data = newData; }

    public class LDCProductViewHolder extends RecyclerView.ViewHolder {

        private TextView ldc_product_name, ldc_product_quantity, ldc_product_price;
        private CheckBox ldc_product_checked;
        private ImageView ldc_product_image;

        public LDCProductViewHolder(@NonNull View itemView) {
            super(itemView);

            ldc_product_name = itemView.findViewById(R.id.ldc_product_name);
            ldc_product_quantity = itemView.findViewById(R.id.ldc_product_quantity);
            ldc_product_price = itemView.findViewById(R.id.ldc_product_price);
            ldc_product_image = itemView.findViewById(R.id.ldc_product_image);

            ldc_product_checked = itemView.findViewById(R.id.ldc_product_checked);
            ldc_product_checked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        // Mettre ds la ldc
                        // Remettre les couleurs vives
                    } else {
                        // Mettre ds le caddie
                        // Grisé le contenu
                    }
                }
            });
        }

        public void bind(Produit product) {
            ldc_product_name.setText(product.getNom());
            ldc_product_quantity.setText(product.getQuantite() + "");
            ldc_product_price.setText(product.getPrix() + " €");


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
}