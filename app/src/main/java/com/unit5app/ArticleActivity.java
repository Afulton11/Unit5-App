package com.unit5app;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Andrew on 2/11/2016.
 * The Activity class used to show an Article to the user.
 */
public class ArticleActivity extends Activity {

    private static final String TAG = "ArticleActivity";

    private static Article article;

    private TextView Heading, Body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_layout);

        Heading = (TextView) findViewById(R.id.article_title);
        Body = (TextView) findViewById(R.id.article_body);

        Heading.setText(toTitleCase(Html.fromHtml(article.getTitle()).toString()));
        Heading.setMovementMethod(new ScrollingMovementMethod());
        //we need a new task/thread because android doesn't allow connecting to a network on the main thread.
        AsyncTask loadBody = new AsyncTask<Object, Void, Void>() {
            Spanned resultBody;
            Drawable image;

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Body.setText(resultBody);
                Body.setMovementMethod(new ScrollingMovementMethod());
                Body.setHorizontalScrollBarEnabled(true);
                Body.setLinksClickable(true);
            }
            @Override
            protected Void doInBackground(Object... params) {
                resultBody = Html.fromHtml(article.getDescription(), new Html.ImageGetter() {
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
        };
        loadBody.execute();
    }

    /*
    Used mainly for titles, this method will make the first letter in every word in the string a Capital.
     */
    static String toTitleCase(String title) {
        StringBuilder sb = new StringBuilder();
        boolean spaceFound = true;
        for(char c : title.toCharArray()) {
            if(c == ' ') {
                spaceFound = true;
            } else if(spaceFound) {
                c = Character.toUpperCase(c);
                spaceFound = false;
            }
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * sets the article of the ArticleActivity - the article that is displayed on the activity.
     * @param set_article - sets the Article of the Activity
     */
    public static void setArticle(Article set_article) { article = set_article; }
}
