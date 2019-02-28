package com.danstonplacard.view.fragment;


import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.danstonplacard.R;

public class SampleSlide extends android.support.v4.app.Fragment {

    private static final String ARG_TITLE = "slideTitle";
    private static final String ARG_DESC = "slideDesc";
    private static final String ARG_IMAGE_RES_ID = "slideImageResId";

    private String title, desc;
    private int imageResId;

    public static SampleSlide newInstance(String title, String desc, int imageResId) {
        SampleSlide sampleSlide = new SampleSlide();

        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_DESC, desc);
        args.putInt(ARG_IMAGE_RES_ID, imageResId);
        sampleSlide.setArguments(args);

        return sampleSlide;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(ARG_TITLE) && getArguments().containsKey(ARG_DESC)
                && getArguments().containsKey(ARG_IMAGE_RES_ID)) {
            title = getArguments().getString(ARG_TITLE);
            desc = getArguments().getString(ARG_DESC);
            imageResId = getArguments().getInt(ARG_IMAGE_RES_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View slide = inflater.inflate(R.layout.activity_app_intro, container, false);

        TextView slideTitle = slide.findViewById(R.id.slide_intro_title);
        slideTitle.setText(title);
        slideTitle.setTextColor(Color.WHITE);

        ImageView slideImg = slide.findViewById(R.id.slide_intro_img);
        slideImg.setImageDrawable(getContext().getDrawable(imageResId));

        TextView slideDesc = slide.findViewById(R.id.slide_intro_desc);
        slideDesc.setText(desc);
        slideDesc.setTextColor(Color.WHITE);

        slide.setBackgroundColor(getContext().getColor(R.color.app_intro_background));

        return slide;
    }
}
