package com.example.td_advert;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DigitalClock;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.ViewFlipper;

import com.example.td_advert.analytics.Analytics;
import com.example.td_advert.bean.Company;
import com.example.td_advert.bean.Company.TabConfig;
import com.example.td_advert.bean.UserEmail;
import com.example.td_advert.constant.TadvertConstants;
import com.example.td_advert.database.MessageSync;
import com.example.td_advert.database.TableColumns;
import com.example.td_advert.database.TestAdapter;
import com.example.td_advert.database.Utility;
import com.example.td_advert.dialog.FeedbackDialog;
import com.example.td_advert.dialog.PromotionDialog;
import com.example.td_advert.dialog.TAdvertBaseDialog;
import com.example.td_advert.exception.ExceptionManager;
import com.example.td_advert.exception.PhoneStatus;
import com.example.td_advert.util.Util;
import com.example.td_advert.web.WebTask;
import com.example.td_advert.web.delegate.WebTaskDelegate;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;

import org.json.JSONException;
import org.json.JSONObject;

public class CompanyScreen extends BaseActivity implements OnClickListener {
	public static boolean isOpen = false;
	// private static String ImagePath = null;
	TextView back_button, feedback_main;// welcome,
										// nu_in_de_bioscope,
										// evenmentan,
										// schrijf_je_in,
										// reservern
	// ,
	// heading1, heading2, heading3, newLayoutText,newLayoutText1,
	// newLayoutText2;

	View welcome, nu_in_de_bioscope, evenmentan, schrijf_je_in, reservern;
	LinearLayout newLayout, newLayout1, newLayout2;
	TextView timeCounter;

	ProgressDialog pd;
	VideoView playvideo, backgroundVideo;
	private RelativeLayout videoPanel = null;
	private FrameLayout backButton = null;
	private ImageButton videoCloseButton = null;

	ViewFlipper viewFlipper;

    MessageSync ms;

	// VideoView playvideo;

	ImageView image1, image2, image3;
	ImageView layout1Image, layout2_Image1, layout2_Image2, layout3_Image1,
			layout3_Image2, layout3_Image3, layout3_Image4, layout4_Image1,
			thumbnail_image;

	// TextView layoutTwoText, layout3Head1, layout3Head2, layout3Text1,
	// layout3Text2, layout5Text1;
	TextView layout5Text1;
	EditText getemailaddress;
	Button sendemail;
	String valid_email = null;

	RelativeLayout mainContainer;
	LinearLayout welocomeScreen;
	// private GoogleAnalytics mGaInstance;
	// private Tracker mGaTracker;
	Drawable myDrawable;

	static float ratingValue;
	AlertDialog categoryDialog;
	Company company;

	TableColumns tablecol = new TableColumns();
	ArrayList<Bitmap> bgs = new ArrayList<Bitmap>();
	ArrayList<Bitmap> bgsAll = new ArrayList<Bitmap>();
	HashMap<Bitmap, String> bgsMap = new HashMap<Bitmap, String>();
	ExceptionManager exceptionManager = null;

	Timer activityDelayTimer = new Timer();
	TimerTask activityCheckTimerTask = new ActivityCheckTimerTask();
	int idleTimeBeforeVideoStarts = 0;
	boolean disableIdleCheck = true;
	private WakeLock mWakeLock;

    public static ArrayList<String> promotionShown = new ArrayList<String>();
    public static ArrayList<String> uniqueAnalytics = new ArrayList<String>();

	// Background Image
	ImageView background_image;

	private class ActivityCheckTimerTask extends TimerTask {

