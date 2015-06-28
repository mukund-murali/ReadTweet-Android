package com.mukundvis.twitnews.models;

import java.util.List;

/**
 * Created by mukundvis on 26/06/15.
 */
public class TaxonomyResponse {

    public List<TaxonomyWithRelevance> getUserTaxonomies() {
        return userTaxonomies;
    }

    public void setUserTaxonomies(List<TaxonomyWithRelevance> userTaxonomies) {
        this.userTaxonomies = userTaxonomies;
    }

    List<TaxonomyWithRelevance> userTaxonomies;
}
