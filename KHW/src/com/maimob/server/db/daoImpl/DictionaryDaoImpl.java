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
                .createQuery("select en from Dictionary en where status = 0 ")
                .getResultList();
    }
    

    @SuppressWarnings("unchecked") 
    public List<Dictionary> findByType(String type) {
        return sessionFactory.getCurrentSession()
                .createQuery("select en from Dictionary en where status = 0 and type = "+type)
                .getResultList();
    }
    

    @SuppressWarnings("unchecked") 
    public List<Dictionary> findByTypeName(String type,String name) {
        return sessionFactory.getCurrentSession()
                .createQuery("select en from Dictionary en where status = 0 and type = "+type+" and name='"+name+"'   ")
                .getResultList();
    }
    
    
}















