package com.example.td_advert.dialog;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.example.td_advert.R;

public class ContactUsDialog extends TAdvertBaseDialog {
	private ViewGroup root = null;

	public ContactUsDialog(Context context, ViewGroup parent) {
		super(context, parent);
		root = parent;
        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.contact_us_screen;
	}

	@Override
	protected void onClickButton(View view) {
		TAdvertBaseDialog feedbackDialog = new FeedbackDialog(
				activity, null, false);
		this.dismiss();
	}

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        Log.v("Zenyai", event.getAction() + " | " + MotionEvent.ACTION_OUTSIDE);
        if(event.getAction() == MotionEvent.ACTION_OUTSIDE){
            System.out.println("TOuch outside the dialog ******************** ");
            this.dismiss();
        }
        return false;
    }


}
