package com.danstonplacard.view.activity;

import android.support.v4.app.Fragment;
import android.os.Bundle;

import com.danstonplacard.R;
import com.danstonplacard.view.fragment.SampleSlide;
import com.github.paolorotolo.appintro.AppIntro;

public class AppIntroActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(SampleSlide.newInstance(getString(R.string.app_intro_slide_1_title), getString(R.string.app_intro_slide_1_desc),
                R.drawable.logo_v1_rounded));
        addSlide(SampleSlide.newInstance(getString(R.string.app_intro_slide_2_title), getString(R.string.app_intro_slide_2_desc),
                R.drawable.app_intro_inv));
        addSlide(SampleSlide.newInstance(getString(R.string.app_intro_slide_3_title), getString(R.string.app_intro_slide_3_desc),
                R.drawable.app_intro_ldc));

        showStatusBar(false);
    }

    @Override
    public void onSkipPressed(android.support.v4.app.Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        finish();
    }

    @Override
    public void onDonePressed(android.support.v4.app.Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        finish();
    }

}
