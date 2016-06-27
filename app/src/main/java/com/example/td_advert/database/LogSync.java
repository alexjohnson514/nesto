package com.example.td_advert.database;

import android.os.Environment;
import android.util.Log;

import com.example.td_advert.bean.AppState;
import com.example.td_advert.constant.TadvertConstants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Narongdej on 3/8/2015.
 */
public class LogSync {

    final AsyncHttpClient client;
    String path;

    public LogSync(){
        path = Environment.getExternalStorageDirectory().toString()+"/Nesto/log";
        client = new AsyncHttpClient();
    }

    public void syncLogFile(){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String currentDate = sdf.format(new Date());

        File f = new File(path);
        final File file[] = f.listFiles();
        Log.d("Files", "Size: "+ file.length);
        for (int i=0; i < file.length; i++)
        {
            if(!file[i].getName().split("_")[1].replace(".txt", "").equals(currentDate)) {
                Log.d("Zenyai", file[i].getName().split("_")[1].replace(".txt", ""));
                final int finalI = i;
                client.get(TadvertConstants.url + "checkLogFile&meid=" + AppState.getInstance().getMeid() + "&logname=" + file[i].getName(), new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                        try {
                            if (response.getBoolean("haveFile") == false) {
                                uploadFile(file[finalI]);
                            } else {
                                file[finalI].delete();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    private void uploadFile(File file){
        RequestParams params = new RequestParams();
        try {
            params.put("userfile", file);
        } catch(FileNotFoundException e) {}

        client.post(TadvertConstants.url + "uploadLogs&meid=" + AppState.getInstance().getMeid() + "&logname=" + file.getName(), params, new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {

            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });
    }
}
