package com.example.td_advert.bean;

import java.util.HashMap;

public class AnalyticEvent {

	private String eventAction;
	private String eventDate;
	private long eventValue;
	private String extras;
	private HashMap<String, String> extrasMap = new HashMap<String, String>();

	public AnalyticEvent(String eventAction, String eventDate, long eventValue) {
		super();
		this.eventAction = eventAction;
		this.eventDate = eventDate;
		this.eventValue = eventValue;
	}

	public AnalyticEvent(String eventAction, String eventDate, long eventValue,
			HashMap<String, String> extrasMap) {
		super();
		this.eventAction = eventAction;
		this.eventDate = eventDate;
		this.eventValue = eventValue;
		this.extrasMap = extrasMap;
	}

	public String getEventAction() {
		return eventAction;
	}

	public void setEventAction(String eventAction) {
		this.eventAction = eventAction;
	}

	public long getEventValue() {
		return eventValue;
	}

	public void setEventValue(long eventValue) {
		this.eventValue = eventValue;
	}

	public String getEventDate() {
		return eventDate;
	}

	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}

	public String getExtras() {
		return extras;
	}

	public void setExtras(String extras) {
		this.extras = extras;
	}

	public HashMap<String, String> getExtrasMap() {
		return extrasMap;
	}

	public void setExtrasMap(HashMap<String, String> extrasMap) {
		this.extrasMap = extrasMap;
	}

}
