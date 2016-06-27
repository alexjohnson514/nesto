package com.example.td_advert.bean;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Feedback {
	private String feedback_type;
	private String name;
	private String phoneNumber;
	private String feedbackText;
	private float rating;
	private String magazineName;

	public Feedback() {
	}

	public Feedback(String feedback_type, String name, String number,
			String message, float rating) {
		super();
		this.feedback_type = feedback_type;
		this.name = name;
		this.phoneNumber = number;
		this.feedbackText = message;
		this.rating = rating;
	}

	public Feedback(String feedback_type, String magazineName) {
		super();
		this.feedback_type = feedback_type;
		this.magazineName = magazineName;
	}

	public String getFeedback_type() {
		return feedback_type;
	}

	public void setFeedback_type(String feedback_type) {
		this.feedback_type = feedback_type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getFeedbackText() {
		return feedbackText;
	}

	public void setFeedbackText(String feedbackText) {
		this.feedbackText = feedbackText;
	}

	public float getRating() {
		return rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}

	public String getMagazineName() {
		return magazineName;
	}

	public void setMagazineName(String magazineName) {
		this.magazineName = magazineName;
	}

	public boolean isValid() {
		return (feedback_type != null && feedback_type.equals("MAGAZINE")
				&& magazineName != null && !magazineName.equals(""))
				|| !(name.equals("") && phoneNumber.equals("")
						&& feedbackText.equals("") && rating == 0.0f);
	}

	public ArrayList<NameValuePair> toPostParams() {
		ArrayList<NameValuePair> arrayList = new ArrayList<NameValuePair>();
		arrayList.add(new BasicNameValuePair("feedback_type", feedback_type));
		
		if (feedback_type != null && feedback_type.equals("MAGAZINE")) {
			arrayList.add(new BasicNameValuePair("selectedMagazine",
					magazineName));
		}

        JSONObject jsonFeedbacks = new JSONObject();
        try {
            jsonFeedbacks.put("name", name);
            jsonFeedbacks.put("phoneNumber", phoneNumber);
            jsonFeedbacks.put("feedbackText", feedbackText);
            jsonFeedbacks.put("rating", Float.toString(rating));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray feedbackArray = new JSONArray();
        feedbackArray.put(jsonFeedbacks);

        ArrayList<NameValuePair> feedbackList = new ArrayList<NameValuePair>();
        feedbackList.add(new BasicNameValuePair("feedbacksList", feedbackArray.toString()));

		return feedbackList;
	}
}
