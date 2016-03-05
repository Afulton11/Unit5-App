package com.unit5app.notifications;

import android.content.Context;

import com.unit5app.calendars.CalendarEvent;
import com.unit5app.calendars.Unit5Calendar;
import com.unit5app.utils.Time;
import com.unit5app.utils.Utils;

/**
 *
 * This class wil manage the toggling of notifications.
 *@author Andrew
 * @version 3/4/16
 */
public class NotificationManager {


    public static void createTodaysNotifications(Context context, Unit5Calendar mainCalendar) {
        for(CalendarEvent e : mainCalendar.getEventsForDate(Time.getCurrentDate(Time.FORMAT_BASIC_DATE)))
            Utils.createNotification(context, e);
    }
}
