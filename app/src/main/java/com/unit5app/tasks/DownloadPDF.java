package com.unit5app.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.joanzapata.pdfview.PDFView;
import com.unit5app.R;

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
 * Class to download and then display PDFs.
 */
public class DownloadPDF extends AsyncTask<String, Void, File> {
    private static final int MEGABYTE = 1024 * 1024;
    private final String TAG = "PDFDownloader";
    private Activity activity;

    public DownloadPDF(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPostExecute(File result) {
        super.onPostExecute(result);

        PDFView pdfView = (PDFView) activity.findViewById(R.id.pdfview);
        pdfView.fromFile(result)
                .defaultPage(1)
                .showMinimap(false)
                .enableSwipe(false)
                .load();
    }

    @Override
    protected File doInBackground(String... strings) {
        String fileUrl = strings[0];   // http://unit5.org/directory/thePdf.pdf
        String fileName = strings[1];  // myPDFName.pdf
        File pdf = new File(activity.getFilesDir(), fileName);

        try {
            if(pdf.createNewFile()) {
                Log.d(TAG, "Successfully created " + pdf.getAbsolutePath() + ".");
            }

            URL u = new URL(fileUrl);
            URLConnection connection = u.openConnection();
            connection.connect();

            InputStream input = new BufferedInputStream(u.openStream(), MEGABYTE);
            OutputStream output = new FileOutputStream(pdf);

            byte[] buffer = new byte[MEGABYTE];
            int length;

            while((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            Log.d(TAG, "Successfully wrote to file from URL.");

            // Flush output
            output.flush();

            // Close streams
            output.close();
            input.close();
        }
        catch(MalformedURLException e) {
            e.printStackTrace();
            Toast.makeText(activity, "Bad URL. PDF could not be downloaded.",
                    Toast.LENGTH_LONG).show();
        }
        catch(IOException e) {
            e.printStackTrace();
        }

        return pdf;
    }
}

