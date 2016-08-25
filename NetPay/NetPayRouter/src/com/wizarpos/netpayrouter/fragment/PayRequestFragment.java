package com.wizarpos.netpayrouter.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wizarpos.netpay.client.MessageHandler;
import com.wizarpos.netpay.client.TcpClient;
import com.wizarpos.netpay.common.Message;
import com.wizarpos.netpayrouter.R;
import com.wizarpos.netpayrouter.View.ProgressWheel;
import com.wizarpos.netpayrouter.activity.PayListenerActivity;
import com.wizarpos.netpayrouter.bean.PayResultsBean;
import com.wizarpos.netpayrouter.bean.TerminalBean;
import com.wizarpos.netpayrouter.utils.DateUtils;
import com.wizarpos.netpayrouter.utils.MyLog;
import com.wizarpos.netpayrouter.utils.SharePreferenceUtils;

/**
 * 
* @author harlen
* @date 2015年11月19日 下午4:18:31
 */
@SuppressLint("NewApi")
public class PayRequestFragment extends Fragment implements
		OnClickListener {
	String inParameter;
	private TextView tv_terminalname;
	private PayListenerActivity  context ;
	private View view;
	
	//新增
	public JSONObject jsonRequest;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.pay_request_fragment_layout,
				container, false);
		initUI();
		return view;
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.context=(PayListenerActivity) activity;
	}

	void initUI() {
		ProgressWheel  proprogress_wheel = (ProgressWheel) view.findViewById(R.id.progress_wheel);
		tv_terminalname = (TextView) view.findViewById(R.id.tv_terminalname);
//		String terminal_name = getIntent().getStringExtra("terminal_name");
		String terminal_name ="";
		tv_terminalname.setText(terminal_name);
//		back = (ImageView) view.findViewById(R.id.iv_back);
//		// back.setBackground(getResources().getDrawable(R.drawable.back));
//		back.setVisibility(View.VISIBLE);
//		back.setOnClickListener(this);
		
		createPayResult();
	}

	
	void  createPayResult(){
		PayResultsBean  payresult= new PayResultsBean();
		payresult.setPaymoney("100.00");
		payresult.setPaystatus(PayResultsBean.status_waitForPay);
		String waterno= DateUtils.getCreateDate(DateUtils.time_format);
		String startno = SharePreferenceUtils.getInstance(context).get("startno");
		int  no = Integer.valueOf(startno);
		MyLog.d("startno:"+no);
		payresult.setWaterno(waterno);
		payresult.setPaytime(System.currentTimeMillis());
//		TerminalBean  terminalBean = (TerminalBean) getIntent().getSerializableExtra("terminal_info");
//        payresult.setSn(terminalBean.getTerminal_sn());		
//		payresult.setTerminal_name(terminalBean.getTerminal_name());
//		DBOperateUtils.getInstance(context).insertDB_payreulsts(payresult);
	}
	
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.iv_back:
//			finish();
			break;

		default:
			break;
		}
	}
	/**
	 * @param currentTerminal
	 */
	public void callNetPay(TerminalBean currentTerminal) {
		String para=this.inParameter;
		MessageHandler listern = new MessageHandler() {
			@Override
			public void messageReceived(Object message) {
				String msgstr=(String)message;
				MyLog.d("messageReceived:"+msgstr);
				context.payReturn(msgstr);
			}

			@Override
			public void exceptionCaught(Throwable cause) {
				cause.printStackTrace();
				MyLog.d("erro:"+cause.getMessage());
				JSONObject result=context.gernerateResult(1, cause.getMessage(), "");
				context.payReturn(result.toString());
			}
		};
		String ip=currentTerminal.getTerminal_ip();
		TcpClient client = new TcpClient(ip,listern);
		Message msg = new Message(1, "请求支付", para);
		client.sendMessage(msg);
	}
	/**
	 * @param tHIRD_APP_PARAMETERS
	 */
	public void setInParameter(String tHIRD_APP_PARAMETERS) {
		this.inParameter=tHIRD_APP_PARAMETERS;
	}
	
	
}
