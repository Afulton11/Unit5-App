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
 */
public class RSSReader {
    private String TAG = "Unit5Reader";

    private static List<String> titles, links, descriptions, pubDates;

    private XmlPullParserFactory xmlFactory;

    private String rssUrl;

    public volatile boolean doneParsing = false;

    /**
     * creates a new RSSReader from the specified Rss url address.
     */
    public RSSReader (String rssURL) {
        try {
            this.rssUrl = rssURL;
            titles = new ArrayList<>();
            links = new ArrayList<>();
            descriptions = new ArrayList<>();
            pubDates = new ArrayList<>();
            doneParsing = false;
        } catch (Exception e) {
            Log.e(TAG, e.getStackTrace().toString());
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

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }

    /*
     * parses the xml from the document such as <title></title> and <description></description>
     * This also calls parseHTML() when it completes parsing the xml.
     */
    public void parse(XmlPullParser myParser) {
        int event;
        String text=null;

        try {
            event = myParser.getEventType();

            while (event != XmlPullParser.END_DOCUMENT) {
                String name=myParser.getName();

                switch (event){
                    case XmlPullParser.START_TAG:
                        break;

                    case XmlPullParser.TEXT:
                        text = myParser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if(text != null) {
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
                        break;
                }

                event = myParser.next();
            }

            doneParsing = false;
        }

        catch (Exception e) {
            e.printStackTrace();
        }

        parseHTML();
    }

    /**
     * parses the html from the document after it has been parsed by the xml.
     * This includes html Entities like &nbsp; which is a
     */
    private void parseHTML() {
        doneParsing = true;
        Log.d(TAG, "Done Parsing!");
    }

    public List<String> getTitles() {
        while(!doneParsing) {
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


}
