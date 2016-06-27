package com.example.td_advert.dialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import com.example.td_advert.R;
import com.example.td_advert.jsinterface.JavaScriptInterface;
import com.example.td_advert.util.Util;

import java.io.File;

/**
 * Created by Narongdej on 17/1/2558.
 */
public class PromotionDialog extends TAdvertBaseDialog {

    static WebView webView;
    ViewGroup parent;
    Context ctx;
    JavaScriptInterface jsInterface;

    public PromotionDialog(Context context, ViewGroup parent) {
        super(context, parent);
        this.parent = parent;
        this.ctx = context;

        Util.appendLog("Promotion popup got create");

        jsInterface = new JavaScriptInterface(context, this);

        CookieSyncManager.createInstance(context);
        CookieSyncManager.getInstance().startSync();

        webView = (WebView) findViewById(R.id.webview);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setFocusable(true);
        webView.setFocusableInTouchMode(true);

        webView.clearCache(true);
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setDomStorageEnabled(true);
        webView.addJavascriptInterface(jsInterface, "JSInterface");

        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        webView.setLongClickable(false);
    }


    public void loadHTML(String fileLocation){
        super.show();
        webView.loadUrl("file://" + fileLocation);
        CookieManager.getInstance().setAcceptCookie(true);
        Util.appendLog("Promotion show: " + fileLocation);
    }

    public static void keyboardLoose(){
        if(webView != null)
            webView.loadUrl("javascript:blurAll()");
    }

    public void editCloseButton(final String filePath, final int left, final int top, final int right, final int bottom){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                lp.setMargins(left, top, right, bottom);
                closeButton.setLayoutParams(lp);

                File root = android.os.Environment.getExternalStorageDirectory();
                Bitmap bmp = BitmapFactory.decodeFile(root.getAbsolutePath() + "/Nesto/" + filePath);
                closeButton.setImageBitmap(bmp);
            }
        });
    }

    public void closeDialog(){
        this.dismiss();
    }

    public void playClick(){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webView.playSoundEffect(SoundEffectConstants.CLICK);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.promotion_screen;
    }

    @Override
    protected void onClickButton(View view) {
        this.dismiss();
    }
}
