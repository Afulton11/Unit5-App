package com.unit5app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.unit5app.R;
import com.unit5app.com.unit5app.parsers.RSSReader;
import com.unit5app.com.unit5app.parsers.WestNewsReader;
import com.unit5app.utils.Utils;

/**
 * Created by Andrew on 2/11/2016.
 * The Activity that reads a rss feed and loads it onto the listView.
 * The items in the list view can then be clicked on to go to an Article Activity.
 */
public class RssActivity  extends BaseActivity {

    private static final String TAG = "unit5ActivityRSS";

    private RSSReader rssReader;
    private WestNewsReader westNews;

    private String[] loading = {"loading..."};
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rss_layout);
        Utils.setCurrentView(Utils.VIEW_ARTICLE_LIST);

        ListView list = (ListView) findViewById(android.R.id.list);

        if(!MainActivity.mainCalendar.newsStartedLoading()) {
            if(Utils.isInternetConnected(getApplicationContext())) {
                adapter = new ArrayAdapter<>(list.getContext(), android.R.layout.simple_list_item_1, loading);
                adapter.notifyDataSetChanged();
                list.setAdapter(adapter);

                MainActivity.mainCalendar.loadNews();
                Log.d("starting set of news!", "starting set of news!");
                adapter = new ArrayAdapter<>(list.getContext(), android.R.layout.simple_list_item_1, MainActivity.mainCalendar.getNewsTask().getNewsArticleTitlesForList());
                adapter.notifyDataSetChanged();
                list.setAdapter(adapter);
            } else {
                String [] no_internet = {"<b>Unable to Load News</b>, User is <b>not conected to the internet</b>.",
                        "Please <b>retry</b> once you are <b>reconnected to the internet</b>."};
                for(int i = 0; i < no_internet.length; i++) {
                    no_internet[i] = Html.fromHtml(no_internet[i]).toString();
                }
                adapter = new ArrayAdapter<>(list.getContext(), android.R.layout.simple_list_item_1, no_internet);
                adapter.notifyDataSetChanged();
                list.setAdapter(adapter);
            }
        } else {
            adapter = new ArrayAdapter<>(list.getContext(), android.R.layout.simple_list_item_1, MainActivity.mainCalendar.getNewsTask().getNewsArticleTitlesForList());
            adapter.notifyDataSetChanged();
            list.setAdapter(adapter);
        }

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
                        ArticleActivity.setArticle(MainActivity.mainCalendar.getNewsTask().getNewsArticleAt(position));
                    } catch (ArrayIndexOutOfBoundsException e) {
                        Log.d(TAG, "Index out of bounds for articles[position]!");
                    }
                    startActivity(new Intent(RssActivity.this, ArticleActivity.class));
                }
            }
        });
    }
}
