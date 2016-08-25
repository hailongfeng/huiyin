package com.wizarpos.netpayrouter.activity;

import java.util.ArrayList;

import com.wizarpos.netpay.util.MyLog;
import com.wizarpos.netpayrouter.R;
import com.wizarpos.netpayrouter.adapter.QueryResultAdapter;
import com.wizarpos.netpayrouter.bean.PayResultsBean;
import com.wizarpos.netpayrouter.db.DBOperateUtils;
import com.wizarpos.netpayrouter.utils.MyToast;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author xuchuanren
 * @date 2015年11月10日 上午11:50:40
 * @modify 修改人： 修改时间：
 */
public class QueryResultActivity extends Activity implements OnClickListener {

	private TextView txt_page_info;
	private TextView title;
	private Button btn_back;
	private Button btn_lastpage;
	private Button btn_nextpage;
	private ListView lv_queryresult;
	private Context context;
	private QueryResultAdapter queryResultAdapter;
	private int currentpage = 1;
	private static final int pageSize = 5;
	private long pageCount = 0;
	ArrayList<PayResultsBean> list;
	String startTime = "";
	String endTime = "";
	int payStatus = 0;
	String sn = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_queryresult);
		context = this;
		data();
		initLayout();
	}

	void data() {
		startTime = getIntent().getStringExtra("startTime");
		endTime = getIntent().getStringExtra("endTime");
		payStatus = getIntent().getIntExtra("paystatus", 0);
		sn = getIntent().getStringExtra("sn");
		MyLog.d("startTime:" + startTime + ",endTime:" + endTime + ",payStatus:" + payStatus+",sn:"+sn);
	}

	void initLayout() {
		txt_page_info = (TextView) findViewById(R.id.txt_page_info);
		title = (TextView) findViewById(R.id.tv_title);
		title.setText("记录查询");
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
		btn_lastpage = (Button) findViewById(R.id.btn_lastpage);
		btn_lastpage.setOnClickListener(this);
		btn_nextpage = (Button) findViewById(R.id.btn_nextpage);
		btn_nextpage.setOnClickListener(this);
		lv_queryresult = (ListView) findViewById(R.id.lv_queryresult);
		long count = DBOperateUtils.getInstance(context).getPayResultCount(startTime, endTime, payStatus, sn,
				currentpage, pageSize);
		MyLog.d("count:" + count);
		pageCount = (count / pageSize) + ((count % pageSize) > 0 ? 1 : 0);

		txt_page_info.setText("页码："+currentpage+" / "+pageCount);
		MyLog.d("pageCount:" + pageCount);
		list = DBOperateUtils.getInstance(context).getPayResultList(startTime, endTime, payStatus, sn, currentpage,
				pageSize);
		queryResultAdapter = new QueryResultAdapter(context, list);
		lv_queryresult.setAdapter(queryResultAdapter);
	}
	
	private void updatePage(){
		txt_page_info.setText("页码："+currentpage+" / "+pageCount);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_lastpage:
			// 上一页
			if (1 == currentpage) {
				MyToast.showShort(context, "目前已是第一页了");
				return;
			}
			currentpage--;
			ArrayList<PayResultsBean> temps1 = DBOperateUtils.getInstance(context).getPayResultList(startTime, endTime,
					payStatus, sn, currentpage, pageSize);
			list.clear();
			list.addAll(temps1);
			queryResultAdapter.notifyDataSetChanged();
			updatePage();
			break;
		case R.id.btn_nextpage:
			// 下一页
			if (pageCount == currentpage) {
				MyToast.showShort(context, "目前已是最后一页了");
				return;
			}
			currentpage++;
			ArrayList<PayResultsBean> temps2 = DBOperateUtils.getInstance(context).getPayResultList(startTime, endTime,
					payStatus, sn, currentpage, pageSize);
			list.clear();
			list.addAll(temps2);
			queryResultAdapter.notifyDataSetChanged();
			updatePage();
			break;

		default:
			break;
		}
	}

}
