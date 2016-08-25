/**
 * 
 */
package com.wizarpos.netpay.server;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.wizarpos.netpay.server.NetPayListernService.NetPayServerStatus;

/**
 * @author harlen
 * @date 2015年11月24日 上午9:03:45
 */
public class NetPayProxy {
	private static NetPayProxy netPayProxy;

	private NetPayProxy() {
	}

	public synchronized static NetPayProxy getInstance() {
		if (netPayProxy == null) {
			netPayProxy = new NetPayProxy();
		}
		return netPayProxy;
	}

	public void startServer(Context context, Class clazz) {
		Intent mIntent = new Intent();
		mIntent.putExtra("clazzName", clazz.getName());
		mIntent.setAction(NetPayListernService.ACTION);// 你定义的service的action
		mIntent.setPackage(context.getPackageName());// 这里你需要设置你应用的包名
		context.startService(mIntent);
	}

	public void stopServer(Context context) {
		Intent mIntent = new Intent();
		mIntent.setAction(NetPayListernService.ACTION);// 你定义的service的action
		mIntent.setPackage(context.getPackageName());// 这里你需要设置你应用的包名
		context.stopService(mIntent);
	}

	public void paySuccess(Context context, String response) {
		Intent mIntent = new Intent(PayReturnBroadcastReceiver.ACTION_NAME);
		mIntent.putExtra("response", response);
		context.sendBroadcast(mIntent);
	}

	public void isLive(final Context context,final Handler handle) {

		ServiceConnection conn = new ServiceConnection() {

			@Override
			public void onServiceDisconnected(ComponentName name) {

			}

			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				NetPayServerStatus binder = (NetPayServerStatus) service;
				boolean isLive = binder.isLive();
				Message msg=handle.obtainMessage();
				msg.what=1;
				msg.obj=isLive;
				handle.sendMessage(msg);
				context.unbindService(this);
			}
		};
		Intent intent = new Intent(context, NetPayListernService.class);
		context.bindService(intent, conn, Context.BIND_AUTO_CREATE);
	}
}
