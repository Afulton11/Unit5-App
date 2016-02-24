package com.unit5app;

import android.app.ListActivity;
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

import com.unit5app.com.unit5app.parsers.RSSReader;
import com.unit5app.com.unit5app.parsers.WestNewsReader;
import com.unit5app.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Andrew on 2/11/2016.
 * The Activity that reads a rss feed and loads it onto the listView.
 * The items in the list view can then be clicked on to go to an Article Activity.
 */
public class RssActivity  extends ListActivity {

    private static final String TAG = "unit5ActivityRSS";

    private RSSReader[] all_readers_executing;
    private RSSReader rssReader;
    private WestNewsReader westNews;

    private static String[] loading = new String[] {"loading..."};
    private static List<Article> articles;
    private static List<String> all_titles; //holds a list of all the html parsed and correctly punctuated titles to be used in the listView.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rss_layout);
        articles = new ArrayList<>();
        all_titles = new ArrayList<>();

        /**
         * unit5 homepage article rss feed.
         */
        rssReader = new RSSReader("http://www.unit5.org/site/RSS.aspx?DomainID=4&ModuleInstanceID=4&PageID=1");
        westNews = new WestNewsReader("http://www.unit5.org/site/RSS.aspx?DomainID=30&ModuleInstanceID=1852&PageID=53");

        /**
         * retrieves the feed from the rssReader.
         */
        new ReadAllFeedTask(getListView(), this, rssReader, westNews).execute();

        /**
         * what to do for each click on an item in the listview.
         */
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!isLoaded()) {
                    Toast.makeText(getApplicationContext(), "Still loading...", Toast.LENGTH_SHORT);
                } else {
                    try {
                        ArticleActivity.setArticle(articles.get(position));
                    } catch (ArrayIndexOutOfBoundsException e) {
                        Log.d(TAG, "Index out of bounds for articles[position]!");
                    }
                    startActivity(new Intent(RssActivity.this, ArticleActivity.class));
                }
            }
        });

    }

    private class ReadAllFeedTask extends AsyncTask<Void, Void, Void> {
        private RSSReader[] readers;
        private ListView list;
        private RssActivity activity;

        public ReadAllFeedTask(ListView list, RssActivity activity, RSSReader... readers) {
            this.list = list;
            this.activity = activity;
            this.readers = readers;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            for(RSSReader reader : readers) {
                if(reader instanceof WestNewsReader) {
                    readLinkOnPost();
                } else {
                    readerPostExecute(list, reader);
                }
            }
            Collections.sort(articles, Utils.articlePubDateSorter);
            all_titles.clear();
            for(Article a : articles) {
                all_titles.add(ArticleActivity.toTitleCase(Html.fromHtml(a.getTitle()).toString()));
            }
            getListView().setAdapter(null);
            ArrayAdapter<String> adapterLoaded;
            adapterLoaded = new ArrayAdapter<>(getListView().getContext(), android.R.layout.simple_list_item_1, all_titles);
            adapterLoaded.notifyDataSetChanged();
            getListView().setAdapter(adapterLoaded);
            Toast.makeText(getApplicationContext(), "Done loading!", Toast.LENGTH_SHORT);
        }

        @Override
        protected Void doInBackground(Void... params) {
            for(RSSReader reader : readers) {
                reader.loadXml();
                if(reader instanceof WestNewsReader) {
                    ((WestNewsReader) reader).loadLinks();
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ArrayAdapter<String> adapter = new ArrayAdapter<>(list.getContext(), android.R.layout.simple_list_item_1, loading);
            list.setAdapter(adapter);
            all_readers_executing = readers;
        }

    }

    private void readLinkOnPost() {
        if(westNews.getNewsArticles().size() > 0) {
            for (int i = 0; i < westNews.getArticles().size(); i++) {
                articles.add(westNews.getNewsArticles().get(i));
            }
        }
    }

    private void readLinkInBackground() {
        westNews.loadLinks();
    }

    private void readerInBackground(RSSReader reader) {
        reader.loadXml();
    }

    private void readerPostExecute(ListView list, RSSReader reader) {
        if (list != null && !reader.isCalendar) {
            if(reader.getArticles().size() > 0) {
                for (int i = 0; i < reader.getArticles().size(); i++) {
                    articles.add(reader.getArticles().get(i));
                }
            }
        }
    }

   private boolean isLoaded() {
       for(RSSReader reader : all_readers_executing) {
           if(!reader.doneParsing()) return false;
       }
       return true;
   }
}
