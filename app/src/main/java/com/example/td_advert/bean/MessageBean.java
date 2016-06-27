package com.example.td_advert.bean;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Narongdej on 20/1/2558.
 */
public class MessageBean {

    public JSONObject getMessage() {
        return message;
    }

    public void setMessage(JSONObject message) {
        this.message = message;
    }

    private JSONObject message;

    public ArrayList<NameValuePair> toPostParams() {

        JSONArray messageArray = new JSONArray();
        messageArray.put(message);

        ArrayList<NameValuePair> messageList = new ArrayList<NameValuePair>();
        messageList.add(new BasicNameValuePair("messageList", messageArray.toString()));

        return messageList;
    }
}
