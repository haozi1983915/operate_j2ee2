package com.maimob.server.db.daoImpl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.maimob.server.db.common.BaseDaoHibernate5;
import com.maimob.server.db.entity.operate_pay_company; 
 

//注入
@Repository
public class PayCompanyDaoImpl extends BaseDaoHibernate5<operate_pay_company>{
    
    @SuppressWarnings("unchecked") 
    public List<operate_pay_company> findAll(String proxyid) {
    	List<operate_pay_company> pc = null;
    	try {
    		pc = sessionFactory.getCurrentSession()
            .createQuery("select en from operate_pay_company en where proxyid="+proxyid) 
            .getResultList();
		} catch (Exception e) {
			// TODO: handle exception
		}
        return pc;
    }

 
    
    
}















