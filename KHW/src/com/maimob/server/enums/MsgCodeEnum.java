package com.maimob.server.enums;

public enum MsgCodeEnum {

    SUCCESS(0, "SUCCESS"),
    FAIL(1, "FAIL"),
    NO_PARAM(2, "参数为空"),
    NO_DATA(3, "无数据");
    private int code;

    private String desc;

    MsgCodeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
