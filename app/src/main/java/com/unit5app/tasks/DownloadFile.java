package com.unit5app.tasks;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class DownloadFile extends AsyncTask<String, Void, File> {
    private static String TAG = "FileDownloader";
    private static final int MEGABYTE = 1024 * 1024;
    private Context context;

    public DownloadFile(Context context) {
        this.context = context;
    }

    @Override
    protected void onPostExecute(File result) {
        super.onPostExecute(result);

        PackageManager packageManager = context.getPackageManager();

        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setType("application/pdf");

        List activities = packageManager.queryIntentActivities(pdfIntent, PackageManager.MATCH_DEFAULT_ONLY);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(Uri.fromFile(result), "application/pdf");

        try {
            context.startActivity(intent);
        }
        catch(ActivityNotFoundException e) {
            Toast.makeText(context, "No activity found to display PDF", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected File doInBackground(String... strings) {
        String fileUrl = strings[0];   // http://unit5.org/directory/thepdf.pdf
        String fileName = strings[1];  // myPDFName.pdf
        File pdf = new File(context.getFilesDir(), fileName);

        try {
            pdf.createNewFile();
            Log.d(TAG, "Successfully created " + pdf.getAbsolutePath());
        }
        catch(IOException e) {
            e.printStackTrace();
        }

        URL u;
        try {
            u = new URL(fileUrl);
            URLConnection connection = u.openConnection();
            connection.connect();

            InputStream input = new BufferedInputStream(u.openStream(), MEGABYTE);
            OutputStream output = new FileOutputStream(pdf);

            byte[] buffer = new byte[MEGABYTE];
            int length;

            while((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            Log.d(TAG, "Successfully wrote to file from URL");

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

        return pdf;
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

