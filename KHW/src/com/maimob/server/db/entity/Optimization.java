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
@Table(name="operate_optimization")
@DynamicUpdate(true)
@DynamicInsert(true)
public class Optimization implements Serializable{


    /**
     * 
     */

	@Id @Column(name="id", nullable=false)
    //ID
    private long id;

	@Id @Column(name="optimization", nullable=false)
    //优化比例
    private int optimization;
    
    @Column(name="startDate")
    //起始时间
    private long startDate;
    
    @Column(name="adminId")
    //修改人id
    private long adminId;
    
    @Column(name="channelId")
    //渠道号
    private long channelId;

    @Column(name="updateDate")
    //修改时间
    private long updateDate;
     

	public String check(long id ,long channelId)
    {
		this.id = id;
		this.channelId = channelId;
    	if(this.optimization == 0)
    	{
    		return "优化比例不能为空";
    	}
    	else if(this.startDate == 0)
    	{
    		return "其实时间不能为空";
    	}
    	else if(this.adminId == 0)
    	{
    		return "修改人不能为空";
    	}
    	else if(this.channelId == 0)
    	{
    		return "渠道号不能为空";
    	}
    	else if(this.updateDate == 0)
    	{
    		this.updateDate = System.currentTimeMillis();
    	}
    	return "";
    }


	public long getId() {
		if(id == 0)
			id = AppTools.getId();
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public int getOptimization() {
		return optimization;
	}


	public void setOptimization(int optimization) {
		this.optimization = optimization;
	}


	public long getStartDate() {
		return startDate;
	}


	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}


	public long getAdminId() {
		return adminId;
	}


	public void setAdminId(long adminId) {
		this.adminId = adminId;
	}


	public long getChannelId() {
		return channelId;
	}


	public void setChannelId(long channelId) {
		this.channelId = channelId;
	}


	public long getUpdateDate() {
		return updateDate;
	}


	public void setUpdateDate(long updateDate) {
		this.updateDate = updateDate;
	}
    
	
}
