package com.example.td_advert.exception;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.os.Debug;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import com.example.td_advert.bean.AppState;

public class PhoneStatus {
	private String version = "";
    private Integer versionCode = 0;
	private String width = "";
	private String height = "";
	private String phoneType = "";
	private String gameVersion = "";
	private String device = "";
	private String os = "";
	private String udid = "";
	private String network = "";
	private String networkStrength = "";
	private String memoryInfoNativePss = "";
	private String memoryInfoNativeSharedDirty = "";
	private String memoryInfoOtherPss = "";
	private String memoryInfoTotalSharedDirty = "";
	private String nativeHeapSize = "";
	private String nativeHeapAllocateSize = "";
	private String nativeHeapFreeSize = "";
	private String threadAllocCount = "";
	private String threadAllocSize = "";
	private String timestamp = "";
	private String runtimeTotalMemory = "";
	private String runtimeFreeMemory = "";
	private String runtimeMaxMemory = "";
	private String activityManagerAvailMem = "";
	private String activityManagerLowMemory = "";
	private String activityManagerThreshold = "";

	private int memoryClass;

	private PhoneStatus() {

	}

	public static PhoneStatus fetchStatus(Context ctx) {
		PhoneStatus phoneStatus = new PhoneStatus();
		try {
			DisplayMetrics displaymetrics = new DisplayMetrics();
			((Activity) ctx).getWindowManager().getDefaultDisplay()
					.getMetrics(displaymetrics);
			phoneStatus.height = "" + displaymetrics.heightPixels;
			phoneStatus.width = "" + displaymetrics.widthPixels;

			TelephonyManager telephonyManager = (TelephonyManager) ctx
					.getSystemService(Context.TELEPHONY_SERVICE);

			if (telephonyManager != null) {
				phoneStatus.phoneType = telephonyManager.getPhoneType() + "";
			}

			phoneStatus.device = Build.MANUFACTURER + " " + Build.MODEL;
			phoneStatus.os = Build.VERSION.RELEASE;

			if (telephonyManager != null) {
				phoneStatus.udid = Secure.getString(
						((ContextWrapper) ctx).getContentResolver(),
						Secure.ANDROID_ID);
			}

			Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
			Debug.getMemoryInfo(memoryInfo);

			phoneStatus.network = TAConnectivityManager.getConnectionType(ctx);
			phoneStatus.networkStrength = TAConnectivityManager
					.getConnectionStrength(ctx) + "";

			phoneStatus.memoryInfoNativePss = memoryInfo.nativePss + "";
			phoneStatus.memoryInfoNativeSharedDirty = memoryInfo.nativeSharedDirty
					+ "";
			phoneStatus.memoryInfoOtherPss = memoryInfo.otherPss + "";
			phoneStatus.memoryInfoTotalSharedDirty = memoryInfo
					.getTotalSharedDirty() + "";
			phoneStatus.nativeHeapSize = Debug.getNativeHeapSize() + "";
			phoneStatus.nativeHeapAllocateSize = Debug
					.getNativeHeapAllocatedSize() + "";
			phoneStatus.nativeHeapFreeSize = Debug.getNativeHeapFreeSize() + "";
			phoneStatus.threadAllocCount = Debug.getThreadAllocCount() + "";
			phoneStatus.threadAllocSize = Debug.getThreadAllocSize() + "";

			phoneStatus.runtimeFreeMemory = java.lang.Runtime.getRuntime()
					.freeMemory() + "";
			phoneStatus.runtimeMaxMemory = java.lang.Runtime.getRuntime()
					.maxMemory() + "";
			phoneStatus.runtimeTotalMemory = java.lang.Runtime.getRuntime()
					.totalMemory() + "";

			ActivityManager activityManager = (ActivityManager) ctx
					.getSystemService(Context.ACTIVITY_SERVICE);
			ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
			try {
				activityManager.getMemoryInfo(memInfo);
				phoneStatus.memoryClass = activityManager.getMemoryClass();
			} catch (Exception e) {
			}
			phoneStatus.activityManagerAvailMem = memInfo.availMem + "";
			phoneStatus.activityManagerLowMemory = memInfo.lowMemory + "";
			phoneStatus.activityManagerThreshold = memInfo.threshold + "";

            phoneStatus.version = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0).versionName;
            phoneStatus.versionCode = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0).versionCode;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return phoneStatus;
	}

	public String getPhoneType() {
		return phoneType;
	}

	public String getVersion() {
		return version;
	}

    public Integer getVersionCode() {
        return versionCode;
    }

	public String getGameVersion() {
		return gameVersion;
	}

	public String getDevice() {
		return device;
	}

	public String getOs() {
		return os;
	}

	public String getUdid() {
		return udid;
	}

	public String getNetwork() {
		return network;
	}

	public String getNetworkStrength() {
		return networkStrength;
	}

	public String getMemoryInfoNativePss() {
		return memoryInfoNativePss;
	}

	public String getMemoryInfoNativeSharedDirty() {
		return memoryInfoNativeSharedDirty;
	}

	public String getMemoryInfoOtherPss() {
		return memoryInfoOtherPss;
	}

	public String getMemoryInfoTotalSharedDirty() {
		return memoryInfoTotalSharedDirty;
	}

	public String getNativeHeapSize() {
		return nativeHeapSize;
	}

	public String getNativeHeapAllocateSize() {
		return nativeHeapAllocateSize;
	}

	public String getNativeHeapFreeSize() {
		return nativeHeapFreeSize;
	}

	public String getThreadAllocCount() {
		return threadAllocCount;
	}

	public String getThreadAllocSize() {
		return threadAllocSize;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public String getRuntimeTotalMemory() {
		return runtimeTotalMemory;
	}

	public String getRuntimeFreeMemory() {
		return runtimeFreeMemory;
	}

	public String getRuntimeMaxMemory() {
		return runtimeMaxMemory;
	}

	public String getActivityManagerAvailMem() {
		return activityManagerAvailMem;
	}

	public String getActivityManagerLowMemory() {
		return activityManagerLowMemory;
	}

	public String getActivityManagerThreshold() {
		return activityManagerThreshold;
	}

	@Override
	public String toString() {
		String message = "";
		try {
			message += "DIMENSIONS : " + width + " x " + height;
			message += ", DEVICE : " + Build.MANUFACTURER + " " + Build.MODEL;
			message += ", OS : " + Build.VERSION.RELEASE;
			message += ", VERSION : " + version;
			message += ", MEID : " + getUdid();
            message += ", TaxiNO :" + AppState.getInstance().getTaxiNumber();
            message += ", PHONE TYPE : " + getPhoneType();
			message += ", MEMORY CLASS: " + memoryClass;
			message += ", NETWORK: " + getNetwork();
			message += ", \nmemoryInfo.nativePss = " + getMemoryInfoNativePss();
			message += ", \nmemoryInfo.nativeSharedDirty = "
					+ getMemoryInfoNativeSharedDirty();
			message += ", \nmemoryInfo.otherPss = " + getMemoryInfoOtherPss();
			message += ", \nmemoryInfo.getTotalSharedDirty() = "
					+ getMemoryInfoTotalSharedDirty();
			message += ", \nNativeHeapAllocateSize = "
					+ getNativeHeapAllocateSize();
			message += ", \ngetNativeHeapFreeSize = " + getNativeHeapFreeSize();
			message += ", \ngetNativeHeapSize = " + getNativeHeapSize();
			message += ", \nRuntime Free Memory = " + getRuntimeFreeMemory();
			message += ", \nRuntime Max Memory = " + getRuntimeMaxMemory();
			message += ", \nRuntime Total Memory = " + getRuntimeTotalMemory();
			message += ", \nActivity Manager Threshold = "
					+ getActivityManagerThreshold();
			message += ", \nActivity Manager Low Memory = "
					+ getActivityManagerLowMemory();
			message += ", \nActivity Manager AvailMem = "
					+ getActivityManagerThreshold();
			message += ", \nCurrent Free Device Memory = "
					+ getCurrentFreeMemory();

			BufferedReader reader = null;
			try {
				Process process = Runtime.getRuntime()
						.exec("cat /proc/meminfo");
				reader = new BufferedReader(new InputStreamReader(
						process.getInputStream()));
				int read;
				char[] buffer = new char[4096];
				StringBuffer output = new StringBuffer();
				while ((read = reader.read(buffer)) > 0) {
					output.append(buffer, 0, read);
				}
				reader.close();

				// Waits for the command to finish.
				process.waitFor();

				message += "\n" + output.toString();

			} catch (Exception e) {
			} finally {
				if (reader != null) {
					reader.close();
				}
			}

		} catch (Exception e) {
		}
		return message;
	}

	public static float getCurrentFreeMemory() {

		BufferedReader reader = null;
		float freeMem = 0;
		float inactiveMem = 0;
		float actualMem = 0;
		try {
			Process process = Runtime.getRuntime().exec("cat /proc/meminfo");
			reader = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			int read;
			char[] buffer = new char[4096];
			StringBuffer output = new StringBuffer();
			while ((read = reader.read(buffer)) > 0) {
				output.append(buffer, 0, read);
			}
			reader.close();

			// Waits for the command to finish.
			process.waitFor();

			String[] tokens = output.toString().split("\n");
			String[] freeMemRow = tokens[1].split(":");
			String[] inactiveMemRow = tokens[6].split(":");
			String[] value = freeMemRow[1].trim().split(" ");
			String[] inactiveValue = inactiveMemRow[1].trim().split(" ");
			freeMem = Float.parseFloat(value[0]) / 1024;
			inactiveMem = Float.parseFloat(inactiveValue[0]) / 1024;

			actualMem = inactiveMem + freeMem;
		} catch (IOException e) {
		} catch (Exception e) {
		} finally {
			try {
				reader.close();
			} catch (Exception e) {
			}
		}
		return actualMem;

	}

}
