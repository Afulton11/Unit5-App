package com.unit5app.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.unit5app.R;
import com.unit5app.tasks.DownloadFile;
import com.unit5app.utils.Utils;

import java.io.File;

public class LunchMenuActivity extends BaseActivity {
    private static final String TAG = "LunchMenuPDFReader";
    private static final String fileUrl = "http://www.unit5.org/cms/lib03/IL01905100/Centricity/Domain/55/2016%20Mar%20Sr%20High%20Lunch.pdf";
    private static final String fileName = "2016 Mar Sr High Lunch.pdf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new DownloadFile().execute(fileUrl, fileName);

        setContentView(R.layout.announcements_layout);
        Utils.setCurrentView(Utils.VIEW_ANNOUNCEMENTS);

        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/pdfCache/" + fileName);  // -> filename = thepdf.pdf
        Uri path = Uri.fromFile(pdfFile);
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {
            startActivity(pdfIntent);
        }
        catch(ActivityNotFoundException e) {
            Toast.makeText(this, "No activity found to display PDF", Toast.LENGTH_SHORT).show();
        }
    }
}
