package com.unit5app;

import android.app.AlarmManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.unit5app.R;
import com.unit5app.calendars.CalendarEvent;
import com.unit5app.utils.Time;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Andrew
 * @version 2/28/16
 * TODO: create a Broadcast Reciever
 *      Useful links for the TODO:
 *          http://nnish.com/2014/12/16/scheduled-notifications-in-android-using-alarm-manager/
 *          http://developer.android.com/reference/android/app/AlarmManager.html
 *
 */
public class NotificationHandler {

    private AlarmManager alarm;
    private Context context;

    public NotificationHandler(Context context) {
        alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.context = context;
    }

    /**
     * Creates a notification that will be
     * @param event - the calendar event to create a notification from
     *              TODO: finsih this method.
     */
    public void createNotification(CalendarEvent event) {
        /*
        Creating the notification http://developer.android.com/guide/topics/ui/notifiers/notifications.html
         */
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(event.getType().toString())
                .setContentTitle(event.getTitle());
        //If we want the notification to open an activity on click, we need to use a TaskStackBuilder.

        /*
        Setting up the timing of the alarm:
         */
        SimpleDateFormat sdf = new SimpleDateFormat(Time.FORMAT_BASIC_DATE);
        Date eventDate = Time.getDateFromString(event.getDate(), Time.FORMAT_BASIC_DATE);
        sdf.format(eventDate);

        Calendar cal = Calendar.getInstance();
        cal.setTime(eventDate);

        long timeMillisOfEvent = cal.getTimeInMillis();
        //RTC_WAKEUP wakes up the device if it is sleeping.
//        alarm.set(AlarmManager.RTC_WAKEUP,timeMillisOfEvent, PendingIntent);
    }
}
