package com.maimob.server.db.daoImpl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.maimob.server.db.common.BaseDaoHibernate5;
import com.maimob.server.utils.StringUtils; 
 

//注入
//@Repository
//public class UserDaoImpl extends BaseDaoHibernate5<User>{
//    
//    @SuppressWarnings("unchecked") 
//    public List<User> findAllByMobileNo(String mobileNo) {
//        return sessionFactory.getCurrentSession()
//                .createQuery("select en from User en where en.mobileNo = ?0")
//                .setParameter(0, mobileNo)
//                .getResultList();
//    }
//     
//    public User findByMobileNo(String mobileNo){
//        List<User> l = findAllByMobileNo( mobileNo);
//        if(l!=null && l.size() >0){
//            return l.get(0);
//        }
//        return null;
//    }
//    
//    @SuppressWarnings("unchecked") 
//    public List<User> findAllByTag(String tag){
//        return sessionFactory.getCurrentSession()
//                .createQuery("select new User(mobileNo) from User en where en.tag = ?0")
//                .setParameter(0, tag)
//                .getResultList();
//    }
//    
//    @SuppressWarnings("deprecation")
//    public long findAllByResultStatus(String resultStatus){
//        return (Long)sessionFactory.getCurrentSession()
//                .createQuery("select count(*) from User en where en.resultStatus = ?0")
//                .setParameter(0, resultStatus)
//                .uniqueResult();
//    }
//    
//    @SuppressWarnings("unchecked") 
//    public List<User> findAllRegisterUser(){
//        return sessionFactory.getCurrentSession()
//                .createQuery("select new User(mobileNo,resultStatus,tag,registerTime) from User en")
//                .getResultList();
//    }
//    
//    @SuppressWarnings("unchecked")
//    public List<User> listUserRegisterBdeforTime(String time){
//        return sessionFactory.getCurrentSession()
//                .createQuery("select new User(mobileNo,id,resultStatus,tag,registerTime) from User en where en.registerTime < ?0")
//                .setParameter(0, time)
//                .getResultList();
//    }
//    
//    @SuppressWarnings("deprecation")
//    public String findCustomerIdByMobileNo(String mobileNo){
//        if(StringUtils.isStrEmpty(mobileNo))return null;
//        return (String)sessionFactory.getCurrentSession()
//                .createQuery("select customerId from User en where en.mobileNo = ?0")
//                .setParameter(0, mobileNo)
//                .uniqueResult();
//    }
//    
//    @SuppressWarnings("unchecked")
//    public List<User> listUserByInvitorId(String invitorId){
//        if(StringUtils.isStrEmpty(invitorId))return null;
//        return sessionFactory.getCurrentSession()
//                .createQuery("select en from User en where en.invitorId = ?0")
//                .setParameter(0, invitorId)
//                .getResultList();
//    }
//    
//    @SuppressWarnings("unchecked")
//    public List<User> listUserByInvitorId(String invitorId,String resultStatus){
//        if(StringUtils.isStrEmpty(invitorId) || StringUtils.isStrEmpty(resultStatus))return null;
//        return sessionFactory.getCurrentSession()
//                .createQuery("select en from User en where en.invitorId = ?0 and en.resultStatus = ?1")
//                .setParameter(0, invitorId)
//                .setParameter(1, resultStatus)
//                .getResultList();
//    }
//    @SuppressWarnings("deprecation")
//    public long countInviteNum(String invitorId){
//        if(StringUtils.isStrEmpty(invitorId))return 0;
//        
//        return (Long)sessionFactory.getCurrentSession()
//                .createQuery("select count(*) from User en where en.invitorId = ?0")
//                .setParameter(0, invitorId)
//                .uniqueResult();
//    }
//    
//    @SuppressWarnings("deprecation")
//    public String findUserCustomerId(String mobileNo){
//        return (String)sessionFactory.getCurrentSession()
//                .createQuery("select en.customerId from User en where en.mobileNo = ?0 ")
//                .setParameter(0, mobileNo)
//                .uniqueResult();
//    }
//    
//    @SuppressWarnings("unchecked")
//    public String findUserMobileNo(String customerId){
//        List<String> mobileNos = sessionFactory.getCurrentSession()
//                .createQuery("select en.mobileNo from User en where en.customerId = ?0 ")
//                .setParameter(0, customerId)
//                .setMaxResults(1)
//                .getResultList();
//        if(mobileNos == null || mobileNos.size()<1){
//            return "";
//        }
//        return mobileNos.get(0);
//    }
//}
