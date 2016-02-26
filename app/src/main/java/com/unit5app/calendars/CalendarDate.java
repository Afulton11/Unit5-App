package com.unit5app.calendars;

import com.unit5app.com.unit5app.parsers.CalendarEvent;

import java.util.ArrayList;
import java.util.List;

/**
 *This class holds everything there is to ever know about a certain date on the Calendar.
 *
 */
public class CalendarDate {

    private String date;
    private List<CalendarEvent> events;
    private String lunch_meal, lunch_special, breakfast_meal, breakfast_special;

    public CalendarDate(String date) {
        this.date = date;
        this.events = new ArrayList<>();
    }

    public String getDate() {
        return date;
    }

    public String getLunch_meal() {
        return lunch_meal;
    }

    public String getLunch_special() {
        return lunch_special;
    }

    public String getBreakfast_meal() {
        return breakfast_meal;
    }

    public String getBreakfast_special() {
        return breakfast_special;
    }

    public List<CalendarEvent> getEvents() {
        return events;
    }

    public void setLunch_meal(String lunch_meal) {
        this.lunch_meal = lunch_meal;
    }

    public void setLunch_special(String lunch_special) {
        this.lunch_special = lunch_special;
    }

    public void setBreakfast_meal(String breakfast_meal) {
        this.breakfast_meal = breakfast_meal;
    }

    public void setBreakfast_special(String breakfast_special) {
        this.breakfast_special = breakfast_special;
    }

    public void setEvents(List<CalendarEvent> calendarEvents) {
        this.events = calendarEvents;
    }

    public void addEvent(CalendarEvent calendarEvent) {
        this.events.add(calendarEvent);
    }


}
