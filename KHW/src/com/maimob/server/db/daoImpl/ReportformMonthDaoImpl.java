package com.maimob.server.db.daoImpl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.maimob.server.db.common.BaseDaoHibernate5;
import com.maimob.server.db.entity.Operate_reportform_month;
 

//注入
@Repository
public class ReportformMonthDaoImpl extends BaseDaoHibernate5<Operate_reportform_month>{
     

    @SuppressWarnings("unchecked") 
    public long findCouByParameter(List<Long> ids,String where) {
    	
    	String hql = "select count(1) from Operate_reportform_month en ";
    	hql += where;


    	if(ids.size() == 0)
    		hql += " and en.channelId > 0 ";
    	
    	if(ids.size() > 0)
    		hql += " and en.channelId in (:ids) ";
    	
    	Query q = sessionFactory.getCurrentSession()
        .createQuery(hql);
    	if(ids.size() > 0)
    		q.setParameterList("ids", ids);
    	
        return (long)q.uniqueResult();
    }


    @SuppressWarnings("deprecation")
    public List<Operate_reportform_month> findByChannelids(List<Long> ids,String where,int start,int maxCount){

    	String hql = "select en from Operate_reportform_month en ";
    	hql += where;


    	if(ids.size() == 0)
    		hql += " and en.channelId > 0 ";
    	
    	if(ids.size() > 0)
    		hql += " and en.channelId in (:ids) ";
    	

    	hql += " order by date  desc,channel asc ";
    	
    	
    	
    	Query q = sessionFactory.getCurrentSession()
        .createQuery(hql);
    	if(ids.size() > 0)
    		q.setParameterList("ids", ids);
    	
        q.setMaxResults(maxCount)
        .setFirstResult(start);
        return q.getResultList();
    }

    

    @SuppressWarnings("unchecked") 
    public long findCouByParameter(String where) {
    	
    	String hql = "select count(1) from Operate_reportform_month en ";
    	hql += where;

    	Query q = sessionFactory.getCurrentSession()
        .createQuery(hql);
    	
        return (long)q.uniqueResult();
    }


    @SuppressWarnings("deprecation")
    public List<Operate_reportform_month> findByChannelids(String where,int start,int maxCount){

    	String hql = "select en from Operate_reportform_month en ";
    	hql += where;

    	hql += " order by date  desc,channel asc ";
    	
    	Query q = sessionFactory.getCurrentSession()
        .createQuery(hql);
        q.setMaxResults(maxCount)
        .setFirstResult(start);
        return q.getResultList();
    }

    
    
    
    
    
    
    
    
    
}















