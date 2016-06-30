package com.example.td_advert;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.td_advert.analytics.Analytics;
import com.example.td_advert.animation.Animation;
import com.example.td_advert.animation.AnimationSequence;
import com.example.td_advert.animation.MultiAnimationExecutor;
import com.example.td_advert.bean.AnalyticEvent;
import com.example.td_advert.bean.AppState;
import com.example.td_advert.bean.Company;
import com.example.td_advert.bean.Feedback;
import com.example.td_advert.bean.UserEmail;
import com.example.td_advert.constant.TadvertConstants;
import com.example.td_advert.database.LogSync;
import com.example.td_advert.database.MessageSync;
import com.example.td_advert.database.TaxiDetails;
import com.example.td_advert.database.TestAdapter;
import com.example.td_advert.dialog.ConfigurationDialog;
import com.example.td_advert.dialog.FeedbackDialog;
import com.example.td_advert.dialog.TAdvertBaseDialog;
import com.example.td_advert.exception.ExceptionManager;
import com.example.td_advert.exception.PhoneStatus;
import com.example.td_advert.pupups.StartPopup;
import com.example.td_advert.task.AbstractTask;
import com.example.td_advert.task.SchedulerTask;
import com.example.td_advert.task.VideoLoopTask;
import com.example.td_advert.thread.VideoLoopThread;
import com.example.td_advert.thread.VideoLoopThread.VideoLooperCallback;
import com.example.td_advert.util.BrightnessManager;
import com.example.td_advert.util.PrelaunchSetup;
import com.example.td_advert.util.Util;
import com.example.td_advert.view.CircularImageView;
import com.example.td_advert.web.WebTask;
import com.example.td_advert.web.delegate.DownloadDelegate;
import com.example.td_advert.web.delegate.WebTaskDelegate;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class LauncharScreen extends BaseActivity
		implements OnClickListener, View.OnTouchListener, VideoLooperCallback, OnMapReadyCallback, LocationListener {

	com.example.td_advert.view.CircularImageView jtImage, pietImage,
			sandtonImage, vrielinkImage, vvvImage, vwimage2, vwimage3,
			vwimage4, vwimage5, vwimage6, videoButton;
	ArrayList<CircularImageView> iconsList = new ArrayList<CircularImageView>();
	ArrayList<View> companyIconParentList = new ArrayList<View>();
	VideoView videoLoopView = null, bgSkyVideo;
	RelativeLayout videoLoopParent = null, videoViewContainer = null;
	TextView jtText, pietText, sandtonText, vrielinkText, vvvText,
			continueVideoLoop, brightnessPercentageText;
	TextView timeCounter;
	RelativeLayout feedBack, contactUs, closeArea,
			brightnessButton = null;
    FrameLayout trailerView;
	AlertDialog categoryDialog;
	TextClock dc;
	Boolean isNight = false;
	private ImageView closeApp = null;
	private GoogleMap mMap;
	private VideoLoopThread videoLoopThread = null;
	public static HashMap<String, String> tabImagesMap = new HashMap<String, String>();
	// private GoogleAnalytics mGaInstance;
	// private Tracker mGaTracker;
	// static float ratingValue;
	int VersionNumber = 0, vlIterationNo;
	SharedPreferences PREF;
	public static boolean versionNumber;
	Drawable myDrawable;
	ProgressDialog pd;
	FrameLayout feedback;
	boolean isCheckingVersion = false;

	TextView piet_text, jt_text, vrielink_text, sandton_text, vw_text,
			vw_text2, vw_text3, vw_text4, vw_text5, vw_text6, videoButtonText;

	boolean disableIdleCheck = true;
    boolean disableClick = false;
	int idleTimeBeforeVideoStarts = 0;
	private WakeLock mWakeLock;

	SchedulerTask schedulerTask = new SchedulerTask();

	Timer activityDelayTimer = new Timer();
	TimerTask activityCheckTimerTask = new ActivityCheckTimerTask();
	private int closeClickTimer = 0;

    private LogSync logSync;

    // Welcome Screen Variables
    ImageView welcome_road;
    ImageView bkk_bg;
    ImageView taxi;
    ImageView touchToStart;
    ImageView welcomeText;
	ImageView taxilogos;
	//ImageView nestologo;
    RelativeLayout welcomeScreenHolder;
	RelativeLayout parentView;
    Handler handler;

    public static boolean skipVideoLoop = false;
    public static boolean isFromCompanyBackScreen = false;
    public static boolean isDownloading = false;
    private boolean hideWelcomeScreen = false;
	private String monthString = "";
	private String weekdayString = "";
	private String dateString = "";
	TextView textClockDay;
    PopupWindow popupWindow;

	//TestIcon Here
	ImageView testQuestionaires;
	ImageView wifiIcon;
	ImageView logo;
	private View glare;
	private FrameLayout topWhiteBar;
	private ImageView ivCalendar;
	private TextClock dclock;
	private TextView feedBackTextView;
	private ImageView ivFeedBack;
	private TextView sleepTextView;
	private ImageView ivBrightness;
	private ImageView ivVideo;
	private TextView contactUsTextView;
	private ImageView backgroundImage;
	private ImageView ivContactUs;
	private GoogleMap googleMap;
	private Location mLastLocation;

	public void onMapSearch(View view) {
		EditText locationSearch = (EditText) findViewById(R.id.editText);
		String location = locationSearch.getText().toString();
		List<Address> addressList = null;

		if (location != null || !location.equals("")) {
			Geocoder geocoder = new Geocoder(this);
			try {
				addressList = geocoder.getFromLocationName(location, 1);

			} catch (IOException e) {
				e.printStackTrace();
			}
			Address address = addressList.get(0);
			LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
			mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
			mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
		}
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;
		mMap.setMyLocationEnabled(true);
		if(mLastLocation==null) return;
		double latitude = mLastLocation.getLatitude();
		double longitude = mLastLocation.getLongitude();
		LatLng latLng = new LatLng(latitude, longitude);
		googleMap.addMarker(new MarkerOptions().position(latLng));
		googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
		googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
	}

	@Override
	public void onLocationChanged(Location location) {
		mLastLocation = location;
		if(mMap==null) return;
		double latitude = location.getLatitude();
		double longitude = location.getLongitude();
		LatLng latLng = new LatLng(latitude, longitude);
		mMap.addMarker(new MarkerOptions().position(latLng));
		mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
		mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
	}

	@Override
	public void onStatusChanged(String s, int i, Bundle bundle) {

	}

	@Override
	public void onProviderEnabled(String s) {

	}

	@Override
	public void onProviderDisabled(String s) {

	}

	//
	private class ActivityCheckTimerTask extends TimerTask {

		@Override
		public void run() {
			idleTimeBeforeVideoStarts--;
			Log.e("idleTimeBeforeVideoStarts", "idleTimeBeforeVideoStarts: " + idleTimeBeforeVideoStarts);
			displayTimer("" + idleTimeBeforeVideoStarts);
			if (idleTimeBeforeVideoStarts <= 0 && !disableIdleCheck && !TAdvertBaseDialog.isOpen) {
				AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						super.onPostExecute(result);
						useHandler();
					}

				};
				task.execute();
			} else {
				if (idleTimeBeforeVideoStarts <= 0) {
					String msg = "" + !disableIdleCheck + "-"
							+ !TAdvertBaseDialog.isOpen;
					displayTimer(msg);
				}

			}
		}
	};

	void displayTimer(final String str) {
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				try {
					timeCounter.setText(str);
				} catch (Exception exception) {
					exception.printStackTrace();
				}

			}
		});
	}

	public void resetIdleTime() {
        Util.appendLog("Reset video loop timer to " + TadvertConstants.idleTimeBeforeLoopStartLauncherScreen);
		idleTimeBeforeVideoStarts = TadvertConstants.idleTimeBeforeLoopStartLauncherScreen;
		disableIdleCheck = false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		changeTheme(true, true);
	}

	/**
	 * Detect night or day then change theme
	 * @param detectNight
	 * @param forceNight
	 */
	private void changeTheme(boolean detectNight, boolean forceNight) {
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		isNight = hour < 6 || hour > 18;
		Boolean nightTheme;
		if(detectNight)
			nightTheme = isNight;
		else
			nightTheme = forceNight;
		isNight = nightTheme;

		int white = getResources().getColor(R.color.white);
		int daycolor = getResources().getColor(R.color.day_text_color);
		if(nightTheme)
		{
			glare.setVisibility(View.GONE);
			logo.setImageResource(R.drawable.tadvert_logo_night);
			logo.setPadding(0, 0, 0, 0);
			topWhiteBar.setBackgroundResource(R.drawable.night_bar);
			ivCalendar.setImageResource(R.drawable.calendar_night);
			dclock.setTextColor(white);
			textClockDay.setTextColor(white);
			feedBackTextView.setTextColor(white);
			ivFeedBack.setImageResource(R.drawable.feedback_night);
			closeApp.setImageResource(R.drawable.sleep_night);
			sleepTextView.setTextColor(white);
			brightnessPercentageText.setTextColor(white);
			ivBrightness.setImageResource(R.drawable.brightness_night);
			ivVideo.setImageResource(R.drawable.theatre_night);
			continueVideoLoop.setTextColor(white);
			contactUsTextView.setTextColor(white);
			ivContactUs.setImageResource(R.drawable.contact_us_night);
			parentView.setBackgroundResource(R.drawable.star_still);
			backgroundImage.setBackgroundResource(R.drawable.star_still);
			if(bgSkyVideo.isPlaying()) {
				bgSkyVideo.stopPlayback();
			}
			bgSkyVideo.setVideoURI(Uri.parse("android.resource://"
					+ getPackageName() + "/" + R.raw.star_loop));
			bgSkyVideo.start();
		} else {
			glare.setVisibility(View.VISIBLE);
			logo.setImageResource(R.drawable.nesto_white1);
			logo.setPadding(0, 0, 0, 0);
			topWhiteBar.setBackgroundResource(R.drawable.white_bar);
			ivCalendar.setImageResource(R.drawable.calendar);
			dclock.setTextColor(daycolor);
			textClockDay.setTextColor(daycolor);
			feedBackTextView.setTextColor(daycolor);
			ivFeedBack.setImageResource(R.drawable.feedback_icon_black);
			closeApp.setImageResource(R.drawable.sleep_button);
			sleepTextView.setTextColor(daycolor);
			brightnessPercentageText.setTextColor(daycolor);
			ivBrightness.setImageResource(R.drawable.brightness);
			ivVideo.setImageResource(R.drawable.theatre);
			continueVideoLoop.setTextColor(daycolor);
			contactUsTextView.setTextColor(daycolor);
			ivContactUs.setImageResource(R.drawable.contact_us);
			parentView.setBackgroundResource(R.drawable.sky_still);
			backgroundImage.setBackgroundResource(R.drawable.sky_still);
			if(bgSkyVideo.isPlaying()) {
				bgSkyVideo.stopPlayback();
			}
			bgSkyVideo.setVideoURI(Uri.parse("android.resource://"
						+ getPackageName() + "/" + R.raw.sky_loop));
			bgSkyVideo.start();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		isNight = hour < 6 || hour > 18;

        Util.appendLog("td_advert.LauncharScreen onCreate");

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().getDecorView().setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_FULLSCREEN
						| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.launcher_full_screen);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionManager(this));
		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH) + 1; // Note: zero based!
		int date = now.get(Calendar.DAY_OF_MONTH);
		int day = now.get(Calendar.DAY_OF_WEEK);
		switch (month) {
			case 1:monthString = "JAN";
				break;
			case 2:monthString = "FEV";
				break;
			case 3:monthString = "MAR";
				break;
			case 4:monthString = "ABR";
				break;
			case 5:monthString = "MAI";
				break;
			case 6:monthString = "JUN";
				break;
			case 7:monthString = "JUL";
				break;
			case 8:monthString = "AGO";
				break;
			case 9:monthString = "SET";
				break;
			case 10:monthString = "OUT";
				break;
			case 11:monthString = "NOV";
				break;
			case 12:monthString = "DEZ";
				break;
			default: monthString = "Invalid month";
				break;
		}
		switch (day) {
			case 1: weekdayString = "Dom";
				break;
			case 2: weekdayString = "Seg";
				break;
			case 3: weekdayString = "Ter";
				break;
			case 4: weekdayString = "Qua";
				break;
			case 5: weekdayString = "Qui";
				break;
			case 6: weekdayString = "Sext";
				break;
			case 7: weekdayString = "Sá";
				break;
			default: weekdayString = "Invalid week";
				break;
		}
		if(String.valueOf(date).length() == 1) {
			dateString = "0"+date;
		} else {
			dateString = ""+date;
		}
		Log.d("yearmonth", year+"/"+month+"/"+date+"/"+day);
		textClockDay = (TextView)findViewById(R.id.textClockDay);
        welcome_road = (ImageView) findViewById(R.id.welcome_road);
        bkk_bg = (ImageView) findViewById(R.id.bkk_bg);
        taxi = (ImageView) findViewById(R.id.taxi);
        touchToStart = (ImageView) findViewById(R.id.touchStart);
        welcomeText = (ImageView) findViewById(R.id.welcomeOnBoard);
		taxilogos = (ImageView) findViewById(R.id.taxilogos);
		//nestologo = (ImageView) findViewById(R.id.nestologo);
		parentView = (RelativeLayout) findViewById(R.id.launcher_parent);
		//Test
		testQuestionaires = (ImageView)findViewById(R.id.testQuestionaires);
		logo = (ImageView) findViewById(R.id.tadvert_logo);
		logo.setOnClickListener(this);
		wifiIcon = (ImageView)findViewById(R.id.wifi_icon);
		testQuestionaires.setOnClickListener(this);
		///
        welcomeScreenHolder = (RelativeLayout) findViewById(R.id.welcomeScreen);
        welcomeScreenHolder.setOnClickListener(this);
