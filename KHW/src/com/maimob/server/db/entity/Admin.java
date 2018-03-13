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
@Table(name="operate_admin")
@DynamicUpdate(true)
@DynamicInsert(true)
public class Admin implements Serializable{
	
	public Admin(long id,String name) {
		this.id = id;
		this.name = name;
	}
	public Admin(long id,String name,int level) {
		this.id = id;
		this.name = name;
		this.level = level;
	}
	

	public Admin(String id,String name,String pwd,String email,String departmentId,String level,String date,String powerId,String higherId,String state) {
		this.id = Long.parseLong(id);
		this.name = name;
		this.pwd = pwd;
		this.email = email;
		this.departmentId = Long.parseLong(departmentId);
		this.level = Integer.parseInt(level);
		this.date = Long.parseLong(date);
		this.powerId = Long.parseLong(powerId);
		this.higherId = Long.parseLong(higherId);
		this.state = Long.parseLong(state);
	}
	
	

	public Admin() {
    	this.setDate(System.currentTimeMillis());
	}
	
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Id @Column(name="id", nullable=false)
    //管理员ID
    private long id;
    
    @Column(name="name")
    //姓名
    private String name;
    
    @Column(name="pwd")
    //密码
    private String pwd;
    

	@Column(name="email")
    //提现密码
    private String email;
    
    @Column(name="departmentId")
    //部门id
    private long departmentId;
    
    @Column(name="level")
    //管理员级别
    private int level;
    
    @Column(name="date")
    //账号创建时间
    private long date;
    
    @Column(name="powerId")
    //操作权限
    private long powerId;
    
    @Column(name="higherId")
    //上级管理员id
    private long higherId;
    

    @Column(name="state")
    //账号状态
    private long state;
    
    @Column(name="flag")
    //修改密码标识
    private int flag;
    

    public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}

	//登录时间
    @Transient
    private long loginDate;
    
    
     public static void main(String[] args) {
    	 Admin a = new Admin();
    	 
    	 System.out.println(a.date);
    	 
    	 
    	 
	}
    
    public String check()
    {
    	if(this.name == null || "".equals(this.name))
    	{
    		return "名字不能为空";
    	}
    	else if(this.pwd == null || "".equals(this.pwd))
    	{
    		return "密码不能为空";
    	}
    	else if(this.email == null || "".equals(this.email))
    	{
    		return "email不能为空";
    	}
    	else if(this.departmentId == 0)
    	{
    		return "部门不能为空";
    	}
    	else if(this.level == 0)
    	{
    		return "账号级别不能为空";
    	}
    	else if(this.higherId == 0 && this.level > 1)
    	{
    		return "上级不能为空";
    	}
    	return "";
    }
    
    
    public long getState() {
		return state;
	}

	public void setState(long state) {
		this.state = state;
	}

	public long getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(long loginDate) {
		this.loginDate = loginDate;
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
		this.name = name.trim();
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	public int getLevel() {
		return level;
	}



	public void setLevel(int level) {
		this.level = level;
	}



	public long getDate() {
		return date;
	}



	public void setDate(long date) {
		this.date = date;
	}

	public long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(long departmentId) {
		this.departmentId = departmentId;
	}

	public long getPowerId() {
		return powerId;
	}

	public void setPowerId(long powerId) {
		this.powerId = powerId;
	}

	public long getHigherId() {
		return higherId;
	}

	public void setHigherId(long higherId) {
		this.higherId = higherId;
	}


    }
