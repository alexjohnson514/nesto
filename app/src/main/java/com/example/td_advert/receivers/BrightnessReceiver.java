package com.example.td_advert.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.td_advert.constant.TadvertConstants;
import com.example.td_advert.util.BrightnessManager;

public class BrightnessReceiver extends BroadcastReceiver {

	public static int brightnessValue= 80;
	@Override
	public void onReceive(Context context, Intent intent) {
//		AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//		PendingIntent morningTimePendingIntent = PendingIntent.getBroadcast(
//				context, 0, intent, 0);
//		manager.cancel(morningTimePendingIntent);
		
		String action = intent.getAction();
		
		if (action != null && !action.equals("")){
			
			if (action.equals(TadvertConstants.BRIGHTNESS_MORNING_TIME_INTENT)){
				brightnessValue = 80;
			}
			else if (action.equals(TadvertConstants.BRIGHTNESS_EVENING_TIME_INTENT)){
				brightnessValue = 40;
			}
			
			BrightnessManager.getInstance().setBrightnessPercentage(brightnessValue);
			BrightnessManager.getInstance().setBrightness(context.getContentResolver());
		}
//		Toast.makeText(context, intent.getAction(), Toast.LENGTH_SHORT).show();
	}

}
