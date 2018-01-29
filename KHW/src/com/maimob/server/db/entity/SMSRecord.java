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
@Table(name="smsrecord")
@DynamicUpdate(true)
@DynamicInsert(true)
public class SMSRecord implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public int getRspcode() {
        return Rspcode;
    }

    public void setRspcode(int rspcode) {
        Rspcode = rspcode;
    }

    public String getMsg_Id() {
        return Msg_Id;
    }

    public void setMsg_Id(String msg_Id) {
        Msg_Id = msg_Id;
    }

    public String getExtInfo() {
        return ExtInfo;
    }

    public void setExtInfo(String extInfo) {
        ExtInfo = extInfo;
    }

    public int getFee() {
        return Fee;
    }

    public void setFee(int fee) {
        Fee = fee;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    
    

    @Id @Column(name="id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    //smsm主键的标识属性
    private Integer id;
    
    @Column(name="mobileNo")
    //mobileNo 
    private String mobileNo;
    
    @Column(name="Rspcode")
    //短信发送返回值 
    private int Rspcode;
    
    @Column(name="content")
    //短信发送内容
    private String content;
    
    @Column(name="Msg_Id")
    //信息标识 
    private String Msg_Id;
    
    @Column(name="ExtInfo")
    //扩展信息 
    private String ExtInfo;
    
    @Column(name="Fee")
    //计费数 
    private int Fee;
    
    @Column(name="sendTime")
    //发送时间
    private String sendTime;
}
