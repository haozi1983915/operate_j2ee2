package com.maimob.server.db.daoImpl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.maimob.server.db.common.BaseDaoHibernate5;
import com.maimob.server.db.entity.AdminPermission;
 

//注入
@Repository
public class AdminPermissionDaoImpl extends BaseDaoHibernate5<AdminPermission> {

    @SuppressWarnings("unchecked")
    public  List<AdminPermission> findPermissionByType(String adminid,String type,String opType) {
        // TODO Auto-generated method stub
        List<AdminPermission> list = sessionFactory.getCurrentSession()
                .createQuery("select en from AdminPermission en where en.adminid = ?0 and en.type = ?1 and en.opType = ?2 ")
                .setParameter(0, adminid)
                .setParameter(1, type)
                .setParameter(2, opType)
                .getResultList();
        return list;
    }

}
