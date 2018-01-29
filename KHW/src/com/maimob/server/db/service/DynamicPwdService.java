package com.maimob.server.db.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maimob.server.db.daoImpl.DynamicPwdDaoImpl;
import com.maimob.server.db.entity.DynamicPwd;


@Service
public class DynamicPwdService {

    //注入DynamicPwdDaoImpl
    @Autowired
    private DynamicPwdDaoImpl dynamicPwdDaoImpl;
    
    public DynamicPwd findDynamicPwdbyMobileNo(String mobileNo){
        return dynamicPwdDaoImpl.findDynamicPwdByMobileNo(mobileNo);
    }
    
    public void saveDynamicPwd(DynamicPwd dynamic){
        dynamicPwdDaoImpl.save(dynamic);
    }
    
    public void updateDynamicPwd(DynamicPwd dynamic){
        dynamicPwdDaoImpl.update(dynamic);
    }
    
}
