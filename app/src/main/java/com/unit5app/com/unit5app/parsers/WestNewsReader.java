package com.unit5app.com.unit5app.parsers;

import android.text.Html;
import android.text.Spanned;
import android.util.Log;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew on 2/16/2016.
 * This class reads the news from the west website by first checking out the links on the homepage.
 */
public class WestNewsReader extends RSSReader{

    private String TAG = "WestNews";

    private List<String> htmlFreeLinks;

    private List<String> descriptions, titles;

    private boolean doneRetrieving = false;

    /*
    @param url - the url of the normalwest homepage.
     */
    public WestNewsReader(String url) {
        super(url);
        Log.d(TAG, "West started!");
        htmlFreeLinks = new ArrayList<>();
    }

    /**
     * initializes the descriptions and titles for all the Articles in the news.
     * THIS MUST BE CALLED BEFORE calling any get() methods.
     */
    public void loadLinks() {
        doneRetrieving = false;
        descriptions = new ArrayList<>();
        titles = new ArrayList<>();
        Log.d(TAG, "Loading Links...");
        for(int i = 0; i < getLinks().size(); i++) {
            addLink(getLinks().get(i));
            Connection connection = Jsoup.connect(htmlFreeLinks.get(i)); //JSoup is used to retrieve a websites html source.
            Log.d(TAG, "Connected to link : " + htmlFreeLinks.get(i));
            try {
                Document doc = connection.get();
                doc.normalise();
                Element Article = doc.body().getElementById("module-content-1852"); //module-content-1852 is the id of a div that contains the article.

                String title = Article.getElementsByTag("h1").get(0).toString();
                Log.d(TAG, title);
                titles.add(title);

                List<Element> element_paragraphs = Article.getElementsByTag("span");
                element_paragraphs.remove(0); //gets rid of the 'Return to Headlines' span.
                element_paragraphs.remove(0); //gets rid of the title span.

                //removing the very last 3 spans as they are used for the author's information, However: west seems to never give an author name/email/phone.
                element_paragraphs.remove(element_paragraphs.size() - 1);
                element_paragraphs.remove(element_paragraphs.size() - 2);
                element_paragraphs.remove(element_paragraphs.size() - 3);

                StringBuffer buffer_body = new StringBuffer();
                for (Element e : element_paragraphs) {
                    buffer_body.append(e.toString());
                }

                String fullBody = buffer_body.toString();
                Log.d(TAG, fullBody);
                descriptions.add(fullBody);
            } catch (Exception e) {
                Log.d(TAG, "Unable to load link: " + htmlFreeLinks.get(i));
            }
        }
        doneRetrieving = true;
        Log.d(TAG, "Loaded links successfully!");
    }

    /*
    adds a link to the htmlFreeLinks list and gets rid of any html that might be part of the link.
     */
    private void addLink(String url) {
        Spanned resultLink = Html.fromHtml(url);
        htmlFreeLinks.add(resultLink.toString());
    }

    /*
    returns all the htmlFreeLinks as a List - these links are free of html and therefore can be used to go to a website properly.
     */
    public List<String> getHtmlFreeLinksLinks() {
        return htmlFreeLinks;
    }

    /**
     * returns the title of the given index in the htmlFreeLinks array.
     * -------This doesn't return an html free title OR a title in correct title case!
     * @param index
     * @return
     */



    /**
     * returns the body of the article from the given index in the htmlFreeLinks array.
     * -----This doesn't return an htmlFree version of the body.
     * @param index
     * @return
     */



//    /**
//     * returns al the titles of all the articles from the given url in the westNewsReader.
//     * -------This doesn't return an html free title OR a title in correct title case!
//     * @return
//     */
//    public List<String> getTitles() {
//        return titles;
//    }

    /**
     * returns the body of all the articles from the westNewsReader's given url.
     * -----This doesn't return an htmlFree version of the body.
     * @return
     */
    public List<String> getDescriptions() {
        if(doneRetrieving) {
            return descriptions;
        }
        List<String> test = new ArrayList<>();
        test.add("Not loading properly....");
        test.add("NotLoadin THE THINGS.");
        return test;
    }
}
