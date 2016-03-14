package com.unit5app.tasks;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadFile extends AsyncTask<String, Void, Void> {
    private static String TAG = "FileDownloader";

    @Override
    protected Void doInBackground(String... strings) {
        String fileUrl = strings[0];   // http://unit5.org/directory/thepdf.pdf
        String fileName = strings[1];  // thepdf.pdf
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        File folder = new File(extStorageDirectory, "pdfCache");
        folder.mkdir();

        File pdfFile = new File(folder, fileName);
        Log.d(TAG, "Attempting to create file " + pdfFile.toString());

        try {
            pdfFile.createNewFile();
            pdfFile.setReadable(true);
            pdfFile.setWritable(true);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        FileDownloader.downloadFile(fileUrl, pdfFile);
        return null;
    }
}

/**
 * Sourced from http://stackoverflow.com/questions/24740228/android-download-pdf-from-url-then-open-it-with-a-pdf-reader
 */
class FileDownloader {
    private static final int MEGABYTE = 1024*1024;

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
}

