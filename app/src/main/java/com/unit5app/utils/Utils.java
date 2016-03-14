package com.unit5app.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.unit5app.Article;
import com.unit5app.Settings;
import com.unit5app.calendars.CalendarEvent;
import com.unit5app.calendars.EventType;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Main utility Class for the Unit5 App. This is where we keep versatile, modular methods that can
 * be used in any class.
 * @author Andrew and Ben
 * @version 2/21/2016
 */
public abstract class Utils {
    public static final String SUPERSCRIPT_TH = "<sup><small>th</small></sup>", SUPERSCRIPT_ND = "<sup><small>nd</small></sup>", SUPERSCRIPT_ST = "<sup><small>st</small></sup>", SUPERSCRIPT_RD  = "<sup><small>rd</small></sup>";
    /**
     * The view id for each different activity/view.
     */
    public static final int VIEW_MAIN = 0, VIEW_LOADING = 1, VIEW_ARTICLE_LIST = 2,
            VIEW_ARTICLE = 3, VIEW_UPCOMING_EVENTS = 4, VIEW_ANNOUNCEMENTS = 5,
            VIEW_SETTINGS = 6;
    private static final String TAG = "Unit5Utils"; /* String name passed to the Logging API */
    /**
     * for each thread waiting at the same time, if they each have a different boolean to start again, we need to add another object and another boolean, along with their respective waitFor and unlock methods.
     */
    private static final Object monitor = new Object(), internet_monitor = new Object();
    public static boolean hadInternetOnLastCheck = false;
    /**
     * can be used to sort a List of articles by their publishing dates.
     * <br><ul><b>How To Use:</b><li>Collections.sort(articleListToSort, Utils.articlePubDateSorter);</li></ul></br>
     */
    public static Comparator<Article> articlePubDateSorter = new Comparator<Article>() {
        @Override
        public int compare(Article article0, Article article1) {
            if (Time.getDateAsNumber(article1.getPubDate()) < Time.getDateAsNumber(article0.getPubDate()))
                return -1; //moves article0 up in the index array
            if (Time.getDateAsNumber(article1.getPubDate()) > Time.getDateAsNumber(article0.getPubDate()))
                return +1; //moves article0 down in the index array
            return 0; //keeps articles at the same position in the index array.
        }
    };
    /**
     * can be used to sort a List of CalendarEvents by their dates.
     * <br><ul><b>How To Use:</b><li>Collections.sort(calendarEventsToSort, Utils.calendarEventDateSorter);</li></ul></br>
     */
    public static Comparator<CalendarEvent> calendarEventDateSorter = new Comparator<CalendarEvent>() {
        @Override
        public int compare(CalendarEvent event0, CalendarEvent event1) {
            if (Time.getDateAsNumber(event1.getDate()) < Time.getDateAsNumber(event0.getDate()))
                return -1; //moves event0 up in the index array
            if (Time.getDateAsNumber(event1.getDate()) > Time.getDateAsNumber(event0.getDate()))
                return +1; //moves event0 down in the index array
            return 0; //keeps events at the same position in the index array.
        }
    };
    /**
     * the current View the user is looking at.
     */
    public static int current_view;
    public static boolean monitorState;
    private static boolean paused = false;

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
    public static int findNthIndexOf(String str, String needle, int occurrence)
            throws IndexOutOfBoundsException {
        /* TODO: Test documentation examples for accuracy. */
        int index = -1;
        Pattern p = Pattern.compile(needle, Pattern.MULTILINE);
        Matcher m = p.matcher(str);
        while (m.find()) {
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
     * Gets the number of occurrences of a char in a string
     * @param str - the string to search for the needle.
     * @param needle - the char to search for within 'str'
     * @return number of times the needle was found in the string --> 0, 1, 2, 3..
     */
    public static int getNumOccurrencesInString(String str, char needle) {
        int count = 0;
        for (int i = 0; i < str.toCharArray().length; i++) {
            if (str.toCharArray()[i] == needle) count++;
        }
        return count;
    }

    /**
     * Gets the number of occurrences of a char in a string
     * @param str - the string to search for the needle.
     * @param needle - the string to search for within 'str'
     * @return number of times the needle was found in the string and the index of each occurence. Returns as a Map(occurence, index in str).
     */
    public static Map<Integer, Integer> getOccurrencesWithIndexInString(String str, String needle) {
        Map<Integer, Integer> map_occurrences = new HashMap<>();
        int occurrences = 0;
        Pattern p = Pattern.compile(needle, Pattern.MULTILINE);
        Matcher m = p.matcher(str);
        while (m.find()) {
            map_occurrences.put(occurrences, m.start());
            occurrences++;
        }
        return map_occurrences;
    }

    /**
     * Gets the number of occurrences of a char in a string
     * @param str - the string to search for the needle.
     * @param needle - the char to search for within 'str'
     * @return number of times the needle was found in the string along with the index of each time it was found in the string in a Map(Integer, Integer)
     * <br>To find the number of occurences just say '.size()' after the returned value</br>
     *
     * <br><b>The Map is returned as: (Nth index of find, index in string)</b>
     */
    public static Map<Integer, Integer> getNumOccurrencesWithIndex(String str, char needle) {
        int count = 0;
        Map<Integer, Integer> occurrencesWithIndexes = new HashMap<>();
        for (int i = 0; i < str.toCharArray().length; i++) {
            if (str.toCharArray()[i] == needle) {
                occurrencesWithIndexes.put(count, i);
                count++;
            }
        }
        return occurrencesWithIndexes;
    }

    /**
     * true if the calendarEvent list given contains an event with the given Event type.
     * @param events the CalendarEvents list to search for the type in.
     * @param type EventType to check if the events list contains an event with the type.
     * @return true if the calendarEvent list given contains an event with the given Event type.
     */
    public static boolean isCalendarEventTypeInList(List<CalendarEvent> events, EventType type) {
        for (CalendarEvent event : events) {
            if (event.getType().equals(type)) return true;
        }
        return false;
    }

    /**
     * Returns true if the user is connected to the internet in some way.
     * @param context - context of an Acivity
     * @return
     *
     *      Author: Silvio Delgado from StackOverFlow at <a href="http://stackoverflow.com/questions/28168867/check-internet-status-from-the-main-activity">link</a>
     *
     *      (Andrew) I believe we need this method in here because we may want to use this for other classes in the future e.g. 'The updater class.' Mainly because the user may be traveling or randomly lose internet
     *      access whilst using the app, which could cause a crash when updating the Calendar without internet.
     */
    public static boolean isInternetConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        hadInternetOnLastCheck = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return hadInternetOnLastCheck;
    }

    /*
    Used mainly for titles, this method will make the first letter in every word in the string a Capital.
     */
    public static String toTitleCase(String title) {
        StringBuilder sb = new StringBuilder();
        boolean spaceFound = true;
        title = title.toLowerCase();
        for (char c : title.toCharArray()) {
            if (c == ' ') {
                spaceFound = true;
            } else if (spaceFound) {
                if (c == ' ') {
                    spaceFound = false;
                    continue;
                }
                c = Character.toUpperCase(c);
                spaceFound = false;
            }
            sb.append(c);
        }
        return sb.toString().trim();
    }

    /**
     * This method is a universalOnPause method that is to be used in every activity's onPause() method.
     */
    public static void universalOnPause(Context context) {
        Log.d(TAG, "PAUSE");
        Settings.save(context);
        paused = true;
    }

    /**
     * This method is a universalOnResume method that is to be used in every activity's onResume() method.
     */
    public static void universalOnResume(Context context) {
        Log.d(TAG, "Resume");
        Settings.load(context);
        paused = false;
    }

    /**
     * returns true if the application is currently paused
     * @return true if the application is currently paused
     */
    public static boolean isAppPaused() {
        return paused;
    }

    /**
     * sets the current View to the inputted view.
     * @param viewId the viewId for the activity the user is current viewing.
     */
    public static void setCurrentView(int viewId) {
        current_view = viewId;
    }

    /**
     * returns the current ViewId.
     * <br><ul>A list of all the possible return values
     * <li>&nbsp;&nbsp;&nbsp;&nbsp;VIEW_MAIN = {@value #VIEW_MAIN}</li>
     * <li>&nbsp;&nbsp;&nbsp;&nbsp;VIEW_LOADING = {@value #VIEW_LOADING}</li>
     * <li>&nbsp;&nbsp;&nbsp;&nbsp;VIEW_ARTICLE_LIST = {@value #VIEW_ARTICLE_LIST}</li>
     * <li>&nbsp;&nbsp;&nbsp;&nbsp;VIEW_ARTICLE = {@value #VIEW_ARTICLE}</li>
     * <li>&nbsp;&nbsp;&nbsp;&nbsp;VIEW_UPCOMING_EVENTS = {@value #VIEW_UPCOMING_EVENTS}</li>
     * <li>&nbsp;&nbsp;&nbsp;&nbsp;VIEW_ANNOUNCEMENTS = {@value #VIEW_ANNOUNCEMENTS}</li>
     * </ul></br>
     *
     * @return the current ViewId.
     */
    public static int getCurrent_view() {
        return current_view;
    }

    /**
     * waits on the thread until unlockWaiter() is called by another thread.
     * Acts similar to pThreads and a mutex.
     */
    public static void waitForMonitorState() {  //http://stackoverflow.com/questions/1036754/difference-between-wait-and-sleep
        Log.d(TAG, "waiting!");
        monitorState = true;
        synchronized (monitor) {
                while (monitorState) {
                    try {
                        monitor.wait(); // sleep until notified or monitorState = true.
                    } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * unlocks all threads locked by the method: waitForMonitorState()
     * Acts similar to pThreads and a mutex.
     */
    public static void unlockWaiter() {
        Log.d(TAG, "unlocked waiter!");
        synchronized (monitor) {
            monitorState = false;
            monitor.notify(); // unlock again
        }
    }

    /**
     * similar to waitForMonitorState(), waitForInternet waits until the user is conntected to the internet.
     */
    public static void waitForInternet(Context context) {
        Log.d(TAG, "waiting!");
        synchronized (internet_monitor) {
            while (!isInternetConnected(context)) {
                try {
                    internet_monitor.wait(); // sleep until notified or monitorState = true.
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            unlockInternetWaiter(); //if we ever get out of the while loop, we must be connected to the internet, so unlock the waiter.
        }
    }

    /**
     * similiar to unlockWaiter(), unlockInternetWaiter unlcoks the thread waiting on the internet to move forward.
     */
    public static void unlockInternetWaiter() {
        Log.d(TAG, "unlocked waiter!");
        synchronized (internet_monitor) {
            internet_monitor.notify(); // unlock again
        }
    }
}