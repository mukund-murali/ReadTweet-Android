package com.mukundvis.twitnews.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mukundvis.twitnews.R;
import com.mukundvis.twitnews.database.DBHelper;
import com.mukundvis.twitnews.models.MyTweet;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TweetView;

/**
 * Created by mukundvis on 28/06/15.
 */
public class ShowTweetInfoFragment extends BaseFragment {

    private static final String KEY_TWEET_ID = "tweet_id";

    long tweetId = -1;

    private LinearLayout llContainer;

    public static ShowTweetInfoFragment getInstance(long tweetId) {
        Bundle args = new Bundle();
        args.putLong(KEY_TWEET_ID, tweetId);
        ShowTweetInfoFragment f = new ShowTweetInfoFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    protected View getDefaultLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.show_tweet_info_fragment, container, false);
    }

    @Override
    protected void getViewReferences(View parent) {
        llContainer = (LinearLayout) parent.findViewById(R.id.ll_container);
    }

    @Override
    protected void extractArguments(Bundle arguments) {
        tweetId = arguments.getLong(KEY_TWEET_ID, -1);
    }

    @Override
    protected void initViews() {
        if (tweetId == -1) {
            Toast.makeText(getActivity(), "Tweet not available", Toast.LENGTH_SHORT).show();
            return;
        }
        MyTweet tweet = DBHelper.getInstance().getTweet(tweetId);
        llContainer.addView(new TweetView(getActivity(), tweet));
    }
}
