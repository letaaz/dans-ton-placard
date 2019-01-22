package com.sem.lamoot.elati.danstonplacard.danstonplacard.view.fragment.ldc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.R;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.RoomDB;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Piece;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Produit;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.ProduitDefaut;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Rayon;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.utils.FetchData;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.view.SearchItemArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class AjouterProduitLDCFragment extends Fragment implements View.OnClickListener {

    public static String ARG = "ARG_LDC";
    private Context mContext;
    private Integer mLdc;

    public static Fragment newInstance(Integer param) {
        Bundle args = new Bundle();
        args.putInt(ARG, param);
        AjouterProduitLDCFragment ajouterProduitFragment = new AjouterProduitLDCFragment();
        ajouterProduitFragment.setArguments(args);
        return ajouterProduitFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getContext();
        if (getArguments() != null) {
            mLdc = getArguments().getInt(ARG);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ajouter_produit_fragment, container, false);
        ImageView imageView3 = (ImageView) view.findViewById(R.id.image_view_scan_codebarre);
        imageView3.setOnClickListener(this);

        ImageView button_add = (ImageView) view.findViewById(R.id.button_add);

        ArrayList<ProduitDefaut> produits = getProduitsDefaults(view.getContext(),"products_FR_fr.json");
        AutoCompleteTextView actv = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextView);
        ArrayAdapter<ProduitDefaut> adapter = new SearchItemArrayAdapter(mContext,R.layout.search_listitem, produits);
        actv.setAdapter(adapter);
        actv.setThreshold(1);

//        produitDao = RoomDB.getDatabase(view.getContext()).produitDao();
//        piece = Piece.getPiece(mPiece);

        actv.setOnItemClickListener((parent, view1, position, id) -> {
//            Produit produit = produitDao.findProductByNom(adapter.getItem(position).getNom(), mPiece);
//            if(produit == null)
//            {
//                Produit newProduit = new Produit(adapter.getItem(position).getNom(), 1, Rayon.FRUITS_LEGUMES, Piece.getPiece(mPiece));
//                newProduit.setUrlImage(adapter.getItem(position).getUrl_image());
//                produitDao.insert(newProduit);
//
//                hideKeyboard();
//                showSnackBar(R.string.msg_produit_ajoute);
//            }
//            else
//            {
//                produitDao.updateQuantityById(produit.getId(), produit.getQuantite() + 1);
//                hideKeyboard();
//                showSnackBar(R.string.msg_produit_miseajour);
//            }
        });

        button_add.setOnClickListener(v -> {
//            Produit produit = produitDao.findProductByNom(actv.getText().toString(), mPiece);
//            if(produit == null) {
//                Produit newProduit = new Produit(actv.getText().toString(), 1, Rayon.DIVERS, piece);
//                produitDao.insert(newProduit);
//                hideKeyboard();
//                showSnackBar(R.string.msg_produit_ajoute);
//            }
//            else
//            {
//                produitDao.updateQuantityById(produit.getId(), produit.getQuantite()+1);
//                hideKeyboard();
//                showSnackBar(R.string.msg_produit_miseajour);
//            }
        });
        return view;
    }

    @Override
    public void onClick(View v) {
        Log.d("dtp", "onclickImageview3");
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
                Log.d("dtp :", "result getContents null");
            } else {
                Log.d("dtp", result.getContents());
                String data_product = "";
                try {
                    data_product = new FetchData(getActivity().getApplicationContext(), result.getContents(), "CUISINE", mLdc).execute(result.getContents()).get();
                    Log.d("dtp", data_product);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
            getActivity().onBackPressed();
        }
    }

    public String loadJSONFromAsset(Context context, String fileName) {
        String json;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public ArrayList<ProduitDefaut> getProduitsDefaults(Context context, String fileName)
    {
        ArrayList<ProduitDefaut> produits = new ArrayList<>();
        String data = loadJSONFromAsset(context, fileName);
        try {
            JSONObject defaultProducts = new JSONObject(data);
            JSONArray products = defaultProducts.getJSONArray("products");
            for(int i = 0; i < products.length(); i++)
            {
                String nom = products.getJSONObject(i).getString("name");
                String url_image = products.getJSONObject(i).getString("img_url");
                String rayon =  products.getJSONObject(i).getString("rayon");
                ProduitDefaut produit = new ProduitDefaut(nom, rayon, url_image);
                produits.add(produit);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return produits;
    }
}
