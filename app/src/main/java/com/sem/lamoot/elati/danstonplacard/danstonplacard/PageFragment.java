package com.sem.lamoot.elati.danstonplacard.danstonplacard;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class PageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    private View view;


    public static PageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        switch (mPage) {
            case 1:
                view = inflater.inflate(R.layout.fragment_pieces_list, container, false);
                initImageButton();
//                Pieces_list nextFrag= new Pieces_list();
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.viewpager, nextFrag,"findThisFragment")
//                        .addToBackStack(null)
//                        .commit();
                break;

            default:
                view = inflater.inflate(R.layout.fragment_page, container, false);
                TextView textView = (TextView) view;
                textView.setText("Fragment #" + mPage);
                break;

        }
        return view;
    }

    private void initImageButton() {
        ImageButton kitchen = (ImageButton) this.view.findViewById(R.id.kitchen_btn);
        kitchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "KITCHEN CLICKED", Toast.LENGTH_LONG).show();
            }
        });

        ImageButton bathroom = (ImageButton) this.view.findViewById(R.id.bathroom_btn);
        bathroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "BATHROOM CLICKED", Toast.LENGTH_LONG).show();
            }
        });


    }

}
