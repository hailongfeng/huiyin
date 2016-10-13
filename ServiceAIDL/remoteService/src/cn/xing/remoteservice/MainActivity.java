package cn.xing.remoteservice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewById(R.id.btn_ok).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent("com.myaction");
				mIntent.putExtra("yaner", "发送广播，相当于在这里传送数据");
				// 发送广播
				sendBroadcast(mIntent);
				finish();
			}
		});
	}
}
