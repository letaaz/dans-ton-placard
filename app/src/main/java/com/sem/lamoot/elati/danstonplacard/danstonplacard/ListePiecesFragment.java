package com.sem.lamoot.elati.danstonplacard.danstonplacard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.sem.lamoot.elati.danstonplacard.danstonplacard.database.model.Piece;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ListePiecesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListePiecesFragment extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";

    private String mParam;

    private View view;

    public ListePiecesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param Parameter 1.
     * @return A new instance of fragment ListePiecesFragment.
     */
    public static ListePiecesFragment newInstance(String param) {
        Bundle args = new Bundle();
        args.putString(ARG_PAGE, param);
        ListePiecesFragment fragment = new ListePiecesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam = getArguments().getString(ARG_PAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_pieces_list, container, false);
        initImageButton();
        return view;
    }

    private void initImageButton() {
        ImageButton kitchen = (ImageButton) this.view.findViewById(R.id.kitchen_btn);
        kitchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "KITCHEN CLICKED", Toast.LENGTH_SHORT).show();
                showInventaireActivity(Piece.CUISINE);
            }
        });

        ImageButton bathroom = (ImageButton) this.view.findViewById(R.id.bathroom_btn);
        bathroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "BATHROOM CLICKED", Toast.LENGTH_SHORT).show();
                showInventaireActivity(Piece.SALLE_DE_BAIN);
            }
        });


    }

    private void showInventaireActivity(Piece piece) {
        Intent intent = new Intent(getActivity(), InventaireActivity.class);
        intent.putExtra("piece", piece);
        startActivity(intent);
    }

    private void showFragmentKitchen() {

        InventaireFragment inventaire_fragment= new InventaireFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.viewpager, inventaire_fragment)
                .addToBackStack(null)
                .commit();
    }




}
