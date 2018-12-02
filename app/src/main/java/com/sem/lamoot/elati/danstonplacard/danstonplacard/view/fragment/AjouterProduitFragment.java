package com.sem.lamoot.elati.danstonplacard.danstonplacard.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.R;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.RoomDB;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Piece;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Produit;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Rayon;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.models.FetchData;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class AjouterProduitFragment extends Fragment implements View.OnClickListener{

    public static String ARGS = "";

    public static Fragment newInstance(String params) {
        Bundle args = new Bundle();
        args.putString(ARGS, params);
        AjouterProduitFragment ajouterProduitFragment = new AjouterProduitFragment();
        ajouterProduitFragment.setArguments(args);
        return ajouterProduitFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.ajouter_produit_fragment, container, false);
        ImageView imageView3 = (ImageView) view.findViewById(R.id.imageView3);
        imageView3.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        Log.d("dtp", "onclickImageview3");
        //Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_LONG).show();

        //IntentIntegrator integrator = new IntentIntegrator(this.getActivity());
        IntentIntegrator integrator = new IntentIntegrator(this.getActivity()).forSupportFragment(this);

        integrator.setPrompt("Scan a barcode or QRcode");

        integrator.setOrientationLocked(false);

        integrator.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            Log.d("dtp : ", "result null");

            if (result.getContents() == null) {
                Log.d("dtp :", "result getContent null");
            } else {
                Log.d("dtp", "avant");

                Log.d("dtp", result.getContents());


                String data_product = "";
                
                try {
                    data_product = new FetchData().execute(result.getContents()).get();
                    Log.d("dtp", data_product);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Log.d("dtp", "apres");

                try {
                    JSONObject jsonDataProduct = new JSONObject(data_product);
                    JSONObject productJSONObject = jsonDataProduct.getJSONObject("product");
                    String product_name = productJSONObject.getString("product_name_fr");
                    float product_weight = productJSONObject.getInt("product_quantity");
                    Rayon product_rayon = Rayon.BIO;
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
                    String dateInString = "31-08-2022";
                    Date product_date = sdf.parse(dateInString);

                    Produit product = new Produit(product_name, 1, product_weight, product_date, product_rayon, 0, Piece.CUISINE);

                    // TODO Ajoute produit
                    RoomDB.getDatabase(this.getContext()).produitDao().insert(product);

                    Log.d("dtp", "PRODUIT DISPO OK");
                    //Toast.makeText(getActivity().getApplicationContext(), "Product found : "+jsonDataProduct.getString("product_name_fr"), Toast.LENGTH_LONG).show();
                    Toast.makeText(getActivity().getApplicationContext(), "Product found : "+product_name+" Quantity : "+product_weight+", Date : "+product_date.toString()+", Rayon : "+product_rayon.toString(), Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    Log.d("dtp", "PRODUIT INDISPO KO");
                    Toast.makeText(getActivity().getApplicationContext(), "Product not found : "+result.getContents(), Toast.LENGTH_LONG).show();
                } /*catch (ParseException e) {
                    e.printStackTrace();
                }*/ catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}