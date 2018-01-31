package com.maimob.server.db.daoImpl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.maimob.server.db.common.BaseDaoHibernate5;
import com.maimob.server.db.entity.Dictionary;
 

//注入
@Repository
public class DictionaryDaoImpl extends BaseDaoHibernate5<Dictionary>{
     

    @SuppressWarnings("unchecked") 
    public List<Dictionary> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("select en from Dictionary en where status = 1 ")
                .getResultList();
    }
    
 
    
}















