/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package com.wizarpos.netpay.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.Executors;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.wizarpos.netpay.client.ClientKeepAliveMessageFactoryImp;
import com.wizarpos.netpay.util.MyLog;

/**
 * 
* @author harlen
* @date 2015年11月18日 下午4:44:08
 */
public class TcpServer extends IoHandlerAdapter{
	/** The listening port (check that it's not already in use) */
	private int port = 18888;
	private MessageHandler onMessageReceivedListern;
	PayResultHandle handle;
	NioSocketAcceptor acceptor ;
	
	private boolean isLive=false;
	
	public TcpServer(int port,PayResultHandle handle) {
		super();
		this.port = port;
		this.handle=handle;
		this.onMessageReceivedListern = this.handle.getListern();
	}
	public void startServer(){
		acceptor= new NioSocketAcceptor();
//		acceptor.getManagedSessions();
//		acceptor.addListener(listener);
        acceptor.getFilterChain().addLast("logger", new LoggingFilter());  
//        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
        acceptor.getFilterChain().addLast( "codec", new ProtocolCodecFilter( new TextLineCodecFactory( Charset.forName( "UTF-8" ))));
        //acceptor.getFilterChain().addLast("keeplive", new KeepAliveFilter(new ServerKeepAliveMessageFactoryImp(), IdleStatus.READER_IDLE, KeepAliveRequestTimeoutHandler.CLOSE,10, 5));
        acceptor.getFilterChain().addLast("threadPool", new ExecutorFilter(Executors.newCachedThreadPool()));
        KeepAliveFilter kaf = new KeepAliveFilter(new ServerKeepAliveMessageFactoryImp(), IdleStatus.READER_IDLE,
				KeepAliveRequestTimeoutHandler.CLOSE);
		kaf.setForwardEvent(true);
		kaf.setRequestInterval(10); // 说明：设置当连接的读取通道空闲的时候，心跳包请求时间间隔
		kaf.setRequestTimeout(20);
		// 说明：设置心跳包请求后 等待反馈超时时间。 超过该时间后则调用KeepAliveRequestTimeoutHandler.CLOSE
		acceptor.getFilterChain().addLast("heart", kaf);
		// 说明： 该过滤器加入到整个通信的过滤链中。
        
        acceptor.setHandler(this);
        try {
        	acceptor.setReuseAddress(true);//加上这句话，避免重启时提示地址被占用
			acceptor.bind(new InetSocketAddress(port));
			isLive=true;
			MyLog.d("Listening on port " + port+" success !");
		} catch (IOException e) {
			MyLog.d("Listening on port " + port+" faild !");
			isLive=false;
			stopServer();
			e.printStackTrace();
		}
	}
	

	public boolean isLive() {
		return isLive;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.mina.core.service.IoHandlerAdapter#exceptionCaught(org.apache.mina.core.session.IoSession, java.lang.Throwable)
	 */
	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		super.exceptionCaught(session, cause);
	}
	
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		super.messageReceived(session, message);
		if(onMessageReceivedListern!=null){
			onMessageReceivedListern.messageReceived(session, message);
		}
	}
	
	
	public void stopServer() {
		if(acceptor!=null){
			acceptor.unbind();
			acceptor.dispose(true);
			handle.unRegisterBoradcastReceiver();
			isLive=false;
		}
	}
	
	
}
