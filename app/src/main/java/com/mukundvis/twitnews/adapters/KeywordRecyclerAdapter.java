package com.mukundvis.twitnews.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mukundvis.twitnews.R;
import com.mukundvis.twitnews.models.KeywordWithRelevance;

import java.util.List;

/**
 * Created by mukundvis on 21/06/15.
 */
public class KeywordRecyclerAdapter extends RecyclerView.Adapter<KeywordRecyclerAdapter.ViewHolder> {

    public List<KeywordWithRelevance> getKeywordWithRelevances() {
        return keywordWithRelevances;
    }

    public void setKeywordWithRelevances(List<KeywordWithRelevance> keywordWithRelevances) {
        this.keywordWithRelevances = keywordWithRelevances;
    }

    private List<KeywordWithRelevance> keywordWithRelevances;

    public KeywordRecyclerAdapter() {
        keywordWithRelevances = null;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvKeyword, tvRelevance, tvInterested, tvIgnored;

        public ViewHolder(View v) {
            super(v);
            tvKeyword = (TextView) v.findViewById(R.id.tv_keyword);
            tvRelevance = (TextView) v.findViewById(R.id.tv_relevance);
            tvInterested = (TextView) v.findViewById(R.id.tv_interested);
            tvIgnored = (TextView) v.findViewById(R.id.tv_ignored);
        }
    }

    public KeywordRecyclerAdapter(List<KeywordWithRelevance> keywordWithRelevances) {
        this.keywordWithRelevances = keywordWithRelevances;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.keyword_row, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        KeywordWithRelevance keywordWithRelevance = keywordWithRelevances.get(position);
        KeywordWithRelevance.Keyword doc = keywordWithRelevance.getDoc();
        holder.tvKeyword.setText(doc.getKeyword());
        holder.tvRelevance.setText(keywordWithRelevance.getRelevance() + "");
        holder.tvInterested.setText(doc.getInterested() + "");
        holder.tvIgnored.setText(doc.getIgnored() + "");
    }

    @Override
    public int getItemCount() {
        if (keywordWithRelevances == null)
            return 0;
        return keywordWithRelevances.size();
    }

}
