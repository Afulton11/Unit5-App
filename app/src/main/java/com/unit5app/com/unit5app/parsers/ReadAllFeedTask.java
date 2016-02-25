package com.unit5app.com.unit5app.parsers;

import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.unit5app.Article;
import com.unit5app.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Andrew
 * @version 2/24/16
 */
public class ReadAllFeedTask extends AsyncTask<Void, Void, Void> {

    private List<RSSReader> all_readers_executing;
    private List<Article> articles;
    private List<String> all_titles; //holds a list of all the html parsed and correctly punctuated titles to be used in the listView.

    private String[] loading = {"loading..."};

    private RSSReader[] readers;
    private ListView list;

    public ReadAllFeedTask(ListView list, RSSReader... readers) {
        this.list = list;
        this.readers = readers;
        articles = new ArrayList<>();
        all_titles = new ArrayList<>();
        all_readers_executing = new ArrayList<>();
    }

    public ReadAllFeedTask() {
        articles = new ArrayList<>();
        all_titles = new ArrayList<>();
        all_readers_executing = new ArrayList<>();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(list != null) {
            all_titles.clear();
            for (Article a : articles) {
                all_titles.add(Utils.toTitleCase(Html.fromHtml(a.getTitle()).toString()));
            }
            list.setAdapter(null);
            ArrayAdapter<String> adapterLoaded;
            adapterLoaded = new ArrayAdapter<>(list.getContext(), android.R.layout.simple_list_item_1, all_titles);
            adapterLoaded.notifyDataSetChanged();
            list.setAdapter(adapterLoaded);
            Toast.makeText(list.getContext(), "Done loading!", Toast.LENGTH_SHORT);
        }
    }

    @Override
    protected Void doInBackground(Void... params) {
        for(RSSReader reader : readers) {
            readerInBackground(reader);
            if(reader instanceof WestNewsReader) {
                readLinkInBackground((WestNewsReader) reader);
            }
        }
        for(RSSReader reader : readers) {
            if (reader instanceof WestNewsReader) {
                readLinkOnPost((WestNewsReader) reader);
            } else if(!(reader instanceof CalendarRssReader)) {
                readerPostExecute(reader);
            }
            all_readers_executing.remove(reader);
        }
        if(articles.size() > 0)
            Collections.sort(articles, Utils.articlePubDateSorter);
        Utils.unlockWaiter();
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(list != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(list.getContext(), android.R.layout.simple_list_item_1, loading);
            list.setAdapter(adapter);
        }
        Collections.addAll(all_readers_executing, readers);
    }

    private void readLinkOnPost(WestNewsReader reader) {
        if(reader.getNewsArticles().size() > 0) {
            for (int i = 0; i < reader.getArticles().size(); i++) {
                articles.add(reader.getNewsArticles().get(i));
            }
        }
    }

    private void readLinkInBackground(WestNewsReader reader) {
        reader.loadLinks();
    }

    private void readerInBackground(RSSReader reader) {
        reader.loadXml();
        Utils.unlockWaiter();
    }

    private void readerPostExecute(RSSReader reader) {
        if (!(reader instanceof CalendarRssReader)) {
            if(reader.getArticles().size() > 0) {
                for (int i = 0; i < reader.getArticles().size(); i++) {
                    articles.add(reader.getArticles().get(i));
                }
            }
        }
    }

    public boolean isLoaded() {
        if(all_readers_executing != null) {
            for (RSSReader reader : all_readers_executing) {
                if (!reader.doneParsing()) return false;
            }
            return true;
        }
        return false;
    }

    public void setReaders(RSSReader... readers) {
        this.readers = readers;
    }

    public void setList(ListView list) {
        this.list = list;
    }

    public List<RSSReader> getCurrentExecutingReaders() {
        return all_readers_executing;
    }

    public RSSReader[] getReaders() {
        return readers;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public Article getNewsArticleAt(int index) {
        return articles.get(index);
    }

    public String[] getNewsArticleTitlesForList() {
        Log.d("Articles", "Articles: " + articles.size());
        String[] titles = new String[articles.size()];
        for(int i = 0; i < titles.length; i++) {
            titles[i] = Utils.toTitleCase(Html.fromHtml(getNewsArticleAt(i).getTitle()).toString());
        }
        return titles;
    }
}
