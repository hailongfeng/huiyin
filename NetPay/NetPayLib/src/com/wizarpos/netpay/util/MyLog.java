package com.wizarpos.netpay.util;

import android.util.Log;
/**
 * 
* @author harlen
* @date 2015年11月12日 下午4:44:08
 */
public class MyLog {
	private static String TAG="mylog";
	public static void d(String msg) {
		Log.d(TAG, "--"+msg);
	}
}
