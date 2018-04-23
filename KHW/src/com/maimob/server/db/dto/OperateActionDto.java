package com.maimob.server.db.dto;

import java.io.Serializable;

/**
 * Created by yang on 2018/4/20.
 */

public class OperateActionDto implements Serializable {

    private String date;

    private Long todayApply;

    private Long otherApply;

    private Long todayOpen;

    private Long otherOpen;

    private Long sumbit;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getTodayApply() {
        return todayApply;
    }

    public void setTodayApply(Long todayApply) {
        this.todayApply = todayApply;
    }

    public Long getOtherApply() {
        return otherApply;
    }

    public void setOtherApply(Long otherApply) {
        this.otherApply = otherApply;
    }

    public Long getTodayOpen() {
        return todayOpen;
    }

    public void setTodayOpen(Long todayOpen) {
        this.todayOpen = todayOpen;
    }

    public Long getOtherOpen() {
        return otherOpen;
    }

    public void setOtherOpen(Long otherOpen) {
        this.otherOpen = otherOpen;
    }

    public Long getSumbit() {
        return sumbit;
    }

    public void setSumbit(Long sumbit) {
        this.sumbit = sumbit;
    }
}
