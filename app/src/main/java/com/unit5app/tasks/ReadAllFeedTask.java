package com.unit5app.tasks;

import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.widget.ListView;

import com.unit5app.Article;
import com.unit5app.activities.MainActivity;
import com.unit5app.activities.RssActivity;
import com.unit5app.com.unit5app.parsers.CalendarRssReader;
import com.unit5app.com.unit5app.parsers.RSSReader;
import com.unit5app.com.unit5app.parsers.WestNewsReader;
import com.unit5app.utils.MethodHolder;
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

    private RSSReader[] readers;
    private ListView list;

    private List<MethodHolder> methodRequests;

    public ReadAllFeedTask(ListView list, RSSReader... readers) {
        this.list = list;
        this.readers = readers;
        articles = new ArrayList<>();
        all_readers_executing = new ArrayList<>();
        methodRequests = new ArrayList<>();
    }

    public ReadAllFeedTask() {
        articles = new ArrayList<>();
        all_readers_executing = new ArrayList<>();
        methodRequests = new ArrayList<>();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (Utils.getCurrentActivity().getClass() == RssActivity.class) {
            MainActivity.mainCalendar.setNewsArticles(articles);
        }

        if(methodRequests != null && methodRequests.size() > 0) {
            for (MethodHolder holder : methodRequests) { //http://www.javaworld.com/article/2077455/learn-java/dynamically-invoking-a-static-method-without-instance-reference-july-6-1999.html
                holder.callMethod();
            }
            methodRequests.clear();
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

    public void setList(ListView list) {
        this.list = list;
    }

    public List<RSSReader> getCurrentExecutingReaders() {
        return all_readers_executing;
    }

    public RSSReader[] getReaders() {
        return readers;
    }

    public void setReaders(RSSReader... readers) {
        this.readers = readers;
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

    /**
     * adds a method request for when the calendar finished executing <b>NOTE: NOT YET WORKING!!</b>
     * @param methodRequests - the class and method to call. (only works for methods with no params as of right now.)
     *                       <br>format of a method Request string: "package\tmethodName"</br>
     *                       <br>So calling MyNotificationHandler.createNotificationsFromSettings(), Would work as follows: </br>
     *                       <br>addMethodRequests(new String[] {"com.unit5app.notifications.MyNotificationHandler\tcreateNotificationsFromSetting"})</br>
     */
    public void addMethodRequests(final MethodHolder...methodRequests) {
        Collections.addAll(this.methodRequests, methodRequests);
    }
}
