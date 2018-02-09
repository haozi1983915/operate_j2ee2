package com.maimob.server.db.daoImpl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.maimob.server.db.common.BaseDaoHibernate5;
import com.maimob.server.db.entity.OptimizationTask;
import com.maimob.server.utils.StringUtils;
 

//注入
@Repository
public class OptimizationTaskDaoImpl extends BaseDaoHibernate5<OptimizationTask>{
     

    @SuppressWarnings("deprecation")
    public List<OptimizationTask> findByAdminId(String adminId){
    	String hql = "select en from OptimizationTask en where adminId = "+adminId+" order by startdate  desc ";
    	Query q = sessionFactory.getCurrentSession().createQuery(hql);
        return q.getResultList();
    }

    @SuppressWarnings("deprecation")
    public List<OptimizationTask> findByNoFinish(){
    	String hql = "select en from OptimizationTask en where status < 2 order by id  desc ";
    	Query q = sessionFactory.getCurrentSession().createQuery(hql);
    	
    	List<OptimizationTask>  ots = q.getResultList();
    	
    	return ots;
    }

    @SuppressWarnings("deprecation")
    public List<OptimizationTask> findByAll(String where){
//    	String hql = "select en from OptimizationTask en order by startDate  desc ";
    	

    	String hql = " select en from OptimizationTask en order by id desc ";
    	if(!StringUtils.isStrEmpty(where))
    	{
    		hql = hql+where;
    	}
    	
    	
    	
    	Query q = sessionFactory.getCurrentSession().createQuery(hql);
        return q.getResultList();
    }



    @SuppressWarnings("unchecked") 
    public int delete(String id) {
        int n = 0;
        try {
        	String sql = "delete OptimizationTask s where s.id="+id;
            Query query = sessionFactory.getCurrentSession().createQuery(sql);
            n = query.executeUpdate();
            
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
        }
        return n;
    }
    
    
}















