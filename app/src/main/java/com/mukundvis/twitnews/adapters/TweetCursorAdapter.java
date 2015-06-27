package com.mukundvis.twitnews.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.mukundvis.twitnews.utils.TwitterUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by mukundvis on 23/06/15.
 */
public class TweetCursorAdapter extends CursorRecyclerViewAdapter {

    private static final String TWITTER_DATE_FORMAT_STRING = "EEE MMM dd HH:mm:ss Z yyyy";
    private static final DateFormat TWITTER_DATE_FORMAT = new SimpleDateFormat(TWITTER_DATE_FORMAT_STRING);

    private static final String DEBUG_TAG = TweetCursorAdapter.class.getSimpleName();

    Context context;

    int relevantColor, nonRelevantColor;

    public TweetCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        this.context = context;
        relevantColor = context.getResources().getColor(R.color.relevant);
        nonRelevantColor = context.getResources().getColor(R.color.non_relevant);
    }

    public interface OnButtonClickListener {
        void onInterested(int position);
        void onIgnored(int position);
        void onTweetClick(int position);
    }

    OnButtonClickListener mListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvScreenName, tvTweet, tvUsername, tvTimeDiff;
        Button btnInterested, btnIgnored;

        View baseView, viewHasArticle;

        public ViewHolder(View v) {
            super(v);
            tvScreenName = (TextView) v.findViewById(R.id.tv_screen_name);
            tvUsername = (TextView) v.findViewById(R.id.tv_username);
            tvTweet = (TextView) v.findViewById(R.id.tv_tweet);
            tvTweet.setOnClickListener(this);
            tvTimeDiff = (TextView) v.findViewById(R.id.tv_time_diff);
            btnIgnored = (Button) v.findViewById(R.id.btn_ignore);
            btnIgnored.setOnClickListener(this);
            btnInterested = (Button) v.findViewById(R.id.btn_interested);
            btnInterested.setOnClickListener(this);
            viewHasArticle = v.findViewById(R.id.view_has_article);
            baseView = v;
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
                    break;
                case R.id.tv_tweet:
                    mListener.onTweetClick(getAdapterPosition());
                    break;
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
        holder.tvUsername.setText(tweet.user.name);
        holder.tvScreenName.setText("@" + tweet.user.screenName);
        String displayText = tweet.getFormattedText();
        holder.tvTweet.setText(displayText);
        try {
            Date date = TWITTER_DATE_FORMAT.parse(tweet.createdAt);
            holder.tvTimeDiff.setText(getTimeDiffFromNow(date));
        } catch (ParseException e) {
            e.printStackTrace();
            holder.tvTimeDiff.setText("");
        }
        if (tweet.isRelevant()) {
            // holder.baseView.setBackgroundColor(relevantColor);
        } else {
            holder.baseView.setBackgroundColor(nonRelevantColor);
        }
        if (tweet.hasArticle()) {
            holder.viewHasArticle.setVisibility(View.VISIBLE);
        } else {
            holder.viewHasArticle.setVisibility(View.GONE);
        }
    }

    private String getTimeDiffFromNow(Date date) {
        long diffInMillis = System.currentTimeMillis() - date.getTime();
        int secondsDiff = (int) (diffInMillis / 1000);
        if (secondsDiff <= 60) {
            return secondsDiff + "s";
        }
        int minutes = secondsDiff / 60;
        if (minutes <= 60) {
            return  minutes + "m";
        }
        int hours = minutes / 60;
        if (hours <= 24) {
            return hours + "h";
        }
        int days = hours / 24;
        return days + "d";
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.tweet_row, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
}
