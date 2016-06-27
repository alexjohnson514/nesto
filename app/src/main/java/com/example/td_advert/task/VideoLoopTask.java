package com.example.td_advert.task;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;

import com.example.td_advert.bean.AppState;
import com.example.td_advert.bean.Company;
import com.example.td_advert.thread.VideoLoopThread.VideoLooperCallback;

public class VideoLoopTask extends AbstractTask {

	private static VideoLoopTask instance = null;
	private ArrayList<Integer> indices = new ArrayList<Integer>();

	public static VideoLoopTask getInstance(Context ctx) {
		if (instance == null || ctx != instance.ctx) {
			instance = new VideoLoopTask(ctx);
		}
		return instance;
	}

	private VideoLoopTask(Context ctx) {
		super(ctx, 0);
		enabled = false;
	}

	private int iterationNumber = 0;

	@Override
	protected void execute() {
		if (indices.size() > 0) {
			int index = indices.remove(iterationNumber);

			((VideoLooperCallback) this.ctx).onLoopRun(index);
			Company company = AppState.getInstance().getCompaniesList()
					.get(index);
			setExecuteAfterSecs(company.getVideoLoopDuration());
			if (indices.size() > 0) {
				iterationNumber = (iterationNumber + 1) % indices.size();
			}
		} else {
			setEnabled(false);
		}
	}

	@Override
	public void setEnabled(boolean enabled) {
		if (!this.enabled && enabled) {// video loop was previously down and it
										// is starting now
			if (this.ctx instanceof VideoLooperCallback) {
				((VideoLooperCallback) this.ctx).onLoopStart();
				iterationNumber = 0;
				setExecuteAfterSecs(0);
				initIndices();
				super.setEnabled(enabled);
			}
		} else if (this.enabled && !enabled) {// The loop was running and now it
												// is shutting down
			if (this.ctx instanceof VideoLooperCallback) {
				((VideoLooperCallback) this.ctx).onLoopEnd();
				super.setEnabled(enabled);
			}
		}

	}

	private void initIndices() {
		indices = new ArrayList<Integer>();
		ArrayList<Company> companies = AppState.getInstance().getCompaniesList();
        int ix = 0;
        for (Company com: companies) {
            if (com.getTabs().getPlaceholder() != 1) {
                indices.add(ix);
            }
            ix++;
        }

        Collections.shuffle(indices);
//		for (int i = 0; i < companies.size(); i++) {
//			indices.add(i);
//		}
//		Random random = new Random();
//		for (int i = 0; i < indices.size(); i++) {
//			int index2 = random.nextInt(10000000) % indices.size();
//			int temp = indices.get(i);
//
//			indices.set(i, indices.get(index2));
//			indices.set(index2, temp);
//		}

	}
}