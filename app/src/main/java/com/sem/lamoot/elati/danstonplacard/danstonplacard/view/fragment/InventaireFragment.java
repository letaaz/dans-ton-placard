package com.sem.lamoot.elati.danstonplacard.danstonplacard.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sem.lamoot.elati.danstonplacard.danstonplacard.PieceIcone;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Piece;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.view.AdapterForRecyclerViewPieces;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.R;

import java.util.ArrayList;
import java.util.List;

public class InventaireFragment extends Fragment {

    public static String ARGS = "ARGUMENTS_PIECES_FRAG";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public static Fragment newInstance(String params) {
        Bundle args = new Bundle();
        args.putString(ARGS, params);
        InventaireFragment inventaireFragment = new InventaireFragment();
        inventaireFragment.setArguments(args);
        return inventaireFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.inventaire_fragment, container, false);

        this.setRecyclerViewPiece(view);

        //initImageButton(view);
        return view;
    }

    private void setRecyclerViewPiece(View view)
    {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.piece_recyclerview);
        GridLayoutManager mGrid = new GridLayoutManager(this.getActivity(), 2);
        recyclerView.setLayoutManager(mGrid);

        List<PieceIcone> pieces = this.getDrawablesIntPieces();

        this.setAdapterToRecyclerViewPieces(recyclerView, pieces);

    }

    public List<PieceIcone> getDrawablesIntPieces()
    {
        List<PieceIcone> pieces = new ArrayList<>();
        pieces.add(new PieceIcone(R.drawable.cuisine, Piece.CUISINE));
        pieces.add(new PieceIcone(R.drawable.salledebain, Piece.SALLE_DE_BAIN));
        pieces.add(new PieceIcone(R.drawable.sejour, Piece.SALLE_A_MANGER));
        pieces.add(new PieceIcone(R.drawable.cave, Piece.CAVE));
        pieces.add(new PieceIcone(R.drawable.chambre, Piece.CHAMBRE));
        pieces.add(new PieceIcone(R.drawable.garage, Piece.GARAGE));
        pieces.add(new PieceIcone(R.drawable.divers, Piece.DIVERS));

        return pieces;
    }

    public void setAdapterToRecyclerViewPieces(RecyclerView recyclerView, List<PieceIcone> pieces)
    {
        AdapterForRecyclerViewPieces adapterForRecyclerViewPieces = new AdapterForRecyclerViewPieces(this.getContext(), pieces);
        recyclerView.setAdapter(adapterForRecyclerViewPieces);
    }

//    private void initImageButton(View rootView) {
//        ImageButton kitchen = (ImageButton) rootView.findViewById(R.id.kitchen_btn);
//        kitchen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showPieceFragment("CUISINE");
//            }
//        });
//
//        ImageButton bathroom = (ImageButton) rootView.findViewById(R.id.bathroom_btn);
//        bathroom.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showPieceFragment("SALLE_DE_BAIN");
//            }
//        });
//
//        ImageButton basement = (ImageButton) rootView.findViewById(R.id.cellar_btn);
//        basement.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showPieceFragment("CAVE");
//            }
//        });
//
//        ImageButton dining_room = (ImageButton) rootView.findViewById(R.id.dinner_btn);
//        dining_room.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showPieceFragment("SALLE_A_MANGER");
//            }
//        });
//    }

    private void showPieceFragment(String piece) {
        FragmentTransaction trans = getFragmentManager().beginTransaction();
        trans.replace(R.id.root_frame, PieceFragment.newInstance(piece));
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        trans.addToBackStack(null);
        trans.commit();
    }
}
