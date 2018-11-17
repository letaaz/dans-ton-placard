package com.sem.lamoot.elati.danstonplacard.danstonplacard.view;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sem.lamoot.elati.danstonplacard.danstonplacard.view.fragment.PageFragment;
import com.sem.lamoot.elati.danstonplacard.danstonplacard.view.fragment.RootInventaireFragment;


public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 4;
    private String tabTitle[]= new String[] {"Inventaire", "Listes", "Recettes", "Promotions"};
    private Context context;
    private final FragmentManager mFragmentManager;

    public SampleFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.mFragmentManager = fm;
        this.context = context;

    }


    @Override
    public Fragment getItem(int i) {
        switch (i)
        {
            case 0:
                return RootInventaireFragment.newInstance("PARAM");
            default :
                return PageFragment.newInstance(i+1);
        }
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    public CharSequence getPageTitle(int i) {
        return tabTitle[i];
    }
}
