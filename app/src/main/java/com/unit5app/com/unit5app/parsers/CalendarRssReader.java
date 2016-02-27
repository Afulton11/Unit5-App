package com.unit5app.com.unit5app.parsers;

import com.unit5app.calendars.CalendarEvent;
import com.unit5app.calendars.EventType;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrew
 * @version 2/24/16
 */
public class CalendarRssReader extends RSSReader{

    private static final String TAG = "CalendarRssReader";

    private List<CalendarEvent> calendarEvents;

    public CalendarRssReader(String calendarUrl) {
        super(calendarUrl);
        if(calendarUrl != null) {
            calendarEvents = new ArrayList<>();
        }
    }

    protected void parse(XmlPullParser parser) {
        try {
            int event = parser.getEventType();
            String text = null;

            while (event != XmlPullParser.END_DOCUMENT) {
                String name = parser.getName();

                switch (event) {

                    /* If <tag> content </tag> */
                    case XmlPullParser.TEXT:
                        text = parser.getText(); /* Capture it */
                        break;

                    case XmlPullParser.END_TAG:
                        /* If we got some text out of it */
                        if(text != null) { /* Use if-else instead of switch statements b/c some tags
                                              may be different depending on the rss you read. */
                            /* If is a title tag */
                            if(name.equals("title") && !text.equals("Calendar")) { // making sure the text != "Calendar" gets rid of the first title on a unit5 calendar which is used to name the calendar itself.
                                calendarEvents.add(new CalendarEvent(text));
                            }
                        }
                        break;
                }
                event = parser.next();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * returns all the calendarEvents found if the RssReader is retrieving from a calendar event.
     * If the reader is not done parsing it will return a blank calendar that has only one day on it.
     * @return list of CalendarEvents
     **/
    public List<CalendarEvent> getCalendarEvents() {
        if(doneParsing) {
            return calendarEvents;
        } else {
            List<CalendarEvent> blankList = new ArrayList<>();
            blankList.add(new CalendarEvent("", "", "", EventType.regular));
            return blankList;
        }
    }
}
