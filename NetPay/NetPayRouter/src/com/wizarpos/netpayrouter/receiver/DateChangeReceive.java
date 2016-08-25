package com.wizarpos.netpayrouter.receiver;

import com.wizarpos.netpayrouter.utils.SharePreferenceUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author xuchuanren
 * @date 2015年11月16日 上午10:29:44
 * @modify 修改人： 修改时间：
 */
public class DateChangeReceive extends BroadcastReceiver {
	private static final String ACTION_DATE_CHANGED = Intent.ACTION_DATE_CHANGED;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
		if (ACTION_DATE_CHANGED.equals(action)) {
			SharePreferenceUtils.getInstance(context).save("startno", "0");
		}
	}

}
