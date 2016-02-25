package com.unit5app.com.unit5app.parsers;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class PDFManager {
    private String TAG = "PDFManager";

    private PDDocument pdDocument;
    private String pdfUrl;
    private String pdfAsString;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                case 0:
                default:
                    break;
            }
        }
    };

    private class PDFWorkerTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                pdDocument = new PDDocument();
                URL url = new URL(pdfUrl);
                Log.d(TAG, "Attempting to connect to PDF at URL:  " + pdfUrl);
                InputStream inputStream = url.openStream();
                Log.d(TAG, "Successfully connected to PDF at URL: " + pdfUrl);


                /* Store info into a PDDocument */
                pdDocument.load(inputStream);

                /* Close the input stream and free its resources. */
                pdDocument.close();
                inputStream.close();
                Log.d(TAG, "Document stream and input stream closed.");
            }
            catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, e.getMessage());
            }

            return null;
        }

        protected void onPostExecute() {
            handler.sendEmptyMessage(1);
        }
    }

    class PDFStripperTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            PDFTextStripper pdfTextStripper;
            try {
                pdfTextStripper = new PDFTextStripper();
                int numPages = pdDocument.getNumberOfPages(); /* This number should be 1, anyways. */

                pdfTextStripper.setStartPage(1);
                pdfTextStripper.setEndPage(numPages);

                pdfAsString = pdfTextStripper.getText(pdDocument);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public PDFManager(String pdfUrl) {
        this.pdfUrl = pdfUrl;
        PDFGrabberTask grabberTask = new PDFGrabberTask();
        grabberTask.execute();

        PDFStripperTask stripperTask = new PDFStripperTask();
        stripperTask.execute();
    }
}
