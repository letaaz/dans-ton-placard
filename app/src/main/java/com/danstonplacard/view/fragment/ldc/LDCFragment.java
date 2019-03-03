package com.danstonplacard.view.fragment.ldc;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.danstonplacard.database.RoomDB;
import com.danstonplacard.database.dao.ListeCoursesDao;
import com.danstonplacard.database.dao.ProduitDao;
import com.danstonplacard.database.model.ListeCourses;
import com.danstonplacard.database.model.Produit;
import com.danstonplacard.viewmodel.ListeCoursesViewModel;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.danstonplacard.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment that contains the list of shopping lists
 */
public class LDCFragment extends Fragment implements LDCAdapter.OnItemClickListener {

    public static String ARGS = "ARGUMENTS_LDC_FRAG";
    public static Integer NEW_LDC = 0x999999;
    private Context mContext;
    private RecyclerView ldcDisponiblesRecyclerView, historyLdcRecyclerView;
    private ImageView btn_hide_show_history, btn_hide_show_ldc;
    private FloatingActionButton btn_create_ldc_fab;
    private ListeCoursesDao listeCoursesDao = null;
    private ProduitDao produitDao = null;
    private ListeCoursesViewModel listeCoursesViewModel;
    private View view;


    public static Fragment newInstance(String params) {
        Bundle args = new Bundle();
        args.putString(ARGS, params);
        LDCFragment ldcFragment = new LDCFragment();
        ldcFragment.setArguments(args);
        return ldcFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getContext();
    }

    @Override
    public void onResume() {
        super.onResume();
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(mContext);
        firebaseAnalytics.setCurrentScreen(this.getActivity(), this.getClass().getSimpleName(), this.getClass().getSimpleName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.ldc_fragment, container, false);

        // Get DAO
        listeCoursesDao = RoomDB.getDatabase(getContext()).listeCoursesDao();
        produitDao = RoomDB.getDatabase(getContext()).produitDao();

        // Get ViewModel
        listeCoursesViewModel = ViewModelProviders.of(this).get(ListeCoursesViewModel.class);

        // Gets items from layout
        btn_create_ldc_fab = view.findViewById(R.id.ajout_ldc_fab);
        NestedScrollView nestedScrollViewLDC = view.findViewById(R.id.nestedScrollViewLDC);
        btn_hide_show_ldc = view.findViewById(R.id.btn_expand_ldc_list);
        btn_hide_show_history = view.findViewById(R.id.btn_expand_history_list);
        RelativeLayout sectionLdc = view.findViewById(R.id.section_ldc_list);
        RelativeLayout sectionHistory = view.findViewById(R.id.section_history_list);

        // Set datas LDC
        setLDC(listeCoursesViewModel);
        // Set datas LDC Archivees
        setLDCArchivees(listeCoursesViewModel);

        setOnClickBtnCreateLDC();
        hideFloatingButtonWhenScrolling(nestedScrollViewLDC);

        // Hide/Show buttons
        setBehaviorToShowHideListOfShoppingLists(sectionLdc);
        setBehaviorToShowHideListOfShoppingListArchived(sectionHistory);

        return view;
    }

