package com.sem.lamoot.elati.danstonplacard.danstonplacard.database.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.RoomDB;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.dao.ProduitDao;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Piece;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Produit;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Rayon;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FetchData extends AsyncTask<String, Void, String> {

    private Context context;
    private String contents;

    public FetchData(Context context, String contents) {
        this.context = context;
        this.contents = contents;
    }

    @Override
    protected String doInBackground(String... params) {
        String data = "";

        try {
            Log.d("dtp", "https://fr.openfoodfacts.org/api/v0/produit/"+ params[0] +".json");

            URL url = new URL("https://fr.openfoodfacts.org/api/v0/produit/" + params[0] + ".json");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while (line != null) {
                data += line;
                line = bufferedReader.readLine();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {

        }
        return data;
    }

    @Override
    protected void onPostExecute(String data) {
        super.onPostExecute(data);


        JSONObject jsonDataProduct = null;
        try {
            jsonDataProduct = new JSONObject(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject productJSONObject = null;
        try {
            productJSONObject = jsonDataProduct.getJSONObject("product");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String product_name = null;
        try {
            product_name = productJSONObject.getString("product_name_fr");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        float product_weight = 0;
        try {
            product_weight = productJSONObject.getInt("product_quantity");
        } catch (JSONException e) {

        }

        Rayon product_rayon = Rayon.BIO;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
        Date product_date = new Date();


        if(product_name!=null){
            Log.d("dtp", "PRODUIT DISPO OK");
            //Toast.makeText(getActivity().getApplicationContext(), "Product found : "+jsonDataProduct.getString("product_name_fr"), Toast.LENGTH_LONG).show();
            Toast.makeText(this.context, "Product found : "+product_name+" Quantity : "+product_weight+", Date : "+product_date.toString()+", Rayon : "+product_rayon.toString(), Toast.LENGTH_LONG).show();
        }
        else{
            Log.d("dtp", "PRODUIT INDISPO KO");
            Toast.makeText(this.context, "Product not found : "+this.contents, Toast.LENGTH_LONG).show();
        }
        // TODO Ajoute produit si produit inexistant sinon mise à jour du produit
        ProduitDao produitDao = RoomDB.getDatabase(this.context).produitDao();

        List<Produit> products = produitDao.findProductByBarcode(this.contents, Piece.CUISINE.toString());

        if(!products.isEmpty()){
            Produit product_found = products.get(0);
            produitDao.updateQuantityById(product_found.getId(), product_found.getQuantite()+1);
        }
        else{
            Produit product = new Produit(product_name, this.contents,1, product_weight, product_date, product_rayon, 0, Piece.CUISINE);
            produitDao.insert(product);
        }




        // TODO Save the product in data base
        //Toast.makeText(this.context, "Product saved and content number : "+this.content, Toast.LENGTH_LONG);

        Log.d("dtp","Product saved");

    }
}