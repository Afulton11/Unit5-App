package com.unit5app;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Toast;

import com.unit5app.com.unit5app.parsers.RSSReader;
import com.unit5app.com.unit5app.parsers.WestNewsReader;

/**
 * Created by Andrew on 2/11/2016.
 * The Activity that reads a rss feed and loads it onto the listView.
 * The items in the list view can then be clicked on to go to an Article Activity.
 */
public class RssActivity  extends ListActivity {

    public static boolean useWestNews = false;

    private String TAG = "unit5ActivityRSS";

    private CalendarView calendar;

    private RSSReader rssReader;
    private WestNewsReader westNews;

    private static String[] loading = new String[] {"loading...", "loading...", "loading...", "loading..."};
    private static String[] titles, descriptions;

    private boolean links_loaded;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rss_layout);
        links_loaded = false;

        /**
         * the calendar to hold all the data from the unit5.org calendar rss feed.
         */
        calendar = (CalendarView) findViewById(R.id.calendarView);


        /**
         * unit5 homepage article rss feed.
         */
        rssReader = new RSSReader("http://www.unit5.org/site/RSS.aspx?DomainID=4&ModuleInstanceID=4&PageID=1");
        westNews = new WestNewsReader("http://www.unit5.org/site/RSS.aspx?DomainID=30&ModuleInstanceID=1852&PageID=53"); // TODO: retrieve information from the links b/c west doesn't give the information directly for some reason.

        /**
         * retrieves the feed from the rssReader.
         */
        if(useWestNews) {
            new ReadFeedTask(westNews, getListView()).execute();
            new LinkTask().execute();
        } else {
            new ReadFeedTask(rssReader, getListView()).execute();
        }

        /**
         * what to do for each click on an item in the listview.
         */
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!links_loaded && useWestNews) {
                    Toast.makeText(getApplicationContext(), "Still loading...", Toast.LENGTH_SHORT);
                } else {
                    try {
                        ArticleActivity.setTitle(titles[position]);
                        ArticleActivity.setBody(descriptions[position]);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        Log.d(TAG, "Index out of bounds for either descriptions[position] or titles[position]!");
                    }
                    startActivity(new Intent(RssActivity.this, ArticleActivity.class));
                }
            }
        });

    }

    /**
     * loads in the descriptions for a westNewsReader. (parses HTML to get an article from the normal west website)
     */
    private class LinkTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            westNews.loadLinks();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            descriptions = new String[westNews.getDescriptions().size()];
            for (int i = 0; i < descriptions.length; i++) {
                descriptions[i] = westNews.getDescriptions().get(i);
            }
            links_loaded = true;
            Toast.makeText(getApplicationContext(), "Done loading!", Toast.LENGTH_SHORT);
        }
    }

    /**
     * reads the feed from the rssReader titled 'rssReader' and sets the article titles seperate items in list view
     */
    public static class ReadFeedTask extends AsyncTask<Void, Void, Void> {

        private RSSReader reader;
        private ListView list;

        public ReadFeedTask(RSSReader reader, ListView list) {
            this.reader = reader;
            this.list = list;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //when finished parsing do the following:
            if (list != null) {
                titles = new String[reader.getTitles().size()];
                for (int i = 0; i < titles.length; i++) {
                    Spanned resultTitle = Html.fromHtml(reader.getTitles().get(i));
                    titles[i] = ArticleActivity.toTitleCase(resultTitle.toString().toLowerCase());
                }
                if(!useWestNews) {
                    descriptions = new String[reader.getDescriptions().size() - 1];
                    for(int i = 1; i < descriptions.length; i++) {
                        descriptions[i - 1] = reader.getDescriptions().get(i);
                    }
                }

                list.setAdapter(null);
                ArrayAdapter<String> adapterLoaded = new ArrayAdapter<String>(list.getContext(), android.R.layout.simple_list_item_1, titles);
                adapterLoaded.notifyDataSetChanged();
                list.setAdapter(adapterLoaded);
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            reader.loadXml();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(list.getContext(), android.R.layout.simple_list_item_1, loading);
            list.setAdapter(adapter);
        }
    }
}
