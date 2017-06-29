package com.jarvis.mytaobao.home;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jarvis.http.CU_JSONResolve;
import com.jarvis.http.GetHttp;
import com.jarvis.mytaobaotest.R;
import com.javis.Adapter.Adapter_ListView_ware;
import com.lesogo.cu.custom.xListview.XListView;
import com.lesogo.cu.custom.xListview.XListView.IXListViewListener;

/**
 * 多个商品展示界面
 * @author http://yecaoly.taobao.com
 */
@SuppressLint("SimpleDateFormat")
public class WareActivity extends Activity implements OnTouchListener, IXListViewListener {
	//显示所有商品的列表
	private XListView listView;
	//整个顶部搜索控件，用于隐藏和显示底部整个控件
	private LinearLayout ll_search;
	//返回按钮
	private ImageView iv_back;
	@SuppressWarnings("unused")
	private EditText ed_search;
	
	private AnimationSet animationSet;
	/**第一次按下屏幕时的Y坐标*/
	float fist_down_Y = 0;
	/**请求数据的页数*/
	private int pageIndex = 0;
	/**存储网络返回的数据*/
	private HashMap<String, Object> hashMap;
	/**存储网络返回的数据中的data字段*/
	private ArrayList<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ware_a);
		initView();
		//请求网络数据
		new WareTask().execute();
	}

	private void initView() {

		ll_search = (LinearLayout) findViewById(R.id.ll_choice);
		ed_search = (EditText) findViewById(R.id.ed_Searchware);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		listView = (XListView) findViewById(R.id.listView_ware);
		listView.setOnTouchListener(this);
		listView.setXListViewListener(this);
		// 设置可以进行下拉加载的功能
		listView.setPullLoadEnable(true);
		listView.setPullRefreshEnable(true);
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onTouch(View arg0, MotionEvent event) {
		float y = event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			//第一次按下时的坐标
			fist_down_Y = y;
			break;
		case MotionEvent.ACTION_MOVE:
			// 向上滑动，隐藏搜索栏
			if (fist_down_Y - y > 250 && ll_search.isShown()) {
				if (animationSet != null) {
					animationSet = null;
				}
				animationSet = (AnimationSet) AnimationUtils.loadAnimation(this, R.anim.up_out);
				ll_search.startAnimation(animationSet);
				ll_search.setY(-100);
				ll_search.setVisibility(View.GONE);
			}
			// 向下滑动，显示搜索栏
			if (y - fist_down_Y > 250 && !ll_search.isShown()) {
				if (animationSet != null) {
					animationSet = null;
				}
				animationSet = (AnimationSet) AnimationUtils.loadAnimation(this, R.anim.down_in);
				ll_search.startAnimation(animationSet);
				ll_search.setY(0);
				ll_search.setVisibility(View.VISIBLE);
			}
			break;

		}
		return false;

	}

	private class WareTask extends AsyncTask<Void, Void, HashMap<String, Object>> {
		
		ProgressDialog dialog=null;

		@Override
		protected void onPreExecute() {
			if (dialog==null) {
				dialog=ProgressDialog.show(WareActivity.this, "","正在加载...");
				dialog.show();
			}
			
			
		}

		@Override
		protected HashMap<String, Object> doInBackground(Void... arg0) {
			String url = "";
			if (pageIndex == 0) {
				url = "http://192.168.0.111:3000/taoBaoQuery";
			} else {
				url = "http://192.168.0.111:3000/taoBaoQuery?pageIndex=" + pageIndex;
			}
			//请求数据，返回json
			String json = GetHttp.RequstGetHttp(url);
			//第一层的数组类型字段
			String[] LIST1_field = { "data" };
			
			//第二层的对象类型字段
			String[] STR2_field = { "id", "name", "price", "sale_num", "address", "pic" };
			ArrayList<String[]> aL_STR2_field = new ArrayList<String[]>();
			//第二层的对象类型字段放入第一层的数组类型字段中
			aL_STR2_field.add(STR2_field);
			//解析返回的json
			hashMap = CU_JSONResolve.parseHashMap2(json, null, LIST1_field, aL_STR2_field);
			return hashMap;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			
			if (dialog!=null&&dialog.isShowing()) {
				dialog.dismiss();
				dialog=null;
			}
			
			
			//如果网络数据请求失败，那么显示默认的数据
			if (result != null && result.get("data") != null) {
				//得到data字段的数据
				arrayList.addAll((Collection<? extends HashMap<String, Object>>) result.get("data"));
				listView.setAdapter(new Adapter_ListView_ware(WareActivity.this, arrayList));
				listView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
						Intent intent = new Intent(WareActivity.this, BabyActivity.class);
						startActivity(intent);
					}
				});
				
			}else {
				listView.setAdapter(new Adapter_ListView_ware(WareActivity.this));
				listView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
						Intent intent = new Intent(WareActivity.this, BabyActivity.class);
						startActivity(intent);
					}
				});
			}

			// 停止刷新和加载
			onLoad();

		}

	}

	/** 刷新 */
	@Override
	public void onRefresh() {
		// 刷新数据
		pageIndex = 0;
		arrayList.clear();
		new WareTask().execute();
		// 停止刷新和加载
		onLoad();

	}

	/** 加载更多 */
	@Override
	public void onLoadMore() {
		pageIndex += 1;
		if (pageIndex >= 4) {
			Toast.makeText(this, "已经最后一页了", Toast.LENGTH_SHORT).show();
			onLoad();
			return;
		}
		new WareTask().execute();

	}

	/** 停止加载和刷新 */
	private void onLoad() {
		listView.stopRefresh();
		// 停止加载更多
		listView.stopLoadMore();

		// 设置最后一次刷新时间
		listView.setRefreshTime(getCurrentTime(System.currentTimeMillis()));
	}

	/** 简单的时间格式 */
	public static SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");

	public static String getCurrentTime(long time) {
		if (0 == time) {
			return "";
		}

		return mDateFormat.format(new Date(time));

	}

}
