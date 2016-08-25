package com.wizarpos.netpayrouter.activity;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.wizarpos.netpayrouter.R;
import com.wizarpos.netpayrouter.bean.PayResultsBean;
import com.wizarpos.netpayrouter.utils.DateUtils;
import com.wizarpos.netpayrouter.utils.MyLog;
import com.wizarpos.netpayrouter.utils.MyToast;

/**
 * @author xuchuanren
 * @date 2015年11月10日 上午11:27:44
 * @modify 修改人： 修改时间：
 */
public class TransQueryActivity extends Activity implements OnClickListener {

	private Button btn_back;
	private Button btn_query;
	private Context context;
	private TextView title;

	private TextView tv_range;
	private  int   tv_range_postion=-1 ;
	
	
	private TextView tv_status;
	private Spinner spn_range;
	private Spinner spn_status;
	private EditText start_et;
	private EditText end_et;
	private  EditText et_sn ;

	private LinearLayout ll_range;
	private LinearLayout ll_status;
	
	
	
	private  String startTime ;
	private  String endTime ;
	private  int payStatus ;
	private  String sn ;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transquery);
		context = this;
		initLayout();
	}

	void initLayout() {
		title = (TextView) findViewById(R.id.tv_title);
		title.setText("记录查询");
		btn_back = (Button) findViewById(R.id.bt_back);
		btn_back.setOnClickListener(this);
		btn_query = (Button) findViewById(R.id.bt_query);
		btn_query.setOnClickListener(this);
		spn_range = (Spinner) findViewById(R.id.spn_range);
		start_et = (EditText) findViewById(R.id.start_et);
		end_et = (EditText) findViewById(R.id.end_et);
		start_et.setInputType(InputType.TYPE_NULL);
		end_et.setInputType(InputType.TYPE_NULL);
		start_et.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean hasFocus) {
				if(hasFocus){
					showDateDialog(context,start_et);
				}
			}
		});

		end_et.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(hasFocus){
					showDateDialog(context,end_et);
				}
			}
		});
		
		// 定义适配器
		final String[] obj = getResources().getStringArray(R.array.date_range);
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, obj);
		spn_range.setAdapter(adapter2);
		spn_range.setPrompt("选择时间范围");
		spn_range.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				// MyToast.showShort(getApplicationContext(), "arg2:" + arg2 +
				// " "
				// + obj[arg2]);

				// 自定义时间段
				if (arg2 == 6) {
					MyLog.d("arg2:" + arg2);
					start_et.setEnabled(true);
					start_et.requestFocus();
					end_et.setEnabled(true);
				} else {
					start_et.setEnabled(false);
					end_et.setEnabled(false);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		ll_range = (LinearLayout) findViewById(R.id.ll_range);
		ll_range.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showSelectMsg(obj, tv_range, false);
			}
		});
		tv_range = (TextView) findViewById(R.id.tv_range);

		tv_range.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showSelectMsg(obj, tv_range, false);
			}
		});

		spn_status = (Spinner) findViewById(R.id.spn_status);
		final String[] paystatus = getResources().getStringArray(
				R.array.pay_status);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, paystatus);
		spn_status.setAdapter(adapter);
		tv_status = (TextView) findViewById(R.id.tv_status);
		ll_status = (LinearLayout) findViewById(R.id.ll_status);
		ll_status.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showSelectMsg(paystatus, tv_status, true);
			}
		});
		
		
		et_sn = (EditText) findViewById(R.id.et_sn);
	}

	protected void showSelectMsg(final String[] charSequences,
			final TextView txt_waiter_msg, final boolean isStatus) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		String title = "请选择时间范围";
		if (isStatus) {
			title = "请选择支付状态";
		}
		builder.setTitle(title)
				.setItems(charSequences, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						txt_waiter_msg.setText(charSequences[which]);
						MyLog.d("which:" + which);
						if (!isStatus) {
							tv_range_postion = which;
							if (which == 6) {//自定义
								MyLog.d("arg2:" + which);
								start_et.setEnabled(true);
								//start_et.requestFocus();
								end_et.setEnabled(true);
							} else {
								
								if(which==0){
									//今天
									startTime=  DateUtils.getTimesmorning(false).getTime()+""   ;
								    endTime = DateUtils.getTimesnight(false).getTime()+"";
								}else if(which==1){
									//昨天
									startTime=  DateUtils.getTimesmorning(true).getTime()+""   ;
								    endTime = DateUtils.getTimesnight(true).getTime()+"";
								}else if(which==2){
									//本周
									startTime=  DateUtils.getTimesWeekmorning(false).getTime()+""   ;
								    endTime = DateUtils.getTimesWeeknight(false).getTime()+"";
								}else if(which ==3){
									//上一周
									startTime=  DateUtils.getTimesWeekmorning(true).getTime()+""   ;
								    endTime = DateUtils.getTimesWeeknight(true).getTime()+"";
								}else if(which==4){
									//本月
									startTime=  DateUtils.getTimesMonthmorning(false).getTime()+""   ;
								    endTime = DateUtils.getTimesMonthnight(false).getTime()+"";
								}else if(which==5){
									//上一月
									startTime=  DateUtils.getTimesMonthmorning(true).getTime()+""   ;
								    endTime = DateUtils.getTimesMonthnight(true).getTime()+"";
								}
								
								start_et.setEnabled(false);
								end_et.setEnabled(false);
							}
						}else{
							//支付状态选择
							if(which!=0){
								payStatus=PayResultsBean.status_paid;
							}else{
								payStatus=PayResultsBean.status_waitForPay ;
							}
							
						}

					}
				}).show();
	}

	
	
	public void showDateDialog(final Context context, final TextView editText) {
		Calendar calendar = Calendar.getInstance();

		int year = calendar.get(Calendar.YEAR);
		int monthOfYear = calendar.get(Calendar.MONTH);
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				String month = (monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1)
						: (monthOfYear + 1) + "";
				String day = dayOfMonth < 10 ? "0" + (dayOfMonth)
						: (dayOfMonth) + "";
				editText.setText(year + "-" + month + "-" + day);
				editText.setText(editText.getText().toString());
			}
		}, year, monthOfYear, dayOfMonth).show();

	}
	
	
	
	
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.bt_back:
			finish();
			break;
		case R.id.bt_query:
			if(tv_range_postion==6){
				//自定义
				startTime= DateUtils.date2TimeStamp(start_et.getText().toString()+" 00:00:00");
				endTime = DateUtils.date2TimeStamp(end_et.getText().toString()+"  23:59:59");
				if(Long.valueOf(startTime)-Long.valueOf(endTime)>=0){
					MyToast.showShort(context, "选择的开始时间大于结束时间");
					return ;
				}
			}
			sn=et_sn.getText().toString();
			Intent intent= new Intent();
			intent.setClass(context, QueryResultActivity.class);
			intent.putExtra("startTime", startTime);
			intent.putExtra("endTime", endTime);
			intent.putExtra("paystatus", payStatus);
			intent.putExtra("sn", sn);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

}
