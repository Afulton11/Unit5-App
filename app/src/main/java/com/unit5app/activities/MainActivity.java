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
import com.unit5app.notifications.NotificationReceiver;
import com.unit5app.utils.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Class to handle the activity the User sees upon opening the App to its main page.
 */
public class MainActivity extends BaseActivity {
    public static Unit5Calendar mainCalendar;
    private static String TAG = "MainActivity";

    // Element declaration (Ignore that these objects and methods "aren't used", they are.)
    @Bind(R.id.btn_testLunch)           protected Button testLunch;
    @Bind(R.id.btn_testSpecials)        protected Button testSpecials;
    @Bind(R.id.btn_testWestNews)        protected Button testWestNews;
    @Bind(R.id.btn_testCalendarReading) protected Button testCalendarReading;
    @Bind(R.id.btn_testSkyward)         protected FloatingActionButton testSkyward;

    @Bind(R.id.clock_end_of_hour)       protected TextView endOfHourTime;

    // Element definition
    @OnClick(R.id.btn_testLunch)
    protected void clickLunch() {
        startActivity(new Intent(this, LunchMenuActivity.class));
    }
    @OnClick(R.id.btn_testSpecials)
    protected void clickSpecials() {
        startActivity(new Intent(this, SpecialsMenuActivity.class));
    }
    @OnClick(R.id.btn_testWestNews)
    protected void clickWestNews() {
        startActivity(new Intent(this, WestNewsActivity.class));
    }
    @OnClick(R.id.btn_testCalendarReading)
    protected void clickCalendarReading() {
        startActivity(new Intent(this, UpcomingEventCalendarActivity.class));
    }
    @OnClick(R.id.btn_testSkyward)
    protected void clickSkyward() {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load object placement as defined in Resources file
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        Settings.load(this);
        if(!NotificationReceiver.started) NotificationReceiver.start(this);

        /*Check if we have internet access*/
        Utils.isInternetConnected(getApplicationContext());

        if(savedInstanceState == null) {
            if(Utils.hadInternetOnLastCheck) {
                MainActivity.mainCalendar = new Unit5Calendar(60);
            }
        }

        // Begin testing to see if it's the end of the hour. The text will update accordingly.
        new EndOfHourHandler(endOfHourTime).start();

        //endOfHourTime.setText(Settings.file_string);

        // If no internet, complain to the user.
        if (!Utils.hadInternetOnLastCheck) {
            Toast.makeText(getApplicationContext(),
                    "You are not connected to the internet. Most features will not work.",
                    Toast.LENGTH_LONG).show();
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
