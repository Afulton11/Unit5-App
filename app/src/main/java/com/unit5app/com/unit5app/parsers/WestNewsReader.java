package com.unit5app.com.unit5app.parsers;

import android.text.Html;
import android.text.Spanned;
import android.util.Log;

import com.unit5app.Article;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrew
 * @version  2/16/16
 * This class reads the news from the west website by first checking out the links on the homepage.
 */
public class WestNewsReader extends RSSReader{

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36";

    private final String TAG = "WestNews";

    private List<String> htmlFreeLinks;

    private List<Article> articles;

    private volatile boolean doneRetrieving;

    /*
    @param url - the url of the normalwest homepage.
     */
    public WestNewsReader(String url) {
        super(url);
        htmlFreeLinks = new ArrayList<>();
        articles = new ArrayList<>();
    }

    /**
     * initializes the descriptions and titles for all the Articles in the news.
     * THIS MUST BE CALLED BEFORE calling any get() methods.
     */
    public void loadLinks() {
        doneRetrieving = false;
        Log.d(TAG, "Loading Links...");
        for(int i = 0; i < getLinks().size(); i++) {
            Article article = new Article();
            addLink(getLinks().get(i));
            if(getArticles().get(i).hasPubDate()) {
                article.setPubDate(getArticles().get(i).getPubDate());
            }
            Connection connection = Jsoup.connect(htmlFreeLinks.get(i)).userAgent(USER_AGENT); //JSoup is used to retrieve a websites html source.
            Log.d(TAG, "Connected to link : " + htmlFreeLinks.get(i));
            try {
                Document doc = connection.get();
                doc.normalise();

                Element article_element = doc.body().getElementById("module-content-1852"); //module-content-1852 is the id of a div that contains the article_element.

                String title = article_element.getElementsByTag("h1").get(0).toString();
                Log.d(TAG, title);
                article.setTitle(title);

                List<Element> element_paragraphs = article_element.getElementsByTag("span");
                element_paragraphs.remove(0); //gets rid of the 'Return to Headlines' span.
                element_paragraphs.remove(0); //gets rid of the title span.

                //removing the very last 3 spans as they are used for the author's information, since west seems to never give an author name/email/phone.
                element_paragraphs.remove(element_paragraphs.size() - 1);
                element_paragraphs.remove(element_paragraphs.size() - 1);
                element_paragraphs.remove(element_paragraphs.size() - 1);

                StringBuilder buffer_body = new StringBuilder();
                for (Element e : element_paragraphs) {
                    buffer_body.append(e.toString());
                }

                String fullBody = buffer_body.toString();
                article.setDescription(fullBody);

                if(article.isArticleFull()) articles.add(article);

            } catch (Exception e) {
                Log.d(TAG, "Unable to load link: " + htmlFreeLinks.get(i));
                e.printStackTrace();
            }
        }
        doneRetrieving = true;

        Log.d(TAG, "Loaded links!");
    }

    /*
    adds a link to the htmlFreeLinks list and gets rid of any html that might be part of the link.
     */
    private void addLink(String url) {
        Spanned resultLink = Html.fromHtml(url);
        htmlFreeLinks.add(resultLink.toString().trim());
    }

    /*
    returns all the htmlFreeLinks as a List - these links are free of html and therefore can be used to go to a website properly.
     */
    public List<String> getHtmlFreeLinksLinks() {
        return htmlFreeLinks;
    }

    /**
     * returns the body of all the articles from the westNewsReader's given url.
     * @return - the list of articles or a blank Article if the reader is not yet done retrieving.
     */
    public List<Article> getNewsArticles() {
        if(doneRetrieving && articles != null) {
            return articles;
        }
        List<Article> test = new ArrayList<>();
        test.add(new Article("Not Loading Properly...", "Unable to Load Article."));
        return test;
    }
}
