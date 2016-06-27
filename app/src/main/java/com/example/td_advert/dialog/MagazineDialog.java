package com.example.td_advert.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;

import com.example.td_advert.LauncharScreen;
import com.example.td_advert.R;
import com.example.td_advert.bean.Feedback;
import com.example.td_advert.constant.TadvertConstants;
import com.example.td_advert.database.TestAdapter;
import com.example.td_advert.web.WebTask;
import com.example.td_advert.web.delegate.WebTaskDelegate;

public class MagazineDialog extends TAdvertBaseDialog {

	// private ImageView m1, m2, m3, m4;

	public MagazineDialog(Context context, ViewGroup parent) {
		this(context, parent, true);

	}

	public MagazineDialog(Context context, ViewGroup parent, boolean showRating) {
		super(context, parent);
		View view = findViewById(R.id.m1);
		view.setOnClickListener(this);
		view = findViewById(R.id.m2);
		view.setOnClickListener(this);
		view = findViewById(R.id.m3);
		view.setOnClickListener(this);
		view = findViewById(R.id.m4);
		view.setOnClickListener(this);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.magazine_screen;
	}

	@Override
	protected void onClickButton(View view) {
		// saveFeedback();
		String selectedChoice = "";
		switch (view.getId()) {
		case R.id.m1:
			selectedChoice = "Hello";
			break;
		case R.id.m2:
			selectedChoice = "Men's Health";
			break;
		case R.id.m3:
			selectedChoice = "BK";
			break;
		case R.id.m4:
			selectedChoice = "OK!";
			break;
		}
		saveFeedback(selectedChoice);
		this.dismiss();
	}

	void saveFeedback(String selectedChoice) {

		final Feedback dto = new Feedback("MAGAZINE", selectedChoice);

		if (dto.isValid()) {
			if (LauncharScreen.isConnectingToInternet(getContext())) {
				String url = TadvertConstants.url + "sendFeedback";
				WebTask task = new WebTask(url, dto.toPostParams(),
						new WebTaskDelegate() {

							@Override
							public void onError(Exception e) {
								saveFeedbackToDB(dto);
							}

						});
				task.execute();
			} else {
				saveFeedbackToDB(dto);
			}
			AlertDialog alertDialog = new AlertDialog.Builder(activity)
					.setTitle("Choice Saved")
					.setMessage("\n\nThank you for your selection\n").setPositiveButton("OK", new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							MagazineDialog.this.dismiss();
						}
					}).create();
			alertDialog.show();
		}
	}

	void saveFeedbackToDB(Feedback dto) {

		TestAdapter mDbHelper = new TestAdapter(getContext());
		mDbHelper.createDatabase();
		mDbHelper.open();

		mDbHelper.SaveFeedback(dto);

		mDbHelper.close();
	}

}
