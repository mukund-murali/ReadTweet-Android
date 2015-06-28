package com.mukundvis.twitnews.models;

/**
 * Created by mukundvis on 26/06/15.
 */
public class TaxonomyWithRelevance {

    public class Taxonomy {
        public String getTaxonomy() {
            return taxonomy;
        }

        public int getOccurence() {
            return occurence;
        }

        public int getSkipped() {
            return skipped;
        }

        public int getInterested() {
            return interested;
        }

        public int getIgnored() {
            return ignored;
        }

        String taxonomy;
        int occurence, skipped, interested, ignored;
    }

    public float getRelevance() {
        return relevance;
    }

    public void setRelevance(float relevance) {
        this.relevance = relevance;
    }

    float relevance;

    public Taxonomy getDoc() {
        return doc;
    }

    Taxonomy doc;

    @Override
    public String toString() {
        return doc.taxonomy + " - " + relevance;
    }
}

