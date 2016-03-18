package com.unit5app.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.unit5app.R;
import com.unit5app.calendars.Unit5Calendar;
import com.unit5app.utils.Utils;

/**
 * Created by Andrew on 2/11/2016.
 * The Activity that reads a rss feed and loads it onto the listView.
 * The items in the list view can then be clicked on to go to an Article Activity.
 */
public class RssActivity  extends BaseActivity {

    private static final String TAG = "unit5ActivityRSS";
    private ArrayAdapter<String> adapter;
    private ListView list;
    private String[] titleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rss_layout);
        list = (ListView) findViewById(android.R.id.list);
        if(MainActivity.mainCalendar == null) MainActivity.mainCalendar = new Unit5Calendar(60);
        if(savedInstanceState != null) {
            titleList = savedInstanceState.getStringArray("key");
            adapter = new ArrayAdapter<>(list.getContext(), android.R.layout.simple_list_item_1, titleList);
            adapter.notifyDataSetChanged();
            list.setAdapter(adapter);
        } else if(!MainActivity.mainCalendar.newsLoaded()) {
            if(!MainActivity.mainCalendar.newsStartedLoading()) {
                if (Utils.isInternetConnected(getApplicationContext())) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            setListViewLoading();
                            MainActivity.mainCalendar.loadNews(getApplicationContext());
                        }
                        @Override
                        protected Void doInBackground(Void... params) {
                            return null;
                        }
                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            setListViewComplete();
                        }
                    }.execute();
                } else {
                    String[] no_internet = {"<b>Unable to Load News</b>, User is <b>not conected to the internet</b>.",
                            "Please <b>retry</b> once you are <b>reconnected to the internet</b>."};
                    for (int i = 0; i < no_internet.length; i++) {
                        no_internet[i] = Html.fromHtml(no_internet[i]).toString();
                    }
                    adapter = new ArrayAdapter<>(list.getContext(), android.R.layout.simple_list_item_1, no_internet);
                    adapter.notifyDataSetChanged();
                    list.setAdapter(adapter);
                }
            } else {
                setListViewLoading();
            }
        } else {
            setListViewComplete();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putStringArray("key", titleList);
        super.onSaveInstanceState(savedInstanceState);
    }

    public void setListViewComplete() {
        String[] article_titles = MainActivity.mainCalendar.getNewsTitles();
        setContentView(R.layout.rss_layout);
        Log.d(TAG, "length: " + article_titles.length);
        list = (ListView) findViewById(android.R.id.list);
        adapter = new ArrayAdapter<>(list.getContext(), android.R.layout.simple_list_item_1, article_titles);
        adapter.notifyDataSetChanged();
        list.setAdapter(adapter);
        titleList = article_titles;

        /**
         * what to do for each click on an item in the listview.
         */
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!MainActivity.mainCalendar.newsLoaded()) {
                    Toast.makeText(getApplicationContext(), "Still loading...", Toast.LENGTH_SHORT);
                } else {
                    try {
                        ArticleActivity.setArticle(MainActivity.mainCalendar.getNewsArticles()[position]);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        Log.d(TAG, "Index out of bounds for articles[position]!");
                    }
                    startActivity(new Intent(RssActivity.this, ArticleActivity.class));
                }
            }
        });
    }

    public void setListViewLoading() {
        setContentView(R.layout.view_loading);
    }

    @Override
    public void onPause() {
        super.onPause();
        Utils.universalOnPause(getApplicationContext());
        MainActivity.mainCalendar.saveNews(getApplicationContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        Utils.universalOnResume(getApplicationContext());
        MainActivity.mainCalendar.saveNews(getApplicationContext());
    }
}
