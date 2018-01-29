package com.maimob.server.db.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;


@Entity
@Table(name="dynamicpwd")
@DynamicUpdate(true)
@DynamicInsert(true)
public class DynamicPwd implements Serializable {
	
    private static final long serialVersionUID = 1L;

    @Id @Column(name="mobileNo")
    //User主键的标识属性
    private String mobileNo;
    
    @Column(name="dynamicPwd")
    //中银消费金融账号
    private String dynamicPwd;
    

    @Column(name="sendDate")
    //中银消费金融账号
    private long sendDate;
    

    @Column(name="sendTimeDay")
    //中银消费金融账号
    private int sendTimeDay;


	public String getMobileNo() {
		return mobileNo;
	}


	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}


	public String getDynamicPwd() {
		return dynamicPwd;
	}


	public void setDynamicPwd(String dynamicPwd) {
		this.dynamicPwd = dynamicPwd;
	}


	public long getSendDate() {
		return sendDate;
	}


	public void setSendDate(long sendDate) {
		this.sendDate = sendDate;
	}


	public int getSendTimeDay() {
		return sendTimeDay;
	}


	public void setSendTimeDay(int sendTimeDay) {
		this.sendTimeDay = sendTimeDay;
	}


	public void addSendTimeDay() {
		this.sendTimeDay++;
	}

	

}
