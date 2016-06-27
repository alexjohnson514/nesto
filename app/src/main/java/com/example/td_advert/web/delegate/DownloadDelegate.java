package com.example.td_advert.web.delegate;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.example.td_advert.LauncharScreen;
import com.example.td_advert.bean.AppConnectVersionCheckResponse;
import com.example.td_advert.bean.AppState;
import com.example.td_advert.bean.Company;
import com.example.td_advert.database.TestAdapter;
import com.example.td_advert.exception.ExceptionManager;
import com.example.td_advert.exception.PhoneStatus;
import com.example.td_advert.util.FileUtil;
import com.example.td_advert.util.Util;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Narongdej on 7/2/2558.
 */
public class DownloadDelegate extends WebTaskDelegate{

    Context ctx;
    ProgressDialog pd;
    int activeCount = 0;
    DownloadManager manager;

    HashMap<Integer,Company> companyData;


    private String downloadCompleteIntentName = DownloadManager.ACTION_DOWNLOAD_COMPLETE;
    private IntentFilter downloadCompleteIntentFilter = new IntentFilter(downloadCompleteIntentName);
    private BroadcastReceiver downloadCompleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            DownloadManager.Query q = new DownloadManager.Query();
            q.setFilterById(extras.getLong(DownloadManager.EXTRA_DOWNLOAD_ID));
            Cursor c = manager.query(q);

            if(c.moveToFirst()){
                int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                if(status == DownloadManager.STATUS_SUCCESSFUL){
                    activeCount--;

                    if(c.getString(c.getColumnIndex(DownloadManager.COLUMN_DESCRIPTION)).equals("zip")){
                        String saveToPath = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                        String path = saveToPath.substring(0, saveToPath.lastIndexOf('/'));
                        try {
                            FileUtil.extractZipContentsOfSourceFile(saveToPath, path + "/");
                        } catch (Exception e){
                            Util.appendLog("Something was wrong when unzipping files: " + e.toString());
                        }
                    }
                }

                if(status == DownloadManager.STATUS_FAILED){
                    Util.appendLog("Download has failed, not saving new version of all companies, restarting download");
                    ((LauncharScreen) ctx).getDialogContext().unregisterReceiver(downloadCompleteReceiver);
                    ((LauncharScreen) ctx).finish();

                    /*Intent i = new Intent();
                    i.setClassName("com.example.td_advert", "com.example.td_advert.LauncharScreen");
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("welcome_screen", false);
                    ctx.startActivity(i);*/
                }
            }

            Util.appendLog(activeCount + " files left to be download");
            if(!pd.isShowing()) {
                pd.show();
            }
            pd.setMessage(activeCount + " files left to be downloaded...");

