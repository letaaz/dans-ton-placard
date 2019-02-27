package com.danstonplacard.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.danstonplacard.database.model.ProduitDefaut;
import com.danstonplacard.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * ArrayAdapter use to found default product in searchBar
 */
public class SearchItemArrayAdapter extends ArrayAdapter<ProduitDefaut> {
    private static final String TAG = "SearchItemArrayAdapter";

    private Context context;

    private List<ProduitDefaut> dataListAllItems;
    private List<ProduitDefaut> produitsDefautsList;

    private ListFilter listFilter = new ListFilter();

    /**
     * @param context
     * @param textViewResourceId
     * @param objects
     */
    public SearchItemArrayAdapter(Context context, int textViewResourceId, List<ProduitDefaut> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        produitsDefautsList = objects;
    }

    @Override
    public int getCount() {
        return this.produitsDefautsList.size();
    }

    @Override
    public ProduitDefaut getItem(int position) {
        ProduitDefaut produit = this.produitsDefautsList.get(position);
        return produit;
    }

    public void addProduct(ProduitDefaut produitDefaut)
    {
        produitsDefautsList.add(produitDefaut);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater inflater = LayoutInflater.from(getContext());

        if (row == null) {
            row = inflater.inflate(R.layout.search_listitem, parent, false);
        }

        ProduitDefaut produit = this.produitsDefautsList.get(position);
        String nom = produit.getNom();
        TextView nomProduit = (TextView) row.findViewById(R.id.nomProduit);
        nomProduit.setText(nom);

        // Get a reference to ImageView holder
        ImageView iconeProduit = (ImageView) row.findViewById(R.id.iconeProduit);

        // Set image Product
        setImageProduct(produit, iconeProduit);

        return row;
    }

    /**
     * Method called to set image to product in searchBar
     * @param produit
     * @param iconeProduit
     */
    private void setImageProduct(ProduitDefaut produit, ImageView iconeProduit) {
        if (produit.getUrl_image().isEmpty()) {
            iconeProduit.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_barcode));
        } else {
            InputStream is;
            try {
                is = context.getAssets().open("icons_products/" + produit.getUrl_image());
                Drawable draw = Drawable.createFromStream(is, null);
                iconeProduit.setImageDrawable(draw);

            } catch (IOException e) {
                e.printStackTrace();
                iconeProduit.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_barcode));
            }
        }
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return listFilter;
    }

    public class ListFilter extends Filter {
        private Object lock = new Object();

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();
            if (dataListAllItems == null) {
                synchronized (lock) {
                    dataListAllItems = new ArrayList<>(produitsDefautsList);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                synchronized (lock) {
                    results.values = dataListAllItems;
                    results.count = dataListAllItems.size();
                }
            } else {
                final String searchStrLowerCase = prefix.toString().toLowerCase();

                ArrayList<ProduitDefaut> matchValues = new ArrayList<>();

                for (ProduitDefaut dataItem : dataListAllItems) {
                    if (dataItem.getNom().toLowerCase().startsWith(searchStrLowerCase)) {
                        matchValues.add(dataItem);
                    }
                }

                results.values = matchValues;
                results.count = matchValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.values != null) {
                produitsDefautsList = (ArrayList<ProduitDefaut>) results.values;
            } else {
                produitsDefautsList = null;
            }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

    }
}


