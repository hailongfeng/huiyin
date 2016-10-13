package cn.xing.remoteservice;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class RemoteService extends Service {
	private long startTime;

	private boolean flag = false;
	private Object mobj = new Object();
	private String myBroadcastReceiverName = "com.myaction";

	/**
	 * IRemoteService.Stub��ʵ����IBinder��IRemoteService�ӿ�
	 * ���Stub��������������ΪonBinder()�����ķ���ֵ.
	 * 
	 * @author xing
	 *
	 */
	public class MyBinder extends IRemoteService.Stub {
		@Override
		public long getServiceRunTime() throws RemoteException {
			return System.currentTimeMillis() - startTime;
		}

		@Override
		public String getCurrentTime() throws RemoteException {
			flag = false;
			IntentFilter myIntentFilter = new IntentFilter();
			myIntentFilter.addAction(myBroadcastReceiverName);
			// ע��㲥
			registerReceiver(mReceiver, myIntentFilter);

			synchronized (mobj) {
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
						try {
							mobj.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy��MM��dd�� HHʱmm��ss�� E ", Locale.CHINESE);
			unregisterReceiver(mReceiver);
			return sdf.format(new Date());
		}

		@Override
		public Person changePersion(Person p) throws RemoteException {
			p.setName(p.getName() + "-service change");
			return p;
		}

	};

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(myBroadcastReceiverName)) {
				synchronized (mobj) {
					try {
						mobj.notify();
					} catch (Exception e) {
						e.printStackTrace();
					}
					String yaner = intent.getStringExtra("yaner");
					Log.d("aa", yaner);
				}
			} else {
				Log.d("aa", "û�п�������");
			}
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return new MyBinder();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		startTime = System.currentTimeMillis();
	}

}
