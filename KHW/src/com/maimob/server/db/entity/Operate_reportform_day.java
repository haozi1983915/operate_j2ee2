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
@Table(name="operate_reportform_day")
@DynamicUpdate(true)
@DynamicInsert(true)
public class Operate_reportform_day implements Serializable{
	
	public Operate_reportform_day() {
		// TODO Auto-generated constructor stub
	}
	

	public Operate_reportform_day(Operate_reportform or) {
		this.id = or.getId();
	    this.channelId = or.getChannelId();
	    
	    this.channel = or.getChannel();
	    this.date = or.getDate();
	    
	    this.h5Click = or.getH5Click();

	    this.h5Register = or.getH5Register();

	    this.activation = or.getActivation();

	    this.register = or.getRegister();

	    this.upload = or.getUpload();

	    this.account = or.getAccount();

	    this.loan = or.getLoan();

	    this.credit = or.getCredit();

	    this.perCapitaCredit = or.getPerCapitaCredit();

	    this.firstGetPer = or.getFirstGetPer();

	    this.firstGetSum = or.getFirstGetSum();
	    
	    this.channelSum = or.getChannelSum();
	    this.channelName = or.getChannelName();
	    this.adminName = or.getAdminName();
	    this.channelType = or.getChannelType();
	    
		
	}
	
	

	public Operate_reportform_day(String adminName,String channelName,long h5click ,  
			long h5register ,     
			long activation ,    
			long register ,   
			long upload , 
			long account , 
			long loan , 
			long credit , 
			long perCapitaCredit , 
			long firstGetPer , 
			long firstGetSum , 
			long channelSum ) {
		this.h5Click = h5click;
		this.h5Register = h5register;    
		this.activation = activation;
		this.register = register;  
		this.upload = upload;
		this.account = account;
		this.loan = loan;
		this.credit = credit;
		this.perCapitaCredit = perCapitaCredit;
		this.firstGetPer = firstGetPer;
		this.firstGetSum = firstGetSum;
		this.channelSum = channelSum;
		this.adminName = adminName;
		this.channelName = channelName;
		
	}
	

//	public Operate_reportform_day(String adminName,String channelName,long h5click , long h5register  ) {
//		this.h5Click = h5click;
//		this.h5Register = h5register;
//		this.adminName = adminName;
//		this.channelName = channelName;
//		
//	}
	
	
	
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Id @Column(name="id", nullable=false)
    //ID
    private long id;
    
    @Column(name="channelId")
    //渠道id
    private long channelId;
    
    @Column(name="channel")
    //channel编码
    private String channel;
    

	@Column(name="date")
    //日期
    private String date;
    
    @Column(name="h5Click")
    //h5点击
    private long h5Click;

    @Column(name="h5Register")
    //h5注册
    private long h5Register;

    @Column(name="activation")
    //激活
    private long activation;

    @Column(name="register")
    //注册数
    private long register;

    @Column(name="upload")
    //进件数
    private long upload;

    @Column(name="account")
    //开户数
    private long account;

    @Column(name="loan")
    //借款数
    private long loan;

    @Column(name="credit")
    //授信总额
    private long credit;

    @Column(name="perCapitaCredit")
    //人均额度
    private long perCapitaCredit;

    @Column(name="firstGetPer")
    //首提人数
    private long firstGetPer;

    @Column(name="firstGetSum")
    //手提总额
    private long firstGetSum;
    
    @Column(name="channelSum")
    //渠道提现总额
    private long channelSum;

    

    @Transient
    //channel名
    private String channelName;
    

    @Transient
    //负责人姓名
    private String adminName;
    

    @Transient
    //进件转化
    private int uploadConversion;

    
    
    
    @Transient
    //开户转化
    private int accountConversion;

    @Transient
    //提现转化
    private int loanConversion;
    
    
    @Transient
    //渠道类别
    private String channelType;
    
     



	public int getUploadConversion() {
		return uploadConversion;
	}


	public void setUploadConversion(int uploadConversion) {
		this.uploadConversion = uploadConversion;
	}

	public void setUploadConversion(String uploadConversion) {
		try {
			this.uploadConversion = Integer.parseInt(uploadConversion);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}


	public int getAccountConversion() {
		return accountConversion;
	}


	public void setAccountConversion(int accountConversion) {
		this.accountConversion = accountConversion;
	}

	public void setAccountConversion(String accountConversion) {
		try {
			this.accountConversion = Integer.parseInt(accountConversion);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}


	public int getLoanConversion() {
		return loanConversion;
	}


	public void setLoanConversion(int loanConversion) {
		this.loanConversion = loanConversion;
	}


	public void setLoanConversion(String loanConversion) {
		try {
			this.loanConversion = Integer.parseInt(loanConversion);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}


	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public String getChannelType() {
		return channelType;
	}

	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getChannelId() {
		return channelId;
	}

	public void setChannelId(long channelId) {
		this.channelId = channelId;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public long getH5Click() {
		return h5Click;
	}

	public void setH5Click(long h5Click) {
		this.h5Click = h5Click;
	}

	public long getH5Register() {
		return h5Register;
	}

	public void setH5Register(long h5Register) {
		this.h5Register = h5Register;
	}

	public long getActivation() {
		return activation;
	}

	public void setActivation(long activation) {
		this.activation = activation;
	}

	public long getRegister() {
		return register;
	}

	public void setRegister(long register) {
		this.register = register;
	}

	public long getUpload() {
		return upload;
	}

	public void setUpload(long upload) {
		this.upload = upload;
	}

	public long getAccount() {
		return account;
	}

	public void setAccount(long account) {
		this.account = account;
	}

	public long getLoan() {
		return loan;
	}

	public void setLoan(long loan) {
		this.loan = loan;
	}

	public long getCredit() {
		return credit;
	}

	public void setCredit(long credit) {
		this.credit = credit;
	}

	public long getPerCapitaCredit() {
		return perCapitaCredit;
	}

	public void setPerCapitaCredit(long perCapitaCredit) {
		this.perCapitaCredit = perCapitaCredit;
	}

	public long getFirstGetPer() {
		return firstGetPer;
	}

	public void setFirstGetPer(long firstGetPer) {
		this.firstGetPer = firstGetPer;
	}

	public long getFirstGetSum() {
		return firstGetSum;
	}

	public void setFirstGetSum(long firstGetSum) {
		this.firstGetSum = firstGetSum;
	}

	public long getChannelSum() {
		return channelSum;
	}

	public void setChannelSum(long channelSum) {
		this.channelSum = channelSum;
	}

      
 
    
    
    
    

    }
