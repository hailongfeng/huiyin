package com.wizarpos.springmvc.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSONObject;
import com.wizarpos.springmvc.bean.Menu;
import com.wizarpos.springmvc.bean.Node;
import com.wizarpos.springmvc.bean.User;
import com.wizarpos.springmvc.service.MenuService;
import com.wizarpos.springmvc.service.Tree;
import com.wizarpos.springmvc.service.UserService;

import junit.framework.TestCase;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-bean.xml","classpath:spring-hibernate.xml"})
public class UserServiceImplTest extends TestCase {
	private static Logger logger = Logger.getLogger(UserServiceImplTest.class);
	@Autowired
	private UserService userService;
	@Autowired
	private MenuService menuService;
	@Test
	public void test() {
		assertNotNull(userService);
	}
	@Test
	public void addUser() {
		User user=new User();
		user.setName("马云");
		user.setAge(45);
		user.setAddress("杭州");
		user.setPhone("18765398876");
		userService.save(user);
		
	}
	@Test
	public void getNode() {
		List<Node> dataList = menuService.getScrollData().getDatas();
		//Tree tree = new Tree(dataList);
		//String menu=tree.buildTree2();
		//System.out.println(menu);
		
		Map<Integer, Menu> map=new HashMap<>();
		for (Node node : dataList) {
			Menu menu=new Menu();
			menu.setId(node.getId());
			menu.setText(node.getName());
			menu.setParentId(node.getParentId());
			map.put(menu.getId(), menu);
		}
		
		List<Menu> roots=new ArrayList<>();
		
		Set<Map.Entry<Integer, Menu>> sets=map.entrySet();
//		Iterator<Map.Entry<Integer, Menu>> iterator=sets.iterator();
		for (Iterator<Map.Entry<Integer, Menu>> it = sets.iterator(); it.hasNext();) {
			Menu m=it.next().getValue();
			if (m.getParentId()==null||m.getParentId()==0) {
				//root.setId(entry.getKey());
				//root.setText(entry.getValue().getText());
				roots.add(m);
			}else {
				map.get(m.getParentId()).addChild(m);
			}
		}
		System.out.println("trees="+JSONObject.toJSONString(roots));
		
	}
	@Test
	public void addNode() {
		Node n1=new Node();
		n1.setName("河南省");
//		n1.setParentId(null);
		n1.setLink("#");
		n1.setIconCss("icon-desktop");
		
		menuService.save(n1);
		
		Node n2=new Node();
		n2.setName("郑州市");
		n2.setParentId(n1.getId());
		n2.setLink("#");
		n2.setIconCss("icon-desktop");
		
		menuService.save(n2);
		
		Node n3=new Node();
		n3.setName("许昌市");
		n3.setParentId(n1.getId());
		n3.setLink("#");
		n3.setIconCss("icon-desktop");
		
		menuService.save(n3);
		
		Node n4=new Node();
		n4.setName("长葛县");
		n4.setParentId(n3.getId());
		n4.setLink("#");
		n4.setIconCss("icon-desktop");
		
		Node n5=new Node();
		n5.setName("禹州县");
		n5.setParentId(n3.getId());
		n5.setLink("#");
		n5.setIconCss("icon-desktop");
		
		
		menuService.save(n4);
		menuService.save(n5);
	}
}
