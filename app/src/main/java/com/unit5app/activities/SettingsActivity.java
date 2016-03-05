package com.unit5app.activities;

import android.os.Bundle;
import android.widget.CheckBox;

import com.unit5app.R;
import com.unit5app.utils.Utils;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Utils.setCurrentView(Utils.VIEW_SETTINGS);

        CheckBox lateStart = (CheckBox) findViewById(R.id.settings_checkBox_lateStart);

    }
}
