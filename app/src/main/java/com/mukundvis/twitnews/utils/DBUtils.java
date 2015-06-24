package com.mukundvis.twitnews.utils;

import android.database.Cursor;

import com.google.gson.Gson;
import com.mukundvis.twitnews.database.DBHelper;
import com.mukundvis.twitnews.models.MyTweet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mukundvis on 24/06/15.
 */
public class DBUtils {

    public static List<MyTweet> getTweetsFromCursor(Cursor cursor) {
        long millisInStart = System.currentTimeMillis();
        List<MyTweet> list = new ArrayList<>();
        Gson gson = new Gson();
        while(cursor.moveToNext()) {
            String tweetJSON = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TWEET_JSON));
            list.add(gson.fromJson(tweetJSON, MyTweet.class));
        }
        long millisDuringCompletion = System.currentTimeMillis();
        long secDiff = (millisDuringCompletion - millisInStart) / 1000;
        return list;
    }

    public static long getTweetId(Cursor cursor) {
        return cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_TWEET_ID));
    }

    public static String getTweetJSON(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TWEET_JSON));
    }

    public static int getInterested(Cursor cursor) {
        return cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_INTERESTED));
    }

    public static int getIgnored(Cursor cursor) {
        return cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_IGNORED));
    }

    public static int getSkipped(Cursor cursor) {
        return cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_SKIPPED));
    }

    public static void closeCursor(Cursor c) {
        if (c == null || c.isClosed()) {
            return;
        }
        c.close();
    }

    public static boolean isCursorUsable(Cursor c) {
        return c != null && c.getCount() > 0;
    }
}
