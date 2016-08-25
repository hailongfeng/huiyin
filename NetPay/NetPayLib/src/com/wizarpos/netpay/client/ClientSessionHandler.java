package com.wizarpos.netpay.client;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.wizarpos.netpay.common.Message;
import com.wizarpos.netpay.util.MyLog;
/**
 * 
* @author harlen
* @date 2015年11月18日 下午4:43:35
 */
public class ClientSessionHandler extends IoHandlerAdapter{

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		super.sessionOpened(session);
		Message msg=new Message(1, "from client", "");
		session.write(msg);
	}
	
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		super.messageReceived(session, message);
		MyLog.d("--客户端接收到消息--");
		Message msg=	(Message)message;
		MyLog.d("--服务器接收到消息--"+msg.getMessage());
		session.close(true);
	}

}
