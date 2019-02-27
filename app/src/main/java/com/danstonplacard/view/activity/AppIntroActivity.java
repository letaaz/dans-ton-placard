package com.danstonplacard.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.danstonplacard.R;
import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntro2Fragment;

public class AppIntroActivity extends AppIntro2 {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(AppIntro2Fragment.newInstance(getString(R.string.app_intro_slide_1_title), getString(R.string.app_intro_slide_1_desc),
                R.drawable.groceries, getColor(R.color.app_intro_background)));
        addSlide(AppIntro2Fragment.newInstance(getString(R.string.app_intro_slide_2_title), getString(R.string.app_intro_slide_2_desc),
                R.drawable.groceries, getColor(R.color.app_intro_background)));
        addSlide(AppIntro2Fragment.newInstance(getString(R.string.app_intro_slide_3_title), getString(R.string.app_intro_slide_3_desc),
                R.drawable.grocery, getColor(R.color.app_intro_background)));
    }

    @Override
    public void onSkipPressed(Fragment currentFragment){
        super.onSkipPressed(currentFragment);
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment){
        super.onDonePressed(currentFragment);
        finish();
    }
}
