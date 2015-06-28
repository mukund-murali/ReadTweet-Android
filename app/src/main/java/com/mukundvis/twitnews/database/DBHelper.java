package com.mukundvis.twitnews.database;

/**
 * Created by mukundvis on 23/06/15.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.mukundvis.twitnews.MyApplication;
import com.mukundvis.twitnews.models.MyTweet;
import com.mukundvis.twitnews.models.TweetInfo;
import com.mukundvis.twitnews.utils.DBUtils;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final int VERSION_NO = 2;

    public static final String DATABASE_NAME = "TwitNews.tweets";
    public static final String TABLE_NAME = "tweets";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TWEET_ID = "tweet_id";
    public static final String COLUMN_TWEET_JSON = "tweet_json";
    public static final String COLUMN_SKIPPED = "skipped";
    public static final String COLUMN_IGNORED = "ignored";
    public static final String COLUMN_INTERESTED = "interested";
    private static final String COLUMN_IS_SYNCED = "is_synced";
    private static final String COLUMN_IS_RELEVANT = "is_relevant";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " PRIMARY KEY, " +
            COLUMN_TWEET_ID + " INTEGER UNIQUE, " +
            COLUMN_TWEET_JSON + " TEXT, " +
            COLUMN_SKIPPED + " INTEGER DEFAULT 0, " +
            COLUMN_IGNORED + " INTEGER DEFAULT 0, " +
            COLUMN_IS_SYNCED + " BOOLEAN DEFAULT 0," +
            COLUMN_IS_RELEVANT + " BOOLEAN DEFAULT 1," +
            COLUMN_INTERESTED + " INTEGER DEFAULT 0);";

    SQLiteDatabase db, readableDb;

    public static DBHelper getInstance() {
        return new DBHelper(MyApplication.getInstance());
    }

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NO);
        db = this.getWritableDatabase();
        readableDb = this.getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long createTweet(long tweetId, String tweetJSON) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TWEET_ID, tweetId);
        values.put(COLUMN_TWEET_JSON, tweetJSON);
        // Ignoring insert if value is already there.
        return db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public Cursor getTweets() {
        String selection = COLUMN_IGNORED + " = 0";
        return readableDb.query(TABLE_NAME, null, selection, null, null, null, COLUMN_TWEET_ID + " DESC");
    }

    public int markIgnored(long tweetId, Cursor cursor) {
        int ignored = DBUtils.getIgnored(cursor);
        String selection = COLUMN_TWEET_ID + "=?";
        String[] selectionArgs = new String[]{tweetId + ""};
        ContentValues values = new ContentValues();
        values.put(COLUMN_IGNORED, ignored + 1);
        values.put(COLUMN_IS_SYNCED, Boolean.FALSE);
        return db.update(TABLE_NAME, values, selection, selectionArgs);
    }

    public int markInterested(long tweetId, Cursor cursor) {
        int interested = DBUtils.getInterested(cursor);
        String selection = COLUMN_TWEET_ID + "=?";
        String[] selectionArgs = new String[]{tweetId + ""};
        ContentValues values = new ContentValues();
        values.put(COLUMN_INTERESTED, interested + 1);
        values.put(COLUMN_IS_SYNCED, Boolean.FALSE);
        return db.update(TABLE_NAME, values, selection, selectionArgs);
    }

    public int markSkipped(long tweetId, Cursor cursor) {
        int skipped = DBUtils.getSkipped(cursor);
        String selection = COLUMN_TWEET_ID + "=?";
        String[] selectionArgs = new String[]{tweetId + ""};
        ContentValues values = new ContentValues();
        values.put(COLUMN_SKIPPED, skipped + 1);
        values.put(COLUMN_IS_SYNCED, Boolean.FALSE);
        return db.update(TABLE_NAME, values, selection, selectionArgs);
    }

    public List<TweetInfo> getTweetsToSync() {
        String andCondition1 = "(" + COLUMN_SKIPPED + " > 0 OR " + COLUMN_IGNORED + " > 0 OR " + COLUMN_INTERESTED + " > 0)";
        String andCondition2 = COLUMN_IS_SYNCED + " = 0";
        String selection = andCondition1 + " AND " + andCondition2;
        Cursor cursor = readableDb.query(TABLE_NAME, null, selection, null, null, null, null);
        List<TweetInfo> tweets = new ArrayList<>();
        while (cursor.moveToNext()) {
            TweetInfo info = new TweetInfo(DBUtils.getTweetId(cursor),
                    DBUtils.getSkipped(cursor), DBUtils.getInterested(cursor),
                    DBUtils.getIgnored(cursor));
            tweets.add(info);
        }
        return tweets;
    }

    public long getMaxTweetId() {
        Cursor c = readableDb.query(TABLE_NAME, new String[]{"MAX(" + COLUMN_TWEET_ID + ")"}, null, null, null, null, null);
        try {
            if (DBUtils.isCursorUsable(c)) {
                c.moveToFirst();
                return c.getLong(0);
            }
        } finally {
            DBUtils.closeCursor(c);
        }
        return Integer.MIN_VALUE;
    }

    public int markTweetsUpdated(List<TweetInfo> tweets) {
        ArrayList<String> tweetIds = new ArrayList<>();
        for (TweetInfo tweetInfo: tweets) {
            tweetIds.add(tweetInfo.tweetId + "");
        }
        String idsToUpdate = TextUtils.join(", ", tweetIds);

        ContentValues values = new ContentValues();
        // Since ignored is used to list tweets, just mark the tweet as ignored to not show
        // Could come useful to show all ignored tweets separately
        // values.put(COLUMN_IGNORED, 0);
        values.put(COLUMN_SKIPPED, 0);
        values.put(COLUMN_INTERESTED, 0);
        values.put(COLUMN_IS_SYNCED, 1);
        int updateResp = 1;
        updateResp = db.update(TABLE_NAME, values, COLUMN_TWEET_ID + " in (" + idsToUpdate + ")", null);
        return updateResp;
    }

    public String getTweetJSON(long tweetId) {
        String selection = COLUMN_TWEET_ID + " =?";
        String[] args = new String[]{tweetId + ""};
        Cursor c = readableDb.query(TABLE_NAME, new String[] {COLUMN_TWEET_JSON}, selection, args, null, null, null);
        try {
            if (DBUtils.isCursorUsable(c)) {
                c.moveToFirst();
                return c.getString(0);
            }
        } finally {
            DBUtils.closeCursor(c);
        }
        return null;
    }

    public MyTweet getTweet(long tweetId) {
        String tweetJSON = getTweetJSON(tweetId);
        Gson gson = new Gson();
        return gson.fromJson(tweetJSON, MyTweet.class);
    }

    public Tweet getOriginalTweet(long tweetId) {
        String tweetJSON = getTweetJSON(tweetId);
        Gson gson = new Gson();
        return gson.fromJson(tweetJSON, Tweet.class);
    }
}