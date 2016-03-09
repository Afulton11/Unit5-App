package com.unit5app.activities;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

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
            notificationBoxes[i].setChecked(Settings.getNotificationBoolean(ID));
            notificationBoxes[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (MyNotificationHandler.isCalendarLoaded()) {
                        Settings.setNotificationBoolean(ID, isChecked);
                        Settings.save(getApplicationContext());
                        if (!isChecked) {
                            MyNotificationHandler.cancelNotificationsOfType(getApplicationContext(), ID);
                        } else {
                            MyNotificationHandler.createAllNotificationsOfType(getApplicationContext(), ID);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please wait.. still loading a few things!", Toast.LENGTH_LONG);
                    }
                }
            });
        }
        /*
        ******************************NOTIFICATION SETTINGS END*****************************************
        */

        /*
        *****************************ARTICLE SETTINGS START*****************************************
         */

        CheckBox scrollWithTile = (CheckBox) findViewById(R.id.settings_checkBox_scrollTitle);
        scrollWithTile.setChecked(Settings.getArticleSettingsBoolean(Settings.ID_ARTICLE_SETTING_SCROLL_WITH_TITLE));
        scrollWithTile.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Settings.setArticleSettingsBoolean(0, isChecked);
                    Settings.save(getApplicationContext());
            }
        });

        /*
        ******************************ARTICLE SETTINGS END*****************************************
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
