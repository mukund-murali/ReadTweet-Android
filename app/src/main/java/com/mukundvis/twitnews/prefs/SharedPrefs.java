package com.mukundvis.twitnews.prefs;

import android.content.SharedPreferences;
import android.os.Build;

/**
 * Created by mukundvis on 21/06/15.
 */
public class SharedPrefs {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public SharedPrefs(SharedPreferences prefs) {
        this.prefs = prefs;
        editor = prefs.edit();
    }

    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_DEVICE_ID = "device_id";

    public SharedPrefs setLoginInfo(long userId, String deviceId) {
        setLongPref(KEY_USER_ID, userId).setStringPref(KEY_DEVICE_ID, deviceId);
        return this;
    }

    public boolean isLoggedIn() {
        long userId = prefs.getLong(KEY_USER_ID, -1);
        String deviceId = prefs.getString(KEY_DEVICE_ID, null);
        return userId != -1 && deviceId != null;
    }

    private SharedPrefs setStringPref(String key, String value) {
        editor.putString(key, value);
        return this;
    }

    private SharedPrefs setIntPref(String key, int value) {
        editor.putInt(key, value);
        return this;
    }

    private SharedPrefs setLongPref(String key, long value) {
        editor.putLong(key, value);
        return this;
    }

    private SharedPrefs setFloatPref(String key, float value) {
        editor.putFloat(key, value);
        return this;
    }

    public void commit() {
        editor.apply();
    }
}
