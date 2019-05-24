package com.example.curriculumtracker;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.JobIntentService;
import android.support.v4.app.NotificationManagerCompat;



public class MyNewIntentService extends JobIntentService {

    private static final int NOTIFICATION_ID = 3;

    public MyNewIntentService(){

    }
    @Override
    protected void onHandleWork(Intent intent) {
        Notification.Builder builder = new Notification.Builder(this);
            builder.setContentTitle("New Intent Service");
            builder.setContentText("This is the body");
            builder.setSmallIcon(R.drawable.ic_launcher_background);

        Intent notifyIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 2, notifyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        Notification notificationCompat = builder.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(NOTIFICATION_ID, notificationCompat);

    }
}
