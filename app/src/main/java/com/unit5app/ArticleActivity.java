package com.unit5app;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
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
            Spanned resultTitle = Html.fromHtml(title);
            Log.d(TAG, resultTitle.toString());
            Heading.setText(resultTitle.toString());
        }
        if(body != null) {
            Spanned resultBody = Html.fromHtml(body);
            Body.setText(resultBody.toString());
        }
    }

    public static void setTitle(String setTitle) { title = setTitle; }
    public static void setBody(String setBody) {body = setBody; }
}
