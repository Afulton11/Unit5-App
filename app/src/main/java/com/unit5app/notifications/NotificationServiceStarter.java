package com.unit5app.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Starts/restarts on time zone changed, power on, and setting the time from the phone itself.
 * @author Andrew
 * @version 3/6/16
 */
public class NotificationServiceStarter extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationReceiver.start(context);
        Log.d("Starting Receiver", "Starting notification Service!");
    }
}