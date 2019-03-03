package com.danstonplacard.view.fragment.ldc;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.danstonplacard.database.RoomDB;
import com.danstonplacard.database.dao.ListeCoursesDao;
import com.danstonplacard.database.dao.ProduitDao;
import com.danstonplacard.database.model.ListeCourses;
import com.danstonplacard.database.model.Produit;
import com.danstonplacard.viewmodel.ListeCoursesViewModel;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.danstonplacard.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * A fragment called to display the detail of a shopping list.
 */
public class DetailLDCFragment extends Fragment{

    final static String ARG_LDC = "ARG_LDC";
    private int idLDC;
    private Context mContext;

    private TextView ldcProductDefaultContent;
    private RecyclerView ldcProductRecyclerview, historyListRecyclerView;
    private ImageButton btnEditLdc;
    private RelativeLayout ldcLabel;

    private ListeCoursesDao listeCoursesDao;
    private ProduitDao produitDao;

    public static Fragment newInstance(int id) {
        Bundle args = new Bundle();
        args.putInt(ARG_LDC, id);
        DetailLDCFragment detailLdcFragment = new DetailLDCFragment();
        detailLdcFragment.setArguments(args);
        return detailLdcFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mContext = this.getContext();
        if (getArguments() != null) {
            idLDC = getArguments().getInt(ARG_LDC);
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
        View view = inflater.inflate(R.layout.detail_ldc_fragment, container, false);

        getActivity().setTitle(R.string.title_activity_main);

        // Get ViewModel
        ListeCoursesViewModel listeCoursesViewModel = ViewModelProviders.of(this).get(ListeCoursesViewModel.class);

        // Get DAO
        this.listeCoursesDao = RoomDB.getDatabase(mContext).listeCoursesDao();
        this.produitDao = RoomDB.getDatabase(mContext).produitDao();


        // Gets items from Layout
        ldcProductDefaultContent = view.findViewById(R.id.ldc_product_list_default_content);
        ldcLabel = view.findViewById(R.id.ldc_label);
        TextView ldc_name_detail = view.findViewById(R.id.ldc_name_detail);
        btnEditLdc = view.findViewById(R.id.btn_edit_ldc);
        ImageButton btnRecycleLdc = view.findViewById(R.id.btn_archive_ldc);
        LinearLayout ldcBottomControl = view.findViewById(R.id.bottom_sheet);


        // Get shopping list
        ListeCourses listeCourses = listeCoursesDao.getListeCoursesById(idLDC);

        if (!listeCourses.getProduitsAPrendre().isEmpty()) {
            ldcProductDefaultContent.setVisibility(View.GONE);
        }

        ldc_name_detail.setText(listeCourses.getNom());
        setDatasProduitsAPrendre(view, listeCoursesViewModel, listeCourses);

        // If "Liste des produits manquants" - generated automatically - Its can't be edited
//        if(idLDC == 1){
//            btnEditLdc.setVisibility(View.INVISIBLE);
//        }

        // If shopping list archived - can't be edited and archived
        if(listeCourses.getEtat() == 1)
        {
            btnRecycleLdc.setVisibility(View.INVISIBLE);
            btnEditLdc.setVisibility(View.INVISIBLE);
        }

        // Set behavior to BtnEditLDC
        setOnClickToBtnEditLdc(btnEditLdc);
        // Srt behavior to BtnRecycler
        setOnClickToBtnRecycler(btnRecycleLdc, listeCourses);


        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(ldcBottomControl);
        setBottomSheetComportement(bottomSheetBehavior, ldcBottomControl);
        setDatasProduitsPris(view, ldcBottomControl, listeCoursesViewModel);

        return view;
    }

    /**
     * Get total price of product to take and taken
     * @param produitsAPrendre
     * @param produitsPris
     * @return
     */
    private float getPrixTotal(List<Produit> produitsAPrendre, List<Produit> produitsPris) {
        float prix_total = 0;
        for(Produit produit : produitsAPrendre)
        {
            if (produit.getQuantite() == 0)
                prix_total += produit.getPrix();
            else
                prix_total += (produit.getPrix() * produit.getQuantite());
        }
        for(Produit produit : produitsPris)
        {
            if (produit.getQuantite() == 0)
                prix_total += produit.getPrix();
            else
                prix_total += (produit.getPrix() * produit.getQuantite());
        }
        return prix_total;
    }

    /**
     * Method called to set products to take
     * @param view
     * @param listeCoursesViewModel
     * @param listeCourses
     */
    private void setDatasProduitsAPrendre(View view, ListeCoursesViewModel listeCoursesViewModel, ListeCourses listeCourses) {
        ldcProductRecyclerview = view.findViewById(R.id.ldc_product_list_recyclerview);
        LDCProductAdapter ldcProductAdapter = new LDCProductAdapter(mContext, idLDC, true);

        listeCoursesViewModel.getListeCoursesByIdLD(idLDC).observe(this, listeCourses1 -> ldcProductAdapter.setData(listeCourses1.getProduitsAPrendre()));
        ldcProductRecyclerview.setAdapter(ldcProductAdapter);
        RecyclerView.LayoutManager ldcProductLayoutManager = new LinearLayoutManager(mContext);
        ldcProductRecyclerview.setLayoutManager(ldcProductLayoutManager);

        float prix_total = getPrixTotal(listeCourses.getProduitsAPrendre(), listeCourses.getProduitsPris());

        /* show Prix Total liste (droite)  */
        TextView price_product_right = view.findViewById(R.id.price_product_right);
        DecimalFormat df = new DecimalFormat("###.##");
        price_product_right.setText(df.format(prix_total)  + " €");
    }

    /**
     * Method called to set behavior to btn EditLDC
     * @param btnEditLdc
     */
    private void setOnClickToBtnEditLdc(ImageButton btnEditLdc) {
        btnEditLdc.setOnClickListener(v -> {
            getFragmentManager().popBackStack();

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.root_ldc_frame, LDCEditFragment.newInstance(idLDC));
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.addToBackStack("toEditLDC");
            transaction.commit();
        });
    }

