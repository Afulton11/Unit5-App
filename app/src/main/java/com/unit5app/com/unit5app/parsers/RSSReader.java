package com.unit5app.com.unit5app.parsers;

import android.util.Log;

import com.unit5app.Article;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
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

    private List<Article> articles;
    /*
    All the links in the given xml url.
     */
    private List<String> links;
    private List<CalendarEvent> calendarEvents;

    private String rssUrl;

    private volatile boolean doneParsing;

    /*
     * whether or not the xml the reader is retrieving is from a calendar or not, true = it is a calendar.
     */
    public boolean isCalendar = false;

    /**
     * creates a new RSSReader from the specified Rss url address, unless the url is null.
     */
    public RSSReader (String rssURL) {
        if(rssURL != null) {
            try {
                this.rssUrl = rssURL;
                articles = new ArrayList<>();
                links = new ArrayList<>();
                calendarEvents = new ArrayList<>();
                doneParsing = false;
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    /**
     * loads the xml onto this rssReader's articles, links, etc...
     */
    public void loadXml() {
        try {
            doneParsing = false;
            articles.clear();
            links.clear();
            calendarEvents.clear();
            URL url = new URL(rssUrl);
            Log.d(TAG, "Requesting URL stream!");
            InputStream stream = url.openStream();
            Log.d(TAG, "Connected to url stream!");


            XmlPullParserFactory xmlFactory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlParser = xmlFactory.newPullParser();

            xmlParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlParser.setInput(stream, null);

            parse(xmlParser);
            stream.close();
            doneParsing = true;
            Log.d(TAG, "done parsing XML!");

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }
    }

    /*
     * parses the xml from the document such as <title></title> and <description></description>
     * This also calls parseHTML() when it completes parsing the xml.
     * @param XmlPullParser - the parser that is streaming xml.
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
                        if(name.equals("item") && !isCalendar) {
                            Log.d(TAG, "Item found!");
                            Article articleToAdd = readArticle(myParser);
                            if (articleToAdd.isArticleFull()) articles.add(articleToAdd);
                            Log.d(TAG, "SIZE: " + articles.size());
                        }
                        break;

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
                                if(name.equals("title") && !text.equals("Calendar")) { // making sure the text != "Calendar" gets rid of the first title on a unit5 calendar which is used to name the calendar itself.
                                    calendarEvents.add(new CalendarEvent(text));
                                }
                            }
                        }
                        break;
                }
                event = myParser.next();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * reads an Article, or item tag, from the parser.
     * @param parser - the XmlPullParser that is reading the xml.
     * @return an Article from the xmlPullParser
     * @throws IOException
     * @throws XmlPullParserException
     */
    private Article readArticle(XmlPullParser parser) throws IOException, XmlPullParserException {
        Article currentArticle = new Article();
        parser.require(XmlPullParser.START_TAG, null, "item");
        while(parser.next() != XmlPullParser.END_TAG) {
            if(parser.getEventType() != XmlPullParser.START_TAG) continue;

            String name = parser.getName();
            Log.d(TAG, name);
            if(name.equals("title")){
                currentArticle.setTitle(readText(parser));
                Log.d(TAG, currentArticle.getTitle());
            } else if(name.equals("description")) {
                currentArticle.setDescription(readDescription(parser));
                Log.d(TAG, currentArticle.getDescription());
            } else if(name.equals("pubDate")) {
                currentArticle.setPubDate(readPubDate(parser));
                Log.d(TAG, currentArticle.getPubDate());
            } else if(name.equals("link")) {
                links.add(readText(parser));
            } else {
                skip(parser);
            }
        }

        return currentArticle;
    }

    /**
     * reads the title tag in the parser.
     * @param parser - the XmlPullParser that is reading the xml.
     * @return text of title tag.
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "title");
        return title;
    }

    /**
     * reads the pubDate tag from the parser.
     * @param parser - the XmlPullParser that is reading the xml.
     * @return - pubDate tag text
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String readPubDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "pubDate");
        String pubDate = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "pubDate");
        return pubDate;
    }

    /**
     * reads the description tag from the xmlPullParser
     * @param parser - the XmlPullParser that is reading the xml.
     * @return - text inside the description.
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "description");
        String description = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "description");
        return description;
    }

    /**
     * reads the text inside the current tag the parser is reading.
     * @param parser - the XmlPullParser that is reading the xml.
     * @return - text inside the current tag
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    /**
     * Skips the tag the xmlPullParser is currently on.
     * @param parser - the XmlPullParser that is reading the xml.
     * @throws XmlPullParserException
     * @throws IOException
     */
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    /**
     * returns a list of articles that the reader has retrieved from the inputted xml url of the reader.
     * @return - a list of articles
     */
    public List<Article> getArticles() {
        return articles;
    }

    /**
     * returns a list of all the article pub dates found while the reader read the xml.
     * @return - List(String) dates --> the date each article was published.
     */
    public List<String> getArticlePubDates() {
        List<String> dates = new ArrayList<>();
        for (Article article : articles) {
            if(article.hasPubDate()) dates.add(article.getPubDate());
        }
        return dates;
    }

    /**
     * returns a list of all the links found in the given reader xml url.
     * @return - a list of Strings that contain unparsed html links.
     */
    public List<String> getLinks() {
        return links;
    }

    /*
    returns all the calendarEvents found if the RssReader is retrieving from a calendar event.
    If the reader is not done parsing, or is not a calendar, it will return a blank calendar that has only one day on it.
     */
    public List<CalendarEvent> getCalendarEvents() {
        if(doneParsing && isCalendar) {
            return calendarEvents;
        } else {
            List<CalendarEvent> blankList = new ArrayList<>();
            blankList.add(new CalendarEvent("", "", "", EventType.regular));
            return blankList;
        }
    }

    public boolean doneParsing() {
        return doneParsing;
    }


}
