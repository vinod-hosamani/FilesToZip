package com.bridgelabz.alarmmanager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/**
 * Created by bridgeit on 9/6/17.
 */
public class SetAlarm {

    public void alarmSetter(Context context) {

        //This will set the alarm time to be at the 14:30
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 14);
        calendar.set(Calendar.MINUTE, 30);

        //This intent, will execute the AlarmBroadcaster when the alarm is triggered
        Intent alertIntent = new Intent(context, AlarmBroadcaster.class);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, alertIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

    }

    public class alarmSetter {
        public alarmSetter(Context context) {
        }
    }
}