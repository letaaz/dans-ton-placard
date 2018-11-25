package com.sem.lamoot.elati.danstonplacard.danstonplacard.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.sem.lamoot.elati.danstonplacard.danstonplacard.R;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Piece;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.view.fragment.PieceFragment;

public class InventaireFragment extends Fragment {

    public static String ARGS = "ARGUMENTS_PIECES_FRAG";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public static InventaireFragment inventaireFragment;
    public static Bundle args;

    public static Fragment newInstance(String params) {
        args = new Bundle();
        args.putString(ARGS, params);
        inventaireFragment = new InventaireFragment();
        inventaireFragment.setArguments(args);
        return inventaireFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.inventaire_fragment, container, false);
        initImageButton(view);
        return view;
    }

    private void initImageButton(View rootView) {
        ImageButton kitchen = (ImageButton) rootView.findViewById(R.id.kitchen_btn);
        kitchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "KITCHEN CLICKED", Toast.LENGTH_SHORT).show();
                showPieceFragment(Piece.CUISINE);
            }
        });

        ImageButton bathroom = (ImageButton) rootView.findViewById(R.id.bathroom_btn);
        bathroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "BATHROOM CLICKED", Toast.LENGTH_SHORT).show();
                showPieceFragment(Piece.SALLE_DE_BAIN);
            }
        });

        ImageButton basement = (ImageButton) rootView.findViewById(R.id.bathroom_btn);
        basement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "BASEMENT CLICKED", Toast.LENGTH_SHORT).show();
                showPieceFragment(Piece.CAVE);
            }
        });

        ImageButton dining_room = (ImageButton) rootView.findViewById(R.id.bathroom_btn);
        dining_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "DINING ROOM CLICKED", Toast.LENGTH_SHORT).show();
                showPieceFragment(Piece.SALLE_A_MANGER);
            }
        });
    }

    private void showPieceFragment(Piece piece) {
        args.putString("PIECE", piece.toString());
        inventaireFragment.setArguments(args);
        FragmentTransaction trans = getFragmentManager().beginTransaction();
        trans.replace(R.id.root_frame, PieceFragment.newInstance(piece.toString()));
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        trans.addToBackStack(null);
        trans.commit();
    }

    private void showFragmentBathroom() {
        // TODO : (replace the root_frame by a Bathroom Fragment)
    }
}
