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
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.RayonCategories;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;

public class FetchData extends AsyncTask<String, Void, String> {

    private Context mContext;
    private String contents;
    private Piece piece;

    public FetchData(Context context, String contents, String piece) {
        this.mContext = context;
        this.contents = contents;

        this.piece = Piece.getPiece(piece);
    }

    @Override
    protected String doInBackground(String... params) {
        return getJsonFile(params[0], 0);
    }

    @Override
    protected void onPostExecute(String data) {
        super.onPostExecute(data);

        if(data.isEmpty())
        {
            Log.d("dtp", "PRODUIT NOT FOUND");
            Toast.makeText(this.mContext, "Product not found : " + this.contents, Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject jsonDataProduct = null;
        try {
            jsonDataProduct = new JSONObject(data);
        } catch (JSONException e) {
            Log.d("dtp", "IMPOSSIBLE TO CREATE JSON OBJECT FROM DATA");
            e.printStackTrace();
        }

        JSONObject productJSONObject;
        try {
            productJSONObject = jsonDataProduct.getJSONObject("product");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("dtp", "IMPOSSIBLE TO CREATE JSON OBJECT FROM PRODUCT");
            Toast.makeText(this.mContext, "PRODUIT INDISPONIBLE : " + this.contents, Toast.LENGTH_SHORT).show();
            return;
        }

        String product_name = null;
        try {
            product_name = productJSONObject.getString("product_name_fr");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String product_brand = null;
        try{
            product_brand = productJSONObject.getString("brands");
        }catch (JSONException e){

        }

        String product_urlImage = null;
        try{
            product_urlImage = productJSONObject.getString("image_url");
        }catch(JSONException e){

        }

        float product_weight = 0;
        try {
            product_weight = productJSONObject.getInt("product_quantity");
        } catch (JSONException e) {

        }


        String product_categories = null;
        String[] categories = null;
        try{
            product_categories = productJSONObject.getString("categories");
            categories = product_categories.split(", ");
            Log.d("dtp", "Categoriie : "+categories[0]);
        }catch(JSONException e){

        }

        // Définition d'un rayon du produit scannée
        Rayon product_rayon = null;
        if(product_urlImage.contains("beauty")){
            product_rayon = Rayon.BEAUTE;
        }
        else{
            RayonCategories rayonCategories = RayonCategories.getInstance();
            product_rayon = rayonCategories.findRayonByCategory(categories);
        }




        Date product_date = new Date();


        if(product_name!=null){
            Log.d("dtp", "PRODUCT FOUND OK");
            Toast.makeText(this.mContext, "Product found : " + product_name + " / Rayon : "+product_rayon.toString(), Toast.LENGTH_SHORT).show();
        }
        else{
            Log.d("dtp", "PRODUIT NOT FOUND KO");
            Toast.makeText(this.mContext, "Product not found : " + this.contents, Toast.LENGTH_SHORT).show();
        }

        ProduitDao produitDao = RoomDB.getDatabase(this.mContext).produitDao();
        List<Produit> products = produitDao.findProductByBarcode(this.contents, this.piece.toString());

        if(!products.isEmpty()){
            Produit product_found = products.get(0);
            produitDao.updateQuantityById(product_found.getId(), product_found.getQuantite()+1);
            Log.d("dtp","PRODUCT QUANTITY UPDATED BY 1");
        }
        else{
            Produit product = new Produit(product_name, this.contents, product_brand, product_urlImage,1, product_weight, product_date, product_rayon, 0, piece);
            produitDao.insert(product);
            Log.d("dtp","PRODUCT INSERTED");
        }
    }

    /**
     * apiNumber 0 => OpenFoodFacts / apiNumber 2 => OpenBeautyFacts / apiNumber 3 => OpenProductsFacts
     * @param scancode
     * @param apiNumber
     */
    private String getJsonFile(String scancode, int apiNumber)
    {
        String urlLink = "";
        switch (apiNumber)
        {
            case 0:
                urlLink = "https://fr.openfoodfacts.org/api/v0/produit/" + scancode + ".json";
                break;
            case 1 :
                urlLink = "https://fr.openbeautyfacts.org/api/v0/produit/"+ scancode +".json";
                break;
            case 2:
                urlLink = "https://fr.openproductsfacts.org/api/v0/produit/"+ scancode +".json";
                break;
            default:
                return "";
        }

        String data = "";
        try {
            URL url = new URL(urlLink);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while (line != null) {
                data += line;
                if(line.contains("product not found")){
                    return getJsonFile(scancode, apiNumber+1);
                }
                line = bufferedReader.readLine();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {

        }
        return data;
    }
}