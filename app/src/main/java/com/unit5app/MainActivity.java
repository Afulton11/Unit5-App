package com.unit5app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.unit5app.com.unit5app.parsers.RSSReader;
import com.unit5app.com.unit5app.parsers.WestNewsReader;
import com.unit5app.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to handle the activity the User sees upon opening the App to its main page.
 * @version 2/21/16
 */
public class MainActivity extends AppCompatActivity {

    /*
    TODO: add a way for us to know what Activity the user is currently looking at and if the user has puased the application (if we are running in the background).
     */

    /* Lists to hold the titles and descriptions for a future search function */
    public static List<String> all_titles = new ArrayList<>();
    public  static List<String> all_descriptions = new ArrayList<>();

    public static Calendar mainCalendar;

    /* Buttons to be pressed */
    private Button testpdf, testWestNews, testCalendarReading;

    /* Text displayed on the screen */
    private TextView endOfHourTime;

    // private RSSReader rssCalendarReader; /* Unnecessary b/c Calendar Reader is its own thing?

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set content view to a loading screen first, Then once everything is loaded we would setContentView to the home screen?

        mainCalendar = new Calendar(60);
        WestNewsReader westNews = new WestNewsReader("http://www.unit5.org/site/RSS.aspx?DomainID=30&ModuleInstanceID=1852&PageID=53");
        RSSReader unit5News = new RSSReader("http://www.unit5.org/site/RSS.aspx?DomainID=4&ModuleInstanceID=4&PageID=1");
        mainCalendar.loadNews(westNews, unit5News);

        /* Load object placement as defined in Resources file */
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* Load the buttons by how they're defined in Resources file */
        testpdf = (Button) findViewById(R.id.btn_testPdf);
        testWestNews = (Button) findViewById(R.id.btn_testWestNews);
        testCalendarReading = (Button) findViewById(R.id.btn_testCalendarReading);

        /* Load text by how they're defined in Resources file */
        endOfHourTime = (TextView) findViewById(R.id.clock_end_of_hour);

        /* Check if we have internet access. */
        Utils.isInternetConnected(getApplicationContext());

        /* If not, complain to the user. */
        if(!Utils.hadInternetOnLastCheck) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "You are not connected to the internet. Most features will not work.",
                    Toast.LENGTH_LONG);
            toast.show();
        }

        /* TODO: Launch internal calendar builder to get latest info on events, etc... */



        /* The calendar reader is loaded up in here so that we will know what is happening on the day the app is opened. It may be a late start day.
         */
        if(Utils.isInternetConnected(getApplicationContext())) {
            UpcomingEventsActivity.loadCalendar();
        }

//        new PDFGrabberTask("http://www.unit5.org/cms/lib03/IL01905100/Centricity/Domain/55/2016%20Feb%20Sr%20High%20Lunch.pdf").start();

        /* Begin testing to see if it's the end of the hour. The text will update accordingly. */
        new EndOfHourHandler(endOfHourTime).start();

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
        testpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AnnouncementActivity.class));
            }
        });

        /* A button to take you to regular Unit5 news. */
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, RssActivity.class));
            }
        });

        /* TEST CODE, PROOF OF CONCEPT */
        //PDDocument doc =  new PDFGrabberTask("http://www.unit5.org/cms/lib03/IL01905100/Centricity/Domain/55/2016%20Feb%20Sr%20High%20Lunch.pdf").getPdf();
        /* END TEST CODE */
    }

    /* Button to take you to settings panel. */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
}
