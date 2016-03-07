package com.unit5app.notifications;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.unit5app.Settings;
import com.unit5app.calendars.CalendarEvent;
import com.unit5app.calendars.EventType;
import com.unit5app.calendars.Unit5Calendar;
import com.unit5app.utils.Time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * This class manages notifications.
 *@author Andrew
 * @version 3/4/16
 */
public class MyNotificationHandler {

    private static Unit5Calendar calendar; //this calendar is used only for this class!
    private static Context context;

    public static void init(Context appContext) {
        context = appContext;
        Settings.load(appContext);
        calendar = new Unit5Calendar(60);

        checkCalendarLoaded();

    }

    private static void checkCalendarLoaded() {
        if(!calendar.hasCalendarStartedLoading()) {
            calendar.loadCalendar("com.unit5app.notifications.MyNotificationHandler\tcreateNotificationsFromSettings");
        }
    }


    /**
     * creates notifications based on the user's settings
     */
    public static void createNotificationsFromSettings() {
        Log.d("MyHandler", "Creating notifications from settings");
        for(int i = 0; i < EventType.values().length; i++)
            if(Settings.getBoolean(i)) {
                createAllNotificationsOfType(context, i);
            }
    }

    /**
     * creates all notifications of the given event type id for todays date.
     * @param context - context
     * @param eventTypeId - the Event type id to create a notification for.
     */
    public static void createAllNotificationsOfType(Context context, int eventTypeId) {
        checkCalendarLoaded();
        for(CalendarEvent e : calendar.getEventsForDate(Time.getCurrentDate(Time.FORMAT_BASIC_DATE))) {
            if(e.getType().getId() == eventTypeId) {
                createNotification(context, e);
            }
        }
    }

    /**
     * Cancels all notifications of the given id, that are active.
     * @param context - context
     * @param eventTypeId - the id of the event type to cancel all notifications for.
     */
    public static void cancelNotificationsOfType(Context context, int eventTypeId) {
        android.app.NotificationManager manager = (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(eventTypeId); // may only cancel one notification of the given id, TODO: test this.
    }

    /**
     * cancels all currently active notifications
     * @param context the context
     */
    public static void cancelAllNotifications(Context context) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancelAll();
    }

    /**
     * creates a new notification that will go off at the time of the event. : tested and worked even when the app was closed by the user :
     * todo: test to make sure this will still work even when the device is turned off then back on, This may make us have to use an Android service if it doesnt work after turning off then back on.
     * @param context - context of the Activity to go to when clicking the notification, preferrably the mainActivity.
     * @param event - a CalendarEvent to notify the person of.
     */
    private static void createNotification(Context context, CalendarEvent event) {
        if(!Settings.list_sentNotificationsContains(event.getTitle())) {
            Settings.list_sentNotifications.add(event.getTitle());

            Intent notificationIntent = new Intent(context, NotificationReceiver.class);

            SimpleDateFormat sdf = new SimpleDateFormat(Time.FORMAT_DATE_TIME_12HOUR);
            Calendar c = Calendar.getInstance();
            if (event != null) {
                try {
                    c.setTime(sdf.parse(event.getDate() + " " + event.getTimeOccurring()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                notificationIntent.putExtra("id", event.getType().getId());
                notificationIntent.putExtra("title", event.getType().toString());
                notificationIntent.putExtra("message", event.getTitle());
                notificationIntent.putExtra("sub", event.getTimeOccurring());
            } else {
                notificationIntent.putExtra("title", "null");
                notificationIntent.putExtra("message", "null");
                notificationIntent.putExtra("sub", "null");
            }

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            alarm.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        }
    }

    /**
     * DEBUG METHOD
     * @param c context
     */
    public static void createNotificationAndSendNow(Context c) {
        Intent notificationIntent = new Intent(context, NotificationReceiver.class);

        SimpleDateFormat sdf = new SimpleDateFormat(Time.FORMAT_DATE_TIME_12HOUR);

            notificationIntent.putExtra("title", "null");
            notificationIntent.putExtra("message", "null");
            notificationIntent.putExtra("sub", "null");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        alarm.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10_000, pendingIntent);
    }
}
