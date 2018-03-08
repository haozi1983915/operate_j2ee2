package com.maimob.server.db.daoImpl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.maimob.server.db.common.BaseDaoHibernate5;
import com.maimob.server.db.entity.AdminPermission;
 

//注入
@Repository
public class AdminPermissionDaoImpl extends BaseDaoHibernate5<AdminPermission> {

    @SuppressWarnings("unchecked")
    public  List<AdminPermission> findPermissionByType(String type,String opType) {
        // TODO Auto-generated method stub
        List<AdminPermission> list = sessionFactory.getCurrentSession()
                .createQuery("select en from AdminPermission en where en.type = ?0 and en.opType = ?0 ")
                .setParameter(0, type)
                .setParameter(1, opType)
                .getResultList();
        return list;
    }

}
