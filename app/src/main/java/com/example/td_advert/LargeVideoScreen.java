package com.example.td_advert;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Window;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.td_advert.database.TestAdapter;
import com.example.td_advert.database.Utility;
import com.example.td_advert.exception.ExceptionManager;

public class LargeVideoScreen extends Activity {

	String url;

	VideoView playvideo;

	MediaController mc;
	int CID;

	private static ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionManager(this));
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.large_videoscreen);

		Intent intent = getIntent();
		CID = intent.getIntExtra("CID", 0);
		System.out.println("GetId>>>>>>>>>>>>>>" + CID);

		playvideo = (VideoView) findViewById(R.id.playvideo);

		MediaController mediaController = new MediaController(this);
		mediaController.setAnchorView(playvideo);
		playvideo.setMediaController(new MediaController(this));

		getVideos(CID);

	}

	void getVideos(int CId) {
		TestAdapter mDbHelper = new TestAdapter(LargeVideoScreen.this);
		mDbHelper.createDatabase();
		mDbHelper.open();

		Cursor testBackgrounddata = mDbHelper.getVideos(CId);
		System.out.println("******count****" + testBackgrounddata.getCount());
		if (testBackgrounddata.getCount() > 0) {
			if (testBackgrounddata.moveToFirst()) {
				do {
					String videoPath = Utility.GetColumnValue(
							testBackgrounddata, "VideoPath");
					System.out.println("VideoPath>>>>>>>>>>>>" + videoPath);
					playvideo.setVideoPath(videoPath);
					playvideo.requestFocus();
					playvideo.start();
				} while (testBackgrounddata.moveToNext());
				testBackgrounddata.close();
			}
		} else {
			Toast.makeText(LargeVideoScreen.this, "No Video Available",
					Toast.LENGTH_SHORT).show();
			finish();
		}

	}
}
