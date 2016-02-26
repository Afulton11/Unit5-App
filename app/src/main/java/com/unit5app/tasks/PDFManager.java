package com.unit5app.tasks;

import android.util.Log;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * "http://www.unit5.org/cms/lib03/IL01905100/Centricity/Domain/55/2016%20Feb%20Sr%20High%20Lunch.pdf"
 */
public abstract class PDFManager {
    private static String TAG = "PDFManager";
    private static String pdfAsString;

    public static String collectAndParsePdf(final String webUrl) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                PDDocument pdf = grabPdf(webUrl);
                String stringPdf = stripPdf(pdf);
                synchronized (pdfAsString) {
                    pdfAsString = parsePdf(stringPdf);
                    pdfAsString.notify();
                }
            }
        });
        thread.start();

        try {
            synchronized (pdfAsString) {
                pdfAsString.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return pdfAsString;
    }

    private static PDDocument grabPdf(String webUrl) {
        try {
            PDDocument pdf = new PDDocument();
            URL url = new URL(webUrl);
            Log.d(TAG, "Attempting to connect to PDF at URL:  " + webUrl);
            InputStream inputStream = url.openStream();
            Log.d(TAG, "Successfully connected to PDF at URL: " + webUrl);


            /* Store info into a PDDocument */
            pdf.load(inputStream);

            /* Close the input stream and free its resources. */
            pdf.close();
            inputStream.close();
            Log.d(TAG, "Document stream and input stream closed.");
            return pdf;
        }
        catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }

        Log.d(TAG, "WARNING: PDF returning as null.");
        return null;
    }

    private static String stripPdf(PDDocument pdf) {
        try {
            PDFTextStripper pdfTextStripper = new PDFTextStripper();

            int numPages = pdf.getNumberOfPages(); /* This number should be 1, anyways. */

            pdfTextStripper.setStartPage(1);
            pdfTextStripper.setEndPage(numPages);

            return pdfTextStripper.getText(pdf);
        }
        catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }

        Log.d(TAG, "WARNING: PDF as text returning null!");
        return null;
    }

    private static String parsePdf(String pdfAsString) {
        /* TODO: implement parsePdf to get needed text */
        return pdfAsString;
    }
}