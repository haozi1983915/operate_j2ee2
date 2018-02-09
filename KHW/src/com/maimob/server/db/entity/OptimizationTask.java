package com.maimob.server.db.entity;

import java.io.Serializable;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.maimob.server.utils.AppTools;
import com.maimob.server.utils.Cache;

@Entity
@Table(name="operate_optimization_task")
@DynamicUpdate(true)
@DynamicInsert(true)
public class OptimizationTask implements Serializable{

	public OptimizationTask() {
		
	}
	public OptimizationTask(Map<String, String> task) {
		
		this.id = Long.parseLong(task.get("id"));
		this.channelId = Integer.parseInt(task.get("channelId"));
		this.channel = task.get("channel");
		this.startDate = task.get("startDate");
		this.endDate = task.get("endDate");
		this.optimization = Integer.parseInt(task.get("optimization"));
		this.tableId = Long.parseLong(task.get("tableId"));
		this.adminId = Long.parseLong(task.get("adminId"));
		this.channelName = task.get("channelName");
		this.comment = task.get("comment");
		Admin admin = Cache.getAdminCatche(this.adminId);
		if(admin != null)
			this.adminName = admin.getName();
		
	}

    @Id @Column(name="id", nullable=false)
    //ID
    private long id;
    
    @Column(name="channelId")
    //渠道id
    private long channelId;
    
    @Column(name="channel")
    //渠道号
    private String channel;
    
	@Column(name="channelName")
    //渠道名
    private String channelName;
    
    @Column(name="startDate")
    //开始时间
    private String startDate;

    @Column(name="endDate")
    //结束时间
    private String endDate;
    
    @Column(name="optimization")
    //优化比例
    private int optimization;
    
    @Column(name="date")
    //任务创建时间
    private String date;
    
    @Column(name="adminId")
    //操作人
    private long adminId;
    
    @Column(name="tableId")
    //报表id
    private long tableId;
    
    @Column(name="status")
    //运行状态
    private long status;


    @Column(name="listId")
    //操作人
    private long listId;
    
    
    @Column(name="comment")
    //备注
    private String comment;
    
    @Transient
    //	创建人姓名
    private String adminName;
    
    @Transient
    //	运行进度
    private float progress;

    @Transient
    //	运行进度消息
    private String progressMsg = "";

    @Transient
    //	运行时间
    private String runDate;
    
    @Transient
    //	任务跨度几天
    private int days;
    

    @Transient
    //	任务运行到哪-天
    private int step;
    
    
	public int getStep() {
		return step;
	}
	public void setStep(int step) {
		this.step = step;
	}
	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public String getRunDate() {
		return runDate;
	}

	public void setRunDate(String runDate) {
		this.runDate = runDate;
	}

	public String getProgressMsg() {
		return progressMsg;
	}

	public void setProgressMsg(String progressMsg) {
		this.progressMsg = progressMsg;
	}

	public float getProgress() {
		
		return progress;
	}

	public void setProgress(float progress) {
		this.progressMsg = "共"+days+"天\n第"+step+"天\n"+runDate + "\n总进度" + progress+"%";
		this.progress = progress;
	}

	public long getListId() {
		return listId;
	}

	public void setListId(long listId) {
		this.listId = listId;
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

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public int getOptimization() {
		return optimization;
	}

	public void setOptimization(int optimization) {
		this.optimization = optimization;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public long getAdminId() {
		return adminId;
	}

	public void setAdminId(long adminId) {
		this.adminId = adminId;
	}

	public long getTableId() {
		return tableId;
	}

	public void setTableId(long tableId) {
		this.tableId = tableId;
	}

	public long getStatus() {
		return status;
	}

	public void setStatus(long status) {
		this.status = status;
	}
    
 

    }
