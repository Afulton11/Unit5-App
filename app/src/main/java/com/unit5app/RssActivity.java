package com.unit5app;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.unit5app.com.unit5app.parsers.RSSReader;

/**
 * Created by Andrew on 2/11/2016.
 * The Activity that reads a rss feed and loads it onto the listView.
 * The items in the list view can then be clicked on to go to an Article Activity.
 */
public class RssActivity  extends ListActivity {


    private String TAG = "unit5ActivityRSS";

    private ListView list;

    private RSSReader rssReader;

    private String[] loading = new String[] {"loading...", "loading...", "loading...", "loading..."};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rss_layout);
        /**
         * unit5 homepage article rss feed.
         */
        rssReader = new RSSReader("http://www.unit5.org/site/RSS.aspx?DomainID=4&ModuleInstanceID=4&PageID=1");

        /**
         * retrieves the feed from the rssReader.
         */
        new readFeedTask().execute();

        /**
         * what to do for each click on an item in the listview.
         */
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArticleActivity.setTitle(rssReader.getTitles().get(position));
                ArticleActivity.setBody(rssReader.getDescriptions().get(position));
                startActivity(new Intent(RssActivity.this, ArticleActivity.class));
            }
        });

    }

    /**
     * reads the feed from the rssReader titled 'rssReader' and sets the article titles seperate items in list view.
     * TODO: add param for RssReader so that this will work for any RssReader
     * TODO: add param for ListView so that this will work for any ListView.
     */
    private class readFeedTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //when finished parsing do the following:
            getListView().setAdapter(null);
            ArrayAdapter<String> adapterLoaded =  new ArrayAdapter<>(getListView().getContext(), android.R.layout.simple_list_item_1, rssReader.getTitles());
            adapterLoaded.notifyDataSetChanged();
            getListView().setAdapter(adapterLoaded);
        }

        @Override
        protected Void doInBackground(Void... params) {
            rssReader.loadXml();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getListView().getContext(), android.R.layout.simple_list_item_1, loading);
            getListView().setAdapter(adapter);
        }
    }
}