//		parentView.setOnClickListener(this);
		Typeface tf = Typeface.createFromAsset(getAssets(),"helvetica-neue-thin-1361522098.ttf");

		piet_text = (TextView) findViewById(R.id.piet_text);
		piet_text.setTypeface(tf, Typeface.BOLD);
		piet_text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);

		jt_text = (TextView) findViewById(R.id.jt_text);
		jt_text.setTypeface(tf, Typeface.BOLD);
		jt_text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);

		vrielink_text = (TextView) findViewById(R.id.vrielink_text);
		vrielink_text.setTypeface(tf, Typeface.BOLD);
		vrielink_text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);

		sandton_text = (TextView) findViewById(R.id.sandton_text);
		sandton_text.setTypeface(tf, Typeface.BOLD);
		sandton_text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);

		vw_text = (TextView) findViewById(R.id.vw_text);
		vw_text.setTypeface(tf, Typeface.BOLD);
		vw_text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);

		vw_text2 = (TextView) findViewById(R.id.vw2_text);
		vw_text2.setTypeface(tf, Typeface.BOLD);
		vw_text2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);

		vw_text3 = (TextView) findViewById(R.id.vw_text_three);
		vw_text3.setTypeface(tf, Typeface.BOLD);
		vw_text3.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);

		vw_text4 = (TextView) findViewById(R.id.vw4_text);
		vw_text4.setTypeface(tf, Typeface.BOLD);
		vw_text4.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);

		vw_text5 = (TextView) findViewById(R.id.vw5_text);
		vw_text5.setTypeface(tf, Typeface.BOLD);
		vw_text5.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);

		vw_text6 = (TextView) findViewById(R.id.vw6_text);
		vw_text6.setTypeface(tf, Typeface.BOLD);
		vw_text6.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);

		videoButtonText = (TextView) findViewById(R.id.video_button_text);
		videoButtonText.setTypeface(tf, Typeface.BOLD);
		videoButtonText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);

		textClockDay.setText(weekdayString+", "+dateString+" "+monthString+" "+year);
		feedBackTextView = (TextView) findViewById(R.id.launcherfeedback);
		feedBackTextView.setTypeface(tf, Typeface.NORMAL);
		contactUsTextView = (TextView) findViewById(R.id.contactUsText);
		contactUsTextView.setTypeface(tf, Typeface.NORMAL);
		sleepTextView = (TextView) findViewById(R.id.sleeptextview);
        sleepTextView.setTypeface(tf, Typeface.NORMAL);

		continueVideoLoop = (TextView) findViewById(R.id.continueVideoLoopText);
		continueVideoLoop.setTypeface(tf, Typeface.NORMAL);
		closeApp = (ImageView) findViewById(R.id.close_app);
		brightnessButton = (RelativeLayout) findViewById(R.id.brightness_button);
		brightnessPercentageText = (TextView) findViewById(R.id.brightness_percentage);
        brightnessPercentageText.setTypeface(tf, Typeface.NORMAL);
		videoLoopParent = (RelativeLayout) findViewById(R.id.videoLoopParent);
		initVideoLoopControls();
		videoViewContainer.setClickable(false);
		versionNumber = false;
		videoViewContainer.forceLayout();

        LayoutInflater layoutInflater
                = (LayoutInflater)getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.contact_us_screen, null);
        popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

		PREF = getSharedPreferences("VERSION_STATUS", MODE_WORLD_READABLE);

		init();

		jtImage.setOnClickListener(this);
		sandtonImage.setOnClickListener(this);
		vvvImage.setOnClickListener(this);
		vrielinkImage.setOnClickListener(this);
		pietImage.setOnClickListener(this);
        vwimage2.setOnClickListener(this);
        vwimage3.setOnClickListener(this);
        vwimage4.setOnClickListener(this);
        vwimage5.setOnClickListener(this);
        vwimage6.setOnClickListener(this);

		feedBack.setOnClickListener(this);
		contactUs.setOnClickListener(this);

		videoButton.setOnClickListener(this);
		trailerView.setOnClickListener(this);
		videoViewContainer.setOnClickListener(this);
		closeArea.setOnClickListener(this);
		brightnessButton.setOnClickListener(this);
		// magazineButton.setOnClickListener(this);
		closeApp.setOnClickListener(this);

		timeCounter = (TextView) findViewById(R.id.timeCounter);
		VideoLoopTask.getInstance(this).setEnabled(false);

        logSync = new LogSync();

		if (isConnectingToInternet(this)) {
            Util.appendLog("Device is connected to the internet");
            Util.appendLog("Disabled video loop countdown");
			disableIdleCheck = true;

			checkVersion();
			sendEventAnalytics();
			sendFeedbacks();
			sendEmails();
			sendExceptions();
            sendCurrentList();
            sendLogs();

			MessageSync ms = new MessageSync(getApplicationContext());
			ms.syncMessage();

            TaxiDetails sn = new TaxiDetails(getApplicationContext());
            sn.getLatestStationNames();
            sn.syncDetails();

		} else {
            PrelaunchSetup prelaunchSetup = new PrelaunchSetup(getApplicationContext());
            prelaunchSetup.setWifi();

            Util.appendLog("Device is NOT connected to the internet");
            Util.appendLog("Enable video loop countdown");
			disableIdleCheck = false;

            TestAdapter adapter = new TestAdapter(this);
            adapter.createDatabase();
            adapter.open();

            adapter.populateState();
            this.assignCompanyToViews(AppState.getInstance().getCompaniesList());
            startIdleThread();
            isFromCompanyBackScreen = false;
            adapter.close();
		}

        Util.appendLog("CompanyScreen.isOpen = false");
		CompanyScreen.isOpen = false;
		schedulerTask.registerTask(VideoLoopTask.getInstance(this));
		schedulerTask.registerTask(new AbstractTask(LauncharScreen.this, 1) {
			private int closeCount = -1;

			@Override
			protected void execute() {
				if (closeClickTimer > 0) {
					if (closeClickTimer == closeCount) {
						closeClickTimer = 0;
						closeCount = -1;
					} else if (closeCount >= -1) {
						closeCount = closeClickTimer;
					}
				}
			}
		});

	}

	public void startIdleThread() {
		try {
			activityCheckTimerTask = new ActivityCheckTimerTask();
			activityDelayTimer.schedule(activityCheckTimerTask, 10, 1000);
			activityDelayTimer.scheduleAtFixedRate(schedulerTask, 3, 1000);
            Util.appendLog("Starting video timer");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void disableIdleCheck(boolean flag) {
		disableIdleCheck = flag;
	}
	public void disableClick(boolean flag) {
		disableClick = flag;
	}

	private void checkVersion() {
		if (!isCheckingVersion) {
			isCheckingVersion = true;

			String url = null;
			url = TadvertConstants.url + "getVersion" + "&appVersionToken=" + PhoneStatus.fetchStatus(this).getVersion();

            String meid = AppState.getInstance().getMeid();

			url += "&meid=" + meid;
            url += "&buildNo=" + PhoneStatus.fetchStatus(this).getVersion();
            Util.appendLog("Sending checkVersion request meid="+meid+"&buildNo="
                    +PhoneStatus.fetchStatus(this).getVersion());

			WebTask task = new WebTask(url, new DownloadDelegate(this));
			task.execute();

			activityCheckTimerTask.cancel();
			activityDelayTimer.purge();
			companyIconParentList = new ArrayList<View>();
		}
	}

	private void sendEventAnalytics() {
		TestAdapter dbHelper = new TestAdapter(this);
		dbHelper.createDatabase();
		dbHelper.open();
		ArrayList<AnalyticEvent> analyticEvents = dbHelper
				.getAllAnalyticsEvents();

		dbHelper.updateAllEventState("In Progress");

		dbHelper.close();
		if (analyticEvents != null && analyticEvents.size() > 0) {
			Gson gson = new Gson();
			String analyticalList = gson.toJson(analyticEvents);
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("AnalyticsList", analyticalList));

			String url = TadvertConstants.url + "getAnalytics";
			url += "&meid=" + AppState.getInstance().getMeid();

			WebTask task = new WebTask(url, params, new WebTaskDelegate() {

				@Override
				public void synchronousPostExecute(String sr) {
					TestAdapter mDbHelper = new TestAdapter(LauncharScreen.this);
					mDbHelper.createDatabase();
					mDbHelper.open();
					mDbHelper.removeAllEventAnalytics();
					mDbHelper.close();
				}

				@Override
				public void onError(Exception e) {
					super.onError(e);
					TestAdapter mDbHelper = new TestAdapter(LauncharScreen.this);
					mDbHelper.createDatabase();
					mDbHelper.open();
					mDbHelper.updateAllEventState("New");
					mDbHelper.close();
				}

			});
			task.execute();
		}

	}

	private void sendFeedbacks() {
		TestAdapter mDbHelper = new TestAdapter(this);
		mDbHelper.createDatabase();
		mDbHelper.open();

		ArrayList<Feedback> feedbackList = mDbHelper.getFeedbacks();
		mDbHelper.close();

		if (feedbackList != null && feedbackList.size() > 0) {
            Util.appendLog(feedbackList.size() + " Feedbacks found, sending it to the server");

			Gson gson = new Gson();
			String jsonFeedbacks = gson.toJson(feedbackList);

			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("feedbacksList", jsonFeedbacks));

			String url = TadvertConstants.url + "sendFeedback";
			WebTask task = new WebTask(url, params, new WebTaskDelegate() {

				@Override
				public void synchronousPostExecute(String sr) {

                    Util.appendLog("Feedbacks are sent successfully, remove all feedbacks from the local database");

					TestAdapter mDbHelper = new TestAdapter(LauncharScreen.this);
					mDbHelper.createDatabase();
					mDbHelper.open();

					mDbHelper.removeAllFeedbacks();
					mDbHelper.close();
				}

			});
			task.execute();
		}
	}

	private void sendEmails() {
		TestAdapter mDbHelper = new TestAdapter(this);
		mDbHelper.createDatabase();
		mDbHelper.open();

		ArrayList<UserEmail> userEmailList = mDbHelper.getUserEmails();
		mDbHelper.close();

		if (userEmailList != null && userEmailList.size() > 0) {

            Util.appendLog(userEmailList.size() + " Emails found, sending it to the server");

            Gson gson = new Gson();
			String jsonEmails = gson.toJson(userEmailList);
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("userEmailList", jsonEmails));

			String url = TadvertConstants.url + "send_user_email";
			WebTask task = new WebTask(url, params, new WebTaskDelegate() {

				@Override
				public void synchronousPostExecute(String sr) {
                    Util.appendLog("Emails are sent successfully, remove all emails from the local database");

                    TestAdapter mDbHelper = new TestAdapter(LauncharScreen.this);
					mDbHelper.createDatabase();
					mDbHelper.open();

					mDbHelper.removeAllUserEmails();
					mDbHelper.close();
				}

			});
			task.execute();
		}
	}

	private void sendExceptions() {
		TestAdapter mDbHelper = new TestAdapter(this);
		mDbHelper.createDatabase();
		mDbHelper.open();

		ArrayList<String> exceptionLogs = mDbHelper.getExceptionLogs();
		mDbHelper.close();

		if (exceptionLogs != null && exceptionLogs.size() > 0) {
			Gson gson = new Gson();
			String jsonExceptions = gson.toJson(exceptionLogs);
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("exceptionLogs", jsonExceptions));

			String url = TadvertConstants.url + "reportException";
			WebTask task = new WebTask(url, params, new WebTaskDelegate() {

				@Override
				public void synchronousPostExecute(String sr) {
					TestAdapter mDbHelper = new TestAdapter(LauncharScreen.this);
					mDbHelper.createDatabase();
					mDbHelper.open();

					mDbHelper.removeAllExceptionLogs();

					mDbHelper.close();
				}

			});
			task.execute();
		}
	}

    private void sendLogs(){
        logSync.syncLogFile();
    }

    private void sendCurrentList(){

        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("meid", AppState.getInstance().getMeid()));
        params.add(new BasicNameValuePair("currentList", AppState.getInstance().getCompaniesListID()));

        Util.appendLog("Sending device company list to the server, " + AppState.getInstance().getCompaniesListID());

        String url = TadvertConstants.url + "updateCurrentList";
        WebTask task = new WebTask(url, params);
        task.execute();

    }

	void init() {

        Util.appendLog("init() got called, setting up references");
		glare = findViewById(R.id.glare);
		topWhiteBar = (FrameLayout) findViewById(R.id.top_white_bar);
		ivCalendar = (ImageView)findViewById(R.id.ivCalendar);
		dclock = (TextClock)findViewById(R.id.digitalClock1);
		ivFeedBack = (ImageView)findViewById(R.id.ivFeedBack);
		ivBrightness = (ImageView)findViewById(R.id.ivBrightness);
		ivVideo = (ImageView)findViewById(R.id.ivVideo);
		ivContactUs = (ImageView)findViewById(R.id.ivContact);
		backgroundImage = (ImageView) findViewById(R.id.placeholder_bg_sky);

		jtImage = (com.example.td_advert.view.CircularImageView) findViewById(R.id.jt_image);
		sandtonImage = (com.example.td_advert.view.CircularImageView) findViewById(R.id.sandton_image1);
		vvvImage = (com.example.td_advert.view.CircularImageView) findViewById(R.id.vw_image1);
		vrielinkImage = (com.example.td_advert.view.CircularImageView) findViewById(R.id.vrielink_image);
		pietImage = (com.example.td_advert.view.CircularImageView) findViewById(R.id.piet_image);
		vwimage2 = (com.example.td_advert.view.CircularImageView) findViewById(R.id.vw2_image);
		vwimage3 = (com.example.td_advert.view.CircularImageView) findViewById(R.id.vw3_image);
		vwimage4 = (com.example.td_advert.view.CircularImageView) findViewById(R.id.vw4_image);
		vwimage5 = (com.example.td_advert.view.CircularImageView) findViewById(R.id.vw5_image);
		vwimage6 = (com.example.td_advert.view.CircularImageView) findViewById(R.id.vw6_image);
		animationState = AnimationState.BEFORE_ANIMATION_START;
		closeArea = (RelativeLayout) findViewById(R.id.closeArea);

		feedBack = (RelativeLayout) findViewById(R.id.feedbackmain);
		contactUs = (RelativeLayout) findViewById(R.id.contactUs);
		trailerView = (FrameLayout) findViewById(R.id.continueVideoLoop);

		feedback = (FrameLayout) findViewById(R.id.feedback);

		bgSkyVideo = (VideoView) findViewById(R.id.bg_sky_video);

		bgSkyVideo.setOnTouchListener(new View.OnTouchListener()		{

			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				testQuestionaires.setVisibility(View.VISIBLE);
				Log.d("videotouch","videotouch");
				return false;
			}
		});


        CircularImageView[] arr = new CircularImageView[] { jtImage, pietImage,
				sandtonImage, vrielinkImage, vvvImage, vwimage2, vwimage3,
				vwimage4, vwimage5, vwimage6 };

		for (int i = 0; i < arr.length; i++) {
			CircularImageView circularImageView = arr[i];
			iconsList.add(circularImageView);
		}
		initVideoLoopControls();
	}

	private void hideAnimatableScreenElements() {
        try {
            topWhiteBar.setVisibility(View.INVISIBLE);
            logo.setVisibility(View.INVISIBLE);
			glare.setVisibility(View.INVISIBLE);
            findViewById(R.id.feedback_area).setVisibility(View.INVISIBLE);
            findViewById(R.id.contact_us_area).setVisibility(View.INVISIBLE);

            findViewById(R.id.company_image1_container).setVisibility(
                    View.INVISIBLE);
            findViewById(R.id.company_image2_container).setVisibility(
                    View.INVISIBLE);
            findViewById(R.id.company_image3_container).setVisibility(
                    View.INVISIBLE);
            findViewById(R.id.company_image4_container).setVisibility(
                    View.INVISIBLE);
            findViewById(R.id.company_image5_container).setVisibility(
                    View.INVISIBLE);
            findViewById(R.id.company_image6_container).setVisibility(
                    View.INVISIBLE);
            findViewById(R.id.company_image7_container).setVisibility(
                    View.INVISIBLE);
            findViewById(R.id.company_image8_container).setVisibility(
                    View.INVISIBLE);
            findViewById(R.id.company_image9_container).setVisibility(
                    View.INVISIBLE);
            findViewById(R.id.company_image10_container).setVisibility(
                    View.INVISIBLE);

            welcomeScreenHolder.setVisibility(View.INVISIBLE);
            welcomeScreenHolder.setClickable(false);

            Util.appendLog("Hiding all animatable screen elements (hideAnimatableScreenElement())");

        } catch (Exception e){
            Util.appendLog("hideAnimatableScreenElements Failed: " + e.toString());
        }
	}

	private enum AnimationState {
		BEFORE_ANIMATION_START, ANIMATION_STARTED, ANIMATION_ENDED
	};

	private AnimationState animationState;

	private void initAnimations() {
		if (animationState == AnimationState.BEFORE_ANIMATION_START) {
            Util.appendLog("Disabled click on all screen");
            disableClick = true;

            Util.appendLog("Start initAnimation(), animate homescreen animation");

			hideAnimatableScreenElements();
			animationState = AnimationState.ANIMATION_STARTED;
			wifiIcon.setVisibility(View.VISIBLE);
			findViewById(R.id.company_image6_container).setVisibility(
					View.VISIBLE);

			AnimationSequence sequence = new AnimationSequence();
			sequence.addPlayable(new Animation(this, topWhiteBar,
					R.anim.topbar_traslation, sequence));
			sequence.addPlayable(new Animation(this, logo, R.anim.logo_scaling,
					sequence));
			MultiAnimationExecutor animationExecutor = new MultiAnimationExecutor();
			animationExecutor
					.addPlayable(new Animation(this, findViewById(R.id.glare),
							R.anim.glare_traslation, sequence));
			animationExecutor.addPlayable(new Animation(this,
					findViewById(R.id.feedback_area), R.anim.bar_items_scaling,
					animationExecutor));
			animationExecutor.addPlayable(new Animation(this,
					findViewById(R.id.contact_us_area),
					R.anim.bar_items_scaling, animationExecutor));
			sequence.addPlayable(animationExecutor);

			sequence.play();
			initCompanyAnimations();
			animationState = AnimationState.ANIMATION_ENDED;
		}
	}

    private void initWelcomeScreen(){
        Util.appendLog("Playing welcome screen animation");
        hideAnimatableScreenElements();

        welcomeScreenHolder.setVisibility(View.VISIBLE);
        welcomeScreenHolder.setClickable(true);

        AnimationSequence sequence = new AnimationSequence() {
            @Override
            public void end() {
                disableClick = false;
            }
        };
        sequence.addPlayable(new com.example.td_advert.animation.Animation(this, welcome_road, R.anim.bottom_up_translation, sequence));
        sequence.addPlayable(new com.example.td_advert.animation.Animation(this, bkk_bg, R.anim.scale_bottom_up, sequence));
        sequence.addPlayable(new com.example.td_advert.animation.Animation(this, welcomeText, R.anim.slide_in_right, sequence));
		sequence.addPlayable(new com.example.td_advert.animation.Animation(this, taxilogos, R.anim.slide_in_right, sequence));
		//sequence.addPlayable(new com.example.td_advert.animation.Animation(this, nestologo, R.anim.slide_in_right, sequence));
        sequence.play();

        android.view.animation.Animation welcomePopup = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.welcome_touch_popup);
        touchToStart.startAnimation(welcomePopup);

        android.view.animation.Animation taxiMoving = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.taxi_accelerate);
        taxi.startAnimation(taxiMoving);

        handler = new Handler();
        final Context ctx = getApplicationContext();
        /*handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(hideWelcomeScreen)
                    return;

                AnimationSequence sequence = new AnimationSequence();
                sequence.addPlayable(new com.example.td_advert.animation.Animation(ctx, touchToStart, R.anim.screen_fade_out_animation, sequence));
                sequence.addPlayable(new com.example.td_advert.animation.Animation(ctx, welcomeText, R.anim.screen_fade_out_animation, sequence));
				sequence.addPlayable(new com.example.td_advert.animation.Animation(ctx, taxilogos, R.anim.screen_fade_out_animation, sequence));
				sequence.addPlayable(new com.example.td_advert.animation.Animation(ctx, nestologo, R.anim.screen_fade_out_animation, sequence));
                sequence.play();
                Util.appendLog("Hiding touchToStart in Thai");
            }
        }, 9000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(hideWelcomeScreen)
                    return;

                touchToStart.setImageResource(R.drawable.welcome_touchstart_brazil);
                welcomeText.setImageResource(R.drawable.welcome_onboard_brazil);
				taxilogos.setImageResource(R.drawable.taxis_logo);
				nestologo.setImageResource(R.drawable.nesto_white1);

                AnimationSequence sequence = new AnimationSequence();
                sequence.addPlayable(new com.example.td_advert.animation.Animation(ctx, touchToStart, R.anim.welcome_touch_popup, sequence));
                sequence.addPlayable(new com.example.td_advert.animation.Animation(ctx, welcomeText, R.anim.slide_in_right, sequence));
				sequence.addPlayable(new com.example.td_advert.animation.Animation(ctx, taxilogos, R.anim.slide_in_right, sequence));
				sequence.addPlayable(new com.example.td_advert.animation.Animation(ctx, nestologo, R.anim.slide_in_right, sequence));
                sequence.play();
                Util.appendLog("Showing touchToStart in English");
            }
        }, 9500);*/

        handler.postDelayed(new Runnable() {
			@Override
			public void run() {

				Util.appendLog("disableClick = true");
				disableClick(true);
				hideAnimatableScreenElements();
				Util.appendLog("welcome_screen handler call playVideo()");
				playVideo();
			}
		}, 25000);

    }

    private void playVideo(){

        if(isDownloading){
            return;
        }

        if(popupWindow.isShowing())
            popupWindow.dismiss();

        videoLoopThread = null;
        disableClick = true;
        Util.appendLog("Disabled click on all screen");
        disableIdleCheck = true;
        Util.appendLog("Disabled video loop check, disableIdleCheck=true");
        skipVideoLoop = false;
        Util.appendLog("skipVideoLoop = false");

        Util.appendLog("Enabling VideoLoopTask, playing videoloop");
        VideoLoopTask.getInstance(this).setEnabled(true);
    }

	private void initCompanyAnimations() {

		AnimationSequence sequence = new AnimationSequence(){
			@Override
			public void end() {
                disableClick = false;
                Util.appendLog("Enable click for LauncharScreen, disableClick=false");
			}
		};
		ArrayList<View> tempList = (ArrayList<View>) companyIconParentList.clone();
		if (tempList != null && tempList.size() > 0) {

            //Show Company at position 2 first, (NOK AIR)
            try {
                if (tempList.size() >= 2) {
                    View v = tempList.remove(2);
                    sequence.addPlayable(new Animation(this, v, R.anim.company_icon_scaling, sequence));
                }
            } catch (Exception e){

            }

			Random random = new Random();

			while (tempList.size() > 0) {
				int index = Math.abs(random.nextInt()) % tempList.size();
				View v = tempList.remove(index);

				sequence.addPlayable(new Animation(this, v, R.anim.company_icon_scaling, sequence));

			}

		}
        Util.appendLog("Playing company popping up animations sequences");
		sequence.play();
		// }
	}

	public void syncComplete() {
        Util.appendLog("Application sync is finish, starting videoloop timer");
		assignCompanyToViews(AppState.getInstance().getCompaniesList());
		isFromCompanyBackScreen = false;
		startIdleThread();

		/*if (!isCharging()) {
			finish();
		}*/
	}

	public void assignCompanyToViews(ArrayList<Company> companyList) {
		// TextView[] texts = new TextView[] { jt_text, piet_text, sandton_text,
		// vrielink_text, vw_text, vw_text2, vw_text3, vw_text4, vw_text5,
		// vw_text6 };
		//
        Util.appendLog("Generating and randomize companyList");
		if (companyList != null) {

            // Make Nok Air show in the middle
            Company tempComp;
            for (int i = 0; i < companyList.size(); i++) {
                if(companyList.get(i).getCompanyId() == 9) {
                    try {
                        tempComp = companyList.get(2);
                        companyList.set(2, companyList.get(i));
                        companyList.set(i, tempComp);
                    } catch (Exception e) {

                    }
                }
            }

			for (int i = 0; i < companyList.size(); i++) {
				showCompanyIcon(companyList.get(i));
			}

		}

	}

	public void useHandler() {
		if (isDownloading) {
            Util.appendLog("Cannot play video loop, application still syncing");
			displayTimer("downlaoding");
			return;
		}
		// Toast.makeText(this, "Loop Started", Toast.LENGTH_SHORT).show();
		// if (videoLoopThread == null || !videoLoopThread.isRunning()) {
		if (!skipVideoLoop || !disableIdleCheck) {
            Util.appendLog("Handler called playVideo()");
			playVideo();
		}
		// } else {
		// displayTimer("downlaoding");
		// }
	}

	public void stopVideoLoopThread() {
		// if (videoLoopThread != null && videoLoopThread.isRunning()) {
		// videoLoopThread.stop();
		// videoLoopThread = null;
		// }
        Util.appendLog("Enabling VideoLoopTask");
		VideoLoopTask.getInstance(this).setEnabled(false);
	}

	@Override
	public void onLoopStart() {
		// if (videoLoopView != null) {
		// videoLoopView = createVideoView();
		// videoViewContainer.addView(videoLoopView, 0);

        Util.appendLog("Starting video loop, onLoopStart() called");

        if(AppState.getInstance().getCompaniesList().size() == 0){
            finish();
            return;
        }

		hideAnimatableScreenElements();
		bgSkyVideo.setVisibility(View.INVISIBLE);
		vlIterationNo = 0;
		videoLoopParent.setVisibility(View.VISIBLE);

		// }
	}

	private void initVideoLoopControls() {
		videoViewContainer = (RelativeLayout) findViewById(R.id.videoViewParent);
		videoButton = (com.example.td_advert.view.CircularImageView) findViewById(R.id.video_button);
	}

	@Override
	public void onLoopRun(final int iteration) {
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {

				if (videoLoopView != null) {
					videoLoopView.pause();
				}
				bgSkyVideo.stopPlayback();
				Company company = AppState.getInstance().getCompaniesList().get(iteration);

				Log.v("Zenyai", company.getTabs().getVideo());
				if (company.getTabs().getVideo() == null) {
					finish();
					return;
				}

				videoLoopView = createVideoView();
				videoLoopView.setVideoPath(company.getTabs().getVideo());
				videoLoopView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
					public boolean onError(MediaPlayer mp, int what, int extra) {
						videoClick();
						return true;
					}
				});
				videoLoopView.setOnPreparedListener(new OnPreparedListener() {
					@Override
					public void onPrepared(MediaPlayer mp) {
						mp.setVolume(0f, 0f);
					}
				});
				videoLoopView.start();

				videoViewContainer.removeAllViews();
				videoViewContainer.addView(videoLoopView, 0);
				Bitmap bitmap1 = BitmapFactory.decodeFile(company.getLogo());
				videoButton.setCompany(company);
				videoButton.setImageBitmap(bitmap1);
				videoButton.setTextView(videoButtonText);

