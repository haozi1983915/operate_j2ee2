package com.maimob.server.controller;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.beans.factory.annotation.Autowired;

import com.maimob.server.db.service.DaoService;
import com.maimob.server.utils.AppTools;

public class AutoRun  implements ServletContextListener {  
    public void contextDestroyed(ServletContextEvent e) {   
    }

    @Autowired
    private DaoService dao;
    
    public void contextInitialized(ServletContextEvent e) {
        System.out.println("------------SocketServer start-------");
//        AppTools.cache(dao);
    }
    
    
}