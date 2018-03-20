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
@Table(name="operate_balance_account")
@DynamicUpdate(true)
@DynamicInsert(true)
public class BalanceAccount implements Serializable{
	 
  
    @Id @Column(name="id", nullable=false)
    //对账公司ID
    private long id;
    
    @Column(name="company")
    //公司名
    private String company;
    
    @Column(name="taxpayerNo")
    //纳税人识别号
    private String taxpayerNo;
    

	@Column(name="address")
    //公司地址
    private String address;
    
    @Column(name="attributeId")
    //属性
    private long attributeId;
    

	@Column(name="phone")
    //公司电话
    private String phone;

	@Column(name="bank")
    //开户行
    private String bank;

	@Column(name="accountNo")
    //结算账户
    private String accountNo;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getTaxpayerNo() {
		return taxpayerNo;
	}

	public void setTaxpayerNo(String taxpayerNo) {
		this.taxpayerNo = taxpayerNo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public long getAttributeId() {
		return attributeId;
	}

	public void setAttributeId(long attributeId) {
		this.attributeId = attributeId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
 
    
    

    }
