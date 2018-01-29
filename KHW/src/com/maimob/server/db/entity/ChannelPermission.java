package com.maimob.server.db.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.maimob.server.utils.AppTools;

@Entity
@Table(name="operate_channel_permission")
@DynamicUpdate(true)
@DynamicInsert(true)
public class ChannelPermission implements Serializable{


    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Id @Column(name="id", nullable=false)
    //ID
    private long id;
    
    @Column(name="registerChartPermission")
    //查看注册数量权限
    private int registerChartPermission;

    @Column(name="loginChartPermission")
    //查看登录数量权限
    private int loginChartPermission;

    @Column(name="applyChartPermission")
    //查看进件人数权限
    private int applyChartPermission;

    @Column(name="loanAcctChartPermission")
    //授信人数权限
    private int loanAcctChartPermission;

    @Column(name="cashNumCharPermission")
    //查看放款人数权限
    private int cashNumCharPermission;

    @Column(name="firstCashAmtChartPermission")
    //首次提现查看权限
    private int firstCashAmtChartPermission;

    @Column(name="totalCashAmtChartPermission")
    //总放款金额查看权限
    private int totalCashAmtChartPermission;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getRegisterChartPermission() {
		return registerChartPermission;
	}

	public void setRegisterChartPermission(int registerChartPermission) {
		this.registerChartPermission = registerChartPermission;
	}

	public int getLoginChartPermission() {
		return loginChartPermission;
	}

	public void setLoginChartPermission(int loginChartPermission) {
		this.loginChartPermission = loginChartPermission;
	}

	public int getApplyChartPermission() {
		return applyChartPermission;
	}

	public void setApplyChartPermission(int applyChartPermission) {
		this.applyChartPermission = applyChartPermission;
	}

	public int getLoanAcctChartPermission() {
		return loanAcctChartPermission;
	}

	public void setLoanAcctChartPermission(int loanAcctChartPermission) {
		this.loanAcctChartPermission = loanAcctChartPermission;
	}

	public int getCashNumCharPermission() {
		return cashNumCharPermission;
	}

	public void setCashNumCharPermission(int cashNumCharPermission) {
		this.cashNumCharPermission = cashNumCharPermission;
	}

	public int getFirstCashAmtChartPermission() {
		return firstCashAmtChartPermission;
	}

	public void setFirstCashAmtChartPermission(int firstCashAmtChartPermission) {
		this.firstCashAmtChartPermission = firstCashAmtChartPermission;
	}

	public int getTotalCashAmtChartPermission() {
		return totalCashAmtChartPermission;
	}

	public void setTotalCashAmtChartPermission(int totalCashAmtChartPermission) {
		this.totalCashAmtChartPermission = totalCashAmtChartPermission;
	}
    
 

}
