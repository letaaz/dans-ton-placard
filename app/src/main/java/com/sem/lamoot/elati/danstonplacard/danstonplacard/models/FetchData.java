package com.sem.lamoot.elati.danstonplacard.danstonplacard.models;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FetchData extends AsyncTask<String, Void, String> {


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
                line = bufferedReader.readLine();
                data += line;
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

        // TODO Save the product in data base
        //Toast.makeText(this.context, "Product saved and content number : "+this.content, Toast.LENGTH_LONG);

        Log.d("dtp","Product saved");
    }
}