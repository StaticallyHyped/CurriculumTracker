package com.example.curriculumtracker;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class MyReceiver extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 0;
    public static final String PRIMARY_CHANNEL_ID = "prime_note_channel";
    private NotificationManager mNotificationManager;

    public MyReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent){
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        deliverNotification(context);


    }
    private void deliverNotification(Context context){
        Intent contentIntent = new Intent(context, SetCourseAlertActivity.class);
        PendingIntent contentPendingIntent = PendingIntent.getActivity(context, NOTIFICATION_ID, contentIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Course Starting Alert")
                .setContentText("You have a course starting within 24 hours")
                .setContentIntent(contentPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
        mNotificationManager.notify(NOTIFICATION_ID, builder.build());

    }
}
