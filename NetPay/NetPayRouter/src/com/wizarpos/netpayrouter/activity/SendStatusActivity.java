package com.wizarpos.netpayrouter.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.wizarpos.netpayrouter.R;
import com.wizarpos.netpayrouter.bean.PayResultsBean;
import com.wizarpos.netpayrouter.bean.TerminalBean;
import com.wizarpos.netpayrouter.db.DBOperateUtils;
import com.wizarpos.netpayrouter.utils.DateUtils;
import com.wizarpos.netpayrouter.utils.MyLog;
import com.wizarpos.netpayrouter.utils.SharePreferenceUtils;

/**
 * @author xuchuanren
 * @date 2015年11月10日 上午10:13:52
 * @modify 修改人： 修改时间：
 */
public class SendStatusActivity extends Activity implements OnClickListener {
	TextView tv_terminalname;
	ImageView back;
	Context  context ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sendstatus);
		context = this ;
		initLayout();
	}

	void initLayout() {
		tv_terminalname = (TextView) findViewById(R.id.tv_terminalname);
		String terminal_name = getIntent().getStringExtra("terminal_name");
		tv_terminalname.setText(terminal_name);
		back = (ImageView) findViewById(R.id.iv_back);
		// back.setBackground(getResources().getDrawable(R.drawable.back));
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(this);
		
		createPayResult();
	}

	
	void  createPayResult(){
		PayResultsBean  payresult= new PayResultsBean();
		payresult.setUid(System.currentTimeMillis()/1000+"");
		payresult.setPaymoney("100.00");
		payresult.setPaystatus(PayResultsBean.status_waitForPay);
		String waterno= DateUtils.getCreateDate(DateUtils.time_format);
		String startno = SharePreferenceUtils.getInstance(context).get("startno");
		int  no = Integer.valueOf(startno);
		MyLog.d("startno:"+no);
		payresult.setWaterno(waterno);
		payresult.setPaytime(System.currentTimeMillis());
		MyLog.d("paytime:"+(System.currentTimeMillis()));
		TerminalBean  terminalBean = (TerminalBean) getIntent().getSerializableExtra("terminal_info");
        payresult.setSn(terminalBean.getTerminal_sn());		
		payresult.setTerminal_name(terminalBean.getTerminal_name());
		DBOperateUtils.getInstance(context).insertPayreulsts(payresult);
		
	}
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.iv_back:
			finish();
			break;

		default:
			break;
		}
	}

}
