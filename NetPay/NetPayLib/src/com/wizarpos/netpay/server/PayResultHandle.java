package com.wizarpos.netpay.server;

import org.apache.mina.core.session.IoSession;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.wizarpos.netpay.util.MyLog;

/**
 * 
 * @author harlen
 * @date 2015年11月18日 下午4:44:08
 */
public class PayResultHandle {
	private Context context;
	private boolean isBusy = false;
	private Object waitObj = new Object();
	private String payResult;
	private String clazzName;

	public PayResultHandle(final Context context) {
		super();
		this.context = context;
		registerBoradcastReceiver();
	}

	public void setClazzName(String clazzName) {
		this.clazzName = clazzName;
	}

	private PayReturnBroadcastReceiver receiver = new PayReturnBroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			super.onReceive(context, intent);
			String response = intent.getExtras().getString("response");
			MyLog.d("收款返回response:" + response);
			payResult = response;
			synchronized (waitObj) {
				waitObj.notify();
			}
		}
	};
	/**
	 * 生成和收款返回相同格式的JSON串
	 * @param code
	 * @param msg
	 * @param exJson
	 * @return
	 */
	public JSONObject gernerateResult(int code,String msg,String exJson){
		JSONObject result=new JSONObject();
		try {
			result.put("code", 1);
			result.put("message", msg);
			result.put("exJson", "");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
	private MessageHandler listern = new MessageHandler() {

		@Override
		public void messageReceived(IoSession session, Object message) {
				String msg = (String) message;
				MyLog.d("收到支付请求：" + msg);
				MyLog.d("收款忙：" + isBusy);
				if (isBusy) {
					JSONObject returnData = gernerateResult(1, "server is busy", "");
					payResult = returnData.toString();
					session.write(payResult);
				} else {
					isBusy = true;
					goToPay(context, msg);
					synchronized (waitObj) {
						try {
							waitObj.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					session.write(payResult);
					isBusy = false;
				}
		}

		@Override
		public void exceptionCaught(IoSession session, Throwable cause) {

		}
	};

	@SuppressWarnings("rawtypes")
	private void goToPay(final Context context, String THIRD_APP) {
		Class payActivity = null;
		try {
			payActivity = Class.forName(this.clazzName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Intent dialogIntent = new Intent(context, payActivity);
		MyLog.d("THIRD_APP:" + THIRD_APP);
		dialogIntent.putExtra("THIRD_APP", THIRD_APP);
		dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(dialogIntent);
	}

	public MessageHandler getListern() {
		return listern;
	}

	public void setListern(MessageHandler listern) {
		this.listern = listern;
	}

	public PayReturnBroadcastReceiver getReceiver() {
		return receiver;
	}

	public void setReceiver(PayReturnBroadcastReceiver receiver) {
		this.receiver = receiver;
	}

	private void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(PayReturnBroadcastReceiver.ACTION_NAME);
		context.registerReceiver(receiver, myIntentFilter);
	}

	private void callPayAPP(String amount) {
		JSONObject jsonRequest = new JSONObject();
		setPayParamers(jsonRequest, amount);
		String reqJson = jsonRequest.toString();
		Intent intent = new Intent();
		intent.setClassName("com.wizarpos.pay2", "com.wizarpos.pay.thirdapp.PayActivityProxy");
		intent.putExtra("THIRD_APP", reqJson);
		context.startActivity(intent);
	}

	private void setPayParamers(JSONObject jsonRequest, String amount) {
		try {
			jsonRequest.put("amount", amount);
			jsonRequest.put("transType", "netTransact");
			jsonRequest.put("button_control", "100");
			jsonRequest.put("memberCard", "");
			jsonRequest.put("noPrint", "1");
			jsonRequest.put("noTicket", "0");
			JSONObject btnAvailable = new JSONObject();
			btnAvailable.put("btnAliPay", "");
			btnAvailable.put("aliPayFlag", ""); // 禁用 1主扫 2被扫
			btnAvailable.put("btnWxPay", "");
			btnAvailable.put("wxPayFlag", ""); // 禁用 1主扫 2被扫
			btnAvailable.put("btnBankPay", "");
			btnAvailable.put("btnCashPay", "");
			btnAvailable.put("btnMemberPay", "");
			btnAvailable.put("btnMixPay", "");
			btnAvailable.put("btnOtherPay", "1");
			btnAvailable.put("btnTicketPay", "");
			jsonRequest.put("btnAvailable", btnAvailable);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	public void unRegisterBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(PayReturnBroadcastReceiver.ACTION_NAME);
		context.unregisterReceiver(receiver);
	}
}
