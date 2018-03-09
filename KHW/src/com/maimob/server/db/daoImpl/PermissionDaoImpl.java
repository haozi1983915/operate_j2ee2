package com.maimob.server.db.daoImpl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.maimob.server.db.common.BaseDaoHibernate5;
import com.maimob.server.db.entity.Permission;
 

//注入
@Repository
public class PermissionDaoImpl extends BaseDaoHibernate5<Permission> {

    @SuppressWarnings("unchecked")
    public List<Permission> findPermissionByType(long type) {
        // TODO Auto-generated method stub
        List<Permission> list = sessionFactory.getCurrentSession()
                .createQuery("select en from Permission en where en.type = ?0")
                .setParameter(0, type)
                .getResultList();
        return list;
    }

    @SuppressWarnings("unchecked")
    public List<Permission> findPermissionByName(String name,String opType) {
        // TODO Auto-generated method stub
        List<Permission> list = sessionFactory.getCurrentSession()
                .createQuery("select en from Permission en where en.name = ?0  and opType = "+opType+"  ")
                .setParameter(0, name)
                .getResultList();
        return list;
    }

    @SuppressWarnings("unchecked")
    public List<Permission> findPermissionByMeta(String meta,String opType) {
        // TODO Auto-generated method stub
        List<Permission> list = sessionFactory.getCurrentSession()
                .createQuery("select en from Permission en where en.meta = '"+meta+"'   and opType = "+opType+"  ")
                .getResultList();
        return list;
    }


}
