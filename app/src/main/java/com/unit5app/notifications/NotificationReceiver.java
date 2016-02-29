package com.unit5app.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.unit5app.R;
import com.unit5app.activities.MainActivity;

/**
 * @author Andrew
 * @version 2/29/16
 */
public class NotificationReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Receiver", "received notification!");
        String contentText = intent.getStringExtra("message");
        String contentTitle = intent.getStringExtra("title");
        String subText = intent.getStringExtra("sub");

        sendNotification(context, contentTitle, contentText, subText);
    }

    public void sendNotification(Context originalContext, String title, String text, String subText) {
        Context context = originalContext.getApplicationContext();
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, new Intent(context, MainActivity.class), 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true); //the notification will automatically go away.
        builder.setLargeIcon(BitmapFactory.decodeResource(Resources.getSystem(), R.mipmap.ic_launcher));
        if(title != null) builder.setContentTitle(title);
        if(text != null) builder.setContentText(text);
        if(subText != null) builder.setSubText(subText);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
        try {
            playUserDefaultRingtone(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playUserDefaultRingtone(Context applicationContext) throws  Exception{
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(applicationContext, notification);
        r.play();
    }
}
