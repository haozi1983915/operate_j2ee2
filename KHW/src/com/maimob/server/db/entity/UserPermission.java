package com.maimob.server.db.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
 
public class UserPermission implements Serializable{
	  
    //权限名称 唯一
    private String meta;
    private String meta_name;
    private String show;
    private List<UserPermission> children;
    
    public UserPermission(Map<String, String> ages) {
    		meta = ages.get("meta");
    		meta_name = ages.get("name");
    		show = ages.get("show");
	}
    
    
    public void addPermission(UserPermission up)
    {
    		if(children == null)
    			children = new ArrayList<UserPermission>();
    		this.children.add(up);
    }
    
	public String getMeta() {
		return meta;
	}
	public void setMeta(String meta) {
		this.meta = meta;
	}
	public String getMeta_name() {
		return meta_name;
	}
	public void setMeta_name(String meta_name) {
		this.meta_name = meta_name;
	}
	public String getShow() {
		return show;
	}
	public void setShow(String show) {
		this.show = show;
	}
	public List<UserPermission> getChildren() {
		return children;
	}
	public void setChildren(List<UserPermission> children) {
		this.children = children;
	}

    
    }
