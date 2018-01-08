package com.wizarpos.springmvc.service.impl;

import org.springframework.stereotype.Service;

import com.wizarpos.springmvc.bean.User;
import com.wizarpos.springmvc.service.UserService;
/**
 * Created by Administrator on 2017/2/10.
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<Long, User> implements UserService{
}
