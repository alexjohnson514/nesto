package com.example.td_advert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DigitalClock;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RatingBar;
import android.widget.VideoView;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.td_advert.arraylist.GlobalArrayList;
import com.example.td_advert.database.TableColumns;
import com.example.td_advert.database.TestAdapter;
import com.example.td_advert.database.Utility;
//import com.google.analytics.tracking.android.EasyTracker;
//import com.google.analytics.tracking.android.GoogleAnalytics;
//import com.google.analytics.tracking.android.Tracker;

public class MainScreen extends Activity implements OnClickListener {

	private static String ImagePath = null;
	TextView back_button, welcome, nu_in_de_bioscope, evenmentan,
			schrijf_je_in, reservern, nuindebioscope, painandgain, win,
			feedback_main, newLayoutText, newLayoutText1, newLayoutText2,
			heading1, heading2, heading3;

	LinearLayout newLayout, newLayout1, newLayout2;

	ProgressDialog pd;
	VideoView playvideo;

	ViewFlipper viewFlipper;

	// VideoView playvideo;

	ImageView image1, image2, image3;
	ImageView layout1Image, layout2_Image1, layout2_Image2, layout3_Image1,
			layout3_Image2, layout3_Image3, layout3_Image4, layout4_Image1,
			thumbnail_image;

	TextView layoutTwoText, layout3Head1, layout3Head2, layout3Text1,
			layout3Text2, layout5Text1;

	EditText getemailaddress;
	Button sendemail;
	String valid_email = null;

	RelativeLayout mainbackground;
	LinearLayout welocomeScreen;
	// private GoogleAnalytics mGaInstance;
	// private Tracker mGaTracker;
	Drawable myDrawable;

	static float ratingValue;
	AlertDialog categoryDialog;
	int Id;

	TableColumns tablecol = new TableColumns();

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

		// mGaInstance = GoogleAnalytics.getInstance(this);
		// mGaTracker = mGaInstance.getTracker("UA-46714754-1");

		pd = new ProgressDialog(MainScreen.this);
		pd.setMessage("Please wait...");

		Intent intent = getIntent();
		Id = intent.getIntExtra("Id", 0);

		System.out.println("Id>>>>>>>>>>>>>>>>>" + Id);

		init();

		// MediaController mediaController = new MediaController(this);
		// mediaController.setAnchorView(playvideo);
		// playvideo.setMediaController(new MediaController(this));

		pd.show();
		getValuefromDataBase();

		setimagetoback();

		individualtabImages(Id + 1, tablecol.getWelcome(), image1, image2,
				image3, null);
		pd.dismiss();

