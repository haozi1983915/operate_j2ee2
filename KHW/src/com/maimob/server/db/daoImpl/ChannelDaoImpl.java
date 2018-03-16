package com.maimob.server.db.daoImpl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.maimob.server.db.common.BaseDaoHibernate5;
import com.maimob.server.db.entity.Admin;
import com.maimob.server.db.entity.Channel;
import com.maimob.server.db.entity.Proxy;
import com.maimob.server.utils.Cache;
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
    public long findCouByProxyId(String where){

    	String hql = "select count(*) from Channel en ";
    	if(!StringUtils.isStrEmpty(where))
    	{
    		hql = hql+where;
    		
    	}
    	
        return (long)sessionFactory.getCurrentSession()
                .createQuery(hql)
                .uniqueResult();
    }

    @SuppressWarnings("deprecation")
    public long findCouByProxyId(long ProxyId){

    	String hql = "select count(*) from Channel en where proxyId= "+ProxyId;
    	
        return (long)sessionFactory.getCurrentSession()
                .createQuery(hql)
                .uniqueResult();
    }
    


    @SuppressWarnings("deprecation")
    public  List<Channel>  findByProxyId(String where,int start,int maxCount){

    	String hql = "select en from Channel en ";
    	if(!StringUtils.isStrEmpty(where))
    	{
    		hql = hql + where;
    		
    	}
    	
    	hql += "  order by proxyid, level asc  ";
    	
        return sessionFactory.getCurrentSession()
                .createQuery(hql)
		        .setMaxResults(maxCount)
		        .setFirstResult(start)
                .getResultList();
    }

    @SuppressWarnings("deprecation")
    public  List<Channel>  findByProxyId(String proxyid){

    	String hql = "select en from Channel en where proxyId="+proxyid+" "; 
    	
        return sessionFactory.getCurrentSession()
                .createQuery(hql)
                .getResultList();
    }
    

    @SuppressWarnings("deprecation")
    public  List<Channel>  findByChannel(String channel){

    		String hql = "select en from Channel en where channel='"+channel+"' "; 
    	
        return sessionFactory.getCurrentSession()
                .createQuery(hql)
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
    	


    		hql += " order by proxyid, level asc ";
    	
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
    public List<Long> findIdByProxyId(long proxyId,String where){

    	String hql = "select en.id from Channel en ";
    	hql += where;
    	hql += " and proxyId="+proxyId;
    	
    	
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
    
    
    //通过渠道商id 和level  获取一级渠道
    @SuppressWarnings("deprecation")
    public List<Channel> findMainChannels(List<Long> proxyId){
    	
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
      
    //通过渠道商id 和level  获取一级渠道
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
    public long findCouByChannel(String channel,String appid) {
        return (long)sessionFactory.getCurrentSession()
                .createQuery("select count(1) from Channel en where channel = '"+channel+"' and appid="+appid+" ").uniqueResult();
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


    @SuppressWarnings("unchecked") 
    public int UpdateOptimization_startDate(long optimizationId,int optimization,long startDate, long id) {
        int n = 0;
        try {
        	String sql = "update from Channel s set s.optimizationId="+optimizationId+",s.optimization="+optimization+",s.startDate="+startDate+" where s.id="+id;
            Query query = sessionFactory.getCurrentSession().createQuery(sql);
            n = query.executeUpdate();
            
            Cache.updateOptimization_startDate(id, optimization,startDate);
            
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
        }
        return n;
    }
    

    @SuppressWarnings("unchecked") 
    public int UpdateOptimization(long optimizationId, long id) {
        int n = 0;
        try {
        	String sql = "update from Channel s set s.optimizationId="+optimizationId+" where s.id="+id;
            Query query = sessionFactory.getCurrentSession().createQuery(sql);
            n = query.executeUpdate();
            
            Cache.updateOptimization(id, optimizationId);
            
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
        }
        return n;
    }

    @SuppressWarnings("unchecked") 
    public int UpdateStuts(int status, long id) {
        int n = 0;
        try {
        	String sql = "update from Channel s set s.status="+status+" where s.id="+id;
            Query query = sessionFactory.getCurrentSession().createQuery(sql);
            n = query.executeUpdate();
            Cache.updateChannelStuts(id, status);
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
        }
        return n;
    }

    @SuppressWarnings("unchecked") 
    public int UpdateSynchronous(int synchronous, long id) {
        int n = 0;
        try {
        	String sql = "update from Channel s set s.synchronous="+synchronous+" where s.id="+id;
            Query query = sessionFactory.getCurrentSession().createQuery(sql);
            n = query.executeUpdate();
            Cache.updateChannelSynchronous(id, synchronous);
            
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
        }
        return n;
    }
    

    @SuppressWarnings("unchecked") 
    public int UpdateChannelName(String channelName,String pwd, long id) {
        int n = 0;
        try {
        	String sql = "update Channel s set s.channelName='"+channelName+"',s.pwd='"+pwd+"' where s.id="+id;
            Query query = sessionFactory.getCurrentSession().createQuery(sql);
            n = query.executeUpdate();
            Cache.updateChannelName(id, channelName);
            
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
        }
        return n;
    }
    

    @SuppressWarnings("unchecked") 
    public int UpdateType(String rewardPrice , long rewardTypeId,long rewardId,long attribute,long type,long subdivision, long proxyId, long adminId) {
        int n = 0;
        try {
        	String sql = "update Channel s set s.rewardPrice='"+rewardPrice+"',s.rewardTypeId='"+rewardTypeId+"', s.rewardId='"+rewardId+"', s.adminId="+adminId+" ,  s.attribute="+attribute+" , s.type="+type+" , s.subdivision="+subdivision+"  where s.proxyId="+proxyId+" and s.rewardId = 0 ";
            Query query = sessionFactory.getCurrentSession().createQuery(sql);
            n = query.executeUpdate();
            
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
        }
        return n;
    }
    
    
}
