package com.example.td_advert.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;

import com.commonsware.cwac.updater.ConfirmationStrategy;
import com.commonsware.cwac.updater.DownloadStrategy;
import com.commonsware.cwac.updater.ImmediateConfirmationStrategy;
import com.commonsware.cwac.updater.InternalHttpDownloadStrategy;
import com.commonsware.cwac.updater.SimpleHttpDownloadStrategy;
import com.commonsware.cwac.updater.SimpleHttpVersionCheckStrategy;
import com.commonsware.cwac.updater.UpdateRequest;
import com.commonsware.cwac.updater.VersionCheckStrategy;
import com.example.td_advert.bean.AppState;
import com.example.td_advert.constant.TadvertConstants;

/**
 * Created by Narongdej on 27/1/2558.
 */
public class UpdaterUtil {

    ProgressDialog pd;
    Context ctx;

    public UpdaterUtil(Context context) {
        ctx = context;
        UpdateRequest.Builder builder = new UpdateRequest.Builder(context);

        builder.setVersionCheckStrategy(buildVersionCheckStrategy())
                .setPreDownloadConfirmationStrategy(buildPreDownloadConfirmationStrategy())
                .setDownloadStrategy(buildDownloadStrategy())
                .setPreInstallConfirmationStrategy(buildPreInstallConfirmationStrategy())
                .execute();
    }

    VersionCheckStrategy buildVersionCheckStrategy() {
        Util.appendLog("Checking if Application APK update exist");
        return(new SimpleHttpVersionCheckStrategy(TadvertConstants.url + "getLatestApkVersionJSON&meid=" + AppState.getInstance().getMeid()));
    }

    ConfirmationStrategy buildPreDownloadConfirmationStrategy() {
        return(new ImmediateConfirmationStrategy());
    }

    DownloadStrategy buildDownloadStrategy() {
        if (Build.VERSION.SDK_INT>=11) {
            return(new InternalHttpDownloadStrategy());
        }
        return(new SimpleHttpDownloadStrategy());
    }

    ConfirmationStrategy buildPreInstallConfirmationStrategy() {
        return(new ImmediateConfirmationStrategy());
    }
}
