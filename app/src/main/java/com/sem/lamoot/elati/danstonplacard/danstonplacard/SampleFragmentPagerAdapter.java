package com.sem.lamoot.elati.danstonplacard.danstonplacard;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.Toast;


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
        //return PageFragment.newInstance(i + 1);
        Toast.makeText(context, "JE SUIS DANS GETITEM", Toast.LENGTH_SHORT).show();
        switch (i)
        {
            case 0:
                return ListePiecesFragment.newInstance("Param");
            case 1:
                return PageFragment.newInstance(i+1);

            default :
                return PageFragment.newInstance(i+1);
        }
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    public CharSequence getPageTitle(int i)
    {
        //return tabTitle[i];
        return null;
    }
}
