package com.maimob.server.sms;

import java.util.ArrayList;

public class DianJiSMSSendResponse {

    public ArrayList<DianJiSMSSendResonseDetail> getRets() {
        return Rets;
    }

    public void setRets(ArrayList<DianJiSMSSendResonseDetail> rets) {
        Rets = rets;
    }

    private ArrayList<DianJiSMSSendResonseDetail> Rets;
    
}