            if(activeCount <= 0){
                TestAdapter adapter = new TestAdapter(ctx);

                for (Company company : companyData.values()) {
                    Util.appendLog("Updating local company (id: " + company.getCompanyId() + ") version to " + company.getVersion());
                    adapter.SaveCompany(company);
                }
                adapter.populateState();
                ((LauncharScreen) ctx).clearCompanies();

                try {
                    pd.dismiss();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                Util.appendLog("Downloads are finish, going to sleep");
                LauncharScreen.isDownloading = false;
                ((LauncharScreen) ctx).syncComplete();
                adapter.close();

                ((LauncharScreen) ctx).getDialogContext().unregisterReceiver(downloadCompleteReceiver);

//                Intent i = new Intent();
//                i.setClassName("com.example.td_advert", "com.example.td_advert.BlackActivity");
//                i.setAction("com.tadvert.app.battery_connected");
//                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                context.startActivity(i);

                ((LauncharScreen) ctx).finish();
            }
        }
    };

    public DownloadDelegate(Context ctx) {
        super();
        this.ctx = ctx;

        Util.appendLog("Starting DownloadDelegate");
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionManager(ctx));

        TestAdapter adapter = new TestAdapter(ctx);
        adapter.createDatabase();
        adapter.populateState();
        LauncharScreen.isDownloading = true;
        ((LauncharScreen) ctx).assignCompanyToViews(AppState.getInstance()
                .getCompaniesList());
        adapter.close();

        manager = (DownloadManager) ctx.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    @Override
    public void synchronousPreExecute() {
        super.synchronousPreExecute();

        Util.appendLog("Start downloading company data from the server");
        pd = new ProgressDialog(this.ctx);
        pd.setMessage("Downloading data from server...");
        pd.setCanceledOnTouchOutside(false);
        pd.setCancelable(false);
        pd.show();
    }

    @Override
    public void dismissDialog() {
        if(pd != null) {
            pd.dismiss();
        }
    }

    private void downloadFile(String url, String filename){
        File root = android.os.Environment.getExternalStorageDirectory();
        String rootDirPath = root.getAbsolutePath() + "/Nesto/files/" + filename;
        Boolean file = new File(rootDirPath).isFile();

        if(!file){
            Util.appendLog("Starting download file: " + filename);
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setDestinationInExternalPublicDir("/Nesto/files/", filename);
            request.setTitle("tadvert-files");
            request.setDescription("other");

            manager.enqueue(request);

            activeCount++;
        }
    }

    private void downloadPromotion(String url, String filename){

        File root = android.os.Environment.getExternalStorageDirectory();
        String rootDirPath = root.getAbsolutePath() + "/Nesto/promotions/" + filename;
        Boolean file = new File(rootDirPath).isFile();

        if(file){
            Util.appendLog("Delete old promotions files");
            DeleteRecursive(new File(rootDirPath));
        }

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        //request.setDes
        //request.setDestinationUri()
        request.setDestinationInExternalPublicDir("/Nesto/promotions/", filename);
        request.setTitle("tadvert-files");
        request.setDescription("zip");

        Util.appendLog("Starting download promotion file: " + filename);
        DownloadManager manager = (DownloadManager) ctx.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);

        activeCount++;
    }

    void DeleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                DeleteRecursive(child);

        fileOrDirectory.delete();
    }

    void CheckFileExist(int companyId, String filename){
        File root = android.os.Environment.getExternalStorageDirectory();
        String rootDirPath = root.getAbsolutePath() + "/Nesto/files/" + filename;
        Boolean file = new File(rootDirPath).isFile();

        if(!file){
            Util.appendLog("Removing companyId: " + companyId + " because of file " + filename + " don't exist");
            RemoveCompany(companyId);
        }

    }

    public boolean FileExisted(String filename){
        File root = android.os.Environment.getExternalStorageDirectory();
        String rootDirPath = root.getAbsolutePath() + "/Nesto/files/" + filename;
        Boolean file = new File(rootDirPath).isFile();

        return file;

    }

    void RemoveCompany(int companyId){
        TestAdapter adapter = new TestAdapter(ctx);
        adapter.createDatabase();
        adapter.open();
        adapter.removeCompany(String.valueOf(companyId));
        adapter.populateState();
        adapter.close();

        Util.appendLog("companyId: " + companyId + " removed");
    }

    @Override
    public void synchronousPostExecute(final String sr) {
        super.synchronousPostExecute(sr);
        Gson gson = new Gson();
        AppConnectVersionCheckResponse appConnectVersionCheckResponse = null;

        try {
            appConnectVersionCheckResponse = gson.fromJson(sr, AppConnectVersionCheckResponse.class);
        } catch (Exception e) {
            Util.appendLog("JSON Error Download Delegate" + e.toString());
            return;
        }


        ArrayList<Company> companyList = appConnectVersionCheckResponse.getCompanyVersions();

        TestAdapter adapter = new TestAdapter(ctx);
        adapter.createDatabase();
        adapter.open();

        adapter.SaveTestDevice(appConnectVersionCheckResponse.getTestDevice());

        String ids = "";
        try {
            for (int i = 0; i < companyList.size(); i++) {
                if (i != 0) {
                    ids += ",";
                }
                ids += "" + companyList.get(i).getCompanyId();
            }
        } catch (Exception e){
            Util.appendLog("Company List generate error: " + e.toString());
            return;
        }

        adapter.removeNotRequiredCompanies(ids);
        adapter.populateState();
        boolean downloadStarted = false;
        activeCount = 0;

        boolean registerRec = false;

        companyData = new HashMap<Integer,Company>();

        for (Company company : companyList) {
            int localVersion = adapter.getCompanyVersion(company.getCompanyId());
            File root = android.os.Environment.getExternalStorageDirectory();
            Log.d("versionlist", localVersion+"/"+company.getVersion()+"/"+company.getCompanyId()+"/"+company.getLogo());
            if (localVersion < company.getVersion()) {
                if(!registerRec){
                    registerRec = true;
                    ((LauncharScreen) ctx).getDialogContext().registerReceiver(downloadCompleteReceiver, downloadCompleteIntentFilter);
                }

                String path = company.getCompany_path() + "/files/";
                boolean fileExixted = FileExisted(company.getLogo());
                if(!fileExixted) {
                    downloadFile(path + company.getLogo(), company.getLogo());
                    downloadFile(path + company.getTabs().getHomepage_background(), company.getTabs().getHomepage_background());
                    downloadFile(path + company.getTabs().getLayout1_background(), company.getTabs().getLayout1_background());
                    downloadFile(path + company.getTabs().getLayout2_background(), company.getTabs().getLayout2_background());
                    downloadFile(path + company.getTabs().getLayout3_background(), company.getTabs().getLayout3_background());
                    downloadFile(path + company.getTabs().getVideo_bg(), company.getTabs().getVideo_bg());
                    downloadFile(path + company.getTabs().getVideo(), company.getTabs().getVideo());
                    downloadFile(path + company.getTabs().getVideo_thumb(), company.getTabs().getVideo_thumb());
                }

                if(!company.getTabs().getPromotion_popup().equals("0")) {
                    String rootDirPath = root.getAbsolutePath() + "/Nesto/promotions/" + company.getCompanyId() + "/";
                    DeleteRecursive(new File(root.getAbsolutePath() + "/Nesto/promotions/" + company.getCompanyId() + "/"));
                    downloadPromotion(company.getCompany_path() + "/promotions/" + PhoneStatus.fetchStatus(ctx).getVersionCode() + "_" + company.getCompanyId() + ".zip", PhoneStatus.fetchStatus(ctx).getVersionCode() + "_" + company.getCompanyId() + ".zip");
                }


                setPaths(company, root.getAbsolutePath() + "/Nesto/");

                companyData.put(company.getCompanyId(), company);

            } /*else {
                Util.appendLog("Checking companyId: " + company.getCompanyId() + " to see if all files exist");
                // Check if file exist
                CheckFileExist(company.getCompanyId(), company.getLogo());
                CheckFileExist(company.getCompanyId(), company.getTabs().getHomepage_background());
                CheckFileExist(company.getCompanyId(), company.getTabs().getLayout1_background());
                CheckFileExist(company.getCompanyId(), company.getTabs().getLayout2_background());
                CheckFileExist(company.getCompanyId(), company.getTabs().getLayout3_background());
                CheckFileExist(company.getCompanyId(), company.getTabs().getVideo_bg());
                CheckFileExist(company.getCompanyId(), company.getTabs().getVideo());
                CheckFileExist(company.getCompanyId(), company.getTabs().getVideo_thumb());

                if(!company.getTabs().getPromotion_popup().equals("0")){
                    String rootDirPath = root.getAbsolutePath() + "/Nesto/promotions/" + company.getCompanyId() + "/";
                    if (!new File(rootDirPath).isDirectory()){
                        RemoveCompany(company.getCompanyId());
                    }
                }
            }*/
        }


        if (activeCount == 0) {
            if (!downloadStarted) {

                adapter.populateState();
                ((LauncharScreen) ctx).clearCompanies();

                try {
                    pd.cancel();
                    LauncharScreen.isDownloading = false;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                ((LauncharScreen) ctx).syncComplete();
            }

        }
        adapter.close();
    }

	private void setPaths(Company company, String rootDirPath) {
		String companyFolderPath = rootDirPath + "files/";

		company.setLogo(companyFolderPath + company.getLogo());
		Company.TabConfig tabs = company.getTabs();
		tabs.setHomepage_background(companyFolderPath + tabs.getHomepage_background());
		tabs.setLayout1_background(companyFolderPath + tabs.getLayout1_background());
		tabs.setLayout2_background(companyFolderPath + tabs.getLayout2_background());
		tabs.setLayout3_background(companyFolderPath + tabs.getLayout3_background());
		tabs.setVideo_bg(companyFolderPath + tabs.getVideo_bg());
		tabs.setVideo(companyFolderPath + tabs.getVideo());
		tabs.setVideo_thumb(companyFolderPath + tabs.getVideo_thumb());

        if(!tabs.getPromotion_popup().equals("0"))
            tabs.setPromotion_popup(rootDirPath + "promotions/" + company.getCompanyId() + "/" + "index.html");
	}

    @Override
	public void onError(Exception e) {
		// if (e instanceof IOException) {
		// if (appVersion == -1) {
        Util.appendLog("Downloading companies update list error");
		try {
			pd.cancel();
		} catch (Exception ex) {

		}
        super.onError(e);
        ((LauncharScreen) ctx).finish();

		//WebTask.showInternetConnectivityDialog(ctx, e.getMessage());
		// } else {
		// TestAdapter adapter = new TestAdapter(ctx);
		// adapter.createDatabase();
		// adapter.open();
		//
		// adapter.populateState();
		// pd.cancel();
		// ((LauncharScreen) ctx).assignCompanyToViews(AppState
		// .getInstance().getCompaniesList());
		// LauncharScreen.isDownloading = false;
		// adapter.close();
		// }
		// } else {
		// }
	}


}
