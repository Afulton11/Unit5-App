package com.unit5app;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.unit5app.com.unit5app.parsers.RSSReader;
import com.unit5app.com.unit5app.parsers.ReadAllFeedTask;
import com.unit5app.com.unit5app.parsers.WestNewsReader;

/**
 * Created by Andrew on 2/11/2016.
 * The Activity that reads a rss feed and loads it onto the listView.
 * The items in the list view can then be clicked on to go to an Article Activity.
 */
public class RssActivity  extends ListActivity {

    private static final String TAG = "unit5ActivityRSS";

    private RSSReader rssReader;
    private WestNewsReader westNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rss_layout);

        /**
         * unit5 homepage article rss feed.
         */
        rssReader = new RSSReader("http://www.unit5.org/site/RSS.aspx?DomainID=4&ModuleInstanceID=4&PageID=1");
        westNews = new WestNewsReader("http://www.unit5.org/site/RSS.aspx?DomainID=30&ModuleInstanceID=1852&PageID=53");

        /**
         * retrieves the feed from the rssReader.
         */
        final ReadAllFeedTask task = new ReadAllFeedTask(getListView(), rssReader, westNews);
        task.execute();

        /**
         * what to do for each click on an item in the listview.
         */
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!task.isLoaded()) {
                    Toast.makeText(getApplicationContext(), "Still loading...", Toast.LENGTH_SHORT);
                } else {
                    try {
                        ArticleActivity.setArticle(task.getArticleAt(position));
                    } catch (ArrayIndexOutOfBoundsException e) {
                        Log.d(TAG, "Index out of bounds for articles[position]!");
                    }
                    startActivity(new Intent(RssActivity.this, ArticleActivity.class));
                }
            }
        });

    }
}
