package com.danstonplacard.view.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import com.danstonplacard.R;
import com.danstonplacard.database.RoomDB;
import com.danstonplacard.database.dao.ListeCoursesDao;
import com.danstonplacard.database.dao.ProduitDao;
import com.danstonplacard.database.model.ListeCourses;
import com.danstonplacard.database.model.Piece;
import com.danstonplacard.database.model.Produit;
import com.danstonplacard.database.model.ProduitDefaut;
import com.danstonplacard.database.model.Rayon;
import com.danstonplacard.view.SearchItemArrayAdapter;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Fragment that adds products to inventory or shopping lists
 */
public class AjouterProduitFragment extends Fragment implements View.OnClickListener{

    public static String ARGS = "";
    private Context mContext = null;
    private String mPiece = null;
    private int idLDC = -1;
    private ProduitDao produitDao = null;
    private ListeCoursesDao listeCoursesDao = null;
    private Piece piece = null;

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
    public void onPause() {
        super.onPause();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.ajouter_produit_fragment, container, false);

        ImageView imageView3 = (ImageView) view.findViewById(R.id.image_view_scan_codebarre);
        AutoCompleteTextView actv = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextView);
        ImageView button_add = (ImageView) view.findViewById(R.id.button_add);

        // Get DAO
        produitDao = RoomDB.getDatabase(view.getContext()).produitDao();
        listeCoursesDao = RoomDB.getDatabase(view.getContext()).listeCoursesDao();


        // Get Piece
        piece = Piece.getPiece(mPiece);


        // Set Method onClick to imageView (scanbar img)
        imageView3.setOnClickListener(this);

        // Get Produit Defaut
        ArrayList<ProduitDefaut> produits = getProduitsDefaults(view.getContext(),"products_FR_fr.json");

        ArrayAdapter<ProduitDefaut> adapter = new SearchItemArrayAdapter(mContext,R.layout.search_listitem, produits);
        actv.setAdapter(adapter);
        actv.setThreshold(1);


        if(idLDC != -1) // Ajout produit à LDC
        {
            ajouterProduitsDefautsALDC(actv, adapter, button_add);
        }
        else // Ajouter produit à l'inventaire
        {
            ajouterProduitDefautAInventaire(actv, adapter, button_add);
        }

        return view;
    }

    /**
     * Method called to add a default product to the inventory
     * @param actv
     * @param adapter
     * @param button_add
     */
    private void ajouterProduitDefautAInventaire(AutoCompleteTextView actv, ArrayAdapter<ProduitDefaut> adapter, ImageView button_add) {
        actv.setOnItemClickListener((parent, view1, position, id) -> {

            // Check if product exists
            Produit produit = produitDao.findProductByNom(adapter.getItem(position).getNom(), mPiece);
            if(produit == null) { // Product not exists
                // Insert new product to BDD
                Produit newProduit = new Produit(adapter.getItem(position).getNom(), 1, Rayon.getRayon(adapter.getItem(position).getRayon()), Piece.getPiece(mPiece));
                newProduit.setUrlImage(adapter.getItem(position).getUrl_image());
                produitDao.insert(newProduit);

                hideKeyboard();
                showSnackBar(R.string.msg_produit_ajoute);
            }
            else { // Products exists
                // Update product
                produitDao.updateQuantityById(produit.getId(), produit.getQuantite() + 1);
                hideKeyboard();
                showSnackBar(R.string.msg_produit_miseajour);
            }
        });


        button_add.setOnClickListener(v -> {
            Produit produit = produitDao.findProductByNom(actv.getText().toString(), mPiece);
            // Check if products exists in BDD
            if(produit == null) { // Product not exists
                // Insert product in BDD
                Produit newProduit = new Produit(actv.getText().toString(), 1, Rayon.DIVERS, piece);
                produitDao.insert(newProduit);
                hideKeyboard();
                showSnackBar(R.string.msg_produit_ajoute);
            }
            else { // Product exists
                // Update products
                produitDao.updateQuantityById(produit.getId(), produit.getQuantite()+1);
                hideKeyboard();
                showSnackBar(R.string.msg_produit_miseajour);
            }
        });
    }

    /**
     * Method called to add a default product to the shopping list
     * @param actv
     * @param adapter
     * @param button_add
     */
    private void ajouterProduitsDefautsALDC(AutoCompleteTextView actv, ArrayAdapter<ProduitDefaut> adapter, ImageView button_add) {
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
                // Update
            }
        });
    }

    /**
     * Method called when the user clicks on the bar scan image.
     * @param v
     */
    @Override
    public void onClick(View v) {
        getFragmentManager().popBackStack();

        // Check if internet is available
        if (isNetworkAvailable()) {
            launchScanbarFragment();
        } else { // Show alert dialog to put on internet connection
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

    /**
     * Method called to launch the barcode scanner
     */
    private void launchScanbarFragment() {
        if(idLDC == -1) { // If Scanbar in LDC - Replace root_inventaire_frame
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.root_inventaire_frame, ScanbarFragment.newInstance(mPiece, idLDC));
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else { // If Scanbar in Inventaire - Replace root_ldc_frame
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.root_ldc_frame, ScanbarFragment.newInstance(mPiece, idLDC));
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }


    /**
     * Method that verifies that the internet connection is active
     * @return true if internet is available - false otherwise
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    /**
     * Called method to load defaults products from a JSON file contained in assets folder
     * @param context
     * @param fileName
     * @return
     */
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


    /**
     * Method called to obtain a list of commodities / default (unbranded)
     * @param context context of the activity
     * @param fileName name of the JSON file that contains defaults products
     * @return List of ProductDefaut
     */
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

    /**
     * Method called to hide the keyboard
     */
    public void hideKeyboard()
    {
        InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getRootView().getWindowToken(), 0);
    }

    /**
     * Method called to display a snackbar when a product is added to the inventory
     * @param msg Message in R.String to show
     */
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
