package com.maimob.server.db.dto;

/**
 * Created by yang on 2018/4/20.
 */
public class OperateLoanDto {

    private String date;

    private Long todayOpen;

    private Long todayLoan;

    private Long otherOpen;

    private Long otherLoan;

    private Long submit;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getTodayOpen() {
        return todayOpen;
    }

    public void setTodayOpen(Long todayOpen) {
        this.todayOpen = todayOpen;
    }

    public Long getTodayLoan() {
        return todayLoan;
    }

    public void setTodayLoan(Long todayLoan) {
        this.todayLoan = todayLoan;
    }

    public Long getOtherOpen() {
        return otherOpen;
    }

    public void setOtherOpen(Long otherOpen) {
        this.otherOpen = otherOpen;
    }

    public Long getOtherLoan() {
        return otherLoan;
    }

    public void setOtherLoan(Long otherLoan) {
        this.otherLoan = otherLoan;
    }

    public Long getSubmit() {
        return submit;
    }

    public void setSubmit(Long submit) {
        this.submit = submit;
    }
}
