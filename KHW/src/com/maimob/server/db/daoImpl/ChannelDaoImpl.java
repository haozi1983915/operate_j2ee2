package com.maimob.server.db.daoImpl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.maimob.server.db.common.BaseDaoHibernate5;
import com.maimob.server.db.entity.Admin;
import com.maimob.server.db.entity.Channel;
import com.maimob.server.db.entity.Proxy;
import com.maimob.server.utils.StringUtils;
 

//注入
@Repository
public class ChannelDaoImpl extends BaseDaoHibernate5<Channel>{
    
    @SuppressWarnings("unchecked") 
    public List<Channel> findAllByName(String Name) {

    	List<Channel> Channels = new ArrayList<Channel>();
    	try {
    		Channels = sessionFactory.getCurrentSession()
                    .createQuery("select en from Channel en where en.name = ?0")
                    .setParameter(0, Name)
                    .getResultList();
		} catch (Exception e) {
			// TODO: handle exception
		}
    	
        return Channels;
    }
    

    @SuppressWarnings("unchecked") 
    public List<Channel> findAll(String where) {
    	String hql = "select en from Channel en ";
    	if(!StringUtils.isStrEmpty(where))
    		hql = hql+" where "+where;
    	List<Channel> Channels = new ArrayList<Channel>();
    	try {
    		Channels = sessionFactory.getCurrentSession()
                    .createQuery(hql)
                    .getResultList();
		} catch (Exception e) {
			// TODO: handle exception
		}
    	
        return Channels;
    }

    @SuppressWarnings("deprecation")
    public long findCouByProxyId(long proxyId){
        return (long)sessionFactory.getCurrentSession()
                .createQuery("select count(*) from Channel en where en.proxyId = ?0")
                .setParameter(0, proxyId)
                .uniqueResult();
    }
    

    @SuppressWarnings("deprecation")
    public  List<Channel>  findByProxyId(long proxyId){
        return sessionFactory.getCurrentSession()
                .createQuery("select en from Channel en where en.proxyId = ?0")
                .setParameter(0, proxyId)
                .getResultList();
    }
    

    @SuppressWarnings("unchecked") 
    public long findCouByParameter(List<Long> ids,String where) {
    	
    	String hql = "select count(1) from Channel en ";
    	hql += where;
    	
    	if(ids.size() > 0)
    		hql += " and en.adminId in (:ids) ";
    	
    	Query q = sessionFactory.getCurrentSession()
        .createQuery(hql);
    	if(ids.size() > 0)
    		q.setParameterList("ids", ids);
    	
        return (long)q.uniqueResult();
    }



    @SuppressWarnings("deprecation")
    public List<Channel> findByAdminids(List<Long> ids,String where,int start,int maxCount){

    	String hql = "select en from Channel en ";
    	hql += where;
    	
    	if(ids.size() > 0)
    		hql += " and en.adminId in (:ids) ";
    	

    	List<Channel> Channels = new ArrayList<Channel>();
    	try {
    		Query q = sessionFactory.getCurrentSession()
    		        .createQuery(hql);
    		    	if(ids.size() > 0)
    		    		q.setParameterList("ids", ids);
    		    	
    		        q.setMaxResults(maxCount)
    		        .setFirstResult(start);
    		Channels = q.getResultList();
    		
		} catch (Exception e) {
			// TODO: handle exception
		}
    	
    	
        return Channels;
    }

    @SuppressWarnings("deprecation")
    public List<Long> findIdByAdminids(List<Long> ids,String where){

    	String hql = "select en.id from Channel en ";
    	hql += where;
    	
    	if(ids.size() > 0)
    		hql += " and en.adminId in (:ids) ";
    	
    	Query q = sessionFactory.getCurrentSession()
        .createQuery(hql);
    	if(ids.size() > 0)
    		q.setParameterList("ids", ids);
    	
    	
    	List<Long> Channels = new ArrayList<Long>();
    	try {
    		Channels = q.getResultList();
    		
		} catch (Exception e) {
			// TODO: handle exception
		}
    	
    	
        return Channels;
    }

    @SuppressWarnings("deprecation")
    public List<Long> findIdByProxyId(long proxyId){

    	String hql = "select en.id from Channel en where proxyId="+proxyId; 
    	
    	Query q = sessionFactory.getCurrentSession()
        .createQuery(hql);
    	
    	
    	List<Long> Channels = new ArrayList<Long>();
    	try {
    		Channels = q.getResultList();
    		
		} catch (Exception e) {
			// TODO: handle exception
		}
    	
    	
        return Channels;
    }
    
    

    @SuppressWarnings("deprecation")
    public List<Channel> findMainChannel(long proxyId){
    	
    	List<Channel> Channels = new ArrayList<Channel>();
    	try {
    		Channels = sessionFactory.getCurrentSession()
                    .createQuery("select en from Channel en where en.proxyId in (:proxyId) and level = 1")
                    .setParameter("proxyId", proxyId)
                    .getResultList();
    		
		} catch (Exception e) {
			// TODO: handle exception
		}
        return Channels;
    }
      
    
    

    @SuppressWarnings("deprecation")
    public List<Long> findProxyidByAdminIds(List<Long> adminids){

    	List<Long> Channels = new ArrayList<Long>();
    	try {
    		Channels = sessionFactory.getCurrentSession()
                    .createQuery("select distinct en.proxyId from Channel en where en.adminId in (:adminids)")
                    .setParameter("adminids", adminids)
                    .getResultList();
    		
		} catch (Exception e) {
			e.printStackTrace();
		}
        return Channels;
    }
      

    @SuppressWarnings("unchecked") 
    public long findCouByChannelNo(String channelNo) {
    	
        return (long)sessionFactory.getCurrentSession()
                .createQuery("select count(1) from Channel en where channelNo = '"+channelNo+"'  ").uniqueResult();
    }



    @SuppressWarnings("unchecked") 
    public List<Channel> findByChannelNo(String where) {

    	List<Channel> Channels = new ArrayList<Channel>();
    	try {
    		Channels = sessionFactory.getCurrentSession()
                    .createQuery("select en from Channel en "+where+"  ") .getResultList();
    		
		} catch (Exception e) {
			// TODO: handle exception
		}
        return Channels;
    }


    
    
}
