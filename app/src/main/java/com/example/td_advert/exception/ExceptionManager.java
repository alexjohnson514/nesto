package com.example.td_advert.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.example.td_advert.database.TestAdapter;
import com.example.td_advert.util.Util;

import android.content.Context;
import android.widget.Toast;

public class ExceptionManager implements UncaughtExceptionHandler {
	private Context ctx;
	private static boolean showingException = false;

	public ExceptionManager(Context ctx) {
		this.ctx = ctx;
	}

	public void clearContext() {
		ctx = null;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		try {
			if (showingException) {
				android.os.Process.killProcess(android.os.Process.myPid());
				return;
			}

			showingException = true;

			PhoneStatus phoneStatus = PhoneStatus.fetchStatus(ctx);

            Util.appendLog("Exception: " + getStackTrace(ex));

			report(phoneStatus,
                    "adil.farooq@gmail.com",
                    "Tadvert Exception Reporting",
                    "An Error has occured. "
                            + "Please describe what operation you were performing while this problem occured and "
                            + "send this mail to the Support Team.. \nThank you for your kind help.\n"
                            + "*********** Describe details below this line ************\n\n\n"
                            + "*************" + getStackTrace(ex) + "\n\n");
		} catch (Throwable e) {
			e.printStackTrace();
			String str = e.getMessage();
			Toast.makeText(ctx, "" + str, Toast.LENGTH_LONG).show();
		}
		 android.os.Process.killProcess(android.os.Process.myPid());
	}

	void saveExceptionLogToDB(String log) {

		TestAdapter mDbHelper = new TestAdapter(ctx);
		mDbHelper.createDatabase();
		mDbHelper.open();

		mDbHelper.saveExceptionLog(log);

		mDbHelper.close();
	}

	public static String getStackTrace(Throwable e) {
		StringWriter s = new StringWriter();
		PrintWriter p = new PrintWriter(s);
		e.printStackTrace(p);
		return s.getBuffer().toString();
	}

	private void report(PhoneStatus phoneStatus, String mailTo,
			String emailSubject, String emailBody) {
		// Intent sendIntent = new Intent(Intent.ACTION_SEND);
		// sendIntent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
		// sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { mailTo });
		emailBody += "\n\n\n";
		emailBody += "******Do not change below********\n\n";
		emailBody += phoneStatus.toString();
		saveExceptionLogToDB(emailBody);
		//
		// try {
		//
		// saveExceptionLogToDB(emailBody);
		// } catch (Throwable throwable) {
		// }
		// sendIntent.putExtra(Intent.EXTRA_TEXT, emailBody);
		//
		// sendIntent.setType("message/rfc822");
		// ctx.startActivity(Intent.createChooser(sendIntent, "Send Email"));
	}

	public ArrayList<NameValuePair> toPostParams(String log) {
		ArrayList<NameValuePair> arrayList = new ArrayList<NameValuePair>();

		arrayList.add(new BasicNameValuePair("exceptionLog", log));

		return arrayList;
	}
}