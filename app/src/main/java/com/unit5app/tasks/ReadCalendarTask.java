package com.unit5app.tasks;

import android.os.AsyncTask;
import android.text.Html;

import com.unit5app.activities.UpcomingEventsActivity;
import com.unit5app.calendars.CalendarEvent;
import com.unit5app.com.unit5app.parsers.CalendarRssReader;
import com.unit5app.utils.Utils;

import java.util.List;

/**
 * reads the feed from the rssReader titled 'rssReader' and sets the article titles seperate items in list view
 **/
public class ReadCalendarTask extends AsyncTask<Void, Void, Void> {

    private CalendarRssReader reader;

    boolean loaded;


    public ReadCalendarTask(String CalendarRssUrl) {
        this.reader = new CalendarRssReader(CalendarRssUrl);
        loaded = false;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        String html_styling_string = "";
        for(int i = 0; i < reader.getCalendarEvents().size(); i++) { //goes through every calendar event found in the xml from the calendar and adds a little bit of styling to the text before displaying it on the textview.
            CalendarEvent event = reader.getCalendarEvents().get(i);
            html_styling_string += ("<br><b>&#8226;" + event.getDate() +
                    "</b></br><br>&nbsp;&nbsp;&nbsp;&nbsp;Title: " + event.getTitle() +
                    "</br><br>&nbsp;&nbsp;&nbsp;&nbsp;Time Occuring: "+ event.getTimeOccurring() +
                    "</br><br>&nbsp;&nbsp;&nbsp;&nbsp;Type (debug): " + event.getType() + "</br><br></br>");
        }
        UpcomingEventsActivity.calendarEventsString = Html.fromHtml(html_styling_string);

        loaded = true;
    }

    @Override
    protected Void doInBackground(Void... params) {
        reader.loadXml();
        Utils.unlockWaiter();
        return null;
    }

    public List<CalendarEvent> getCalendarEvents() {
        return reader.getCalendarEvents();
    }

    public boolean isLoaded() {
        return loaded;
    }
}
