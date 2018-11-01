package com.sem.lamoot.elati.danstonplacard.danstonplacard;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 4;
    private String tabTitle[]= new String[] {"Inventaire", "Listes", "Recettes", "Promotions"};
    private Context context;

    public SampleFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }


    @Override
    public Fragment getItem(int i) {
        return PageFragment.newInstance(i + 1);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    public CharSequence getPageTitle(int i)
    {
        return tabTitle[i];
    }
}