    /**
     * Method called to set behavior when user click on button on the right of the title of the list of shopping list
     * The list will be hide and recycler resize
     * @param sectionHistory
     */
    private void setBehaviorToShowHideListOfShoppingListArchived(RelativeLayout sectionHistory) {
        sectionHistory.setOnClickListener(v -> {
            if (historyLdcRecyclerView.getVisibility() == View.GONE) {
                btn_hide_show_history.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.drag_list_down_dark));
                historyLdcRecyclerView.setVisibility(View.VISIBLE);
            } else { // Assuming the view is visible
                btn_hide_show_history.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.drag_list_up_dark));
                historyLdcRecyclerView.setVisibility(View.GONE);
            }
        });
    }

    /**
     * Method called to set behavior when user click on button on the right of the title of the list of shopping list archived
     * The list will be hide and recycler resize
     * @param sectionLdc
     */
    private void setBehaviorToShowHideListOfShoppingLists(RelativeLayout sectionLdc) {
        sectionLdc.setOnClickListener(v -> {
            if (ldcDisponiblesRecyclerView.getVisibility() == View.GONE) {
                btn_hide_show_ldc.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.drag_list_down_dark));
                ldcDisponiblesRecyclerView.setVisibility(View.VISIBLE);
            } else { // Assuming the view is visible
                btn_hide_show_ldc.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.drag_list_up_dark));
                ldcDisponiblesRecyclerView.setVisibility(View.GONE);
            }
        });
    }

    /**
     * Method called to hide the floating button when the user scrolls in the list of lists to prevent the button from hiding lists.
     * @param nestedScrollViewLDC
     */
    private void hideFloatingButtonWhenScrolling(NestedScrollView nestedScrollViewLDC) {
        nestedScrollViewLDC.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY > oldScrollY) {
                btn_create_ldc_fab.hide();
            } else {
                btn_create_ldc_fab.show();
            }
        });
    }

    /**
     * Method called to set behavior to floating button
     * Method will call Fragment LDCEditFragment
     */
    private void setOnClickBtnCreateLDC() {
        btn_create_ldc_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the view for adding a product to the current piece
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.root_ldc_frame, LDCEditFragment.newInstance(NEW_LDC));
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    /**
     * Set LDC Archivees datas to RecyclerView
     * @param listeCoursesViewModel
     */
    private void setLDCArchivees(ListeCoursesViewModel listeCoursesViewModel) {
        // RecyclerView hist LDC
        LDCAdapter historyAdapter = new LDCAdapter(mContext, this);
        listeCoursesViewModel.getListesCoursesArchivees().observe(this, ldc_archivees -> historyAdapter.setData(ldc_archivees));

        setRecyclerForLDCArchivees(historyAdapter);

    }

    /**
     * Set recyclerView of LDC Archivees
     * @param historyAdapter
     */
    private void setRecyclerForLDCArchivees(LDCAdapter historyAdapter) {
        historyLdcRecyclerView = view.findViewById(R.id.history_ldc_recyclerview);

        historyLdcRecyclerView.setItemAnimator(new DefaultItemAnimator());
        historyLdcRecyclerView.setAdapter(historyAdapter);

        RecyclerView.LayoutManager historyLayoutManager = new LinearLayoutManager(getActivity());
        historyLdcRecyclerView.setLayoutManager(historyLayoutManager);
        historyLdcRecyclerView.setNestedScrollingEnabled(false);
    }

    /**
     * Set LDC to RecyclerView
     * @param listeCoursesViewModel
     */
    private void setLDC(ListeCoursesViewModel listeCoursesViewModel) {
        LDCAdapter ldcAdapter = new LDCAdapter(mContext, this);
        listeCoursesViewModel.getListesCoursesDisponibles().observe(this, ldc_disponibles -> ldcAdapter.setData(ldc_disponibles));

        setRecyclerForLDC(ldcAdapter);
    }

    /**
     * Set REcyclerView of LDC
     * @param ldcAdapter
     */
    private void setRecyclerForLDC(LDCAdapter ldcAdapter) {
        // RecyclerView LDC
        ldcDisponiblesRecyclerView = view.findViewById(R.id.ldc_list_recyclerview);
        ldcDisponiblesRecyclerView.setNestedScrollingEnabled(false);
        ldcDisponiblesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        ldcDisponiblesRecyclerView.setAdapter(ldcAdapter);

        RecyclerView.LayoutManager ldcLayoutManager = new LinearLayoutManager(getActivity());
        ldcDisponiblesRecyclerView.setLayoutManager(ldcLayoutManager);
        ldcDisponiblesRecyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    public void onItemClickListener(ListeCourses ldcDefaut) {
        if(ldcDefaut.getId() == 1) // Liste automatique - Récupération des produits indisponibles
        {
            List<Produit> produitsIndisponibles = produitDao.getAllProduitsIndisponibles();
            updateListeAutomatique(produitsIndisponibles, ldcDefaut);
        }

        // Launch the view for liste de course  detail
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.root_ldc_frame, DetailLDCFragment.newInstance(ldcDefaut.getId()));
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack("toDetailLDC");
        transaction.commit();
    }

    /**
     * This function will compare the missing products (in the inventory) and the products that are in the shopping list generated from the missing products.
     * If the unavailable products in the inventory are not the same as the generated shopping list, the generated shopping list will be updated with the deleted products or products that have been added.
     * The comparison is made on the product IDs.
     * @param allIndispos Lists of unavailable products (in the inventory)
     * @param li Generated Shopping list
     */
    private void updateListeAutomatique(List<Produit> allIndispos, ListeCourses li)
    {
        List<Produit> produitsInLDC = new ArrayList<>();
        List<Produit> aPrendre = li.getProduitsAPrendre();
        List<Produit> estPris = li.getProduitsPris();
        produitsInLDC.addAll(aPrendre);
        produitsInLDC.addAll(estPris);
        boolean allIndispoContainsProductLDC = checkIfProductsListsAreSame(produitsInLDC, allIndispos);

        if(! allIndispoContainsProductLDC){ //Les deux listes sont différentes - il faut mettre jour aPrendre et estPris
            List<Integer> idInLDC = new ArrayList<>();
            List<Integer> idInIndispo = new ArrayList<>();
            for(Produit produit : allIndispos){ idInIndispo.add(produit.getId());}
            for(Produit produit : produitsInLDC){ idInLDC.add(produit.getId());}
            if(allIndispos.size() < produitsInLDC.size()){ // On a supprimé des produits indisponibles - Il faut retirer de aPrendre/estPris
                idInLDC.removeAll(idInIndispo);
                Produit produit = null;
                for(Integer id : idInLDC) {
                    produit = produitDao.findProductById(id);
                    if(produit != null) {
                        if (checkIfProductListContainsProduct(aPrendre, produit)) {  // produit est dans aPrendre
                            removeProductFromList(aPrendre, produit);
                        } else { // produti est dans estPris
                            removeProductFromList(estPris, produit);
                        }
                    }
                    else
                    {
                        for(Produit p : aPrendre) {
                            if(p.getId() == id){aPrendre.remove(p); break;}
                        }
                        for(Produit p : estPris) {
                            if(p.getId() == id){estPris.remove(p); break;}
                        }
                    }
                    li.setProduitsPris(estPris);
                    li.setProduitsAPrendre(aPrendre);
                    listeCoursesDao.updateListe(li);
                }
            }
            else{ // On a ajouté des produits aux indisponibles - Il faut ajouter à aPrendre
                idInIndispo.removeAll(idInLDC);
                Produit produit = null;
                for(Integer id : idInIndispo) {
                    produit = produitDao.findProductById(id);
                    aPrendre.add(produit);
                }
                li.setProduitsAPrendre(aPrendre);
                listeCoursesDao.updateListe(li);
            }

        }
    }


    /**
     * Check if the listA and listB contains same element of type Produit. If there are equals (Product with same ID) return true - false otherwise
     * @param listA List of Produit to compare
     * @param listB List of Produit to compare
     * @return True if list A and list B contains elements of type Produit with the same ID
     */
    public boolean checkIfProductsListsAreSame(List<Produit> listA, List<Produit> listB) {
        if(listB.size() > listA.size()) {
            List<Produit> att = listA;
            listA = listB;
            listB = att;
        }
        if(listA.size() > 0 && listB.size() == 0){return false;}
        for(Produit produitA : listA) {
           for(int j = 0; j < listB.size(); j++) {
               if(produitA.getId() == listB.get(j).getId()) {
                   break;
               }
               if(j == listB.size()-1 && produitA.getId() != listB.get(j).getId()) {
                   return false;
               }
           }
        }
        return true;
    }

    /**
     * Check if list contains produit of type Produit (same ID)
     * @param list list to check
     * @param produit produit to find
     * @return true if ID of produit is same as one produit in list
     */
    public boolean checkIfProductListContainsProduct(List<Produit> list, Produit produit) {
        for(Produit pro : list) {
            if(produit.getId() == pro.getId())
                return true;
        }
        return false;
    }

    /**
     * Remove produit of type Produit from list thank to its ID
     * @param list list of Produit which we need to remove a product
     * @param produit the product to remove
     * @return the new list without the product (or same list if product not exists)
     */
    public static List<Produit> removeProductFromList(List<Produit> list, Produit produit) {
        for(int i = 0; i < list.size(); i++) {
            if(list.get(i).getId() == produit.getId()) {
                list.remove(i);
                return list;
            }
        }
        return list;
    }

}

