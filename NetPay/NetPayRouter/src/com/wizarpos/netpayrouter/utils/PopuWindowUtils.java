package com.wizarpos.netpayrouter.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.wizarpos.netpayrouter.R;
import com.wizarpos.netpayrouter.bean.TerminalBean;

/**
 * @author xuchuanren
 * @date 2015年11月13日 上午9:33:28
 * @modify 修改人： 修改时间：
 */
public class PopuWindowUtils {

	public static PopupWindow mPopupWindow;

	public static void showPopuWindow(Context context) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.popuwindow_netping, null);
		mPopupWindow = new PopupWindow(view, 400, 280);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setBackgroundDrawable(context.getResources().getDrawable(
				R.drawable.popup));// 设置弹出窗口的背景
		mPopupWindow.setOutsideTouchable(false);
		// mPopupWindow.showAsDropDown(view);
		mPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
		ImageView close = (ImageView) view.findViewById(R.id.iv_close);
		final AnimationDrawable loadingAnimation;
		View myview = view.findViewById(R.id.v_netping);

		loadingAnimation = (AnimationDrawable) myview.getBackground();
		loadingAnimation.start();

		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				MyLog.d("popuwindow:" + "被点击了");
				if (mPopupWindow.isShowing()) {
					mPopupWindow.dismiss();
					loadingAnimation.stop();
				}
			}
		});
		mPopupWindow.update();
	}

	public static void closeMyPopuWindow() {
		if (mPopupWindow != null && mPopupWindow.isShowing()) {
			mPopupWindow.dismiss();
		}
	}

	public static void testPing(Context context,
			ArrayList<TerminalBean> terminal, Handler uiHandler) {
		if (terminal.size() == 0) {
			uiHandler.sendEmptyMessage(3);
			return;
		}

		for (int x = 0; x < terminal.size(); x++) {
			if(x==0){
				uiHandler.sendEmptyMessage(1);
			}
			try {
				Process p = null;
				try {
					p = Runtime.getRuntime().exec(
							"ping -c 7 -w 10 "
									+ terminal.get(x).getTerminal_ip());

					int status = p.waitFor();
					InputStream input = p.getInputStream();
					BufferedReader in = new BufferedReader(
							new InputStreamReader(input));
					StringBuffer buffer = new StringBuffer();
					String str = "";
					while ((str = in.readLine()) != null) {
						buffer.append(str);
						if (str.contains("packet loss")) {
							int i = str.indexOf("received");
							int j = str.indexOf("%");
							System.out.println("丢包率:"
									+ str.substring(i + 10, j + 1));
							if (!str.substring(i + 10, j + 1).contains("100%")) {
								terminal.get(x).setTerminal_status("1");
								uiHandler.sendEmptyMessage(0);
							}
							if (x == terminal.size() - 1) {
								uiHandler.sendEmptyMessage(2);
							}
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
