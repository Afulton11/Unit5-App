package com.unit5app.calendars;


import android.util.Log;

import com.unit5app.utils.Utils;

/**
 * Created by Andrew on 2/18/2016.
 * This holds all the information needed for each calendar event on a calendar date.
 */
public class CalendarEvent {

    private static final String TAG = "CalendarEvent";

    private String date, timeOccurring, title;
    private EventType type;

    /**
     * creates a new calendar event.
     * @param title -- title of the calendar event
     * @param date -- date of the calendar event
     * @param timeOccurring -- time the event is occuring
     * @param type -- type of event
     */
    public CalendarEvent(String title, String date, String timeOccurring, EventType type) {
        this.title = title;
        this.date = date;
        this.timeOccurring = timeOccurring;
        this.type = type;
    }

    /**
    parses the full title from a calendar rss item title. Used when reading a calendar rss feed.
     */
    public CalendarEvent(String fullTitle) {
        Log.d(TAG, fullTitle);

        int firstSpaceLoc = fullTitle.indexOf(' ');
        if(firstSpaceLoc != -1) {
            this.date = fullTitle.substring(0, firstSpaceLoc);
            int useSpaceNum = 1;
            try {
                this.timeOccurring = fullTitle.substring(firstSpaceLoc + 1, fullTitle.indexOf('M') + 1);
                useSpaceNum = 2; //if we do find a time use the third space found in the fullTitle to get the title from the xml.
            } catch (Exception e){
                this.timeOccurring = "12:00 AM"; // if their is no specific time found in the title string, just set the time occuring to "12am".
                useSpaceNum = 0;
                Log.d(TAG, "\tNo time for calendar event found.");
            } finally {
                this.title = fullTitle.substring(Utils.findNthIndexOf(fullTitle," ", useSpaceNum) + 1); // the rest of the title tag should be the name of the event.;
                parseEventType();
                logEvent();
            }
        }
    }

    /**
     * attempts to retrieve the event type by just looking at the title of the calendarEvent.
     *              - this then sets the EventType of the CalendarEvent to the found Event type.
     */
    private void parseEventType() { //TODO: add a list of all the school holidays to checkfor the 'holiday' event type.
        String tempTitle = this.title;
        tempTitle = tempTitle.toLowerCase();
        if(tempTitle.contains("no school") || tempTitle.contains("teacher's institute") || tempTitle.contains("teacher's work day") || tempTitle.contains("snow day")) { //example: no school
            this.type = EventType.noSchool;
        } else if(tempTitle.contains("late start")) {//example: late start wednesday
            this.type = EventType.lateStart;
        } else if(tempTitle.contains("last day")) {//example last day before spring break;
            this.type = EventType.lastDayBeforeBreak;
        } else if(tempTitle.contains("end of")) { //example: end of first semester
            this.type = EventType.endOf;
        } else if(tempTitle.contains("meeting")) {
            this.type = EventType.meeting;
        } else {
            this.type = EventType.regular;
        }
    }

    public String getDate() { return  date; }
    public String getTitle() { return  title; }
    public String getTimeOccurring() {return timeOccurring; }
    public EventType getType() { return type; }

    /**
     * checks if 2 CalendarEvents are equal to eachother.
     *              - This ignores case in strings.
     * @param obj - the CalendarEvent to compare the current CalendarEvent to.
     * @return boolean
     */
    public boolean equals(Object obj) {
        if(obj instanceof CalendarEvent) {
            CalendarEvent event = (CalendarEvent) obj;
            if (event.getDate().equalsIgnoreCase(this.date) && event.getTimeOccurring().equalsIgnoreCase(this.getTimeOccurring()) &&
                    event.getTitle().equalsIgnoreCase(this.title) && event.getType() == this.type) {
                return true;
            }
        }
        return false;

    }

    public void logEvent() {
        Log.d(TAG, " Title: " + this.title + " Date: " + this.date + " TimeOccuring: " + this.timeOccurring + " Type: " + this.type.toString());
    }
}
