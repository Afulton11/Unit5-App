package com.unit5app.com.unit5app.parsers;

import android.util.Log;

import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.InputStream;
import java.io.IOException;
import java.net.URL;

/**
 * Task to handle the loading and parsing of PDFs from the internet.
 * @version 2/24/16
 */
public abstract class PDFReader {
    private static String TAG = "PDFReader";

    public static PDDocument pullPDF(final String webUrl) {
        final PDDocument pdf = new PDDocument();
        Runnable documentGetter = new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(webUrl);
                    Log.d(TAG, "Attempting to connect to PDF at URL: \t" + webUrl);
                    InputStream inputStream = url.openStream();
                    Log.d(TAG, "Successfully connected to PDF at URL: \t" + webUrl);

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
        };

        return pdf;
    }

}