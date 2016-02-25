package com.unit5app.com.unit5app.parsers;

import android.util.Log;

import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Class to grab PDF documents from the Web and convert them into PDDocument objects.
 */
public class PDFGrabber {
    private PDDocument pdf;
    private String TAG = "PDFGrabber";

    /**
     * Retrieves a PDF from the Web as a PDDocument.
     * @param pdfUrl - the location of the PDF on the Internet.
     * @return the PDF specified by the URL passed.
     */
    public PDDocument fetchPDF(final String pdfUrl) {
        Thread grabber = new Thread() {
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
                    pdf.close();
                    inputStream.close();
                    Log.d(TAG, "Document stream and input stream closed.");
                }
                catch (IOException e) {
                    e.printStackTrace();
                    Log.d(TAG, e.getMessage());
                }
            }
        };
        grabber.start();

        return pdf;
    }
}
