package com.wizarpos.netpayrouter.activity;

import com.wizarpos.netpayrouter.R;
import com.wizarpos.netpayrouter.fragment.RouterParameterFragment;
import com.wizarpos.netpayrouter.fragment.TerminalParameterFragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author xuchuanren
 * @date 2015年11月10日 下午2:45:03
 * @modify 修改人： 修改时间：
 */
public class ParametersSettingActivity extends FragmentActivity implements
		OnClickListener {

	private TextView title;
	private TextView tv_terminal;
	private TextView tv_router;
	private ImageView iv_back;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_parametersettings);
		initLayout();
	}

	void initLayout() {
		title = (TextView) findViewById(R.id.tv_title);
		title.setText("终端设置");
		
		replaceLayout(new TerminalParameterFragment());
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setVisibility(View.VISIBLE);
		iv_back.setOnClickListener(this);
	}

	@SuppressLint("NewApi")
	void replaceLayout(Fragment fragment) {
		FragmentTransaction fragmentManager = getFragmentManager()
				.beginTransaction();
		fragmentManager.replace(R.id.ll_contain, fragment);
		fragmentManager.commit();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.iv_back:
			finish();
			break;
		default:
			break;
		}
	}

}
