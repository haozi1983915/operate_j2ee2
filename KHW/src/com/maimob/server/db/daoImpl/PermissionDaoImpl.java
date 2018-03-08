package com.maimob.server.db.daoImpl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.maimob.server.db.common.BaseDaoHibernate5;
import com.maimob.server.db.entity.Permission;
 

//注入
@Repository
public class PermissionDaoImpl extends BaseDaoHibernate5<Permission> {

    @SuppressWarnings("unchecked")
    public List<Permission> findPermissionByType(String type,String opType) {
        // TODO Auto-generated method stub
        List<Permission> list = sessionFactory.getCurrentSession()
                .createQuery("select en from Permission en where en.type = ?0 and en.opType = ?0 ")
                .setParameter(0, type)
                .setParameter(1, opType)
                .getResultList();
        return list;
    }

}
