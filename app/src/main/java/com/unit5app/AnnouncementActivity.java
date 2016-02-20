package com.unit5app;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Andrew on 2/18/2016.
 *
 * If we want to parse the pdf instead of load it through a webView we can use this source: <a href="http://www.ehow.com/how_6582916_read-pdf-file-java.html">source</a>
 */
public class AnnouncementActivity extends Activity {

    private String pdfUrl = "http://www.unit5.org/cms/lib03/IL01905100/Centricity/Domain/1084/2%2012%2016.pdf"; //test url for now.

    private WebView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.announcements_layout);
        pdfView = (WebView) findViewById(R.id.announcements);

        pdfView.getSettings().setJavaScriptEnabled(true);
        pdfView.setWebViewClient(new Callback());
        pdfView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + pdfUrl);

    }

    public void setPdfUrl(String pdfUrl) { this.pdfUrl = pdfUrl; }

    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(
                WebView view, String url) {
            return(false);
        }
    }
}
