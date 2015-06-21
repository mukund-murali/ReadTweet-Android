package com.mukundvis.twitnews.activities;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.mukundvis.twitnews.R;
import com.mukundvis.twitnews.adapters.TweetRecyclerAdapter;
import com.mukundvis.twitnews.constants.ApiConstants;
import com.mukundvis.twitnews.models.TweetResponse;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by mukundvis on 21/06/15.
 */
public class DashboardActivity extends BaseLoggedInActivity {

    RecyclerView mRecyclerView;

    TweetRecyclerAdapter mAdapter;

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
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_tweets);
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
        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new android.support.v7.widget.LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        GetTweetsService service = restAdapter.create(GetTweetsService.class);
        service.getTweets(getPrefs().getDeviceId(), getPrefs().getUserId(), "", new Callback<TweetResponse>() {
            @Override
            public void success(TweetResponse obj, Response response) {
                mAdapter = new TweetRecyclerAdapter(obj.tweets);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void failure(RetrofitError error) {
                int i = 10;
                Toast.makeText(DashboardActivity.this, "Error fetching tweets", Toast.LENGTH_LONG).show();
            }
        });
    }
}
