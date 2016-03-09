package com.unit5app.calendars;

import com.unit5app.com.unit5app.parsers.RSSReader;
import com.unit5app.tasks.ReadAllFeedTask;
import com.unit5app.tasks.ReadCalendarTask;
import com.unit5app.utils.MethodHolder;
import com.unit5app.utils.Time;
import com.unit5app.utils.Utils;

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
    public void loadNews() {
        if(rssReaders != null) {
            startedLoadingNews = true;
            newsTask = new ReadAllFeedTask();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (Utils.hadInternetOnLastCheck) {
                        if (rssReaders.length > 0) {
                            newsTask.setReaders(rssReaders);
                            newsTask.execute();
                        }
                    }
                }
            }).start();
        }
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

    public void loadLunchMenu(String lunchMenuPdfUrl) {

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

    public ReadCalendarTask getCalendarTask() {
        return calendarTask;
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

    public ReadCalendarTask getCalendarTask() {
        return calendarTask;
    }


}
