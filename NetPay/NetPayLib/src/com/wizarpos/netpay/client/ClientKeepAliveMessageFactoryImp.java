package com.wizarpos.netpay.client;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;

import com.wizarpos.netpay.util.MyLog;

/**
 * 
* @author harlen
* @date 2015年11月18日 下午4:43:27
 */
public class ClientKeepAliveMessageFactoryImp implements KeepAliveMessageFactory{

	@Override
	public boolean isRequest(IoSession session, Object message) {
		
		if(message instanceof String && message.equals("ping")){
			MyLog.d("---Client 心跳反应请求包-----");
			return true;
		}
		
		return false;
	}

	@Override
	public boolean isResponse(IoSession session, Object message) {
		if(message instanceof String && message.equals("pong")){
			MyLog.d("---Client 心跳反应接收包-----");
			return true;
		}
		return false;
	}

	@Override
	public Object getRequest(IoSession session) {
		return "ping";
	}

	@Override
	public Object getResponse(IoSession session, Object request) {
		return null;
	}
	
	
}