package com.maimob.server.db.entity;

import com.maimob.server.utils.DecimalFormatUtils;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name="operate_reportform_app_today")
@DynamicUpdate(true)
@DynamicInsert(true)

@ApiModel(value = "埋点整体", description = "埋点整体")
public class OperateReportFormAppToday {
    @Id
    @Column(name="id", nullable=false)
    private Long id;

    @Column(name = "channelId")
    private Long channelId;

    @Column(name = "channel")
    private String channel;

    @Column(name = "date")
    private String date;

    @Column(name = "hour")
    private Integer hour;

    @Column(name = "month")
    private String month;

    @Column(name = "register")
    private Long register;

    @Transient
    private Long todayRegister;

    @Transient
    private Long otherRegister;

    @Transient
    private double registerWholeCoversion = 1.0;

    @Transient
    private double registerStepCoversion = 1.0;

    @Column(name = "login")
    private Long login;

    @Column(name = "idcard")
    private Long idCard;

    @Transient
    private Long todayIdCard;

    @Transient
    private Long otherIdCard;

    @Transient
    private double idCardWholeCoversion;

    @Transient
    private double idCardStepCoversion;

    @Column(name = "debitCard")
    private Long debitCard;

    @Transient
    private Long todayDebitCard;

    @Transient
    private Long otherDebitCard;

    @Transient
    private double debitCardWholeCoversion;

    @Transient
    private double debitCardStepCoversion;

    @Column(name = "homeJob")
    private Long homeJob;

    @Transient
    private Long todayHomeJob;

    @Transient
    private Long otherHomeJob;

    @Transient
    private double homeJobWholeCoversion;

    @Transient
    private double homeJobStepCoversion;

    @Column(name = "contacts")
    private Long contacts;

    @Column(name = "person")
    private Long person;

    @Column(name = "vedio")
    private Long vedio;

    @Transient
    private Long todayVedio;

    @Transient
    private Long otherVedio;

    @Transient
    private double vedioWholeCoversion;

    @Transient
    private double vedioStepCoversion;

    @Column(name = "upload")
    private Long upload;

    @Transient
    private Long todayUpload;

    @Transient
    private Long otherUpload;

    @Transient
    private double uploadWholeCoversion;

    @Transient
    private double uploadStepCoversion;

    @Column(name = "unaccount")
    private Long unAccount;

    @Transient
    private Long todayUnAccount;

    @Transient
    private Long otherUnAccount;

    @Transient
    private double unAccountWholeCoversion;

    @Column(name = "account")
    private Long account;

    @Transient
    private Long todayAccount;

    @Transient
    private Long otherAccount;

    @Transient
    private double accountWholeCoversion;

    @Transient
    private double accountStepCoversion;

    @Column(name = "loan")
    private Long loan;

    @Transient
    private Long todayLoan;

    @Transient
    private Long otherLoan;

    @Transient
    private double loanWholeCoversion;

    @Transient
    private double loanStepCoversion;

    @Column(name = "adminId")
    private Long adminId;

    @Column(name = "channelAttribute")
    private Long channelattribute;

    @Column(name = "channelType")
    private Long channelType;

    @Column(name = "subdivision")
    private Long subDivision;

    @Column(name = "channelName")
    private String channelName;

    @Column(name = "proxyid")
    private Long proxyId;

    @Column(name = "appid")
    private Long appid;

    @Column(name = "app")
    private String app;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
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

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Long getRegister() {
        return register;
    }

    public void setRegister(Long register) {
        this.register = register;
    }

    public Long getLogin() {
        return login;
    }

    public void setLogin(Long login) {
        this.login = login;
    }

    public Long getIdCard() {
        return idCard;
    }

    public void setIdCard(Long idCard) {
        this.idCard = idCard;
    }

    public Long getDebitCard() {
        return debitCard;
    }

    public void setDebitCard(Long debitCard) {
        this.debitCard = debitCard;
    }

    public Long getHomeJob() {
        return homeJob;
    }

    public void setHomeJob(Long homeJob) {
        this.homeJob = homeJob;
    }

    public Long getContacts() {
        return contacts;
    }

    public void setContacts(Long contacts) {
        this.contacts = contacts;
    }

    public Long getPerson() {
        return person;
    }

    public void setPerson(Long person) {
        this.person = person;
    }

    public Long getVedio() {
        return vedio;
    }

    public void setVedio(Long vedio) {
        this.vedio = vedio;
    }

    public Long getUpload() {
        return upload;
    }

    public void setUpload(Long upload) {
        this.upload = upload;
    }

