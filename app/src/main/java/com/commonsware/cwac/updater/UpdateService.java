/***
Copyright (c) 2012 CommonsWare, LLC

Licensed under the Apache License, Version 2.0 (the "License"); you may
not use this file except in compliance with the License. You may obtain
a copy of the License at
  http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.commonsware.cwac.updater;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.commonsware.cwac.wakeful.WakefulIntentService;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class UpdateService extends WakefulIntentService {
  public UpdateService() {
    super("UpdateService");
  }

  @Override
  protected void doWakefulWork(Intent cmd) {
    UpdateRequest req=new UpdateRequest(cmd);
    VersionCheckStrategy vcs=req.getVersionCheckStrategy();

    try {
      if (req.getPhase() == UpdateRequest.PHASE_DOWNLOAD) {
        downloadAndInstall(cmd, req, req.getUpdateURL());
      }
      else if (req.getPhase() == UpdateRequest.PHASE_INSTALL) {
        install(req, req.getInstallUri());
      }
      else {
        int updateVersionCode=vcs.getVersionCode();
        int currentVersionCode=
            getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;

        if (updateVersionCode > currentVersionCode) {
          ConfirmationStrategy strategy=
              req.getPreDownloadConfirmationStrategy();

          if (strategy == null
              || strategy.confirm(this,
                                  buildDownloadPhase(cmd,
                                                     vcs.getUpdateURL()))) {
            downloadAndInstall(cmd, req, vcs.getUpdateURL());
          }
        }
        else if (updateVersionCode < currentVersionCode) {
          // TODO: file an error
        }
      }
    }
    catch (Exception e) {
      Log.e("CWAC-Update", "Exception in applying update", e);
    }
  }

  private void downloadAndInstall(Intent cmd, UpdateRequest req,
                                  String updateURL) throws Exception {
    DownloadStrategy ds=req.getDownloadStrategy();
    Uri apk=ds.downloadAPK(this, updateURL);

    if (apk != null) {
      confirmAndInstall(cmd, req, apk);
    }
  }

  private void confirmAndInstall(Intent cmd, UpdateRequest req, Uri apk)
                                                                        throws Exception {
    ConfirmationStrategy strategy=
        req.getPreInstallConfirmationStrategy();

    if (strategy == null
        || strategy.confirm(this, buildInstallPhase(cmd, apk))) {
      install(req, apk);
    }
  }

  /*private void install(UpdateRequest req, Uri apk) {
    Intent i;

    Log.d("Zenyai", apk.toString());
    Log.d("Zenyai", getApplicationContext().getFilesDir().getAbsolutePath() + "/CWAC-Update/update.apk");
    
    /*if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
      i=new Intent(Intent.ACTION_INSTALL_PACKAGE);
      i.putExtra(Intent.EXTRA_ALLOW_REPLACE, true);
    }
    else {
      i=new Intent(Intent.ACTION_VIEW);
    }

    i.setDataAndType(apk, "application/vnd.android.package-archive");
    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

    startActivity(i);
  }*/

  private void install(UpdateRequest req, Uri apk){
      final String libs = "LD_LIBRARY_PATH=/vendor/lib:/system/lib ";


      final String[] commands = {
              libs + "pm install -r " + apk.getPath(),
              libs + "am start -n com.example.td_advert/.BlackActivity\n"
      };
      execute_as_root(commands);	// not supposed to return if successful
  }

   private void execute_as_root( String[] commands ) {
        try {
            // Do the magic
            Process p = Runtime.getRuntime().exec( "su" );
            InputStream es = p.getErrorStream();
            DataOutputStream os = new DataOutputStream(p.getOutputStream());

            for( String command : commands ) {
                //Log.i(TAG,command);
                os.writeBytes(command + "\n");
            }
            os.writeBytes("exit\n");
            os.flush();

            int read;
            byte[] buffer = new byte[4096];
            String output = new String();
            while ((read = es.read(buffer)) > 0) {
                output += new String(buffer, 0, read);
            }

            p.waitFor();
            Log.e("Zenyai", output.trim() + " (" + p.exitValue() + ")");
        } catch (IOException e) {
            Log.e("Zenyai", e.getMessage());
        } catch (InterruptedException e) {
            Log.e("Zenyai", e.getMessage());
        }
    }

  private PendingIntent buildDownloadPhase(Intent cmd, String updateURL) {
    UpdateRequest.Builder builder=new UpdateRequest.Builder(this, cmd);

    builder.setPhase(UpdateRequest.PHASE_DOWNLOAD);
    builder.setUpdateURL(updateURL);

    return(builder.buildPendingIntent());
  }

  private PendingIntent buildInstallPhase(Intent cmd, Uri apk) {
    UpdateRequest.Builder builder=new UpdateRequest.Builder(this, cmd);

    builder.setPhase(UpdateRequest.PHASE_INSTALL);
    builder.setInstallUri(apk);

    return(builder.buildPendingIntent());
  }
}