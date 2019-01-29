package com.danstonplacard.view.fragment.ldc;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.danstonplacard.R;
import com.danstonplacard.database.RoomDB;
import com.danstonplacard.database.converter.DateTypeConverter;
import com.danstonplacard.database.dao.ListeCoursesDao;
import com.danstonplacard.database.model.ListeCourses;
import com.danstonplacard.database.model.Produit;
import com.danstonplacard.view.fragment.AjouterProduitFragment;
import com.danstonplacard.view.fragment.DetailProduitFragment;
import com.danstonplacard.view.fragment.inventaire.ProduitAdapter;
import com.danstonplacard.viewmodel.ListeCoursesViewModel;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;

/**
 * Fragment called to edit a shopping list
 */
public class LDCEditFragment extends Fragment
        implements ProduitAdapter.OnMinusImageViewClickListener,
        ProduitAdapter.OnAddImageViewClickListener,
        ProduitAdapter.OnProductItemClickListener {

    public static String ARG_LDC = "";
    private Context mContext;
    private int idLdc;

    private ImageButton ldcSaveEdit;
    private RecyclerView ldcEditProductRecyclerView;
    private FloatingActionButton ldcEditAddProduct;
    private TextInputLayout ldcInputLayout;
    private EditText ldcNameEdit;

    private ListeCoursesDao listeCoursesDao;
    private ListeCourses listeCourse;

    public static Fragment newInstance(Integer param) {
        Bundle args = new Bundle();
        args.putInt(ARG_LDC, param);
        LDCEditFragment ldcEditFragment = new LDCEditFragment();
        ldcEditFragment.setArguments(args);
        return ldcEditFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getContext();
        if (getArguments() != null) {
            idLdc = getArguments().getInt(ARG_LDC);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(mContext);
        firebaseAnalytics.setCurrentScreen(this.getActivity(), this.getClass().getSimpleName(), this.getClass().getSimpleName());

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ldc_edit_fragment, container, false);

        // Get DAO
        listeCoursesDao = RoomDB.getDatabase(mContext).listeCoursesDao();

        // Get ViewModel
        ListeCoursesViewModel listeCoursesViewModel = ViewModelProviders.of(this).get(ListeCoursesViewModel.class);

        // Get items from Layout
        ldcSaveEdit = view.findViewById(R.id.btn_save_ldc_edit);
        ldcInputLayout = view.findViewById(R.id.ldc_edit_name_layout);
        ldcNameEdit = view.findViewById(R.id.ldc_edit_name);
        ldcEditAddProduct = view.findViewById(R.id.ldc_edit_ajout_produit_fab);
        TextView ldcEditDefaultContent = view.findViewById(R.id.ldc_edit_default_content);
        ldcEditProductRecyclerView = view.findViewById(R.id.ldc_product_edit_recyclerview);


        if (idLdc == LDCFragment.NEW_LDC) {
            listeCourse = new ListeCourses("");
            String defaultName = "Liste du " + DateTypeConverter.DATE_FORMATTER.format(listeCourse.getDateCreation());
            listeCourse.setNom(defaultName);
            idLdc = (int) listeCoursesDao.insert(listeCourse);
        } else {
            listeCourse = listeCoursesDao.getListeCoursesById(idLdc);
            if (!listeCourse.getProduitsAPrendre().isEmpty())
                ldcEditDefaultContent.setVisibility(View.GONE);
        }

        ldcNameEdit.setText(listeCourse.getNom());

        setOnClickSaveEditBtn();
        setOnClickEditAddProduct();

        setProductsAPRendreInLDC(listeCoursesViewModel);

        return view;
    }

    /**
     * Method called to set RecyclerView and products to take to the RecyclerView
     * @param listeCoursesViewModel
     */
    private void setProductsAPRendreInLDC(ListeCoursesViewModel listeCoursesViewModel) {
        ProduitAdapter produitAdapter = new ProduitAdapter(mContext, this, this, idLdc);
        produitAdapter.setOnProductItemClickListener(this::onProductItemClickListener);

        listeCoursesViewModel.getListeCoursesByIdLD(idLdc).observe(this, listeCourses1 -> produitAdapter.setData(listeCourses1.getProduitsAPrendre()));

        ldcEditProductRecyclerView.setItemAnimator(new DefaultItemAnimator());
        ldcEditProductRecyclerView.setAdapter(produitAdapter);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(mContext);
        ldcEditProductRecyclerView.setLayoutManager(manager);
        ldcEditProductRecyclerView.setNestedScrollingEnabled(false);
    }

    /**
     * Method called to add behavior to add button in EditLDC
     * This method will call Fragment AjouterProductFragment
     */
    private void setOnClickEditAddProduct() {
        ldcEditAddProduct.setOnClickListener(v -> {
            // Launch the view for adding a product to the current piece
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.root_ldc_frame, AjouterProduitFragment.newInstance("DIVERS", idLdc));
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.addToBackStack(null);
            transaction.commit();
        });
    }

    /**
     * Method called to set behavior to SaveEditBTN
     */
    private void setOnClickSaveEditBtn() {
        ldcSaveEdit.setOnClickListener(v -> {
            if (!validateName()) {
                return;
            }
            listeCourse = listeCoursesDao.getListeCoursesById(idLdc);
            listeCourse.setNom(ldcNameEdit.getText().toString());
            listeCourse.setId(idLdc);
            listeCoursesDao.updateListe(listeCourse);

            getFragmentManager().popBackStack();

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.root_ldc_frame, DetailLDCFragment.newInstance(idLdc));
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.addToBackStack(null);
            transaction.commit();
        });
    }

    /**
     * Method called to check if name of the shopping list is valide
     * @return true if name is valide - false otherwise
     */
    private boolean validateName() {
        if (ldcNameEdit.getText().toString().trim().isEmpty()) {
            ldcInputLayout.setError(getString(R.string.err_msg_name));
            requestFocus(ldcNameEdit);
            return false;
        } else {
            ldcInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    /**
     * Method called when user click on (-) button
     * @param produit
     */
    @Override
    public void onMinusImageViewClickListener(Produit produit) {
        listeCourse = listeCoursesDao.getListeCoursesById(idLdc);
        List<Produit> aPrendre = listeCourse.getProduitsAPrendre();
        for (Produit p : aPrendre) {
            if (p.getId() == produit.getId() && p.getQuantite() > 0) {
                p.setQuantite(p.getQuantite() - 1);
            }
        }
        listeCourse.setProduitsAPrendre(aPrendre);
        listeCoursesDao.updateListe(listeCourse);
    }

    /**
     * Method called when user click on (+) button
     * @param produit
     */
    @Override
    public void onAddImageViewClickListener(Produit produit) {
        listeCourse = listeCoursesDao.getListeCoursesById(idLdc);
        List<Produit> aPrendre = listeCourse.getProduitsAPrendre();

        for (Produit p : aPrendre) {
            if (p.getId() == produit.getId()) {
                p.setQuantite(p.getQuantite() + 1);
            }
        }
        listeCourse.setProduitsAPrendre(aPrendre);
        listeCoursesDao.updateListe(listeCourse);
    }

    /**
     * Method called when user click on product in shopping list
     * Method will call the DetailProductFragment
     * @param produit
     */
    @Override
    public void onProductItemClickListener(Produit produit) {
        // Launch the view for product's detail
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        String[] params = new String[]{produit.getId() + "", "" + idLdc};
        transaction.replace(R.id.root_ldc_frame, DetailProduitFragment.newInstance(params));
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
