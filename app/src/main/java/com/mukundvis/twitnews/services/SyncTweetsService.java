package com.mukundvis.twitnews.services;

import android.app.IntentService;
import android.content.Intent;

import com.mukundvis.twitnews.MyApplication;
import com.mukundvis.twitnews.constants.ApiConstants;
import com.mukundvis.twitnews.database.DBHelper;
import com.mukundvis.twitnews.models.SyncResponse;
import com.mukundvis.twitnews.models.TweetInfo;
import com.mukundvis.twitnews.prefs.SharedPrefs;
import com.mukundvis.twitnews.providers.TweetProvider;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by mukundvis on 24/06/15.
 */
public class SyncTweetsService extends IntentService {

    public SyncTweetsService() {
        super(SyncTweetsService.class.getSimpleName());

    }
    public static void startSync() {
        MyApplication instance = MyApplication.getInstance();
        Intent serviceIntent = new Intent(instance, SyncTweetsService.class);
        instance.startService(serviceIntent);
    }

    public interface SyncTweetsDefinition {
        @POST(ApiConstants.URL_SYNC_TWEETS)
        void syncTweets(
                @Query("device_id") String deviceId,
                @Query("user_id") long userId,
                @Body List<TweetInfo> tweets,
                retrofit.Callback<SyncResponse> callback
        );
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ApiConstants.END_POINT_V2)
                .build();
        SyncTweetsDefinition service = restAdapter.create(SyncTweetsDefinition.class);
        SharedPrefs prefs = MyApplication.getInstance().getPrefs();
        String deviceId = prefs.getDeviceId();
        long userId = prefs.getUserId();
        final DBHelper helper = DBHelper.getInstance();
        final List<TweetInfo> tweets = helper.getTweetsToSync();
        service.syncTweets(deviceId, userId, tweets, new Callback<SyncResponse>() {
            @Override
            public void success(SyncResponse tweetResponse, Response response) {
                String m = tweetResponse.message;
                helper.markTweetsUpdated(tweets);
                getContentResolver().notifyChange(TweetProvider.CONTENT_URI, null);
            }

            @Override
            public void failure(RetrofitError error) {
                String resp = error.getUrl();
            }
        });
    }
}