		welcome.setCompoundDrawablesWithIntrinsicBounds(R.drawable.backicon, 0,
				0, 0);

	}

	void init() {

		viewFlipper = (ViewFlipper) findViewById(R.id.viewfliper);
		Typeface tf = Typeface.createFromAsset(getAssets(),
				"SourceSansPro-BoldItalic.ttf");
		back_button = (TextView) findViewById(R.id.backbutton);
		back_button.setTypeface(tf, Typeface.BOLD);
		feedback_main = (TextView) findViewById(R.id.feedbackmain);
		feedback_main.setTypeface(tf, Typeface.BOLD);
		welcome = (TextView) findViewById(R.id.welocomeButton);
		welcome.setTypeface(tf, Typeface.BOLD);
		nu_in_de_bioscope = (TextView) findViewById(R.id.nu_in_de_bioascope);
		nu_in_de_bioscope.setTypeface(tf, Typeface.BOLD);
		evenmentan = (TextView) findViewById(R.id.evenementen);
		evenmentan.setTypeface(tf, Typeface.BOLD);
		schrijf_je_in = (TextView) findViewById(R.id.schrjit_win);
		schrijf_je_in.setTypeface(tf, Typeface.BOLD);
		reservern = (TextView) findViewById(R.id.reserveen);
		reservern.setTypeface(tf, Typeface.BOLD);
		mainbackground = (RelativeLayout) findViewById(R.id.MainBackground);
		welocomeScreen = (LinearLayout) findViewById(R.id.linearLayout2);
		DigitalClock dc = (DigitalClock) findViewById(R.id.digitalClock1);
		dc.setTypeface(tf, Typeface.BOLD);
		newLayoutText = (TextView) findViewById(R.id.newLayoutText);
		newLayoutText.setTypeface(tf, Typeface.BOLD);
		newLayoutText1 = (TextView) findViewById(R.id.newLayoutText1);
		newLayoutText1.setTypeface(tf, Typeface.BOLD);
		newLayoutText2 = (TextView) findViewById(R.id.newLayoutText2);
		newLayoutText2.setTypeface(tf, Typeface.BOLD);

		newLayout = (LinearLayout) findViewById(R.id.newLayout);
		newLayout1 = (LinearLayout) findViewById(R.id.newLayout1);
		newLayout2 = (LinearLayout) findViewById(R.id.newLayout2);

		
		
		thumbnail_image = (ImageView) findViewById(R.id.videoImageClick);

		heading1 = (TextView) findViewById(R.id.Heading1);
		heading1.setTypeface(tf, Typeface.BOLD);
		heading2 = (TextView) findViewById(R.id.Heading2);
		heading2.setTypeface(tf, Typeface.BOLD);
		heading3 = (TextView) findViewById(R.id.Heading3);
		heading3.setTypeface(tf, Typeface.BOLD);

		layoutTwoText = (TextView) findViewById(R.id.layouttwotext);
		layoutTwoText.setTypeface(tf, Typeface.BOLD);

		layout3Head1 = (TextView) findViewById(R.id.layout3head1);
		layout3Head1.setTypeface(tf, Typeface.BOLD);
		layout3Head2 = (TextView) findViewById(R.id.layout3head2);
		layout3Head2.setTypeface(tf, Typeface.BOLD);

		layout3Text1 = (TextView) findViewById(R.id.layout3text1);
		layout3Text1.setTypeface(tf, Typeface.BOLD);
		layout3Text2 = (TextView) findViewById(R.id.layout3text2);
		layout3Text2.setTypeface(tf, Typeface.BOLD);

		layout5Text1 = (TextView) findViewById(R.id.layout5text1);
		layout5Text1.setTypeface(tf, Typeface.BOLD);

		image1 = (ImageView) findViewById(R.id.image1);
		image2 = (ImageView) findViewById(R.id.image2);
		image3 = (ImageView) findViewById(R.id.image3);

		layout1Image = (ImageView) findViewById(R.id.layout1image);
		layout2_Image1 = (ImageView) findViewById(R.id.layout2_image1);
		layout2_Image2 = (ImageView) findViewById(R.id.layout2_image2);
		layout3_Image1 = (ImageView) findViewById(R.id.layout3_image1);
		layout3_Image2 = (ImageView) findViewById(R.id.layout3_image2);
		layout3_Image3 = (ImageView) findViewById(R.id.layout3_image3);
		layout3_Image4 = (ImageView) findViewById(R.id.layout3_image4);

		getemailaddress = (EditText) findViewById(R.id.emailaddress);
		getemailaddress.setTypeface(tf, Typeface.BOLD);
		sendemail = (Button) findViewById(R.id.sendemailtoaddress);

		back_button.setOnClickListener(this);
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

		playvideo = (VideoView) findViewById(R.id.playvideo);

		MediaController mediaController = new MediaController(this);
		mediaController.setAnchorView(playvideo);
		playvideo.setMediaController(new MediaController(this));
	}
	

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.backbutton:
			finish();
			break;
		case R.id.welocomeButton:
			try {
				welcome.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.backicon, 0, 0, 0);
				nu_in_de_bioscope.setCompoundDrawablesWithIntrinsicBounds(0, 0,
						0, 0);
				evenmentan.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				schrijf_je_in.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0,
						0);
				reservern.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				viewFlipper.setDisplayedChild(0);
				pd.show();
				individualtabImages(Id + 1, tablecol.getWelcome(), image1,
						image2, image3, null);
				System.out.println("id>>>>>>>>>>>>>>>" + tablecol.getWelcome());
			} catch (Exception e) {
				e.printStackTrace();
			}

			break;
		case R.id.nu_in_de_bioascope:
			try {
				welcome.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				nu_in_de_bioscope.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.backicon, 0, 0, 0);
				evenmentan.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				schrijf_je_in.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0,
						0);
				reservern.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				viewFlipper.setDisplayedChild(1);
				pd.show();
				individualtabImages(Id + 1, tablecol.getLayout1(),
						layout1Image, null, null, null);
				System.out.println("id>>>>>>>>>>>>>>>" + tablecol.getLayout1());
			} catch (Exception e) {
				e.printStackTrace();
			}

			break;
		case R.id.evenementen:
			try {
				viewFlipper.setDisplayedChild(2);
				welcome.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				nu_in_de_bioscope.setCompoundDrawablesWithIntrinsicBounds(0, 0,
						0, 0);
				evenmentan.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.backicon, 0, 0, 0);
				schrijf_je_in.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0,
						0);
				reservern.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

				individualtabImages(Id + 1, tablecol.getLayout2(),
						layout2_Image1, layout2_Image2, null, null);
				System.out.println("id>>>>>>>>>>>>>>>" + tablecol.getLayout2());
			} catch (Exception e) {
				e.printStackTrace();
			}

			break;
		case R.id.schrjit_win:
			try {
				viewFlipper.setDisplayedChild(3);
				welcome.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				nu_in_de_bioscope.setCompoundDrawablesWithIntrinsicBounds(0, 0,
						0, 0);
				evenmentan.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				schrijf_je_in.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.backicon, 0, 0, 0);
				reservern.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				individualtabImages(Id + 1, tablecol.getLayout3(),
						layout3_Image1, layout3_Image2, layout3_Image3,
						layout3_Image4);
				System.out.println("id>>>>>>>>>>>>>>>" + tablecol.getLayout3());
			} catch (Exception e) {
				e.printStackTrace();
			}

			break;
		case R.id.reserveen:
			try {
				viewFlipper.setDisplayedChild(4);
				welcome.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				nu_in_de_bioscope.setCompoundDrawablesWithIntrinsicBounds(0, 0,
						0, 0);
				evenmentan.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				schrijf_je_in.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0,
						0);
				reservern.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.backicon, 0, 0, 0);
				individualtabImages(Id + 1, tablecol.getLayout4(),
						layout4_Image1, null, null, null);

				System.out.println("id>>>>>>>>>>>>>>>" + tablecol.getLayout4());
			} catch (Exception e) {
				e.printStackTrace();
			}
			// getVideos(Id + 1);

			break;

		case R.id.feedbackmain:
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			final View layout = inflater.inflate(R.layout.feedbackrating,
					(ViewGroup) findViewById(R.id.root));
			AlertDialog.Builder builder = new AlertDialog.Builder(
					getDialogContext());
			final Button submit = (Button) layout.findViewById(R.id.submit);
			final Button cancel = (Button) layout.findViewById(R.id.cancel);
			layout.setBackgroundColor(Color.TRANSPARENT);

			builder.setCancelable(false);
			builder.setView(layout);

			submit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (ratingValue <= 0.0) {
						Toast.makeText(getDialogContext(),
								"Please submit some rating.",
								Toast.LENGTH_SHORT).show();
					} else {
						Intent intent = new Intent(Intent.ACTION_SEND);
						intent.setType("text/html");
						intent.putExtra(Intent.EXTRA_EMAIL,
								new String[] { "g.benjamins@t-advert.nl" });
						intent.putExtra(Intent.EXTRA_SUBJECT, "T-Advert Rating");
						intent.putExtra(Intent.EXTRA_TEXT,
								"Rating Given By User: " + ratingValue);
						startActivity(Intent
								.createChooser(intent, "Send Email"));
						categoryDialog.dismiss();
					}
				}
			});

			cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					categoryDialog.dismiss();
				}
			});

			RatingBar rating = (RatingBar) layout.findViewById(R.id.ratingBar1);
			rating.setNumStars(5);
			rating.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

				@Override
				public void onRatingChanged(RatingBar ratingBar, float rating,
						boolean fromUser) {
					// TODO Auto-generated method stub
					ratingValue = rating;
				}
			});

			categoryDialog = builder.create();
			categoryDialog.show();
			break;

		case R.id.sendemailtoaddress:
			if (!isEmailValid(getemailaddress.getText().toString())) {
			} else {
				String to = getemailaddress.getText().toString();

				Intent email = new Intent(Intent.ACTION_SEND);
				email.putExtra(Intent.EXTRA_EMAIL,
						new String[] { "g.benjamins@t-advert.nl" });
				email.putExtra(Intent.EXTRA_CC, new String[] { to });
				email.putExtra(Intent.EXTRA_BCC, new String[] { to });
				email.putExtra(Intent.EXTRA_SUBJECT, "offical email");
				email.putExtra(Intent.EXTRA_TEXT, to);
				// need this to prompts email client only
				email.setType("message/rfc822");

				startActivity(Intent.createChooser(email,
						"Choose an Email client :"));

			}
			break;

		case R.id.videoImageClick:
			viewFlipper.setDisplayedChild(5);

			getVideos(Id + 1);
			// startActivity(new Intent(MainScreen.this, LargeVideoScreen.class)
			// .putExtra("CID", Id + 1));
			break;

		// ------------------------------------------------------
		case R.id.newLayout:
			try {
				welcome.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				nu_in_de_bioscope.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.backicon, 0, 0, 0);
				evenmentan.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				schrijf_je_in.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0,
						0);
				reservern.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				viewFlipper.setDisplayedChild(1);
				pd.show();
				individualtabImages(Id + 1, tablecol.getLayout1(),
						layout1Image, null, null, null);
				System.out.println("id>>>>>>>>>>>>>>>" + tablecol.getLayout1());
			} catch (Exception e) {
				e.printStackTrace();
			}

			break;

		case R.id.newLayout1:
			try {
				viewFlipper.setDisplayedChild(2);
				welcome.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				nu_in_de_bioscope.setCompoundDrawablesWithIntrinsicBounds(0, 0,
						0, 0);
				evenmentan.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.backicon, 0, 0, 0);
				schrijf_je_in.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0,
						0);
				reservern.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

				individualtabImages(Id + 1, tablecol.getLayout2(),
						layout2_Image1, layout2_Image2, null, null);
				System.out.println("id>>>>>>>>>>>>>>>" + tablecol.getLayout2());
			} catch (Exception e) {
				e.printStackTrace();
			}

			break;

		case R.id.newLayout2:
			try {
				viewFlipper.setDisplayedChild(3);
				welcome.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				nu_in_de_bioscope.setCompoundDrawablesWithIntrinsicBounds(0, 0,
						0, 0);
				evenmentan.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				schrijf_je_in.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.backicon, 0, 0, 0);
				reservern.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				individualtabImages(Id + 1, tablecol.getLayout3(),
						layout3_Image1, layout3_Image2, layout3_Image3,
						layout3_Image4);
				System.out.println("id>>>>>>>>>>>>>>>" + tablecol.getLayout3());
			} catch (Exception e) {
				e.printStackTrace();
			}

			break;

		}

	}

	@Override
	public void onStart() {
		super.onStart();
		// The rest of your onStart() code.
		// EasyTracker.getInstance().activityStart(this); // Add this method.
		// mGaTracker.sendView("/MainScreen");
	}

	@Override
	protected void onStop() {
		// EasyTracker.getInstance().activityStop(this);
		Log.d(getClass().getName(), "onStop");
		super.onStop();
	}

	public Context getDialogContext() {
		Context context;
		if (getParent() != null)
			context = getParent();
		else
			context = this;
		return context;
	}

	public void setimagetoback() {
		try {
			Bitmap bitmap1 = BitmapFactory.decodeFile(GlobalArrayList.imagePath
					.get(Id));
			System.out.println("bitmap>>>>>>>>" + bitmap1);
			myDrawable = new BitmapDrawable(getResources(), bitmap1);
			mainbackground.setBackgroundDrawable(myDrawable);
			System.out.println("myDrawable>>>>>>>>" + myDrawable);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void getValuefromDataBase() {

		TestAdapter mDbHelper = new TestAdapter(MainScreen.this);
		mDbHelper.createDatabase();

		mDbHelper.open();

		Cursor testTabsdata = mDbHelper.getTabsDetail(Id + 1);
		System.out.println("Count>>>>>>>>>>>" + testTabsdata.getCount());
		if (testTabsdata.getCount() > 0) {
			if (testTabsdata.moveToFirst()) {
				do {
					String videoName = Utility.GetColumnValue(testTabsdata,
							"Video");
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
					String primaryId = Utility.GetColumnValue(testTabsdata,
							"Id");
					String _heading1 = Utility.GetColumnValue(testTabsdata,
							"Heading1");
					String _heading2 = Utility.GetColumnValue(testTabsdata,
							"Heading2");
					String _heading3 = Utility.GetColumnValue(testTabsdata,
							"Heading3");
					System.out
							.println(">>>>>>>GetDataFromDataBase>>>>>>>>>>"
									+ videoName + "  " + companyType + "  "
									+ companyTitle + "  " + companyImage1
									+ "  " + companyText1 + "  "
									+ companyImage2 + "  " + companyText2
									+ "  " + companyImage3 + "  "
									+ companyText3 + "   " + primaryId + "   "
									+ _heading1 + "   " + _heading2 + "   "
									+ _heading3);
					if (companyType.equalsIgnoreCase("homepage")) {
						welcome.setText(" " + companyTitle);
						heading1.setText(_heading1);
						heading2.setText(_heading2);
						heading3.setText(_heading3);
						newLayoutText.setText(companyText1);
						newLayoutText1.setText(companyText2);
						newLayoutText2.setText(companyText3);
						tablecol.setWelcome(Integer.parseInt(primaryId));
					} else if (companyType.equalsIgnoreCase("layout1")) {
						nu_in_de_bioscope.setText(" " + companyTitle);
						layoutTwoText.setText(companyText1);
						tablecol.setLayout1(Integer.parseInt(primaryId));
					} else if (companyType.equalsIgnoreCase("layout2")) {
						evenmentan.setText(" " + companyTitle);
						layout3Head1.setText(_heading1);
						layout3Head2.setText(_heading2);
						layout3Text1.setText(companyText1);
						layout3Text2.setText(companyText2);
						tablecol.setLayout2(Integer.parseInt(primaryId));
					} else if (companyType.equalsIgnoreCase("layout3")) {
						schrijf_je_in.setText(" " + companyTitle);
						tablecol.setLayout3(Integer.parseInt(primaryId));
					} else if (companyType.equalsIgnoreCase("layout4")) {
						reservern.setText(" " + companyTitle);
						layout5Text1.setText(companyText1);
						tablecol.setLayout4(Integer.parseInt(primaryId));
					}
				} while (testTabsdata.moveToNext());
				testTabsdata.close();
			}
		}
		mDbHelper.close();
	}

	void opendataBaseAndGetImages(int CId, int Id) {

		TestAdapter mDbHelper = new TestAdapter(MainScreen.this);
		mDbHelper.createDatabase();
		mDbHelper.open();

		Cursor testBackgrounddata = mDbHelper.getUpdatedImages(CId, Id);
		System.out.println("******count****" + testBackgrounddata.getCount());
		String ImagePath1, ImagePath2, ImagePath3, ImagePath4;

		if (testBackgrounddata.getCount() > 0) {
			if (testBackgrounddata.moveToFirst()) {
				do {
					ImagePath1 = Utility.GetColumnValue(testBackgrounddata,
							"Image1");
					try {
						Bitmap bitmap1 = BitmapFactory.decodeFile(ImagePath1);
						image1.setImageBitmap(bitmap1);
					} catch (Exception e) {
						e.printStackTrace();
					}
					ImagePath2 = Utility.GetColumnValue(testBackgrounddata,
							"Image2");
					try {
						Bitmap bitmap2 = BitmapFactory.decodeFile(ImagePath2);
						image2.setImageBitmap(bitmap2);
					} catch (Exception e) {
						e.printStackTrace();
					}
					ImagePath3 = Utility.GetColumnValue(testBackgrounddata,
							"Image3");
					try {
						Bitmap bitmap3 = BitmapFactory.decodeFile(ImagePath3);
						image3.setImageBitmap(bitmap3);
					} catch (Exception e) {
						e.printStackTrace();
					}
					ImagePath4 = Utility.GetColumnValue(testBackgrounddata,
							"Image4");
					System.out.println(">>>>>>>GetImages>>>>>>>>>>" + ImagePath
							+ "  " + ImagePath2 + "  " + ImagePath3 + "  "
							+ ImagePath4);
				} while (testBackgrounddata.moveToNext());
				testBackgrounddata.close();

			}
		}
	}

	void individualtabImages(int CId, int Id, ImageView image1,
			ImageView image2, ImageView image3, ImageView image4) {

		TestAdapter mDbHelper = new TestAdapter(MainScreen.this);
		mDbHelper.createDatabase();
		mDbHelper.open();

		Cursor testBackgrounddata = mDbHelper.getUpdatedImages(CId, Id);
		System.out.println("******count****" + testBackgrounddata.getCount());
		String ImagePath1, ImagePath2, ImagePath3, ImagePath4;
		if (testBackgrounddata.getCount() > 0) {
			if (testBackgrounddata.moveToFirst()) {
				do {
					ImagePath1 = Utility.GetColumnValue(testBackgrounddata,
							"Image1");
					try {
						Bitmap bitmap1 = BitmapFactory.decodeFile(ImagePath1);
						image1.setImageBitmap(bitmap1);
					} catch (Exception e) {
						e.printStackTrace();
					}
					ImagePath2 = Utility.GetColumnValue(testBackgrounddata,
							"Image2");
					try {
						Bitmap bitmap2 = BitmapFactory.decodeFile(ImagePath2);
						image2.setImageBitmap(bitmap2);
					} catch (Exception e) {
						e.printStackTrace();
					}
					ImagePath3 = Utility.GetColumnValue(testBackgrounddata,
							"Image3");
					try {
						Bitmap bitmap3 = BitmapFactory.decodeFile(ImagePath3);
						image3.setImageBitmap(bitmap3);
					} catch (Exception e) {
						e.printStackTrace();
					}
					ImagePath4 = Utility.GetColumnValue(testBackgrounddata,
							"Image4");
					try {
						Bitmap bitmap4 = BitmapFactory.decodeFile(ImagePath4);
						image4.setImageBitmap(bitmap4);
					} catch (Exception e) {
						e.printStackTrace();
					}
					System.out.println(">>>>>>>GetImages>>>>>>>>>> 1 "
							+ ImagePath1 + " 2 " + ImagePath2 + " 3 "
							+ ImagePath3 + " 4 " + ImagePath4);
				} while (testBackgrounddata.moveToNext());

			}
			pd.dismiss();
			testBackgrounddata.close();
		} else {
			Toast.makeText(MainScreen.this, "No Data Available",
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

	void getVideos(int CId) {
		TestAdapter mDbHelper = new TestAdapter(MainScreen.this);
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
			Toast.makeText(MainScreen.this, "No Video Available",
					Toast.LENGTH_SHORT).show();
			finish();
		}

		playvideo
				.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

					@Override
					public void onCompletion(MediaPlayer mp) {
						// TODO Auto-generated method stub
						viewFlipper.setDisplayedChild(4);
						playvideo.invalidate();
					}
				});
	}
}
