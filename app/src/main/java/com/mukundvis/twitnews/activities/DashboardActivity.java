package com.mukundvis.twitnews.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.malinskiy.superrecyclerview.swipe.SparseItemRemoveAnimator;
import com.malinskiy.superrecyclerview.swipe.SwipeDismissRecyclerViewTouchListener;
import com.mukundvis.twitnews.R;
import com.mukundvis.twitnews.adapters.TweetCursorAdapter;
import com.mukundvis.twitnews.constants.ApiConstants;
import com.mukundvis.twitnews.database.DBHelper;
import com.mukundvis.twitnews.models.MyTweet;
import com.mukundvis.twitnews.models.TweetResponse;
import com.mukundvis.twitnews.providers.TweetProvider;
import com.mukundvis.twitnews.services.SyncTweetsService;
import com.mukundvis.twitnews.utils.DBUtils;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by mukundvis on 21/06/15.
 */
public class DashboardActivity extends BaseLoggedInActivity implements SwipeDismissRecyclerViewTouchListener.DismissCallbacks,
        SwipeRefreshLayout.OnRefreshListener, OnMoreListener, LoaderManager.LoaderCallbacks<Cursor>,TweetCursorAdapter.OnButtonClickListener {

    private static final int TWEETS_LOADER = 1012;

    Cursor activeCursor = null;

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection = DBHelper.COLUMN_IGNORED + " = 0";
        return new CursorLoader(this, TweetProvider.CONTENT_URI, null,
                selection, null, DBHelper.COLUMN_TWEET_ID + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        activeCursor = data;
        mAdapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        /*
         * Clears out the adapter's reference to the Cursor.
         * This prevents memory leaks.
         */
        mAdapter.changeCursor(null);
    }

    @Override
    public void onInterested(int position) {
        markTweetInterested(position);
    }

    @Override
    public void onIgnored(int position) {
        markTweetIgnored(position);
    }

    public interface GetTweetsService {
        @GET(ApiConstants.URL_GET_TWEETS)
        void getTweets(
                @Query("device_id") String deviceId,
                @Query("user_id") long userId,
                @Query("since_tweet_id") String sinceTweetId,
                retrofit.Callback<TweetResponse> callback
        );
    }

    SuperRecyclerView mRecyclerView;
    SparseItemRemoveAnimator mSparseAnimator;

    RestAdapter restAdapter;
    GetTweetsService service;

    DBHelper helper;

    TweetCursorAdapter mAdapter;

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
                .setEndpoint(ApiConstants.END_POINT_V2)
                .build();
        service = restAdapter.create(GetTweetsService.class);

        helper = new DBHelper(this);
        initializeRecyclerView();
        fetchNewTweets();
        getSupportLoaderManager().initLoader(TWEETS_LOADER, null, this);
    }

    private void fetchNewTweets() {
        mRecyclerView.getSwipeToRefresh().setRefreshing(true);
        String sinceTweetId = helper.getMaxTweetId() + "";
        service.getTweets(getPrefs().getDeviceId(), getPrefs().getUserId(), sinceTweetId, new Callback<TweetResponse>() {
            @Override
            public void success(TweetResponse obj, Response response) {
                // add the tweets to database
                Gson gson = new Gson();
                if (obj.tweets == null) {
                    return;
                }
                for (MyTweet tweet : obj.tweets) {
                    String tweetJson = gson.toJson(tweet);
                    long tweetId = tweet.id;
                    long insertResp = helper.createTweet(tweetId, tweetJson);
                    getContentResolver().notifyChange(TweetProvider.CONTENT_URI, null);
                    int i = 10;
                }
                mRecyclerView.getSwipeToRefresh().setRefreshing(false);
            }

            @Override
            public void failure(RetrofitError error) {
                mRecyclerView.getSwipeToRefresh().setRefreshing(false);
                if (error.getResponse() == null) {
                    return;
                }
                switch (error.getResponse().getStatus()) {
                    case 403:
                        // unauthorized
                        Toast.makeText(DashboardActivity.this, "Un authorized", Toast.LENGTH_SHORT).show();
                        mRecyclerView.getProgressView().setVisibility(View.GONE);
                        break;
                }
            }
        });
    }

    private void initializeRecyclerView() {
        // TODO: height of the row gets affected when a view is removed. Need to take care of this.
        // TODO: Will setting height common for everything help?
        RecyclerView.LayoutManager mLayoutManager = new android.support.v7.widget.LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // swipe to dismiss
        mRecyclerView.setupSwipeToDismiss(this);
        mSparseAnimator = new SparseItemRemoveAnimator();
        // mRecyclerView.getRecyclerView().setItemAnimator(mSparseAnimator);

        // onMore
        mRecyclerView.setRefreshListener(this);
        mRecyclerView.setRefreshingColorResources(android.R.color.holo_orange_light,
                android.R.color.holo_blue_light, android.R.color.holo_green_light,
                android.R.color.holo_red_light);
        mRecyclerView.setupMoreListener(this, 1);

        mAdapter = new TweetCursorAdapter(this, null);
        mAdapter.setOnButtonClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
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
        }
    }

    private void markTweetIgnored(int position) {
        activeCursor.moveToPosition(position);
        long tweetId = DBUtils.getTweetId(activeCursor);
        MyTweet tweet = mAdapter.getTweet(tweetId, activeCursor);
        int markIgnored = helper.markIgnored(tweet.id, activeCursor);
        getContentResolver().notifyChange(TweetProvider.CONTENT_URI, null);
    }

    private void markTweetInterested(int position) {
        activeCursor.moveToPosition(position);
        long tweetId = DBUtils.getTweetId(activeCursor);
        MyTweet tweet = mAdapter.getTweet(tweetId, activeCursor);
        int markInterested = helper.markInterested(tweet.id, activeCursor);
        getContentResolver().notifyChange(TweetProvider.CONTENT_URI, null);
    }

    private void markTweetSkipped(int position) {
        activeCursor.moveToPosition(position);
        long tweetId = DBUtils.getTweetId(activeCursor);
        MyTweet tweet = mAdapter.getTweet(tweetId, activeCursor);
        int markIgnored = helper.markSkipped(tweet.id, activeCursor);
        getContentResolver().notifyChange(TweetProvider.CONTENT_URI, null);
    }

    @Override
    public void onRefresh() {
        fetchNewTweets();
    }

    @Override
    public void onMoreAsked(int numberOfItems, int numberBeforeMore, int currentItemPos) {
        int i = 10;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_compose:
                composeTweet();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void composeTweet() {
        TweetComposer.Builder builder = new TweetComposer.Builder(this);
        builder.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SyncTweetsService.startSync();
    }
}
