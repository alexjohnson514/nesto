package com.example.td_advert.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import com.example.td_advert.util.Util;

public class BootReciever extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent arg1) {
		IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		Intent batteryStatus = context.registerReceiver(null, ifilter);
		int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
		boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ;
		
		Intent i = new Intent();
		i.setClassName("com.example.td_advert", "com.example.td_advert.BlackActivity");
		
		if (isCharging){
			i.setAction("com.tadvert.app.battery_connected");
		}else{
			i.setAction("com.tadvert.app.battery_disconnected");
		}

        Util.appendLog("Boot receiver get called, charging status " + isCharging);

        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(i);

	}

}
