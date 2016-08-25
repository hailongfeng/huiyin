package com.wizarpos.netpayrouter.activity;

import com.wizarpos.netpay.util.MyLog;
import com.wizarpos.netpayrouter.R;
import com.wizarpos.netpayrouter.utils.KeyBoardOperate;
import com.wizarpos.netpayrouter.utils.MyToast;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author xuchuanren
 * @date 2015年11月10日 下午1:59:56
 * @modify 修改人： 修改时间：
 */
public class SafePasswordActivity extends Activity implements OnClickListener {

	TextView title;
	EditText edt_input;
	private Context context;
	private KeyBoardOperate keyBoardOperate;
	private Button btn_exit;
	private Button btn_confirm;
	
	private static final String safePas="88888888";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_safepassword);
		context = this;
		initLayout();
	}

	void initLayout() {
		title = (TextView) findViewById(R.id.tv_title);
		title.setText("请输入安全密码");
		edt_input = (EditText) findViewById(R.id.edt_input);
		edt_input.setInputType(InputType.TYPE_NULL);
		//设置光标停留位置
		//edt_input.getSelectionStart();
		edt_input.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(s.length()>0){
					edt_input.setSelection(edt_input.getText().length());
				}
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		keyBoardOperate = new KeyBoardOperate();
		keyBoardOperate.initClickListener(getWindow(), null, edt_input);
		btn_exit = (Button) findViewById(R.id.btn_exit);
		btn_exit.setOnClickListener(this);

		btn_confirm = (Button) findViewById(R.id.btn_confirm);
		btn_confirm.setOnClickListener(this);

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.btn_exit:
			finish();
			break;
		case R.id.btn_confirm:
			if(edt_input.getTag()==null){
				MyToast.showShort(this, "安全密码不能为空");
				return;
			}
			String safeKey=edt_input.getTag().toString();
			MyLog.d(safeKey);
			if(TextUtils.isEmpty(safeKey)){
				MyToast.showShort(this, "安全密码不能为空");
			}else{
				if(safeKey.equalsIgnoreCase(safeKey)){
					startActivity(new Intent(context, ParametersSettingActivity.class));
					finish();
				}else{
					MyToast.showShort(this, "安全密码不正确");
				}
			}
			
			break;
		default:
			break;
		}
	}

}
