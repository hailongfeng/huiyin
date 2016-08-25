package com.wizarpos.netpayrouter.adapter;

import java.util.ArrayList;

import com.wizarpos.netpayrouter.R;
import com.wizarpos.netpayrouter.bean.PayResultsBean;
import com.wizarpos.netpayrouter.utils.DateUtils;
import com.wizarpos.netpayrouter.utils.MyLog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author xuchuanren
 * @date 2015年11月10日 下午3:06:10
 * @modify 修改人： 修改时间：
 */
public class QueryResultAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<PayResultsBean>  payResultList ;

	public QueryResultAdapter(Context context,ArrayList<PayResultsBean>  payResultList) {
		this.context = context;
        this.payResultList = payResultList ;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return payResultList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return payResultList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.component_queryresults, null);
			holder.tv_waterid = (TextView) convertView
					.findViewById(R.id.tv_waterid);
			holder.tv_paydate = (TextView) convertView
					.findViewById(R.id.tv_paydate);
			holder.tv_paymoney = (TextView) convertView
					.findViewById(R.id.tv_paymoney);
			holder.tv_terminalname = (TextView) convertView
					.findViewById(R.id.tv_terminalname);
			holder.tv_terminalsn = (TextView) convertView
					.findViewById(R.id.tv_terminalSN);
			holder.tv_paystatus = (TextView) convertView
					.findViewById(R.id.tv_paystatus);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();

		}
		String no="";
		if(position+1<10){
			no="0"+(position+1);
		}else{
			no =(position+1)+"";
		}
		holder.tv_waterid.setText(payResultList.get(position).getWaterno()+no);
		MyLog.d("paytime1:"+payResultList.get(position).getPaytime());
		holder.tv_paydate.setText(DateUtils.TimeStamp2date(payResultList.get(position).getPaytime()+""));
		holder.tv_paymoney.setText(payResultList.get(position).getPaymoney());
		holder.tv_terminalname.setText(payResultList.get(position).getTerminal_name());
		holder.tv_terminalsn.setText(payResultList.get(position).getSn());
		holder.tv_paystatus.setText(payResultList.get(position).getPaystatusText(context));
		return convertView;
	}

	class ViewHolder {
		TextView tv_waterid;
		TextView tv_paydate;
		TextView tv_paymoney;
		TextView tv_terminalname;
		TextView tv_terminalsn;
		TextView tv_paystatus;
	}

}
