package com.wizarpos.netpay.server;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.wizarpos.netpay.util.MyLog;

/**
 * 
 * @author harlen
 * @date 2015年11月18日 下午4:44:08
 */
public class NetPayListernService extends Service {

	public static final String ACTION = "com.wizarpos.netpay.service.NetPayListernService";
	private TcpServer tcpServer;
	private String clazzName;
	private PayResultHandle payResultHandle;

	@Override
	public IBinder onBind(Intent intent) {
		return myBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		MyLog.d("NetPayListernService onCreate");
		payResultHandle = new PayResultHandle(getApplicationContext());
		tcpServer = new TcpServer(18888, payResultHandle);
		tcpServer.startServer();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		MyLog.d("NetPayListernService onDestroy");
		tcpServer.stopServer();
		payResultHandle = null;
		tcpServer = null;
		myBinder=null;
		clazzName=null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		MyLog.d("NetPayListernService onStart");
		super.onStart(intent, startId);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		MyLog.d("NetPayListernService onStartCommand");
		try {
			if (intent != null && payResultHandle != null) {
				clazzName = intent.getStringExtra("clazzName");
				payResultHandle.setClazzName(clazzName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	private NetPayServerStatus myBinder = new NetPayServerStatus();

	public class NetPayServerStatus extends Binder {

		public NetPayServerStatus getService() {
			return NetPayServerStatus.this;
		}

		public boolean isLive() {
			return tcpServer.isLive();
		}
	}
}
