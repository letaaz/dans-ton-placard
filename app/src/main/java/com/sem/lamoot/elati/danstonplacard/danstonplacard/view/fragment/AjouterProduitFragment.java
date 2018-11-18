package com.sem.lamoot.elati.danstonplacard.danstonplacard.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sem.lamoot.elati.danstonplacard.danstonplacard.R;

public class AjouterProduitFragment extends Fragment {

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
        return inflater.inflate(R.layout.ajouter_produit_fragment, container, false);
    }

}
