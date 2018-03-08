package com.maimob.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Controller;

import com.maimob.server.data.task.DataTask;
import com.maimob.server.db.service.DaoService;
import com.maimob.server.utils.Cache;

@Controller
public class AutoController  implements ApplicationListener<ContextRefreshedEvent>{

	@Autowired
	private DaoService dao;
	public static DataTask dt = null;
	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {

		Cache.AdminCatche(dao);

		Cache.channelCatche(dao);
		
		if(dt == null)
		{
//			dt = new DataTask();
//			dt.start();
		}
		
	}

}

