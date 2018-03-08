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

@Entity
@Table(name="operate_permission")
@DynamicUpdate(true)
@DynamicInsert(true)
public class AdminPermission implements Serializable{
	

    @Id @Column(name="id", nullable=false)
    //权限ID
    private long id;
    
    @Column(name="name")
    //权限名称 唯一
    private String name;

    @Column(name="adminId")
    //创建人
    private long adminId;
    
    @Column(name="type")
    //权限类别
    private long type;

    @Column(name="show")
    //是否显示
    private int show;

    @Column(name="updateAdminId")
    //创建人
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

	public long getAdminId() {
		return adminId;
	}

	public void setAdminId(long adminId) {
		this.adminId = adminId;
	}

	public int getShow() {
		return show;
	}

	public void setShow(int show) {
		this.show = show;
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
