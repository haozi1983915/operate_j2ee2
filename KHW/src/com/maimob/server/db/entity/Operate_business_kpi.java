package com.maimob.server.db.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name="operate_business_kpi")
@DynamicUpdate(true)
@DynamicInsert(true)
public class Operate_business_kpi implements Serializable{
	
	 private static final long serialVersionUID = 1L;

	    @Id @Column(name="id", nullable=false)
	    //主键
	    private long id;
	    
	    @Column(name="adminid")
	    //负责人
	    private long adminid;
	    
	    @Column(name="month")
	    //月份
	    private String month;
	    

		@Column(name="registerkpi")
	    //注册kpi
	    private long registerkpi;
	    
	    @Column(name="updateadminid")
	    //修改人id
	    private long updateadminid;
	    
	    @Column(name="updatetime")
	    //修改时间
	    private String updatetime;

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public long getAdminid() {
			return adminid;
		}

		public void setAdminid(long adminid) {
			this.adminid = adminid;
		}

		public String getMonth() {
			return month;
		}

		public void setMonth(String month) {
			this.month = month;
		}

		public long getRegisterkpi() {
			return registerkpi;
		}

		public void setRegisterkpi(long registerkpi) {
			this.registerkpi = registerkpi;
		}

		public long getUpdateadminid() {
			return updateadminid;
		}

		public void setUpdateadminid(long updateadminid) {
			this.updateadminid = updateadminid;
		}

		public String getUpdatetime() {
			return updatetime;
		}

		public void setUpdatetime(String updatetime) {
			this.updatetime = updatetime;
		}

		public static long getSerialversionuid() {
			return serialVersionUID;
		}

		public Operate_business_kpi(long adminid, String month, long registerkpi, long updateadminid) {
			this.adminid = adminid;
			this.month = month;
			this.registerkpi = registerkpi;
			this.updateadminid = updateadminid;
		}

	    
}
