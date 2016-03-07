package com.unit5app.notifications;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.unit5app.R;
import com.unit5app.Settings;
import com.unit5app.activities.MainActivity;

/**
 * @author Andrew
 * @version 3/6/16
 */
public class NotificationIntentService extends IntentService {


    public NotificationIntentService() {
        super(NotificationIntentService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("OnHandle", "Handling Intent");
        try {
            int id = intent.getIntExtra("id", 0);
            String contentText = intent.getStringExtra("message");
            String contentTitle = intent.getStringExtra("title");
            String subText = intent.getStringExtra("sub");

            sendNotification(this, id, contentTitle, contentText, subText);
            Log.d("Service", "Sending Notification!");
        } finally {
            WakefulBroadcastReceiver.completeWakefulIntent(intent);
        }
    }

    public void sendNotification(Context originalContext, int id, String title, String text, String subText) {
        if(Settings.getNotificationBoolean(id)) { //reassuring that the user did in fact choose to have this type of notification sent to them, now we can just end all the notifications and only the ones the user has chosen will be seen.
            Context context = originalContext.getApplicationContext();
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, new Intent(context, MainActivity.class), 0);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentIntent(pendingIntent);
            builder.setAutoCancel(true); //the notification will automatically go away.
            builder.setLargeIcon(BitmapFactory.decodeResource(Resources.getSystem(), R.mipmap.ic_launcher));
            builder.setWhen(System.currentTimeMillis());

            if (title != null) builder.setContentTitle(title);
            if (text != null) builder.setContentText(text);
            if (subText != null) builder.setSubText(subText);
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(id, builder.build());
        }
    }

}
