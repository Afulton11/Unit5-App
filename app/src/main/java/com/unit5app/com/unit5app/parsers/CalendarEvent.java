package com.unit5app.com.unit5app.parsers;

/**
 * Created by Andrew on 2/18/2016.
 * This holds all the information needed for each calendar event on a calendar date.
 */
public class CalendarEvent {

    private String date, timeOccurring, title;
    private EventType type;

    private enum EventType {regular, holiday,};

    public CalendarEvent(String title, String date, String timeOccurring, EventType type) {
        this.title = title;
        this.date = date;
        this.timeOccurring = timeOccurring;
        this.type = type;
    }

    public String getDate() { return  date; }
    public String getTitle() { return  title; }
    public String getTimeOccurring() { return timeOccurring; }
    public EventType getType() { return type; }
}
