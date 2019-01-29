package com.danstonplacard.view.fragment.inventaire;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.danstonplacard.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link RootInventaireFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RootInventaireFragment extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param Parameter 1.
     * @return A new instance of fragment RootInventaireFragment.
     */
    public static RootInventaireFragment newInstance(String param) {
        Bundle args = new Bundle();
        args.putString(ARG_PAGE, param);
        RootInventaireFragment fragment = new RootInventaireFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam = getArguments().getString(ARG_PAGE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.root_inventaire_fragment, container, false);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.root_inventaire_frame, new InventaireFragment());
        transaction.commit();
        return view;
    }

}
