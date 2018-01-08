package com.wizarpos.springmvc.dao.impl;

import org.springframework.stereotype.Repository;

import com.wizarpos.springmvc.bean.Node;
import com.wizarpos.springmvc.dao.MenuDao;

/**
 * Created by Administrator on 2016/5/6.
 */
@Repository
public class MenuDaoImpl extends BaseDaoImpl<Integer,Node> implements MenuDao {

	public MenuDaoImpl() {
		super(Node.class);
	}
    
}