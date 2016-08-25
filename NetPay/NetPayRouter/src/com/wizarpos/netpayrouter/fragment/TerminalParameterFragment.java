package com.wizarpos.netpayrouter.fragment;

import java.util.ArrayList;

import com.wizarpos.netpayrouter.R;
import com.wizarpos.netpayrouter.activity.AddTerminalActivity;
import com.wizarpos.netpayrouter.adapter.PayRouterTerminalListAdapter;
import com.wizarpos.netpayrouter.bean.TerminalBean;
import com.wizarpos.netpayrouter.db.DBOperateUtils;
import com.wizarpos.netpayrouter.utils.MyLog;
import com.wizarpos.netpayrouter.utils.MyToast;
import com.wizarpos.netpayrouter.utils.PopuWindowUtils;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

/**
 * @author xuchuanren
 * @date 2015年11月10日 下午5:01:16
 * @modify 修改人： 修改时间：
 */
@SuppressLint("NewApi")
public class TerminalParameterFragment extends Fragment implements OnClickListener {

	private View view;
	private ListView lv_terminalList;
	private PayRouterTerminalListAdapter listAdapter = null;
	ArrayList<TerminalBean> terminal = new ArrayList<TerminalBean>();

	private Button btn_add; // 增加
	private Button btn_del;// 删除
	private Button btn_edit;
	private Button btn_lastpage;
	private Button btn_nextpage;

	private Button btn_netping;
	private Context context;

	private String currentsn = "";
	private int currentpageno = 1;
	private int maxResult = 10;
	//新增
	private  long  totalPages=0l;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.component_terminallist_layout, container, false);
		context = getActivity();

		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		init();
	}

	void init() {
		totalPages= DBOperateUtils.getInstance(getActivity()).getTotalTerminalsPage(maxResult);
		MyLog.d("totalPages:"+totalPages);
		lv_terminalList = (ListView) view.findViewById(R.id.lv_terminalList);
		// TerminalBean terminalBean = new TerminalBean();
		// terminalBean.setTerminal_ip("192.168.30.104");
		// terminalBean.setTerminal_name("大堂一楼");
		// terminalBean.setTerminal_sn("0001");
		// terminalBean.setTerminal_status("0");
		// terminalBean.setCreatedate("2015-01-01");
		//
		// TerminalBean terminalBean1 = new TerminalBean();
		// terminalBean1.setTerminal_ip("192.168.30.105");
		// terminalBean1.setTerminal_name("大堂二楼");
		// terminalBean1.setTerminal_sn("0002");
		// terminalBean1.setTerminal_status("0");
		// terminalBean1.setCreatedate("2015-01-01");
		// terminals.add(terminalBean);
		// terminals.add(terminalBean1);
		terminal = DBOperateUtils.getInstance(getActivity()).getTerminalListByPage(currentpageno, maxResult);
		listAdapter = new PayRouterTerminalListAdapter(getActivity(), terminal);
		lv_terminalList.setAdapter(listAdapter);
		lv_terminalList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
				// TODO Auto-generated method stub
				listAdapter.setCurrentposition(position);
				// view.setBackgroundColor(context.getResources().getColor(R.color.yellow));
				listAdapter.notifyDataSetChanged();
				currentsn = terminal.get(position).getTerminal_sn();
			}
		});

		btn_add = (Button) view.findViewById(R.id.btn_add);
		btn_add.setOnClickListener(this);

		btn_del = (Button) view.findViewById(R.id.btn_del);
		btn_del.setOnClickListener(this);

		btn_edit = (Button) view.findViewById(R.id.btn_edit);
		btn_edit.setOnClickListener(this);

		btn_netping = (Button) view.findViewById(R.id.btn_netping);
		btn_netping.setOnClickListener(this);

		btn_lastpage = (Button) view.findViewById(R.id.btn_lastpage);
		btn_lastpage.setOnClickListener(this);
		btn_nextpage = (Button) view.findViewById(R.id.btn_nextpage);
		btn_nextpage.setOnClickListener(this);
	}

	Handler uiHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				listAdapter.notifyDataSetChanged();
				break;
			case 1:
				PopuWindowUtils.showPopuWindow(getActivity());
				break;
			case 2:

				PopuWindowUtils.closeMyPopuWindow();
				break;
			case 3:
				MyToast.showLong(context, "无需进行网络测试");
				break;
			default:
				break;
			}

		};
	};

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.btn_add:// 添加操作
			startActivity(new Intent(getActivity(), AddTerminalActivity.class));
			break;
		case R.id.btn_del:
			if (!TextUtils.isEmpty(currentsn)) {
				int deal = DBOperateUtils.getInstance(context).deleteTerminalBySN(currentsn);
				if (deal != -1) {
					MyToast.showShort(context, "删除成功");
					terminal.remove(listAdapter.getCurrentposition());
					listAdapter.notifyDataSetChanged();
					totalPages= DBOperateUtils.getInstance(getActivity()).getTotalTerminalsPage(maxResult);
				}
				currentsn = "";
			} else {
				MyToast.showShort(context, "请选择要删除的条目");
			}
			break;
		case R.id.btn_edit:
			if (!TextUtils.isEmpty(currentsn)) {
				TerminalBean bean = terminal.get(listAdapter.getCurrentposition());
				Intent intent = new Intent();
				intent.setClass(context, AddTerminalActivity.class);
				intent.putExtra("terminal_info", bean);
				startActivity(intent);
				currentsn = "";
			} else {
				MyToast.showShort(context, "请选择要修改的条目");
			}

			break;
		case R.id.btn_lastpage:// 上一页
			currentpageno = currentpageno - 1;
			if (currentpageno <= 0) {
				MyToast.showShort(context, "目前已是第一页了");
				return;
			}
			terminal = DBOperateUtils.getInstance(context).getTerminalListByPage(currentpageno, maxResult);
			if (terminal.size() == 0) {
				MyToast.showShort(context, "目前已是第一页了");
			}
			listAdapter.notifyDataSetChanged();
			break;
		case R.id.btn_nextpage:
			currentpageno = currentpageno + 1;
			if(currentpageno>totalPages){
				MyToast.showShort(context, "目前已是最后一页了");
				currentpageno = 1;
			}else{
				terminal = DBOperateUtils.getInstance(context).getTerminalListByPage(currentpageno, maxResult);
			}
			listAdapter.notifyDataSetChanged();
			break;
		case R.id.btn_netping:
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					PopuWindowUtils.testPing(getActivity(), terminal, uiHandler);
				}
			}).start();
			// getActivity().runOnUiThread(new Runnable() {
			//
			// @Override
			// public void run() {
			// // TODO Auto-generated method stub
			// MyLog.d("网络测试");
			// PopuWindowUtils
			// .testPing(getActivity(), terminal, uiHandler);
			// }
			// });

			break;
		default:
			break;
		}
	}

}
