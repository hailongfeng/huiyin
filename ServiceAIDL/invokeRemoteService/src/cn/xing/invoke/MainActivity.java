package cn.xing.invoke;

import cn.xing.remoteservice.IRemoteService;
import cn.xing.remoteservice.Person;
import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	private IRemoteService iRemoteService = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// ����һ��ָ��MyService��intent
		Intent intent = new Intent("cn.xing.action.remote_service");
		this.bindService(intent, new MyServiceConnection(),
				Service.BIND_AUTO_CREATE);

		Button button = (Button) this.findViewById(R.id.button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (iRemoteService != null) {
					try {
						Toast.makeText(getApplicationContext(), "RemoteService�Ѿ�������" + iRemoteService.getServiceRunTime()
										+ "����", Toast.LENGTH_LONG).show();
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				} 
			}
		});
		Button button1 = (Button) this.findViewById(R.id.button1);
		button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (iRemoteService != null) {
					try {
						Toast.makeText(getApplicationContext(), "��ǰʱ�䣺" + iRemoteService.getCurrentTime()
								, Toast.LENGTH_LONG).show();
						Person p=new Person();
						p.setName("cilent");
						p.setSex(1);
						Toast.makeText(getApplicationContext(), "�޸ĺ�" + iRemoteService.changePersion(p).getName()
								, Toast.LENGTH_LONG).show();
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				} 
			}
		});
	}

	/**
	 * ʵ��ServiceConnection�ӿ�
	 * 
	 * @author xing
	 * 
	 */
	private final class MyServiceConnection implements ServiceConnection {
		/**
		 * ��RemoteService��ʱϵͳ�ص��������
		 */
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// �˴�����ʹ��ǿ��ת��, Ӧ�õ���Stub��ľ�̬�����Ӷ����IRemoteService�ӿڵ�ʵ������
			Log.d("aa", "���ӳɹ�");
			iRemoteService = IRemoteService.Stub.asInterface(service);
		}

		/**
		 * �����RemoteService�İ�ʱϵͳ�ص��������
		 */
		@Override
		public void onServiceDisconnected(ComponentName name) {
			// �����RemoteService�İ󶨺�, ��iRemoteService����Ϊnull.
			iRemoteService = null;
		}
	}
}