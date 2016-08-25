package com.wizarpos.netpayrouter.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.wizarpos.netpayrouter.R;
import com.wizarpos.netpayrouter.bean.TerminalBean;

/**
 * @author xuchuanren
 * @date 2015年11月9日 下午5:05:18
 * @modify 修改人： 修改时间：
 */
public class PayRouterTerminalListAdapter extends BaseAdapter {
	private int currentposition = -1;
	public Context context;
	private ArrayList<TerminalBean> terminalList = null;

	public PayRouterTerminalListAdapter(Context context,
			ArrayList<TerminalBean> terminalList) {
		this.context = context;
		this.terminalList = terminalList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return terminalList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return terminalList.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.component_terminalitem_layout, null);
			holder.terminal_name = (TextView) convertView
					.findViewById(R.id.tv_terminalname);
			holder.terminal_ip = (TextView) convertView
					.findViewById(R.id.tv_terminalip);
			holder.terminal_status = (TextView) convertView
					.findViewById(R.id.tv_onlinestatus);
			holder.sn = (TextView) convertView.findViewById(R.id.tv_terminalsn);
			holder.termianl_createdate = (TextView) convertView
					.findViewById(R.id.tv_createdate);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.terminal_name.setText(terminalList.get(position)
				.getTerminal_name());
		holder.terminal_ip.setText(terminalList.get(position).getTerminal_ip());
		holder.sn.setText(terminalList.get(position).getTerminal_sn());
		if (terminalList.get(position).getTerminal_status().equals("1")) {
			holder.terminal_status.setText("online");
		} else {
			holder.terminal_status.setText("offline");
		}
		holder.termianl_createdate.setText(terminalList.get(position)
				.getCreatedate());
		if (getCurrentposition() != -1 && position == getCurrentposition()) {
			convertView.setBackgroundColor(context.getResources().getColor(
					R.color.yellow));
		} else {
			convertView.setBackgroundColor(context.getResources().getColor(
					R.color.gray));

		}

		return convertView;
	}

	/**
	 * @return the currentposition
	 */
	public int getCurrentposition() {
		return currentposition;
	}

	/**
	 * @param currentposition
	 *            the currentposition to set
	 */
	public void setCurrentposition(int currentposition) {
		this.currentposition = currentposition;
	}

	class ViewHolder {
		TextView sn;
		TextView terminal_name;
		TextView terminal_ip;
		TextView termianl_createdate;
		TextView terminal_status;
	}

}
