/**
 * 
 */
package com.wizarpos.thirdnetpay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

/**
 * 
 * @author harlen
 * @date 2015年12月2日 下午1:35:56
 */
public class ClientPay {
//	private final String serverHoset = "10.1.1.178";
//	 String serverHoset = "192.168.1.13";
	private final String serverHoset = "192.168.1.142";
	private final int port = 18888;

	public static void main(String args[]) { 
		new ClientPay().pay();
	}

	/**
	 * 并发支付
	 */
	public void moreThreadPay(){
		for (int i = 0; i < 2; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					new ClientPay().pay();
				}
			}).start();
		}
	}
	/**
	 * 支付接口
	 */
	public void pay() {
		JSONObject jsonRequest = new JSONObject();
		int money;
		do {
			money = new Random().nextInt(100000);
		} while (0 == money);
		setPayParameters(jsonRequest, money + "");
		String requestPara = jsonRequest.toString();
		Socket socket = null;
		PrintWriter os = null;
		BufferedReader is = null;
		System.out.println("Client:" + requestPara);
		try {
			socket = new Socket(serverHoset, port);
			os = new PrintWriter(socket.getOutputStream());
			is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			os.println(requestPara);
//			os.println("{");
			os.flush();
			System.out.println("Server:" + is.readLine());
		} catch (Exception e) {
			System.out.println("Error" + e); // 出错，则打印出错信息
		} finally {
			os.close(); // 关闭Socket输出流
			try {
				is.close();// 关闭Socket输入流
				socket.close(); // 关闭Socket
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void setPayParameters(JSONObject jsonRequest, String amount) {
		try {
//			jsonRequest.put("mid", "WP14521000001121");
//			jsonRequest.put("mid", "100300010000001");
//			jsonRequest.put("mid", "100100210000013");
			jsonRequest.put("mid", "100300010000001");
			jsonRequest.put("amount", amount);
//			jsonRequest.put ("choosePayMode", 3);
			jsonRequest.put("transType", "netTransact");
			jsonRequest.put("button_control", "100");
			jsonRequest.put("memberCard", "");
//			jsonRequest.put("noPrint", "1");
			jsonRequest.put("noTicket", "0");
			JSONObject btnAvailable = new JSONObject();
			btnAvailable.put("btnAliPay", "0");
			btnAvailable.put("aliPayFlag", "");
			btnAvailable.put("btnWxPay", "1");
			btnAvailable.put("wxPayFlag", "");
			btnAvailable.put("btnBankPay", "");
			btnAvailable.put("btnCashPay", "");
			btnAvailable.put("btnMemberPay", "");
			btnAvailable.put("btnMixPay", "");
			btnAvailable.put("btnOtherPay", "");
			btnAvailable.put("btnTicketPay", "");
			jsonRequest.put("btnAvailable", btnAvailable);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
