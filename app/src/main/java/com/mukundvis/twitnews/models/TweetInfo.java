package com.mukundvis.twitnews.models;

/**
 * Created by mukundvis on 24/06/15.
 */
public class TweetInfo {

    public long tweetId;
    int skipped, interested, ignored;

    public TweetInfo(long tweetId, int skipped, int interested, int ignored) {
        this.tweetId = tweetId;
        this.skipped = skipped;
        this.interested = interested;
        this.ignored = ignored;
    }
}
