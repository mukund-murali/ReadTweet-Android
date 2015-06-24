package com.mukundvis.twitnews.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mukundvis.twitnews.R;
import com.mukundvis.twitnews.database.DBHelper;
import com.mukundvis.twitnews.models.MyTweet;
import com.mukundvis.twitnews.utils.DBUtils;

import java.util.HashMap;

/**
 * Created by mukundvis on 23/06/15.
 */
public class TweetCursorAdapter extends CursorRecyclerViewAdapter {

    public TweetCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }

    public interface OnButtonClickListener {
        void onInterested(int position);
        void onIgnored(int position);
    }

    OnButtonClickListener mListener;


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvScreenName, tvTweet, tvUsername;
        Button btnInterested, btnIgnored;

        public ViewHolder(View v) {
            super(v);
            tvScreenName = (TextView) v.findViewById(R.id.tv_screen_name);
            tvUsername = (TextView) v.findViewById(R.id.tv_username);
            tvTweet = (TextView) v.findViewById(R.id.tv_tweet);
            btnIgnored = (Button) v.findViewById(R.id.btn_ignore);
            btnIgnored.setOnClickListener(this);
            btnInterested = (Button) v.findViewById(R.id.btn_interested);
            btnInterested.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener ==  null) {
                return;
            }
            switch (v.getId()) {
                case R.id.btn_ignore:
                    mListener.onIgnored(getAdapterPosition());
                    break;
                case R.id.btn_interested:
                    mListener.onInterested(getAdapterPosition());
            }
        }
    }

    public void setOnButtonClickListener(final OnButtonClickListener onButtonClickListener) {
        this.mListener = onButtonClickListener;
    }

    public HashMap<Long, MyTweet> tweets = new HashMap<>();
    Gson gson = new Gson();

    public MyTweet getTweet(long tweetId, Cursor cursor) {
        MyTweet tweet;
        if (!tweets.containsKey(tweetId)) {
            tweet = MyTweet.fromCursor(cursor, gson);
            tweets.put(tweetId, tweet);
        } else {
            tweet = tweets.get(tweetId);
        }
        return tweet;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, Cursor cursor) {
        ViewHolder holder = (ViewHolder) viewHolder;
        long tweetId = DBUtils.getTweetId(cursor);
        MyTweet tweet = getTweet(tweetId, cursor);
        holder.tvScreenName.setText(tweet.createdAt);
        holder.tvUsername.setText("@" + tweet.user.name);
        holder.tvTweet.setText(tweet.text);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.tweet_row, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
}
