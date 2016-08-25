package com.wizarpos.netpayrouter.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.wizarpos.netpayrouter.R;
import com.wizarpos.netpayrouter.bean.TerminalBean;

/**
 * @author xuchuanren
 * @date 2015年11月9日 下午5:05:18
 * @modify 修改人： 修改时间：
 */
public class TerminalListAdapter extends BaseAdapter {
	public Context context;
	private ArrayList<TerminalBean> terminalList = null;

	private int currentSelectedItem = -1;

	public TerminalListAdapter(Context context, ArrayList<TerminalBean> terminalList) {
		this.context = context;
		this.terminalList = terminalList;
	}

	@Override
	public int getCount() {
		return terminalList.size();
	}

	@Override
	public Object getItem(int position) {
		return terminalList.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	public TerminalBean getSelectedObject() {
		if (currentSelectedItem == -1) {
			return null;
		} else {
			return terminalList.get(currentSelectedItem);
		}
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.terminallist_layout, null);
			holder.terminal_no = (TextView) convertView.findViewById(R.id.tv_no);
			holder.terminal_name = (TextView) convertView.findViewById(R.id.tv_terminalname);
			holder.terminal_ip = (TextView) convertView.findViewById(R.id.tv_terminalip);
			holder.terminal_status = (TextView) convertView.findViewById(R.id.tv_terminalstatus);
			holder.sn = (TextView) convertView.findViewById(R.id.tv_terminalsn);
			holder.isSelected = (CheckBox) convertView.findViewById(R.id.cb_selected);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.terminal_no.setText((position + 1) + "");
		holder.terminal_name.setText(terminalList.get(position).getTerminal_name());
		holder.terminal_ip.setText(terminalList.get(position).getTerminal_ip());
		holder.sn.setText(terminalList.get(position).getTerminal_sn());
		if (terminalList.get(position).getTerminal_status().equals("1")) {
			holder.terminal_status.setText("online");
		} else {
			holder.terminal_status.setText("offline");
		}

		holder.isSelected.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				changeSelected(position);
				notifyDataSetChanged();
			}
		});

		if(currentSelectedItem==position){
			holder.isSelected.setChecked(true);
		}else{
			holder.isSelected.setChecked(false);
		}

		return convertView;
	}

	class ViewHolder {
		TextView terminal_no;
		TextView terminal_name;
		TextView terminal_ip;
		TextView sn;
		TextView terminal_status;
		CheckBox isSelected;
	}

	/**
	 * @param position
	 */
	public void changeSelected(int position) {
		if(currentSelectedItem==position){
			currentSelectedItem=-1;
		}else{
			currentSelectedItem=position;
		}
	}

}
