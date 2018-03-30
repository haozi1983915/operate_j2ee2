package com.maimob.server.db.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name="operate_costing")
@DynamicUpdate(true)
@DynamicInsert(true)
public class OperateCost implements Serializable{
	
	private static final long serialVersionUID = 1L;

    @Id @Column(name="id", nullable=false)
    //序号id
    private long id;
    
    @Column(name="dateNew")
    //日期
    private String dateNew;
    
    @Column(name="appId")
    //产品id
    private int appId;
    

	@Column(name="companyId")
    //合作方公司名称
    private int companyId;
    
    @Column(name="cooperationContext")
    //合作内容
    private String cooperationContext;
    
    @Column(name="cost")
    //其他属性
    private float cost;
    
    @Column(name="remark")
    //备注
    private String remark;

	

	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDateNew() {
		return dateNew;
	}

	public void setDateNew(String dateNew) {
		this.dateNew = dateNew;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public String getCooperationContext() {
		return cooperationContext;
	}

	public void setCooperationContext(String cooperationContext) {
		this.cooperationContext = cooperationContext;
	}

	public float getCost() {
		return cost;
	}

	public void setCost(float cost) {
		this.cost = cost;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public OperateCost() {
	}

	public OperateCost(String dateNew, int appId, int companyId, String cooperationContext, float cost, String remark) {
		this.dateNew = dateNew;
		this.appId = appId;
		this.companyId = companyId;
		this.cooperationContext = cooperationContext;
		this.cost = cost;
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "OperateCost [id=" + id + ", dateNew=" + dateNew + ", appId=" + appId + ", companyId=" + companyId
				+ ", cooperationContext=" + cooperationContext + ", cost=" + cost + ", remark=" + remark + "]";
	}
	
	
    
    

}
