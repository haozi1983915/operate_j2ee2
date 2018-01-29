package com.maimob.server.sms;

public class SMSMobileParms {

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String[] getParms() {
        return parms;
    }

    public void setParms(String[] parms) {
        this.parms = parms;
    }

    private String mobile;
    
    private String[] parms;
}
