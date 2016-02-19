package com.unit5app.com.unit5app.parsers;

/**
 * Created by Andrew on 2/18/2016.
 */
public class CalendarEvent {

    private String date, timeOccurring, title;

    public CalendarEvent(String title, String date, String timeOccurring) {
        this.title = title;
        this.date = date;
        this.timeOccurring = timeOccurring;
    }

    public String getDate() { return  date; }
    public String getTitle() { return  title; }
    public String getTimeOccurring() { return timeOccurring; }
}
