package com.example.td_advert.analytics;

import java.util.Timer;
import java.util.TimerTask;

public class Analytics {
	public static final String RIDE_EVENT_CATEGORY = "Ride Events";
	public static final String RIDE_START_EVENT = "Ride Start";
	public static final String RIDE_DURATION_EVENT = "Ride Duration V1";
	public static final String CLICK_PER_RIDE_EVENT = "Screen Clicks per Ride";
	public static final String COMPANY_SCREEN_DURATION_EVENT = "Company Screen Duration V1";
	public static final String COMPANY_SCREEN_TOUCH_EVENT = "Company Screen Touch";
	public static final String SCREEN_TOUCH_EVENT = "Screen Touch";
	
	private long secsElapsed = 0;
	
	private  long rideStartTime;
	private  long rideEndTime;

	private  long companyScreenStartTime;
	private  long companyScreenEndTime;
	private  int clicksPerRide = 0;
	private  int clicksPerCompany = 0;
	private  String currentCompany = "";

	private static Analytics instance= null;
	private Timer analyticsTimer = new Timer();
	
	public static Analytics getInstance(){
		if (instance == null){
			instance = new Analytics();
		}
		return instance;
	}
	
	private Analytics(){
		analyticsTimer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				secsElapsed++;
			}
		}, 10, 1000);
	}

	public long getSecsElapsed(){
		return secsElapsed;
	}
	
	public  void rideStarted() {
		rideStartTime = secsElapsed;
		rideEndTime = 0;
		clicksPerRide = 0;
	}

	public  void rideEnded() {
		rideEndTime = secsElapsed;
	}

	public  long getRideDuration() {
		return rideEndTime - rideStartTime;
	}
	public  int getRideClicks(){
		return clicksPerRide;
	}
	public  void screenClicked(){
		clicksPerRide++;
	}

	public  void companyScreenStarted(String companyName) {
		companyScreenStartTime = secsElapsed;
		currentCompany = companyName;
		clicksPerCompany = 0;
	}

	public  void companyScreenEnded() {
		companyScreenEndTime = secsElapsed;
	}

	public  long getCompanyScreenDuration() {
		return companyScreenEndTime - companyScreenStartTime;
	}

	public  String getCurrentCompany() {
		return currentCompany;
	}
	public  int getCompanyClicks(){
		return clicksPerCompany;
	}
	public  void companyClicked(){
		clicksPerCompany++;
	}

}
