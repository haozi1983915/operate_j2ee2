package com.maimob.server.db.daoImpl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.maimob.server.db.common.BaseDaoHibernate5;
import com.maimob.server.db.entity.BalanceAccount; 
 

//注入
@Repository
public class BalanceAccountDaoImpl extends BaseDaoHibernate5<BalanceAccount>{
    
    @SuppressWarnings("unchecked") 
    public List<BalanceAccount> findAll(String where) {
    	List<BalanceAccount> BalanceAccounts = new ArrayList<BalanceAccount>();
    	try {
    		BalanceAccounts = sessionFactory.getCurrentSession()
            .createQuery("select en from BalanceAccount en "+where) 
            .getResultList();
		} catch (Exception e) {
			// TODO: handle exception
		}
        return BalanceAccounts;
    }


}















