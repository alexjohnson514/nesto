package com.example.td_advert.constant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.widget.Toast;

public class Webservices {

	static SharedPreferences TOKEN_PREF;

	static Toast msg;
	static String result;
	
	public static String resultString, Exception;


	public static String  ApiCall(String url, JSONObject json,Context ct) throws IOException
	{
		
		
				
			String json2=json.toString();
			HttpParams httpParameters = new BasicHttpParams();
//			int timeoutConnection = 3000;
			int timeoutConnection = 29000;
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
			int timeoutSocket = 30000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
			HttpPost httpPostRequest = new HttpPost(url);
			
			StringEntity se;
			se = new StringEntity(json.toString());
			httpPostRequest.setEntity(se);
					httpPostRequest.setHeader("Accept", "application/json");
					httpPostRequest.setHeader("Content-type", "application/json");
					
			HttpResponse response = (HttpResponse) httpclient.execute(httpPostRequest);
			HttpEntity entity = response.getEntity();

			if (entity != null) 
			{
				// Read the content stream
				InputStream instream = entity.getContent();
				// convert content stream to a String
				result = convertStreamToString(instream);
				instream.close();
			}
		
		return result;
	}
	
	
	public static String  ApiCallGet(String url)
	{                
		try                     
		{

			Exception="false";	
			String urlnew=url;
			HttpParams httpParameters = new BasicHttpParams();
			int timeoutConnection = 3000;
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
			int timeoutSocket = 5000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
			HttpGet httpget = new HttpGet(urlnew);
			httpget.setHeader("Accept", "application/json");
			httpget.setHeader("Content-type", "application/json");
//			httpget.setHeader("Authorization", "ApiKey "+TOKEN_PREF.getString("DEVICE_USER_NAME","NOTHING")+":"+TOKEN_PREF.getString("API_KEY","NOTHING"));
//			httpget.setHeader("Authorization", "ApiKey testing_application:13ec21837c64890527713e8f4cd86e1a8dac646a");
			HttpResponse response = (HttpResponse) httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if (entity != null) 
			{
				InputStream instream = entity.getContent();

				result= convertStreamToString(instream);
				instream.close();
			}
		}                
		catch(Exception e)
		{
			result="true";
			e.printStackTrace();
		}
		return result;
	}
	public static String  ApiCallGet(String url,Context ctx)
	{                

			
			try {
//				TOKEN_PREF= ctx.getSharedPreferences("SAVING_TOKEN_PREF", ctx.MODE_WORLD_READABLE);
//				url="http://tourvox.herokuapp.com/api/v1/waypoints/?lat1=37.819586&lon1=-122.478532&lat2=37.802358&lon2=-122.405806&limit=5";
//				url="http://tourvox.herokuapp.com/api/v1/waypoints/?lat1=4.879&lon1=52.3519&lat2=4.7976&lon2=52.4068&limit=5";
//				url="http://tourvox.herokuapp.com/api/v1/waypoints/?lat1=52.095&lon1=4.8791&lat2=53&lon2=5&limit=5";
//				url="http://tourvox.herokuapp.com/api/v1/waypoints/4f96605a3ad10aa9620a7358";
				Exception="false";	
				String urlnew=url;
				
				HttpParams httpParameters = new BasicHttpParams();
				int timeoutConnection = 3000;
				HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
				int timeoutSocket = 5000;
				HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
				DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
				HttpGet httpget = new HttpGet(urlnew);
				httpget.setHeader("Accept", "application/json");
				httpget.setHeader("Content-type", "application/json");
//				httpget.setHeader("Authorization", "ApiKey Goel:bf66107cc07a10deb546d9fe533360a3722cca1f");
//				httpget.setHeader("Authorization", "ApiKey "+TOKEN_PREF.getString("DEVICE_USER_NAME","NOTHING")+":"+TOKEN_PREF.getString("API_KEY","NOTHING"));
				HttpResponse response;
				response = (HttpResponse) httpclient.execute(httpget);
				System.out.println("----RESPONSE----"+response);
				HttpEntity entity = response.getEntity();
				if (entity != null) 
				{
					InputStream instream = entity.getContent();

					result= convertStreamToString(instream);
					instream.close();
				}
			} catch (ClientProtocolException e) 
			{
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

		return result;
	}
	
	

	public static String convertStreamToString(InputStream is) 
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try 
		{
			while ((line = reader.readLine()) != null)
			{
				sb.append(line + "\n");
			}
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		} 
		finally 
		{
			try 
			{
				is.close();
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	


	public static void toastmessage(Context tourMapTest, int string)
	{
		msg = Toast.makeText(tourMapTest,string,Toast.LENGTH_LONG);
		msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset() / 2);
		msg.show();

	}

	
	
	
	public static final boolean isInternetOn(Context c) 
	{
			ConnectivityManager connec = (ConnectivityManager) c
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			try
			{
			 
//			    NetworkInfo wifiNetwork = connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//			    if (wifiNetwork != null)
//			    {
//			    	if(wifiNetwork.isConnected()) {
//			      return true;
//			    	}
//			    }
//
//			    NetworkInfo mobileNetwork = connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//			    if (mobileNetwork != null)
//			    	{
//			    	if(mobileNetwork.isConnected()) {
//			      return true;
//			    	}
//			    }

			    NetworkInfo activeNetwork = connec.getActiveNetworkInfo();
			    if (activeNetwork != null)
			    	{
			    	if(activeNetwork.isConnected()) {
			      return true;
			    	}
			    }
			   
				
			}catch(Exception e)
			{
				e.printStackTrace();
				
			}
			return false;



	}
	public static void StringToastMessage(Context tourMapTest, String string)
	{
		msg = Toast.makeText(tourMapTest,string,Toast.LENGTH_LONG);
		msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset() / 2);
		msg.show();
	}  
}

