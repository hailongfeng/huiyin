package com.wizarpos.netpay.common;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * 
* @author harlen
* @date 2015年11月18日 下午4:44:08
 */
public class Message implements Serializable{
	
	private static final long serialVersionUID = 1L;
	public static final int FAIL=-1;
	public static final int SUCCESS=1;
	
	private int code;
	private String message;
	private String data;
	
	
	public Message() {
		super();
	}
	public Message(int code, String message, String data) {
		super();
		this.code = code;
		this.message = message;
		this.data = data;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		JSONObject  object=new JSONObject();
		try {
			object.put("code", this.code);
			object.put("message", this.message);
			object.put("exJson", "");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return object.toString();
	}
}
