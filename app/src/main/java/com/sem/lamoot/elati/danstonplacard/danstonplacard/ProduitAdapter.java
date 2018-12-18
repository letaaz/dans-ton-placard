package com.sem.lamoot.elati.danstonplacard.danstonplacard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.RoomDB;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.dao.ProduitDao;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Produit;

import java.util.ArrayList;
import java.util.List;

public class ProduitAdapter extends RecyclerView.Adapter<ProduitAdapter.ProduitViewHolder> {

    public interface OnMinusImageViewClickListener{
        void onMinusImageViewClickListener(Produit produit);
    }

    public interface OnAddImageViewClickListener {
        void onAddImageViewClickListener(Produit produit);
    }

    private List<Produit> data;
    private Context context;
    private LayoutInflater layoutInflater;
    private OnMinusImageViewClickListener onMinusImageViewClickListener;
    private OnAddImageViewClickListener onAddImageViewClickListener;


    public ProduitAdapter(Context context, OnMinusImageViewClickListener listener, OnAddImageViewClickListener addListener){
        this.data = new ArrayList<>();
        this.context = context;
        this.onMinusImageViewClickListener = listener;
        this.onAddImageViewClickListener = addListener;
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



    public class ProduitViewHolder extends RecyclerView.ViewHolder{

        private TextView nom_produit, quantite, id_produit;
        private ImageView retirerUnProduit, ajouterUnProduit;


        public ProduitViewHolder(View itemView) {
            super(itemView);

            nom_produit = (TextView) itemView.findViewById(R.id.produit);
            quantite = (TextView) itemView.findViewById(R.id.quantite_produit_textview);
            id_produit = (TextView) itemView.findViewById(R.id.id_product_txt);

            retirerUnProduit = (ImageView) itemView.findViewById(R.id.minus_button);
            ajouterUnProduit = (ImageView) itemView.findViewById(R.id.add_button);
        }

        public void bind(Produit produit) {
            if(produit != null)
            {
                nom_produit.setText(produit.getNom());
                quantite.setText("Quantité : " + String.valueOf(produit.getQuantite()));
                id_produit.setText(String.valueOf(produit.getId()));

                retirerUnProduit.setOnClickListener(v -> {
                    if (onMinusImageViewClickListener != null)
                        onMinusImageViewClickListener.onMinusImageViewClickListener(produit);
                });

                ajouterUnProduit.setOnClickListener(v -> {
                    if(onAddImageViewClickListener != null)
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
