package com.example.td_advert.bean;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserEmail {
	private String companyName;
	private String emailAddress;

	public UserEmail() {
		super();
	}

	public UserEmail(String companyName, String emailAddress) {
		super();
		this.companyName = companyName;
		this.emailAddress = emailAddress;
	}

	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public ArrayList<NameValuePair> toPostParams() {

        JSONObject jsonEmail = new JSONObject();
        try {
            jsonEmail.put("companyName", companyName);
            jsonEmail.put("emailAddress", emailAddress);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray emailArray = new JSONArray();
        emailArray.put(jsonEmail);

        ArrayList<NameValuePair> emailList = new ArrayList<NameValuePair>();
        emailList.add(new BasicNameValuePair("userEmailList", emailArray.toString()));

        return emailList;
	}

}
