package com.unit5app.activities;

import android.content.Intent;
import android.os.Bundle;

import com.unit5app.R;
import com.unit5app.Settings;
import com.unit5app.calendars.Unit5Calendar;
import com.unit5app.com.unit5app.parsers.RSSReader;
import com.unit5app.com.unit5app.parsers.WestNewsReader;
import com.unit5app.notifications.NotificationReceiver;
import com.unit5app.utils.Utils;

public class onStartUpActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_loading);
        Settings.load(this);

        if(!NotificationReceiver.started) NotificationReceiver.start(this);

        //set content view to a loading screen first, Then once everything is loaded we would setContentView to the home screen?
         /*Check if we have internet access*/
        Utils.isInternetConnected(getApplicationContext());

        if(savedInstanceState == null) {
            if(Utils.hadInternetOnLastCheck) {
                MainActivity.mainCalendar = new Unit5Calendar(60);
                WestNewsReader westNews = new WestNewsReader("http://www.unit5.org/site/RSS.aspx?DomainID=30&ModuleInstanceID=1852&PageID=53");
                RSSReader unit5News = new RSSReader("http://www.unit5.org/site/RSS.aspx?DomainID=4&ModuleInstanceID=4&PageID=1");
                MainActivity.mainCalendar.setNewsRssReaders(westNews, unit5News);
                MainActivity.mainCalendar.loadNews();
            }
        }

        startActivity(new Intent(onStartUpActivity.this, MainActivity.class));
        finish(); //removes the back stack so the user can't go back to the loading screen.
    }

}
