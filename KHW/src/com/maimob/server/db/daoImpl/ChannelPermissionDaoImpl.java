package com.maimob.server.db.daoImpl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.maimob.server.db.common.BaseDaoHibernate5;
import com.maimob.server.db.entity.Admin;
import com.maimob.server.db.entity.ChannelPermission;
 

//注入
@Repository
public class ChannelPermissionDaoImpl extends BaseDaoHibernate5<ChannelPermission>{
    
    @SuppressWarnings("unchecked") 
    public ChannelPermission findById(long id) {
    	
    	ChannelPermission cp = null;
    	try {
        	cp = (ChannelPermission)sessionFactory.getCurrentSession()
                    .createQuery("select en from ChannelPermission en where en.id = ?0")
                    .setParameter(0, id).getSingleResult();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
        return cp;
    }

    @SuppressWarnings("unchecked") 
    public List<ChannelPermission> findByProxyId(long proxyId) {
    	
    	List<ChannelPermission> cplist = null;
    	try {
    		cplist =  sessionFactory.getCurrentSession()
                    .createQuery("select en from ChannelPermission en where en.proxyId = ?0")
                    .setParameter(0, proxyId).getResultList();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
        return cplist;
    }
    
    
    
    
    
}
