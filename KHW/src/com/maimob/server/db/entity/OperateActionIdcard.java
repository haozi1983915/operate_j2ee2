package com.maimob.server.db.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by yang on 2018/4/19.
 */
@Entity
@Table(name="operate_action_idcard")
@DynamicUpdate(true)
@DynamicInsert(true)
public class OperateActionIdcard implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id @Column(name="id", nullable=false)
    private Integer id;

    private String date;

    private Long register;

    private Long type;

    private Long today;

    private Long one;

    private Long two;

    private Long three;

    private Long four;

    private Long fiv;

    private Long six;

    private Long seven;

    private Long sevenLater;

    private Short appid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getRegister() {
        return register;
    }

    public void setRegister(Long register) {
        this.register = register;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Long getToday() {
        return today;
    }

    public void setToday(Long today) {
        this.today = today;
    }

    public Long getOne() {
        return one;
    }

    public void setOne(Long one) {
        this.one = one;
    }

    public Long getTwo() {
        return two;
    }

    public void setTwo(Long two) {
        this.two = two;
    }

    public Long getThree() {
        return three;
    }

    public void setThree(Long three) {
        this.three = three;
    }

    public Long getFour() {
        return four;
    }

    public void setFour(Long four) {
        this.four = four;
    }

    public Long getFiv() {
        return fiv;
    }

    public void setFiv(Long fiv) {
        this.fiv = fiv;
    }

    public Long getSix() {
        return six;
    }

    public void setSix(Long six) {
        this.six = six;
    }

    public Long getSeven() {
        return seven;
    }

    public void setSeven(Long seven) {
        this.seven = seven;
    }

    public Long getSevenLater() {
        return sevenLater;
    }

    public void setSevenLater(Long sevenLater) {
        this.sevenLater = sevenLater;
    }

    public Short getAppid() {
        return appid;
    }

    public void setAppid(Short appid) {
        this.appid = appid;
    }
}
