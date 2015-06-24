package com.mukundvis.twitnews.models;

import android.database.Cursor;

import com.google.gson.Gson;
import com.mukundvis.twitnews.database.DBHelper;
import com.mukundvis.twitnews.utils.DBUtils;
import com.twitter.sdk.android.core.models.Coordinates;
import com.twitter.sdk.android.core.models.Place;
import com.twitter.sdk.android.core.models.TweetEntities;
import com.twitter.sdk.android.core.models.User;

import java.util.List;

/**
 * Created by mukundvis on 21/06/15.
 */
public class MyTweet extends com.twitter.sdk.android.core.models.Tweet {

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
}
