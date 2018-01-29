package com.maimob.server.db.daoImpl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.maimob.server.db.common.BaseDaoHibernate5;
import com.maimob.server.db.entity.Admin;
import com.maimob.server.db.entity.Channel;
import com.maimob.server.db.entity.Reward;
import com.maimob.server.utils.StringUtils;
 

//注入
@Repository
public class RewardDaoImpl extends BaseDaoHibernate5<Reward>{
    
    @SuppressWarnings("unchecked") 
    public List<Reward> findById(long id) {
    	
        return sessionFactory.getCurrentSession()
                .createQuery("select en from Reward en where en.id = ?0")
                .setParameter(0,id).getResultList();
    }
    

    @SuppressWarnings("unchecked") 
    public List<Reward> findByChannelId(long channelId) {
    	
        return sessionFactory.getCurrentSession()
                .createQuery("select en from Reward en where en.channelId = ?0")
                .setParameter(0,channelId).getResultList();
    }
    
    
    
}
