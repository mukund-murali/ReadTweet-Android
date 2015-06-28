package com.mukundvis.twitnews.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mukundvis.twitnews.R;
import com.mukundvis.twitnews.models.KeywordWithRelevance;
import com.mukundvis.twitnews.models.TaxonomyWithRelevance;

import java.util.List;

/**
 * Created by mukundvis on 21/06/15.
 */
public class TaxonomyRecyclerAdapter extends RecyclerView.Adapter<TaxonomyRecyclerAdapter.ViewHolder> {

    public List<TaxonomyWithRelevance> getTaxonomyWithRelevances() {
        return taxonomyWithRelevances;
    }

    public void setTaxonomyWithRelevances(List<TaxonomyWithRelevance> taxonomyWithRelevances) {
        this.taxonomyWithRelevances = taxonomyWithRelevances;
    }

    private List<TaxonomyWithRelevance> taxonomyWithRelevances;

    public TaxonomyRecyclerAdapter() {
        taxonomyWithRelevances = null;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvTaxonomy, tvRelevance, tvInterested, tvIgnored;

        public ViewHolder(View v) {
            super(v);
            tvTaxonomy = (TextView) v.findViewById(R.id.tv_taxonomy);
            tvRelevance = (TextView) v.findViewById(R.id.tv_relevance);
            tvInterested = (TextView) v.findViewById(R.id.tv_interested);
            tvIgnored = (TextView) v.findViewById(R.id.tv_ignored);
        }
    }

    public TaxonomyRecyclerAdapter(List<TaxonomyWithRelevance> taxonomyWithRelevances) {
        this.taxonomyWithRelevances = taxonomyWithRelevances;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.taxonomy_row, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TaxonomyWithRelevance taxonomyWithRelevance = taxonomyWithRelevances.get(position);
        TaxonomyWithRelevance.Taxonomy doc = taxonomyWithRelevance.getDoc();
        holder.tvTaxonomy.setText(doc.getTaxonomy());
        holder.tvRelevance.setText(taxonomyWithRelevance.getRelevance() + "");
        holder.tvInterested.setText(doc.getInterested() + "");
        holder.tvIgnored.setText(doc.getIgnored() + "");
    }

    @Override
    public int getItemCount() {
        if (taxonomyWithRelevances == null)
            return 0;
        return taxonomyWithRelevances.size();
    }

}
