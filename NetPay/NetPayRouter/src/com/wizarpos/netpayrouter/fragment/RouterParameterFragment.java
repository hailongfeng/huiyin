package com.wizarpos.netpayrouter.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.wizarpos.netpayrouter.R;

/**
 * @author xuchuanren
 * @date 2015年11月10日 下午5:01:16
 * @modify 修改人： 修改时间：
 */
@SuppressLint("NewApi")
public class RouterParameterFragment extends Fragment implements
		OnClickListener {

	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.component_routeritems_layout,
				container, false);
		init();
		return view;
	}

	void init() {
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		default:
			break;
		}
	}

}
