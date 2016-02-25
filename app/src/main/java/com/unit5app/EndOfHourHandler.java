package com.unit5app;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.widget.TextView;

import com.unit5app.utils.Time;
import com.unit5app.utils.Utils;

/**
 * @author Andrew
 * @version 2/21/2016.
 * TODO: Integrate with internal calendar as opposed to checking the internet again.
 */
public class EndOfHourHandler {

    private static final String TAG = "EndOfHourHandler";

    private TextView view;
    private Context context;

    private Period[] periods = new Period[] {
            new Period(0, "07:10 AM"),  new Period(1, "08:05 AM"), new Period(2, "09:00 AM"),  new Period(3, "09:55 AM"), new Period(4, "10:50 AM"),
            new Period(5, "11:45 AM"), new Period(6, "12:40 PM"),  new Period(7, "01:35 PM"), new Period(8, "02:30 PM")
    };

    /* Index representing the current period and its ending time */
    private int currentPeriod = 0;

    public void start() {
        final long initialDelayTime = 0; //the time to wait before starting the entire loop that uses delayTime.
        final long delayTime = 10000; //the amount of time to wait before checking the time again in milliseconds. 1000 ms = 1 second.
        final String startBufferText = "# hour ends at [TIME-HERE].";
        try {
            final Runnable timerTask = new Runnable() { //used to check the time and update the textView every 30 seconds.
                StringBuffer buffer;
                @Override
                public void run() {
                    if(!Utils.isAppPaused()) {
                        if (isSchoolInSession()) { //we make sure the calendar reader is done parsing because if it isn't, we may get some null pointer exceptions when checking things about today's date.
                            buffer = new StringBuffer(startBufferText);
                            setCurrentPeriodAndEndTime();
                            Period current = periods[currentPeriod];

                            int periodLoc = buffer.indexOf("#");
                            buffer.replace(0, periodLoc + 1, current.getPeriod());

                            int hourLoc = buffer.indexOf("[TIME-HERE]");
                            String s_currentEndTime = current.getEndOfPeriod();
                            buffer.delete(hourLoc, hourLoc + 10); //removes the colon as it is not needed anymore.
                            buffer.replace(hourLoc, hourLoc + 1, s_currentEndTime);

                            if (s_currentEndTime.toCharArray()[0] == '0') //removes the 0 from the start of the end time, if the first character is a 0.
                                buffer.delete(hourLoc, hourLoc + 1);

                            Spanned formatted = Html.fromHtml(buffer.toString());

                            view.setText(formatted); //sets the text of the textView after doing any html formatting to the text.

                            /**
                             * If the calendar hasn't finished parsing, or isn't being parsed, and the user reconnects to the internet, this will
                             * reload the calendar so that everything will work with the information provided by the calendar.
                             */
                            if (!Utils.hadInternetOnLastCheck && Utils.isInternetConnected(context) && !MainActivity.mainCalendar.hasCalendarStartedLoading()) {
                                MainActivity.mainCalendar.loadCalendar();
                            }
                            Log.d(TAG, "Updated End Of Hour TextView.");
                        } else {
                            view.setText("School is not currently in session.");
                        }
                    }
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
        String currentTime = Time.getCurrentTime("HH:mm");
        int currentHours = Integer.parseInt(currentTime.substring(0, 2));
        int currentMinutes = Integer.parseInt(currentTime.substring(3, 5));
        if (currentTime.contains("PM")) {
            currentHours += 12;
        }
        int lastMinutesEnd = 0;
        for (int i = 0; i < periods.length; i++) {
            String endHourTime = periods[i].getEndOfPeriod();
            int colLoc = endHourTime.indexOf(':');
            int hoursEnd = Integer.parseInt(endHourTime.substring(0, colLoc));
            int minutesEnd = Integer.parseInt(endHourTime.substring(colLoc + 1, colLoc + 3));
            if (endHourTime.contains("PM") && hoursEnd != 12) {
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
        String currentTime = Time.getCurrentTime("HH:mm");
//        String currentDate = Utils.getCurrentDate("MM/dd/yy");

        if(!Time.isWeekend()) {
            int currentHours = Integer.parseInt(currentTime.substring(0, 2));
            int currentMinutes = Integer.parseInt(currentTime.substring(3, 5));

            String endStartingPeriod = periods[0].getEndOfPeriod();
            String endLastPeriod = periods[8].getEndOfPeriod();

            int startHours = Integer.parseInt(endStartingPeriod.substring(0, 2));
            int endHours = Integer.parseInt(endLastPeriod.substring(0, 2)) + 12;// we add 12 to convert the hours back to a 12 hour clock.

            int startMinutes = Integer.parseInt(endStartingPeriod.substring(3, 5));
            int endMinutes = Integer.parseInt(endLastPeriod.substring(3, 5));

            if (currentHours < endHours && currentHours > startHours ||
                    (currentHours == startHours && currentMinutes >= startMinutes) ||
                    (currentHours == endHours && currentMinutes <= endMinutes)) {
                return true;
            }
        }
        return false;
    }

}
