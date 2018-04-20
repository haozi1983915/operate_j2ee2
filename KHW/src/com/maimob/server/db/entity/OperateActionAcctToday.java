package com.maimob.server.db.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by yang on 2018/4/20.
 */

@Entity
@Table(name="operate_action_acct_today")
@DynamicUpdate(true)
@DynamicInsert(true)
public class OperateActionAcctToday implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="id", nullable=false)
    private Long id;

    private String date;

    private Long today;

    private Long yestoday;

    private Long seven;

    @Column(name = "Thirty")
    private Long thirty;

    private Long other;

    private Short appid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getToday() {
        return today;
    }

    public void setToday(Long today) {
        this.today = today;
    }

    public Long getYestoday() {
        return yestoday;
    }

    public void setYestoday(Long yestoday) {
        this.yestoday = yestoday;
    }

    public Long getSeven() {
        return seven;
    }

    public void setSeven(Long seven) {
        this.seven = seven;
    }

    public Long getThirty() {
        return thirty;
    }

    public void setThirty(Long thirty) {
        this.thirty = thirty;
    }

    public Long getOther() {
        return other;
    }

    public void setOther(Long other) {
        this.other = other;
    }

    public Short getAppid() {
        return appid;
    }

    public void setAppid(Short appid) {
        this.appid = appid;
    }
}
