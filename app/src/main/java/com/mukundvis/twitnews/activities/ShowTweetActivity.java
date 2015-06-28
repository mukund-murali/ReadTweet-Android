package com.mukundvis.twitnews.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.mukundvis.twitnews.R;
import com.mukundvis.twitnews.database.DBHelper;
import com.mukundvis.twitnews.fragments.ShowArticleFragment;
import com.mukundvis.twitnews.fragments.ShowTweetInfoFragment;
import com.mukundvis.twitnews.models.MyTweet;

/**
 * Created by mukundvis on 27/06/15.
 */
public class ShowTweetActivity  extends BaseActivity {

    public static final String KEY_TWEET_ID = "tweet_id";
    public static final String KEY_PAGE_TO_OPEN = "page_to_open";

    private static final String DEBUG_TAG = ShowTweetActivity.class.getSimpleName();

    public static final int PAGE_TWEET = 0;
    public static final int PAGE_ARTICLE = 1;

    int pageToOpen = PAGE_TWEET;

    long tweetId;
    MyTweet tweet;

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private TabLayout tabLayout;

    @Override
    protected int getDefaultLayout() {
        return R.layout.show_tweet;
    }

    @Override
    protected void getViewReferences() {
        mPager = (ViewPager) findViewById(R.id.pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DBHelper helper = new DBHelper(this);
        tweet = helper.getTweet(tweetId);
        if (tweet == null) {
            Toast.makeText(this, "Some error occured. Try later.", Toast.LENGTH_SHORT).show();
            this.finish();
            return;
        }
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        tabLayout.setupWithViewPager(mPager);
        mPager.setCurrentItem(pageToOpen);
    }

    @Override
    protected void extractArguments(Bundle arguments) {
        tweetId = arguments.getLong(KEY_TWEET_ID, -1);
        pageToOpen = arguments.getInt(KEY_PAGE_TO_OPEN, PAGE_TWEET);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        private static final String TITLE_TWEET = "Tweet";
        private static final String TITLE_ARTICLE = "Article";

        private static final int NUM_PAGES = 2;

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return ShowTweetInfoFragment.getInstance(tweetId);
                default:
                    return ShowArticleFragment.getInstance(tweet.getArticleURL());
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return TITLE_TWEET;
                default:
                    return TITLE_ARTICLE;
            }
        }
    }

}