    public Long getUnAccount() {
        return unAccount;
    }

    public void setUnAccount(Long unAccount) {
        this.unAccount = unAccount;
    }

    public Long getAccount() {
        return account;
    }

    public void setAccount(Long account) {
        this.account = account;
    }

    public Long getLoan() {
        return loan;
    }

    public void setLoan(Long loan) {
        this.loan = loan;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public Long getChannelattribute() {
        return channelattribute;
    }

    public void setChannelattribute(Long channelattribute) {
        this.channelattribute = channelattribute;
    }

    public Long getChannelType() {
        return channelType;
    }

    public void setChannelType(Long channelType) {
        this.channelType = channelType;
    }

    public Long getSubDivision() {
        return subDivision;
    }

    public void setSubDivision(Long subDivision) {
        this.subDivision = subDivision;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public Long getProxyId() {
        return proxyId;
    }

    public void setProxyId(Long proxyId) {
        this.proxyId = proxyId;
    }

    public Long getAppid() {
        return appid;
    }

    public void setAppid(Long appid) {
        this.appid = appid;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public Long getTodayRegister() {
        return todayRegister;
    }

    public void setTodayRegister(Long todayRegister) {
        this.todayRegister = todayRegister;
    }

    public Long getOtherRegister() {
        return otherRegister;
    }

    public void setOtherRegister(Long otherRegister) {
        this.otherRegister = otherRegister;
    }

    public Long getTodayIdCard() {
        return todayIdCard;
    }

    public void setTodayIdCard(Long todayIdCard) {
        this.todayIdCard = todayIdCard;
    }

    public Long getOtherIdCard() {
        return otherIdCard;
    }

    public void setOtherIdCard(Long otherIdCard) {
        this.otherIdCard = otherIdCard;
    }

    public Long getTodayDebitCard() {
        return todayDebitCard;
    }

    public void setTodayDebitCard(Long todayDebitCard) {
        this.todayDebitCard = todayDebitCard;
    }

    public Long getOtherDebitCard() {
        return otherDebitCard;
    }

    public void setOtherDebitCard(Long otherDebitCard) {
        this.otherDebitCard = otherDebitCard;
    }

    public Long getTodayHomeJob() {
        return todayHomeJob;
    }

    public void setTodayHomeJob(Long todayHomeJob) {
        this.todayHomeJob = todayHomeJob;
    }

    public Long getOtherHomeJob() {
        return otherHomeJob;
    }

    public void setOtherHomeJob(Long otherHomeJob) {
        this.otherHomeJob = otherHomeJob;
    }

    public Long getTodayVedio() {
        return todayVedio;
    }

    public void setTodayVedio(Long todayVedio) {
        this.todayVedio = todayVedio;
    }

    public Long getOtherVedio() {
        return otherVedio;
    }

    public void setOtherVedio(Long otherVedio) {
        this.otherVedio = otherVedio;
    }

    public Long getTodayUpload() {
        return todayUpload;
    }

    public void setTodayUpload(Long todayUpload) {
        this.todayUpload = todayUpload;
    }

    public Long getOtherUpload() {
        return otherUpload;
    }

    public void setOtherUpload(Long otherUpload) {
        this.otherUpload = otherUpload;
    }

    public Long getTodayLoan() {
        return todayLoan;
    }

    public void setTodayLoan(Long todayLoan) {
        this.todayLoan = todayLoan;
    }

    public Long getOtherLoan() {
        return otherLoan;
    }

    public void setOtherLoan(Long otherLoan) {
        this.otherLoan = otherLoan;
    }

    public Long getTodayAccount() {
        return todayAccount;
    }

    public void setTodayAccount(Long todayAccount) {
        this.todayAccount = todayAccount;
    }

    public Long getOtherAccount() {
        return otherAccount;
    }

    public void setOtherAccount(Long otherAccount) {
        this.otherAccount = otherAccount;
    }

    public double getRegisterWholeCoversion() {
        return registerWholeCoversion;
    }

    public void setRegisterWholeCoversion(double registerWholeCoversion) {
        this.registerWholeCoversion = registerWholeCoversion;
    }

    public double getRegisterStepCoversion() {
        return registerStepCoversion;
    }

    public void setRegisterStepCoversion(double registerStepCoversion) {
        this.registerStepCoversion = registerStepCoversion;
    }

    public double getIdCardWholeCoversion() {
        return DecimalFormatUtils.formatDouble2(todayIdCard.doubleValue()/todayRegister);
    }

    public void setIdCardWholeCoversion(double idCardWholeCoversion) {
        this.idCardWholeCoversion = idCardWholeCoversion;
    }

    public double getIdCardStepCoversion() {
        return DecimalFormatUtils.formatDouble2(todayIdCard.doubleValue()/todayRegister);
    }

    public void setIdCardStepCoversion(double idCardStepCoversion) {
        this.idCardStepCoversion = idCardStepCoversion;
    }

    public double getDebitCardWholeCoversion() {
        return DecimalFormatUtils.formatDouble2(todayDebitCard.doubleValue()/todayRegister);
    }

    public void setDebitCardWholeCoversion(double debitCardWholeCoversion) {
        this.debitCardWholeCoversion = debitCardWholeCoversion;
    }

    public double getDebitCardStepCoversion() {
        return DecimalFormatUtils.formatDouble2(todayDebitCard.doubleValue()/todayIdCard);
    }

    public void setDebitCardStepCoversion(double debitCardStepCoversion) {
        this.debitCardStepCoversion = debitCardStepCoversion;
    }

    public double getHomeJobWholeCoversion() {
        return DecimalFormatUtils.formatDouble2(todayHomeJob.doubleValue()/todayRegister);
    }

    public void setHomeJobWholeCoversion(double homeJobWholeCoversion) {
        this.homeJobWholeCoversion = homeJobWholeCoversion;
    }

    public double getHomeJobStepCoversion() {
        return DecimalFormatUtils.formatDouble2(todayHomeJob.doubleValue()/todayDebitCard);
    }

    public void setHomeJobStepCoversion(double homeJobStepCoversion) {
        this.homeJobStepCoversion = homeJobStepCoversion;
    }

    public double getVedioWholeCoversion() {
        return DecimalFormatUtils.formatDouble2(todayVedio.doubleValue()/todayRegister);
    }

    public void setVedioWholeCoversion(double vedioWholeCoversion) {
        this.vedioWholeCoversion = vedioWholeCoversion;
    }

    public double getVedioStepCoversion() {
        return DecimalFormatUtils.formatDouble2(todayVedio.doubleValue()/todayHomeJob);
    }

    public void setVedioStepCoversion(double vedioStepCoversion) {
        this.vedioStepCoversion = vedioStepCoversion;
    }

    public double getUploadWholeCoversion() {
        return DecimalFormatUtils.formatDouble2(todayUpload.doubleValue()/todayRegister);
    }

    public void setUploadWholeCoversion(double uploadWholeCoversion) {
        this.uploadWholeCoversion = uploadWholeCoversion;
    }

    public double getUploadStepCoversion() {
        return DecimalFormatUtils.formatDouble2(todayUpload.doubleValue()/todayVedio);
    }

    public void setUploadStepCoversion(double uploadStepCoversion) {
        this.uploadStepCoversion = uploadStepCoversion;
    }

    public double getAccountWholeCoversion() {
        return DecimalFormatUtils.formatDouble2(todayAccount.doubleValue()/todayRegister);
    }

    public void setAccountWholeCoversion(double accountWholeCoversion) {
        this.accountWholeCoversion = accountWholeCoversion;
    }

    public double getAccountStepCoversion() {
        return DecimalFormatUtils.formatDouble2(todayAccount.doubleValue()/todayUpload);
    }

    public void setAccountStepCoversion(double accountStepCoversion) {
        this.accountStepCoversion = accountStepCoversion;
    }

    public double getLoanWholeCoversion() {
        return DecimalFormatUtils.formatDouble2(todayLoan.doubleValue()/todayRegister);
    }

    public void setLoanWholeCoversion(double loanWholeCoversion) {
        this.loanWholeCoversion = loanWholeCoversion;
    }

    public double getLoanStepCoversion() {
        return DecimalFormatUtils.formatDouble2(todayLoan.doubleValue()/todayAccount);
    }

    public void setLoanStepCoversion(double loanStepCoversion) {
        this.loanStepCoversion = loanStepCoversion;
    }

    public Long getTodayUnAccount() {
        return todayUnAccount;
    }

    public void setTodayUnAccount(Long todayUnAccount) {
        this.todayUnAccount = todayUnAccount;
    }

    public Long getOtherUnAccount() {
        return otherUnAccount;
    }

    public void setOtherUnAccount(Long otherUnAccount) {
        this.otherUnAccount = otherUnAccount;
    }

    public double getUnAccountWholeCoversion() {
        return DecimalFormatUtils.formatDouble2(todayUnAccount.doubleValue()/todayAccount);
    }

    public void setUnAccountWholeCoversion(double unAccountWholeCoversion) {
        this.unAccountWholeCoversion = unAccountWholeCoversion;
    }
}