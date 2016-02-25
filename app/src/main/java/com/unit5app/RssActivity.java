package com.unit5app;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.unit5app.com.unit5app.parsers.RSSReader;
import com.unit5app.com.unit5app.parsers.WestNewsReader;
import com.unit5app.utils.Utils;

/**
 * Created by Andrew on 2/11/2016.
 * The Activity that reads a rss feed and loads it onto the listView.
 * The items in the list view can then be clicked on to go to an Article Activity.
 */
public class RssActivity  extends ListActivity {

    private static final String TAG = "unit5ActivityRSS";

    private RSSReader rssReader;
    private WestNewsReader westNews;

    private String[] loading = {"loading..."};
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rss_layout);
        Utils.setCurrentView(Utils.VIEW_ARTICLE_LIST);

        if(!MainActivity.mainCalendar.newsLoaded()) {
            adapter = new ArrayAdapter<>(getListView().getContext(), android.R.layout.simple_list_item_1, loading);
            adapter.notifyDataSetChanged();
            getListView().setAdapter(adapter);
            new waitUntilNewsLoaded().execute();
        } else {
            adapter = new ArrayAdapter<>(getListView().getContext(), android.R.layout.simple_list_item_1, MainActivity.mainCalendar.getNewsTask().getNewsArticleTitlesForList());
            adapter.notifyDataSetChanged();
            getListView().setAdapter(adapter);
        }

        /**
         * what to do for each click on an item in the listview.
         */
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!MainActivity.mainCalendar.newsLoaded()) {
                    Toast.makeText(getApplicationContext(), "Still loading...", Toast.LENGTH_SHORT);
                } else {
                    try {
                        ArticleActivity.setArticle(MainActivity.mainCalendar.getNewsTask().getNewsArticleAt(position));
                    } catch (ArrayIndexOutOfBoundsException e) {
                        Log.d(TAG, "Index out of bounds for articles[position]!");
                    }
                    startActivity(new Intent(RssActivity.this, ArticleActivity.class));
                }
            }
        });
    }

    private class waitUntilNewsLoaded extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            getListView().setAdapter(null);
            adapter = new ArrayAdapter<>(getListView().getContext(), android.R.layout.simple_list_item_1, MainActivity.mainCalendar.getNewsTask().getNewsArticleTitlesForList());
            adapter.notifyDataSetChanged();
            getListView().setAdapter(adapter);
        }


        @Override
        protected Void doInBackground(Void... params) {
            Log.d(TAG, "waiting for monitor state!");
            if(!MainActivity.mainCalendar.newsLoaded()) {
                Utils.waitForMonitorState();
            }
            Log.d(TAG, "Done waiting for monitor state.");
            return null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Utils.universalOnPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        Utils.universalOnResume();
    }
}
