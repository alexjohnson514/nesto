package com.example.td_advert;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.td_advert.analytics.Analytics;
import com.example.td_advert.bean.AppState;
import com.example.td_advert.constant.TadvertConstants;
import com.example.td_advert.receivers.AlarmReceiver;
import com.example.td_advert.util.BrightnessManager;
import com.example.td_advert.util.PrelaunchSetup;
import com.example.td_advert.util.UpdaterUtil;
import com.example.td_advert.util.Util;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;

import java.util.ArrayList;
import java.util.Calendar;

public class BlackActivity extends BaseActivity implements OnClickListener {

    private PrelaunchSetup prelaunchSetup;
	BroadcastReceiver closeBroadcastReceive;
    Calendar cal;
    int cTime;

    private PendingIntent pendingIntent;
    private AlarmManager manager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        Util.appendLog("td_advert.BlackActivity onCreate");

        prelaunchSetup = new PrelaunchSetup(getApplicationContext());
        prelaunchSetup.runEverything();

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		setContentView(R.layout.black_screen);

        String meid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        AppState.getInstance().setMeid(meid);

		ImageView awake_logo = (ImageView) findViewById(R.id.awake_logo);
		awake_logo.setOnClickListener(this);
		Intent i = getIntent();

        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);

        manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        int interval = 1000 * 60 * 5;

        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);

		if (i.getAction() == null || i.getAction().equals("android.intent.action.MAIN") || i.getAction().equals("com.tadvert.app.battery_connected")) {
			startLaunchar(true, true);
		} else if (i.getAction().equals("com.tadvert.app.battery_disconnected")) {
			startScreenSaver();
		}

		closeBroadcastReceive = new BroadcastReceiver()
		{
			public void onReceive(Context arg0, Intent arg1)
			{
				Intent startMain = new Intent(Intent.ACTION_MAIN);
				startMain.addCategory(Intent.CATEGORY_HOME);
				startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startMain.putExtra("welcome_screen", true);
				startActivity(startMain);
				BlackActivity.this.finish();
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		};
		registerReceiver(closeBroadcastReceive, new IntentFilter(TadvertConstants.CLOSE_INTENT));

	}


	/*private class CloseBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {

			Intent startMain = new Intent(Intent.ACTION_MAIN);
			startMain.addCategory(Intent.CATEGORY_HOME);
			startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startMain.putExtra("welcome_screen", true);
			startActivity(startMain);
			BlackActivity.this.finish();
			android.os.Process.killProcess(android.os.Process.myPid());
		}

	}*/

	@Override
	protected void onStart() {
		super.onStart();
        new UpdaterUtil(this);
        prelaunchSetup.setWifi();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub

		try{
			if(closeBroadcastReceive != null)
				unregisterReceiver(closeBroadcastReceive);
		}catch(Exception e)
		{

		}
		super.onDestroy();

	}

	private void startScreenSaver() {
		Intent i = new Intent();
		i.setClassName("com.example.td_advert", "com.example.td_advert.ScreenSaverActivity");
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Util.appendLog("Calling td_advert.ScreenSaverActivity");
		startActivity(i);
	}

    private void startLaunchar(boolean logAna){
        startLaunchar(logAna, false);
    }

	private void startLaunchar(boolean logAnalytics, boolean welcomeScreen) {
		Intent i = new Intent();
		i.setClassName("com.example.td_advert","com.example.td_advert.LauncharScreen");

        if(welcomeScreen)
            i.putExtra("welcome_screen", true);

        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Util.appendLog("Calling td_advert.LauncharScreen with welcome_screen = " + welcomeScreen);

        LauncharScreen.skipVideoLoop = true;

        cal = Calendar.getInstance();
        cTime = cal.get(Calendar.HOUR_OF_DAY);

        if(cTime >= 18 || cTime < 6){
            BrightnessManager.getInstance().setBrightnessPercentage(30);
            BrightnessManager.getInstance().setBrightness(getContentResolver());
        } else {
            BrightnessManager.getInstance().setBrightnessPercentage(80);
            BrightnessManager.getInstance().setBrightness(getContentResolver());
        }

		startActivity(i);

		if (logAnalytics) {
			Analytics.getInstance().rideStarted();
			EasyTracker.getInstance(BlackActivity.this).send(
					MapBuilder.createEvent(Analytics.RIDE_EVENT_CATEGORY,
							Analytics.RIDE_START_EVENT,
							Analytics.RIDE_START_EVENT, 1l).build());

			saveAnalytic(Analytics.RIDE_START_EVENT, 1l);

			/*BrightnessManager.getInstance().setBrightnessPercentage(BrightnessReceiver.brightnessValue);
			BrightnessManager.getInstance().setBrightness(getContentResolver());*/
		}
	}

	protected void onNewIntent(Intent intent) {

		super.onNewIntent(intent);
		String action = intent.getAction();
		if (action != null && action.equals("com.tadvert.app.battery_connected")) {
            CompanyScreen.promotionShown = new ArrayList<String>();
            CompanyScreen.uniqueAnalytics = new ArrayList<String>();
			startLaunchar(true, true);
		}
	}

	@Override
	public void onClick(View v) {
		LauncharScreen.skipVideoLoop = true;
		startLaunchar(false);
	}

	@Override
	protected void onPause() {
		super.onPause();
		overridePendingTransition(R.anim.screen_fade_in_animation,
				R.anim.screen_fade_out_animation);
	}
}
