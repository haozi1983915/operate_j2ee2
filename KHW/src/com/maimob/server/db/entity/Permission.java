package com.maimob.server.db.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.maimob.server.utils.AppTools;
import com.maimob.server.utils.Cache;

@Entity
@Table(name="operate_permission")
@DynamicUpdate(true)
@DynamicInsert(true)
public class Permission implements Serializable{
	

    @Id @Column(name="id", nullable=false)
    //权限ID
    private long id;
    
    @Column(name="name")
    //权限名称 唯一
    private String name;
    
    @Column(name="type")
    //权限类别
    private long type;

    @Column(name="allshow")
    //是否显示
    private int allshow;

    @Column(name="isuse")
    //是否启用
    private int isuse;

    @Column(name="createAdminId")
    //创建人
    private long createAdminId;
    

    @Column(name="meta")
    //创建人
    private String meta;
    
    @Column(name="updateAdminId")
    //修改人
    private long updateAdminId;
    
    @Column(name="updateTime")
    //创建时间
    private long updateTime;

    @Column(name="opType")
    //哪个系统
    private int opType;
    
    @Transient
    //创建人
    private String updateAdmin;


	//修改人名称
    @Transient
    private String adminName;

	public String getAdminName() {
    	
	    	if(adminName == null)
	    	{
	    		if(Cache.getAdminCatche(updateAdminId) != null)
	    			this.adminName = Cache.getAdminCatche(updateAdminId).getName();
	    		else
	    			this.adminName = "未知";
	    	}
    	
		return adminName;
	}
    
	public String getMeta() {
		return meta;
	}

	public void setMeta(String meta) {
		this.meta = meta;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public long getCreateAdminId() {
		return createAdminId;
	}

	public void setCreateAdminId(long createAdminId) {
		this.createAdminId = createAdminId;
	}

	public int getOpType() {
		return opType;
	}

	public void setOpType(int opType) {
		this.opType = opType;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getType() {
		return type;
	}

	public void setType(long type) {
		this.type = type;
	}

	public int getAllshow() {
		return allshow;
	}

	public void setAllshow(int allshow) {
		this.allshow = allshow;
	}

	public int getIsuse() {
		return isuse;
	}

	public void setIsuse(int isuse) {
		this.isuse = isuse;
	}

	public long getUpdateAdminId() {
		return updateAdminId;
	}

	public void setUpdateAdminId(long updateAdminId) {
		this.updateAdminId = updateAdminId;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateAdmin() {
		return updateAdmin;
	}

	public void setUpdateAdmin(String updateAdmin) {
		this.updateAdmin = updateAdmin;
	}
    
    
    }
