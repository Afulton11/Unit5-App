package com.unit5app.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.unit5app.R;
import com.unit5app.tasks.DownloadFile;

import java.io.File;
import java.util.concurrent.ExecutionException;
/* http://www.unit5.org/cms/lib03/IL01905100/Centricity/Domain/55/2016%20Mar%20Sr%20High%20Lunch.pdf
 */

public class LunchMenuActivity extends BaseActivity {
    private static final String TAG = "LunchMenuPDFReader";
    private static final String fileUrl = "http://www.unit5.org/cms/lib03/IL01905100/Centricity/Domain/55/2016%20Mar%20Sr%20High%20Lunch.pdf";
    private static final String fileName = "MarchMenu.pdf";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.announcements_layout);

        Log.d(TAG, "Started downloading " + fileUrl);
        new DownloadFile(getApplicationContext()).execute(fileUrl, fileName);

        /*
        File pdf = new File(getApplicationContext().getFilesDir(), fileName);
        Uri path = Uri.fromFile(pdf);
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {
            startActivity(pdfIntent);
        }
        catch(ActivityNotFoundException e) {
            Toast.makeText(this, "No activity found to display PDF", Toast.LENGTH_SHORT).show();
        } */
    }
}
