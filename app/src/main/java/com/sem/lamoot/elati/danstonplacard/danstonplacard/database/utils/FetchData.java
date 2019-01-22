package com.sem.lamoot.elati.danstonplacard.danstonplacard.database.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;

import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.RoomDB;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.dao.ListeCoursesDao;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.dao.ProduitDao;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.ListeCourses;
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

    private int idDLC;
    private Context mContext;
    private String contents;
    private Piece piece;
    private String scancode;

    public FetchData(Context context, String contents, String piece, int idDLC) {
        this.mContext = context;
        this.contents = contents;

        this.idDLC = idDLC;
        this.piece = Piece.getPiece(piece);
    }

    @Override
    protected String doInBackground(String... params) {
        scancode = params[0];
        return getJsonFile(params[0], 0);
    }

    @Override
    protected void onPostExecute(String data) {
        super.onPostExecute(data);

        if("".equals(data))
        {
            Toast.makeText(this.mContext, "Le produit n'existe pas : " + this.contents, Toast.LENGTH_LONG).show();
            return;
        }

        JSONObject jsonDataProduct = null;
        JSONObject productJSONObject = null;
        String product_name = null;
        String product_brand = null;
        String product_urlImage = null;
        float product_weight = 0;
        String product_categories;
        String[] categories = null;
        RayonCategories rayonCategories = RayonCategories.getInstance();
        Rayon product_rayon = Rayon.DIVERS;
        Date product_date = new Date();

        try {
            jsonDataProduct = new JSONObject(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Get données du produit scanné
        try {
            productJSONObject = jsonDataProduct.getJSONObject("product");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Set le nom du produit
        try {
            product_name = productJSONObject.getString("product_name_fr");
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this.mContext, "Le produit n'existe pas : " + this.contents, Toast.LENGTH_LONG).show();
            return;
        }

        // Set la marque du produit
        try{
            product_brand = productJSONObject.getString("brands");
        }catch (JSONException e){
            e.printStackTrace();

        }

        // Set l'url de l'image du produit
        try{
            product_urlImage = productJSONObject.getString("image_url");
        }catch(JSONException e){
            e.printStackTrace();

        }

        // Set le poids du produit
        try {
            product_weight = productJSONObject.getInt("product_quantity");
        } catch (JSONException e) {
            e.printStackTrace();

        }


        // Set le rayon du produit
        try{
            product_categories = productJSONObject.getString("categories");
            categories = product_categories.split(",");

            if(categories != null) {
                // Définition d'un rayon du produit scannée
                product_rayon = rayonCategories.findRayonByCategory(categories);
            }

        }catch(JSONException e){
            e.printStackTrace();
        }

        Toast.makeText(this.mContext, "Produit trouvé : " + product_name + " / Rayon : "+product_rayon.toString(), Toast.LENGTH_SHORT).show();

        ProduitDao produitDao = RoomDB.getDatabase(this.mContext).produitDao();
        ListeCoursesDao listeCoursesDao = RoomDB.getDatabase(this.mContext).listeCoursesDao();

        if(idDLC == -1) {
            List<Produit> products = produitDao.findProductByBarcode(this.contents, this.piece.toString());

            // Update du produit si il est déjà enregistré - sinon on l'ajoute à la base
            if (!products.isEmpty()) {
                Produit product_found = products.get(0);
                produitDao.updateQuantityById(product_found.getId(), product_found.getQuantite() + 1);
                Log.d("dtp", "PRODUCT QUANTITY UPDATED BY 1");
            } else {
                Produit product = new Produit(product_name, this.contents, product_brand, product_urlImage, 1, product_weight, product_date, product_rayon, 0, piece);
                produitDao.insert(product);
                Log.d("dtp", "PRODUCT INSERTED");
            }
        }
        else
        {
            Log.d("dtp", "Fetch idLDC = " + idDLC);
            Produit product = new Produit(product_name, this.contents, product_brand, product_urlImage, 0, product_weight, product_date, product_rayon, 0, piece);
            long idProduct = produitDao.insert(product);
            product.setId((int) idProduct);

            ListeCourses li = listeCoursesDao.getListeCoursesById(idDLC);
            List<Produit> aPrendre = li.getProduitsAPrendre();
            aPrendre.add(product);
            li.setProduitsAPrendre(aPrendre);
            listeCoursesDao.updateListe(li);
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

        Log.i("dtp", "urlLink = " + urlLink);
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
            e.printStackTrace();
        }
        return data;
    }
}