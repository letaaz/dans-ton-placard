package com.danstonplacard.view.fragment.inventaire;

import android.app.Activity;
import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.danstonplacard.R;
import com.danstonplacard.database.RoomDB;
import com.danstonplacard.database.dao.ProduitDao;
import com.danstonplacard.database.model.Piece;
import com.danstonplacard.database.model.Produit;
import com.danstonplacard.database.model.ProduitDefaut;
import com.danstonplacard.database.model.Rayon;
import com.danstonplacard.view.SearchItemArrayAdapter;
import com.danstonplacard.view.fragment.DetailProduitFragment;
import com.danstonplacard.view.fragment.ScanbarFragment;
import com.danstonplacard.viewmodel.ProduitViewModel;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Fragment that contains the available and unavailable products depending on the selected room
 */
public class PieceFragment extends Fragment
        implements ProduitAdapter.OnMinusImageViewClickListener, ProduitAdapter.OnAddImageViewClickListener,
        ProduitAdapter.OnProductItemClickListener {

    public static String ARG_PIECE = "";
    private Context mContext = null;
    private String mParam = null;
    private String mPiece = null;
    private String colonneTri = "nom";
    private String trierPar = "ASC";
    private int checkedItemSort = 0;

    // RecyclerView + Adapter + TexView - produits disponibles
    private RecyclerView produitsDisponiblesRecyclerView;
    private ProduitAdapter produitsDisponiblesAdapter;
    private TextView produitsDisponiblesCount;

    // RecyclerView + Adapter + TextView - produits indisponibles
    private RecyclerView produitsIndisponiblesRecyclerView;
    private ProduitAdapter produitsIndisponiblesAdapter;
    private TextView produitsIndisponiblesCount;

    private ProduitViewModel produitViewModel;


    private View view;
    private int mPosition;

    private Handler handler;
    private NestedScrollView nestedScrollView;
    private int scrollX = 0;
    private int scrollY = -1;

    public static Fragment newInstance(String param) {
        Bundle args = new Bundle();
        args.putString(ARG_PIECE, param);
        args.putString("PIECE", param);
        PieceFragment pieceFragment = new PieceFragment();
        pieceFragment.setArguments(args);
        return pieceFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getContext();
        if (getArguments() != null) {
            mParam = getArguments().getString(ARG_PIECE);
            mPiece = getArguments().getString("PIECE");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(mContext);
        firebaseAnalytics.setCurrentScreen(this.getActivity(), this.getClass().getSimpleName(), this.getClass().getSimpleName());
        nestedScrollView.postDelayed(() -> nestedScrollView.scrollTo(scrollX, scrollY), 100);
    }

    @Override
    public void onPause(){
        super.onPause();
        scrollX = nestedScrollView.getScrollX();
        scrollY = nestedScrollView.getScrollY();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_menu_piece_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        String[] piecesStringFormat = getResources().getStringArray(R.array.pieces_format);

        switch (item.getItemId()) {
            case R.id.action_piece_precedente:
                for (int i = 0; i < piecesStringFormat.length; i++) {
                    if (piecesStringFormat[i].equals(mParam)) {
                        if (i > 0) {
                            openAnOtherPieces(piecesStringFormat[i - 1]);
                        }
                        else
                        {
                            openAnOtherPieces(piecesStringFormat[piecesStringFormat.length-1]);
                        }
                    }
                }                return true;
            case R.id.action_piece_suivante:
                for (int i = 0; i < piecesStringFormat.length; i++) {
                    if (piecesStringFormat[i].equals(mParam)) {
                        if (i < piecesStringFormat.length - 1) {
                            openAnOtherPieces(piecesStringFormat[i + 1]);
                        }
                        else{ openAnOtherPieces(piecesStringFormat[0]);}
                    }
                }                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.piece_fragment, container, false);
        this.view = view;

        // Set RecyclerView + Set Datas (Products Availables and unavailables)
        produitViewModel = ViewModelProviders.of(this).get(ProduitViewModel.class);
        setProduitsDisponibles(colonneTri, trierPar, "");
        setProduitsIndisponibles(colonneTri, trierPar, "");

        getActivity().setTitle(getPiece(mPiece));
        setHasOptionsMenu(true);

        RelativeLayout section_dispo = view.findViewById(R.id.section_produits_dispo);
        ImageView btn_hide_show_available_product = view.findViewById(R.id.section_show_all_button_dispo);
        RelativeLayout section_indispo = view.findViewById(R.id.section_produits_indispo);
        ImageView btn_hide_show_unavailable_product = view.findViewById(R.id.section_show_all_button_indispo);
        nestedScrollView = view.findViewById(R.id.nestedScrollView);
        FloatingActionButton add_fab = view.findViewById(R.id.ajout_produit_fab);

        AutoCompleteTextView actv = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextView);


        // Set autoCompleteTextView to suggest defaults products (with no barcode for example)
        ArrayList<ProduitDefaut> produits = getProduitsDefaults(view.getContext(), "products_FR_fr.json");

        ArrayAdapter<ProduitDefaut> adapter = new SearchItemArrayAdapter(mContext, R.layout.search_listitem, produits);
        actv.setAdapter(adapter);
        actv.setThreshold(1);
        actv.addTextChangedListener(new TextWatcher() {
                                        @Override
                                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                        }

                                        @Override
                                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                                            String searchWord = s.toString().trim();
                                            setProduitsDisponibles(colonneTri, trierPar, searchWord);
                                            setProduitsIndisponibles(colonneTri, trierPar, searchWord);
                                        }

                                        @Override
                                        public void afterTextChanged(Editable s) {
                                            if(adapter.getCount() == 0) {
                                                handler.removeMessages(100);
                                                handler.sendEmptyMessageDelayed(100, 100);
                                            }
                                        }
                                    }
                                    );

        handler = new Handler(msg -> {
            if(msg.what == 100){
                if(!TextUtils.isEmpty(actv.getText())){
                    List<ProduitDefaut> newProduits = new ArrayList<>();
                    newProduits.add(new ProduitDefaut(actv.getText().toString(), "DIVERS", ""));
                    ((SearchItemArrayAdapter) adapter).setData(newProduits);
                    adapter.notifyDataSetChanged();
                }
            }
            return false;
        });

        actv.setOnItemClickListener((parent, view1, position, id) -> {
            ProduitDao produitDao = RoomDB.getDatabase(view1.getContext()).produitDao();

            // Check if product exists
            Produit produit = produitDao.findProductByNom(adapter.getItem(position).getNom(), mPiece);
            if (produit == null) { // Product not exists
                // Insert new product to BDD
                Produit newProduit = new Produit(adapter.getItem(position).getNom(), 1, Rayon.getRayon(adapter.getItem(position).getRayon()), Piece.getPiece(mPiece));
                newProduit.setUrlImage(adapter.getItem(position).getUrl_image());
                produitDao.insert(newProduit);
            } else { // Products exists
                // Update product
                produitDao.updateQuantityById(produit.getId(), produit.getQuantite() + 1);
            }
        });

        // Add behavior to close icon after autocompletetextview
        ImageView close_actv = view.findViewById(R.id.close_actv);
        close_actv.setOnClickListener(v -> {
            actv.setText("");
            hideKeyboard();
        });

        // Sortting products by something (name, ray, price or date)
        String[] listSortingBy = getResources().getStringArray(R.array.sort_by);

        TextView btn_sort_by = view.findViewById(R.id.sort_by_btn);
        btn_sort_by.setOnClickListener(v -> {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
            mBuilder.setTitle("Trier par");

            mBuilder.setSingleChoiceItems(listSortingBy, checkedItemSort, (dialogInterface, i) -> {
                checkedItemSort = i;
                String[] parts = listSortingBy[checkedItemSort].split(" ");
                refreshSortCriteria(parts);

                setProduitsDisponibles(colonneTri, trierPar, "");
                setProduitsIndisponibles(colonneTri, trierPar, "");

                dialogInterface.dismiss();
            });

            AlertDialog mDialog = mBuilder.create();
            mDialog.show();
        });

        // Implementation of the behavior to close down / reduce the list of available products
        setOnClickOnBtnShowAllProduitsDispos(section_dispo, btn_hide_show_available_product);

        // Setting up the behavior to close down / reduce the list of unavailable products
        setOnClickOnBtnShowAllProduitsIndispos(section_indispo, btn_hide_show_unavailable_product);

        // Setting behavior to floating button
        setOnClickToFloatingButton(add_fab);


        // Hide floating button when scroll in list of product
        hideFloatingButton(nestedScrollView, add_fab);


        return view;
    }


    private void openAnOtherPieces(String pieceToOpen) {
        getFragmentManager().popBackStack();
        FragmentManager manager = ((AppCompatActivity) mContext).getSupportFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.replace(R.id.root_inventaire_frame, PieceFragment.newInstance(pieceToOpen), "PieceFragment");
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        trans.addToBackStack("changePiece");
        trans.commit();
    }

    /**
     * Hides the floating button when the user moves down the list -
     * prevents the button from being above the (-) and (+) buttons of a product
     *
     * @param nestedScrollView
     * @param add_fab
     */
    private void hideFloatingButton(NestedScrollView nestedScrollView, FloatingActionButton add_fab) {
        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY > oldScrollY) {
                add_fab.hide();
            } else {
                add_fab.show();
            }
        });
    }

    /**
     * Set onClick Behavor for Floating Button
     * Open new Fragment - AjouterProduitFragment to add new Product
     *
     * @param add_fab
     */
    private void setOnClickToFloatingButton(FloatingActionButton add_fab) {
        add_fab.setOnClickListener(view1 -> {

            // Check if internet is available
            if (isNetworkAvailable()) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.root_inventaire_frame, ScanbarFragment.newInstance(mPiece, -1));
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(null);
                transaction.commit();
            } else { // Show alert dialog to put on internet connection
                String alertMsg = mContext.getResources().getString(R.string.msgAlertDialogInternetConnection);
                String title = mContext.getResources().getString(R.string.titleAlertDialogInternetConnection);
                String buttonText = mContext.getResources().getString(R.string.buttonAlertDialogInternetConnection);

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                alertDialog.setTitle(title);

                alertDialog.setMessage(Html.fromHtml(alertMsg));
                alertDialog.setPositiveButton(buttonText, (dialog, which) -> dialog.cancel());
                AlertDialog dialog = alertDialog.create();
                dialog.show();
            }
        });
    }

    /**
     * @param section_indispo
     * @param btn_hide_show_unavailable_product
     */
    private void setOnClickOnBtnShowAllProduitsIndispos(RelativeLayout section_indispo, ImageView btn_hide_show_unavailable_product) {
        section_indispo.setOnClickListener(v -> {
            if (produitsIndisponiblesRecyclerView.getVisibility() == View.GONE) {
                btn_hide_show_unavailable_product.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.drag_list_down_dark));
                produitsIndisponiblesRecyclerView.setVisibility(View.VISIBLE);
            } else {
                btn_hide_show_unavailable_product.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.drag_list_up_dark));
                produitsIndisponiblesRecyclerView.setVisibility(View.GONE);
            }
        });

    }

    /**
     * @param section_dispo
     * @param btn_hide_show_available_product
     */
    private void setOnClickOnBtnShowAllProduitsDispos(RelativeLayout section_dispo, ImageView btn_hide_show_available_product) {
        section_dispo.setOnClickListener(v -> {
            if (produitsDisponiblesRecyclerView.getVisibility() == View.GONE) {
                btn_hide_show_available_product.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.drag_list_down_dark));
                produitsDisponiblesRecyclerView.setVisibility(View.VISIBLE);
            } else {
                btn_hide_show_available_product.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.drag_list_up_dark));
                produitsDisponiblesRecyclerView.setVisibility(View.GONE);
            }
        });
    }


    /**
     * Set Produits Disponibles in RecyclerView
     */
    private void setProduitsDisponibles(String colonne, String trierPar, String nom) {
        produitViewModel.getProduitsDisponiblesTrierPar(mParam, colonne, trierPar, nom).observe(this,
                produits -> {
                    produitsDisponiblesAdapter.setData(produits);
                    produitsDisponiblesCount.setText("(" + produits.size() + ")");
                }
        );
        setRecyclerViewProduitsDisponibles();
    }

    /**
     * Set RecyclerView of Produits Disponibles
     */
    private void setRecyclerViewProduitsDisponibles() {
        // Set recyclerView + Adapter - Produits disponibles
        produitsDisponiblesAdapter = new ProduitAdapter(this.mContext, this, this, -1);
        produitsDisponiblesAdapter.setOnProductItemClickListener(this::onProductItemClickListener);
        produitsDisponiblesRecyclerView = (RecyclerView) view.findViewById(R.id.inventaireDispo_recyclerview);
        produitsDisponiblesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        produitsDisponiblesRecyclerView.setAdapter(produitsDisponiblesAdapter);

        RecyclerView.LayoutManager produitsDisponiblesLayoutManager = new LinearLayoutManager(getActivity());
        produitsDisponiblesRecyclerView.setLayoutManager(produitsDisponiblesLayoutManager);
        produitsDisponiblesRecyclerView.setNestedScrollingEnabled(false);

        produitsDisponiblesCount = view.findViewById(R.id.section_count);
    }


    /**
     * Set Products Indisponibles in RecyclerView
     */
    private void setProduitsIndisponibles(String colonne, String trierPar, String nom) {
        produitViewModel.getProduitsIndisponiblesTrierPar(mParam, colonne, trierPar, nom).observe(this,
                produits_indispos ->  {
                    produitsIndisponiblesAdapter.setData(produits_indispos);
                    produitsIndisponiblesCount.setText("(" + produits_indispos.size() + ")");
                });
        setRecyclerViewProduitsIndisponibles();
    }

    /**
     * Set RecyclerView of Produits Indisponibles
     */
    private void setRecyclerViewProduitsIndisponibles() {
        // Set recyclerView + Adapter - Produits indisponibles
        produitsIndisponiblesAdapter = new ProduitAdapter(this.mContext, this, this, -1);
        produitsIndisponiblesAdapter.setOnProductItemClickListener(this::onProductItemClickListener);
        produitsIndisponiblesRecyclerView = (RecyclerView) view.findViewById(R.id.inventaireIndispo_recyclerview);
        produitsIndisponiblesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        produitsIndisponiblesRecyclerView.setAdapter(produitsIndisponiblesAdapter);

        RecyclerView.LayoutManager produitsIndisponiblesLayoutManager = new LinearLayoutManager(getActivity());
        produitsIndisponiblesRecyclerView.setLayoutManager(produitsIndisponiblesLayoutManager);
        produitsIndisponiblesRecyclerView.setNestedScrollingEnabled(false);

        produitsIndisponiblesCount = view.findViewById(R.id.section_count2);
    }


    /**
     * Method called when (-) button on a product is clicked
     *
     * @param produit
     */
    @Override
    public void onMinusImageViewClickListener(Produit produit) {
        if (produit.getQuantite() > 0)
            produitViewModel.updateProduit(produit.getId(), produit.getQuantite() - 1);
    }

    /**
     * Method called when (+) button on a product is clicked
     *
     * @param produit
     */
    @Override
    public void onAddImageViewClickListener(Produit produit) {
        int quantity = produit.getQuantite();
        produitViewModel.updateProduit(produit.getId(), produit.getQuantite() + 1);
        if (quantity == 0) {
            setProduitsDisponibles(colonneTri, trierPar, "");
        }
    }

    /**
     * Method called when clicking on a product - Opens DetailProduitFragment
     *
     * @param produit
     */
    @Override
    public void onProductItemClickListener(Produit produit) {
        // Launch the view for product's detail
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        String[] params = new String[]{produit.getId() + "", "-1"};
        transaction.replace(R.id.root_inventaire_frame, DetailProduitFragment.newInstance(params));
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack("toDetailProduct");
        transaction.commit();
    }

    /**
     * Called method to load defaults products from a JSON file contained in assets folder
     *
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
     *
     * @param context  context of the activity
     * @param fileName name of the JSON file that contains defaults products
     * @return List of ProductDefaut
     */
    public ArrayList<ProduitDefaut> getProduitsDefaults(Context context, String fileName) {
        ArrayList<ProduitDefaut> produits = new ArrayList<>();
        String data = loadJSONFromAsset(context, fileName);
        try {
            JSONObject defaultProducts = new JSONObject(data);
            JSONArray products = defaultProducts.getJSONArray("products");
            for (int i = 0; i < products.length(); i++) {
                String nom = products.getJSONObject(i).getString("name");
                String url_image = products.getJSONObject(i).getString("img_url");
                String rayon = products.getJSONObject(i).getString("rayon");
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

    public String getNamePiece()
    {
        return getPiece(mPiece);
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

    private void refreshSortCriteria(String[] parts) {
        switch (parts[0]) {
            case "Nom":
                this.colonneTri = "nom";
                break;
            case "Rayon":
                this.colonneTri = "rayon";
                break;
            case "Date":
                this.colonneTri = "dlc";
                break;
            case "Prix":
                this.colonneTri = "prix";
                break;

        }
        switch (parts[1]) {
            case "[A-Z]":
                this.trierPar = "ASC";
                break;
            case "[Z-A]":
                this.trierPar = "DESC";
                break;
            case "[- récent]":
                this.trierPar = "ASC";
                break;
            case "[+ récent]":
                this.trierPar = "DESC";
                break;
            case "[croissant]":
                this.trierPar = "ASC";
                break;
            case "[décroissant]":
                this.trierPar = "DESC";
                break;
        }
    }

    private String getPiece(String mPiece) {
        switch (mPiece) {
            case "CUISINE":
                return "Cuisine";
            case "SALLE_DE_BAIN":
                return "Salle de bain";
            case "CAVE":
                return "Cave";
            case "GARAGE":
                return "Garage";
            case "SALLE_A_MANGER":
                return "Séjour";
            case "CHAMBRE":
                return "Chambre";
            case "DIVERS":
                return "Divers";
            default:
                return "Divers";
        }
    }
}
