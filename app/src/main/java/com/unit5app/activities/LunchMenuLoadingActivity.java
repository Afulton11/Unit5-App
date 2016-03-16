package com.unit5app.activities;

import android.os.Bundle;
import android.util.Log;

import com.unit5app.R;
import com.unit5app.tasks.DownloadPdfTask;
import com.unit5app.utils.Utils;

/**
 * Activity to display a PDF with the current lunch menu.
 */
public class LunchMenuLoadingActivity extends LoadingActivity {
    /* TODO: Generate the URL with JSoup so that it's not hardcoded */
    /* NOTE: If U5's naming scheme is consistent, next month's menu should be '2016 Apr Sr...' */
    private static final String TAG = "LunchMenuPDFReader";
    private static final String fileUrl = "http://www.unit5.org/cms/lib03/IL01905100/Centricity/" +
            "Domain/55/2016%20Mar%20Sr%20High%20Lunch.pdf";
    private static final String fileName = "03_menu.pdf"; // I'm thinking name convention should be
                                                          // mm_menu.pdf where mm is the month

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.view_loading);
        Utils.setCurrentView(Utils.VIEW_LOADING);
        Log.d(TAG, "Layout set. Starting PDF download....");

        new DownloadPdfTask(this).execute(fileUrl, fileName);
    }
}
