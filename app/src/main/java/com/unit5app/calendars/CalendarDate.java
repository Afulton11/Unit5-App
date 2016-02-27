package com.unit5app.calendars;

import com.unit5app.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 *  This class holds everything there is to ever know about a certain date on the Unit5Calendar.
 *  @author Andrew
 *  @version 2/26/16
 */
public class CalendarDate {

    private String date;
    private List<CalendarEvent> events;
    private String lunch_meal, lunch_special, breakfast_meal, breakfast_special;
    private String morning_announcements;

    /**
     * Creates a new CalendarDate which is used to hold and organize information about a certain date.
     * @param date the date of which this calendarDate will represent for the Unit5Calendar (e.g. '2/26/16')
     */
    public CalendarDate(String date) {
        this.date = date;
        this.events = new ArrayList<>();
    }

    /**
     * @return true if the date contains a CalendarEvent whose type is a latestart.
     */
    public boolean isLateStart() {
        return Utils.isCalendarEventTypeInList(events, EventType.lateStart);
    }

    /**
     * @return true if the date contains a CalendarEvent whose type is noSchool.
     */
    public boolean isNoSchoolDay() {
        return Utils.isCalendarEventTypeInList(events, EventType.noSchool);
    }

    /**
     * @return true if the date contains a CalendarEvent whose type is lastDayBeforeBreak.
     */
    public boolean isLastDayBeforeBreak() {
        return Utils.isCalendarEventTypeInList(events, EventType.lastDayBeforeBreak);
    }

    /**
     * @return true if the date contains a CalendarEvent whose type is a holiday.
     */
    public boolean isHoliday() {
        return Utils.isCalendarEventTypeInList(events, EventType.holiday);
    }

    /**
     * @return true if the date contains a CalendarEvent whose type is endOf.
     */
    public boolean isEndOfGradingPeriod() {
        return Utils.isCalendarEventTypeInList(events, EventType.endOf);
    }

    /**
     * @return true if the date contains a CalendarEvent whose type is a meeting.
     */
    public boolean hasMeeting() {
        return Utils.isCalendarEventTypeInList(events, EventType.meeting);
    }

    /**
     * @return true if the date contains all regular calendar events.
     */
    public boolean isRegularDay() {
        return (!isEndOfGradingPeriod() || !hasMeeting() || !isHoliday() || !isLastDayBeforeBreak() || !isNoSchoolDay() || !isLateStart());
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

    public String getMorning_announcements() {
        return morning_announcements;
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

    public void setMorning_announcements(String announcements) {
        this.morning_announcements = announcements;
    }

    public void setEvents(List<CalendarEvent> calendarEvents) {
        this.events = calendarEvents;
    }

    public void addEvent(CalendarEvent calendarEvent) {
        this.events.add(calendarEvent);
    }


}
