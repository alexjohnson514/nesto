package com.example.td_advert.database;

import android.content.Context;

import com.example.td_advert.LauncharScreen;
import com.example.td_advert.bean.AppState;
import com.example.td_advert.bean.MessageBean;
import com.example.td_advert.constant.TadvertConstants;
import com.example.td_advert.util.Util;
import com.example.td_advert.web.WebTask;
import com.example.td_advert.web.delegate.WebTaskDelegate;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Narongdej on 20/1/2558.
 *
 * This class basically store any JSON and send it to the server
 *
 */
public class MessageSync {

    Context mContext;

    public MessageSync(Context context){
        mContext = context;
    }


    public void storeMessage(JSONObject json){

        try {
            json.put("meid", AppState.getInstance().getMeid());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final MessageBean dto = new MessageBean();
        dto.setMessage(json);

        Util.appendLog("MessageSync store a message");

        if (LauncharScreen.isConnectingToInternet(mContext)) {
            String url = TadvertConstants.url + "sendMessages";
            WebTask task = new WebTask(url, dto.toPostParams(),
                    new WebTaskDelegate() {

                        @Override
                        public void onError(Exception e) {
                            saveMessageToDB(dto);
                        }

                    });
            task.execute();
        } else {
            saveMessageToDB(dto);
        }

    }

    public void syncMessage(){

        if (LauncharScreen.isConnectingToInternet(mContext)) {

            Util.appendLog("Sending MessageSync Message to the server");

            TestAdapter mDbHelper = new TestAdapter(mContext);
            mDbHelper.createDatabase();
            mDbHelper.open();

            final JSONArray messageList = mDbHelper.getMessages();
            mDbHelper.close();

            if (messageList != null && messageList.length() > 0) {
                Util.appendLog(messageList.length() + " MessageSync found, sending it to the server");

                ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("messageList", messageList.toString()));

                String url = TadvertConstants.url + "sendMessages";
                WebTask task = new WebTask(url, params, new WebTaskDelegate() {

                    @Override
                    public void synchronousPostExecute(String sr) {

                        Util.appendLog("MessageSync are sent successfully, remove all feedbacks from the local database");

                        TestAdapter mDbHelper = new TestAdapter(mContext);
                        mDbHelper.createDatabase();
                        mDbHelper.open();

                        mDbHelper.removeAllMessages();
                        mDbHelper.close();
                    }

                });
                task.execute();
            }
        }
    }

    private void saveMessageToDB(MessageBean dto){
        TestAdapter mDbHelper = new TestAdapter(mContext);
        mDbHelper.createDatabase();
        mDbHelper.open();
        mDbHelper.SaveMessage(dto);
        mDbHelper.close();
    }
}