//				videoButton.getTextView().setText(company.getName());
				videoButton.setVisibility(View.VISIBLE);
				videoButtonText.setVisibility(View.VISIBLE);
				wifiIcon.setVisibility(View.GONE);
				testQuestionaires.setVisibility(View.GONE);


				vlIterationNo++;
				EasyTracker.getInstance(LauncharScreen.this).send(MapBuilder.createEvent("video_loop_event", "video_played", company.getName() + "_video",
						null).build());

				saveAnalytic(company.getName() + "_video_played", 1);

				disableClick = false;
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		//overridePendingTransition(R.anim.screen_fade_in_animation, R.anim.screen_fade_out_animation);
	}

	@Override
	public void onLoopEnd() {
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (!CompanyScreen.isOpen) {
					try {
						hideAnimatableScreenElements();
						startSkyBGAnimation();

						videoLoopParent.setVisibility(View.GONE);
						videoButton.setVisibility(View.GONE);
						videoButtonText.setVisibility(View.GONE);
						wifiIcon.setVisibility(View.VISIBLE);
						videoLoopView.stopPlayback();
						videoLoopView = null;
						videoViewContainer.removeAllViews();

						animationState = AnimationState.BEFORE_ANIMATION_START;
						initAnimations();

					} catch (Exception e) {
						Util.appendLog("Remove all view failed, videoViewContainer.removeAllViews()");
					}
				}
				resetIdleTime();
			}
		});
	}

	private VideoView createVideoView() {

        Util.appendLog("Creating videoloop view");

		VideoView view = new VideoView(this);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.FILL_PARENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		params.addRule(RelativeLayout.ALIGN_PARENT_TOP);

		view.setLayoutParams(params);

		view.setClickable(true);
		view.setFocusableInTouchMode(true);
		view.setZOrderMediaOverlay(true);
		return view;
	}


	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

        checkVersion();
        sendEventAnalytics();
        sendFeedbacks();
        sendEmails();
        sendExceptions();
        sendLogs();

        MessageSync ms = new MessageSync(getApplicationContext());
        ms.syncMessage();

        TaxiDetails sn = new TaxiDetails(getApplicationContext());
        sn.getLatestStationNames();
        sn.syncDetails();

		resetIdleTime();
	}

	@Override
	protected void onDestroy() {

        Util.appendLog("td_advert.LauncharScreen onDestroy");

		activityCheckTimerTask.cancel();
		activityDelayTimer.cancel();
		stopVideoLoopThread();
		tabImagesMap = null;

		myDrawable = null;
		pd = null;
		feedback = null;
		piet_text = null;
		jt_text = null;
		vrielink_text = null;
		sandton_text = null;
		vw_text = null;
		vw_text2 = null;
		vw_text3 = null;
		vw_text4 = null;
		vw_text5 = null;
		vw_text6 = null;
		videoButtonText = null;

        super.onDestroy();

	}

	@Override
	protected void onStop() {
        Util.appendLog("td_advert.LauncharScreen onStop");

        try {
            onLoopEnd();
        } catch (Exception e){
            Util.appendLog("failed onLoopEnd() on onStop(), videoloop is not playing");
        }

		disableIdleCheck = true;
		EasyTracker.getInstance(this).activityStop(this); // Add this method.
		mWakeLock.release();
		videoLoopView = null;
		videoViewContainer.removeAllViews();
		videoLoopParent.setVisibility(View.GONE);
		videoButton.setVisibility(View.GONE);
		videoButtonText.setVisibility(View.GONE);
		videoLoopThread = null;
		bgSkyVideo.stopPlayback();
        super.onStop();
	}

	public void showCompanyIcon(Company company) {
        TextView[] texts = new TextView[] { jt_text, piet_text, sandton_text,
				vrielink_text, vw_text, vw_text2, vw_text3, vw_text4, vw_text5,
				vw_text6 };

		CircularImageView view = null;
		vwimage6.setImageResource(R.drawable.google_map);
		if (iconsList != null) {
			for (int i = 0; i < iconsList.size()-1; i++) {
				view = iconsList.get(i);
				if (view.getCompany() == null || view.getCompany().equals(company)) {
					view.setCompany(company);
					view.setTextView(texts[i]);
					break;
				}
			}

			if (view != null) {
				Bitmap bitmap1 = BitmapFactory.decodeFile(company.getLogo());
				Log.d("imageurl2", company.getLogo());
				view.setImageBitmap(bitmap1);
                if(company.getTabs().getPlaceholder() != 1)
				    view.getTextView().setVisibility(View.VISIBLE);
//				view.getTextView().setText(company.getName()); // TODO: 6/7/2016  
				// view.setImageBitmap(bm)
				View parent = (View) view.getParent();

				if (!companyIconParentList.contains(parent)) {
					companyIconParentList.add((View) view.getParent());
				}
				if (animationState != AnimationState.BEFORE_ANIMATION_START) {
					((View) view.getParent()).setVisibility(View.VISIBLE);
				}
			} else {
				throw new RuntimeException("View NOt Found for company: "
						+ company.getName());
			}
			isCheckingVersion = false;
		}

        Util.appendLog("Showing company icon for " + company.getName() + "(" + company.getCompanyId() + ")");

    }

	public void clearCompanies() {
		TextView[] texts = new TextView[] { jt_text, piet_text, sandton_text,
				vrielink_text, vw_text, vw_text2, vw_text3, vw_text4, vw_text5,
				vw_text6 };

		CircularImageView view = null;
		if (iconsList != null) {
			for (int i = 0; i < iconsList.size(); i++) {
				view = iconsList.get(i);
				view.setCompany(null);

				view.setTextView(texts[i]);
				view.getTextView().setVisibility(View.INVISIBLE);
			}
		}
        Util.appendLog("Clear all the companies icon from LauncharScreen");
	}

    private void hideWelcomeScreen(Boolean anim){
        hideWelcomeScreen = true;

        if(!anim){
            hideAnimatableScreenElements();
        } else {
            AnimationSequence sequence = new AnimationSequence() {
                @Override
                public void end() {
                    Util.appendLog("Hiding welcome screen element completed");
                    hideAnimatableScreenElements();
                    animationState = AnimationState.BEFORE_ANIMATION_START;
                    initAnimations();
                    handler.removeCallbacksAndMessages(null);
                    Util.appendLog("Remove callback and messages");
                }
            };
            taxi.setAlpha(0f);
            //sequence.addPlayable(new com.example.td_advert.animation.Animation(this, taxi, R.anim.screen_fade_out_animation, sequence));
            sequence.addPlayable(new com.example.td_advert.animation.Animation(this, welcomeText, R.anim.screen_fade_out_animation, sequence));
            sequence.addPlayable(new com.example.td_advert.animation.Animation(this, touchToStart, R.anim.screen_fade_out_animation, sequence));
            sequence.addPlayable(new com.example.td_advert.animation.Animation(this, bkk_bg, R.anim.scale_bottom_out, sequence));
            sequence.addPlayable(new com.example.td_advert.animation.Animation(this, welcome_road, R.anim.bottom_down_translation, sequence));
			sequence.addPlayable(new com.example.td_advert.animation.Animation(this, taxilogos, R.anim.screen_fade_out_animation, sequence));
			//sequence.addPlayable(new com.example.td_advert.animation.Animation(this, nestologo, R.anim.screen_fade_out_animation, sequence));
            sequence.play();

            Util.appendLog("Playing hiding welcome screen elements");
        }
    }

	public  void showGooglemap() {
		((RelativeLayout)this.findViewById(R.id.googlemap)).setVisibility(View.VISIBLE);
		wifiIcon.setVisibility(View.GONE);
		testQuestionaires.setVisibility(View.GONE);
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		String bestProvider = locationManager.getBestProvider(criteria, true);
		Location location = locationManager.getLastKnownLocation(bestProvider);
		if (location != null) {
			onLocationChanged(location);
		}
		locationManager.requestLocationUpdates(bestProvider, 1, 0, this);

		mapFragment.getMapAsync(this);
	}

	@Override
	public void onClick(View v) {

		resetIdleTime();
		Analytics.getInstance().screenClicked();

		EasyTracker.getInstance(LauncharScreen.this).send(MapBuilder.createEvent("SCREEN_TOUCH", Analytics.SCREEN_TOUCH_EVENT, Analytics.SCREEN_TOUCH_EVENT, 1l).build());

		this.saveAnalytic(Analytics.SCREEN_TOUCH_EVENT, 1);

        if(v.getId() == 0)
            return;

		switch (v.getId()) {
            case R.id.welcomeScreen:
                if(!disableClick) {
                    Util.appendLog("Click welcome screen");
                    hideWelcomeScreen(true);
                }
                disableClick = true;
                break;

            case R.id.vw_image1:
            case R.id.piet_image:
            case R.id.vrielink_image:
            case R.id.vw5_image:
            case R.id.vw4_image:
            case R.id.vw3_image:
            case R.id.vw2_image:
            case R.id.sandton_image1:
            case R.id.jt_image:
			/*case R.id.launcher_parent:
				Log.d("testQuestions","testQuestions");
				testQuestionaires.setVisibility(View.VISIBLE);
				break;*/
            case R.id.video_button:
                disableClick = true;
                if (!CompanyScreen.isOpen) {
                    Company company = ((CircularImageView) v).getCompany();

                    try {
                        company.getCompanyId();
                        Util.appendLog("User click company: " + company.getName() + " (" + company.getCompanyId() + ")");
                    } catch (Exception e) {
                        return;
                    }
                    
                    if (company != null && company.getCompanyId() != 0) {
                        CompanyScreen.isOpen = true;
                        startActivity(new Intent(LauncharScreen.this,
                                CompanyScreen.class).putExtra("id",
                                company.getCompanyId()));

                        stopVideoLoopThread();
                        disableIdleCheck = false;
                        LauncharScreen.this.finish();
                    }
                }

                break;

            case R.id.continueVideoLoop:
                if(!disableClick) {
                    Util.appendLog("User click to play videoloop");
                    playVideo();
                }
                break;

            case R.id.contactUs:
                if(!disableClick){
                    disableClick = true;
                    Util.appendLog("User click Contact us Dialog");
                    popupWindow.showAsDropDown(contactUs, 120, 0);
                } else {
                    popupWindow.dismiss();
                    disableClick = false;
                }
                break;
            case R.id.feedbackmain:
                if(!disableClick){
                    Util.appendLog("User click to feedback dialog on LauncharScreen");
                    disableClick = true;
                    TAdvertBaseDialog feedbackDialog = new FeedbackDialog(this,(ViewGroup) findViewById(R.layout.launcher));
                    feedbackDialog.show();
                }
                break;
			case R.id.vw6_image:
				Log.d("googlemapview", "googlemapview");
				showGooglemap();
				break;
			case R.id.tadvert_logo:
				if(((RelativeLayout)this.findViewById(R.id.googlemap)).getVisibility() == View.VISIBLE)
					((RelativeLayout)this.findViewById(R.id.googlemap)).setVisibility(View.GONE);
				break;
            case R.id.close_app:
                Util.appendLog("User request to close the application");
                LauncharScreen.this.finish();
                break;
            case R.id.videoViewParent:
                if(!disableClick) {
                    Util.appendLog("User press to go out of video loop");
                    videoClick();
                }
                break;
            case R.id.brightness_button:
                if(disableClick)
                    return;

                Util.appendLog("User press brightness button");
                BrightnessManager.getInstance().brightnessStepUp(getContentResolver());
                brightnessPercentageText.setText(BrightnessManager.getInstance().getBrightnessPercentage() + "%");

                final Handler handler = new Handler();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int timeToBlink = 500;    //in ms
                        try{
                            Thread.sleep(timeToBlink);
                        } catch (Exception e) {

                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                brightnessPercentageText.setText("Brightness");
                            }
                        });
                    }}).start();

                break;
            case R.id.closeArea:
                if (!VideoLoopTask.getInstance(this).enabled()) {
                    closeClickTimer++;
                    if (closeClickTimer >= 10) {
                        closeClickTimer = 0;

                        ConfigurationDialog configurationDialog = new ConfigurationDialog(
                                this, null);
                        configurationDialog.show();
                    }
                } else {
                    videoClick();
                }
                break;
			case R.id.testQuestionaires://Test
				start_query();
				break;

            default:
                break;
		}
		
	}

	private void start_query() {

		this.startActivity(new Intent(this, StartPopup.class));
	}

	private void videoClick() {
		synchronized (this) {

            Util.appendLog("videoClick() got called");
			resetIdleTime();
			videoLoopParent.setVisibility(View.GONE);
			videoButton.setVisibility(View.GONE);

			stopVideoLoopThread();
			initVideoLoopControls();
			disableIdleCheck = false;
		}
	}

	public Context getDialogContext() {
		Context context;
		if (getParent() != null)
			context = getParent();
		else
			context = this;
		return context;
	}

	@Override
	public void onStart() {
		super.onStart();

        Util.appendLog("onStart() get called");
        resetIdleTime();

        startSkyBGAnimation();
        disableClick = false;
        CompanyScreen.isOpen = false;
        EasyTracker.getInstance(this).activityStart(this);
        disableIdleCheck = VideoLoopTask.getInstance(this).enabled();//

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "My Tag");
        mWakeLock.acquire();

        welcomeScreenHolder.setVisibility(View.INVISIBLE);
        welcomeScreenHolder.setClickable(false);

        if (getIntent().getBooleanExtra("welcome_screen", false)) {
            Util.appendLog("skipVideoLoop = true");
            skipVideoLoop = true;
            initWelcomeScreen();
        } else {
			wifiIcon.setVisibility(View.VISIBLE);
            if(topWhiteBar.getVisibility() == View.INVISIBLE){
                animationState = AnimationState.BEFORE_ANIMATION_START;
                initAnimations();
            }
        }
	}


	private void startSkyBGAnimation() {
        backgroundImage.setAlpha(1f);
        bgSkyVideo.setVisibility(View.VISIBLE);
		bgSkyVideo.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				mp.setLooping(true);
			}
		});
        bgSkyVideo.start();

        AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);
        anim.setDuration(2500);
        anim.setFillAfter(true);
        backgroundImage.startAnimation(anim);
        Util.appendLog("Starting sky background animation");
	}

	public static boolean isConnectingToInternet(Context ctx) {
		ConnectivityManager connectivity = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		boolean result = false;
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						result = true;
						break;
					}

		}
