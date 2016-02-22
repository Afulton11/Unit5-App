package com.unit5app.utils;

import com.unit5app.UpcomingEventsActivity;
import com.unit5app.com.unit5app.parsers.CalendarEvent;
import com.unit5app.com.unit5app.parsers.EventType;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Utility class to get and manipulate Time information.
 * @version 2/22/16
 */
public abstract class Time {
    /**
     * Gets the current time based on a given format. ex: 13:05 or 01:05 PM.
     * <ul>Examples:</>
     * <li>getCurrentTime("HH:mm a") would return "12:00 AM".</>
     * <li>getCurrentTime("HH:mm") would return "12:00".</>
     * @param format - String to fit to a SimpleDateFormat.
     * @param twelveHour - true to return time in 12-hour format, false to return in 24-hour.
     * @return a String representing the current Time based on 'format'.
     */
    public static String getCurrentTime(String format, boolean twelveHour) {
        Date date = new Date(); /* Instantiation gets current time to nearest millisecond. */
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String time = sdf.format(date);

        if(twelveHour) {
            int hours = Integer.parseInt(time.substring(0, 2));

            if(time.contains("PM") && hours != 12) {
                hours -= 12; /* -= 12 converts 24-hour time to 12-hour */
            }

            time = new String(hours + time.substring(2, time.length()));
        }
        return time;
    }

    /**
     * Gets the current time based on a given format like a 24-hour clock. ex: 10:05 or 14:05 PM.
     * <ul>Examples:</>
     * <li>getCurrentTime("HH:mm a") would return "00:00 AM".</>
     * <li>getCurrentTime("HH:mm") would return "00:00".</>
     * @param format - String to fit to a SimpleDateFormat.
     * @return a String representing the current Time based on 'format' like a 24-hour clock.
     */
    public static String getCurrentTime(String format) {
        return getCurrentTime(format, false);
    }

    /**
     * Given a format, returns today's date without leading zeroes.
     * <ul>Examples:</>
     * <li>getCurrentDate("MM/dd/yy") would return "1/1/1970".</>
     * <li>getCurrentTime("E, dd of M, YY") would return "Thursday, 1 of January, 1970".</>
     * @param format - String to fit to a SimpleDateFormat.
     * @return a String representing the current Date based on 'format', without leading zeroes.
     */
    public static String getCurrentDate(String format) {
        Date today = new Date(); /* Instantiation gets current time to nearest millisecond. */
        DateFormat sdf = new SimpleDateFormat(format); /* http://docs.oracle.com/javase/7/docs/
                                                            api/java/text/SimpleDateFormat.html */
        String formattedDate = sdf.format(today);
        StringBuffer buf = new StringBuffer();
        char lastChar = ' ';

        /* Remove unnecessary 0s from date. 04/01/10 becomes 4/1/10. */
        for(int i = 0; i < formattedDate.toCharArray().length; i++) {
            char c = formattedDate.toCharArray()[i]; /* Current char */
            if(c == '0' && i == 0) continue; /* Re-loop if first char is 0 */
            if(lastChar == '/' && c == '0') continue; /* Re-loop if the char right after a / is 0 */

            lastChar = c;
            buf.append(c); // This has the effect of only adding in real characters and good 0s.
        }

        return buf.toString();
    }

    /**
     * Gets the day of the week for a specified date. ("Mon", "Tue", "Wed", etc...)
     * @param date - Date to get day of.
     * @return a String representing that Date's day of the week.
     */
    public static String getDOTW(Date date) {
        /*
        Get rid of the parameter and make it just return new SimpleDateFormat("E").format(new Date()).toString();?
         */
        return new SimpleDateFormat("E").format(date).toString();
    }

    public static int getCurrentHour() {
        return Integer.parseInt(getCurrentTime("HH:mm").substring(0, 2));
    }

    public static int getCurrentMinute() {
        return Integer.parseInt(getCurrentTime("HH:mm").substring(3, 5));
    }

    /**
     * Checks to see if today is a Late Start Wednesday.
     * @return true if today is a Late Start, false otherwise.
     */
    public static boolean isTodayLateStart() {
        if (UpcomingEventsActivity.rssCalendarReader.doneParsing()) {

            List<CalendarEvent> calendarEvents = UpcomingEventsActivity.rssCalendarReader.getCalendarEvents(); //gets all the calendar events from the rssCalendarReader.

            for (CalendarEvent event : calendarEvents) {
                if (event.getType().toString().equals(EventType.lateStart.toString()) &&
                        event.getDate().equals(getCurrentDate("MM/dd/yy"))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * checks to see if today is currently the weekend (Saturday, or Sunday).
     * @return true if today is Saturday or Sunday.
     */
    public static boolean isWeekend() {
        String DOTW = getDOTW(new Date());
        if(DOTW.equalsIgnoreCase("Sat") || DOTW.equalsIgnoreCase("Sun")) {
            return true;
        }
        return false;
    }
}
