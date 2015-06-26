package com.mukundvis.twitnews.models;

import java.util.List;

/**
 * Created by mukundvis on 26/06/15.
 */
public class KeywordResponse {

    public List<KeywordWithRelevance> getUserKeywords() {
        return userKeywords;
    }

    public void setUserKeywords(List<KeywordWithRelevance> userKeywords) {
        this.userKeywords = userKeywords;
    }

    List<KeywordWithRelevance> userKeywords;
}
