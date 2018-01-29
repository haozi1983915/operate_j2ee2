package com.maimob.server.db.daoImpl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.maimob.server.db.common.BaseDaoHibernate5;
import com.maimob.server.utils.StringUtils;
//
////注入
//@Repository
//public class UserInfoDaoImpl extends BaseDaoHibernate5<UserInfo>{
//
//    @SuppressWarnings("unchecked")
//    public UserInfo findUserInfoByCustomerId(String customerId) {
//        if(StringUtils.isStrEmpty(customerId)) return null;
//        List<UserInfo> list = sessionFactory.getCurrentSession()
//                .createQuery("select en from UserInfo en where en.customerId = ?0")
//                .setParameter(0, customerId)
//                .setMaxResults(1)
//                .getResultList();
//        
//        if(list!=null && list.size() >0){
//            return list.get(0);
//        }
//        return null;
//    }
//}
