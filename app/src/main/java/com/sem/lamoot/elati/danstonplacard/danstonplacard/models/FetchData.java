package com.sem.lamoot.elati.danstonplacard.danstonplacard.models;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.sem.lamoot.elati.danstonplacard.danstonplacard.view.activity.InventaireActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FetchData extends AsyncTask<Void, Void, Void> {

    private String content;
    private String data = "";


    public FetchData(String content) {
        this.content = content;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        try {
            //Log.d("URL : ", "https://fr.openfoodfacts.org/api/v0/produit/"+this.content+".json");
            URL url = new URL("https://fr.openfoodfacts.org/api/v0/produit/" + this.content + ".json");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while (line != null) {
                line = bufferedReader.readLine();
                this.data = this.data + line;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {

        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        // TODO the product in data base
        //Toast.makeText(this.context, "Product saved and content number : "+this.content, Toast.LENGTH_LONG);
        Log.d("response :","PRoduct saved");
    }
}