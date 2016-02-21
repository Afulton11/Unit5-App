package com.unit5app;

import com.unit5app.com.unit5app.parsers.CalendarEvent;
import com.unit5app.com.unit5app.parsers.EventType;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Andrew on 2/19/2016.
 * The main Utils Class for our unit5 app. This is where we store methods that are 'utility' methods that can be used in any class.
 */
public class Utils {

    private static final String TAG = "Utils";

    /**
     * returns the index location of a character after finding it n times in the string
     * @param occurrence - the amount of occurences before returning the indexed position in the string.
     * @param needle - the character of which to search for, or pattern, or string of characters
     * @param str - the string to search for the char.
     * @return
     *
     *      Credit to John Giotta on stackOverflow <a href="http://stackoverflow.com/questions/5678152/find-the-nth-occurence-of-a-substring-in-a-string-in-java">Source</a>
     */
    public static int findNthIndexOf (String str, String needle, int occurrence)
            throws IndexOutOfBoundsException {
        int index = -1;
        Pattern p = Pattern.compile(needle, Pattern.MULTILINE);
        Matcher m = p.matcher(str);
        while(m.find()) {
            if (occurrence-- == 0) {
                index = m.start();
                break;
            }
        }
        if (index < 0) throw new IndexOutOfBoundsException();
        return index;
    }


    /**
     * returns the current time as Hours:Minutes AM/PM. Example : 07:15 AM or 2:30 PM
     * @return
     */
    public static String getCurrentHHMMAM() {
        Date date = new Date();

        String strDateFormat = "HH:mm a"; //http://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        String time = sdf.format(date);

        int hours = Integer.parseInt(time.substring(0, 2));

        if(time.contains("PM")) {
            hours -= 12; //we -= 12 because we don't want to use a 24 hour clock.
        }

        time = new String(hours + time.substring(2, time.length()));

        return sdf.format(date);
    }


    /**
     * returns todays date as a string such as: 2/20/16
     * @return
     */
    public static String getTodaysDate() {
        DateFormat sdf = new SimpleDateFormat("MM/dd/yy"); //http://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html
        String formattedDate = sdf.format(new Date());

        StringBuffer buffer = new StringBuffer();
        char lastChar = ' ';
        for(int i = 0; i < formattedDate.toCharArray().length; i++) { //removes all unneeded zeroes in the formatted date. so.. before loop: 04/01/16 --> after loop: 4/1/16
            char c = formattedDate.toCharArray()[i];

            if(c == '0' && i == 0) continue;
            if(lastChar == '/' && c == '0') continue;

            lastChar = c;
            buffer.append(c);
        }

        return buffer.toString();
    }

    /**
     * returns the current day of the week. For example: 'Mon', 'Tue'...'Sun'
     * @return
     */
    public static String getCurrentDOTW() {
        return new SimpleDateFormat("E").format(new Date()).toString();
    }

    /**
     * returns true if the current date of the day is a late start.
     * @return
     */
    public static boolean isTodayLateStart() {
        if(UpcomingEventsActivity.rssCalendarReader.doneParsing) {
            List<CalendarEvent> calendarEvents = UpcomingEventsActivity.rssCalendarReader.getCalendarEvents(); //gets all the calendar events from the rssCalendarReader.

            for (CalendarEvent event : calendarEvents) {
                if (event.getType().toString().equals(EventType.lateStart.toString()) &&
                        event.getDate().equals(getTodaysDate())) {
                    return true;
                }
            }
        }
        return false;
    }

}
