package com.mukundvis.twitnews.models;

import android.database.Cursor;

import com.google.gson.Gson;
import com.mukundvis.twitnews.database.DBHelper;
import com.mukundvis.twitnews.utils.DBUtils;
import com.twitter.sdk.android.core.models.Coordinates;
import com.twitter.sdk.android.core.models.HashtagEntity;
import com.twitter.sdk.android.core.models.MediaEntity;
import com.twitter.sdk.android.core.models.MentionEntity;
import com.twitter.sdk.android.core.models.Place;
import com.twitter.sdk.android.core.models.TweetEntities;
import com.twitter.sdk.android.core.models.UrlEntity;
import com.twitter.sdk.android.core.models.User;

import java.util.List;

/**
 * Created by mukundvis on 21/06/15.
 */
public class MyTweet extends com.twitter.sdk.android.core.models.Tweet {

    public boolean isRelevant() {
        return isRelevant;
    }

    boolean isRelevant = false;

    public MyTweet(Coordinates coordinates, String createdAt, Object currentUserRetweet, TweetEntities entities, Integer favoriteCount, boolean favorited, String filterLevel, long id, String idStr, String inReplyToScreenName, long inReplyToStatusId, String inReplyToStatusIdStr, long inReplyToUserId, String inReplyToUserIdStr, String lang, Place place, boolean possiblySensitive, Object scopes, int retweetCount, boolean retweeted, com.twitter.sdk.android.core.models.Tweet retweetedStatus, String source, String text, boolean truncated, User user, boolean withheldCopyright, List<String> withheldInCountries, String withheldScope, boolean isRelevant) {
        super(coordinates, createdAt, currentUserRetweet, entities, favoriteCount, favorited, filterLevel, id, idStr, inReplyToScreenName, inReplyToStatusId, inReplyToStatusIdStr, inReplyToUserId, inReplyToUserIdStr, lang, place, possiblySensitive, scopes, retweetCount, retweeted, retweetedStatus, source, text, truncated, user, withheldCopyright, withheldInCountries, withheldScope);
        this.isRelevant = isRelevant;
    }

    public static MyTweet fromCursor(Cursor cursor, Gson gson) {
        String tweetJSON = DBUtils.getTweetJSON(cursor);
        if (gson == null) {
            gson = new Gson();
        }
        return gson.fromJson(tweetJSON, MyTweet.class);
    }

    public boolean hasArticle() {
        List<UrlEntity> urls = entities.urls;
        return urls != null && urls.size() == 1;
    }

    public boolean hasPicture() {
        List<MediaEntity> media = entities.media;
        return media != null && media.size() == 1;
    }

    public String getFormattedText() {
        TweetEntities entities = this.entities;
        List<MediaEntity> media = entities.media;
        List<HashtagEntity> hashtags = entities.hashtags;
        List<UrlEntity> urls = entities.urls;
        List<MentionEntity> mentions = entities.userMentions;

        String tweetText = this.text;

        if (hasArticle()) {
            UrlEntity url = urls.get(0);
            // replacing the link with empty text.
            // We will show an icon if the tweet has an url
            tweetText = tweetText.replace(url.url, "");
        }
        if (hasPicture()) {
            MediaEntity img = media.get(0);
            tweetText = tweetText.replace(img.url, img.displayUrl);
        }
        return tweetText;
    }
}
