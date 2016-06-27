package com.example.td_advert.exception;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;


public class TAConnectivityManager {

	public enum CONNECTTION_STRENGTH{BAD, GOOD, VERY_GOOD, EXCELLENT, UN_AVAILABLE, NON_WIFI, FAIR};

	public static CONNECTTION_STRENGTH getConnectionStrength(Context ctx) {
		try {
			ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo ni = cm.getActiveNetworkInfo();
			if (ni != null && ni.getTypeName().equalsIgnoreCase("wifi") ) {
				WifiManager wifiMngr = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
				if (wifiMngr.isWifiEnabled()) {

					WifiInfo wifiInfo = wifiMngr.getConnectionInfo();
					int rssi = (wifiInfo.getRssi());
					int speed = WifiManager.calculateSignalLevel(rssi, 4);

					switch (speed) {
					case 1:
						return CONNECTTION_STRENGTH.BAD;
					case 2:
						return CONNECTTION_STRENGTH.GOOD;
					case 3:
						return CONNECTTION_STRENGTH.VERY_GOOD;
					case 4:
						return CONNECTTION_STRENGTH.EXCELLENT;
					default:
						return CONNECTTION_STRENGTH.FAIR;
					}
				} else {
					return CONNECTTION_STRENGTH.UN_AVAILABLE;
				}
			} else {
				return CONNECTTION_STRENGTH.NON_WIFI;
			}
		} catch (Exception e) {
			return CONNECTTION_STRENGTH.BAD;
		}
	}
	
	@SuppressWarnings("finally")
	public static String getConnectionType(Context ctx) {
		String connectionType = "";
		try {
			ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo ni = cm.getActiveNetworkInfo();
			if (ni != null) {
				connectionType = ni.getTypeName();
			}
		} catch (Exception e) {
			
		} finally {
			return connectionType;
		}
	}
}