package com.unit5app.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
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

/**
 * Class to handle the activity the User sees upon opening the App to its main page.
 * @version 2/21/16
 */
public class MainActivity extends BaseActivity {
    /* Lists to hold the titles and descriptions for a future search function */
    //public static List<String> all_titles = new ArrayList<>();
    //public  static List<String> all_descriptions = new ArrayList<>();

    public static Unit5Calendar mainCalendar;
    private static String TAG = "MainActivity";

    /* Buttons to be pressed */
    private Button testLunch, testSpecials, testWestNews, testCalendarReading;

    /* Text displayed on the screen */
    private TextView endOfHourTime;

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
        /* Load object placement as defined in Resources file */
        setContentView(R.layout.activity_main);

        /* Load the buttons by how they're defined in Resources file */
        testLunch = (Button) findViewById(R.id.btn_testLunch);
        testSpecials = (Button) findViewById(R.id.btn_testSpecials);
        testWestNews = (Button) findViewById(R.id.btn_testWestNews);
        testCalendarReading = (Button) findViewById(R.id.btn_testCalendarReading);

        /* Load text by how they're defined in Resources file */
        endOfHourTime = (TextView) findViewById(R.id.clock_end_of_hour);

        /* Begin testing to see if it's the end of the hour. The text will update accordingly. */
        new EndOfHourHandler(endOfHourTime).start();

//        endOfHourTime.setText(Settings.file_string);

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

        /* For testing how the lunch menu is displayed: */
        testLunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LunchMenuActivity.class));
            }
        });

        /* For testing how the specials menu is displayed: */
        testSpecials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SpecialsMenuActivity.class));
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

        /* If no internet, complain to the user. */
        if (!Utils.hadInternetOnLastCheck) {
            Toast.makeText(getApplicationContext(),
                    "You are not connected to the internet. Most features will not work.",
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * starts the user's skyward app for checking grades.
     */
    private void startSkywardApp() {
        boolean hasSkyward = Utils.isPackageInstalled("com.skyward.mobileaccess", this);

        if(hasSkyward) {
            PackageManager packageManager = getPackageManager();
            Intent intent = packageManager.getLaunchIntentForPackage("com.skyward.mobileaccess");

            startActivity(intent);
        } else {
            Log.d(TAG, "Skyward app not installed. Attempting to open web browser....");
            Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse("https://skyward-web." +
                    "unit5.org/scripts/wsisa.dll/WService=wsSky/seplog01.w"));
            startActivity(browser);
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
}
