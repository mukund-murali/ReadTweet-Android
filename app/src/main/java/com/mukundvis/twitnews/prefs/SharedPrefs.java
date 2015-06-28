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
    private static final String SHOULD_SHOW_IMAGES = "show_images";

    public SharedPrefs setLoginInfo(long userId, String deviceId) {
        setLongPref(KEY_USER_ID, userId).setStringPref(KEY_DEVICE_ID, deviceId);
        return this;
    }

    public String getDeviceId() {
        return prefs.getString(KEY_DEVICE_ID, null);
    }

    public long getUserId() {
        return prefs.getLong(KEY_USER_ID, -1);
    }

    public boolean isLoggedIn() {
        long userId = getUserId();
        String deviceId = getDeviceId();
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

    public boolean shouldShowImages() {
        return prefs.getBoolean(SHOULD_SHOW_IMAGES, true);
    }

    public SharedPrefs setShouldShowImages(boolean shouldShowImages) {
        editor.putBoolean(SHOULD_SHOW_IMAGES, shouldShowImages);
        return this;
    }
}
