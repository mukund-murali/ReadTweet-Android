package com.mukundvis.twitnews.database;

/**
 * Created by mukundvis on 23/06/15.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "TwitNews.tweets";
    public static final String TABLE_NAME = "tweets";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TWEET_ID = "tweet_id";
    public static final String COLUMN_TWEET_JSON = "tweet_json";
    public static final String COLUMN_SKIPPED = "skipped";
    public static final String COLUMN_IGNORED = "ignored";
    public static final String COLUMN_INTERESTED = "interested";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " PRIMARY KEY, " +
            COLUMN_TWEET_ID + " INTEGER UNIQUE, " +
            COLUMN_TWEET_JSON + " TEXT, " +
            COLUMN_SKIPPED + " INTEGER DEFAULT 0, " +
            COLUMN_IGNORED + " INTEGER DEFAULT 0, " +
            COLUMN_INTERESTED + " INTEGER DEFAULT 0);";

    SQLiteDatabase db, readableDb;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        db = this.getWritableDatabase();
        readableDb = this.getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS contacts");
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

    public int markIgnored(long tweetId) {
        String selection = COLUMN_TWEET_ID + "=?";
        String[] selectionArgs = new String[]{tweetId + ""};
        ContentValues values = new ContentValues();
        values.put(COLUMN_IGNORED, 1);
        return db.update(TABLE_NAME, values, selection, selectionArgs);
    }

}