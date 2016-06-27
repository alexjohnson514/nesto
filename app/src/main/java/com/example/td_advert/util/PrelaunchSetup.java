package com.example.td_advert.util;

import android.app.AlarmManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.example.td_advert.constant.TadvertConstants;

import java.io.File;
import java.io.IOException;

/**
 * Created by Narongdej on 13/1/2558.
 */
public class PrelaunchSetup {

    private Context appcontext;

    public PrelaunchSetup(Context context){
        appcontext = context;
    }

    public void runEverything(){
        removeNavigationBar();
        unzipTAdvert();
        setWifi();
        editTimezone();

        try {
            Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", "ime set com.example.td_advert/.keyboard.SimpleIME"});
            proc.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void unzipTAdvert(){
        File root = android.os.Environment.getExternalStorageDirectory();
        String rootDirPath = root.getAbsolutePath() + "/Nesto.zip";
        Boolean file = new File(rootDirPath).isFile();

        if(file){
            String path = rootDirPath.substring(0, rootDirPath.lastIndexOf('/'));
            FileUtil.extractZipContentsOfSourceFile(rootDirPath, path + "/");
            FileUtil.deleteFileByPath(rootDirPath);
        }
    }

    private void removeNavigationBar(){
        try
        {
            Process proc = Runtime.getRuntime().exec(new String[]{"su","-c","service call activity 42 s16 com.android.systemui"});
            proc.waitFor();
            Util.appendLog("Removing navigation bar, succeed");
        }
        catch(Exception ex)
        {
            Util.appendLog("Removing navigation bar, failed, ROOT ERROR");
            Log.e("ROOT ERROR", ex.getMessage());
        }
    }

    public void setWifi(){

        Util.appendLog("Checking Wi-Fi Connection");
        ConnectivityManager connManager = (ConnectivityManager) appcontext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (!mWifi.isConnected()) {
            Util.appendLog("Trying to connect to "+TadvertConstants.wifi_ssid+ " wifi network");
            WifiConfiguration wifiConfig = new WifiConfiguration();
            wifiConfig.SSID = String.format("\"%s\"", TadvertConstants.wifi_ssid);
            wifiConfig.preSharedKey = String.format("\"%s\"", TadvertConstants.wifi_password);

            WifiManager wifiManager = (WifiManager) appcontext.getSystemService(Context.WIFI_SERVICE);
            int netId = wifiManager.addNetwork(wifiConfig);
            wifiManager.disconnect();
            wifiManager.enableNetwork(netId, true);
            wifiManager.reconnect();
        }
    }

    private void editTimezone(){
        Util.appendLog("Set system timezone to Asia/Bangkok");
        AlarmManager am = (AlarmManager) appcontext.getSystemService(Context.ALARM_SERVICE);
        am.setTimeZone("Asia/Bangkok");
    }

}
