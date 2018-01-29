package com.maimob.server.db.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name="operate_reward")
@DynamicUpdate(true)
@DynamicInsert(true)
public class Reward implements Serializable{


    /**
     * 
     */

	@Id @Column(name="id", nullable=false)
    //ID
    private long id;

	@Id @Column(name="rewardOrder", nullable=false)
    //方案排序
    private int order;
    
    @Column(name="typeId")
    //分成方式id
    private long typeId;
    
    @Column(name="max")
    //数量阈值，超过此数量用下一个阶梯统计分成
    private int max;
    
    @Column(name="price")
    //单价  百分比或元
    private int price;
    
    @Column(name="adminId")
    //负责商务id
    private long adminId;
    
    @Column(name="updateAdminId")
    //修改人id
    private long updateAdminId;
    
    @Column(name="date")
    //创建时间
    private long date;

    @Column(name="costing")
    //成本计算方式
    private long costing;

    @Column(name="settlementCycle")
    //结算周期
    private long settlementCycle;

    @Column(name="channelId")
    //成本计算方式
    private long channelId;
    
    @Transient
    private String rewardPrice;

	public String getRewardPrice() {
		return rewardPrice;
	}

	public void setRewardPrice(String rewardPrice) {
		this.rewardPrice = rewardPrice;
	}

	public long getChannelId() {
		return channelId;
	}

	public void setChannelId(long channelId) {
		this.channelId = channelId;
	}

	public long getTypeId() {
		return typeId;
	}

	public void setTypeId(long typeId) {
		this.typeId = typeId;
	}

	public long getCosting() {
		return costing;
	}

	public void setCosting(long costing) {
		this.costing = costing;
	}

	public long getSettlementCycle() {
		return settlementCycle;
	}

	public void setSettlementCycle(long settlementCycle) {
		this.settlementCycle = settlementCycle;
	}

	public String check(long id ,long channelId)
    {
    		this.id = id;
    		this.channelId = channelId;
    	if(this.order == 0)
    	{
    		return "方案排序不能为空";
    	}
    	else if(this.typeId == 0)
    	{
    		return "分成方式不能为空";
    	}
    	else if(this.price == 0)
    	{
    		return "单价不能为空";
    	}
    	else if(this.adminId == 0)
    	{
    		return "负责人不能为空";
    	}
    	else if(this.updateAdminId == 0)
    	{
    		return "修改人不能为空";
    	}
    	else if(this.date == 0)
    	{
    		this.date = System.currentTimeMillis();
    	}
    	return "";
    }
    
	
	
    private static final long serialVersionUID = 1L;

    public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public long getAdminId() {
		return adminId;
	}

	public void setAdminId(long adminId) {
		this.adminId = adminId;
	}

	public long getUpdateAdminId() {
		return updateAdminId;
	}

	public void setUpdateAdminId(long updateAdminId) {
		this.updateAdminId = updateAdminId;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}


}