		@Override
		public void run() {
			idleTimeBeforeVideoStarts--;
			// displayTimer();
			if (idleTimeBeforeVideoStarts == 0 && !disableIdleCheck&& !TAdvertBaseDialog.isOpen) {
				AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						super.onPostExecute(result);
						Intent i = new Intent();
						i.setClassName("com.example.td_advert",
								"com.example.td_advert.LauncharScreen");
						i.setAction("com.tadvert.app.start_video");
						i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						CompanyScreen.this.startActivity(i);
						CompanyScreen.this.finish();
					}

				};
				task.execute();
			} else if(TAdvertBaseDialog.isOpen){
                resetIdleTime();
            }
		}
	};

	void displayTimer() {
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				try {
					timeCounter.setText("" + idleTimeBeforeVideoStarts);
				} catch (Exception exception) {
					exception.printStackTrace();
				}

			}
		});
	}

	public void resetIdleTime() {
		idleTimeBeforeVideoStarts = TadvertConstants.idleTimeBeforeLoopStartCompanyScreen;
		disableIdleCheck = false;
	}

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        Util.appendLog("CompanyScreen onCreate() called");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		exceptionManager = new ExceptionManager(this);
		Thread.setDefaultUncaughtExceptionHandler(exceptionManager);

		Intent intent = getIntent();
		int id = intent.getIntExtra("id", 0);

		pd = new ProgressDialog(CompanyScreen.this);
		pd.setMessage("Please wait...");
		pd.setCanceledOnTouchOutside(false);
		pd.setCancelable(false);

        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        TestAdapter mDbHelper = new TestAdapter(CompanyScreen.this);
		mDbHelper.createDatabase();
		mDbHelper.open();

		company = mDbHelper.findCompany(id);
		if (company == null) {
			finish();
		}

		mDbHelper.close();

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.new_main);
		init();

		// welcome.setCompoundDrawablesWithIntrinsicBounds(R.drawable.backicon,
		// 0,
		// 0, 0);
		timeCounter = (TextView) findViewById(R.id.timeCounter);
    }


	private void animateHeader() {
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.feedbacklayer);
		TranslateAnimation translateAnimation = new TranslateAnimation(0, 0,
				-90, 0);
		translateAnimation.setDuration(750);
		translateAnimation.setFillAfter(true);
		linearLayout.startAnimation(translateAnimation);
        Util.appendLog("Animation of menu in CompanyScreen has start");
	}

	@Override
	public void onStart() {
		// pd.show();
		super.onStart();
        Util.appendLog("CompanyScreen onStart() called");
		activityDelayTimer.schedule(activityCheckTimerTask, 10, 1000);
        Util.appendLog("Reset videoloop countdown timer");
		resetIdleTime();

		Analytics.getInstance().companyScreenStarted(company.getName());
		EasyTracker.getInstance(this).activityStart(this);
		EasyTracker.getInstance(this).send(
				MapBuilder.createEvent("screen_event", "company_screen_load",
						company.getName(), null).build());
		saveAnalytic("company_screen_load_" + company.getName(), 1,
				getCompanyIdHashMap());

		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "My Tag");
		mWakeLock.acquire();

		// String videoPath = "android.resource://" + getPackageName() + "/"
		// + R.raw.home_page;
		// playBackgroundVideo(videoPath);
		animateHeader();
		// The rest of your onStart() code.
		// EasyTracker.getInstance().activityStart(this); // Add this method.
		// mGaTracker.sendView("/MainScreen");
	}

	void init() {

		viewFlipper = (ViewFlipper) findViewById(R.id.viewfliper);
		Typeface headerFont = Typeface.createFromAsset(getAssets(),
				"helvetica-neue-thin-1361522098.ttf");
		Typeface tf = headerFont;
		// Typeface.createFromAsset(getAssets(),
		// "helvetica-neue-thin-1361522098.ttf");
		backButton = (FrameLayout) findViewById(R.id.backbuttonView);
		back_button = (TextView) findViewById(R.id.backbutton);
		back_button.setTypeface(headerFont, Typeface.NORMAL);
		feedback_main = (TextView) findViewById(R.id.feedbackmain);
		feedback_main.setTypeface(headerFont, Typeface.NORMAL);
		welcome = findViewById(R.id.welocomeButton);
		// welcome.setTypeface(tf, Typeface.BOLD);
		nu_in_de_bioscope = findViewById(R.id.nu_in_de_bioascope);
		// nu_in_de_bioscope.setTypeface(tf, Typeface.BOLD);
		evenmentan = findViewById(R.id.evenementen);
		// evenmentan.setTypeface(tf, Typeface.BOLD);
		schrijf_je_in = findViewById(R.id.schrjit_win);
		// schrijf_je_in.setTypeface(tf, Typeface.BOLD);
		//
		reservern = findViewById(R.id.reserveen);
		// reservern.setTypeface(tf, Typeface.BOLD);
		mainContainer = (RelativeLayout) findViewById(R.id.MainContainer);
		welocomeScreen = (LinearLayout) findViewById(R.id.linearLayout2);
		DigitalClock dc = (DigitalClock) findViewById(R.id.digitalClock1);
		dc.setTypeface(headerFont, Typeface.BOLD);

		newLayout = (LinearLayout) findViewById(R.id.newLayout);
		newLayout1 = (LinearLayout) findViewById(R.id.newLayout1);
		newLayout2 = (LinearLayout) findViewById(R.id.newLayout2);

		thumbnail_image = (ImageView) findViewById(R.id.videoImageClick);

		layout5Text1 = (TextView) findViewById(R.id.layout5text1);
		layout5Text1.setTypeface(tf, Typeface.BOLD);
		//
		image1 = (ImageView) findViewById(R.id.image1);
		image2 = (ImageView) findViewById(R.id.image2);
		image3 = (ImageView) findViewById(R.id.image3);

		getemailaddress = (EditText) findViewById(R.id.emailaddress);
		getemailaddress.setTypeface(tf, Typeface.BOLD);
		sendemail = (Button) findViewById(R.id.sendemailtoaddress);

		backButton.setOnClickListener(this);
		welcome.setOnClickListener(this);
		nu_in_de_bioscope.setOnClickListener(this);
		evenmentan.setOnClickListener(this);
		schrijf_je_in.setOnClickListener(this);
		reservern.setOnClickListener(this);
		feedback_main.setOnClickListener(this);
		sendemail.setOnClickListener(this);
		thumbnail_image.setOnClickListener(this);

		newLayout.setOnClickListener(this);
		newLayout1.setOnClickListener(this);
		newLayout2.setOnClickListener(this);

		videoPanel = (RelativeLayout) findViewById(R.id.video_panel);
		videoCloseButton = (ImageButton) findViewById(R.id.video_close_button);
		videoCloseButton.setOnClickListener(this);
		playvideo = (VideoView) findViewById(R.id.playvideo);
		backgroundVideo = (VideoView) findViewById(R.id.background_video);

		MediaController mediaController = new MediaController(this);
		mediaController.setAnchorView(playvideo);
		playvideo.setMediaController(new MediaController(this));

		String path = company.getTabs().getHomepage_background();
		background_image = (ImageView) findViewById(R.id.background_image);

		// if (path != null && path.endsWith("mp4")) {
		// this.playBackgroundVideo(path);
		// } else {
		// Bitmap bitmap1 = BitmapFactory.decodeFile(path);
		// if (bitmap1 == null) {
		// throw new RuntimeException("Exception in image creation: "
		// + path);
		// }
		// // Toast.makeText(this,path, Toast.LENGTH_LONG).show();
		//
		// myDrawable = new BitmapDrawable(getResources(), bitmap1);
		//
		// mainContainer.setBackgroundDrawable(myDrawable);
		// }
		this.setBackground(path);

		// TabConfig tabs = company.getTabs();

		// welcome.setText(tabs.getHomepage_title());
		// nu_in_de_bioscope.setText(tabs.getLayout1_title());
		// evenmentan.setText(tabs.getLayout2_title());
		// schrijf_je_in.setText(tabs.getLayout3_title());
		// reservern.setText(tabs.getVideo_title());

        // TEMP ANALYTICS
        ms = new MessageSync(getApplicationContext());
        JSONObject obj = new JSONObject();
        try {
            obj.put("type", "analytics");
            obj.put("name", company.getName() + "_show");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ms.storeMessage(obj);
        // END TEMP

        if(!uniqueAnalytics.contains("unique_" + company.getName() + "_show")){
            obj = new JSONObject();
            try {
                obj.put("type", "analytics");
                obj.put("name", "unique_" + company.getName() + "_show");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ms.storeMessage(obj);
            uniqueAnalytics.add("unique_" + company.getName() + "_show");
        }

        if(!company.getTabs().getPromotion_popup().equals("0") && !promotionShown.contains(Integer.toString(company.getCompanyId())) && PhoneStatus.fetchStatus(this).getVersionCode() >= 2005){
            PromotionDialog promotionDialog = new PromotionDialog(this, (ViewGroup) findViewById(R.id.MainContainer));
            promotionDialog.loadHTML(company.getTabs().getPromotion_popup());

            //TEMPORARY FOR CHECKING NOK AIR
            obj = new JSONObject();
            try {
                obj.put("type", "analytics");
                obj.put("name", "promotion_popup");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ms.storeMessage(obj);

            if(!uniqueAnalytics.contains("unique_" + "promotion_popup_unique")){
                obj = new JSONObject();
                try {
                    obj.put("type", "analytics");
                    obj.put("name", "unique_" + "promotion_popup_unique");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ms.storeMessage(obj);
                uniqueAnalytics.add("unique_" + "promotion_popup_unique");
            }
            // END TEMP

            EasyTracker.getInstance(this).send(
                    MapBuilder.createEvent("promotion_popup",
                    company.getName() + "_promotion", "Show promotion",
                    null).build());
        }
	}

    private boolean inVideoPlayer = true;
    final Handler handler = new Handler();
	private void setBackground(String path) {

        Util.appendLog("Called setBackground for:" + path);

		if (path != null && path.endsWith("mp4")) {
			Uri uri = Uri.parse(path);

            backgroundVideo.setVideoURI(uri);
			backgroundVideo.setEnabled(true);
			backgroundVideo.setClickable(true);

            backgroundVideo.start();


            if(!inVideoPlayer){
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        background_image.setVisibility(View.INVISIBLE);
                        backgroundVideo.setVisibility(View.VISIBLE);
                        backgroundVideo.setAlpha(1f);
                        inVideoPlayer = true;
                    }
                }, 200);
            }
		} else {

			backgroundVideo.stopPlayback();
			backgroundVideo.setAlpha(0f);

			Bitmap bitmap1 = BitmapFactory.decodeFile(path);
			background_image.setImageBitmap(bitmap1);
			background_image.setVisibility(View.VISIBLE);
            inVideoPlayer = false;

			/*
			 * Bitmap bitmap1 = BitmapFactory.decodeFile(path); if (bitmap1 ==
			 * null) { throw new
			 * RuntimeException("Exception in image creation: " + path); } //
			 * Toast.makeText(this,path, Toast.LENGTH_LONG).show();
			 * 
			 * //myDrawable = new BitmapDrawable(getResources(),
			 * Bitmap.createScaledBitmap(bitmap1, 1600, 900, true));
			 * //mainContainer.setBackgroundDrawable(myDrawable);
			 */

		}

	}

	// private void setBG(String path) {
	// Bitmap bitmap1 = BitmapFactory.decodeFile(path);
	// if (bitmap1 == null) {
	// throw new RuntimeException("Exception in image creation: " + path);
	// }
	//
	// myDrawable = new BitmapDrawable(getResources(), bitmap1);
	// mainContainer.setBackgroundDrawable(myDrawable);
	// }

	@Override
	public void onClick(View v) {
		Analytics.getInstance().screenClicked();
		Analytics.getInstance().companyClicked();

		EasyTracker.getInstance(CompanyScreen.this).send(
				MapBuilder.createEvent("SCREEN_TOUCH",
						Analytics.COMPANY_SCREEN_TOUCH_EVENT,
						company.getName(), 1l).build());

		saveAnalytic(
				Analytics.COMPANY_SCREEN_TOUCH_EVENT + "(" + company.getName()
						+ ")", 1, getCompanyIdHashMap());

		resetIdleTime();
		playvideo.pause();
		playvideo.clearFocus();
		videoPanel.setVisibility(View.INVISIBLE);

		TabConfig tabs = company.getTabs();

		viewFlipper.setVisibility(ViewFlipper.INVISIBLE);
		switch (v.getId()) {

		case R.id.backbuttonView:
            Util.appendLog("User click home button from CompanyScreen");
            LauncharScreen.isFromCompanyBackScreen = true;
            startActivityForResult(
                    new Intent(Intent.ACTION_PICK).setClassName("com.example.td_advert", "com.example.td_advert.LauncharScreen").setAction("com.tadvert.app.company_back_pressed"), 1
            );

            //CompanyScreen.this.startActivity(i);
            CompanyScreen.this.finish();

			// finish();
			break;
		case R.id.welocomeButton:
            try {
                viewFlipper.setVisibility(ViewFlipper.VISIBLE);
                viewFlipper.setDisplayedChild(0);

                // setBG(tabs.getHomepage_background());
                EasyTracker.getInstance(this).send(
                        MapBuilder.createEvent("company_screen_tab_click",
                                company.getName() + "_tab_click", "Welcome",
                                null).build());
                saveAnalytic("welcome_tab_click (" + company.getName() + ")",
                        1, getCompanyIdHashMap());
                this.setBackground(tabs.getHomepage_background());

                JSONObject obj = new JSONObject();
                try {
                    obj.put("type", "analytics");
                    obj.put("name", "welcome_tab_click (" + company.getName() + ")");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ms.storeMessage(obj);

                if(!uniqueAnalytics.contains("unique_" + "welcome_tab_click (" + company.getName() + ")")){
                    obj = new JSONObject();
                    try {
                        obj.put("type", "analytics");
                        obj.put("name", "unique_" + "welcome_tab_click (" + company.getName() + ")");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ms.storeMessage(obj);
                    uniqueAnalytics.add("unique_" + "welcome_tab_click (" + company.getName() + ")");
                }


                if (backgroundVideo.getAlpha() != 0f) {
                    MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                    mediaMetadataRetriever.setDataSource(tabs
                            .getHomepage_background());
                    String str = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                    int time = Integer.parseInt(str);
                    backgroundVideo.seekTo(time);
                }

            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }

			break;
		case R.id.nu_in_de_bioascope:
		case R.id.newLayout:
            if (company.getTabs().getPlaceholder() == 1) {
                return;
            }

            try {
                EasyTracker.getInstance(this).send(
                        MapBuilder.createEvent("company_screen_tab_click",
                                company.getName() + "_tab_click", "Tab1", null)
                                .build());

                saveAnalytic("tab1_click (" + company.getName() + ")", 1,
                        getCompanyIdHashMap());

                JSONObject obj = new JSONObject();
                try {
                    obj.put("type", "analytics");
                    obj.put("name", "tab1_click (" + company.getName() + ")");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ms.storeMessage(obj);

                if(!uniqueAnalytics.contains("unique_" + "tab1_click (" + company.getName() + ")")){
                    obj = new JSONObject();
                    try {
                        obj.put("type", "analytics");
                        obj.put("name", "unique_" + "tab1_click (" + company.getName() + ")");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ms.storeMessage(obj);
                    uniqueAnalytics.add("unique_" + "tab1_click (" + company.getName() + ")");
                }

                this.setBackground(tabs.getLayout1_background());
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }

			break;
		case R.id.evenementen:
		case R.id.newLayout1:
            if (company.getTabs().getPlaceholder() == 1) {
                return;
            }

            try {
                // viewFlipper.setDisplayedChild(2);
                // welcome.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                // nu_in_de_bioscope.setCompoundDrawablesWithIntrinsicBounds(0,
                // 0,
                // 0, 0);
                // evenmentan.setCompoundDrawablesWithIntrinsicBounds(
                // R.drawable.backicon, 0, 0, 0);
                // schrijf_je_in.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                // 0,
                // 0);
                // reservern.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0,
                // 0);
                // setBG(tabs.getLayout2_background());
                EasyTracker.getInstance(this).send(
                        MapBuilder.createEvent("company_screen_tab_click",
                                company.getName() + "_tab_click", "Tab2", null)
                                .build());

                saveAnalytic("tab2_click (" + company.getName() + ")", 1,
                        getCompanyIdHashMap());

                JSONObject obj = new JSONObject();
                try {
                    obj.put("type", "analytics");
                    obj.put("name", "tab2_click (" + company.getName() + ")");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ms.storeMessage(obj);

                if(!uniqueAnalytics.contains("unique_" + "tab2_click (" + company.getName() + ")")){
                    obj = new JSONObject();
                    try {
                        obj.put("type", "analytics");
                        obj.put("name", "unique_" + "tab2_click (" + company.getName() + ")");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ms.storeMessage(obj);
                    uniqueAnalytics.add("unique_" + "tab2_click (" + company.getName() + ")");
                }

                this.setBackground(tabs.getLayout2_background());
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }

			break;
		case R.id.newLayout2:
		case R.id.schrjit_win:
            if (company.getTabs().getPlaceholder() == 1) {
                return;
            }

            try {
                // viewFlipper.setDisplayedChild(3);
                // welcome.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                // nu_in_de_bioscope.setCompoundDrawablesWithIntrinsicBounds(0,
                // 0,
                // 0, 0);
                // evenmentan.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0,
                // 0);
                // schrijf_je_in.setCompoundDrawablesWithIntrinsicBounds(
                // R.drawable.backicon, 0, 0, 0);
                // reservern.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0,
                // 0);
                // setBG(tabs.getLayout3_background());
                EasyTracker.getInstance(this).send(
                        MapBuilder.createEvent("company_screen_tab_click",
                                company.getName() + "_tab_click", "Tab3", null)
                                .build());

                saveAnalytic("tab3_click (" + company.getName() + ")", 1,
                        getCompanyIdHashMap());

                JSONObject obj = new JSONObject();
                try {
                    obj.put("type", "analytics");
                    obj.put("name", "tab3_click (" + company.getName() + ")");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ms.storeMessage(obj);

                if(!uniqueAnalytics.contains("unique_" + "tab3_click (" + company.getName() + ")")){
                    obj = new JSONObject();
                    try {
                        obj.put("type", "analytics");
                        obj.put("name", "unique_" + "tab3_click (" + company.getName() + ")");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ms.storeMessage(obj);
                    uniqueAnalytics.add("unique_" + "tab3_click (" + company.getName() + ")");
                }

                this.setBackground(tabs.getLayout3_background());
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }

			break;

		case R.id.video_close_button:
			videoPanel.setVisibility(View.INVISIBLE);
			playvideo.stopPlayback();
			viewFlipper.destroyDrawingCache();
			// viewFlipper.buildDrawingCache();
			viewFlipper.setVisibility(ViewFlipper.VISIBLE);
			viewFlipper.setDisplayedChild(4);
			viewFlipper.dispatchWindowVisibilityChanged(ViewFlipper.VISIBLE);
            enableOtherButton();
			break;
		case R.id.reserveen:
            if (company.getTabs().getPlaceholder() == 1) {
                return;
            }

            try {
                layout5Text1.setText(tabs.getVideo_text());
                String path = tabs.getVideo_bg();

                if (path != null && !path.equals("") && !path.endsWith(".")
                        && !path.endsWith("/")) {
                    // setBG(path);
                    this.setBackground(path);
                } else {
                    // setBG(tabs.getHomepage_background());
                    this.setBackground(tabs.getHomepage_background());
                }

                viewFlipper.destroyDrawingCache();
                // viewFlipper.buildDrawingCache();
                viewFlipper.setVisibility(ViewFlipper.VISIBLE);
                viewFlipper.setDisplayedChild(4);
                viewFlipper
                        .dispatchWindowVisibilityChanged(ViewFlipper.VISIBLE);
                ImageView thumbnail = (ImageView) findViewById(R.id.videoImageClick);
                path = tabs.getVideo_thumb();
                if (path != null && !path.equals("") && !path.endsWith(".")
                        && !path.endsWith("/")) {
                    Bitmap bitmap1 = BitmapFactory.decodeFile(tabs
                            .getVideo_thumb());
                    if (bitmap1 == null) {
                        throw new RuntimeException(
                                "Exception in image creation: " + path);
                    }
                    thumbnail.setBackground(new BitmapDrawable(getResources(),
                            bitmap1));
                }

                // welcome.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                // nu_in_de_bioscope.setCompoundDrawablesWithIntrinsicBounds(0,
                // 0,
                // 0, 0);
                // evenmentan.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0,
                // 0);
                // schrijf_je_in.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                // 0,
                // 0);
                // reservern.setCompoundDrawablesWithIntrinsicBounds(
                // R.drawable.backicon, 0, 0, 0);
                EasyTracker.getInstance(this).send(
                        MapBuilder.createEvent("company_screen_tab_click",
                                company.getName() + "_tab_click", "VideoTab",
                                null).build());

                saveAnalytic("video_tab_click (" + company.getName() + ")", 1,
                        getCompanyIdHashMap());

                JSONObject obj = new JSONObject();
                try {
                    obj.put("type", "analytics");
                    obj.put("name", "video_tab_click (" + company.getName() + ")");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ms.storeMessage(obj);

                if(!uniqueAnalytics.contains("unique_" + "video_tab_click (" + company.getName() + ")")){
                    obj = new JSONObject();
                    try {
                        obj.put("type", "analytics");
                        obj.put("name", "unique_" + "video_tab_click (" + company.getName() + ")");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ms.storeMessage(obj);
                    uniqueAnalytics.add("unique_" + "video_tab_click (" + company.getName() + ")");
                }

                // if (backgroundVideo.isShown()) {
                // backgroundVideo.setVisibility(View.INVISIBLE);
                // }

            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
			break;

		case R.id.feedbackmain:
            viewFlipper.setVisibility(ViewFlipper.VISIBLE);
            TAdvertBaseDialog feedbackDialog = new FeedbackDialog(this, (ViewGroup) findViewById(R.id.MainContainer));
            feedbackDialog.show();
			break;

		case R.id.sendemailtoaddress:
            viewFlipper.setVisibility(ViewFlipper.VISIBLE);
            if (!isEmailValid(getemailaddress.getText().toString())) {
            } else {
                saveEmail();
            }
			break;

		case R.id.videoImageClick:
            videoPanel.setVisibility(View.VISIBLE);
            // setimagetoback();
            viewFlipper.setVisibility(ViewFlipper.VISIBLE);
            viewFlipper.setDisplayedChild(5);
            disableOtherButton();
            getVideos(tabs.getVideo());
            EasyTracker.getInstance(this).send(
                    MapBuilder.createEvent("company_screen_tab_click",
                            company.getName() + "_tab_click", "VideoThumbnail",
                            null).build());
            saveAnalytic("VideoThumbnail_click (" + company.getName() + ")", 1,
                    getCompanyIdHashMap());


            JSONObject obj = new JSONObject();
            try {
                obj.put("type", "analytics");
                obj.put("name", "VideoThumbnail_click (" + company.getName() + ")");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ms.storeMessage(obj);

            if(!uniqueAnalytics.contains("unique_" + "VideoThumbnail_click (" + company.getName() + ")")){
                obj = new JSONObject();
                try {
                    obj.put("type", "analytics");
                    obj.put("name", "unique_" + "VideoThumbnail_click (" + company.getName() + ")");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ms.storeMessage(obj);
                uniqueAnalytics.add("unique_" + "VideoThumbnail_click (" + company.getName() + ")");
            }

			break;

		}
		//System.gc();
	}

    private void disableOtherButton() {
        backButton.setClickable(false);
        welcome.setClickable(false);
        nu_in_de_bioscope.setClickable(false);
        evenmentan.setClickable(false);
        schrijf_je_in.setClickable(false);
        reservern.setClickable(false);
        feedback_main.setClickable(false);
        sendemail.setClickable(false);
        thumbnail_image.setClickable(false);

        newLayout.setClickable(false);
        newLayout1.setClickable(false);
        newLayout2.setClickable(false);
    }

    private void enableOtherButton() {
        backButton.setClickable(true);
        welcome.setClickable(true);
        nu_in_de_bioscope.setClickable(true);
        evenmentan.setClickable(true);
        schrijf_je_in.setClickable(true);
        reservern.setClickable(true);
        feedback_main.setClickable(true);
        sendemail.setClickable(true);
        thumbnail_image.setClickable(true);

        newLayout.setClickable(true);
        newLayout1.setClickable(true);
        newLayout2.setClickable(true);
    }

    void saveEmail() {
		String emailAddress = getemailaddress.getText().toString();
		String companyName = company.getName();

		final UserEmail dto = new UserEmail(companyName, emailAddress);

		if (LauncharScreen.isConnectingToInternet(this)) {
			String url = TadvertConstants.url + "send_user_email";
			WebTask task = new WebTask(url, dto.toPostParams(),
					new WebTaskDelegate() {

						@Override
						public void onError(Exception e) {
							saveEmailToDB(dto);
						}

					});
			task.execute();
		} else {
			saveEmailToDB(dto);
		}
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getemailaddress.getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);

		AlertDialog alertDialog = new AlertDialog.Builder(this)
				.setMessage("\nThank you for your contacting us\n")
				.setTitle("Email Saved")
				.setPositiveButton("Ok",
						new android.content.DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								CompanyScreen.this.onClick(findViewById(R.id.welocomeButton));
								getemailaddress.setText("");
								dialog.dismiss();
							}

						}).create();
		alertDialog.show();

	}

	void saveEmailToDB(UserEmail dto) {

		TestAdapter mDbHelper = new TestAdapter(this);
		mDbHelper.createDatabase();
		mDbHelper.open();

		mDbHelper.saveUserEmail(dto);

		mDbHelper.close();

	}

	@Override
	protected void onStop() {
		// EasyTracker.getInstance().activityStop(this);
		CompanyScreen.isOpen = false;
		Log.d(getClass().getName(), "onStop");
		super.onStop();
		mWakeLock.release();
		Analytics.getInstance().companyScreenEnded();
		EasyTracker.getInstance(this).send(
				MapBuilder.createEvent("screen_event",
						Analytics.COMPANY_SCREEN_DURATION_EVENT,
						company.getName(),
						Analytics.getInstance().getCompanyScreenDuration())
						.build());

		saveAnalytic(Analytics.COMPANY_SCREEN_DURATION_EVENT, Analytics
				.getInstance().getCompanyScreenDuration(),
				getCompanyIdHashMap());

		EasyTracker.getInstance(this).activityStop(this);
		this.finish();
	}

	public Context getDialogContext() {
		Context context;
		if (getParent() != null)
			context = getParent();
		else
			context = this;
		return context;
	}

	void getValuefromDataBase() {

		TestAdapter mDbHelper = new TestAdapter(CompanyScreen.this);
		mDbHelper.createDatabase();

		mDbHelper.open();

		Cursor testTabsdata = mDbHelper
				.getTabsDetail(company.getCompanyId() + 1);

		// System.out.println("Count>>>>>>>>>>>" + testTabsdata.getCount());
		if (testTabsdata.getCount() > 0) {
			if (testTabsdata.moveToFirst()) {
				// do {
				String videoName = Utility
						.GetColumnValue(testTabsdata, "Video");
				String companyType = Utility.GetColumnValue(testTabsdata,
						"Type");
				String companyTitle = Utility.GetColumnValue(testTabsdata,
						"Title");
				String companyImage1 = Utility.GetColumnValue(testTabsdata,
						"Image1");
				String companyText1 = Utility.GetColumnValue(testTabsdata,
						"Text1");
				String companyImage2 = Utility.GetColumnValue(testTabsdata,
						"Image2");
				String companyText2 = Utility.GetColumnValue(testTabsdata,
						"Text2");
				String companyImage3 = Utility.GetColumnValue(testTabsdata,
						"Image3");
				String companyText3 = Utility.GetColumnValue(testTabsdata,
						"Text3");
				String companyImage4 = Utility.GetColumnValue(testTabsdata,
						"Image4");
				String companyText4 = Utility.GetColumnValue(testTabsdata,
						"Text4");
				String primaryId = Utility.GetColumnValue(testTabsdata, "Id");
				// String _heading1 = Utility.GetColumnValue(testTabsdata,
				// "Heading1");
				// String _heading2 = Utility.GetColumnValue(testTabsdata,
				// "Heading2");
				// String _heading3 = Utility.GetColumnValue(testTabsdata,
				// "Heading3");
				// System.out
				// .println(">>>>>>>GetDataFromDataBase>>>>>>>>>>"
				// + videoName + "  " + companyType + "  "
				// + companyTitle + "  " + companyImage1
				// + "  " + companyText1 + "  "
				// + companyImage2 + "  " + companyText2
				// + "  " + companyImage3 + "  "
				// + companyText3 + "   " + primaryId + "   "
				// + _heading1 + "   " + _heading2 + "   "
				// + _heading3);
				// if (companyType.equalsIgnoreCase("homepage")) {
				// welcome.setText(" " + companyTitle);
				// heading1.setText(_heading1);
				// heading2.setText(_heading2);
				// heading3.setText(_heading3);
				Cursor tabsContentCursor = mDbHelper.getTabContent(
						company.getCompanyId() + 1, 0);
				if (tabsContentCursor.moveToFirst()) {
					// newLayoutText.setText(Utility.GetColumnValue(
					// tabsContentCursor, "heading1"));
					// newLayoutText1.setText(Utility.GetColumnValue(
					// tabsContentCursor, "heading2"));
					// newLayoutText2.setText(Utility.GetColumnValue(
					// tabsContentCursor, "heading3"));
				}
				tabsContentCursor.close();
				tablecol.setWelcome(Integer.parseInt(primaryId));
				// } else if (companyType.equalsIgnoreCase("layout1")) {
				// nu_in_de_bioscope.setText(" " + companyText1);
				// layoutTwoText.setText(companyText1);
				// tablecol.setLayout1(Integer.parseInt(primaryId));
				// } else if (companyType.equalsIgnoreCase("layout2")) {
				// evenmentan.setText(" " + companyText2);
				// layout3Head1.setText(_heading1);
				// layout3Head2.setText(_heading2);
				// layout3Text1.setText(companyText1);
				// layout3Text2.setText(companyText2);
				// tablecol.setLayout2(Integer.parseInt(primaryId));
				// } else if (companyType.equalsIgnoreCase("layout3")) {
				// schrijf_je_in.setText(" " + companyText3);
				// tablecol.setLayout3(Integer.parseInt(primaryId));
				// } else if (companyType.equalsIgnoreCase("layout4")) {
				// reservern.setText(" " + companyText4);

				tabsContentCursor = mDbHelper.getTabContent(
						company.getCompanyId() + 1, 4);
				if (tabsContentCursor.moveToFirst()) {
					layout5Text1.setText(Utility.GetColumnValue(
							tabsContentCursor, "heading1"));

				}
				tabsContentCursor.close();
				// layout5Text1.setText(companyText1);
				// tablecol.setLayout4(Integer.parseInt(primaryId));
				// }
				// } while (testTabsdata.moveToNext());
				testTabsdata.close();
			}
		}
		mDbHelper.close();
	}

	void individualtabImages(int CId, int Id, ImageView image1,
			ImageView image2, ImageView image3, ImageView image4) {

		TestAdapter mDbHelper = new TestAdapter(CompanyScreen.this);
		mDbHelper.createDatabase();
		mDbHelper.open();

		Cursor testBackgrounddata = mDbHelper.getUpdatedImages(CId, Id);
		// System.out.println("******count****" +
		// testBackgrounddata.getCount());
		String ImagePath1, ImagePath2, ImagePath3, ImagePath4;
		if (testBackgrounddata.getCount() > 0) {
			if (testBackgrounddata.moveToFirst()) {
				do {
					ImagePath1 = Utility.GetColumnValue(testBackgrounddata,
							"Image1");
					try {
						Bitmap bitmap1 = BitmapFactory.decodeFile(ImagePath1);
						image1.setImageBitmap(bitmap1);
						// bgs.add(bitmap1);
					} catch (Exception e) {
						e.printStackTrace();
					}
					ImagePath2 = Utility.GetColumnValue(testBackgrounddata,
							"Image2");
					try {
						Bitmap bitmap2 = BitmapFactory.decodeFile(ImagePath2);
						image2.setImageBitmap(bitmap2);
						// bgs.add(bitmap2);
					} catch (Exception e) {
						e.printStackTrace();
					}
					ImagePath3 = Utility.GetColumnValue(testBackgrounddata,
							"Image3");
					try {
						Bitmap bitmap3 = BitmapFactory.decodeFile(ImagePath3);
						image3.setImageBitmap(bitmap3);
						// bgs.add(bitmap3);
					} catch (Exception e) {
						e.printStackTrace();
					}
					ImagePath4 = Utility.GetColumnValue(testBackgrounddata,
							"Image4");
					try {
						Bitmap bitmap4 = BitmapFactory.decodeFile(ImagePath4);
						image4.setImageBitmap(bitmap4);
						// bgs.add(bitmap4);
					} catch (Exception e) {
						e.printStackTrace();
					}
					// System.out.println(">>>>>>>GetImages>>>>>>>>>> 1 "
					// + ImagePath1 + " 2 " + ImagePath2 + " 3 "
					// + ImagePath3 + " 4 " + ImagePath4);
				} while (testBackgrounddata.moveToNext());

			}
			// pd.dismiss();
			testBackgrounddata.close();
		} else {
			Toast.makeText(CompanyScreen.this, "No Data Available",
					Toast.LENGTH_SHORT).show();
		}
	}

	private boolean isEmailValid(String email) {
		String regExpn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
				+ "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
				+ "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
				+ "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
				+ "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
				+ "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

		CharSequence inputStr = email;

		Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);

		if (matcher.matches())
			return true;
		else
			Toast.makeText(this, "Please enter valid email id.",
					Toast.LENGTH_LONG).show();
		return false;
	}

	void getVideos(String videoPath) {

		File videoFile = new File(videoPath);
		if (videoFile.exists()) {

			backgroundVideo.setVisibility(View.INVISIBLE);

			playvideo.setEnabled(true);
			playvideo.setActivated(true);
			playvideo.setVideoPath(videoPath);
			playvideo.requestFocus();
            playvideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setVolume(0f, 0f);
                }
            });
			playvideo.start();

		} else {
			Toast.makeText(CompanyScreen.this,
					"No Video Available at path: " + videoPath,
					Toast.LENGTH_LONG).show();
			finish();
		}

		playvideo
				.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

					@Override
					public void onCompletion(MediaPlayer mp) {
                        enableOtherButton();
                        viewFlipper.setDisplayedChild(4);
						playvideo.invalidate();
					}
				});
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	public void onPause() {
		super.onPause();
		//overridePendingTransition(R.anim.screen_fade_in_animation, R.anim.screen_fade_out_animation);
	}

	public void onDestroy() {

		activityDelayTimer.cancel();
		activityCheckTimerTask.cancel();

		activityDelayTimer = null;
		activityCheckTimerTask = null;

		for (int i = 0; i < bgsAll.size(); i++) {
			Bitmap bmp = bgsAll.get(i);
			if (bmp != null) {
				bmp.recycle();
			}
		}
		bgs.clear();
		bgsAll.clear();
		myDrawable = null;

		mainContainer.setBackgroundDrawable(myDrawable);
		mainContainer.removeAllViews();
		mainContainer = null;
		exceptionManager.clearContext();

		thumbnail_image.setBackgroundDrawable(null);
		thumbnail_image.setImageDrawable(null);
		thumbnail_image = null;

		newLayout.removeView(image1);
		newLayout.removeView(image2);
		newLayout.removeView(image3);
		newLayout = null;
		newLayout1.removeAllViews();
		newLayout1 = null;
		newLayout2.removeAllViews();
		newLayout2 = null;

		// image1.getDrawable()
		image1.setImageDrawable(null);
		image1 = null;

		image2.setImageDrawable(null);
		image2 = null;

		image3.setImageDrawable(null);
		image3 = null;

		//System.gc();
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {

		// super.onBackPressed();
		/*Intent i = new Intent();
		i.setClassName("com.example.td_advert",
				"com.example.td_advert.LauncharScreen");
		i.setAction("com.tadvert.app.start_video");
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		CompanyScreen.this.startActivity(i);
		CompanyScreen.this.finish();*/

        LauncharScreen.isFromCompanyBackScreen = true;
        startActivityForResult(
                new Intent(Intent.ACTION_PICK).setClassName("com.example.td_advert", "com.example.td_advert.LauncharScreen").setAction("com.tadvert.app.company_back_pressed"), 1
        );

        //CompanyScreen.this.startActivity(i);
        CompanyScreen.this.finish();
	}

	public HashMap<String, String> getCompanyIdHashMap() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("company_id", Integer.toString(company.getCompanyId()));
		return map;
	}
}
