package com.wizarpos.netpayrouter.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author xuchuanren
 * @date 2015年11月16日 上午10:15:41
 * @modify 修改人： 修改时间：
 */
public class SharePreferenceUtils {

	SharedPreferences mySharedPreferences = null;
	private static SharePreferenceUtils sharePreferenceUtils;

	private SharePreferenceUtils(Context context) {
		// 实例化SharedPreferences对象（第一步）
		mySharedPreferences = context.getSharedPreferences("test",
				Activity.MODE_PRIVATE);

	}

	public  static SharePreferenceUtils getInstance(Context context) {
		if (sharePreferenceUtils == null) {
			sharePreferenceUtils = new SharePreferenceUtils(context);
		}

		return sharePreferenceUtils;
	}

	public void save(String key, String value) {
		SharedPreferences.Editor editor = mySharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public  String get(String key){
		String vaule="1";
		vaule = mySharedPreferences.getString(key, "0");
		return vaule ;
	}
	
	
	
}
