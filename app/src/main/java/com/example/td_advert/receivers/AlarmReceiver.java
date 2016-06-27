package com.example.td_advert.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import com.example.td_advert.util.PrelaunchSetup;
import com.example.td_advert.util.Util;

/**
 * Created by Narongdej on 3/20/2015.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context arg0, Intent arg1) {
        // For our recurring task, we'll just display a message
        PrelaunchSetup prelaunchSetup = new PrelaunchSetup(arg0);
        prelaunchSetup.setWifi();

        Util.appendLog("Try connecting wifi, 5 minute routine");
        Util.appendLog("Battery percentage: " + getBatteryLevel(arg0));
    }

    public float getBatteryLevel(Context ctx) {
        Intent batteryIntent = ctx.getApplicationContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        // Error checking that probably isn't needed but I added just in case.
        if(level == -1 || scale == -1) {
            return 50.0f;
        }

        return ((float)level / (float)scale) * 100.0f;
    }

}