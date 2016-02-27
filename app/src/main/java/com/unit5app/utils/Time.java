package com.unit5app.utils;

import com.unit5app.activities.MainActivity;
import com.unit5app.activities.UpcomingEventsActivity;
import com.unit5app.calendars.CalendarEvent;
import com.unit5app.calendars.EventType;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class to get and manipulate Time information.
 * @version 2/22/16
 */
public abstract class Time {

    public static final String FORMAT_BASIC_DATE = "MM/dd/yy";

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
            time = hours + time.substring(2, time.length());
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
     * <ul>Examples:
     * <li>getCurrentDate("MM/dd/yy") would return "1/1/1970".</>
     * <li>getCurrentTime("E, dd of M, YY") would return "Thursday, 1 of January, 1970".</>
     * </ul>
     *
     * look here for more ways to format the date: <a href="https://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html">JavaDoc</a>
     * @param format - String to fit to a SimpleDateFormat.
     * @return a String representing the current Date based on 'format', without leading zeroes.
     */
    public static String getCurrentDate(String format) {
        Date today = new Date(); /* Instantiation gets current time to nearest millisecond. */
        DateFormat sdf = new SimpleDateFormat(format); /* http://docs.oracle.com/javase/7/docs/
                                                            api/java/text/SimpleDateFormat.html */
        String formattedDate = sdf.format(today);
        StringBuilder buf = new StringBuilder();
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

    private static final int MONTH_MULTIPLIER = 10, DAY_MULTIPLIER = 1, YEAR_MULTIPLIER = 100;

    /**
     * returns the given date as a number. the number of months is multiplied by MONTH_MULTIPLIER ({@value #MONTH_MULTIPLIER}), days by DAY_MULTIPLER ({@value #DAY_MULTIPLIER}),
     * and years by YEAR_MULTIPLIER ({@value #YEAR_MULTIPLIER}).
     * @param date a string date can be any date. e.g. "04/13/01" or "4/1/2"
     * @return returns the date parsed as an integer. so "04/13/01" would be returned as: 40 + 13 + 100, or 153.
     */
    public static int getDateAsNumber(String date) {
        String[] monthDayYear = date.split("/");
        return (Integer.parseInt(monthDayYear[0]) * MONTH_MULTIPLIER) + (Integer.parseInt(monthDayYear[1]) * DAY_MULTIPLIER) + (Integer.parseInt(monthDayYear[2]) * YEAR_MULTIPLIER);
    }

    /**
     * Gets the day of the week for a specified date. ("Mon", "Tue", "Wed", etc...)
     * @param date - Date to get day of.
     * @return a String representing that Date's day of the week.
     */
    public static String getDOTW(Date date) {
        /*
        (Andrew) should we get rid of the parameter and make it just return new SimpleDateFormat("E").format(new Date()).toString();? Or is their applications where inputting a Date is useful?
         */
        return new SimpleDateFormat("E").format(date);
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

            CalendarEvent[] calendarEvents = MainActivity.mainCalendar.getEventsForDate(getCurrentDate("MM/dd/yy")); //gets all the calendar events from the rssCalendarReader.

            for (CalendarEvent event : calendarEvents) {
                if (event.getType().toString().equals(EventType.lateStart.toString())) {
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
        return (getDOTW(new Date()).matches("Sat|Sun"));
        /*(Andrew) the | operation is like saying or in the middle of the string. the | represents a bitwise inclusive OR statement. However
        it doesn't short-circuit like a normal || OR statement. You can switch it back to the regular || if you would like.*/
    }
}