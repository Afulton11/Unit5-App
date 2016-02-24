package com.unit5app.com.unit5app.parsers;

import android.util.Log;

import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Class to grab PDF documents from the Web and convert them into PDDocument objects.
 */
public class PDFGrabberTask extends Thread {
    private PDDocument pdf;
    private String pdfUrl;
    private String TAG = "PDFGrabberTask";

    @Override
    public void run() {
        try {
            pdf = new PDDocument();
            URL url = new URL(pdfUrl);
            Log.d(TAG, "Attempting to connect to PDF at URL:  " + pdfUrl);
            InputStream inputStream = url.openStream();
            Log.d(TAG, "Successfully connected to PDF at URL: " + pdfUrl);

            /* Store info into a PDDocument */
            pdf.load(inputStream);

            /* Close the input stream and free its resources. */
            inputStream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }
    }

    public PDFGrabberTask(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public PDDocument getPdf() {
        while (pdf == null) {
            Log.d(TAG, "Waiting for PDF to not be null.");
        }

        return pdf;
    }
}
