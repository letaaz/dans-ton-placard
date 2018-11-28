package com.sem.lamoot.elati.danstonplacard.danstonplacard;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.RoomDB;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.dao.ProduitDao;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.dao.ProduitDao_Impl;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Produit;

import org.w3c.dom.Text;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private final List<Produit> allProducts;



    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public RecyclerView.Adapter adapter;

        public final TextView produit;
        public final TextView quantite;
        public final TextView id_produit;

        public final ImageView removeProduct;
        public final ImageView addProduct;

        public MyViewHolder(View v, MyAdapter myAdapter)
        {
            super(v);

            this.adapter = myAdapter;

            produit = (TextView) v.findViewById(R.id.produit);
            quantite = (TextView) v.findViewById(R.id.quantite_produit_textview);
            id_produit = (TextView) v.findViewById(R.id.id_product_txt);

            removeProduct = (ImageView) v.findViewById(R.id.minus_button);
            addProduct = (ImageView) v.findViewById(R.id.add_button);
        }
    }

    public MyAdapter(List<Produit> allProducts) {
        this.allProducts = allProducts;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        final View view = inflater.inflate(R.layout.layout_liste_produits_inventaire, viewGroup, false);

        return new MyViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        myViewHolder.produit.setText(allProducts.get(i).getNom());
        myViewHolder.quantite.setText("Quantité : " + allProducts.get(i).getQuantite());
        myViewHolder.id_produit.setText(allProducts.get(i).getId() + "");

        myViewHolder.removeProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Remove product clicked", Toast.LENGTH_SHORT).show();
                // TODO Update quantity of product in DB
                int quantite_product = allProducts.get(i).getQuantite();
                if(quantite_product > 0) {
                    RoomDB.getDatabase(v.getContext()).produitDao().updateQuantityById(allProducts.get(i).getId(),
                            quantite_product - 1);
                    myViewHolder.adapter.notifyDataSetChanged();
                }

            }
        });

        myViewHolder.addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Add product clicked", Toast.LENGTH_SHORT).show();
                // TODO Update quantity of product in DB
                RoomDB.getDatabase(v.getContext()).produitDao().updateQuantityById(allProducts.get(i).getId(),
                        allProducts.get(i).getQuantite() + 1);
                myViewHolder.adapter.notifyDataSetChanged();

            }
        });


    }

    @Override
    public int getItemCount() {
        return allProducts.size();
    }


}
