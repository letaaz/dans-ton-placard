package com.danstonplacard.view.fragment.inventaire;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.danstonplacard.PieceIcone;
import com.danstonplacard.database.model.Piece;
import com.danstonplacard.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment that contains the icons that represent the available pieces
 */
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
        return view;
    }

    /**
     * Set the RecyclerView to put Rooms and Icons
     * @param view
     */
    private void setRecyclerViewPiece(View view)
    {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.piece_recyclerview);
        GridLayoutManager mGrid = new GridLayoutManager(this.getActivity(), 2);
        recyclerView.setLayoutManager(mGrid);

        List<PieceIcone> pieces = this.getDrawablesIntPieces();

        this.setAdapterToRecyclerViewPieces(recyclerView, pieces);
    }


    /**
     * Set adapter to the RecyclerView
     * @param recyclerView
     * @param pieces
     */
    public void setAdapterToRecyclerViewPieces(RecyclerView recyclerView, List<PieceIcone> pieces)
    {
        AdapterForRecyclerViewPieces adapterForRecyclerViewPieces = new AdapterForRecyclerViewPieces(this.getContext(), pieces);
        recyclerView.setAdapter(adapterForRecyclerViewPieces);
    }


    /**
     * Get the list of available rooms associated with its image / icon
     * @return list of PieceIcone
     */
    public List<PieceIcone> getDrawablesIntPieces()
    {
        List<PieceIcone> pieces = new ArrayList<>();
        pieces.add(new PieceIcone(R.drawable.home_cuisine, Piece.CUISINE));
        pieces.add(new PieceIcone(R.drawable.home_salledebain, Piece.SALLE_DE_BAIN));
        pieces.add(new PieceIcone(R.drawable.home_sejour, Piece.SALLE_A_MANGER));
        pieces.add(new PieceIcone(R.drawable.home_cave, Piece.CAVE));
        pieces.add(new PieceIcone(R.drawable.home_chambre, Piece.CHAMBRE));
        pieces.add(new PieceIcone(R.drawable.home_garage, Piece.GARAGE));
        pieces.add(new PieceIcone(R.drawable.home_divers, Piece.DIVERS));

        return pieces;
    }


}
