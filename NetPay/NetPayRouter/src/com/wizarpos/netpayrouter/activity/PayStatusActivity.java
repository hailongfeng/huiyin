package com.wizarpos.netpayrouter.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.wizarpos.netpayrouter.R;

/**
 * @author xuchuanren
 * @date 2015年11月10日 上午10:13:52
 * @modify 修改人： 修改时间：
 */
public class PayStatusActivity extends Activity implements OnClickListener {
	private TextView tv_paystatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_paystatus);
		initLayout();
	}

	void initLayout() {
		// 支付信息
		tv_paystatus = (TextView) findViewById(R.id.tv_paystatus);

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

}
