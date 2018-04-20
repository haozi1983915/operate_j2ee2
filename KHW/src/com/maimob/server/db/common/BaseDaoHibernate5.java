package com.maimob.server.db.common;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import com.maimob.server.db.entity.OperateReportFormAppToday;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;

public class BaseDaoHibernate5<T> implements BaseDao<T>{

    @Resource(name = "sessionFactory")
    protected SessionFactory sessionFactory;
    
    @Override
    public T get(Class<T> entityClazz, Serializable id) {
        // TODO Auto-generated method stub
        return (T)sessionFactory.getCurrentSession().get(entityClazz, id);
    }

    @Override
    public Serializable save(T enetity) {
        // TODO Auto-generated method stub
    	Session session = sessionFactory.getCurrentSession();
        session.clear();  
        Serializable s = session.save(enetity);
        session.flush();
    	
        return s;
    }

    @Override
    public void update(T entity) {
        // TODO Auto-generated method stub 

    	Session session = sessionFactory.getCurrentSession();
        session.clear();  
        session.update(entity);
        session.flush();
    	
    }

    @Override
    public void delete(T entity) {
        // TODO Auto-generated method stub
    	Session session = sessionFactory.getCurrentSession();
        session.clear();  
        session.delete(entity);
        session.flush();
    	
    }

    @Override
    public void delete(Class<T> entityClazz, Serializable id) {
        // TODO Auto-generated method stub
        sessionFactory.getCurrentSession().createQuery("delete "+entityClazz.getSimpleName() + " en where en.id = ?0")
        .setParameter(0, id)
        .executeUpdate();
    }

    @Override
    public List<T> findAll(Class<T> entityClazz) {
        // TODO Auto-generated method stub
        return find("select en from "+ entityClazz.getSimpleName()+" en");
    }

    @SuppressWarnings("deprecation")
    @Override
    public long findCount(Class<T> entityClazz) {
        // TODO Auto-generated method stub
        return  (long) sessionFactory.getCurrentSession()
                .createQuery("select count(*) from "+ entityClazz.getSimpleName())
                .uniqueResult();
    }

    @Override
    public void saveOrUpdate(Object entity) {
        // TODO Auto-generated method stub
        Session session = sessionFactory.getCurrentSession();
        session.clear();  
        session.saveOrUpdate(entity);
        session.flush();
        
        
    }
    
    @SuppressWarnings("unchecked")
    protected List<T> find(String hql){
        return (List<T>)sessionFactory.getCurrentSession()
                .createQuery(hql)
                .getResultList();
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected List<T> find(String hql , Object... params){
        Query query = sessionFactory.getCurrentSession()
                .createQuery(hql);
        for(int i=0 , len = params.length; i < len ; i++){
            query.setParameter(i + "", params[i]);
        }
        return (List<T>)query.getResultList();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<T> findAllById(Class<T> entityClazz , String customerId) {
        return sessionFactory.getCurrentSession()
                .createQuery("select en from "+ entityClazz.getSimpleName()+" en where en.id = ?0")
                .setParameter(0, customerId)
                .getResultList();
    }

    @Override
    public List<T> queryForPage(String hql, int pageNo, int pageSize, Class<T> clazz) {
        List list = sessionFactory.getCurrentSession()
                .createQuery(hql)
                .setFirstResult((pageNo - 1) * pageSize)
                .setMaxResults(pageNo * pageSize)
                .setResultTransformer(Transformers.aliasToBean(clazz))
                .list();
        return list;
    }

    @Override
    public int getCount(String hql) {
        int cou = sessionFactory.getCurrentSession().createQuery(hql).list().size();
        return cou;
    }

    @SuppressWarnings("unchecked")
    public T findSignalById(Class<T> entityClazz , String customerId){
        List<T> list = sessionFactory.getCurrentSession()
                .createQuery("select en from "+ entityClazz.getSimpleName()+" en where en.id = ?0")
                .setParameter(0, customerId)
                .setMaxResults(1)
                .getResultList();
        
        if(list!=null && list.size() >0){
            return list.get(0);
        }
        return null;
    }
}
