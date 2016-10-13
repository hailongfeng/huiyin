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

		// 创建一个指向MyService的intent
		Intent intent = new Intent("cn.xing.action.remote_service");
		this.bindService(intent, new MyServiceConnection(),
				Service.BIND_AUTO_CREATE);

		Button button = (Button) this.findViewById(R.id.button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (iRemoteService != null) {
					try {
						Toast.makeText(getApplicationContext(), "RemoteService已经运行了" + iRemoteService.getServiceRunTime()
										+ "毫秒", Toast.LENGTH_LONG).show();
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
						Toast.makeText(getApplicationContext(), "当前时间：" + iRemoteService.getCurrentTime()
								, Toast.LENGTH_LONG).show();
						Person p=new Person();
						p.setName("cilent");
						p.setSex(1);
						Toast.makeText(getApplicationContext(), "修改后：" + iRemoteService.changePersion(p).getName()
								, Toast.LENGTH_LONG).show();
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				} 
			}
		});
	}

	/**
	 * 实现ServiceConnection接口
	 * 
	 * @author xing
	 * 
	 */
	private final class MyServiceConnection implements ServiceConnection {
		/**
		 * 和RemoteService绑定时系统回调这个方法
		 */
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// 此处不能使用强制转换, 应该调用Stub类的静态方法从而获得IRemoteService接口的实例对象
			Log.d("aa", "链接成功");
			iRemoteService = IRemoteService.Stub.asInterface(service);
		}

		/**
		 * 解除和RemoteService的绑定时系统回调这个方法
		 */
		@Override
		public void onServiceDisconnected(ComponentName name) {
			// 解除和RemoteService的绑定后, 将iRemoteService设置为null.
			iRemoteService = null;
		}
	}
}