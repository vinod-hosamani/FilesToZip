package com.bridgelabz.alarmmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by bridgeit on 9/6/17.
 */
public class DeviceBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
                SetAlarm.alarmSetter alarmSetter = new SetAlarm.alarmSetter(context);
            }
        }
    }
}