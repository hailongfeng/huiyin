/**
 * 
 */
package com.wizarpos.keybord.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.wizarpos.keybord.R;


/**
 * @Description:通用键盘
 * @author harlen
 * @date 2016年3月7日 下午3:44:18
 */
public class KeyboardView extends LinearLayout implements OnClickListener {

	private Context mContext;
	private String mValue = "";
	private String num_or_money = "0";
	private OnKeyBoardInput mOnKeyBoardInput;
	private int maxLength = -1;//最大长度

	/**
	 * @param context
	 */
	public KeyboardView(Context context) {
		super(context);
	}

	public KeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context, attrs);
	}

	public KeyboardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context, attrs);
	}

	private void initView(Context context, AttributeSet attrs) {
		TypedArray t = getContext().obtainStyledAttributes(attrs, R.styleable.KeyboardView);
		boolean withDot = t.getBoolean(R.styleable.KeyboardView_with_dot, true);
		num_or_money = t.getString(R.styleable.KeyboardView_num_or_money);
		int divideColor  = t.getColor(R.styleable.KeyboardView_divideColor,0xFFFFFFFF);
		LayoutInflater inflater = LayoutInflater.from(context);
		View root = inflater.inflate(R.layout.partion_keyboard_with_dot, null);
		root.setBackgroundColor(divideColor);
		View dot = root.findViewById(R.id.key_dot);
		View del = root.findViewById(R.id.key_del);
		if (!withDot) {
			dot.setVisibility(View.GONE);
			LayoutParams delParam = (LayoutParams) del.getLayoutParams();
			delParam.weight = 2.0f;
			del.setLayoutParams(delParam);
		}
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		root.findViewById(R.id.key_0).setOnClickListener(this);
		root.findViewById(R.id.key_1).setOnClickListener(this);
		root.findViewById(R.id.key_2).setOnClickListener(this);
		root.findViewById(R.id.key_3).setOnClickListener(this);
		root.findViewById(R.id.key_4).setOnClickListener(this);
		root.findViewById(R.id.key_5).setOnClickListener(this);
		root.findViewById(R.id.key_6).setOnClickListener(this);
		root.findViewById(R.id.key_7).setOnClickListener(this);
		root.findViewById(R.id.key_8).setOnClickListener(this);
		root.findViewById(R.id.key_9).setOnClickListener(this);
		root.findViewById(R.id.key_dot).setOnClickListener(this);
		root.findViewById(R.id.key_del).setOnClickListener(this);

		addView(root, params);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		int viewId = v.getId();
		String tag = v.getTag().toString();
		int inputType = Integer.parseInt(num_or_money);
		if (!TextUtils.isEmpty(mValue)) {
			if (viewId == R.id.key_del) {
				//
				if (mValue.length() > 0) {
					mValue = mValue.substring(0, mValue.length() - 1);
				}
			} else if (viewId == R.id.key_dot) {
				//
				if(maxLength!=-1 && mValue.length() >= maxLength) {
					return;
				}
				if (mValue.contains(".")) {

				} else {
					mValue += tag;
				}
			} else {
				//以0开头，而且不是dot
				if(maxLength!=-1 && mValue.length() >= maxLength) {
					return;
				}
				if (mValue.startsWith("0")) {
					if (mValue.contains(".")) {
						mValue += tag;
					}
				} else {
					mValue += tag;
				}
			}
		} else {
			if (viewId == R.id.key_del) {
			} else if (viewId == R.id.key_dot) {
			} else {
				mValue += tag;
			}
		}
		if (mOnKeyBoardInput != null) {
			mOnKeyBoardInput.onKeyClick(mValue);
		}
	}

	public String getValue() {
		return mValue;
	}
	public String cleverValue() {
		return mValue="";
	}
	public void setDefaultValue(String value) {
		mValue=value;
	}

	public OnKeyBoardInput getOnKeyBoardInput() {
		return mOnKeyBoardInput;
	}

	public void setOnKeyBoardInput(OnKeyBoardInput mOnKeyBoardInput) {
		this.mOnKeyBoardInput = mOnKeyBoardInput;
	}
	
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public interface OnKeyBoardInput {
		void onKeyClick(String value);
	}

}
