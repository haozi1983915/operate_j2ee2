package com.maimob.server.db.entity;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="operate_channel_history")
@DynamicUpdate(true)
@DynamicInsert(true)
public class OperateChannelHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="id", nullable=false)
    private long id;

    @Column(name="updateBy")
    private long updateBy;

    @Column(name="channelId")
    private long channelId;

    @Column(name="updateDate")
    private Date updateDate;

    @Column(name="del_flag")
    private short delFlag;

    @Column(name="log")
    private String log;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(long updateBy) {
        this.updateBy = updateBy;
    }

    public long getChannelId() {
        return channelId;
    }

    public void setChannelId(long channelId) {
        this.channelId = channelId;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public short getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(short delFlag) {
        this.delFlag = delFlag;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }
}
