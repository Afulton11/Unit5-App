package com.unit5app.com.unit5app.parsers;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew on 2/11/2016.
 * Reads the Xml from an rss.
 */
public class RSSReader {

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

            Log.d(TAG, "Starting parse of XML!");
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
        int event;
        String text=null;

        try {
            event = myParser.getEventType();

            while (event != XmlPullParser.END_DOCUMENT) {
                String name = myParser.getName();

                switch (event){

                    case XmlPullParser.START_TAG:
                        break;

                    case XmlPullParser.TEXT:
                        text = myParser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if(text != null) { //i decided to use if else instead of switch statements b/c some tagsmay be different depending on the rss your reading.
                            if(isCalendar) {
                                if(name.equals("title")) { //making sure the text != "Calendar" gets rid of the first titleon a unit5 calendar which is used to name the calendar itself.
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

    public List<String> getTitles() {
        while(!doneParsing) { //it should be done parsing by the time these get methods are called, but just in case.
            try {
                this.wait(100);
            } catch (Exception e) {

            }
        }
        return titles;
    }

    public List<String> getDescriptions() {
        while(!doneParsing) {
            try {
                this.wait(100);
            } catch (Exception e) {

            }
        }
        return descriptions;
    }

    public List<String> getLinks() {
        while(!doneParsing) {
            try {
                this.wait(100);
            } catch (Exception e) {

            }
        }
        return links;
    }

    /*
    returns the dates of which articles/items were published.
     */
    public List<String> getPubDates() {
        while(!doneParsing) {
            try {
                this.wait(100);
            } catch (Exception e) {

            }
        }
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
