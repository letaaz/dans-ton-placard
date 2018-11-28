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

import com.google.gson.JsonObject;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.R;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.models.FetchData;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.view.activity.InventaireActivity;

import org.json.JSONException;
import org.json.JSONObject;

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

        //Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_LONG).show();

        IntentIntegrator integrator = new IntentIntegrator(this.getActivity());
        integrator.setPrompt("Scan a barcode or QRcode");

        integrator.setOrientationLocked(false);

        integrator.initiateScan();

//        Use this for more customization

//        IntentIntegrator integrator = new IntentIntegrator(this);

//        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);

//        integrator.setPrompt("Scan a barcode");

//        integrator.setCameraId(0);  // Use a specific camera of the device

//        integrator.setBeepEnabled(false);

//        integrator.setBarcodeImageEnabled(true);

//        integrator.initiateScan();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result==null)
            Log.d("resultt : ", null);

        if (result != null) {
            if (result.getContents() == null) {
                //Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                Log.d("content :", result.getContents().toString());
            } else {
                Log.d("avant", "avant");
                FetchData process = new FetchData(result.getContents());
                process.execute();
                Log.d("apres", "apres");

                try {
                    JSONObject jsonDataProduct = new JSONObject(process.getData());
                    Log.d("resultat produit:", "OK");
                    Toast.makeText(getActivity().getApplicationContext(), "Product found", Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    Log.d("resultat produit:", "KO");
                    Toast.makeText(getActivity().getApplicationContext(), "Product not found", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}