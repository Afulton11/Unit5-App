package com.unit5app.com.unit5app.parsers;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrew
 * @version 2/21/2016.
 * Reads, then parses XML grabbed from an RSS feed.
 */
public class RSSReader {
    /* TODO: Implementation that's not hardcoded to differentiate between Calendars and other XML */

    private String TAG = "Unit5Reader";

    private static List<String> titles, links, descriptions, pubDates;
    private List<CalendarEvent> calendarEvents;

    private XmlPullParserFactory xmlFactory;

    private String rssUrl;

    private boolean doneParsing;

    /*
     * whether or not the xml the reader is retrieving is from a calendar or not, true = it is a calendar.
     */
    public boolean isCalendar = false;

    public boolean doneParsing() {
        return doneParsing;
    }

    /**
     * creates a new RSSReader from the specified Rss url address, unless the url is null.
     */
    public RSSReader (String rssURL) {
        if(rssURL != null) {
            try {
                this.rssUrl = rssURL;
                titles = new ArrayList<>();
                links = new ArrayList<>();
                descriptions = new ArrayList<>();
                pubDates = new ArrayList<>();
                calendarEvents = new ArrayList<>();
                doneParsing = false;
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    /**
     * loads the xml onto this rssReader's titles and descriptions, and etc...
     */
    public void loadXml() {
        try {
            doneParsing = false;
            URL url = new URL(rssUrl);
            Log.d(TAG, "Requesting rss URL");
            InputStream stream = url.openStream();

            Log.d(TAG, "Connected to url!");

            xmlFactory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlParser = xmlFactory.newPullParser();

            xmlParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlParser.setInput(stream, null);

            parse(xmlParser);
            stream.close();
            doneParsing = true;

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }
    }

    /*
     * parses the xml from the document such as <title></title> and <description></description>
     * This also calls parseHTML() when it completes parsing the xml.
     */
    protected void parse(XmlPullParser myParser) {
        Log.d(TAG, "Starting parse of XML!");

        int event;
        String text=null;

        try {
            event = myParser.getEventType();

            /* While not at end of XML document */
            while (event != XmlPullParser.END_DOCUMENT) {
                String name = myParser.getName();

                switch (event) {
                    /* If <tag> */
                    case XmlPullParser.START_TAG:
                        break; /* Do nothing */

                    /* If <tag> content </tag> */
                    case XmlPullParser.TEXT:
                        text = myParser.getText(); /* Capture it */
                        break;

                    case XmlPullParser.END_TAG:
                        /* If we got some text out of it */
                        if(text != null) { /* Use if-else instead of switch statements b/c some tags
                                              may be different depending on the rss you read. */
                            if(isCalendar) { /* NOTE: not sure if instance variable best solution */
                                /* If is a title tag */
                                if(name.equals("title")) { // making sure the text != "Calendar" gets rid of the first titleon a unit5 calendar which is used to name the calendar itself.
                                    calendarEvents.add(new CalendarEvent(text));
                                }
                            } else {
                                if (name.equals("title")) {
                                    Log.d(TAG, text);
                                    titles.add(text);
                                } else if (name.equals("link")) {
                                    links.add(text);
                                } else if (name.equals("description")) {
                                    descriptions.add(text);
                                } else if (name.equals("pubDate")) {
                                    pubDates.add(text);
                                }
                            }
                        }
                        break;
                }
                event = myParser.next();
            }
            if(!isCalendar) {
                titles.remove(0); //remove the 'homepage' thing.. for unit5 websites
                links.remove(0); //removes the homepage link.. for unit5 websites
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void waitForDoneParsing() {
        /* DRY principle: If you have to say the same thing two times or more, wrap it
         * into its own method. */
        while(!doneParsing) {
            try {
                this.wait(100);
            } catch (Exception e) {
                // Intentionally blank!
            }
        }
    }

    public List<String> getTitles() {
        waitForDoneParsing();
        return titles;
    }

    public List<String> getDescriptions() {
        waitForDoneParsing();
        return descriptions;
    }

    public List<String> getLinks() {
        waitForDoneParsing();
        return links;
    }

    /*
    returns the dates of which articles/items were published.
     */
    public List<String> getPubDates() {
        waitForDoneParsing();
        return pubDates;
    }

    /*
    returns all the calendarEvents found if the RssReader is retrieving from a calendar event.
    If the reader is not done parsing, or is not a calendar, it will return a blank calendar that has only one day on it.
     */
    public List<CalendarEvent> getCalendarEvents() {
        if(doneParsing && isCalendar) {
            calendarEvents.remove(0); //remove the calendar's title, the title for the calendar itself.
            return calendarEvents;
        } else {
            List<CalendarEvent> blankList = new ArrayList<>();
            blankList.add(new CalendarEvent("", "", "", EventType.regular));
            return blankList;
        }
    }

    public boolean isDoneParsing() {
        return doneParsing;
    }


}
