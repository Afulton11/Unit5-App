package com.unit5app.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.unit5app.EndOfHourHandler;
import com.unit5app.R;
import com.unit5app.Settings;
import com.unit5app.calendars.Unit5Calendar;
import com.unit5app.com.unit5app.parsers.RSSReader;
import com.unit5app.com.unit5app.parsers.WestNewsReader;
import com.unit5app.notifications.NotificationReceiver;
import com.unit5app.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to handle the activity the User sees upon opening the App to its main page.
 * @version 2/21/16
 */
public class MainActivity extends BaseActivity {

    private static String TAG = "MainActivity";

    /*
    TODO: add a way for us to know what Activity the user is currently looking at and if the user has paused the application (if we are running in the background).
     */

    /* Lists to hold the titles and descriptions for a future search function */
    public static List<String> all_titles = new ArrayList<>();
    public  static List<String> all_descriptions = new ArrayList<>();

    public static Unit5Calendar mainCalendar;

    private static MainActivity instance;

    /* Buttons to be pressed */
    private Button testPdf, testWestNews, testCalendarReading;

    /* Text displayed on the screen */
    private TextView endOfHourTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setCurrentView(Utils.VIEW_LOADING); //TODO: make setContentView(R.layout.view_loading) not get rid of the major details gained from the base activity such as the title when setting the content view again.
        Settings.load(this);
        instance = this;

        if(!NotificationReceiver.started) NotificationReceiver.start(this);

        //set content view to a loading screen first, Then once everything is loaded we would setContentView to the home screen?
         /*Check if we have internet access*/
       Utils.isInternetConnected(getApplicationContext());

        if(savedInstanceState == null) {

        /* TODO: Launch internal calendar builder to get latest info on events, etc... */
            if(Utils.hadInternetOnLastCheck) {
                mainCalendar = new Unit5Calendar(60);
                WestNewsReader westNews = new WestNewsReader("http://www.unit5.org/site/RSS.aspx?DomainID=30&ModuleInstanceID=1852&PageID=53");
                RSSReader unit5News = new RSSReader("http://www.unit5.org/site/RSS.aspx?DomainID=4&ModuleInstanceID=4&PageID=1");
                mainCalendar.setNewsRssReaders(westNews, unit5News);
                mainCalendar.loadNews();
            }
        }

        /* Load object placement as defined in Resources file */
        setContentView(R.layout.activity_main);
        Utils.setCurrentView(Utils.VIEW_MAIN);

        /* Load the buttons by how they're defined in Resources file */
        testPdf = (Button) findViewById(R.id.btn_testPdf);
        testWestNews = (Button) findViewById(R.id.btn_testWestNews);
        testCalendarReading = (Button) findViewById(R.id.btn_testCalendarReading);

        /* Load text by how they're defined in Resources file */
        endOfHourTime = (TextView) findViewById(R.id.clock_end_of_hour);

        /* Begin testing to see if it's the end of the hour. The text will update accordingly. */
        new EndOfHourHandler(endOfHourTime).start();

//        endOfHourTime.setText(Settings.file_string);
        /* If not, complain to the user. */
        if(!Utils.hadInternetOnLastCheck) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "You are not connected to the internet. Most features will not work.",
                    Toast.LENGTH_LONG);
            toast.show();
        }

        /* Define button click actions. */
        /* For testing the calendar: */
        testCalendarReading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, UpcomingEventsActivity.class));
            }
        });

        /* For testing checking NCWHS news feed: */
        testWestNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, RssActivity.class));
            }
        });

        /* For testing how PDFs are displayed: */
        testPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LunchMenuActivity.class));
            }
        });

        /* A button to take you to regular Unit5 news. */
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSkywardApp();
            }
        });
    }

    /**
     * starts the user's skyward app for checking grades.
     */
    private void startSkywardApp() {
        PackageManager packageManager = getPackageManager();

        Intent intent = packageManager.getLaunchIntentForPackage("com.skyward.mobileaccess"); //package found from going to the app's page on the google play store and looking at the last part of the url.


        List activities = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        boolean hasSkyward = activities.size() > 0;
        if(hasSkyward) {
            startActivity(intent);
        }
    }

    /* Function to handle when you click something in the action bar */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
    }

    return super.onOptionsItemSelected(item);
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

    public static MainActivity getInstance() {
        return instance;
    }
}