//
//		if (result) {
//			WifiManager wifiMgr = (WifiManager) ctx
//					.getSystemService(Context.WIFI_SERVICE);
//			WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
//			String name = wifiInfo.getSSID();
//
//			File root = android.os.Environment.getExternalStorageDirectory();
//			final String rootDirPath = root.getAbsolutePath() + "/Tadvert/";
//			File dir = new File(rootDirPath);
//			if (!dir.exists()) {
//				dir.mkdirs();
//			}
//
//			String path = rootDirPath + "wifi_config.txt";
//
//			try {
//				String wifiName = FileUtil.readFromFile(path);
//				if (wifiName == null || wifiName.equals("")) {
//					FileUtil.writeToFile(path, name);
//					result = true;
//				} else if (wifiName.equals(name)) {
//					result = true;
//				} else {
//					result = false;
//				}
//			} catch (Exception e) {
//
//				Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_LONG).show();
//			}
//
//		}

		return result;
	}

	public static void deleteFileByPath(String path) {
		File fileToBeWritten = new File(path);
		if (fileToBeWritten.exists()) {
			fileToBeWritten.delete();
		}

	}

	// @Override
	public boolean onTouch(View v, MotionEvent event) {
		if (disableClick)
            return false;

		if (VideoLoopTask.getInstance(this).enabled()) {
            Util.appendLog("User click to disable video loop");
			videoClick();
		}
		return true;
	}

	public boolean isCharging() {
		IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		Intent batteryStatus = registerReceiver(null, ifilter);

		int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
		boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING
				|| status == BatteryManager.BATTERY_STATUS_FULL;

        TestAdapter adapter = new TestAdapter(this);
        adapter.createDatabase();
        adapter.open();
        if(adapter.getTestDeviceStatus() == 1){
            isCharging = true;
        }
        adapter.close();


        Util.appendLog("Check charging, result = " + isCharging);


        return isCharging;
	}

	public void onChangeTheme(View view) {
		changeTheme(false, !isNight);
	}

}
