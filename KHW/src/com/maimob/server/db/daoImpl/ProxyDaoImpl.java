package com.maimob.server.db.daoImpl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.maimob.server.db.common.BaseDaoHibernate5;
import com.maimob.server.db.entity.Admin;
import com.maimob.server.db.entity.Channel;
import com.maimob.server.db.entity.Proxy;

//注入
@Repository
public class ProxyDaoImpl extends BaseDaoHibernate5<Proxy>{
    
    @SuppressWarnings("unchecked") 
    public List<Proxy> findAllById(long id) {
    	

    	List<Proxy>Proxys = new ArrayList<Proxy>();
    	try {
    		Proxys = sessionFactory.getCurrentSession()
                    .createQuery("select en from Proxy en where en.id = ?0")
                    .setParameter(0, id)
                    .getResultList();
    		
		} catch (Exception e) {
			// TODO: handle exception
		}
    	
    	
        return Proxys;
    }
    

//    @SuppressWarnings("unchecked") 
//    public List<Proxy> findAll() {
//        return findAll("");
//    }
    

//    @SuppressWarnings("unchecked") 
//    public List<Proxy> findAll(String where) {
//    	
//    	String hql = "select en from Proxy en";
//    	if(!StringUtils.isStrEmpty(where))
//    		hql = hql+" where "+where;
//        return sessionFactory.getCurrentSession()
//                .createQuery(hql)
//                .getResultList();
//    }
    

    @SuppressWarnings("unchecked") 
    public long findCouByParameter(List<Long> ids,String where) {
    	
    	String hql = "select count(1) from Proxy en ";
    	hql += where;
    	
    	if(where.equals(" where  1=1 "))
    	{
        	if(ids.size() > 0)
        		hql += " and en.id in (:ids) ";
    	}
    	
    	
    	Query q = sessionFactory.getCurrentSession()
        .createQuery(hql);
    	if(where.equals(" where  1=1 "))
    	{
	    	if(ids.size() > 0)
	    		q.setParameter("ids", ids);
    	}
    	
        return (long)q.uniqueResult();
    }
 

    @SuppressWarnings("unchecked") 
    public int Update(long ProxyId, long permissionId,long cou,String channelNo) {
//        return sessionFactory.getCurrentSession()
//                .createQuery("update en from Proxy en "+where+"  ")
//                .setParameter(0, AdminId)
//                .getResultList();
        int n = 0;
        try {
        	String sql = "update from Proxy s set s.permissionId="+permissionId+",s.channelCou="+cou+",s.channelNo='"+channelNo+"' where s.id="+ProxyId;
        	if(!channelNo.equals(""))
        		sql = "update from Proxy s set s.permissionId="+permissionId+",s.channelCou="+cou+",s.channelNo='"+channelNo+"' where s.id="+ProxyId;
        	else 
        		sql = "update from Proxy s set s.permissionId="+permissionId+",s.channelCou="+cou+" where s.id="+ProxyId;
        		
            Query query = sessionFactory.getCurrentSession().createQuery(sql);
            n = query.executeUpdate();
            
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
        }
        
        return n;
    }
    

    @SuppressWarnings("unchecked") 
    public long findCouByMobileNo(String MobileNo) {
    	
        return (long)sessionFactory.getCurrentSession()
                .createQuery("select count(1) from Proxy en where MobileNo = '"+MobileNo+"'  ").uniqueResult();
    }

    

    @SuppressWarnings("unchecked") 
    public List<Proxy> findAllByAdminId(long AdminId) {

    	List<Proxy>Proxys = new ArrayList<Proxy>();
    	try {
    		Proxys = sessionFactory.getCurrentSession()
                    .createQuery("select en from Proxy en where en.adminid = ?0")
                    .setParameter(0, AdminId)
                    .getResultList();
    		
		} catch (Exception e) {
			// TODO: handle exception
		}
    	
    	
    	
        return Proxys;
    }

    @SuppressWarnings("deprecation")
    public List<Proxy> findByIds(List<Long> ids,String where,int start,int maxCount){

    	String hql = "select en from Proxy en ";
    	hql += where;
    	if(where.equals(" where  1=1 "))
    	{
	    	if(ids.size() > 0)
	    		hql += " and en.id in (:ids) ";
    	}

		hql += " order by createTime desc ";
    	
    	
    	Query q = sessionFactory.getCurrentSession()
        .createQuery(hql);
    	if(where.equals(" where  1=1 "))
    	{
	    	if(ids.size() > 0)
	    		q.setParameter("ids", ids);
    	}
    	
        q.setMaxResults(maxCount)
        .setFirstResult(start);
        

    	List<Proxy>Proxys = new ArrayList<Proxy>();
    	try {
    		Proxys = q.getResultList();
    		
		} catch (Exception e) {
			// TODO: handle exception
		}
    	
        
        return Proxys;
    }
    
    

    @SuppressWarnings("deprecation")
    public List<Proxy> findNameByIds(List<Long> ids,String where){

    	String hql = "select new Proxy(en.id,en.company) from Proxy en ";
    	hql += where;
    	
    	if(ids.size() > 0)
    		hql += " and en.id in (:ids) ";
    	
    	Query q = sessionFactory.getCurrentSession()
        .createQuery(hql);
    	if(ids.size() > 0)
    		q.setParameter("ids", ids);

    	List<Proxy>Proxys = new ArrayList<Proxy>();
    	try {
    		Proxys = q.getResultList();
    		
		} catch (Exception e) {
			// TODO: handle exception
		}
    	
        return Proxys;
    }
      
      
    
}
