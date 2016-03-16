package com.unit5app.tasks;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.unit5app.activities.ShowLunchMenuActivity;

import java.io.File;

/**
 * Task to download and display PDF documents.
 */
public class DownloadPdfTask extends DownloadFileTask {
    private static final String TAG = "DownloadPdfTask";

    /**
     * Makes a new DownloadPdfTask. Call taskName.execute(String url, String fileName) to start it.
     * @param activity the Task is running for/running in. Needed to get context for file saving and
     *                 for modifying the UI.
     */
    public DownloadPdfTask(Activity activity) {
        super(activity);
    }

    @Override
    protected void onPostExecute(File result) {
        super.onPostExecute(result);
        Log.d(TAG, "PDF received, displaying....");

        ShowLunchMenuActivity.setResult(result);
        activity.startActivity(new Intent(activity.getApplicationContext(), ShowLunchMenuActivity.class));
        activity.finish();
    }
}

