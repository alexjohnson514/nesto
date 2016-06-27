package com.example.td_advert;

import java.util.Timer;

import com.example.td_advert.constant.TadvertConstants;
import com.example.td_advert.task.AbstractTask;
import com.example.td_advert.task.SchedulerTask;
import com.example.td_advert.util.Util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

public class ScreenSaverActivity extends BaseActivity implements OnClickListener{

	private RelativeLayout closeArea = null;
	private int clickCounter = 0;
	
	private Timer activityTimer = new Timer();
	private SchedulerTask schedulerTask = new SchedulerTask();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        Util.appendLog("td_advert.ScreenSaverActivity onCreate");
        Util.logFile = null;

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.screensaver);
		
		closeArea = (RelativeLayout)findViewById(R.id.closeArea);
		closeArea.setOnClickListener(this);

        PowerManager manager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        final PowerManager.WakeLock wl = manager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "My Tag");
        wl.acquire();

        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                wl.release();
            }
        }, 120000);


		activityTimer.scheduleAtFixedRate(schedulerTask, 3, 1000);
		schedulerTask.registerTask(new AbstractTask(ScreenSaverActivity.this, 1) {
			private int closeCount = -1;

			@Override
			protected void execute() {
				if (clickCounter > 0) {
					if (clickCounter == closeCount) {
						clickCounter = 0;
						closeCount = -1;
					} else if (closeCount >= -1) {
						closeCount = clickCounter;
					}
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		clickCounter++;
		if (clickCounter >= 10) {
			clickCounter = 0;
			Intent intent = new Intent(TadvertConstants.CLOSE_INTENT);
			this.sendBroadcast(intent);
			finish();
		}
	}
	
}
