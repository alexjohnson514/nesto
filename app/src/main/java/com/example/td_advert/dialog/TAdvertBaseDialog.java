package com.example.td_advert.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.td_advert.CompanyScreen;
import com.example.td_advert.LauncharScreen;
import com.example.td_advert.R;

public abstract class TAdvertBaseDialog extends Dialog implements
		OnClickListener {
	protected int layoutId;
	public static boolean isOpen = false;
	protected Activity activity = null;
    protected ImageView closeButton;

	public TAdvertBaseDialog(Context context, ViewGroup parent) {
		super(context, R.style.Transparent);
		activity = (Activity) context;
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		// getWindow().getDecorView().setSystemUiVisibility(
		// View.SYSTEM_UI_FLAG_FULLSCREEN
		// | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		LayoutInflater inflater = this.getLayoutInflater();
		View view = inflater.inflate(getLayoutId(), null);
		this.setContentView(view);
		this.setCancelable(false);
		this.setCanceledOnTouchOutside(false);

		closeButton = (ImageView) findViewById(R.id.close_button);
		closeButton.setOnClickListener(this);
	}

	@Override
	public void show() {
		super.show();
		isOpen = true;
	}

	@Override
	public void dismiss() {
		super.dismiss();
		isOpen = false;

        if (activity instanceof LauncharScreen) {
            ((LauncharScreen) activity).disableClick(false);
        }
	}

	protected abstract int getLayoutId();

	protected abstract void onClickButton(View view);

	@Override
	public void onClick(View view) {

		if (activity instanceof LauncharScreen) {
			((LauncharScreen) activity).resetIdleTime();
		} else if (activity instanceof CompanyScreen) {
			((CompanyScreen) activity).resetIdleTime();
		}

		switch (view.getId()) {
		case R.id.close_button:
			this.dismiss();
			break;
		default:
			onClickButton(view);
		}
	}

}
