package com.unit5app;

import com.unit5app.activities.MainActivity;
import com.unit5app.com.unit5app.parsers.CalendarEvent;
import com.unit5app.com.unit5app.parsers.RSSReader;
import com.unit5app.tasks.ReadAllFeedTask;
import com.unit5app.tasks.ReadCalendarTask;
import com.unit5app.utils.Time;
import com.unit5app.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Calendar {

    /*
    TODO: add the day's lunch to every date.
     */

    public static final CalendarEvent[] NO_CALENDAR_EVENTS = {};
    private static final String CALENDAR_URL = "http://www.unit5.org/site/RSS.aspx?DomainID=4&ModuleInstanceID=1&PageID=2";

    private ReadAllFeedTask newsTask;

    private List<CalendarEvent> calendarEvents;
    private boolean startedLoadingCalendar, startedLoadingNews;

//    private CalendarDate[] dates;

    /**
     * creates a new Calendar from the Main calendar url: <a href="{@value #CALENDAR_URL}">See Calendar Url</a>
     * @param numDays the number of days to extend from the current day.
     */
    public Calendar(int numDays) {
//        dates = new CalendarDate[numDays];
        if(Utils.hadInternetOnLastCheck) {
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
     * @param readers the RssReaders used to load the news from the school websites.
     */
    public void loadNews(RSSReader... readers) {
        if(Utils.hadInternetOnLastCheck && !startedLoadingNews) {
            startedLoadingNews = true;
            newsTask = new ReadAllFeedTask();
            if(readers.length == 0) readers = MainActivity.newsReaders;
            newsTask.setReaders(readers);
            newsTask.execute();
            Utils.waitForMonitorState();
        }
    }

    /**
     * loads the Calendar with calendarEvents from the Unit 5 calendar.
     */
    public void loadCalendar() {
        if(!startedLoadingCalendar) {
            startedLoadingCalendar = true;
            calendarEvents = new ArrayList<>();
            ReadCalendarTask calendarTask = new ReadCalendarTask(CALENDAR_URL);
            calendarTask.execute();
            if (!calendarTask.isLoaded()) Utils.waitForMonitorState();
            for (CalendarEvent event : calendarTask.getCalendarEvents()) {
                calendarEvents.add(event);
            }
            sortCalendarEvents();
        }
    }

    public void loadLunchMenu(String lunchMenuPdfUrl) {

    }

    /**
     * This would combine all of today's info onto one date (events, lunch meals, breakfast meals, announcements, and anything else. Perhaps we should make an Overall 'CalendarDate' class or something).
     */
    public void loadTodaysInformation() {

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
            if(Time.getDateAsNumber(date) == Time.getDateAsNumber(event.getDate())) {
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
        return (newsTask != null && newsTask.isLoaded());
    }

    public boolean newsStartedLoading() {
        return startedLoadingNews;
    }

    /**
     * true if the calendar is loaded or is currently being loaded
     * @return true if the calendar has been loaded or is currently being loaded
     */
    public boolean hasCalendarStartedLoading() {
        return startedLoadingCalendar;
    }

}
