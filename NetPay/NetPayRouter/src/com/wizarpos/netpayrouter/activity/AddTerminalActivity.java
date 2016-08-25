package com.wizarpos.netpayrouter.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.wizarpos.netpayrouter.R;
import com.wizarpos.netpayrouter.bean.TerminalBean;
import com.wizarpos.netpayrouter.db.DBOperateUtils;
import com.wizarpos.netpayrouter.utils.DateUtils;
import com.wizarpos.netpayrouter.utils.MyToast;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author xuchuanren
 * @date 2015年11月10日 下午1:38:31
 * @modify 修改人： 修改时间：
 */
public class AddTerminalActivity extends Activity implements OnClickListener {

	private TextView tv_title;
	private Button bt_checkstatus;
	private Button bt_cancle;
	private Button btn_confirm;
	private EditText et_inputip;
	private EditText et_inputname;
	private EditText et_inputsn;
	private Context context;
	private boolean isupdate = false;
	private String sn = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addterminal);
		context = this;
		initLayout();
	}

	void initLayout() {

		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("新增路由终端");
		bt_checkstatus = (Button) findViewById(R.id.bt_checkstatus);
		bt_checkstatus.setOnClickListener(this);
		bt_cancle = (Button) findViewById(R.id.bt_cancle);
		bt_cancle.setOnClickListener(this);
		btn_confirm = (Button) findViewById(R.id.bt_confirm);
		btn_confirm.setOnClickListener(this);

		et_inputip = (EditText) findViewById(R.id.et_inputip);
		et_inputname = (EditText) findViewById(R.id.et_inputname);
		et_inputsn = (EditText) findViewById(R.id.et_inputsn);
		if (getIntent().getSerializableExtra("terminal_info") != null) {
			tv_title.setText("修改路由终端");
			TerminalBean bean = (TerminalBean) getIntent().getSerializableExtra("terminal_info");
			et_inputip.setText(bean.getTerminal_ip());
			et_inputname.setText(bean.getTerminal_name());
			et_inputsn.setText(bean.getTerminal_sn());
			et_inputsn.setEnabled(false);
			sn = bean.getTerminal_sn();
			isupdate = true;
		}

	}

	public TerminalBean getBean() {
		String terminalname = et_inputname.getEditableText().toString();
		String ip = et_inputip.getEditableText().toString();
		String sn = et_inputsn.getEditableText().toString();
		if (TextUtils.isEmpty(ip)) {
			MyToast.showShort(context, "ip地址为空");
			return null;
		} else {
			if (TextUtils.isEmpty(terminalname)) {
				MyToast.showShort(context, "终端名称为空");
				return null;
			} else {
				if (TextUtils.isEmpty(sn)) {
					MyToast.showShort(context, "sn为空");
					return null;
				} else {
					TerminalBean bean = new TerminalBean();
					bean.setTerminal_ip(ip);
					bean.setTerminal_name(terminalname);
					bean.setTerminal_sn(sn);
					bean.setCreatedate(DateUtils.getCreateDate(DateUtils.time_format));
					return bean;
				}
			}
		}

	}

	void resetui() {
		et_inputname.setText("");
		et_inputip.setText("");
		et_inputsn.setText("");
	}

	Handler uiHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				MyToast.showLong(context, "检测完成，该IP可用");
				break;
			case 1:
				MyToast.showLong(context, "检测完成，该IP不可用");
				break;
			default:
				break;
			}

		};
	};

	public void testping(String ip, Handler uiHandler) {
		Process p = null;
		try {
			p = Runtime.getRuntime().exec("ping -c 7 -w 10 " + ip);
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
					if (!str.substring(i + 10, j + 1).equals("100%")) {
						uiHandler.sendEmptyMessage(0);
					} else {
						uiHandler.sendEmptyMessage(1);
					}

				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			uiHandler.sendEmptyMessage(1);
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			uiHandler.sendEmptyMessage(1);
			e.printStackTrace();
		}

	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.bt_checkstatus: // 检测
			if (TextUtils.isEmpty(et_inputip.getText().toString())) {
				MyToast.showLong(context, "请输入IP地址");
			} else {
				new Thread(new Runnable() {
					public void run() {
						testping(et_inputip.getText().toString(), uiHandler);
					}
				}).start();
			}
			break;
		case R.id.bt_cancle: // 返回
			finish();
			break;
		case R.id.bt_confirm: // 确认
			TerminalBean terminalBean = getBean();
			if (terminalBean != null) {
				if (isupdate) {
					int isSuccess = DBOperateUtils.getInstance(context).updateTerminalBySN(sn, terminalBean);
					if (isSuccess == 1) {
						MyToast.showLong(context, "更新成功");
						finish();
					} else {
						MyToast.showLong(context, "更新失败");
					}
					return;
				}

				int success = DBOperateUtils.getInstance(context).insertDB(terminalBean);
				if (success == 1) {
					MyToast.showLong(context, "添加成功");
					resetui();
				} else {
					if (success == 0) {
						MyToast.showLong(context, "相同sn号设置已存在");
					} else {
						MyToast.showLong(context, "添加失败");
					}

				}

			}
			break;
		default:
			break;
		}
	}

}
