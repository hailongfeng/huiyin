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
package com.wizarpos.netpay.client;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import android.os.Handler;

import com.wizarpos.netpay.common.Message;
import com.wizarpos.netpay.util.MyLog;

/**
 * 
 * @author harlen
 * @date 2015年11月18日 下午4:44:02
 */
public class TcpClient extends IoHandlerAdapter {
	/** 会话连接 */
	private IoConnector connector;
	private MessageHandler onMessageReceivedListern;
	private static final long CONNECT_TIMEOUT = 5 * 1000L;
	private String serverIP = "";
	private int port = 0;
	private boolean isReceiveMessage;
	private static final int exceptionCaught = -1;
	private static final int messageReceived = 1;
	private static final int messageSent = 2;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case exceptionCaught:
				// 异常
				if (onMessageReceivedListern != null) {
					Throwable cause = (Throwable) msg.obj;
					onMessageReceivedListern.exceptionCaught(cause);
				}
				break;
			case messageReceived:
				if (onMessageReceivedListern != null) {
					onMessageReceivedListern.messageReceived(msg.obj);
				}
				break;

			default:
				break;
			}
		};
	};

	/**
	 * 
	 * @param serverIP
	 *            服务器地址
	 * @param port
	 *            服务器端口
	 * @param onMessageReceivedListern
	 *            回调方法
	 */
	public TcpClient(String serverIP, int port, MessageHandler onMessageReceivedListern) {
		this.serverIP = serverIP;
		this.port = port;
		this.onMessageReceivedListern = onMessageReceivedListern;
		connector = new NioSocketConnector();
		connector.setConnectTimeoutMillis(CONNECT_TIMEOUT);
		connector.setHandler(TcpClient.this);
		connector.getFilterChain().addLast("logger", new LoggingFilter());
		// connector.getFilterChain().addLast("codec",new
		// ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
		connector.getFilterChain().addLast("codec",
				new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));
//		connector.getFilterChain().addLast("keeplive", new KeepAliveFilter(new ClientKeepAliveMessageFactoryImp(),
//				IdleStatus.READER_IDLE, KeepAliveRequestTimeoutHandler.DEAF_SPEAKER, 10, 5));
		KeepAliveFilter kaf = new KeepAliveFilter(new ClientKeepAliveMessageFactoryImp(), IdleStatus.READER_IDLE,
				KeepAliveRequestTimeoutHandler.CLOSE);
		kaf.setForwardEvent(true);
		kaf.setRequestInterval(10); // 说明：设置当连接的读取通道空闲的时候，心跳包请求时间间隔
		kaf.setRequestTimeout(20);
		// 说明：设置心跳包请求后 等待反馈超时时间。 超过该时间后则调用KeepAliveRequestTimeoutHandler.CLOSE
		connector.getFilterChain().addLast("heart", kaf);
		// 说明： 该过滤器加入到整个通信的过滤链中。
	}

	public TcpClient(String serverIP, MessageHandler onMessageReceivedListern) {
		this(serverIP, 18888, onMessageReceivedListern);
	}

	/**
	 * {@异常发生}
	 */
	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		MyLog.d("----exceptionCaught");
		android.os.Message msg = android.os.Message.obtain();
		msg.obj = cause;
		msg.what = exceptionCaught;
		mHandler.sendMessage(msg);
		session.close(true);
	}

	/**
	 * {@收到消息}
	 */
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		MyLog.d("----服务器返回:" + message.toString());
		isReceiveMessage = true;
		android.os.Message msg = android.os.Message.obtain();
		msg.obj = message;
		msg.what = messageReceived;
		mHandler.sendMessage(msg);
		session.close(true);
	}

	/**
	 * {@消息已发送}
	 */
	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		// String msg=(String) message;
		MyLog.d("message is send");
	}

	/**
	 * {@会话关闭}
	 */
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		if (!isReceiveMessage) {
			android.os.Message msg = android.os.Message.obtain();
			Message message = new Message(Message.FAIL, "网络链接中断", "");
			msg.obj = message.toString();
			msg.what = messageReceived;
			mHandler.sendMessage(msg);
		}
		MyLog.d("----client sessionClosed");
	}

	/**
	 * {@会话创建}
	 */
	@Override
	public void sessionCreated(IoSession session) throws Exception {
	}

	/**
	 * {@会话空闲}
	 */
	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
	}

	/**
	 * {@会话打开}
	 */
	@Override
	public void sessionOpened(IoSession session) throws Exception {
	}

	public void sendMessage(final Message msg) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					MyLog.d("--连接");
					ConnectFuture connFuture = connector
							.connect(new InetSocketAddress(TcpClient.this.serverIP, TcpClient.this.port));
					connFuture.awaitUninterruptibly();
					IoSession session = connFuture.getSession();
					MyLog.d("request data is :"+msg.getData());
					session.write(msg.getData());
					isReceiveMessage=false;
				}catch(Exception e){
					MyLog.d("--出错");
					android.os.Message msg = android.os.Message.obtain();
					msg.obj = e;
					msg.what = exceptionCaught;
					mHandler.sendMessage(msg);
				}
			}
		}).start();
	}
}
