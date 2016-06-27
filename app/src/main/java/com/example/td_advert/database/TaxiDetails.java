package com.example.td_advert.database;

import android.content.Context;

import com.example.td_advert.bean.AppState;
import com.example.td_advert.constant.TadvertConstants;
import com.example.td_advert.util.Util;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Narongdej on 3/15/2015.
 */
public class TaxiDetails {
    final AsyncHttpClient client;
    Context ctx;
    String path;

    public TaxiDetails(Context context){
        client = new AsyncHttpClient();
        ctx = context;
    }

    public void getLatestStationNames(){
        client.get(TadvertConstants.url + "getStationNames", new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                TestAdapter mDbHelper = new TestAdapter(ctx);
                mDbHelper.createDatabase();
                mDbHelper.open();

                mDbHelper.removeAllStationName();

                JSONArray c = null;
                try {
                    c = response.getJSONArray("data");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONObject obj = null;
                for (int i = 0 ; i < c.length(); i++) {
                    try {
                        obj = c.getJSONObject(i);
                        mDbHelper.saveTaxiStation(obj.getInt("id"), obj.getString("stationName"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                mDbHelper.close();
            }
        });
    }

    public void syncDetails() {
        if(getUpdateStatus() == 1){
            RequestParams params = new RequestParams();
            params.put("meid", AppState.getInstance().getMeid());
            params.put("taxiNo", getTaxiNumber());
            params.put("stationNumber", getStationNumber());

            client.post(TadvertConstants.url + "setTaxiDetails", params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    TestAdapter mDbHelper = new TestAdapter(ctx);
                    mDbHelper.createDatabase();
                    mDbHelper.open();
                    mDbHelper.setUpdateStatus(0);
                    mDbHelper.close();
                }
            });
        } else {
            client.get(TadvertConstants.url + "getTaxiDetails&meid=" + AppState.getInstance().getMeid(), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    TestAdapter mDbHelper = new TestAdapter(ctx);
                    mDbHelper.createDatabase();
                    mDbHelper.open();

                    try {
                        mDbHelper.SaveTaxiNo(response.getString("taxiNo"));
                        mDbHelper.SaveTaxiStationID(response.getInt("stationId"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    mDbHelper.close();
                }
            });
        }
    }

    private String getTaxiNumber() {
        TestAdapter mDbHelper = new TestAdapter(ctx);
        mDbHelper.createDatabase();
        mDbHelper.open();

        String taxiNo = mDbHelper.getTaxiNo();
        // AppState.getInstance().setTaxiNumber(taxiNo);
        mDbHelper.close();
        Util.appendLog("Taxi No: " + taxiNo);

        return taxiNo;
    }

    private Integer getStationNumber() {
        TestAdapter mDbHelper = new TestAdapter(ctx);
        mDbHelper.createDatabase();
        mDbHelper.open();

        Integer stationID = mDbHelper.getTaxiStationID();
        // AppState.getInstance().setTaxiNumber(taxiNo);
        mDbHelper.close();

        return stationID;
    }

    private Integer getUpdateStatus() {
        TestAdapter mDbHelper = new TestAdapter(ctx);
        mDbHelper.createDatabase();
        mDbHelper.open();

        Integer updateStatus = mDbHelper.getUpdateStatus();
        // AppState.getInstance().setTaxiNumber(taxiNo);
        mDbHelper.close();
        Util.appendLog("Update Status: " + updateStatus);

        return updateStatus;
    }
}
