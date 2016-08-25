package com.wizarpos.netpayrouter.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;

/**
 * @author Administrator Please Write all config here
 */
public class Configs {

	public final static boolean IsLoggable = true;
	public final static boolean IsLogInFile = true;
	public final static boolean IsRecordInExternalStorage = true;
	public final static String MyLogFile = "MyLogFile.txt";
	static {

	}

	public static boolean isApkDebugable(Context context) {
		try {
			ApplicationInfo info = context.getApplicationInfo();
			return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
