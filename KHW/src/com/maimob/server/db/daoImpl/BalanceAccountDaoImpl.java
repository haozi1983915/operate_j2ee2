package com.maimob.server.db.daoImpl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
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
    
    /**
     * 查找所有的付款公司详情
     * @return
     */
    @SuppressWarnings("unchecked") 
    public List<BalanceAccount> findAllBalanceAccount() {
    	List<BalanceAccount> BalanceAccounts = new ArrayList<BalanceAccount>();
    	try {
    		BalanceAccounts = sessionFactory.getCurrentSession()
            .createQuery("select en from BalanceAccount en ") 
            .getResultList();
		} catch (Exception e) {
			// TODO: handle exception
		}
        return BalanceAccounts;
    }
    
    @SuppressWarnings("unchecked") 
    public List<BalanceAccount> findBalanceAccountById(JSONObject jobj) {
    	List<BalanceAccount> BalanceAccounts = new ArrayList<BalanceAccount>();
    	int id = jobj.getIntValue("ourCompanyId");
    	try {
    		BalanceAccounts = sessionFactory.getCurrentSession()
            .createQuery("select en from BalanceAccount en where en.id = " + id) 
            .getResultList();
		} catch (Exception e) {
			// TODO: handle exception
		}
        return BalanceAccounts;
    }

    public List<BalanceAccount> findById(String id) {
    	List<BalanceAccount> BalanceAccounts = new ArrayList<BalanceAccount>();
    	try {
    		BalanceAccounts = sessionFactory.getCurrentSession()
            .createQuery("select en from BalanceAccount en where  id="+id) 
            .getResultList();
		} catch (Exception e) {
			// TODO: handle exception
		}
        return BalanceAccounts;
    }


    @SuppressWarnings("unchecked") 
    public long findCouByCompany(String company) {
        return (long)sessionFactory.getCurrentSession()
                .createQuery("select count(1) from BalanceAccount en where company = '"+company+"'  ").uniqueResult();
    }

    @SuppressWarnings("unchecked") 
    public long findCouByTaxpayerNo(String taxpayerNo) {
        return (long)sessionFactory.getCurrentSession()
                .createQuery("select count(1) from BalanceAccount en where taxpayerNo = '"+taxpayerNo+"'  ").uniqueResult();
    }


    @SuppressWarnings("unchecked") 
    public long findCouByAccountNo(String accountNo) {
        return (long)sessionFactory.getCurrentSession()
                .createQuery("select count(1) from BalanceAccount en where accountNo = '"+accountNo+"'  ").uniqueResult();
    }
    
    
    
}















