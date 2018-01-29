package com.maimob.server.datasource;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.ThrowsAdvice;


public class DataSourceAdvice implements MethodBeforeAdvice, AfterReturningAdvice, ThrowsAdvice {

    public static Logger logger = LoggerFactory.getLogger(DataSourceAdvice.class);
    
    @Override
    public void afterReturning(Object arg0, Method arg1, Object[] arg2, Object arg3) throws Throwable {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void before(Method method, Object[] arg, Object target) throws Throwable {
        logger.info("切入点: {} 类中 {} 方法" , target.getClass().getName() , method.getName() );
        if(method.getName().startsWith("add")
                || method.getName().startsWith("create")
                || method.getName().startsWith("save")
                || method.getName().startsWith("edit")
                || method.getName().startsWith("update")
                || method.getName().startsWith("delete")
                || method.getName().startsWith("add")
                || method.getName().startsWith("remove")){
            
            logger.info("切换到: master");
            DataSourceSwitcher.setMaster();

        }else{
            logger.info("切换到: slave");
            DataSourceSwitcher.setSlave();
        }    
    }

    // 抛出Exception之后被调用
    public void afterThrowing(Method method, Object[] args, Object target, Exception ex) throws Throwable {
        DataSourceSwitcher.setMaster();
        logger.error("出现异常,切换到: master ex = {} ",ex);
    }
    
}
