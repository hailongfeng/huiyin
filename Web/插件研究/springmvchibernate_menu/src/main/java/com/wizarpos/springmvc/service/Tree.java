package com.wizarpos.springmvc.service;

import java.util.ArrayList;
import java.util.List;

import com.wizarpos.springmvc.bean.Node;

public class Tree {
	private StringBuffer html = new StringBuffer();
	private List<Node> nodes;

	public Tree(List<Node> nodes) {
		this.nodes = nodes;
	}

	public String buildTree() {
		html.append("<ul>");
		for (Node node : nodes) {
			Integer id = node.getId();
			if (node.getParentId() == null) {
				html.append("\r\n<li id='" + id + "'>" + node.getName() + "</li>");
				build(node);
			}
		}
		html.append("\r\n</ul>");
		return html.toString();
	}

	private void build(Node node) {
		List<Node> children = getChildren(node);
		if (!children.isEmpty()) {
			html.append("\r\n<ul>");
			for (Node child : children) {
				Integer id = child.getId();
				html.append("\r\n<li id='" + id + "'>" + child.getName() + "</li>");
				build(child);
			}
			html.append("\r\n</ul>");
		}
	}

	//生成ootstrap 类型的菜单 
	public String buildTree2() {
		StringBuilder sb = new StringBuilder("<ul class=\"nav nav-list\">");
		html.append("<ul class=\"nav nav-list\">");
		for (Node node : nodes) {
			if (node.getParentId()==0) {
				showAll2(node);
			}
		}
		sb.append("</ul>");
		return html.toString();
	}

	public void showAll2(Node root) {
		String text = root.getName();
		String css=root.getIconCss();
		StringBuilder li1 = new StringBuilder("<li><a href=\"javascript:displaypage('"+root.getLink()+"');\"><i class=\""+css+"\"></i><span class=\"menu-text\">" + text+ "</span></a></li>");// 没有children的初始li结构
		StringBuilder li2 = new StringBuilder("<li><a href=\"#\" class=\"dropdown-toggle\"><i class=\"icon-desktop\"></i><span class=\"menu-text\">"+ text + "</span><b class=\"arrow icon-angle-down\"></b></a>");// 有children的初始li结构
		List<Node> childrens = getChildren(root);
		if (childrens.size() > 0) {
			html.append(li2);
			String ul = "<ul class=\"submenu\">";
			html.append(ul);
			for (Node node : childrens) {
				showAll2(node);
			}
			html.append("</ul>");
			html.append("</li>");
		} else {
			html.append(li1);
		}
	}

	public void showAll2(Node root, StringBuilder sb) {
		String text = root.getName();
		StringBuilder li1 = new StringBuilder(
				"<li><a href=\"\"><i class=\"icon-dashboard\"></i><span class=\"menu-text\">" + text
						+ "</span></a></li>");// 没有children的初始li结构
		StringBuilder li2 = new StringBuilder(
				"<li><a href=\"#\" class=\"dropdown-toggle\"><i class=\"icon-desktop\"></i><span class=\"menu-text\">"
						+ text + "</span><b class=\"arrow icon-angle-down\"></b></a>");// 有children的初始li结构
		List<Node> childrens = getChildren(root);
		if (childrens.size() > 0) {
			String ul = "<ul class=\"submenu\">";
			li2.append(ul);
			for (Node node : childrens) {
				showAll2(node, li2);
			}
			li2.append("</ul>");
			li2.append("</li>");
			sb.append(li2);
		} else {
			sb.append(li1);
		}
	}

	private List<Node> getChildren(Node node) {
		List<Node> children = new ArrayList<Node>();
		Integer id = node.getId();
		for (Node child : nodes) {
			if (id.equals(child.getParentId())) {
				children.add(child);
			}
		}
		return children;
	}
}
