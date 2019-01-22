package com.sem.lamoot.elati.danstonplacard.danstonplacard.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sem.lamoot.elati.danstonplacard.danstonplacard.R;

public class PageFragment extends Fragment{

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
            case 3:
                return inflater.inflate(R.layout.recipe_fragment_layout, container, false);
            case 4:
                return inflater.inflate(R.layout.promotion_fragment_layout, container, false);
            default:
                return view = inflater.inflate(R.layout.fragment_page, container, false);
        }
    }
}
