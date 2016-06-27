package com.example.td_advert.constant;

public interface TadvertConstants {
//	String baseurl = "http://www.t-advert.com/control-panel/index.php/request/";
	String baseurl = "http://gyde.asia/cms-brazil/index.php/request/";
//String baseurl = "http://gyde.asia/cms-bkk/index.php/request/";
//	String baseurl = "http://192.168.1.36:6388/tadvert/index.php/request/";
//    String baseurl = "http://172.16.1.191:6388/tadvert/index.php/request/";
//	String url = baseurl+"?testV=true&func=";
	String url = baseurl+"?func=";
	
//	String url = "http://t-advert.com/TAdvert/v1/?func=";
//	String url = "http://192.168.1.52:8585/TAdvert/?func=";

    // WIFI SETUP
    static final String wifi_ssid = "TAdvert Taxi";
    static final String wifi_password = "updatetaxi";

    /*static final String wifi_ssid = "Zenyai";
    static final String wifi_password = "sarnsuwan";*/
	
	
	String CATAGORIES_LIST = "response";
	String prefFileName = "TAdvert_Pref";
	
	int idleTimeBeforeLoopStartLauncherScreen = 60;// secs
	int idleTimeBeforeLoopStartCompanyScreen = 118;// secs
	int timePerVideo = 15;// secs
	int tutorialVideoDuration = 19;// secs
	
	public static final String CLOSE_INTENT = "com.tadvert.close";// Intent to close the app
	public static final String BRIGHTNESS_MORNING_TIME_INTENT = "com.tadvert.morning_time";// Intent to close the app
	public static final String BRIGHTNESS_EVENING_TIME_INTENT = "com.tadvert.evening_time";// Intent to close the app
	
	public static final String CONFIG_TAXI_NO = "Taxi No";

}
