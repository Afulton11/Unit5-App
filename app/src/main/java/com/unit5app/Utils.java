package com.unit5app;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.unit5app.com.unit5app.parsers.CalendarEvent;
import com.unit5app.com.unit5app.parsers.EventType;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Andrew and Ben
 * @version 2/21/2016.
 * Main utility Class for the Unit5 App. This is where we keep versatile, modular methods that can
 * be used in any class.
 */
public class Utils {
    private static final String TAG = "Utils"; /* String name passed to the Logging API */

    public static boolean hadInternetOnLastCheck = false;
    
    /**
     * Gets the index location of 'occurrence' character in a string. Case-sensitive.
     * <ul>Examples:</>
     * <li>findNthIndexOf("Hello, world!", "Hello", "1") returns 0.</>
     * <li>findNthIndexOf("You, you, you.", "you", 2) returns 11.</>
     * @param occurrence - the number of instances of 'needle' to find before returning the index.
     * @param needle - the String to search for within 'str'.
     * @param str - the String to search in for 'needle'.
     * @return int index of the 'occurrence' instance of 'needle'.
     *
     * Credit to John Giotta on www.stackOverflow.com
     * <a href="http://stackoverflow.com/questions/5678152/find-the-nth-occurence-of-a-substring-in-a-string-in-java">Source</a>
     */
    public static int findNthIndexOf (String str, String needle, int occurrence)
            throws IndexOutOfBoundsException {
        /* TODO: Test documentation examples for accuracy. */
        int index = -1;
        Pattern p = Pattern.compile(needle, Pattern.MULTILINE);
        Matcher m = p.matcher(str);
        while(m.find()) {
            if ((occurrence--) == 0) { /* Found occurrence... */
                index = m.start();
                break;
            }
        }
        if (index < 0) throw new IndexOutOfBoundsException(); /* If 'needle' is not found it WILL
                                                                 crash. */
        return index;
    }

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
                hours -= 12; //we -= 12 because we don't want to use a 24 hour clock.
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
        DateFormat sdf = new SimpleDateFormat("MM/dd/yy"); /* http://docs.oracle.com/javase/7/docs/
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
        return new SimpleDateFormat("E").format(date).toString();
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
                        event.getDate().equals(getCurrentDate())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if the user is connected to the internet in some way.
     * @param context - context of an Acivity
     * @return
     *
     *      Author: Silvio Delgado from StackOverFlow at <a href="http://stackoverflow.com/questions/28168867/check-internet-status-from-the-main-activity">link</a>
     */
    public static boolean isInternetConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}