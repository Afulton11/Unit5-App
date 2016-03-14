package com.unit5app.activities;

import android.os.Bundle;
import android.util.Log;

import com.unit5app.R;
import com.unit5app.tasks.DownloadPDF;

/**
 * Activity to display the current lunch menu.
 */
public class LunchMenuActivity extends BaseActivity {
    /* TODO: Generate the URL with JSoup */
    private static final String TAG = "LunchMenuPDFReader";
    private static final String fileUrl = "http://www.unit5.org/cms/lib03/IL01905100/Centricity/Domain/55/2016%20Mar%20Sr%20High%20Lunch.pdf";
    private static final String fileName = "menu.pdf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.lunchmenu_layout);
        Log.d(TAG, "Layout set. Starting PDF download....");

        new DownloadPDF(this).execute(fileUrl, fileName);
    }
}
