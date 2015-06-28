package com.mukundvis.twitnews;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.mukundvis.twitnews.prefs.SharedPrefs;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import io.fabric.sdk.android.Fabric;

/**
 * Created by mukundvis on 21/06/15.
 */
public class MyApplication extends Application {

    private static final String PREFS_NAME = "prefs";
    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "newmaQeDfrHRjXrAM1I8ANjNB";
    private static final String TWITTER_SECRET = "MU5jZwjloaW1Rr3DnHqDUukmpxdajZ9fcwbRvu5lQ0yyK2ID9r";

    private static MyApplication sInstance;

    private SharedPrefs prefs;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig), new Crashlytics());
    }

    public static MyApplication getInstance() {
        return sInstance;
    }

    public SharedPrefs getPrefs() {
        if (prefs == null) {
            prefs = new SharedPrefs(getSharedPreferences(PREFS_NAME, 0));
        }
        return prefs;
    }
}
