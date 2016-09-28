package com.tiger.quicknews.utils;

import android.app.Activity;
import android.util.DisplayMetrics;

public class BaseTools {
	
	/** ��ȡ��Ļ�Ŀ�� */
	public final static int getWindowsWidth(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}
}
