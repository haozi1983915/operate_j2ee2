//package com.maimob.server.db.service;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.maimob.server.db.daoImpl.UserDaoImpl;
//import com.maimob.server.db.daoImpl.UserInfoDaoImpl;
//import com.maimob.server.utils.StringUtils;
// 
//@Service
//public class UserService {
//
//    //注入UserDaoImpl
//    @Autowired
//    private UserDaoImpl userDaoImpl;
//    
//    @Autowired
//    private UserInfoDaoImpl userInfoDaoImpl;
//    
//    public void saveUser(User user){
//        userDaoImpl.save(user);
//    }
//    
//    public void saveUser(User user,UserInfo userInfo){
//        userDaoImpl.save(user);
//        userInfoDaoImpl.save(userInfo);
//    }
//    
//    public User findUserByCustomerId(String customerId){
//        if(StringUtils.isStrEmpty(customerId)) return null;
//        return userDaoImpl.findSignalByCustomerId(User.class, customerId);
//    }
//    
//    public User findUserByMobileNo(String mobileNo){
//        if(StringUtils.isStrEmpty(mobileNo)) return null;
//        return userDaoImpl.findByMobileNo(mobileNo);
//    }
//    
//    public List<User> listUserByInvitorId(String invitorId){
//        if(StringUtils.isStrEmpty(invitorId))return null;
//        return userDaoImpl.listUserByInvitorId(invitorId);
//    }
//    
//    public List<User> listUserByInvitorId(String invitorId,String resultStatus){
//        if(StringUtils.isStrEmpty(invitorId) || StringUtils.isStrEmpty(resultStatus))return null;
//        return userDaoImpl.listUserByInvitorId(invitorId,resultStatus);
//    }
//    
//    public long countInviteNum(String invitorId){
//        if(StringUtils.isStrEmpty(invitorId))return 0;
//        return userDaoImpl.countInviteNum(invitorId);
//    }
//     
//    
//    public String findUserCustomerId(String mobileNo){
//        if(StringUtils.isStrEmpty(mobileNo))return "";
//        return userDaoImpl.findUserCustomerId(mobileNo);
//    }
//    
//    public String findUserMobileNo(String customerId){
//        if(StringUtils.isStrEmpty(customerId))return "";
//        return userDaoImpl.findUserMobileNo(customerId);
//    }
//}
