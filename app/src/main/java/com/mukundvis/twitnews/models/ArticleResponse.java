package com.mukundvis.twitnews.models;

import java.util.List;

/**
 * Created by mukundvis on 28/06/15.
 */
public class ArticleResponse {

    public class Article {
        public String html, title, text, author;
    }

    public boolean hasArticle() {
        return objects != null && objects.size() > 0;
    }

    public List<Article> objects;

}
