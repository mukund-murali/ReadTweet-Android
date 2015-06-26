package com.mukundvis.twitnews.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mukundvis.twitnews.R;
import com.mukundvis.twitnews.database.DBHelper;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.LoadCallback;
import com.twitter.sdk.android.tweetui.TweetUtils;
import com.twitter.sdk.android.tweetui.TweetView;

/**
 * Created by mukundvis on 27/06/15.
 */
public class ShowTweetActivity  extends BaseActivity {

    public static final String KEY_TWEET_ID = "tweet_id";

    private static final String DEBUG_TAG = ShowTweetActivity.class.getSimpleName();

    long tweetId;

    private LinearLayout llContainer;

    @Override
    protected int getDefaultLayout() {
        return R.layout.show_tweet;
    }

    @Override
    protected void getViewReferences() {
        llContainer = getLinearLayout(R.id.ll_container);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(DEBUG_TAG, tweetId + ";");
        DBHelper helper = new DBHelper(this);
        Tweet tweet = helper.getTweet(tweetId);
        if (tweet == null) {
            Toast.makeText(this, "Some error occured. Try later.", Toast.LENGTH_SHORT).show();
            this.finish();
            return;
        }
        llContainer.addView(new TweetView(
                ShowTweetActivity.this, tweet));
    }

    @Override
    protected void extractArguments(Bundle arguments) {
        tweetId = arguments.getLong(KEY_TWEET_ID);
    }
}
