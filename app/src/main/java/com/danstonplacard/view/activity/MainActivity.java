package com.danstonplacard.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.ColorRes;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;

import com.danstonplacard.R;
import com.danstonplacard.view.SampleFragmentPagerAdapter;
import com.danstonplacard.view.fragment.inventaire.PieceFragment;

/**
 * Main activity of the application - Contains the toolbar - the fragments - Navigation Drawer
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TabLayout tabLayout;
    private int tabPosition;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private Activity thisActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.thisActivity = this;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /* content_main xml */ /* Help : https://guides.codepath.com/android/Google-Play-Style-Tabs-using-TabLayout#sliding-tabs-layout*/
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager(), this));

        /* Set the tabLayout of the application*/
        setTabLayout(viewPager);

        //startActivity(new Intent(this, AppIntroActivity.class));

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Initialize SharedPreferences
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                //  Create a new boolean and preference and set it to true
                boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

                //  If the activity has never started before...
                if (isFirstStart) {

                    //  Launch app intro
                    final Intent i = new Intent(MainActivity.this, AppIntroActivity.class);

                    runOnUiThread(() -> startActivity(i));

                    //  Make a new preferences editor
                    SharedPreferences.Editor e = getPrefs.edit();

                    //  Edit preference to make it false because we don't want this to run again
                    e.putBoolean("firstStart", false);

                    //  Apply changes
                    e.apply();
                }
            }
        });

        // Start the thread
        t.start();
    }

    /**
     * Set the tabLayout
     *
     * @param viewPager The ViewPager which contains the TabLayout
     */
    private void setTabLayout(ViewPager viewPager) {
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        /* Set icons to TabLayout */
//        int[] imageResId = {R.drawable.ic_fridge, R.drawable.ic_list, R.drawable.ic_recipe_book, R.drawable.ic_discount};
        int[] imageResId = {R.drawable.ic_fridge, R.drawable.ic_list};
        for (int i = 0; i < imageResId.length; i++) {
            tabLayout.getTabAt(i).setIcon(imageResId[i]);
        }




        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabPosition = tab.getPosition();
                if (tab.getPosition() == 1) {
                    thisActivity.setTitle(R.string.title_activity_main);
                    MenuItem itemArrowLeft = toolbar.getMenu().findItem(R.id.action_piece_precedente);
                    MenuItem itemArrowRight = toolbar.getMenu().findItem(R.id.action_piece_suivante);

                    if(itemArrowLeft != null){
                        itemArrowLeft.setVisible(false);
                    }
                    if(itemArrowRight != null){ itemArrowRight.setVisible(false);}
                }
                if(tab.getPosition() == 0)
                {
                    PieceFragment pieceFragment = (PieceFragment) getSupportFragmentManager().findFragmentByTag("PieceFragment");
                    if(pieceFragment != null && pieceFragment.isVisible())
                    {
                        thisActivity.setTitle(pieceFragment.getNamePiece());
                        MenuItem itemArrowLeft = toolbar.getMenu().findItem(R.id.action_piece_precedente);
                        MenuItem itemArrowRight = toolbar.getMenu().findItem(R.id.action_piece_suivante);
                        if(itemArrowLeft != null){
                            itemArrowLeft.setVisible(true);
                        }
                        if(itemArrowRight != null){ itemArrowRight.setVisible(true);}
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                FragmentManager fm = getSupportFragmentManager();

                if (fm.getBackStackEntryCount() > 0) {
                    if (tabPosition == 0) {
                        fm.popBackStack("changePiece", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        fm.popBackStack("toPiece", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        fm.popBackStack("toDetail", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    } else {
                        fm.popBackStack("toDetailLDC", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        fm.popBackStack("toAjoutProduitLDC", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        fm.popBackStack("toEditLDC", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        fm.popBackStack("fromEdittoDetailLDC", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (tabPosition != 0 && this.getSupportFragmentManager().getBackStackEntryCount() == 0) {
            tabLayout.getTabAt(0).select();

        } else {
            super.onBackPressed();
        }
    }

    /**
     * Method that manages the different actions to be performed by the items in the navigation drawer
     *
     * @param item The menuItem selected by the user
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        resetAllMenuItemsTextColor(navigationView);
        boolean selected = false;

        switch (item.getItemId()) {
            case R.id.nav_accueil:
                selected = true;
                setTextColorForMenuItem(item, R.color.white);
                break;
            case R.id.nav_blog:
                openPage("http://danstonplacardapp.wordpress.com");
                break;
            case R.id.nav_facebook:
                String facebookPageURLurl = getFacebookPageURL(this);
                openPage(facebookPageURLurl);
                break;
            case R.id.nav_about:
                Intent it = new Intent(this, AboutActivity.class);
                startActivity(it);
                break;
            case R.id.nav_notez_nous:
                String googlePlayRedirect = "market://details?id=" + getPackageName();
                openPage(googlePlayRedirect);
                break;
            default:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return selected;
    }

    /**
     * Method used to open a web page in a browser
     *
     * @param url the address link to open
     */
    private void openPage(String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }


    /**
     * Get the link to Facebook Page of the Application - In the format compatible with the mobile application Facebook
     *
     * @param context Context of the activity
     * @return valid address
     */
    private String getFacebookPageURL(Context context) {
        PackageManager packageManager = context.getPackageManager();
        String FACEBOOK_URL = "https://www.facebook.com/danstonplacardapp/";
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                long FACEBOOK_PAGE_ID = 255891538403374l;
                return "fb://page/" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL;
        }
    }

    private void setTextColorForMenuItem(MenuItem menuItem, @ColorRes int color) {
        SpannableString spanString = new SpannableString(menuItem.getTitle().toString());
        spanString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, color)), 0, spanString.length(), 0);
        menuItem.setTitle(spanString);
    }

    private void resetAllMenuItemsTextColor(NavigationView navigationView) {
        for (int i = 0; i < navigationView.getMenu().size(); i++)
            setTextColorForMenuItem(navigationView.getMenu().getItem(i), R.color.text_black);
    }
}
