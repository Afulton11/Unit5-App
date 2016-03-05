package com.unit5app.activities;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.unit5app.R;
import com.unit5app.utils.Utils;

/**
 * Created by Andrew on 2/18/2016.
 *
 * If we want to parse the pdf instead of load it through a webView we can use this source: <a href="http://www.ehow.com/how_6582916_read-pdf-file-java.html">source</a>
 */
public class AnnouncementActivity extends BaseActivity {

    private String pdfUrl = "http://www.unit5.org/cms/lib03/IL01905100/Centricity/Domain/55/2016%20Mar%20Sr%20High%20Lunch.pdf"; //changed to the lunch menu because I want to know what is for lunch before lunch
    private WebView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.announcements_layout);
        Utils.setCurrentView(Utils.VIEW_ANNOUNCEMENTS);

        pdfView = (WebView) findViewById(R.id.announcements);

        if(Utils.isInternetConnected(this)) {
            pdfView.getSettings().setJavaScriptEnabled(true);
            pdfView.setWebViewClient(new Callback());
            pdfView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + pdfUrl);
        }
    }

    public void setPdfUrl(String pdfUrl) { this.pdfUrl = pdfUrl; }

    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(
                WebView view, String url) {
            return(false);
        }
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
