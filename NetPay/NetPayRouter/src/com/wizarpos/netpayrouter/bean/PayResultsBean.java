package com.wizarpos.netpayrouter.bean;

import android.content.Context;


/**
 * @author xuchuanren
 * @date   2015年11月16日 上午9:43:55
 * @modify  修改人：              修改时间：
 */
public class PayResultsBean {
	private  String uid;
     private String  waterno ;
     private long  paytime ;
     private String  paymoney ;
     private String  terminal_name ;
     private  String  sn ;
     private  int  paystatus ;
     
     public static final int status_waitForPay=1;
     public static final int status_paid=2;
	/**
	 * @return the waterno
	 */
	public String getWaterno() {
		return waterno;
	}
	/**
	 * @param waterno the waterno to set
	 */
	public void setWaterno(String waterno) {
		this.waterno = waterno;
	}
	
	public long getPaytime() {
		return paytime;
	}
	public void setPaytime(long paytime) {
		this.paytime = paytime;
	}
	/**
	 * @return the paymoney
	 */
	public String getPaymoney() {
		return paymoney;
	}
	/**
	 * @param paymoney the paymoney to set
	 */
	public void setPaymoney(String paymoney) {
		this.paymoney = paymoney;
	}
	/**
	 * @return the terminal_name
	 */
	public String getTerminal_name() {
		return terminal_name;
	}
	/**
	 * @param terminal_name the terminal_name to set
	 */
	public void setTerminal_name(String terminal_name) {
		this.terminal_name = terminal_name;
	}
	/**
	 * @return the sn
	 */
	public String getSn() {
		return sn;
	}
	/**
	 * @param sn the sn to set
	 */
	public void setSn(String sn) {
		this.sn = sn;
	}
	
	public int getPaystatus() {
		return paystatus;
	}
	public String  getPaystatusText(Context context) {
		if(paystatus==status_waitForPay){
			return "待支付";
		}else{
			return "已支付";
		}
	}
	
	public void setPaystatus(int paystatus) {
		this.paystatus = paystatus;
	}
	/**
	 * @return the uid
	 */
	public String getUid() {
		return uid;
	}
	/**
	 * @param uid the uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}
     
}
