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
@Table(name="operate_dictionary")
@DynamicUpdate(true)
@DynamicInsert(true)
public class Dictionary implements Serializable{
	  
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Id @Column(name="id", nullable=false)
    //管理员ID
    private long id;
    
    @Column(name="name")
    //名称
    private String name;
    
    @Column(name="namePY")
    //名称拼音
    private String namePY;
    

	@Column(name="higherId")
    //上级id
    private long higherId;
    
    @Column(name="type")
    //字典类别
    private int type;
    
    @Column(name="other")
    //其他属性
    private String other;
    

    @Column(name="creatAdminId")
    //创建人
    private long creatAdminId;

    @Column(name="updateAdminId")
    //修改人
    private long updateAdminId;

    @Column(name="updateTime")
    //修改时间
    private long updateTime;


    @Transient
    //创建人
    private String creatAdmin;

    @Transient
    //修改人
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

	public String getCreatAdmin() {
		return creatAdmin;
	}

	public void setCreatAdmin(String creatAdmin) {
		this.creatAdmin = creatAdmin;
	}

	public String getUpdateAdmin() {
		return updateAdmin;
	}

	public void setUpdateAdmin(String updateAdmin) {
		this.updateAdmin = updateAdmin;
	}

	public long getCreatAdminId() {
		return creatAdminId;
	}

	public void setCreatAdminId(long creatAdminId) {
		

		this.creatAdminId = creatAdminId;
	}

	public long getUpdateAdminId() {
		return updateAdminId;
	}

	public void setUpdateAdminId(long updateAdminId) {

		
		this.updateAdminId = updateAdminId;
	}
	
	
	public void init(){

		Admin admin = Cache.getAdminCatche(creatAdminId);
		if(admin != null)
		{
			this.creatAdmin = admin.getName();
		}
		
		admin = Cache.getAdminCatche(updateAdminId);
		if(admin != null)
		{
			this.updateAdmin = admin.getName();
		}
	}
	

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public long getId() {

		if(id == 0)
			id = AppTools.getId();
		
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

	public String getNamePY() {
		return namePY;
	}

	public void setNamePY(String namePY) {
		this.namePY = namePY;
	}

	public long getHigherId() {
		return higherId;
	}

	public void setHigherId(long higherId) {
		this.higherId = higherId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}
 

    }
