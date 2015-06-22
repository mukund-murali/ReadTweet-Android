package com.mukundvis.twitnews.activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.malinskiy.superrecyclerview.swipe.SparseItemRemoveAnimator;
import com.malinskiy.superrecyclerview.swipe.SwipeDismissRecyclerViewTouchListener;
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
public class DashboardActivity extends BaseLoggedInActivity implements SwipeDismissRecyclerViewTouchListener.DismissCallbacks, SwipeRefreshLayout.OnRefreshListener, OnMoreListener {

    SuperRecyclerView mRecyclerView;
    SparseItemRemoveAnimator mSparseAnimator;

    RestAdapter restAdapter;
    GetTweetsService service;

    TweetRecyclerAdapter mAdapter;

    @Override
    protected int getDefaultLayout() {
        return R.layout.dashboard_activity;
    }

    @Override
    protected void getViewReferences() {
        mRecyclerView = (SuperRecyclerView) findViewById(R.id.rv_tweets);
    }

    @Override
    protected void extractArguments(Bundle arguments) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restAdapter = new RestAdapter.Builder()
                .setEndpoint(ApiConstants.END_POINT)
                .build();
        service = restAdapter.create(GetTweetsService.class);
        initializeRecyclerView();
        fetchNewTweets();
    }

    private void fetchNewTweets() {
        mRecyclerView.getSwipeToRefresh().setRefreshing(true);
        String sinceTweetId = "";
        service.getTweets(getPrefs().getDeviceId(), getPrefs().getUserId(), sinceTweetId, new Callback<TweetResponse>() {
            @Override
            public void success(TweetResponse obj, Response response) {
                if (mAdapter == null) {
                    mAdapter = new TweetRecyclerAdapter(obj.tweets);
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    mAdapter.setTweets(obj.tweets);
                    mAdapter.notifyDataSetChanged();
                }
                mRecyclerView.getSwipeToRefresh().setRefreshing(false);
            }

            @Override
            public void failure(RetrofitError error) {
                int i = 10;
                Toast.makeText(DashboardActivity.this, "Error fetching tweets", Toast.LENGTH_LONG).show();
                mRecyclerView.getSwipeToRefresh().setRefreshing(false);
            }
        });
    }

    private void initializeRecyclerView() {
        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new android.support.v7.widget.LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // swipe to dismiss
        mRecyclerView.setupSwipeToDismiss(this);
        mSparseAnimator = new SparseItemRemoveAnimator();
        mRecyclerView.getRecyclerView().setItemAnimator(mSparseAnimator);

        // onMore
        mRecyclerView.setRefreshListener(this);
        mRecyclerView.setRefreshingColorResources(android.R.color.holo_orange_light,
                android.R.color.holo_blue_light, android.R.color.holo_green_light,
                android.R.color.holo_red_light);
        mRecyclerView.setupMoreListener(this, 1);
    }

    @Override
    public boolean canDismiss(int i) {
        return true;
    }

    @Override
    public void onDismiss(RecyclerView recyclerView, int[] reverseSortedPositions) {
        for (int position : reverseSortedPositions) {
            mSparseAnimator.setSkipNext(true);
            markTweetIgnored(position);
            mAdapter.remove(position);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void markTweetIgnored(int position) {
        // TODO
    }

    @Override
    public void onRefresh() {
        fetchNewTweets();
    }

    @Override
    public void onMoreAsked(int numberOfItems, int numberBeforeMore, int currentItemPos) {
        int i =10;
    }

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

}
