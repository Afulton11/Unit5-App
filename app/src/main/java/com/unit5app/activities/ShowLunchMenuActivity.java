package com.unit5app.activities;

import android.os.Bundle;

import com.joanzapata.pdfview.PDFView;
import com.unit5app.R;
import com.unit5app.utils.Utils;

import java.io.File;

/**
 *
 */
public class ShowLunchMenuActivity extends BaseActivity {

    private static File result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setCurrentView(Utils.VIEW_LUNCH_MENU);
        setContentView(R.layout.pdfviewer_layout);

        PDFView pdfView = (PDFView) findViewById(R.id.pdfview);
        if(result != null) {
            pdfView.fromFile(result)
                    .defaultPage(1)
                    .showMinimap(false)
                    .enableSwipe(false)
                    .load();
        }
    }

    public static void setResult(File res) {
        result = res;
    }
}
