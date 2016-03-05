package com.unit5app.activities;

import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

import com.unit5app.R;
import com.unit5app.com.unit5app.parsers.CalendarRssReader;
import com.unit5app.utils.Utils;

/**
 * @author Andrew
 * @version 2/19/16
 */
public class UpcomingEventsActivity extends BaseActivity {

    private TextView textView_calendarEvents;

    public static CalendarRssReader rssCalendarReader;

    public static Spanned calendarEventsString = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_layout);

        textView_calendarEvents = (TextView) findViewById(R.id.upcomingEvents_EventText);
        textView_calendarEvents.setMovementMethod(new ScrollingMovementMethod());

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(!MainActivity.mainCalendar.hasCalendarStartedLoading()) {
                    Log.d("Upcoming", "start loading? : " + MainActivity.mainCalendar.hasCalendarStartedLoading());
                    if(Utils.isInternetConnected(getApplicationContext())) {
                        MainActivity.mainCalendar.loadCalendar();
                    } else {
                        calendarEventsString = Html.fromHtml("<b>Unable to load</b> Upcoming Events while user is <b>not connected to the Internet</b>.");
                    }
                }
                textView_calendarEvents.setText("loading calendar events...");
                if(calendarEventsString != null)
                    textView_calendarEvents.setText(calendarEventsString);
                else
                    textView_calendarEvents.setText("Error Loading Upcoming Events.");
            }
        };

        textView_calendarEvents.post(runnable);

    }

    public void setTextView_calendarEvents(String string) {
        textView_calendarEvents.setText(string);
    }

    @Override
    public void onPause() {
        super.onPause();
        Utils.universalOnPause(getApplicationContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        Utils.universalOnResume(getApplicationContext());
    }
}
