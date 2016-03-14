package com.unit5app.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class DownloadFile extends AsyncTask<String, Void, Void> {
    private static String TAG = "FileDownloader";
    private static final int MEGABYTE = 1024 * 1024;

    @Override
    protected Void doInBackground(String... strings) {
        String fileUrl = strings[0];   // http://unit5.org/directory/thepdf.pdf
        String fileName = strings[1];  // myPDFName.pdf
        String storageDir = Environment.getExternalStorageDirectory().toString() + "/Unit5-App/";
        storageDir += fileName;

        URL u;
        try {
            u = new URL(fileUrl);
            URLConnection connection = u.openConnection();
            connection.connect();

            InputStream input = new BufferedInputStream(u.openStream(), MEGABYTE);
            OutputStream output = new FileOutputStream(storageDir);

            byte[] buffer = new byte[MEGABYTE];
            int length;

            while((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            // Flush output
            output.flush();

            // Close streams
            output.close();
            input.close();
        }
        catch(MalformedURLException e) {
            e.printStackTrace();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

/**
 * Sourced from http://stackoverflow.com/questions/24740228/android-download-pdf-from-url-then-open-it-with-a-pdf-reader
 */
/*class FileDownloader {
    public static void downloadFile(String fileUrl, File directory) {
        try {
            URL url = new URL(fileUrl);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(directory);
            int totalSize = urlConnection.getContentLength();

            byte[] buffer = new byte[MEGABYTE];
            int bufferLength = 0;
            while((bufferLength = inputStream.read(buffer))>0 ){
                fileOutputStream.write(buffer, 0, bufferLength);
            }
            fileOutputStream.close();
        }
        catch (FileNotFoundException | MalformedURLException e) {
            e.toString();
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
} */

