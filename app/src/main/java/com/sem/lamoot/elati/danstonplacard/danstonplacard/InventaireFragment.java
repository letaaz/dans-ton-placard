package com.sem.lamoot.elati.danstonplacard.danstonplacard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Piece;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Produit;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Rayon;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link InventaireFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InventaireFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    private String mParam1;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public InventaireFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment InventaireFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InventaireFragment newInstance(String param1) {
        InventaireFragment fragment = new InventaireFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_inventaire_fragment, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.inventaireDispo_recyclerview);
        //mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        Produit tabProduits[] = {new Produit("pizza", 1, Rayon.SURGELE, Piece.CUISINE),
                new Produit("oeufs", 1, Rayon.BIO, Piece.CUISINE),
                new Produit("pates", 1, Rayon.BIO, Piece.CUISINE)
        };

        mAdapter = new MyAdapter(tabProduits);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        TextView btn = (TextView) rootView.findViewById(R.id.section_show_all_button_dispo);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int visibilite = ((mRecyclerView.getVisibility() == View.INVISIBLE) ? View.VISIBLE : View.INVISIBLE);
                mRecyclerView.setVisibility(visibilite);
            }
        });


        return rootView;
    }
}
