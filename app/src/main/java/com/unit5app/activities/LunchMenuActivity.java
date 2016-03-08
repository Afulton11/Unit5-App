package com.unit5app.activities;

import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.webkit.WebView;

import com.sun.pdfview.PDFImage;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PDFPaint;
import com.unit5app.R;

import net.sf.andpdf.refs.HardReference;

import java.io.File;
import java.net.URL;

/**
 * Created by z on 3/8/16.
 */
public class LunchMenuActivity extends BaseActivity {
    private static final String TAG = "LunchMenuPDFReader";
    private WebView webView;
    private int viewSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView

        /* PDF Display Settings */
        PDFImage.sShowImages = true; // Displays images
        PDFPaint.s_doAntiAlias = true; // Smooths text
        HardReference.sKeepCaches = true; // Saves images in cache

        webView = (WebView)findViewById(R.id.lunchmenu_pdf);
        webView.getSettings().setBuiltInZoomControls(true); // Enable zoom buttons
        webView.getSettings().setSupportZoom(true);     // Make zoom work

        webView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                viewSize = webView.getWidth();
                webView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

        try {
            //File file = new File(String.valueOf(new URL("http://www.unit5.org/cms/lib03/IL01905100/Centricity/Domain/55/2016%20Mar%20Sr%20High%20Lunch.pdf")));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
