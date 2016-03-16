package com.unit5app.activities;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;

import com.unit5app.Article;
import com.unit5app.R;
import com.unit5app.Settings;
import com.unit5app.utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * Created by Andrew on 2/11/2016.
 * The Activity class used to show an Article to the user.
 */
public class ArticleActivity extends BaseActivity {

    private static final String TAG = "ArticleActivity";

    private static Article article;

    private TextView Heading, Body;

    /**
     * sets the article of the ArticleActivity - the article that is displayed on the activity.
     *
     * @param set_article - sets the Article of the Activity
     */
    public static void setArticle(Article set_article) {
        article = set_article;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_layout);
        if(Settings.getArticleSettingsBoolean(Settings.ID_ARTICLE_SETTING_SCROLL_WITH_TITLE)) {
            ScrollView view = (ScrollView) findViewById(R.id.article_scrollView);
            view.setVerticalScrollBarEnabled(false);
        }
        Heading = (TextView) findViewById(R.id.article_title);
        Body = (TextView) findViewById(R.id.article_body);

        Heading.setText(Utils.toTitleCase(Html.fromHtml(article.getTitle()).toString()));
        if(!Settings.getArticleSettingsBoolean(Settings.ID_ARTICLE_SETTING_SCROLL_WITH_TITLE)) Heading.setMovementMethod(new ScrollingMovementMethod());
        //we need a new task/thread because android doesn't allow connecting to a network on the main thread.
        new AsyncTask<Object, Void, Void>() {
            Spanned resultBody;
            Drawable image;

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Body.setText(resultBody);
                Body.setMovementMethod(LinkMovementMethod.getInstance());
                if(!Settings.getArticleSettingsBoolean(Settings.ID_ARTICLE_SETTING_SCROLL_WITH_TITLE)) {
                    Body.setHorizontalScrollBarEnabled(true);
                    Body.setVerticalScrollBarEnabled(true);
                }
                Body.setClickable(true);
                Body.setLinksClickable(true);
                Body.setTextIsSelectable(true);

            }
            @Override
            protected Void doInBackground(Object... params) {
                String description = article.getDescription();
                /**
                 * Fix links..
                 */
                description = fixLinks(description);
                /**
                 * Done fixing links..
                 */



                resultBody = Html.fromHtml(description, new Html.ImageGetter() {
                    @Override
                    public Drawable getDrawable(String source) { //retrieve any images from unit5
                        String[] splitSource = source.split("/");
                        String sourceName = splitSource[splitSource.length - 1];
                        image = null;
                        try {
                            InputStream input = (InputStream) new URL("http://www.unit5.org" + source).getContent();
                            image = Drawable.createFromStream(input, sourceName);
                            image.setBounds(0, 0, image.getIntrinsicWidth() * 4, image.getIntrinsicHeight() * 4); //TODO: figure out why the image loads so small when not muliplied by 4?
                        } catch (MalformedURLException e) {
                            if (e.getMessage() != null) Log.e(TAG, e.getMessage(), e);
                            else
                                Log.e(TAG, "Exception", e);
                        } catch (IOException ioe) {
                            if (ioe.getMessage() != null) Log.e(TAG, ioe.getMessage(), ioe);
                            else
                                Log.e(TAG, "IOException", ioe);
                        }
                        Log.d(TAG, "Successfully loaded image!");
                        return image;
                    }
                }, null);
                return null;
            }
        }.execute();
    }

    /**
     * Attempts to fix links. It is mainly hyperlinks that are causing problems.
     * @param description - the description to fix the links for
     * @return a description with fixed links.
     */
    private String fixLinks(String description) {
        StringBuilder builder = new StringBuilder(description);
        int linkStartPos = builder.indexOf("<a href=\"");
        if(linkStartPos > 0) {
            Map<Integer, Integer> map_links = Utils.getOccurrencesWithIndexInString(description, "<a href=\"");
            final int linkStartLength = 8; //the length of <a href="
//            int linkEndQuote;
//            int linkEndArrow;
            for (Map.Entry<Integer, Integer> entry : map_links.entrySet()) {
                linkStartPos = entry.getValue();
//                linkEndQuote = builder.indexOf("\"", linkStartPos + linkStartLength + 1);
//                linkEndArrow = builder.indexOf(">", linkEndQuote);
//                Map<Integer, Integer> spaceLocs = Utils.getOccurrencesWithIndexInString(builder.substring(linkStartPos + linkStartLength, linkEndArrow), " ");
//                for (Map.Entry<Integer, Integer> spaceLoc : spaceLocs.entrySet()) {
//                    int sum = linkStartPos + linkStartLength + spaceLoc.getValue();
//                    if (builder.charAt(sum) == ' ') {
//                        builder.replace(sum, sum + 1, "%20");
//                    }
//                }
                int slashPos = linkStartPos + linkStartLength + 1;
                if (builder.charAt(slashPos) == '/') {
                    builder.replace(slashPos, slashPos + 1, "http://www.unit5.org/");
                    if(builder.toString().contains("//")) {
                        builder.replace(builder.indexOf("//"), builder.indexOf("//") + 1, "/");
                    }
                }

//                builder.replace(linkStartPos, linkStartPos + linkStartLength + 1, ""); //remove html tags
//                builder.replace(linkEndQuote, linkEndArrow, ""); //remove html tags
                description = builder.toString();
            }
        }
        return description;
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
