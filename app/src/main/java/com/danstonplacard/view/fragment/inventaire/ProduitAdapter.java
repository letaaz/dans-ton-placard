package com.danstonplacard.view.fragment.inventaire;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.bumptech.glide.Glide;
import com.danstonplacard.database.RoomDB;
import com.danstonplacard.database.dao.ListeCoursesDao;
import com.danstonplacard.database.dao.ProduitDao;
import com.danstonplacard.database.model.ListeCourses;
import com.danstonplacard.database.model.Produit;
import com.danstonplacard.database.model.Rayon;
import com.danstonplacard.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.danstonplacard.view.fragment.ldc.LDCFragment.removeProductFromList;

public class ProduitAdapter extends RecyclerView.Adapter<ProduitAdapter.ProduitViewHolder> {

    private OnMinusImageViewClickListener onMinusImageViewClickListener;
    private OnAddImageViewClickListener onAddImageViewClickListener;
    private OnProductItemClickListener onProductItemClickListener;
    private List<Produit> data;
    private Context context;
    private LayoutInflater layoutInflater;
    private int idLDC;


    public interface OnMinusImageViewClickListener{
        void onMinusImageViewClickListener(Produit produit);
    }

    public interface OnAddImageViewClickListener {
        void onAddImageViewClickListener(Produit produit);
    }

    public interface OnProductItemClickListener {
        void onProductItemClickListener(Produit produit);
    }




    public ProduitAdapter(Context context, OnMinusImageViewClickListener minusListener, OnAddImageViewClickListener addListener, int idLDC){
        this.data = new ArrayList<>();
        this.context = context;
        this.onMinusImageViewClickListener = minusListener;
        this.onAddImageViewClickListener = addListener;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.idLDC = idLDC;
    }

    @NonNull
    @Override
    public ProduitViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        final View view = inflater.inflate(R.layout.produits_inventaire_item, viewGroup, false);

        return new ProduitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProduitViewHolder produitViewHolder, int i) {

        // Get DAO
        ProduitDao produitDao = RoomDB.getDatabase(produitViewHolder.itemView.getContext()).produitDao();
        ListeCoursesDao listeCoursesDao = RoomDB.getDatabase(context).listeCoursesDao();

        produitViewHolder.bind(data.get(produitViewHolder.getAdapterPosition()));


        setOnLongClickOnProduct(produitViewHolder, i, produitDao, listeCoursesDao);

    }

    /**
     * Called method when the user presses a product long
     * Allows users to remove the product from the list of available or unavailable products
     * @param produitViewHolder
     * @param i position of the item
     * @param produitDao
     * @param listeCoursesDao
     */
    private void setOnLongClickOnProduct(ProduitViewHolder produitViewHolder, int i, ProduitDao produitDao, ListeCoursesDao listeCoursesDao) {

        String alertMsg = this.context.getResources().getString(R.string.msgAlertDialogSupprimerProduit);
        String title = this.context.getResources().getString(R.string.titleAlterDialogSupprimerProduit);
        String positiveButtonTxt = this.context.getResources().getString(R.string.positiveButtonAlertDialogSupprimer);
        String negativeButtonTxt = this.context.getResources().getString(R.string.negativeButtonAlertDialogSupprimer);

        produitViewHolder.itemView.setOnLongClickListener(v -> {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
            alertDialog.setTitle(title);

            alertDialog.setMessage(Html.fromHtml(alertMsg + " <b> " + data.get(i).getNom() + "</b>"));
            alertDialog.setNegativeButton(negativeButtonTxt, (dialog, which) -> dialog.cancel());
            alertDialog.setPositiveButton(positiveButtonTxt, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if(idLDC == -1) {
                        produitDao.deleteProductById(data.get(produitViewHolder.getAdapterPosition()).getId());
                    }
                    else {
                        ListeCourses li = listeCoursesDao.getListeCoursesById(idLDC);
                        List<Produit> aPrendre = li.getProduitsAPrendre();
                        Produit produit = data.get(produitViewHolder.getAdapterPosition());
                        aPrendre = removeProductFromList(aPrendre,produit);
                        li.setProduitsAPrendre(aPrendre);
                        listeCoursesDao.updateListe(li);
                        produitDao.deleteProductById(produit.getId());
                    }
                }
            });
            AlertDialog dialog = alertDialog.create();
            dialog.show();
            return true;
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

    /**
     * Call when user click on product
     * @param onProductItemClickListener
     */
    public void setOnProductItemClickListener(OnProductItemClickListener onProductItemClickListener) {
        this.onProductItemClickListener = onProductItemClickListener;
    }

    public class ProduitViewHolder extends RecyclerView.ViewHolder {

        private TextView nom_produit, quantite, prix;
        private ImageView imageProduit, retirerUnProduit, ajouterUnProduit;
        private View container;

        public ProduitViewHolder(View itemView) {
            super(itemView);

            container = itemView;
            nom_produit = itemView.findViewById(R.id.inventaire_product_name);
            quantite = itemView.findViewById(R.id.inventaire_product_quantite);
            prix = itemView.findViewById(R.id.inventaire_product_price);

            imageProduit = itemView.findViewById(R.id.ldc_icon);
            retirerUnProduit = itemView.findViewById(R.id.minus_button);
            ajouterUnProduit = itemView.findViewById(R.id.add_button);

            itemView.setOnClickListener(v -> {
                if (onProductItemClickListener != null)
                    onProductItemClickListener.onProductItemClickListener(data.get(getAdapterPosition()));
            });
        }

        public void bind(Produit produit) {
            if (produit != null) {

                // Set background color - Represent Shelf of the Product
                container.setBackgroundColor(context.getResources().getColor(Rayon.getRayonColor(produit.getRayon())));

                // Set name of the product
                // Brand + Name or Name
                if(produit.getMarque() != null) {
                    nom_produit.setText(produit.getNom()+ " - " +produit.getMarque());
                }else {
                    nom_produit.setText(produit.getNom());
                }

                // Set Quantity
                quantite.setText(String.valueOf(produit.getQuantite()));

                // Set Price
                prix.setText(produit.getPrix() + " €");

                // Set Image
                setImageToProduct(produit);

                // Set method on (-) btn
                retirerUnProduit.setOnClickListener(v -> {
                    if (onMinusImageViewClickListener != null)
                        onMinusImageViewClickListener.onMinusImageViewClickListener(produit);
                });

                // Set method on (+) btn
                ajouterUnProduit.setOnClickListener(v -> {
                    if (onAddImageViewClickListener != null)
                        onAddImageViewClickListener.onAddImageViewClickListener(produit);
                });
            }
        }

        private void setImageToProduct(Produit produit) {
            if(produit.getUrlImage() == null || produit.getUrlImage().isEmpty()){
                imageProduit.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_barcode));
            }
            else{
                if(produit.getUrlImage().contains("http")) {
                    Glide.with(context).load(produit.getUrlImage()).into(imageProduit);
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
