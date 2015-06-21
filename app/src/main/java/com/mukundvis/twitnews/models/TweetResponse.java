package com.mukundvis.twitnews.models;


import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

/**
 * Created by mukundvis on 21/06/15.
 */
public class TweetResponse {

    public List<Tweet> relevantTweets;
    public List<MyTweet> tweets;

}
