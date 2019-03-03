package com.danstonplacard.view.fragment.ldc;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.danstonplacard.R;

/**
 * Fragment that will contain all the fragment of LDC Feature
 */
public class RootLDCFragment extends Fragment{

    public static final String ARG_PAGE = "ARG_PAGE";

    public static Fragment newInstance(String param) {
        Bundle args = new Bundle();
        args.putString(ARG_PAGE, param);
        RootLDCFragment fragment = new RootLDCFragment();
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
        View view = inflater.inflate(R.layout.root_ldc_fragment, container, false);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.root_ldc_frame, LDCFragment.newInstance("PARAM"));
        transaction.commit();
        return view;
    }
}
