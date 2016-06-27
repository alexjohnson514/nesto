package com.example.td_advert.thread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Vector;

import android.os.Handler;
import android.util.Log;

import com.example.td_advert.bean.AppState;
import com.example.td_advert.bean.Company;
import com.example.td_advert.constant.TadvertConstants;

public class VideoLoopThread implements Runnable {
	private Handler mHandler = null;
	private int currentIndex = Math.abs(new Random(System.currentTimeMillis()).nextInt());
	private VideoLooperCallback callback = null;
	private boolean isRunning = false;
	private Vector<Integer> indices = new Vector<Integer>();

	public VideoLoopThread(VideoLooperCallback callback) {
		super();
		this.callback = callback;
		ArrayList<Company> size = AppState.getInstance().getCompaniesList();
        int i = 0;
        for (Company com: size){
            if(com.getTabs().getPlaceholder() != 1){
                Log.v("Zenyai", "added");
                indices.add(i);
            }
            Log.v("Zenyai", "added");
            i++;
        }

        Collections.shuffle(indices);

	}

	public void start() {
		mHandler = new Handler();
		isRunning = true;
		if (mHandler != null) {
			mHandler.postDelayed(this, 1);
		}
		callback.onLoopStart();
	}

	public void stop() {
		
		isRunning = false;
		if (mHandler != null) {
			mHandler.postDelayed(this, 10);
			mHandler = null;
		}
		callback.onLoopEnd();
		callback = null;
//		currentIndex = new Random(System.currentTimeMillis()).nextInt();
	}

	@Override
	public void run() {
		
		if (isRunning) {
			if (indices.size() == 0) {
				stop();
			} else {
				currentIndex = indices.get(0);
                //Log.v("Zenyai", currentIndex + " CCC");
				callback.onLoopRun(indices.get(currentIndex));
				indices.removeElementAt(currentIndex);

				if (mHandler != null) {
					mHandler.postDelayed(this, TadvertConstants.timePerVideo * 1000);
				}
			}
		}
	}

	public boolean isRunning() {
		return isRunning;
	}

	public interface VideoLooperCallback {
		public void onLoopStart();

		public void onLoopRun(int iterationNumber);

		public void onLoopEnd();
	}
}
