package com.mukundvis.twitnews.models;

/**
 * Created by mukundvis on 26/06/15.
 */
public class KeywordWithRelevance {

    public class Keyword {
        public String getKeyword() {
            return keyword;
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

        String keyword;
        int occurence, skipped, interested, ignored;
    }

    public float getRelevance() {
        return relevance;
    }

    public void setRelevance(float relevance) {
        this.relevance = relevance;
    }

    float relevance;

    public Keyword getDoc() {
        return doc;
    }

    Keyword doc;

    @Override
    public String toString() {
        return doc.keyword + " - " + relevance;
    }
}

