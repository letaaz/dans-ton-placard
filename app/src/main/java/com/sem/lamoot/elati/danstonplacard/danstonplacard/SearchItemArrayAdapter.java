package com.sem.lamoot.elati.danstonplacard.danstonplacard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SearchItemArrayAdapter extends ArrayAdapter<ProduitDefaut> {
        private static final String tag = "SearchItemArrayAdapter";
        private ProduitDefaut produit;
        private TextView nomProduit;
        private ImageView iconeProduit;
        private Context context;

        private List<ProduitDefaut> dataListAllItems;
        private List<ProduitDefaut> produitsDefautsList;



    private ListFilter listFilter = new ListFilter();

        /**
         *
         * @param context
         * @param textViewResourceId
         * @param objects
         */
        public SearchItemArrayAdapter(Context context, int textViewResourceId, List<ProduitDefaut> objects)
        {
            super(context, textViewResourceId, objects);
            this.context = context;
            produitsDefautsList = objects;
            Log.d(tag, "Search List -> journalEntryList := " + produitsDefautsList.toString());
        }

        @Override
        public int getCount()
        {
            return this.produitsDefautsList.size();
        }

        @Override
        public ProduitDefaut getItem(int position)
        {
            ProduitDefaut produit = this.produitsDefautsList.get(position);
            Log.d(tag, "*-> Retrieving JournalEntry @ position: " + String.valueOf(position) + " : " +  produit.toString());
            return produit;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            View row = convertView;
            LayoutInflater inflater = LayoutInflater.from(getContext());

            if (row == null)
            {
                row = inflater.inflate(R.layout.search_listitem, parent, false);
            }

            produit = this.produitsDefautsList.get(position);
            String nom = produit.getNom();
            nomProduit = (TextView) row.findViewById(R.id.nomProduit);
            nomProduit.setText(nom);

            // Get a reference to ImageView holder
            iconeProduit = (ImageView) row.findViewById(R.id.iconeProduit);

            if("".equals(produit.getUrl_image()))
            {
                iconeProduit.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_barcode));

            }
            else {
                Bitmap bitmap = null;
                try {
                    bitmap = new AsyncTaskLoadImage().execute(produit.getUrl_image()).get();
                    iconeProduit.setImageBitmap(bitmap);

                } catch (ExecutionException e) {
                    e.printStackTrace();
                    iconeProduit.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_barcode));

                } catch (InterruptedException e) {
                    e.printStackTrace();
                    iconeProduit.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_barcode));
                }
            }
            return row;
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
                    dataListAllItems = new ArrayList<ProduitDefaut>(produitsDefautsList);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                synchronized (lock) {
                    results.values = dataListAllItems;
                    results.count = dataListAllItems.size();
                }
            } else {
                final String searchStrLowerCase = prefix.toString().toLowerCase();

                ArrayList<ProduitDefaut> matchValues = new ArrayList<ProduitDefaut>();

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
                produitsDefautsList = (ArrayList<ProduitDefaut>)results.values;
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


