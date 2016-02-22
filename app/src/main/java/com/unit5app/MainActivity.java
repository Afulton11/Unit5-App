package com.unit5app;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Class to handle the activity the User sees upon opening the App to its main page.
 * @version 2/21/16
 */
public class MainActivity extends AppCompatActivity {

    /* Lists to hold the titles and descriptions for a future search function */
    public static List<String> all_titles = new ArrayList<>();
    public  static List<String> all_descriptions = new ArrayList<>();

    /* Buttons to be pressed */
    private Button testpdf, testWestNews, testCalendarReading;

    /* Text displayed on the screen */
    private TextView endOfHourTime;

    // private RSSReader rssCalendarReader; /* Unnecessary b/c Calendar Reader is its own thing?

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Load object placement as defined in Resources file */
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* Define the buttons by how they're defined in Resources file */
        testpdf = (Button) findViewById(R.id.btn_testPdf);
        testWestNews = (Button) findViewById(R.id.btn_testWestNews);
        testCalendarReading = (Button) findViewById(R.id.btn_testCalendarReading);

        /* Define text by how they're defined in Resources file */
        endOfHourTime = (TextView) findViewById(R.id.clock_end_of_hour);

        /* Check if we have internet access. */
        Utils.hadInternetOnLastCheck = Utils.isInternetConnected(getApplicationContext());

        /* If not, complain. */
        if(!Utils.hadInternetOnLastCheck) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "You are not connected to the internet. Most features will not work.",
                    Toast.LENGTH_LONG);
            toast.show();
        }

        /* TODO: Launch internal calendar builder to get latest info on events, etc... */
        /*
        The calendar reader is loaded up in here so that we will know what is happening on the day the app is opened. It may be a late start day.
         */
        if(Utils.isInternetConnected(getApplicationContext())) {
            UpcomingEventsActivity.loadCalendar();
        }
        else {

        }

        new EndOfHourHandler(endOfHourTime).start();

        testCalendarReading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, UpcomingEventsActivity.class));
            }
        });

        testWestNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RssActivity.useWestNews = true;
                startActivity(new Intent(MainActivity.this, RssActivity.class));
            }
        });

        testpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AnnouncementActivity.class));
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                RssActivity.useWestNews = false;
                startActivity(new Intent(MainActivity.this, RssActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

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

    /**
     * returns true if the user has an internet connection currently.
     * @return
     */
    public boolean hasInternetConnection() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity != null) {
            NetworkInfo[] inf = connectivity.getAllNetworkInfo();
            if (inf != null)
                for (int i = 0; i < inf.length; i++)
                    if (inf[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }
}
