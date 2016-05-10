package com.unit5app.tasks;

import android.app.Activity;
import android.util.Log;

import com.joanzapata.pdfview.PDFView;
import com.unit5app.R;

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
        activity.setContentView(R.layout.pdfviewer_layout);

        PDFView pdfView = (PDFView) activity.findViewById(R.id.pdfview);
        if(result != null) {
            try {
                pdfView.fromFile(result)
                        .defaultPage(1)
                        .showMinimap(false)
                        .enableSwipe(false)
                        .load();
            } catch (RuntimeException e) {
                Log.d(TAG, "PDF is Corrupted! - DownloadPdfTask.java");
            }
        }
    }
}

