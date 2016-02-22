package com.unit5app;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.widget.TextView;

/**
 * @author Andrew
 * @version 2/21/2016.
 */
public class EndOfHourHandler {

    private static final String TAG = "EndOfHourHandler";

    private TextView view;
    private Context context;

    /* NOTE: Having multiple arrays with indexes that match up is behavior similar to objects.
     * Should we just create a new "Period.java" class? */

    /* Hour names ("1st, 2nd, 3rd, etc...") */
    private String[] periods = new String[] {"0<sup><small>th</small></sup>",
            "1<sup><small>st</small></sup>", "2<sup><small>nd</small></sup>",
            "3<sup><small>rd</small></sup>", "4<sup><small>th</small></sup>",
            "5<sup><small>th</small></sup>", "6<sup><small>th</small></sup>",
            "7<sup><small>th</small></sup>", "8<sup><small>th</small></sup>"};
    /* Period ending times (earliest to latest) in format HH:mm a with leading 0s left. */
    private String[] endOfHourTimes = new String[] {"07:10 AM", "08:05 AM", "09:00 AM",
                                                    "09:55 AM", "10:50 AM", "11:45 AM",
                                                    "12:40 PM", "01:35 PM", "02:30 PM"};

    /* Index representing the current period and its ending time */
    private int currentPeriod = 0;

    public void start() {
        final long initialDelayTime = 2235; //the time to wait before starting the entire loop that uses delayTime.
        final long delayTime = 10000; //the amount of time to wait before checking the time again in milliseconds. 1000 ms = 1 second.
        final String startBufferText = "# hour ends at [TIME-HERE].";

        try {
            Runnable timerTask = new Runnable() { //used to check the time and update the textView every 30 seconds.
                StringBuffer buffer;
                @Override
                public void run() {
                    if( isSchoolInSession() && (!Utils.hadInternetOnLastCheck || UpcomingEventsActivity.rssCalendarReader.isDoneParsing())) { //we make sure the calendar reader is done parsing because if it isn't, we may get some null pointer exceptions when checking things about today's date.
                        buffer = new StringBuffer(startBufferText);

                        setCurrentPeriodAndEndTime();

                        int periodLoc = buffer.indexOf("#");
                        buffer.replace(0, periodLoc + 1, periods[currentPeriod]);

                        int hourLoc = buffer.indexOf("[TIME-HERE]");
                        String s_currentEndTime = endOfHourTimes[currentPeriod];
                        buffer.delete(hourLoc, hourLoc + 10); //removes the colon as it is not needed anymore.
                        buffer.replace(hourLoc, hourLoc + 1, s_currentEndTime);

                        if (s_currentEndTime.toCharArray()[0] == '0') //removes the 0 from the start of the end time, if the first character is a 0.
                            buffer.delete(hourLoc, hourLoc + 1);

                        Spanned formatted = Html.fromHtml(buffer.toString());

                        view.setText(formatted); //sets the text of the textView after doing any html formatting to the text.

                        /**
                         * My attempt at reloading the calendar to be used by the clock when the user is reconnected to the internet
                         * Not sure if this works yet.
                         */
                        if(!Utils.hadInternetOnLastCheck && Utils.isInternetConnected(context)) {
                            UpcomingEventsActivity.loadCalendar();
                        }

                    } else {
                        view.setText("School is not currently in session.");
                    }

                    Log.d(TAG, "Updated End Of Hour TextView.");
                    view.postDelayed(this, delayTime);//this will run this runnable (timerTask) after every delayTime (30,000) millisecond
                }
            };
            view.postDelayed(timerTask, initialDelayTime);

        } catch (IllegalStateException e){
            Log.e(TAG, "starting end of hour error");
        }
    }

    /**
     * Creates a new EndOfHourHandler for the specified TextView. The text in the TextView is
     * dynamically changed to reflect the current school period and when it ends. Updates the view
     * every ten seconds.
     * @param view - TextView to draw the current period and end time onto.
     * TODO: add support for late starts/weekends/holidays/etc..
     */
    public EndOfHourHandler(TextView view) {
        this.view = view;
        context = view.getContext();
    }

    private void setCurrentPeriodAndEndTime() {
        String currentTime = Utils.getCurrentTime("HH:mm");
        int currentHours = Integer.parseInt(currentTime.substring(0, 2));
        int currentMinutes = Integer.parseInt(currentTime.substring(3, 5));
        if (currentTime.contains("PM")) {
            currentHours += 12;
        }
        int lastMinutesEnd = 0;
        for (int i = 0; i < endOfHourTimes.length; i++) {
            String endHourTime = endOfHourTimes[i];
            int colLoc = endHourTime.indexOf(':');
            int hoursEnd = Integer.parseInt(endHourTime.substring(0, colLoc));
            int minutesEnd = Integer.parseInt(endHourTime.substring(colLoc + 1, colLoc + 3));
            if (endHourTime.contains("PM")) {
                hoursEnd += 12;
            }

            if((currentHours == hoursEnd && currentMinutes < minutesEnd) || (currentHours == hoursEnd - 1 && currentMinutes > lastMinutesEnd)) {
                currentPeriod = i;
                break;
            } else if(currentHours == hoursEnd && currentMinutes > minutesEnd) {
                currentPeriod = i + 1;
                break;
            }

            lastMinutesEnd = minutesEnd;
        }
    }

    /*
    Whether or not school is currently in session.
     */
    public boolean isSchoolInSession() {
        /* TODO: Account for the current date, as well. */
        String currentTime = Utils.getCurrentTime("HH:mm");
        //String currentDate = Utils.getCurrentDate("MM/dd/yy");

        int currentHours = Integer.parseInt(currentTime.substring(0, 2));
        int currentMinutes = Integer.parseInt(currentTime.substring(3, 5));

        int startHours = Integer.parseInt(endOfHourTimes[0].substring(0, 2));
        int endHours = Integer.parseInt(endOfHourTimes[8].substring(0, 2)) + 12;// we add 12 to convert the hours back to a 12 hour clock.

        int startMinutes = Integer.parseInt(endOfHourTimes[0].substring(3,5));
        int endMinutes = Integer.parseInt(endOfHourTimes[8].substring(3, 5));

        if(currentHours < endHours && currentHours > startHours ||
                (currentHours == startHours && currentMinutes >= startMinutes) ||
                (currentHours == endHours && currentMinutes <= endMinutes)) {
            return true;
        }
        return false;
    }

}
