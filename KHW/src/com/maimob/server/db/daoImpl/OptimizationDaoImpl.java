package com.maimob.server.db.daoImpl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.maimob.server.db.common.BaseDaoHibernate5;
import com.maimob.server.db.entity.Operate_reportform_month;
import com.maimob.server.db.entity.Optimization;
 

//注入
@Repository
public class OptimizationDaoImpl extends BaseDaoHibernate5<Optimization>{
     

    @SuppressWarnings("deprecation")
    public List<Optimization> findByChannelId(String channelId){

    	String hql = "select en from Optimization en where channelId = "+channelId+" order by updatedate  desc ";
    	
    	Query q = sessionFactory.getCurrentSession()
        .createQuery(hql);
        return q.getResultList();
    }


    @SuppressWarnings("deprecation")
    public List<Optimization> findByChannelIdLast(String channelId){

    	String hql = "select en from Optimization en where channelId = "+channelId+" order by updatedate  desc ";
    	
    	Query q = sessionFactory.getCurrentSession()
        .createQuery(hql)
        .setMaxResults(1)
        .setFirstResult(0);
        return q.getResultList();
    }

    @SuppressWarnings("deprecation")
    public List<Optimization> findById(String id){

    	String hql = "select en from Optimization en where id = "+id;
    	
    	Query q = sessionFactory.getCurrentSession()
        .createQuery(hql);
        return q.getResultList();
    }
    
    
}















