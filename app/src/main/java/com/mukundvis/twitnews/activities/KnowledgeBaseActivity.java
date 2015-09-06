package com.mukundvis.twitnews.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.mukundvis.twitnews.R;
import com.mukundvis.twitnews.fragments.ShowKeywordsFragment;
import com.mukundvis.twitnews.fragments.ShowTaxonomyFragment;

/**
 * Created by mukundvis on 26/06/15.
 */
public class KnowledgeBaseActivity extends BaseLoggedInActivity {

    private static final String DEBUG_TAG = KnowledgeBaseActivity.class.getSimpleName();

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private Toolbar toolbar;

    @Override
    protected int getDefaultLayout() {
        return R.layout.kb_activity;
    }

    @Override
    protected void getViewReferences() {
        mPager = (ViewPager) findViewById(R.id.pager);
    }

    @Override
    protected void extractArguments(Bundle arguments) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPagerAdapter = new ChangeViewsPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        setupActionBar();
    }


    private void setupActionBar() {
        toolbar = getToolbar();
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        TabLayout tl = new TabLayout(this);
        tl.setTabTextColors(getResources().getColor(R.color.tab_normal_color),
                getResources().getColor(R.color.tab_selected_color));
        tl.setupWithViewPager(mPager);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(tl);
    }

    private class ChangeViewsPagerAdapter extends FragmentStatePagerAdapter {

        private static final String TITLE_KEYWORDS = "Keywords";
        private static final String TITLE_TAXONOMIES = "Taxonomy";

        private static final int NUM_PAGES = 2;

        public ChangeViewsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return ShowKeywordsFragment.getInstance();
                default:
                    return ShowTaxonomyFragment.getInstance();
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // If article is not there, do not show the article tab
            switch (position) {
                case 0:
                    return TITLE_KEYWORDS;
                default:
                    return TITLE_TAXONOMIES;
            }
        }
    }

}
