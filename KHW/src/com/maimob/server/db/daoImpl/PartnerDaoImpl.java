package com.maimob.server.db.daoImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.maimob.server.db.common.BaseDaoHibernate5;
import com.maimob.server.db.entity.Partner;


@Repository
public class PartnerDaoImpl extends BaseDaoHibernate5<Partner>{

	//获取所有的合作方公司详情
	@SuppressWarnings("unchecked") 
    public List<Partner> findAll() {
    	List<Partner> partners = new ArrayList<Partner>();
    	try {
    		partners = sessionFactory.getCurrentSession()
                    .createQuery("select en from Partner en")
                    .getResultList();
		} catch (Exception e) {
			// TODO: handle exception
		}
    	
        return partners;
    }
	
	//通过id获取合作方详情
	 @SuppressWarnings("unchecked") 
	    public List<Partner> findAllById(String id) {
	    	List<Partner> partners = new ArrayList<Partner>();
	    	try {
	    		partners = sessionFactory.getCurrentSession()
	            .createQuery("select en from Partner en where en.id = " + id)
	            .getResultList();
			} catch (Exception e) {
				// TODO: handle exception
			}
	    	
	    	
	        return partners;
	    }

}
