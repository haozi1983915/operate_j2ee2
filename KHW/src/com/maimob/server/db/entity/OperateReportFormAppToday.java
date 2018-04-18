package com.maimob.server.db.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name="operate_reportform_app_today")
@DynamicUpdate(true)
@DynamicInsert(true)
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

    @Column(name = "login")
    private Long login;

    @Column(name = "idcard")
    private Long idCard;

    @Transient
    private Long todayIdCard;

    @Transient
    private Long otherIdCard;

    @Column(name = "debitCard")
    private Long debitCard;

    @Transient
    private Long todayDebitCard;

    @Transient
    private Long otherDebitCard;

    @Column(name = "homeJob")
    private Long homeJob;

    @Transient
    private Long todayHomeJob;

    @Transient
    private Long otherHomeJob;

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

    @Column(name = "upload")
    private Long upload;

    @Transient
    private Long todayUpload;

    @Transient
    private Long otherUpload;

    @Column(name = "unaccount")
    private Long unAccount;

    @Column(name = "account")
    private Long account;

    @Transient
    private Long todayAccount;

    @Transient
    private Long otherAccount;

    @Column(name = "loan")
    private Long loan;

    @Transient
    private Long todayLoan;

    @Transient
    private Long otherLoan;

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
}