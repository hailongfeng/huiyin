package com.wizarpos.springmvc.bean;

import java.util.ArrayList;
import java.util.List;

public class Menu {
	private Integer id;
	private Integer parentId;
	private String text;
	private String link;
	private String iconCss;
	private List<Menu> nodes;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getIconCss() {
		return iconCss;
	}
	public void setIconCss(String iconCss) {
		this.iconCss = iconCss;
	}
	public List<Menu> getNodes() {
		return nodes;
	}
	public void setNodes(List<Menu> nodes) {
		this.nodes = nodes;
	}
	
	public void addChild(Menu child) {
		if (nodes==null) {
			nodes=new ArrayList<>();
		}
		this.nodes.add(child);
	}
	
	
	
	
}
