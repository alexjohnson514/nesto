package com.example.td_advert.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.td_advert.util.Util;

public class BatteryReciever extends BroadcastReceiver {

    int level, scale;

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();

		if (action.contains("ACTION_POWER_CONNECTED")) {
			Intent i = new Intent();
			i.setClassName("com.example.td_advert", "com.example.td_advert.BlackActivity");
            i.putExtra("welcome_screen", true);
			i.setAction("com.tadvert.app.battery_connected");
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

            Util.appendLog("Battery connected, starting BlackActivity welcome_screen");

			context.startActivity(i);
		}
	}

}
