package com.unit5app.tasks;

import android.app.Activity;
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

/**
 * Abstract class to download files from URLs. Subclasses should extend the onPostExecute()
 * method for file type-specific tasks (like .pdf files, .doc files, etc...) and to modify the UI.
 */
public abstract class DownloadFileTask extends AsyncTask<String, Void, File> {
    protected static final int MEGABYTE = 1024*1024;
    protected Activity activity;
    private static final String TAG = "DownloadFileTask";

    /**
     * Makes a new DownloadFileTask. Call taskName.execute(String url, String fileName) to start it.
     * @param activity the Task is running for/running in. Needed to get context for file saving and
     *                 for modifying the UI in derived subclasses.
     */
    public DownloadFileTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected File doInBackground(String... params) {
        String fileUrl = params[0];   // http://website.org/directory/filename.extension
        String fileName = params[1];  // whatIWantToCallFile.extension
        File file = new File(activity.getFilesDir(), fileName);

        try {
            if(file.createNewFile()) {
                Log.d(TAG, "Successfully created file " + file.getAbsolutePath() + ".");
            }

            URL u = new URL(fileUrl);
            URLConnection connection = u.openConnection();
            connection.connect();

            InputStream input = new BufferedInputStream(u.openStream(), MEGABYTE);
            OutputStream output = new FileOutputStream(file);

            byte[] buffer = new byte[MEGABYTE];
            int length;

            while((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            Log.d(TAG, "Finished writing to file " + fileName + " from URL.");

            // Flush output
            output.flush();

            // Close streams
            output.close();
            input.close();
        }
        catch(MalformedURLException e) {
            e.printStackTrace();
            Toast.makeText(activity, "Bad URL. File could not be downloaded.",
                    Toast.LENGTH_LONG).show();
        }
        catch(IOException e) {
            e.printStackTrace();
        }

        return file;
    }
}
