package com.wizarpos.netpayrouter.activity;

import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.wizarpos.netpay.util.MyLog;
import com.wizarpos.netpayrouter.R;
import com.wizarpos.netpayrouter.bean.PayResultsBean;
import com.wizarpos.netpayrouter.bean.TerminalBean;
import com.wizarpos.netpayrouter.db.DBOperateUtils;
import com.wizarpos.netpayrouter.fragment.PayListenerFragment;
import com.wizarpos.netpayrouter.fragment.PayRequestFragment;
import com.wizarpos.netpayrouter.utils.DateUtils;
import com.wizarpos.netpayrouter.utils.SharePreferenceUtils;

public class PayListenerActivity extends FragmentActivity {
	private ViewPager pager;
	PayListenerFragment payListenerFragment;
	PayRequestFragment payRequestFragment;
	private String THIRD_APP_PARAMETERS = null;
	private long money = 0L;
	Button query;
	Button imbtn_settings;
	private Context mContext;
	public static boolean isPaying = false;

	public JSONObject jsonRequest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_main);
		THIRD_APP_PARAMETERS = getInParameter();
		parserInParameters(THIRD_APP_PARAMETERS);
		checkPaying();
		payListenerFragment = new PayListenerFragment();
		payListenerFragment.setInParameter(THIRD_APP_PARAMETERS);
		//payListenerFragment.setPayParameters();
		
		payRequestFragment = new PayRequestFragment();
		payRequestFragment.setInParameter(THIRD_APP_PARAMETERS);

		FragmentPagerAdapter adapter = new TabPageIndicatorAdapter(getSupportFragmentManager());
		pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(adapter);
		pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				changeSelect(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		imbtn_settings = (Button) findViewById(R.id.imbtn_settings);
		imbtn_settings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, SafePasswordActivity.class));
			}
		});
		query = (Button) findViewById(R.id.bt_query);
		query.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, TransQueryActivity.class));
			}
		});

	}

	public String getInParameter() {
		Intent requestIntent = getIntent();
		Bundle bundle = requestIntent.getExtras();
		if (bundle != null) {
			String requestPara = bundle.getString("THIRD_APP");
			if (!TextUtils.isEmpty(requestPara)) {
				MyLog.d("THIRD_APP:"+requestPara);
				return requestPara;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}


	public void changeSelect(int index) {
		switch (index) {
		case 0:
			query.setVisibility(View.VISIBLE);
			;
			imbtn_settings.setVisibility(View.VISIBLE);
			pager.setCurrentItem(0, true);
			break;
		case 1:
			query.setVisibility(View.INVISIBLE);
			;
			imbtn_settings.setVisibility(View.INVISIBLE);
			pager.setCurrentItem(1, true);
			break;

		default:
			break;
		}
	}

	private static final String[] TITLE = new String[] { "路由监听", "发送支付" };

	class TabPageIndicatorAdapter extends FragmentPagerAdapter {
		public TabPageIndicatorAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// 新建一个Fragment来展示ViewPager item的内容，并传递参数
			if (position == 0) {
				return payListenerFragment;
			} else {
				return payRequestFragment;
			}
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLE[position % TITLE.length];
		}

		@Override
		public int getCount() {
			return TITLE.length;
		}
	}

	void checkPaying() {
		if (isPaying) {
			String msg = "您上笔订单在支付中，请支付完成后再发起支付";
			JSONObject result = gernerateResult(1, msg, "");
			payReturn(result.toString());
		}
	}

	public JSONObject gernerateResult(int code, String msg, String exJson) {
		JSONObject result = new JSONObject();
		try { 
			result.put("code", code);
			result.put("message", msg);
			result.put("exJson", exJson);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}

	public void payReturn(String msg) {
		Intent returenIntent = new Intent();
		MyLog.d("response123:" + msg);
		try {
			JSONObject responsePara = new JSONObject(msg);
			int code = responsePara.getInt("code");
			if (code == 0) {
				payresult.setPaystatus(PayResultsBean.status_paid);
				DBOperateUtils.getInstance(this).updatePayStatus(payresult);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		returenIntent.putExtra("response", msg);
		setResult(RESULT_OK, returenIntent);
		finish();
	}

	private void parserInParameters(String para) {
		if (!TextUtils.isEmpty(para)) {
			try {
				JSONObject paraObj = new JSONObject(para);
				String smoney = paraObj.getString("amount");
				this.money = Long.parseLong(smoney);
			} catch (JSONException e1) {
				e1.printStackTrace();
				MyLog.d("erro:"+e1.getMessage());
				String info = "参数错误！";
				JSONObject result = gernerateResult(1, info, "");
				payReturn(result.toString());
			}
		}
	}

	public Long getMoney() {
		return this.money;

	}

	/**
	 * @param currentTerminal
	 */
	public void callNetPay(TerminalBean currentTerminal) {
		createPayResult(this.money + "", currentTerminal);
		changeSelect(1);
		payRequestFragment.callNetPay(currentTerminal);

	}

	private static PayResultsBean payresult;

	void createPayResult(String money, TerminalBean currentTerminal) {
		payresult = new PayResultsBean();
		String payRecodeId = UUID.randomUUID().toString();
		payresult.setUid(payRecodeId);
		payresult.setPaymoney(money);
		payresult.setPaystatus(PayResultsBean.status_waitForPay);
		String waterno = DateUtils.getCreateDate(DateUtils.time_format);
		String startno = SharePreferenceUtils.getInstance(this).get("startno");
		int no = Integer.valueOf(startno);
		payresult.setWaterno(waterno);
		payresult.setPaytime(System.currentTimeMillis());
		payresult.setSn(currentTerminal.getTerminal_sn());
		payresult.setTerminal_name(currentTerminal.getTerminal_name());
		DBOperateUtils.getInstance(this).insertPayreulsts(payresult);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		// super.onBackPressed();

	}
}
