package com.unit5app.activities;

import android.os.Bundle;
import android.util.Log;

import com.unit5app.R;
import com.unit5app.tasks.DownloadPdfTask;
import com.unit5app.utils.Time;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Activity to display a PDF with the current lunch menu.
 */
public class LunchMenuActivity extends BaseActivity {
    private static final String TAG = "LunchMenuPDFReader";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_loading);
        SimpleDateFormat format = new SimpleDateFormat("M");
        Date month = new Date();
        format.format(month);
        Log.d(TAG, "Layout set. Starting PDF download....");
        String fileUrl = "http://www.unit5.org/cms/lib03/IL01905100/Centricity/" +
                "Domain/55/2016%20" +  month.toString().split(" ")[1] +"%20Sr%20High%20Lunch.pdf";
        String fileName = Time.getCurrentDate(Time.FORMAT_BASIC_DATE).split("/")[0] + "_menu.pdf";

        new DownloadPdfTask(this).execute(fileUrl, fileName);
    }
}
