package com.maimob.server.db.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name="operate_partner")
@DynamicUpdate(true)
@DynamicInsert(true)
public class Partner implements Serializable{
	
	 private static final long serialVersionUID = 1L;
	 
	 @Id @Column(name="id", nullable=false)
	    //合作方序号  主键
	    private long id;
	    
	    @Column(name="company")
	    //公司名称
	    private String company;
	    
	    @Column(name="bankAccount")
	    //银行账号
	    private String bankAccount;
	    
		@Column(name="bank")
	    //开户行
	    private String bank;
	    
	    @Column(name="dutyParagraph")
	    //税号
	    private String dutyParagraph;
	    
	    @Column(name="taxcode")
	    //税收编码
	    private String taxcode;
	    
	    @Column(name="phone")
	    //公司电话
	    private String phone;
	    
	    @Column(name="address")
	    //公司地址
	    private String address;
	    
	    @Column(name="contacts")
	    //联系人
	    private String contacts;
	    
	    @Column(name="mobileno")
	    //联系人电话
	    private long mobileno;
	    
	    @Column(name="email")
	    //联系人邮箱
	    private String email;

	    @Column(name="pwd")
	    //初始化密码
	    private String pwd;
	    
	    @Column(name="cooperationContent")
	    //合作内容
	    private String cooperationContent;
	    

	    @Column(name="cooperationType")
	    //合作方式
	    private int cooperationType;
	    
	    @Column(name="ourCompanyId")
	    //我方公司id
	    private int ourCompanyId;
	    
	    @Column(name="invoiceContentId")
	    //发票内容id
	    private int invoiceContentId;
	   
	    
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

		public String getTaxcode() {
			return taxcode;
		}

		public void setTaxcode(String taxcode) {
			this.taxcode = taxcode;
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

		public String getCooperationContent() {
			return cooperationContent;
		}

		public void setCooperationContent(String cooperationContent) {
			this.cooperationContent = cooperationContent;
		}

		public int getCooperationType() {
			return cooperationType;
		}

		public void setCooperationType(int cooperationType) {
			this.cooperationType = cooperationType;
		}	

		public int getOurCompanyId() {
			return ourCompanyId;
		}

		public void setOurCompanyId(int ourCompanyId) {
			this.ourCompanyId = ourCompanyId;
		}

		public int getInvoiceContentId() {
			return invoiceContentId;
		}

		public void setInvoiceContentId(int invoiceContentId) {
			this.invoiceContentId = invoiceContentId;
		}

		public Partner() {
		}
		
		public Partner(long id, String company) {
			this.id = id;
			this.company = company;
		}

		public Partner(String company, String contacts, long mobileno, String cooperationContent, int cooperationType) {
			this.company = company;
			this.contacts = contacts;
			this.mobileno = mobileno;
			this.cooperationContent = cooperationContent;
			this.cooperationType = cooperationType;
		}

		public Partner(String company, String bankAccount, String bank, String dutyParagraph, String taxcode,
				String phone, String address, String contacts, long mobileno, String email, String pwd,
				String cooperationContent, int cooperationType, int ourCompanyId, int invoiceContentId) {
			this.company = company;
			this.bankAccount = bankAccount;
			this.bank = bank;
			this.dutyParagraph = dutyParagraph;
			this.taxcode = taxcode;
			this.phone = phone;
			this.address = address;
			this.contacts = contacts;
			this.mobileno = mobileno;
			this.email = email;
			this.pwd = pwd;
			this.cooperationContent = cooperationContent;
			this.cooperationType = cooperationType;
			this.ourCompanyId = ourCompanyId;
			this.invoiceContentId = invoiceContentId;
		}
		
		

		public Partner(String company, String bankAccount, String bank, String dutyParagraph, String taxcode,String phone,
				String address, String cooperationContent, int cooperationType, int invoiceContentId) {
			this.company = company;
			this.bankAccount = bankAccount;
			this.bank = bank;
			this.dutyParagraph = dutyParagraph;
			this.taxcode = taxcode;
			this.phone = phone;
			this.address = address;
			this.cooperationContent = cooperationContent;
			this.cooperationType = cooperationType;
			this.invoiceContentId = invoiceContentId;
		}

		@Override
		public String toString() {
			return "{company=" + company + ", bankAccount=" + bankAccount + ", bank=" + bank
					+ ", dutyParagraph=" + dutyParagraph + ", taxcode=" + taxcode + ", phone=" + phone + ", address="
					+ address + ", contacts=" + contacts + ", mobileno=" + mobileno + ", email=" + email
					+ ", cooperationContent=" + cooperationContent + ", cooperationType=" + cooperationType
					+ ", ourCompanyId=" + ourCompanyId + ", invoiceContentId=" + invoiceContentId + "}";
		}

		
	    
		
	    
}
