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


public class Operate_reportform implements Serializable{
	
	public Operate_reportform() {
		// TODO Auto-generated constructor stub
	}

	public Operate_reportform(String adminName,String channelName,long h5click ,  
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
	
	
    //ID
    private long id;
    
    //渠道id
    private long channelId;
    
    //channel编码
    private String channel;

    //channel编码
    private String rewardType;

    //日期
    private String date;

    //日期
    private String month;
    
    //h5点击
    private long h5Click;

    //h5注册
    private long h5Register;

    //激活
    private long activation;
    //激活转化
    private long activationConversion;

    //注册数
    private long register;

    //外部注册数
    private long outRegister;

    //注册转化
    private long registerConversion;
    //进件数
    private long upload;
    //进件转化
    private int uploadConversion;

    //开户数
    private long account;
    //外开户数
    private long outAccount;

    //开户转化
    private int accountConversion;
    
    //借款数
    private long loan;
    //进件转化
    private int loanConversion;
    
    //借款人数
    private long loaner;
    
    //授信总额
    private long credit;

    //人均额度
    private long perCapitaCredit;

    //首提人数
    private long firstGetPer;

    //首提人数
    private long outFirstGetPer;
    //手提总额
    private long firstGetSum;
    //首提人均批额
    private long firstPerCapitaCredit;

    //续贷人数
    private long secondGetPer;
    //续贷笔数
    private long secondGetPi;
    //续放总额
    private long secondGetSum;
    //续放笔均
    private long secondPerCapitaCredit;

    //渠道提现总额
    private long channelSum;
    //外部渠道提现总额
    private long outChannelSum;
    //渠道笔均
    private long channelCapitaCredit;
    //收入
    private long income;
    //成本
    private long cost;
    //毛利
    private long grossProfit;

    //毛利率
    private long grossProfitRate;
    //渠道商id
    private long proxyId;

    

    @Transient
    //channel名
    private String channelName;
    

    @Transient
    //负责人姓名
    private String adminName;
    

    @Transient
    //渠道类别
    private String channelType;
    

    @Transient
    //负责人姓名
    private String app;
    
    
     
 
	public String getRewardType() {
		return rewardType;
	}

	public void setRewardType(String rewardType) {
		this.rewardType = rewardType;
	}

	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public long getActivationConversion() {
		return activationConversion;
	}

	public void setActivationConversion(long activationConversion) {
		this.activationConversion = activationConversion;
	}

	public long getOutRegister() {
		return outRegister;
	}

	public void setOutRegister(long outRegister) {
		this.outRegister = outRegister;
	}

	public long getRegisterConversion() {
		return registerConversion;
	}

	public void setRegisterConversion(long registerConversion) {
		this.registerConversion = registerConversion;
	}

	public int getUploadConversion() {
		return uploadConversion;
	}

	public void setUploadConversion(int uploadConversion) {
		this.uploadConversion = uploadConversion;
	}

	public long getOutAccount() {
		return outAccount;
	}

	public void setOutAccount(long outAccount) {
		this.outAccount = outAccount;
	}

	public int getAccountConversion() {
		return accountConversion;
	}

	public void setAccountConversion(int accountConversion) {
		this.accountConversion = accountConversion;
	}

	public int getLoanConversion() {
		return loanConversion;
	}

	public void setLoanConversion(int loanConversion) {
		this.loanConversion = loanConversion;
	}

	public long getLoaner() {
		return loaner;
	}

	public void setLoaner(long loaner) {
		this.loaner = loaner;
	}

	public long getOutFirstGetPer() {
		return outFirstGetPer;
	}

	public void setOutFirstGetPer(long outFirstGetPer) {
		this.outFirstGetPer = outFirstGetPer;
	}

	public long getFirstPerCapitaCredit() {
		return firstPerCapitaCredit;
	}

	public void setFirstPerCapitaCredit(long firstPerCapitaCredit) {
		this.firstPerCapitaCredit = firstPerCapitaCredit;
	}

	public long getSecondGetPer() {
		return secondGetPer;
	}

	public void setSecondGetPer(long secondGetPer) {
		this.secondGetPer = secondGetPer;
	}

	public long getSecondGetPi() {
		return secondGetPi;
	}

	public void setSecondGetPi(long secondGetPi) {
		this.secondGetPi = secondGetPi;
	}

	public long getSecondGetSum() {
		return secondGetSum;
	}

	public void setSecondGetSum(long secondGetSum) {
		this.secondGetSum = secondGetSum;
	}

	public long getSecondPerCapitaCredit() {
		return secondPerCapitaCredit;
	}

	public void setSecondPerCapitaCredit(long secondPerCapitaCredit) {
		this.secondPerCapitaCredit = secondPerCapitaCredit;
	}

	public long getOutChannelSum() {
		return outChannelSum;
	}

	public void setOutChannelSum(long outChannelSum) {
		this.outChannelSum = outChannelSum;
	}

	public long getChannelCapitaCredit() {
		return channelCapitaCredit;
	}

	public void setChannelCapitaCredit(long channelCapitaCredit) {
		this.channelCapitaCredit = channelCapitaCredit;
	}

	public long getIncome() {
		return income;
	}

	public void setIncome(long income) {
		this.income = income;
	}

	public long getCost() {
		return cost;
	}

	public void setCost(long cost) {
		this.cost = cost;
	}

	public long getGrossProfit() {
		return grossProfit;
	}

	public void setGrossProfit(long grossProfit) {
		this.grossProfit = grossProfit;
	}

	public long getGrossProfitRate() {
		return grossProfitRate;
	}

	public void setGrossProfitRate(long grossProfitRate) {
		this.grossProfitRate = grossProfitRate;
	}

	public long getProxyId() {
		return proxyId;
	}

	public void setProxyId(long proxyId) {
		this.proxyId = proxyId;
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
