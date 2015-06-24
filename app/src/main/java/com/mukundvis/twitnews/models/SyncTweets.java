package com.mukundvis.twitnews.models;

import java.util.List;

/**
 * Created by mukundvis on 24/06/15.
 */
public class SyncTweets {

    public List<TweetInfo> tweets;

    public SyncTweets(List<TweetInfo> tweets) {
        this.tweets = tweets;
    }

}
