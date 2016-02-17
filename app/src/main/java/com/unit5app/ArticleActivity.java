package com.unit5app;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

/**
 * Created by Andrew on 2/11/2016.
 * The Activity class used to show an Article to the user.
 */
public class ArticleActivity extends Activity {

    private static final String TAG = "ArticleActivity";

    private static String title = "";
    private static String body = "";

    private TextView Heading, Body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_layout);

        Heading = (TextView) findViewById(R.id.article_title);
        Body = (TextView) findViewById(R.id.article_body);

        if(title != null) {
//            Spanned resultTitle = Html.fromHtml(title);
//            Log.d(TAG, upperCaseFirstLetter(resultTitle.toString()));
            Heading.setText(title);
            Heading.setMovementMethod(new ScrollingMovementMethod());
        }
        if(body != null) {
            Spanned resultBody = Html.fromHtml(body);
            Body.setText(resultBody.toString());
            Body.setMovementMethod(new ScrollingMovementMethod());
        }
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

    public static void setTitle(String setTitle) { title = setTitle; }
    public static void setBody(String setBody) {body = setBody; }
}
