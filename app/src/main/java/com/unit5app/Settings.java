package com.unit5app;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.unit5app.calendars.EventType;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * @author Andrew
 * @version 3/4/16
 */
public class Settings {

    private static final String TAG = "Settings";

    private static final String FILE_PATH = "./Settings.txt";

    /**
     * if true, notifications are on. if false, notifications are off.
     */
    private static boolean notifications;

    /**
     * a boolean for each of the Calendar Event Types. for each true boolean and notifications are on (true), then the user will be notified of each turned on event type.
     */
    private static final int NUM_NOTIFICATION_TYPES = EventType.values().length;
    private static boolean[] notificationTypes = new boolean[NUM_NOTIFICATION_TYPES];
    /**
     * get User's last known settings, called from startUp or first time needed to access settings.
     * The Syntax of the file is currently like this:
     * <br></br>
     * <br>    data-type\tnameID\tcondition</br>
     *
     *      <br>bool\t0\tfalse</br>
     *      <br>bool\t1\ttrue</br>
     */
    public static void load(Context context) {
        try {
            Log.d(TAG, "DIR: " + context.getFilesDir());
            File file = new File(context.getFilesDir(), FILE_PATH);
            Log.d(TAG, "DIR: " + file.getCanonicalPath());
            if (!file.exists()) file.createNewFile();

            InputStream in = context.openFileInput(file.getName());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String currentLine;
            String[] tokens;
            while((currentLine = reader.readLine()) != null) {
                if(!currentLine.startsWith("//")) {
                    tokens = currentLine.split("\t");
                    if (tokens[0].equalsIgnoreCase("bool") && tokens.length > 2) {
                        boolean currentBool = Boolean.parseBoolean(tokens[2]);
                        setBoolean(Integer.parseInt(tokens[1]), currentBool);
//                        continue; //next line.
                    }
                }
            }
        } catch (FileNotFoundException e) {
            Log.d(TAG, e.getMessage(), e);
        } catch (IOException ioe) {
            Log.d(TAG, ioe.getMessage(), ioe);
        }
    }

    /**
     * Saves the user's current settings when the app is paused/stopped.
     */
    public static void save(Context context) {
        try {
            File file = new File(context.getFilesDir(), FILE_PATH);
            if (!file.exists()) file.createNewFile();

            OutputStreamWriter out = new OutputStreamWriter(context.openFileOutput(file.getName(), Context.MODE_PRIVATE));
            BufferedWriter writer = new BufferedWriter(out);

            for(int i = 0; i < NUM_NOTIFICATION_TYPES; i++) {
                writer.write("bool\t" + i + "\t" + notificationTypes[i]);
                writer.newLine();
            }
            writer.flush();
            writer.close();
            Toast.makeText(context, "Done Writing to settings!", Toast.LENGTH_LONG);
        } catch (FileNotFoundException e) {
            Log.d(TAG, e.getMessage(), e);
        } catch (IOException ioe) {
            Log.d(TAG, ioe.getMessage(), ioe);
        }
    }

    private static void setBoolean(int id, boolean currentBool){
        try {
            notificationTypes[id] = currentBool;
        } catch(IndexOutOfBoundsException e) {
            Log.d(TAG, e.getMessage(), e);
        }
    }

    public static boolean isNotifications() {
        return notifications;
    }

    public static void setNotifications(boolean bool) {
        notifications = bool;
    }

    public static boolean isNotify_regularTypes() {
        return notificationTypes[EventType.regular.getId()];
    }

    public static void setNotify_regularTypes(boolean bool) {
        notificationTypes[EventType.regular.getId()] = bool;
    }

    public static boolean isNotify_holidays() {
        return notificationTypes[EventType.holiday.getId()];
    }

    public static void setNotify_holidays(boolean bool) {
        notificationTypes[EventType.holiday.getId()] = bool;
    }

    public static boolean isNotify_lateStarts() {
        return notificationTypes[EventType.lateStart.getId()];
    }

    public static void setNotify_lateStarts(boolean bool) {
        notificationTypes[EventType.lateStart.getId()] = bool;
    }

    public static boolean isNotify_noSchools() {
        return notificationTypes[EventType.noSchool.getId()];
    }

    public static void setNotify_noSchools(boolean bool) {
        notificationTypes[EventType.noSchool.getId()] = bool;
    }

    public static boolean isNotify_lastDayBeforeBreaks() {
        return notificationTypes[EventType.lastDayBeforeBreak.getId()];
    }

    public static void setNotify_lastDayBeforeBreaks(boolean bool) {
        notificationTypes[EventType.lastDayBeforeBreak.getId()] = bool;
    }

    public static boolean isNotify_endOfGradingPeriods() {
        return notificationTypes[EventType.endOf.getId()];
    }

    public static void setNotify_endOfGradingPeriods(boolean bool) {
        notificationTypes[EventType.endOf.getId()] = bool;
    }

    public static boolean isNotify_meetings() {
        return notificationTypes[EventType.meeting.getId()];
    }

    public static void setNotify_meetings(boolean bool) {
        notificationTypes[EventType.meeting.getId()] = bool;
    }
}
