package com.example.td_advert;

import java.util.HashMap;

import android.app.Application;

import com.google.analytics.tracking.android.Tracker;

public class TAdvertApp extends Application {

	// The following line should be changed to include the correct property id.
	private static final String PROPERTY_ID = "UA-50498015-1";

	public static int GENERAL_TRACKER = 0;

	public enum TrackerName {
		APP_TRACKER, // Tracker used only in this app.
		GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg:
						// roll-up tracking.
		ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a
							// company.
	}

	HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

	public TAdvertApp() {
		super();
		
	}

	synchronized Tracker getTracker(TrackerName trackerId) {
		// if (!mTrackers.containsKey(trackerId)) {
		//
		// GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
		// Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics
		// .newTracker(PROPERTY_ID)
		// : (trackerId == TrackerName.GLOBAL_TRACKER) ? analytics
		// .newTracker(R.xml.global_tracker) : analytics
		// .newTracker(R.xml.ecommerce_tracker);
		// mTrackers.put(trackerId, t);
		//
		// }
		return mTrackers.get(trackerId);
	}
}
