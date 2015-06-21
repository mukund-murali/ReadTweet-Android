package com.mukundvis.twitnews.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mukundvis.twitnews.R;
import com.mukundvis.twitnews.models.MyTweet;

import java.util.List;

/**
 * Created by mukundvis on 21/06/15.
 */
public class TweetRecyclerAdapter extends RecyclerView.Adapter<TweetRecyclerAdapter.ViewHolder> {

    private List<MyTweet> tweets;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvScreenName, tvTweet, tvUsername;

        public ViewHolder(View v) {
            super(v);
            tvScreenName = (TextView) v.findViewById(R.id.tv_screen_name);
            tvUsername = (TextView) v.findViewById(R.id.tv_username);
            tvTweet = (TextView) v.findViewById(R.id.tv_tweet);
        }
    }

    public TweetRecyclerAdapter(List<MyTweet> tweets) {
        this.tweets = tweets;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.tweet_row, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MyTweet tweet = tweets.get(position);
        holder.tvScreenName.setText(tweet.user.name);
        holder.tvUsername.setText("@" + tweet.user.name);
        holder.tvTweet.setText(tweet.text);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }
}
