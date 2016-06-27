package com.example.td_advert.jsinterface;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.webkit.JavascriptInterface;

import com.example.td_advert.CompanyScreen;
import com.example.td_advert.database.MessageSync;
import com.example.td_advert.dialog.PromotionDialog;
import com.example.td_advert.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Narongdej on 21/1/2558.
 */
public class JavaScriptInterface {

    Context mContext;
    PromotionDialog caller;

    public JavaScriptInterface(Context context, PromotionDialog parent){
        mContext = context;
        caller = parent;
    }

    @JavascriptInterface
    public void alert(String alertTxt){
        AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                .setMessage(alertTxt)
                .setTitle("Alert")
                .setPositiveButton("Ok",
                        new android.content.DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }

                        }).create();
        alertDialog.show();
    }

    @JavascriptInterface
    public void saveData(String jsonString) {
        JSONObject jsonParse = null;

        Util.appendLog("PromotionPopup got saved");

        try {
            jsonParse = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MessageSync msc = new MessageSync(mContext);
        msc.storeMessage(jsonParse);

        try {
            CompanyScreen.promotionShown.add(jsonParse.get("companyId").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        final JSONObject finalJsonParse = jsonParse;
//        AlertDialog alertDialog = new AlertDialog.Builder(mContext)
//                .setMessage("\nThank you for your contacting us\n")
//                .setTitle("Information Saved")
//                .setPositiveButton("Ok",
//                        new android.content.DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog,
//                                                int which) {
//                                if (finalJsonParse != null) {
//                                    try {
//                                        CompanyScreen.promotionShown.add(finalJsonParse.get("companyId").toString());
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                                caller.closeDialog();
//                                dialog.dismiss();
//                            }
//
//                        }).create();
//        alertDialog.show();
    }

    @JavascriptInterface
    public void editCloseButton(String filePath, String left, String top, String right, String bottom){
        caller.editCloseButton(filePath, Integer.parseInt(left), Integer.parseInt(top), Integer.parseInt(right), Integer.parseInt(bottom));
    }

    @JavascriptInterface
    public void saveMessageSync(String jsonString){
        JSONObject jsonParse = null;

        try {
            jsonParse = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MessageSync msc = new MessageSync(mContext);
        msc.storeMessage(jsonParse);
    }

    @JavascriptInterface
    public void closeDialog(){
        caller.closeDialog();
    }

    @JavascriptInterface
    public void playClick(){
        caller.playClick();
    }
}
