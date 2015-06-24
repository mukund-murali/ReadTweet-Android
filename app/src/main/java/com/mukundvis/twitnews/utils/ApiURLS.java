package com.mukundvis.twitnews.utils;

import com.mukundvis.twitnews.MyApplication;
import com.mukundvis.twitnews.prefs.SharedPrefs;

/**
 * Created by mukundvis on 24/06/15.
 */
public class ApiURLS {

    public static String getURL(String baseURL) {
        SharedPrefs prefs = MyApplication.getInstance().getPrefs();
        String deviceId = prefs.getDeviceId();
        long userId = prefs.getUserId();
        return baseURL + "?device_id=" + deviceId + "&user_id=" + userId;
    }
}
