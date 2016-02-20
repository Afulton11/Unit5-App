package com.unit5app;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.unit5app.com.unit5app.parsers.CalendarEvent;
import com.unit5app.com.unit5app.parsers.RSSReader;

import java.util.List;

/**
 * Created by Andrew on 2/19/2016.
 */
public class UpcomingEventsActivity extends Activity {

    private TextView textView_calendarEvents;

    private RSSReader rssCalendarReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_layout);

        textView_calendarEvents = (TextView) findViewById(R.id.upcomingEvents_EventText);
        textView_calendarEvents.setText("loading calendar events...");
        textView_calendarEvents.setMovementMethod(new ScrollingMovementMethod());

        rssCalendarReader = new RSSReader("http://www.unit5.org/site/RSS.aspx?DomainID=4&ModuleInstanceID=1&PageID=2");
        rssCalendarReader.isCalendar = true;

        new ReadCalendarTask(rssCalendarReader, this).execute();

    }

    public void setTextView_calendarEvents(String string) {
        textView_calendarEvents.setText(string);
    }

    /**
     * reads the feed from the rssReader titled 'rssReader' and sets the article titles seperate items in list view
     */
    public static class ReadCalendarTask extends AsyncTask<Void, Void, Void> {

        private RSSReader reader;
        private UpcomingEventsActivity activity;

        public ReadCalendarTask(RSSReader reader, UpcomingEventsActivity activity) {
            this.reader = reader;
            this.activity = activity;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //when finished parsing do the following:
            List<CalendarEvent> events = reader.getCalendarEvents();

            StringBuffer html_styling_buffer = new StringBuffer();

            for(int i = 0; i < events.size(); i++) { //goes through every calendar event found in the xml from the calendar and adds a little bit of styling to the text before displaying it on the textview.
                CalendarEvent event = events.get(i);
                html_styling_buffer.append("<br><b>&#8226;" + event.getDate() +
                        "</b></br><br>&nbsp;&nbsp;&nbsp;&nbsp;Title: " + event.getTitle() +
                        "</br><br>&nbsp;&nbsp;&nbsp;&nbsp;Time Occuring: "+ event.getTimeOccurring() +
                        "</br><br>&nbsp;&nbsp;&nbsp;&nbsp;Type (debug): " + event.getType() + "</br><br></br>");
            }

            Spanned html_parsed = Html.fromHtml(html_styling_buffer.toString());
            activity.setTextView_calendarEvents(html_parsed.toString());
        }

        @Override
        protected Void doInBackground(Void... params) {
            reader.loadXml();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

}
