package com.example.td_advert.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.td_advert.constant.TadvertConstants;
import com.example.td_advert.web.delegate.WebTaskDelegate;

public class WebTask extends AsyncTask<Void, String, String> {

	private String url;
	private ArrayList<NameValuePair> postParameters;;
	private Exception ex;
	private WebTaskDelegate delegate;

	public WebTask(String url) {
		this(url, null, new WebTaskDelegate());
	}

	public WebTask(String url, WebTaskDelegate delegate) {
		this(url, null, delegate);
	}

	public WebTask(String url, ArrayList<NameValuePair> postParameters) {
		this(url, postParameters, new WebTaskDelegate());
	}

	public WebTask(String url, ArrayList<NameValuePair> postParameters,
			WebTaskDelegate delegate) {
		super();
		this.url = url;
		this.postParameters = postParameters;
		this.delegate = delegate;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (delegate != null) {
			delegate.synchronousPreExecute();
		}
	}

	@Override
	protected String doInBackground(Void... params) {
		InputStream instream = null;
		String result = "";
		try {
			if (delegate != null) {
				delegate.asynchronousPreExecute();
			}

			HttpParams httpParameters = new BasicHttpParams();

            HttpProtocolParams.setContentCharset(httpParameters, HTTP.UTF_8);
            HttpProtocolParams.setHttpElementCharset(httpParameters, HTTP.UTF_8);


            int timeoutConnection = 5000;
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);

			int timeoutSocket = 5000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);

			HttpRequestBase request = null;


			url = url.replaceAll(" ", "%20");


			if (postParameters != null) {
				HttpPost httpPost = new HttpPost(url);
				request = httpPost;
				httpPost.setEntity(new UrlEncodedFormEntity(postParameters, "UTF-8"));
			} else {
				request = new HttpGet(url);
			}
			// request.setHeader("Accept", "application/json");
			// request.setHeader("Content-type", "application/json");

			HttpResponse response = (HttpResponse) httpclient.execute(request);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				instream = entity.getContent();
				result = processInputStream(instream);
			}
		} catch (Exception e) {
			ex = e;
		} finally {
			try {
				instream.close();
			} catch (Exception e) {
			}
		}

		if (delegate != null) {
			delegate.asynchronousPostExecute(result);
		}

		return result;
	}

	protected String processInputStream(InputStream instream) {
		return convertStreamToString(instream);
	}

	/*public void dimissDialog() {
		if(delegate != null) {
			delegate.synchronousDead();
		}
	}*/

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);

		if (ex != null) {
			if (delegate != null) {
				delegate.dismissDialog();
				delegate.onError(ex);
				return;
			}
		}

		if (delegate != null) {
			delegate.dismissDialog();
			delegate.synchronousPostExecute(result);
		}
	}

	protected String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	private static boolean error;

	public static void showInternetConnectivityDialog(final Context ctx,
			final String message) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);
		if (!error) {
			// set title
			alertDialogBuilder.setTitle("ERROR");
			// "There is a problem with internet connectivity. "
			// + "\nPlease check your connection and try again."
			// set dialog message
			alertDialogBuilder
					.setMessage(message)
					.setCancelable(false)
					.setPositiveButton("Close App",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									Intent intent = new Intent(
											TadvertConstants.CLOSE_INTENT);
									ctx.sendBroadcast(intent);
									if (ctx instanceof Activity) {
										((Activity) ctx).finish();
									}
									// android.os.Process
									// .killProcess(android.os.Process
									// .myPid());
								}
							});

			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();

			if (alertDialog.getWindow() != null) {
				// show it
				try {
					alertDialog.show();
				} catch (Exception e) {
					showToast(ctx, message);
				}
			} else {
				showToast(ctx, message);
			}
			error = true;
		}
	}

	private static void showToast(final Context ctx, final String message) {
		((Activity) ctx).runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();

			}
		});
	}
}
