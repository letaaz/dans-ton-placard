package com.sem.lamoot.elati.danstonplacard.danstonplacard;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Produit;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private final Produit[] mDataSet;



    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public final TextView produit;
        public final TextView quantite;

        public MyViewHolder(View v)
        {
            super(v);

            produit = (TextView) v.findViewById(R.id.produit);
            quantite = (TextView) v.findViewById(R.id.quantite_produit_textview);

        }
    }

    public MyAdapter(Produit[] myDataSet) {
        this.mDataSet = myDataSet;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        TextView v = (TextView) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_liste_produits_inventaire, viewGroup, false);
//        MyViewHolder vh = new MyViewHolder(v);
//        return vh;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.layout_liste_produits_inventaire, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.produit.setText(mDataSet[i].getNom());
        myViewHolder.quantite.setText("Quantité : " + mDataSet[i].getQuantite());
    }

    @Override
    public int getItemCount() {
        return mDataSet.length;
    }


}
