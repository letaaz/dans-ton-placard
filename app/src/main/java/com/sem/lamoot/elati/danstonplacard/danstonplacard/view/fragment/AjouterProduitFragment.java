package com.sem.lamoot.elati.danstonplacard.danstonplacard.view.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.R;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.RoomDB;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.dao.ListeCoursesDao;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.dao.ProduitDao;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.ListeCourses;
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

public class AjouterProduitFragment extends Fragment implements View.OnClickListener{

    public static String ARGS = "";
    private Context mContext = null;
    private String mPiece = null;
    private int idLDC = -1;
    private ProduitDao produitDao = null;
    private ListeCoursesDao listeCoursesDao = null;
    private Piece piece = null;
    private boolean isLDC = false;

    public static Fragment newInstance(String params) {
        Bundle args = new Bundle();
        args.putString(ARGS, params);
        args.putString("PIECE", params);
        args.putInt("idLDC", -1);
        AjouterProduitFragment ajouterProduitFragment = new AjouterProduitFragment();
        ajouterProduitFragment.setArguments(args);
        return ajouterProduitFragment;
    }


    public static Fragment newInstance(String params, int idLDC) {
        Bundle args = new Bundle();
        args.putString(ARGS, params);
        args.putString("PIECE", params);
        args.putInt("idLDC", idLDC);
        AjouterProduitFragment ajouterProduitFragment = new AjouterProduitFragment();
        ajouterProduitFragment.setArguments(args);
        return ajouterProduitFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getContext();
        if (getArguments() != null) {
            mPiece = getArguments().getString("PIECE");
            idLDC = getArguments().getInt("idLDC");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(mContext);
        firebaseAnalytics.setCurrentScreen(this.getActivity(), this.getClass().getSimpleName(), this.getClass().getSimpleName());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.ajouter_produit_fragment, container, false);
        ImageView imageView3 = (ImageView) view.findViewById(R.id.image_view_scan_codebarre);
        imageView3.setOnClickListener(this);

        Toast.makeText(mContext, "idLDC = " + idLDC, Toast.LENGTH_SHORT).show();

        ImageView button_add = (ImageView) view.findViewById(R.id.button_add);

        ArrayList<ProduitDefaut> produits = getProduitsDefaults(view.getContext(),"products_FR_fr.json");
        AutoCompleteTextView actv = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextView);
        ArrayAdapter<ProduitDefaut> adapter = new SearchItemArrayAdapter(mContext,R.layout.search_listitem, produits);
        actv.setAdapter(adapter);
        actv.setThreshold(1);

        produitDao = RoomDB.getDatabase(view.getContext()).produitDao();
        piece = Piece.getPiece(mPiece);


        if(idLDC != -1) // Ajout produit à LDC
        {
            listeCoursesDao = RoomDB.getDatabase(view.getContext()).listeCoursesDao();
            ListeCourses listeCourses = listeCoursesDao.getListeCoursesById(idLDC);

            actv.setOnItemClickListener((parent, view1, position, id) -> {
                Produit produit = produitDao.findProductByNom(adapter.getItem(position).getNom(), mPiece);
                if(produit == null)
                {
                    Produit newProduit = new Produit(adapter.getItem(position).getNom(), 0, Rayon.getRayon(adapter.getItem(position).getRayon()), Piece.DIVERS);
                    newProduit.setUrlImage(adapter.getItem(position).getUrl_image());
                    long id_newProduit = produitDao.insert(newProduit);

                    newProduit.setId((int) id_newProduit);
                    listeCourses.getProduitsAPrendre().add(newProduit);
                    listeCoursesDao.updateListe(listeCourses);

                    hideKeyboard();
                    showSnackBar(R.string.msg_produit_ajoute_ldc);
                    //getActivity().onBackPressed();
                }
                else
                {
                    //Update
                }
            });

            button_add.setOnClickListener(v -> {
                Produit produit = produitDao.findProductByNom(actv.getText().toString(), mPiece);
                if(produit == null) {
                    Produit newProduit = new Produit(actv.getText().toString(), 0, Rayon.DIVERS, Piece.DIVERS);
                    produitDao.insert(newProduit);
                    listeCourses.getProduitsAPrendre().add(newProduit);
                    listeCoursesDao.updateListe(listeCourses);

                    hideKeyboard();
                    showSnackBar(R.string.msg_produit_ajoute_ldc);
                    //getActivity().onBackPressed();
                }
                else
                {
                    // Update
                }
            });
        }
        else // Ajouter produit à la LDC
        {
            actv.setOnItemClickListener((parent, view1, position, id) -> {
                Produit produit = produitDao.findProductByNom(adapter.getItem(position).getNom(), mPiece);
                if(produit == null)
                {
                    Produit newProduit = new Produit(adapter.getItem(position).getNom(), 1, Rayon.getRayon(adapter.getItem(position).getRayon()), Piece.getPiece(mPiece));
                    newProduit.setUrlImage(adapter.getItem(position).getUrl_image());
                    produitDao.insert(newProduit);

                    hideKeyboard();
                    showSnackBar(R.string.msg_produit_ajoute);
                }
                else
                {
                    produitDao.updateQuantityById(produit.getId(), produit.getQuantite() + 1);
                    hideKeyboard();
                    showSnackBar(R.string.msg_produit_miseajour);
                }
            });

            button_add.setOnClickListener(v -> {
                Produit produit = produitDao.findProductByNom(actv.getText().toString(), mPiece);
                if(produit == null) {
                    Produit newProduit = new Produit(actv.getText().toString(), 1, Rayon.DIVERS, piece);
                    produitDao.insert(newProduit);
                    hideKeyboard();
                    showSnackBar(R.string.msg_produit_ajoute);
                }
                else
                {
                    produitDao.updateQuantityById(produit.getId(), produit.getQuantite()+1);
                    hideKeyboard();
                    showSnackBar(R.string.msg_produit_miseajour);
                }
            });
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        if (isNetworkAvailable()) {
            Log.d("dtp", "onclickImageview3");
            IntentIntegrator integrator = new IntentIntegrator(this.getActivity()).forSupportFragment(this);
            integrator.setPrompt("Scan a barcode or QRcode");
            integrator.setOrientationLocked(false);
            integrator.initiateScan();
        } else {
            String alertMsg = mContext.getResources().getString(R.string.msgAlertDialogInternetConnection);
            String title = mContext.getResources().getString(R.string.titleAlertDialogInternetConnection);
            String buttonText = mContext.getResources().getString(R.string.buttonAlertDialogInternetConnection);

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
            alertDialog.setTitle(title);

            alertDialog.setMessage(Html.fromHtml(alertMsg));
            alertDialog.setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog dialog = alertDialog.create();
            dialog.show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() == null) {
                getActivity().onBackPressed();
            } else {
                String data_product = "";
                try {
                    if(idLDC != -1)
                    {
                        data_product = new FetchData(getActivity().getApplicationContext(), result.getContents(), "DIVERS", idLDC).execute(result.getContents()).get();
                    }
                    else {
                        data_product = new FetchData(getActivity().getApplicationContext(), result.getContents(), mPiece, -1).execute(result.getContents()).get();
                    }
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
                onClick(getView());
            }

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

    public void hideKeyboard()
    {
        InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getRootView().getWindowToken(), 0);
    }

    public void showSnackBar(int msg)
    {
        Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.voir, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        snackbar.show();
    }
}
