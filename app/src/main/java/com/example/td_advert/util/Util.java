package com.example.td_advert.util;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.widget.Toast;

import org.apache.http.conn.util.InetAddressUtils;

public class Util {
    public static File logFile;

	public static boolean runCommand(Context ctx, String command) {
		Process p;
		boolean result = false;
		try {
			// Preform su to get root privledges
			p = Runtime.getRuntime().exec("su");

			// Attempt to write a file to a root-only
			DataOutputStream os = new DataOutputStream(p.getOutputStream());
			os.writeBytes(command);

			// Close the terminal
			os.writeBytes("exit\n");
			os.flush();
			try {
				p.waitFor();
				if (p.exitValue() != 255) {
					// Sucess :-)
					result = true;
				} else {
					// Fail
					Toast.makeText(ctx, "Install Failed", Toast.LENGTH_LONG)
							.show();
				}

			} catch (InterruptedException e) {
				// Fail
				Toast.makeText(ctx, "Process Interrupted", Toast.LENGTH_LONG)
						.show();
			}
		} catch (IOException e) {
			// Fail
			String str = getStackTrace(e);

			Toast.makeText(ctx, str, Toast.LENGTH_LONG).show();
		}
		return result;
	}
	
	
	public static String getStackTrace(Throwable e) {
		StringWriter s = new StringWriter();
		PrintWriter p = new PrintWriter(s);
		e.printStackTrace(p);
		return s.getBuffer().toString();
	}

    /**
     * Get IP address from first non-localhost interface
     * @param ipv4  true=return ipv4, false=return ipv6
     * @return  address or empty string
     */
    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress().toUpperCase();
                        boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 port suffix
                                return delim<0 ? sAddr : sAddr.substring(0, delim);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) { } // for now eat exceptions
        return "";
    }

    public static void appendLog(String text)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String currentDate = sdf.format(new Date());

        File root = android.os.Environment.getExternalStorageDirectory();
        String rootDirPath = root.getAbsolutePath() + "/Nesto/log/";
        File dir = new File(rootDirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (logFile == null){
            logFile = new File(rootDirPath + "log_" + currentDate + ".txt");
            appendLog("######################################################");
            appendLog("");
            appendLog("######################################################");
        }
        if (!logFile.exists())
        {
            try
            {
                logFile.createNewFile();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try
        {
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String currentDateandTime = sdf2.format(new Date());
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append("[" + currentDateandTime + "] " + text);
            buf.newLine();
            buf.close();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
