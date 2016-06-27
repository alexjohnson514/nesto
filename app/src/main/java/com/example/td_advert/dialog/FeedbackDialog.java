package com.example.td_advert.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RatingBar;

import com.example.td_advert.LauncharScreen;
import com.example.td_advert.R;
import com.example.td_advert.bean.Feedback;
import com.example.td_advert.constant.TadvertConstants;
import com.example.td_advert.database.TestAdapter;
import com.example.td_advert.web.WebTask;
import com.example.td_advert.web.delegate.WebTaskDelegate;

public class FeedbackDialog extends TAdvertBaseDialog {

	private boolean showRating;

	public FeedbackDialog(Context context, ViewGroup parent) {
        this(context, parent, true);
	}

	public FeedbackDialog(Context context, ViewGroup parent, boolean showRating) {
		super(context, parent);
		this.showRating = showRating;
		View view = findViewById(R.id.ratingBar);

		if (view != null) {
			if (showRating) {
				view.setVisibility(View.VISIBLE);
				RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar1);
				LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
				stars.getDrawable(2).setColorFilter(Color.rgb(22, 47, 147), PorterDuff.Mode.SRC_ATOP);
			} else {
				view.setVisibility(View.GONE);
			}
		}

		view = this.findViewById(R.id.send_button);
		view.setOnClickListener(this);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.feedback_screen;
	}

	@Override
	protected void onClickButton(View view) {
		saveFeedback();
		
		this.dismiss();
	}

	void saveFeedback() {
		EditText nameView = (EditText) findViewById(R.id.name);
		EditText phoneView = (EditText) findViewById(R.id.phone_num);
		EditText feedbackTextView = (EditText) findViewById(R.id.contaxtUsTextArea);
		RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar1);

		final Feedback dto = new Feedback("FEEDBACK", nameView.getText()
				.toString(), phoneView.getText().toString(), feedbackTextView
				.getText().toString(), ratingBar.getRating());

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
//			LayoutInflater inflater = (LayoutInflater)getContext().getSystemService
//				      (Context.LAYOUT_INFLATER_SERVICE);
//			View view = inflater.inflate(R.layout.feedback_confirmation, null);
//			FeedbackConfirmationDialog alertDialog = new FeedbackConfirmationDialog(activity, null);
			AlertDialog alertDialog = new AlertDialog.Builder(this.getContext())
					.setMessage("\nThank you for your Feedback\n")
					//.setView(view)
					.setPositiveButton("Ok", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					}).create();
			alertDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
