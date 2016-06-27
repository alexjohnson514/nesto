package com.example.td_advert.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.td_advert.LauncharScreen;
import com.example.td_advert.util.UpdaterUtil;
import com.example.td_advert.util.Util;
import com.example.td_advert.web.WebTask;
import com.example.td_advert.web.delegate.WebTaskDelegate;

public class ConnectivityReciever extends BroadcastReceiver {
	private static boolean connected = false;
	private static int count = 0;

	@Override
	public void onReceive(final Context context, Intent intent) {
		final Intent _intent = intent;

        Util.appendLog("Device automatically connect to Wi-Fi");

		boolean isConnected = LauncharScreen.isConnectingToInternet(context);
		if (isConnected) {

			final Context ctx = context;
			WebTask task = new WebTask("http://www.t-advert.com",new WebTaskDelegate() {

						@Override
						public void onError(Exception e) {
                            Util.appendLog("Cannot connect to t-advert.com, try again in 3 seconds");
							count++;
							if (count < 3){
								try {
									Thread.sleep(3000);
								} catch (InterruptedException e1) {
								}
								onReceive(ctx, _intent);
							}
							else{
								count = 0;
							}
						}

						@Override
						public void synchronousPostExecute(String sr) {
							if (!connected) {
                                Util.appendLog("Successfully connected to t-advert.com, calling td_advert.LauncharScreen with welcome_screen off");
                                new UpdaterUtil(context);
								connected = true;
								Toast.makeText(ctx, "Connected",
										Toast.LENGTH_SHORT).show();
								Intent i = new Intent();
								i.setClassName("com.example.td_advert", "com.example.td_advert.LauncharScreen");
								i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.putExtra("welcome_screen", false);
                                ctx.startActivity(i);
//                                Intent i = new Intent();
//                                i.setClassName("com.example.td_advert", "com.example.td_advert.BlackActivity");
//                                i.setAction("com.tadvert.app.battery_connected");
//                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//								ctx.startActivity(i);
							}
						}

					});
			if (!connected) {
				task.execute();
			}

		} else {
			connected = false;
			// Toast.makeText(context, "Not Connected",
			// Toast.LENGTH_SHORT).show();
		}

	}
}
