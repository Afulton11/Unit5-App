package com.unit5app.calendars;

import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;

import com.unit5app.Article;
import com.unit5app.com.unit5app.parsers.RSSReader;
import com.unit5app.com.unit5app.parsers.WestNewsReader;
import com.unit5app.tasks.ReadAllFeedTask;
import com.unit5app.tasks.ReadCalendarTask;
import com.unit5app.utils.MethodHolder;
import com.unit5app.utils.Time;
import com.unit5app.utils.Utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Unit5Calendar{

    /*
    TODO: add the day's lunch to every date.
     */

    public static final CalendarEvent[] NO_CALENDAR_EVENTS = {};
    private static final String CALENDAR_URL = "http://www.unit5.org/site/RSS.aspx?DomainID=4&ModuleInstanceID=1&PageID=2";

    private RSSReader[] rssReaders;
    private ReadAllFeedTask newsTask;
    private File newsFile;
    private final String NEWS_FILE_NAME = "articles.txt";
    private boolean newsReady;

    private ReadCalendarTask calendarTask;

    private List<CalendarEvent> calendarEvents;
    private boolean startedLoadingCalendar, startedLoadingNews;

    /*TODO: inform each calendar date of everything else besides calendar events.*/
    private CalendarDate[] dates;


    /**
     * creates a new Calendar from the Main calendar url: <a href="{@value #CALENDAR_URL}">See Calendar Url</a>
     * @param numDays the number of days to extend from the current day.
     */
    public Unit5Calendar(int numDays) {
        WestNewsReader westNews = new WestNewsReader("http://www.unit5.org/site/RSS.aspx?DomainID=30&ModuleInstanceID=1852&PageID=53");
        RSSReader unit5News = new RSSReader("http://www.unit5.org/site/RSS.aspx?DomainID=4&ModuleInstanceID=4&PageID=1");
        setNewsRssReaders(westNews, unit5News);
        newsTask = new ReadAllFeedTask();
        dates = new CalendarDate[numDays];
        String currentDate = Time.getCurrentDate(Time.FORMAT_BASIC_DATE);
        int currentDateNum = Time.getDateAsNumber(currentDate);
        String previousDate = currentDate;
        for(int i = 0; i < dates.length; i++) {
            if(i == 0) {
                dates[0] = new CalendarDate(currentDate);
            } else {
                currentDate = Time.getDateAfterDate(previousDate);
                dates[i] = new CalendarDate(currentDate);
                previousDate = currentDate;
            }
        }
        if(Utils.hadInternetOnLastCheck) {
            loadCalendar();
        }
    }

    /**
     * creates a new Calendar from the Main calendar url: <a href="{@value #CALENDAR_URL}">See Calendar Url</a>
     * @param numDays the number of days to extend from the current day.
     * @param loadCalendarImmediately true to load the calendar in the constructor, false to load the calendar from calling a loadcalendar() function.
     */
    public Unit5Calendar(int numDays, boolean loadCalendarImmediately) {
        dates = new CalendarDate[numDays];
        String currentDate = Time.getCurrentDate(Time.FORMAT_BASIC_DATE);
        int currentDateNum = Time.getDateAsNumber(currentDate);
        String previousDate = currentDate;
        for(int i = 0; i < dates.length; i++) {
            if(i == 0) {
                dates[0] = new CalendarDate(currentDate);
            } else {
                currentDate = Time.getDateAfterDate(previousDate);
                dates[i] = new CalendarDate(currentDate);
                previousDate = currentDate;
            }
        }
        if(loadCalendarImmediately && Utils.hadInternetOnLastCheck) {
            loadCalendar();
        }
    }

    /**
     * this can be called from the UpdateManager, once we get that going.
     */
    public void update() {

    }

    /**
     * loads the news if there was internet on the last time we checked for internet.
     * <br><b>Make Sure to always setNewsRssReaders() in the calendar before calling loadNews()!</b></br>
     */
    public void loadNews(final Context context) {
        if(newsFile == null) newsFile = new File(context.getFilesDir(), NEWS_FILE_NAME);
        if(newsFile.exists()) {
            newsArticles = loadNewsFromFile(context);
        } else if(rssReaders != null) {
            updateNews();
        }
    }

    /**
     * updates the news and the news file..
     */
    private void updateNews() {
        startedLoadingNews = true;
        newsReady = false;
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (Utils.hadInternetOnLastCheck) { //will be used when fetching files to compare with old file for an update.
                    if (rssReaders.length > 0) {
                        newsTask.setReaders(rssReaders);
                        newsTask.execute();
                    }
                }
            }
            @Override
            protected Void doInBackground(Void... params) {
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                newsReady = true;
            }
        }.execute();
    }

    private static List<Article> newsArticles = new ArrayList<>();
    private final String START_ARTICLE = "START_ARTICLE", END_ARTICLES = "END_ARTICLES", NEXT = "@NXT@";

    public void saveNews(Context context) {
        try {
            if(newsFile == null) newsFile = new File(context.getFilesDir(), NEWS_FILE_NAME);
            if (!newsFile.exists()) newsFile.createNewFile();

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(context.openFileOutput(NEWS_FILE_NAME, Context.MODE_PRIVATE)));

            Collections.sort(newsArticles, Utils.articlePubDateSorter);
            for(Article article : newsArticles) {
                writer.write(START_ARTICLE);
                writer.newLine();
                if(article.hasPubDate()) {
                    writer.write("pubDate:" + article.getPubDate());
                    writer.newLine();
                    writer.write(NEXT);
                    writer.newLine();
                }
                writer.write("title:" + article.getTitle());
                writer.newLine();
                writer.write(NEXT);
                writer.newLine();
                writer.write("desc:" + article.getDescription());
                writer.newLine();
                writer.write(NEXT);
                writer.newLine();
            }
            writer.write(END_ARTICLES);
        } catch (IOException e) {
            Log.d("Unit5Calendar", e.getMessage(), e);
        }
    }

    private List<Article> loadNewsFromFile(Context context) {
        startedLoadingNews = true;
        List<Article> articles = new ArrayList<>();
        if(newsFile == null) newsFile = new File(context.getFilesDir(), NEWS_FILE_NAME);
        if(newsFile.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(newsFile));
                String currentLine;
                while ((currentLine = reader.readLine()) != null) {
                    if (currentLine.equals(START_ARTICLE)) {
                        Log.d("q32", currentLine);
                        articles.add(readArticle(reader));
                    }
                }
            } catch (IOException e) {
                Log.d("Unit5Calendar", e.getMessage(), e);
            }
            Collections.sort(newsArticles, Utils.articlePubDateSorter);
            newsReady = true;
        }
        return articles;
    }

    private Article readArticle(BufferedReader reader) throws IOException{
        Article article = new Article();
        String currentLine;
        while((currentLine = reader.readLine()) != null && !currentLine.equals(END_ARTICLES) && !currentLine.equals(START_ARTICLE)) {
            int firstCol = currentLine.indexOf(':') + 1; //we want the text after the first colon, so we add 1.
            if(currentLine.startsWith("pubDate")) {
                article.setPubDate(readPubDate(currentLine, reader, firstCol));
            } else if(currentLine.startsWith("title")) {
                article.setTitle(readTitle(currentLine, reader, firstCol));
            } else if(currentLine.startsWith("desc")) {
                article.setDescription(readDescription(currentLine, reader, firstCol));
            }
        }
        return article;
    }

    private String readPubDate(String currentLine, BufferedReader reader, int colPos) throws IOException{
        String pubDate = currentLine.substring(colPos);
        Log.d("q32", currentLine);
        while((currentLine = reader.readLine()) != null && !currentLine.equals(NEXT)) {
            pubDate += System.getProperty("line.separator") + currentLine;
        }
        return pubDate;
    }

    private String readTitle(String currentLine, BufferedReader reader, int colPos) throws IOException {
        String title = currentLine.substring(colPos);
        Log.d("q32", currentLine);

        while((currentLine = reader.readLine()) != null && !currentLine.equals(NEXT)) {
            title += System.getProperty("line.separator") + currentLine;
            Log.d("Calendar23", currentLine);
        }
        return title;
    }

    private String readDescription(String currentLine, BufferedReader reader, int colPos) throws IOException {
        String description = currentLine.substring(colPos);
        Log.d("q32", currentLine);

        while((currentLine = reader.readLine()) != null && !currentLine.equals(NEXT)) {
            description += System.getProperty("line.separator") + currentLine;
        }
        return description;
    }

    /**
     * @param readers the RssReaders used to load the news from the school websites.
     */
    public void setNewsRssReaders(RSSReader...readers) {
        rssReaders = readers;
    }

    /**
     * loads the Calendar with calendarEvents from the Unit 5 calendar.
     */
    public void loadCalendar() {
        if(!startedLoadingCalendar) {
            startedLoadingCalendar = true;
            calendarEvents = new ArrayList<>();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ReadCalendarTask calendarTask = new ReadCalendarTask(CALENDAR_URL);
                    calendarTask.execute();
                    if (!calendarTask.isLoaded()) Utils.waitForMonitorState();
                    for (CalendarEvent event : calendarTask.getCalendarEvents()) {
                        calendarEvents.add(event);
                    }
                    sortCalendarEvents();
                    for(CalendarDate date : dates) {
                        for(CalendarEvent event : calendarEvents) {
                            if(event.getDate().equals(date.getDate())) date.addEvent(event);
                        }
                    }
                }
            }).start();
        }
    }

    /**
     * loads a calendar and calls the requested method at the end of the calendar async task.
     * @param methodRequests - the class and method to call. (only works for methods with no params as of right now.)
     *                       <br>format of a method Request string: "package\tmethodName"</br>
     *                       <br>So calling MyNotificationHandler.createNotificationsFromSettings(), Would work as follows: </br>
     *                       <br>addMethodRequests(new String[] {"com.unit5app.notifications.MyNotificationHandler\tcreateNotificationsFromSetting"})</br>
     */
    public void loadCalendar(final MethodHolder... methodRequests) {
        if(!startedLoadingCalendar) {
            startedLoadingCalendar = true;
            calendarEvents = new ArrayList<>();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    calendarTask = new ReadCalendarTask(CALENDAR_URL);
                    calendarTask.addMethodRequests(methodRequests);
                    calendarTask.execute();
                    if (!calendarTask.isLoaded()) Utils.waitForMonitorState();
                    for (CalendarEvent event : calendarTask.getCalendarEvents()) {
                        calendarEvents.add(event);
                    }
                    sortCalendarEvents();
                    for(CalendarDate date : dates) {
                        for(CalendarEvent event : calendarEvents) {
                            if(event.getDate().equals(date.getDate())) date.addEvent(event);
                        }
                    }
                }
            }).start();
        }
    }

    /**
     * This would combine all of today's info onto one date (events, lunch meals, breakfast meals, announcements, and anything else. Perhaps we should make an Overall 'CalendarDate' class or something).
     */
    public void loadTodaysInformation() {
        String date = Time.getCurrentDate(Time.FORMAT_BASIC_DATE);
    }

    /**
     *
     * @param date the date to get the events for
     * @return a CalendarEvent[] containing all the events happening on that day. If the date is blank, it will return an empty array.
     *              <ul>You can test if the returned array is blank by doing this:
     *                  <li>&nbsp;&nbsp;&nbsp;&nbsp;returned array == Calendar.NO_CALENDAR_EVENTS</li></ul>
     */
    public CalendarEvent[] getEventsForDate(String date) {
        List<CalendarEvent> events = new ArrayList<>();
        for(CalendarEvent event : calendarEvents) {
            if(Time.isDateEqualToDate(date, event.getDate())) {
                events.add(event);
            }
        }
        if(events.size() < 1) return NO_CALENDAR_EVENTS;
        CalendarEvent[] events_array = new CalendarEvent[events.size()];
        events.toArray(events_array);
        return events_array;
    }

    /**
     * sorts the calendar events by date.
     */
    public void sortCalendarEvents() {
        Collections.sort(calendarEvents, Utils.calendarEventDateSorter);
    }

    public ReadAllFeedTask getNewsTask() {
        return newsTask;
    }

    public boolean newsLoaded() {
        return newsReady;
    }

    public boolean newsStartedLoading() {
        return startedLoadingNews;
    }

    public static void setNewsArticles(List<Article> articles) {
        newsArticles = articles;
        Collections.sort(newsArticles, Utils.articlePubDateSorter);
    }

    public Article[] getNewsArticles() {
        Article[] articles = new Article[newsArticles.size()];
        for(int i = 0; i < articles.length; i++) {
            articles[i] = newsArticles.get(i);
        }
        return articles;
    }

    public String[] getNewsTitles() {
        String[] titles = new String[newsArticles.size()];
        for(int i = 0; i < newsArticles.size(); i++) {
            titles[i] = Utils.toTitleCase(Html.fromHtml(newsArticles.get(i).getTitle()).toString());
        }
        return titles;
    }

    /**
     * true if the calendar is loaded or is currently being loaded
     * @return true if the calendar has been loaded or is currently being loaded
     */
    public boolean hasCalendarStartedLoading() {
        return startedLoadingCalendar;
    }

    public ReadCalendarTask getCalendarTask() {
        return calendarTask;
    }


}
