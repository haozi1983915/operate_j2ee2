package com.maimob.server.sms;

import java.util.ArrayList;

public class SMSObjectContent {

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<SMSMobileParms> getMobileParms() {
        return mobileParms;
    }

    public void setMobileParms(ArrayList<SMSMobileParms> mobileParms) {
        this.mobileParms = mobileParms;
    }

    private String content;
    
    private ArrayList<SMSMobileParms> mobileParms;
}

