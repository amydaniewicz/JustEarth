/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.justearth;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;

import java.util.ArrayList;
import java.util.List;

public class ArticleActivity extends AppCompatActivity implements LoaderCallbacks<List<Article>>{

    public static final String LOG_TAG = ArticleActivity.class.getName();
    private ArticleAdapter mAdapter;
    private static final int ARTICLE_LOADER_ID = 1;
    private TextView mEmptyStateTextView;



    /**
     * URL to query The Guardian for article information
     */
    private static final String GUARDIAN_REQUEST_URL =
            "https://content.guardianapis.com/search?section=environment&api-key=b3760c6a-d5f0-49a1-aa2a-31f32904f851&show-fields=thumbnail&show-tags=contributor";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_activity);

        // Find a reference to the {@link ListView} in the layout
        ListView articleListView = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.emptyView);
        articleListView.setEmptyView(mEmptyStateTextView);

        // Create a new {@link ArrayAdapter} of articles
        mAdapter = new ArticleAdapter(this, new ArrayList<Article>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        articleListView.setAdapter(mAdapter);

        articleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Article currentArticle = mAdapter.getItem(position);
                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri articleUri = Uri.parse(currentArticle.getUrl());

                // Create a new intent to view the article URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, articleUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();

        if (isConnected) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(ARTICLE_LOADER_ID, null, this);
        } else {
            View loadingSpinner = findViewById(R.id.loadingSpinner);
            loadingSpinner.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet);
        }
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {
        return new ArticleLoader(this, GUARDIAN_REQUEST_URL );
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {

        View loadingSpinner = findViewById(R.id.loadingSpinner);
        loadingSpinner.setVisibility(View.GONE);

        mAdapter.clear();
        // If there is a valid list of {@link Article}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (articles != null && !articles.isEmpty()) {
            mAdapter.addAll(articles);
        }
        mEmptyStateTextView.setText(R.string.no_articles);
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        // Clear the adapter of previous article data
        mAdapter.clear();
    }

}
