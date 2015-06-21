package com.mukundvis.twitnews.activities;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.mukundvis.twitnews.R;
import com.mukundvis.twitnews.constants.ApiConstants;
import com.mukundvis.twitnews.models.LoginResponse;
import com.mukundvis.twitnews.models.TweetResponse;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.CompactTweetView;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by mukundvis on 21/06/15.
 */
public class DashboardActivity extends BaseLoggedInActivity {

    private LinearLayout llRelevantTweets;

    // Obtain the tweets and show here.
    public interface GetTweetsService {
        @GET(ApiConstants.URL_GET_TWEETS)
        void getTweets(
                @Query("device_id") String deviceId,
                @Query("user_id") long userId,
                @Query("since_id") String sinceTweetId,
                retrofit.Callback<TweetResponse> callback
        );
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.dashboard_activity;
    }

    @Override
    protected void getViewReferences() {
        llRelevantTweets = (LinearLayout) findViewById(R.id.ll_relevant_tweets);
    }

    @Override
    protected void extractArguments(Bundle arguments) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ApiConstants.END_POINT)
                .build();

        GetTweetsService service = restAdapter.create(GetTweetsService.class);
        service.getTweets(getPrefs().getDeviceId(), getPrefs().getUserId(), "", new Callback<TweetResponse>() {
            @Override
            public void success(TweetResponse obj, Response response) {
                int i = 10;
                for (Tweet tweet : obj.relevantTweets) {
                    llRelevantTweets.addView(
                            new CompactTweetView(
                                    DashboardActivity.this,
                                    tweet));
                }
            }

            @Override
            public void failure(RetrofitError error) {
                int i = 10;
            }
        });
    }
}
