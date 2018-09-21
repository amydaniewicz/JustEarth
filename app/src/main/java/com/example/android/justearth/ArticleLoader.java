package com.example.android.justearth;

import android.content.Context;
import android.content.AsyncTaskLoader;

import java.util.List;

public class ArticleLoader extends AsyncTaskLoader<List<Article>> {

    /** Tag for log messages */
    private static final String LOG_TAG = ArticleLoader.class.getName();

    /** Query URL */
    private String mUrl;

    public ArticleLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Article> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        // Perform the HTTP request for article data and process the response.
        List<Article> result = QueryUtils.fetchArticleData(mUrl);
        return result;
    }

}
