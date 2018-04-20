package com.maimob.server.db.common;

import java.io.Serializable;
import java.util.List;

public interface BaseDao<T> {

    // 根据id查找实体
    T get(Class<T> entityClazz , Serializable id);
    
    //保存实体
    Serializable save(T enetity);
    
    //更新实体
    void update(T entity);
    
    //删除实体
    void delete(T entity);
    
    //根据id删除实体
    void delete(Class<T> entityClazz , Serializable id);
    
    //获取所有实例
    List<T> findAll(Class<T> entityClazz);
    
    //获取实例总数
    long findCount(Class<T> entityClazz);
    
    //添加 或者更改
    void saveOrUpdate(T entity);
    
    List<T> findAllById(Class<T> entityClazz , String customerId);

    //查询分页数据
    List<T> queryForPage(String hql,int pageNo,int pageSize, Class<T> clazz);

    //查询总记录数
    int getCount(String hql);


        

}
