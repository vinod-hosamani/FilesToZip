package com.bridgelabz.alarmmanager;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;


/**
 * Created by bridgeit on 9/6/17.
 */
public class NotificationTrigger extends IntentService
{
    Context context;
    private static final String ALARM = "your.package.ALARM";

    public NotificationTrigger() {
        super("NotificationTrigger");
    }

    public static void starNotification(Context context)
    {
        Intent intent = new Intent(context, NotificationTrigger.class);
        intent.setAction(ALARM);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent)
    {
        if (intent != null)
        {
            final String action = intent.getAction();
            if (ALARM.equals(action))
            {
                handleAlarm();
            }
        }
    }

    private void handleAlarm() {

        PendingIntent notificationIntent = PendingIntent.getActivity(this, 0, new Intent(this, CoolClassToOpenOnAlarm.class), 0);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.cool_icon)
                .setContentTitle("title")
                .setTicker(context.getString(R.string.app_name))
                .setContentText("wake up user!");

        notificationBuilder.setContentIntent(notificationIntent);

        notificationBuilder.setDefaults(NotificationCompat.DEFAULT_VIBRATE);

        notificationBuilder.setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }


}