package com.unit5app;

import android.content.Context;
import android.util.Log;

import com.unit5app.calendars.EventType;
import com.unit5app.utils.Time;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrew
 * @version 3/4/16
 */
public class Settings {

    private static final String TAG = "Settings";

    private static final String FILE_NAME = "Settings.txt";

    /**
     * if true, notifications are on. if false, notifications are off.
     */
    private static boolean notifications;

//    public static String file_string = null;

    /**
     * a boolean for each of the Calendar Event Types. for each true boolean and notifications are on (true), then the user will be notified of each turned on event type.
     */
    private static final int NUM_NOTIFICATION_TYPES = EventType.values().length;
    private static boolean[] notificationTypes = new boolean[NUM_NOTIFICATION_TYPES];

    public static String lastSendDate = null;
    /**
     * list of the sentNotifications.
     */
    public static List<String> list_sentNotifications = new ArrayList<>();

    /**
     * get User's last known settings, called from startUp or first time needed to access settings.
     * The Syntax of the file is currently like this:
     * <br></br>
     * NOTIFICATION TOGGLES:
     * <br>
     *     type\tnameID\tcondition
     * </br>
     *      <br>bool\t0\tfalse</br>
     *      <br>bool\t1\ttrue</br>
     *      Whether or not notifications have been sent out today:
     *      <br>TodaysDate\tdate</br>
     *      <br>CalEvent\ttrue</br>
     *      <br>CalEvent\tfalse</br>
     */
    public static void load(Context context) {
        if(context != null) {
            try {
                Log.d(TAG, "Loading Settings...");
                File file = new File(context.getFilesDir(), FILE_NAME);
                if (!file.exists()) file.createNewFile();

                InputStream in = context.openFileInput(file.getName());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String currentLine;
                String[] tokens;

//            StringBuffer buffer = new StringBuffer();
                while ((currentLine = reader.readLine()) != null) {
//               buffer.append(currentLine + System.getProperty("line.separator"));
                    if (!currentLine.startsWith("//")) {
                        tokens = currentLine.split("\t");
                        if (tokens.length > 2 && tokens[0].equalsIgnoreCase("bool")) {
                            boolean currentBool = Boolean.parseBoolean(tokens[2]);
                            setBoolean(Integer.parseInt(tokens[1]), currentBool);
                        } else if (tokens.length > 1) {
                            if (tokens[0].equals("TodaysDate")) {
                                lastSendDate = tokens[1];
                            } else if (tokens[0].equals("CalEvent") && lastSendDate.equals(Time.getCurrentDate(Time.FORMAT_BASIC_DATE))) {
                                list_sentNotifications.add(tokens[1]);
                            }
                        }
                    }
                }
                Log.d(TAG, "done loading settings!");
//            file_string = buffer.toString();
            } catch (FileNotFoundException e) {
                Log.d(TAG, e.getMessage(), e);
            } catch (IOException ioe) {
                Log.d(TAG, ioe.getMessage(), ioe);
            }
        }
    }

    public static void clearSave(Context context) {
        try {
            Log.d(TAG, "Clearing Settings.txt...");
            File file = new File(context.getFilesDir(), FILE_NAME);
            if (!file.exists()) file.createNewFile();

            OutputStreamWriter out = new OutputStreamWriter(context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE));
            BufferedWriter writer = new BufferedWriter(out);

            writer.write("");
            writer.flush();
            writer.close();
            Log.d(TAG, "Cleared Settings.txt!");
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
            Log.d(TAG, "Saving Settings...");
            File file = new File(context.getFilesDir(), FILE_NAME);
            if (!file.exists()) file.createNewFile();

            OutputStreamWriter out = new OutputStreamWriter(context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE));
            BufferedWriter writer = new BufferedWriter(out);

            for(int i = 0; i < NUM_NOTIFICATION_TYPES; i++) {
                writer.write("bool\t" + i + "\t" + notificationTypes[i]);
                writer.newLine();
            }
            writer.write("TodaysDate\t" + Time.getCurrentDate(Time.FORMAT_BASIC_DATE));
            writer.newLine();
            for(int i = 0; i < list_sentNotifications.size(); i++) {
                writer.write("CalEvent\t" + list_sentNotifications.get(i));
                writer.newLine();
            }
            writer.flush();
            writer.close();
            Log.d(TAG, "Saved Settings!");
        } catch (FileNotFoundException e) {
            Log.d(TAG, e.getMessage(), e);
        } catch (IOException ioe) {
            Log.d(TAG, ioe.getMessage(), ioe);
        }
    }

    /**
     * true if the list_sentNotifications contains the given title
     * @param title the title of a notification to check if it has already beens sent once today.
     * @return true if the title is found in the list.
     */
    public static boolean list_sentNotificationsContains(String title) {
        for(String s : list_sentNotifications) if(s.equalsIgnoreCase(title)) return true;
        return false;
    }

    public static void setBoolean(int id, boolean bool){
        try {
            notificationTypes[id] = bool;
        } catch(IndexOutOfBoundsException e) {
            Log.d(TAG, e.getMessage(), e);
        }
    }

    public static boolean getBoolean(int id) {
        return notificationTypes[id];
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
