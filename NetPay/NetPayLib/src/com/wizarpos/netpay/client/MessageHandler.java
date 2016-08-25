package com.wizarpos.netpay.client;


/**
 * 
* @author harlen
* @date 2015年11月18日 下午4:43:47
 */
public interface MessageHandler {
	void messageReceived( Object message);
	void exceptionCaught(Throwable cause);
}
