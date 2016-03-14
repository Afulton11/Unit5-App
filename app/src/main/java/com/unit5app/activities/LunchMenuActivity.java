package com.unit5app.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;

public class LunchMenuActivity extends BaseActivity {
    private static final String TAG = "LunchMenuPDFReader";
    private static final String fileName = "2016 Mar Sr High Lunch.pdf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
