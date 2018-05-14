package com.maimob.server.db.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.maimob.server.utils.AppTools;

@Entity
@Table(name = "operate_proxy")
@DynamicUpdate(true)
@DynamicInsert(true)
public class Proxy implements Serializable {

	public Proxy() {
		// TODO Auto-generated constructor stub
	}

	public Proxy(long id, String company, String channelNo, long channelCou) {
		this.setId(id);
		this.setCompany(company);
		this.setChannelNo(channelNo);
		this.setChannelCou(channelCou);
	}

	public Proxy(long id, String company, String contacts, String pwd, String channelNo) {
		this.setId(id);
		this.setCompany(company);
		this.setContacts(contacts);
		this.setPwd(pwd);
		this.setChannelNo(channelNo);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", nullable = false)
	// 管理员ID
	private long id;

	@Column(name = "company")
	// 公司名称
	private String company;
	
	@Column(name = "abbreviation")
	// 公司简称
	private String abbreviation;

	@Column(name = "bankAccount")
	// 银行账号
	private String bankAccount;

	@Column(name = "bank")
	// 开户行
	private String bank;

	@Column(name = "dutyParagraph")
	// 税号
	private String dutyParagraph;

	
	@Column(name = "taxNo")
	// 税收编码
	private String taxNo;
	
	@Column(name = "phone")
	// 公司电话
	private String phone;

	@Column(name = "address")
	// 公司地址
	private String address;

	@Column(name = "contacts")
	// 联系人姓名
	private String contacts;

	@Column(name = "mobileno")
	// 联系人电话
	private long mobileno;

	@Column(name = "email")
	// 联系人姓名
	private String email;

	@Column(name = "pwd")
	// 联系人姓名
	private String pwd;

	@Column(name = "channelCou")
	// 渠道数量
	private long channelCou;

	@Column(name = "permissionId")
	// 渠道数量
	private long permissionId;

	@Column(name = "channelNo")
	// 主渠道编号
	private String channelNo;

	@Column(name = "supplier_id")
	private String supplier_id;

	public String getSupplier_id() {
		return supplier_id;
	}

	public void setSupplier_id(String supplier_id) {
		this.supplier_id = supplier_id;
	}

	// 登录时间
	@Transient
	private long loginDate;

	public String getTaxNo() {
		return taxNo;
	}

	public void setTaxNo(String taxNo) {
		this.taxNo = taxNo;
	}
	// 渠道权限
	// @Transient
	// private ChannelPermission channelPermission;

	@Transient
	private List<ChannelPermission> channelPermissionList;

	public long getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(long loginDate) {
		this.loginDate = loginDate;
	}

	public List<ChannelPermission> getChannelPermissionList() {
		return channelPermissionList;
	}

	public void setChannelPermissionList(List<ChannelPermission> channelPermissionList) {
		this.channelPermissionList = channelPermissionList;
	}

	public String check() {
		if (this.channelPermissionList == null) {
			return "渠道权限不能为空";
		}

		if (this.company == null || "".equals(this.company)) {
			return "公司名字不能为空";
		} else if (this.company.length() > 20) {
			return "公司名字不能大于20个字";
		}

		// else if(this.bankAccount == null || "".equals(this.bankAccount))
		// {
		// return "银行账号不能为空";
		// }
		// else if(this.bank == null || "".equals(this.bank))
		// {
		// return "开户行不能为空";
		// }
		// else if(this.phone == null || "".equals(this.phone))
		// {
		// return "公司电话不能为空";
		// }
		// else if(this.address == null || "".equals(this.address))
		// {
		// return "公司地址不能为空";
		// }
		// else if(this.dutyParagraph == null || "".equals(this.dutyParagraph))
		// {
		// return "税号不能为空";
		// }
		else if (this.contacts == null || "".equals(this.contacts)) {
			return "联系人姓名不能为空";
		} else if (this.email == null || "".equals(this.email)) {
			return "联系人邮箱不能为空";
		} else if (this.pwd == null || "".equals(this.pwd)) {
			return "密码不能为空";
		} else if (this.mobileno == 0) {
			return "联系人电话不能为空";
		}
		return "";
	}

	public String getChannelNo() {
		return channelNo;
	}

	public void setChannelNo(String channelNo) {
		this.channelNo = channelNo;
	}

	public long getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(long permissionId) {
		this.permissionId = permissionId;
	}

	public long getChannelCou() {
		return channelCou;
	}

	public void setChannelCou(long channelCou) {
		this.channelCou = channelCou;
	}

	@Column(name = "createTime")
	// 渠道商创建时间
	private String createTime;

	public long getId() {
		if (id == 0) {
			this.setCreateTime(AppTools.getTime(0));
			id = AppTools.getId();
		}
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

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getDutyParagraph() {
		return dutyParagraph;
	}

	public void setDutyParagraph(String dutyParagraph) {
		this.dutyParagraph = dutyParagraph;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public long getMobileno() {
		return mobileno;
	}

	public void setMobileno(long mobileno) {
		this.mobileno = mobileno;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
