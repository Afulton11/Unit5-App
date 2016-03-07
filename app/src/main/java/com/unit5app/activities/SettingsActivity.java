package com.unit5app.activities;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.unit5app.R;
import com.unit5app.Settings;
import com.unit5app.notifications.MyNotificationHandler;
import com.unit5app.utils.Utils;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Utils.setCurrentView(Utils.VIEW_SETTINGS);
        /*
        *****************************NOTIFICATION SETTINGS START*****************************************
         */
        CheckBox regularEvents = (CheckBox) findViewById(R.id.settings_checkBox_regularEvents);
        CheckBox holidays = (CheckBox) findViewById(R.id.settings_checkBox_holidays);
        CheckBox lateStarts = (CheckBox) findViewById(R.id.settings_checkBox_lateStart);
        CheckBox noSchoolDays = (CheckBox) findViewById(R.id.settings_checkBox_noSchool);
        CheckBox lastDayBeforeBreak = (CheckBox) findViewById(R.id.settings_checkBox_lastDayBeforeBreak);
        CheckBox endOfGradingPeriods = (CheckBox) findViewById(R.id.settings_checkBox_endOfGradingPeriods);
        CheckBox meetings = (CheckBox) findViewById(R.id.settings_checkBox_meetings);

        /*
        Purposely put notificationBoxes in the same order as their respective event type id's. This makes implementing the code for each check box easier.
         */
        CheckBox[] notificationBoxes = new CheckBox[] {regularEvents, holidays, lateStarts, noSchoolDays, lastDayBeforeBreak,
        endOfGradingPeriods, meetings};

        for(int i = 0; i < notificationBoxes.length; i++) {
            final int ID = i;
            notificationBoxes[i].setChecked(Settings.getBoolean(ID));
            notificationBoxes[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Settings.setBoolean(ID, isChecked);
                    Settings.save(getApplicationContext());
                    if(!isChecked) {
                        MyNotificationHandler.cancelNotificationsOfType(getApplicationContext(), ID);
                    } else {
                        MyNotificationHandler.createAllNotificationsOfType(getApplicationContext(), ID);
                    }
                }
            });
        }
        /*
        ******************************NOTIFICATION SETTINGS END*****************************************
        */
    }

    @Override
    public void onPause() {
        super.onPause();
        Utils.universalOnPause(getApplicationContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        Utils.universalOnResume(getApplicationContext());
    }

}