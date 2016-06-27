package com.example.td_advert.dialog;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.example.td_advert.R;

public class FeedbackConfirmationDialog extends TAdvertBaseDialog {


	public FeedbackConfirmationDialog(Context context, ViewGroup parent) {
		this(context, parent, true);
	}

	public FeedbackConfirmationDialog(Context context, ViewGroup parent, boolean showRating) {
		super(context, parent);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.feedback_confirmation;
	}

	@Override
	protected void onClickButton(View view) {
		this.dismiss();
	}

	

}
