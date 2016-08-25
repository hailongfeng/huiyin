package com.wizarpos.netpayrouter.bean;

import java.io.Serializable;

/**
 * @author xuchuanren
 * @date 2015年11月9日 下午6:10:57
 * @modify 修改人： 修改时间：
 */
public class TerminalBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private String uid;
	private String terminal_name; // 终端名称
	private String terminal_ip; // IP地址
	private String terminal_sn; // sn
	private String terminal_status;// 是否在线
	private String createdate; // 创建日期
	private boolean isSelected;

	/**
	 * @return the terminal_name
	 */
	public String getTerminal_name() {
		return terminal_name;
	}

	/**
	 * @param terminal_name
	 *            the terminal_name to set
	 */
	public void setTerminal_name(String terminal_name) {
		this.terminal_name = terminal_name;
	}

	/**
	 * @return the terminal_ip
	 */
	public String getTerminal_ip() {
		return terminal_ip;
	}

	/**
	 * @param terminal_ip
	 *            the terminal_ip to set
	 */
	public void setTerminal_ip(String terminal_ip) {
		this.terminal_ip = terminal_ip;
	}

	/**
	 * @return the terminal_sn
	 */
	public String getTerminal_sn() {
		return terminal_sn;
	}

	/**
	 * @param terminal_sn
	 *            the terminal_sn to set
	 */
	public void setTerminal_sn(String terminal_sn) {
		this.terminal_sn = terminal_sn;
	}

	/**
	 * @return the terminal_status
	 */
	public String getTerminal_status() {
		return terminal_status;
	}

	/**
	 * @param terminal_status
	 *            the terminal_status to set
	 */
	public void setTerminal_status(String terminal_status) {
		this.terminal_status = terminal_status;
	}


	/**
	 * @return the createdate
	 */
	public String getCreatedate() {
		return createdate;
	}

	/**
	 * @param createdate
	 *            the createdate to set
	 */
	public void setCreatedate(String createdate) {
		this.createdate = createdate;
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

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	
}
