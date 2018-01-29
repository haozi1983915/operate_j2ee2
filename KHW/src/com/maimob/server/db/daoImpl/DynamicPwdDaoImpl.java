package com.maimob.server.db.daoImpl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.maimob.server.db.common.BaseDaoHibernate5;
import com.maimob.server.db.entity.DynamicPwd;
 

//注入
@Repository
public class DynamicPwdDaoImpl extends BaseDaoHibernate5<DynamicPwd> {

    @SuppressWarnings("unchecked")
    public DynamicPwd findDynamicPwdByMobileNo(String mobileNo) {
        // TODO Auto-generated method stub
        List<DynamicPwd> list = sessionFactory.getCurrentSession()
                .createQuery("select en from DynamicPwd en where en.mobileNo = ?0")
                .setParameter(0, mobileNo)
                .setMaxResults(1)
                .getResultList();
        if(list!=null && list.size() >0){
            return list.get(0);
        }
        return null;
    }

}
