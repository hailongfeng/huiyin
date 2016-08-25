package com.wizarpos.netpayrouter.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.os.Handler;

import com.wizarpos.netpayrouter.bean.TerminalBean;

/**
 * @author xuchuanren
 * @date 2015年11月11日 下午2:08:48
 * @modify 修改人： 修改时间：
 */
public class NetPingTestUtils {

	public void testPing(ArrayList<TerminalBean> terminal, Handler uiHandler) {

		for (int x = 0; x < terminal.size(); x++) {
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
							if (!str.substring(i + 10, j + 1).equals("100%")) {
								terminal.get(x).setTerminal_status("1");
								uiHandler.sendEmptyMessage(0);
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
