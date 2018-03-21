package com.maimob.server.db.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.maimob.server.utils.Cache;

@Entity
@Table(name="operate_pay_company")
@DynamicUpdate(true)
@DynamicInsert(true)
public class operate_pay_company implements Serializable{
	 
  
    @Id @Column(name="id", nullable=false)
    //对账公司ID
    private long id;
    
    @Column(name="appid")
    //appid
    private long appid;
    
    @Column(name="companyId")
    //companyId
    private long companyId;

    @Column(name="proxyid")
    //proxyid
    private long proxyid;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getAppid() {
		return appid;
	}

	public void setAppid(long appid) {
		this.appid = appid;
	}

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	public long getProxyid() {
		return proxyid;
	}

	public void setProxyid(long proxyid) {
		this.proxyid = proxyid;
	}
    
 
    

    }
