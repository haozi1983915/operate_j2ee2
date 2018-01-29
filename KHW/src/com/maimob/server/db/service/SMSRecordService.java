package com.maimob.server.db.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maimob.server.db.daoImpl.SMSRecordDaoImpl;
import com.maimob.server.db.entity.SMSRecord;

@Service
public class SMSRecordService {

    @Autowired
    private SMSRecordDaoImpl smsRecordDaoImpl;
    
    public Integer saveSMSRecord(SMSRecord smsRecord){
        return (Integer)smsRecordDaoImpl.save(smsRecord);
    }
    
}
