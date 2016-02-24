package com.unit5app;

import com.unit5app.com.unit5app.parsers.RSSReader;
import com.unit5app.com.unit5app.parsers.ReadAllFeedTask;


public class Calendar {

    private ReadAllFeedTask rssTask;

    public Calendar(String calendarUrl) {

    }

    public void update() {

    }

    public void loadNews(RSSReader... readers) {
        rssTask = new ReadAllFeedTask(null, readers);
        rssTask.execute();
    }

    public void loadLunchMenu(String lunchMenuPdfUrl) {

    }

    public void loadCurrentHourInfo() {

    }

    public void loadTodaysEvents() {
    }

}
