package com.unit5app.com.unit5app.parsers;

import android.util.Log;

import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Class to grab PDF documents from the Web and convert them into PDDocument objects.
 */
public class PDFGrabberTask implements Runnable {
    private PDDocument pdf;
    private String pdfUrl;
    private String TAG = "PDFGrabberTask";

    @Override
    public void run() {
        try {
            URL url = new URL(pdfUrl);
            Log.d(TAG, "Attempting to connect to PDF at URL: \t" + pdfUrl);
            InputStream inputStream = url.openStream();
            Log.d(TAG, "Successfully connected to PDF at URL: \t" + pdfUrl);

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
        pdf = new PDDocument();
    }

    public PDDocument getPdf() {
        return pdf;
    }
}
