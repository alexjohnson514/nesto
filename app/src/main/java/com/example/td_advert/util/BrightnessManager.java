package com.example.td_advert.util;

import android.content.ContentResolver;

public class BrightnessManager {

	private static final int MAX_BRIGHTNESS = 255;
	private static final int BRIGHTNESS_PERCENTAGE_STEP = 20;
	private int brightnessPercentage;
	private static BrightnessManager instance = new BrightnessManager();

	public static BrightnessManager getInstance() {
		return instance;
	}

	private BrightnessManager() {
		brightnessPercentage = 80;
	}

	public int getBrightnessPercentage() {
		return brightnessPercentage;
	}

	public void setBrightnessPercentage(int brightnessPercentage) {
		this.brightnessPercentage = brightnessPercentage;
	}

	public void brightnessStepUp(ContentResolver contentResolver) {
        if (brightnessPercentage == 30)
            brightnessPercentage = 40;

		brightnessPercentage = (brightnessPercentage + BRIGHTNESS_PERCENTAGE_STEP) % 100;
		if (brightnessPercentage == 0)
			brightnessPercentage = 100;

		setBrightness(contentResolver);
	}

	public void setBrightness(ContentResolver contentResolver) {
        Util.appendLog("Set brightness to " + brightnessPercentage + "%");
		android.provider.Settings.System.putInt(contentResolver,
				android.provider.Settings.System.SCREEN_BRIGHTNESS,
				BrightnessManager.MAX_BRIGHTNESS * brightnessPercentage / 100);
	}
}
