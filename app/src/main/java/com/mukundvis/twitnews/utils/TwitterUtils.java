package com.mukundvis.twitnews.utils;

import android.util.Log;

import com.mukundvis.twitnews.models.MyTweet;
import com.twitter.sdk.android.core.models.HashtagEntity;
import com.twitter.sdk.android.core.models.MediaEntity;
import com.twitter.sdk.android.core.models.MentionEntity;
import com.twitter.sdk.android.core.models.TweetEntities;
import com.twitter.sdk.android.core.models.UrlEntity;

import java.util.List;

/**
 * Created by mukundvis on 27/06/15.
 */
public class TwitterUtils {


    private static final String DEBUG_TAG = TwitterUtils.class.getSimpleName();

    public static String getFormattedTweet(MyTweet tweet) {
        TweetEntities entities = tweet.entities;
        List<MediaEntity> media = entities.media;
        List<HashtagEntity> hashtags = entities.hashtags;
        List<UrlEntity> urls = entities.urls;
        List<MentionEntity> mentions = entities.userMentions;

        String tweetText = tweet.text;

        if (urls != null && urls.size() > 0) {
            UrlEntity url = urls.get(0);
            tweetText = tweetText.replace(url.url, url.displayUrl);
        }

        if (media != null && media.size() > 0) {
            MediaEntity img = media.get(0);
            tweetText = tweetText.replace(img.url, img.displayUrl);
        }
        return tweetText;
    }
}
