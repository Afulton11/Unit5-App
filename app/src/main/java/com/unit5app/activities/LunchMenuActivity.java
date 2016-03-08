package com.unit5app.activities;

import android.os.Bundle;
import android.webkit.WebView;

import com.sun.pdfview.PDFImage;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PDFPaint;
import com.unit5app.R;

import net.sf.andpdf.refs.HardReference;

/**
 * Created by z on 3/8/16.
 */
public class LunchMenuActivity extends BaseActivity {
    private static final String TAG = "LunchMenuPDFReader";
    private WebView webView;

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
    }
}
