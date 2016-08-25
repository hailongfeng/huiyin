package com.wizarpos.netpay.server;

import org.apache.mina.core.session.IoSession;

/**
 * 
* @author harlen
* @date 2015年11月18日 下午4:44:08
 */
public interface MessageHandler {
	void messageReceived(IoSession session, Object message);
	void exceptionCaught(IoSession session, Throwable cause);
}
