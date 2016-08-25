package com.wizarpos.netpayrouter.fragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wizarpos.netpayrouter.R;
import com.wizarpos.netpayrouter.activity.PayListenerActivity;
import com.wizarpos.netpayrouter.adapter.TerminalListAdapter;
import com.wizarpos.netpayrouter.bean.TerminalBean;
import com.wizarpos.netpayrouter.db.DBOperateUtils;
import com.wizarpos.netpayrouter.utils.MyLog;
import com.wizarpos.netpayrouter.utils.MyToast;
import com.wizarpos.netpayrouter.utils.PopuWindowUtils;
import com.wizarpos.netpayrouter.utils.Tools;

/**
 * 
 * @author harlen
 * @date 2015年11月19日 下午4:18:55
 */
@SuppressLint("NewApi")
public class PayListenerFragment extends Fragment implements OnClickListener {
	String inParameter;
	private View view;
	private EditText et_inputmoney;
	Button sendmsg;
	Button back;
	Button pingnet;
	ListView terminalListView;
	TerminalListAdapter terminalListAdapter;
	PayListenerActivity context;
	ArrayList<TerminalBean> terminals;
	public JSONObject jsonRequest;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.pay_listener_fragment_layout, container, false);
		terminals = new ArrayList<TerminalBean>();

		MyLog.d("onCreateView");
		initUI();
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		MyLog.d("onAttach");
		this.context = (PayListenerActivity) activity;
	}

	void initUI() {
		et_inputmoney = (EditText) view.findViewById(R.id.et_inputmoney);
		terminalListView = (ListView) view.findViewById(R.id.lv_terminals);
		terminals.addAll(DBOperateUtils.getInstance(context).getTerminalList());
		terminalListAdapter = new TerminalListAdapter(context, terminals);
		terminalListView.setAdapter(terminalListAdapter);
		terminalListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				terminalListAdapter.changeSelected(position);
				terminalListAdapter.notifyDataSetChanged();
			}
		});

		sendmsg = (Button) view.findViewById(R.id.bt_send);
		sendmsg.setOnClickListener(this);
		back = (Button) view.findViewById(R.id.bt_back);
		back.setOnClickListener(this);
		pingnet = (Button) view.findViewById(R.id.bt_ping);
		pingnet.setOnClickListener(this);
		long money = context.getMoney();
		if (money != 0) {
			et_inputmoney.setText(Tools.formatFen(money));
		} else {
			et_inputmoney.setText("");
		}

	}

	public void onResume() {
		super.onResume();
		MyLog.d("--onResume");
		terminals.clear();
		terminals.addAll(DBOperateUtils.getInstance(context).getTerminalList());
		terminalListAdapter.notifyDataSetChanged();
		new Thread(new Runnable() {
			@Override
			public void run() {
				testPing(terminals, false);
			}
		}).start();
	}

	Handler uiHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				terminalListAdapter.notifyDataSetChanged();
				break;
			case 1:
				MyToast.showLong(context, "开始网络联通性测试");
				PopuWindowUtils.showPopuWindow(context);
				break;
			case 2:
				MyToast.showLong(context, "结束网络联通性测试");
				PopuWindowUtils.closeMyPopuWindow();
				break;
			default:
				break;
			}

		}
	};

	private PopupWindow mPopupWindow;
	private TerminalBean currentTerminal;

	void showPopuWindow() {

		long money = context.getMoney();
		if (money == 0) {
			MyToast.showShort(getActivity(), "没有收到支付方请求，不能支付");
			return;
		}
		currentTerminal = terminalListAdapter.getSelectedObject();
		if (currentTerminal == null) {
			MyToast.showShort(getActivity(), "请选择终端");
			return;
		}

		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.popuwindow_notice, null);
		// 方法1 Android获得屏幕的宽和高
		mPopupWindow = new PopupWindow(view, 400, 280);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popup));// 设置弹出窗口的背景
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

		LinearLayout ll_close = (LinearLayout) view.findViewById(R.id.ll_close);
		ll_close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (mPopupWindow.isShowing()) {
					mPopupWindow.dismiss();
				}
			}
		});

		TextView msg = (TextView) view.findViewById(R.id.tv_terminalname);
		msg.setText(currentTerminal.getTerminal_name());
		Button confirm = (Button) view.findViewById(R.id.bt_confirm);
		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mPopupWindow.dismiss();
				context.callNetPay(currentTerminal);
			}
		});
		Button cancle = (Button) view.findViewById(R.id.bt_back);
		cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				mPopupWindow.dismiss();
			}
		});
		mPopupWindow.update();

	}

	public void testPing(ArrayList<TerminalBean> terminal, boolean showDialog) {
		if (terminal.size() == 0) {
			return;
		}
		if (showDialog) {
			uiHandler.sendEmptyMessage(1);
		}
		for (int x = 0; x < terminal.size(); x++) {
			try {
				Process p = null;
				try {
					p = Runtime.getRuntime().exec("ping -c 7 -w 10 " + terminal.get(x).getTerminal_ip());

					int status = p.waitFor();
					InputStream input = p.getInputStream();
					BufferedReader in = new BufferedReader(new InputStreamReader(input));
					StringBuffer buffer = new StringBuffer();
					String str = "";
					while ((str = in.readLine()) != null) {
						buffer.append(str);
						if (str.contains("packet loss")) {
							int i = str.indexOf("received");
							int j = str.indexOf("%");
							System.out.println("丢包率:" + str.substring(i + 10, j + 1));
							if (!str.substring(i + 10, j + 1).contains("100%")) {
								terminal.get(x).setTerminal_status("1");
								uiHandler.sendEmptyMessage(0);
							}

						}
					}

					if (x == terminal.size() - 1) {
						if (showDialog) {
							uiHandler.sendEmptyMessage(2);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
					uiHandler.sendEmptyMessage(2);
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
				uiHandler.sendEmptyMessage(2);
			}
		}

	}

	@Override
	public void onClick(View arg0) {

		switch (arg0.getId()) {
		case R.id.bt_send:// 发送支付
			showPopuWindow();
			break;
		case R.id.bt_ping:// 网络测试
			new Thread(new Runnable() {
				@Override
				public void run() {
					testPing(terminals, true);
				}
			}).start();

			break;
		case R.id.bt_back:// 返回
			// finish();
			String para = context.getInParameter();
			if (TextUtils.isEmpty(para)) {
				context.finish();
			} else {
				JSONObject result = context.gernerateResult(0, "支付取消", "");
				context.payReturn(result.toString());
			}
			break;
		default:
			break;
		}
	}

	/**
	 * @param inParameter
	 */
	public void setInParameter(String inParameter) {
		this.inParameter = inParameter;
	}

}
