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
				mIntent.putExtra("yaner", "���͹㲥���൱�������ﴫ������");
				// ���͹㲥
				sendBroadcast(mIntent);
				finish();
			}
		});
	}
}
