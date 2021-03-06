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
import com.unit5app.utils.MethodHolder;
import com.unit5app.utils.Time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * This class manages notifications.
 *@author Andrew
 * @version 3/4/16
 */
public class MyNotificationHandler {

    private static Unit5Calendar calendar; //this calendar is used only for this class!
    private static Context context;

    public static void init(Context appContext) {
        Settings.load(appContext);
        context = appContext;
        calendar = new Unit5Calendar(60, false);
        checkCalendarLoaded();
    }

    private static void checkCalendarLoaded() {
        if(!calendar.hasCalendarStartedLoading()) {
            Log.d("MyNotificationHandler", "Loading calendar");
            calendar.loadCalendar(new MethodHolder(MyNotificationHandler.class.getName(), "createNotificationsFromSettings", null));
        }
    }

    public static void addMethodRequestToCalendar(MethodHolder methodHolder) {
        checkCalendarLoaded();
        if(!isCalendarLoaded()) {
            calendar.getCalendarTask().addMethodRequests(methodHolder);
        }
    }

    public static boolean isCalendarLoaded() {
        return calendar.getCalendarTask().isLoaded();
    }


    /**
     * creates notifications based on the user's settings
     */
    public static void createNotificationsFromSettings() {
        for(int i = 0; i < EventType.values().length; i++)
            if(Settings.getNotificationBoolean(i)) {
                Log.d("MyHandler", "Creating notifications from settings");
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
            if(e.getType().getId() == eventTypeId && !Settings.list_sentNotificationsContains(e.getTitle())) {
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
     * @param context - context of the Activity to go to when clicking the notification, preferrably the mainActivity.
     * @param event - a CalendarEvent to notify the person of.
     */
    private static void createNotification(Context context, CalendarEvent event) {
        Intent notificationIntent = new Intent(context, NotificationReceiver.class);
        Log.d("MyHandler", "creating notification!");
        SimpleDateFormat sdf = new SimpleDateFormat(Time.FORMAT_DATE_TIME_12HOUR);
        Calendar c = Calendar.getInstance();
        if (event != null) {
            if(!Settings.list_sentNotificationsContains(event.getTitle())) {
                try {
                    c.setTime(sdf.parse(event.getDate() + " " + event.getTimeOccurring()));
                    if(c.getTimeInMillis() < System.currentTimeMillis()) c.setTimeInMillis(System.currentTimeMillis() + 500);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                notificationIntent.putExtra("id", event.getType().getId());
                notificationIntent.putExtra("title", event.getTitle());
                notificationIntent.putExtra("message", event.getTitle());
                notificationIntent.putExtra("sub", event.getTimeOccurring());
            }
        } else {
            notificationIntent.putExtra("title", "Unit5 App");
            notificationIntent.putExtra("message", "Sorry, this notification should not have been sent.");
            notificationIntent.putExtra("sub", "");
        }
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }
}
