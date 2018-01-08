package com.wizarpos.springmvc.dao.impl;

import org.springframework.stereotype.Repository;

import com.wizarpos.springmvc.bean.User;
import com.wizarpos.springmvc.dao.UserDao;

/**
 * Created by Administrator on 2016/5/6.
 */
@Repository
public class UserDaoImpl extends BaseDaoImpl<Long, User> implements UserDao {

	public UserDaoImpl() {
		super(User.class);
	}
    
}