package cn.jeeweb.modules.sys.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import cn.jeeweb.core.common.entity.AbstractEntity;
import cn.jeeweb.modules.sys.security.shiro.session.mgt.OnlineSession;
import javax.persistence.*;
import java.util.Date;

/**
 * 
 * All rights Reserved, Designed By www.jeeweb.cn
 * 
 * @title: UserOnline.java
 * @package cn.jeeweb.modules.sys.entity
 * @description: 当前在线会话
 * @author: 王存见
 * @date: 2017年7月3日 下午3:59:32
 * @version V1.0
 * @copyright: 2017 www.jeeweb.cn Inc. All rights reserved.
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "sys_user_online")
public class UserOnline extends AbstractEntity<String> {

	/**
	 * 用户会话id ===> uid
	 */
	@Id
	@GeneratedValue(generator = "assigned")
	@GenericGenerator(name = "assigned", strategy = "assigned")
	private String id;

	// 当前登录的用户Id
	@Column(name = "user_id")
	private String userId;

	@Column(name = "username")
	private String username;

	/**
	 * 用户主机地址
	 */
	@Column(name = "host")
	private String host;

	/**
	 * 用户登录时系统IP
	 */
	@Column(name = "system_host")
	private String systemHost;

	/**
	 * 用户浏览器类型
	 */
	@Column(name = "user_agent")
	private String userAgent;

	/**
	 * 在线状态
	 */
	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private OnlineSession.OnlineStatus status = OnlineSession.OnlineStatus.on_line;

	/**
	 * session创建时间
	 */
	@Column(name = "start_timestsamp")
	private Date startTimestamp;
	/**
	 * session最后访问时间
	 */
	@Column(name = "last_access_time")
	private Date lastAccessTime;

	/**
	 * 超时时间
	 */
	@Column(name = "online_timeout")
	private Long timeout;

	/**
	 * 备份的当前用户会话
	 */
	@Column(name = "online_session")
	@Type(type = "cn.jeeweb.core.repository.hibernate.type.ObjectSerializeUserType")
	private OnlineSession session;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getStartTimestamp() {
		return startTimestamp;
	}

	public void setStartTimestamp(Date startTimestamp) {
		this.startTimestamp = startTimestamp;
	}

	public Date getLastAccessTime() {
		return lastAccessTime;
	}

	public void setLastAccessTime(Date lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}

	public Long getTimeout() {
		return timeout;
	}

	public void setTimeout(Long timeout) {
		this.timeout = timeout;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public OnlineSession.OnlineStatus getStatus() {
		return status;
	}

	public void setStatus(OnlineSession.OnlineStatus status) {
		this.status = status;
	}

	public OnlineSession getSession() {
		return session;
	}

	public void setSession(OnlineSession session) {
		this.session = session;
	}

	public String getSystemHost() {
		return systemHost;
	}

	public void setSystemHost(String systemHost) {
		this.systemHost = systemHost;
	}

	public static final UserOnline fromOnlineSession(OnlineSession session) {
		UserOnline online = new UserOnline();
		online.setId(String.valueOf(session.getId()));
		online.setUserId(session.getUserId());
		online.setUsername(session.getUsername());
		online.setStartTimestamp(session.getStartTimestamp());
		online.setLastAccessTime(session.getLastAccessTime());
		online.setTimeout(session.getTimeout());
		online.setHost(session.getHost());
		online.setUserAgent(session.getUserAgent());
		online.setSystemHost(session.getSystemHost());
		online.setSession(session);

		return online;
	}

}
