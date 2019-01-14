package com.sem.lamoot.elati.danstonplacard.danstonplacard.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sem.lamoot.elati.danstonplacard.danstonplacard.AsyncTaskLoadImage;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.R;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.RoomDB;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.dao.ProduitDao;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Produit;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ProduitAdapter extends RecyclerView.Adapter<ProduitAdapter.ProduitViewHolder> {

    public interface OnMinusImageViewClickListener{
        void onMinusImageViewClickListener(Produit produit);
    }

    public interface OnAddImageViewClickListener {
        void onAddImageViewClickListener(Produit produit);
    }

    public interface OnProductItemClickListener {
        void onProductItemClickListener(Produit produit);
    }

    private List<Produit> data;
    private Context context;
    private LayoutInflater layoutInflater;
    private OnMinusImageViewClickListener onMinusImageViewClickListener;
    private OnAddImageViewClickListener onAddImageViewClickListener;
    private OnProductItemClickListener onProductItemClickListener;


    public ProduitAdapter(Context context, OnMinusImageViewClickListener minusListener, OnAddImageViewClickListener addListener,
                          OnProductItemClickListener itemListener){
        this.data = new ArrayList<>();
        this.context = context;
        this.onMinusImageViewClickListener = minusListener;
        this.onAddImageViewClickListener = addListener;
        this.onProductItemClickListener = itemListener;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public ProduitViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        final View view = inflater.inflate(R.layout.layout_liste_produits_inventaire, viewGroup, false);

        return new ProduitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProduitViewHolder produitViewHolder, int i) {

        String alertMsg = this.context.getResources().getString(R.string.msgAlertDialogSupprimerProduit);
        String title = this.context.getResources().getString(R.string.titleAlterDialogSupprimerProduit);
        String positiveButtonTxt = this.context.getResources().getString(R.string.positiveButtonAlertDialogSupprimerProduit);
        String negativeButtonTxt = this.context.getResources().getString(R.string.negativeButtonAlertDialogSupprimerProduit);
        ProduitDao produitDao = RoomDB.getDatabase(produitViewHolder.itemView.getContext()).produitDao();

        produitViewHolder.bind(data.get(produitViewHolder.getAdapterPosition()));
        produitViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
                alertDialog.setTitle(title);

                alertDialog.setMessage(Html.fromHtml(alertMsg + " <b> " + data.get(i).getNom() + "</b>"));
                alertDialog.setNegativeButton(negativeButtonTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.setPositiveButton(positiveButtonTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        produitDao.deleteProductById(data.get(produitViewHolder.getAdapterPosition()).getId());
                    }
                });
                AlertDialog dialog = alertDialog.create();
                dialog.show();
                return true;
            }
        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Produit> newData)
    {
        if(data != null)
        {
            ProduitDiffCallback produitDiffCallback = new ProduitDiffCallback(data, newData);
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(produitDiffCallback);

            data.clear();
            data.addAll(newData);
            diffResult.dispatchUpdatesTo(this);
        }
        else{
            data = newData;
        }
    }


    public class ProduitViewHolder extends RecyclerView.ViewHolder {

        private TextView nom_produit, quantite, id_produit;
        private ImageView imageProduit, retirerUnProduit, ajouterUnProduit;
        private View item_produit;

        public ProduitViewHolder(View itemView) {
            super(itemView);

            nom_produit = itemView.findViewById(R.id.produit);
            quantite = itemView.findViewById(R.id.quantite_produit_textview);
            id_produit = itemView.findViewById(R.id.id_product_txt);

            imageProduit = itemView.findViewById(R.id.id_product_image);
            retirerUnProduit = itemView.findViewById(R.id.minus_button);
            ajouterUnProduit = itemView.findViewById(R.id.add_button);


            //item_produit = itemView.findViewById(R.id.item_product);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onProductItemClickListener != null)
                        onProductItemClickListener.onProductItemClickListener(data.get(getAdapterPosition()));
                }
            });
        }

        public void bind(Produit produit) {
            if (produit != null) {

                if(produit.getMarque() != null)
                    nom_produit.setText(produit.getMarque() + " - " + produit.getNom());
                else
                    nom_produit.setText(produit.getNom());

                quantite.setText("Quantité : " + String.valueOf(produit.getQuantite()));
                id_produit.setText(String.valueOf(produit.getId()));

                if(produit.getUrlImage() != null) {
                    if(produit.getUrlImage().contains("http")) {
                        try {
                            Bitmap bitmap = new AsyncTaskLoadImage().execute(produit.getUrlImage()).get();
                            imageProduit.setImageBitmap(bitmap);
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                            imageProduit.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_barcode));
                        }
                    }
                    else{
                        try {
                            InputStream is = context.getAssets().open("icons_products/"+produit.getUrlImage());
                            Drawable draw = Drawable.createFromStream(is, null);
                            imageProduit.setImageDrawable(draw);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else
                    imageProduit.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_barcode));

                retirerUnProduit.setOnClickListener(v -> {
                    if (onMinusImageViewClickListener != null)
                        onMinusImageViewClickListener.onMinusImageViewClickListener(produit);
                });

                ajouterUnProduit.setOnClickListener(v -> {
                    if (onAddImageViewClickListener != null)
                        onAddImageViewClickListener.onAddImageViewClickListener(produit);
                });
            }
        }

    }

    public class ProduitDiffCallback extends DiffUtil.Callback{

        private final List<Produit> oldProduits, newProduits;

        ProduitDiffCallback(List<Produit> oldProduits, List<Produit> newProduits) {
            this.oldProduits = oldProduits;
            this.newProduits = newProduits;
        }

        @Override
        public int getOldListSize() {
            return oldProduits.size();
        }

        @Override
        public int getNewListSize() {
            return newProduits.size();
        }

        @Override
        public boolean areItemsTheSame(int i, int i1) {
            return oldProduits.get(i).getId() == newProduits.get(i1).getId();
        }

        @Override
        public boolean areContentsTheSame(int i, int i1) {
            return oldProduits.get(i).equals(newProduits.get(i1));
        }
    }

}
