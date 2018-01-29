package com.maimob.server.sms;

public class DianJiSMSSendResonseDetail {

    //0 成功
    //1   用户鉴权错误
    //2   IP鉴权错误
    //3   手机号码在黑名单
    //4   手机号码格式错误
    //5   短信内容有误
    //7   手机号数量超限
    //8   账户已停用
    //9   未知错误
    //10  时间戳已过期
    //11  同号码同模板发送频率过快
    //12  同号码同模板发送次数超限
    //13  包含敏感词
    //14  扩展号不合法
    //15  扩展信息长度过长
    //16  
    //17  
    //18  json解析错误
    //99  账户余额不足
    private int Rspcode;
    
    public int getRspcode() {
        return Rspcode;
    }

    public void setRspcode(int rspcode) {
        Rspcode = rspcode;
    }

    public String getMsg_Id() {
        return Msg_Id;
    }

    public void setMsg_Id(String msg_Id) {
        Msg_Id = msg_Id;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getExtInfo() {
        return ExtInfo;
    }

    public void setExtInfo(String extInfo) {
        ExtInfo = extInfo;
    }

    public int getFee() {
        return Fee;
    }

    public void setFee(int fee) {
        Fee = fee;
    }

    //信息标识，用于对应状态报告响应码不成功时无此内容
    private String Msg_Id;
    
    //手机号
    private String Mobile;
    
    //扩展信息为空时无这个字段
    private String ExtInfo;
    
    //计费数
    private int Fee;
    
}
