package com.example.td_advert;

import java.util.HashMap;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.example.td_advert.analytics.Analytics;
import com.example.td_advert.database.TestAdapter;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;

public class BaseActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onStart() {
		super.onStart();

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.ACTION_POWER_DISCONNECTED");
        filter.addAction("android.intent.action.ACTION_POWER_CONNECTED");
        filter.addAction("android.intent.action.ACTION_BATTERY_CHANGED");
        registerReceiver(batteryBroadcastReceiver, filter);
	}

	@Override
	protected void onStop() {
		super.onStop();
		unregisterReceiver(batteryBroadcastReceiver);
	}

	protected void saveAnalytic(String eventAction, long eventValue) {
		TestAdapter dbAdapter = new TestAdapter(this);
		dbAdapter.createDatabase();
		dbAdapter.open();
		dbAdapter.saveAnalyticsEvent(eventAction, eventValue);

		Log.e("Analytics", "Analytic Saved=== eventAction: " + eventAction
				+ ", eventValue: " + eventValue);

		dbAdapter.close();
	}

	protected void saveAnalytic(String eventAction, long eventValue,HashMap<String, String> extras) {
		TestAdapter dbAdapter = new TestAdapter(this);
		dbAdapter.createDatabase();
		dbAdapter.open();
		dbAdapter.saveAnalyticsEvent(eventAction, eventValue,extras);

		Log.e("Analytics", "Analytic Saved=== eventAction: " + eventAction
				+ ", eventValue: " + eventValue);

		dbAdapter.close();
	}
	private BroadcastReceiver batteryBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.contains("ACTION_POWER_DISCONNECTED")) {
				Intent i = new Intent();
				i.setClassName("com.example.td_advert",
						"com.example.td_advert.ScreenSaverActivity");
				i.setAction("com.tadvert.app.battery_disconnected");
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(i);

				Analytics.getInstance().rideEnded();

				EasyTracker.getInstance(BaseActivity.this).send(
						MapBuilder.createEvent(Analytics.RIDE_EVENT_CATEGORY,
								Analytics.RIDE_DURATION_EVENT,
								Analytics.RIDE_DURATION_EVENT,
								Analytics.getInstance().getRideDuration())
								.build());

				saveAnalytic(Analytics.RIDE_DURATION_EVENT, Analytics
						.getInstance().getRideDuration());

				EasyTracker.getInstance(BaseActivity.this).send(
						MapBuilder.createEvent(
								Analytics.RIDE_EVENT_CATEGORY,
								Analytics.CLICK_PER_RIDE_EVENT,
								Analytics.CLICK_PER_RIDE_EVENT,
								Long.valueOf(Analytics.getInstance().getRideClicks())).build());

				saveAnalytic(Analytics.CLICK_PER_RIDE_EVENT, Analytics.getInstance().getRideClicks());

				finish();

			}
		}
	};
}