    /**
     * Method called to set behavior to RecylerBTN
     * @param btnRecycleLdc
     * @param listeCourses
     */
    private void setOnClickToBtnRecycler(ImageButton btnRecycleLdc, ListeCourses listeCourses) {
        // Archiver liste de courses
        btnRecycleLdc.setOnClickListener(v -> {
            // Set archive field to 1
            if(listeCourses.getId() == 1) { // If "Liste de course produits manquants" - Create copy of list and update product in inventory
                ListeCourses upListeCourse = listeCoursesDao.getListeCoursesById(1);
                ListeCourses li = new ListeCourses(upListeCourse);
                li.setEtat(1);

                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String strDate = formatter.format(date);

                li.setDateArchive(date);
                li.setNom("Liste des produits manquants du " + strDate);

                listeCoursesDao.insert(li);

                for(Produit produit : li.getProduitsPris()) {
                    produitDao.updateQuantityById(produit.getId(), produit.getQuantite());
                }
            }
            else { // Update LDC and Product in inventory
                ListeCourses li = listeCoursesDao.getListeCoursesById(listeCourses.getId());

                li.setEtat(1);
                li.setDateArchive(new Date());
                listeCoursesDao.updateListe(li);

                for (Produit produit : li.getProduitsPris()) {
                    if(produit.getQuantite() != 0) {
                        produitDao.updateQuantityById(produit.getId(), produit.getQuantite());
                    }
                    else {
                        produitDao.updateQuantityById(produit.getId(), 1);
                    }
                }
            }
            getFragmentManager().popBackStack();
        });
    }

    /**
     * Method called to set behavior to BottomSheet
     * @param bottomSheetBehavior
     * @param ldcBottomControl
     */
    public void setBottomSheetComportement(BottomSheetBehavior bottomSheetBehavior, LinearLayout ldcBottomControl){
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@android.support.annotation.NonNull View view, int newState) {
                ImageView arrow_up = ldcBottomControl.findViewById(R.id.view_show_history);
                ImageView arrow_down = ldcBottomControl.findViewById(R.id.view_show_ldc_list);
                ConstraintLayout peekLayout = ldcBottomControl.findViewById(R.id.bottom_sheet_control);
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    arrow_down.setVisibility(View.VISIBLE);
                    //peekLayout.setBackgroundColor(Color.DKGRAY);
                    peekLayout.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
                    ldcProductRecyclerview.setVisibility(View.GONE);
                    arrow_up.setVisibility(View.GONE);
                    ldcLabel.setVisibility(View.GONE);
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    arrow_up.setVisibility(View.VISIBLE);
                    ldcLabel.setVisibility(View.VISIBLE);
                    peekLayout.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
                    ldcProductRecyclerview.setVisibility(View.VISIBLE);
                    arrow_down.setVisibility(View.GONE);
                }
            }

            @Override
            public void onSlide(@android.support.annotation.NonNull View view, float v) {
                return;
            }
        });
    }

    /**
     * Method called to set products taken
     * @param view
     * @param ldcBottomControl
     * @param listeCoursesViewModel
     */
    public void setDatasProduitsPris(View view, LinearLayout ldcBottomControl, ListeCoursesViewModel listeCoursesViewModel)
    {
        historyListRecyclerView = ldcBottomControl.findViewById(R.id.bottom_sheet_content_ldc_history_recyclerview);
        LDCProductAdapter historyAdapter = new LDCProductAdapter(mContext, idLDC, false);

        listeCoursesViewModel.getListeCoursesByIdLD(idLDC).observe(this, listeCourses1 -> {
            List<Produit> estPris = listeCourses1.getProduitsPris();
            historyAdapter.setData(estPris);
            float prix_estPris = 0;
            for(Produit produit : estPris)
            {
                if (produit.getQuantite() == 0)
                    prix_estPris += produit.getPrix();
                else
                    prix_estPris += (produit.getPrix() * produit.getQuantite());
            }
            TextView price_caddie = view.findViewById(R.id.price_product_caddie);
            DecimalFormat df = new DecimalFormat("###.##");
            price_caddie.setText(df.format(prix_estPris) + " €");
        });

        historyListRecyclerView.setAdapter(historyAdapter);
        RecyclerView.LayoutManager historyLayoutManager = new LinearLayoutManager(mContext);
        historyListRecyclerView.setLayoutManager(historyLayoutManager);
    }
}
