package com.wizarpos.springmvc.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.wizarpos.springmvc.bean.Menu;
import com.wizarpos.springmvc.bean.Node;
import com.wizarpos.springmvc.bean.User;
import com.wizarpos.springmvc.service.MenuService;
import com.wizarpos.springmvc.service.Tree;
import com.wizarpos.springmvc.service.UserService;
import com.wizarpos.springmvc.util.QueryResult;

/**
 * Created by Administrator on 2017/2/11.
 */
@Controller
public class MainController {
	private User user;
	@Autowired
	private UserService userService;
	@Autowired
	private MenuService menuService;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Model model) {
		user = userService.findById(1L);
		
		model.addAttribute(user);
		return "index";
	}

	@RequestMapping(value = "/menu", method = RequestMethod.GET)
	public String menu(Model model) {
		// 读取层次数据结果集列表
		QueryResult<Node> result= menuService.getScrollData();
		List<Node> dataList=result.getDatas();
		Tree tree = new Tree(dataList);
		String menu=tree.buildTree2();
		model.addAttribute("menu", menu);
		return "tree";
	}
	@RequestMapping(value = "/menu/add", method = RequestMethod.GET)
	public String addUI(Model model) {
		System.out.println("=========addUI");
		// 读取层次数据结果集列表
		String menu=getNode();
		model.addAttribute("menu", menu);
		return "add";
	}
	@RequestMapping(value = "/menu/add", method = RequestMethod.POST)
	public String add(@RequestParam String name,@RequestParam(defaultValue="0") Integer pid,Model model) {
		// 读取层次数据结果集列表
		System.out.println("=========name="+name+",pid="+pid);
		Node newNode=new Node();
		newNode.setName(name);
		newNode.setParentId(pid);
		newNode.setIconCss("icon-desktop");
		newNode.setLink("#");
		menuService.save(newNode);
		
		String menu=getNode();
		model.addAttribute("menu", menu);
		model.addAttribute("result", "success");
		return "add";
	}
	
	
	public String getNode() {
		List<Node> dataList = menuService.getScrollData().getDatas();
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
		for (Iterator<Map.Entry<Integer, Menu>> it = sets.iterator(); it.hasNext();) {
			Menu m=it.next().getValue();
			if (m.getParentId()==null||m.getParentId()==0) {
				roots.add(m);
			}else {
				map.get(m.getParentId()).addChild(m);
			}
		}
		return JSONObject.toJSONString(roots);
		
	}
	
}
