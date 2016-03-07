package com.unit5app.notifications;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.unit5app.Settings;
import com.unit5app.utils.Utils;

/**
 * @author Andrew
 * @version 2/29/16
 */
public class NotificationReceiver extends WakefulBroadcastReceiver{

    public static boolean started = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Receiver", "received notification!");
        Settings.load(context);

        if(!Settings.list_sentNotificationsContains(intent.getStringExtra("title")) && Settings.getBoolean(intent.getIntExtra("id", 0))) {
            ComponentName comp = new ComponentName(context.getPackageName(), com.unit5app.notifications.NotificationIntentService.class.getName());
            startWakefulService(context, (intent.setComponent(comp))); //calls onHandleIntent for the NotificationIntentService

            Settings.list_sentNotifications.add(intent.getStringExtra("title"));
            Settings.save(context);
        }

        try { //attempting to play default ringtone
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }

        setResultCode(Activity.RESULT_OK);
    }

    /**
     * starts the notification service.
     * @param context context
     */
    public static void start(Context context) { //TODO: fix the error where context is null when starting from the mainActivity, it may also be null when powering on the device and recieving context from the NotificationStarter
        if(Utils.isInternetConnected(context)) {
            started = true;
            MyNotificationHandler.init(context);
        }

    }

}
