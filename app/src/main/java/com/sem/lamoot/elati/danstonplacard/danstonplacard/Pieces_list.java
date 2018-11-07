package com.sem.lamoot.elati.danstonplacard.danstonplacard;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Pieces_list.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Pieces_list#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Pieces_list extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";

    private String mParam;

    private View view;

    public Pieces_list() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param Parameter 1.
     * @return A new instance of fragment Pieces_list.
     */
    public static Pieces_list newInstance(String param) {
        Bundle args = new Bundle();
        args.putString(ARG_PAGE, param);
        Pieces_list fragment = new Pieces_list();
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
        Log.d("click", "onCreateView");
        initUI(view);
        Log.d("click", "end onCreateView");
        return view;
    }

    private void initUI(View view) {
        Log.d("click", "IM INSIDE INITUI");
        ImageButton kitchen = (ImageButton) view.findViewById(R.id.kitchen_btn);
        kitchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "KITCHEN CLICKED", Toast.LENGTH_LONG).show();
                Log.d("click","CLICKED KITCHEN");
            }
        });
    }



}
