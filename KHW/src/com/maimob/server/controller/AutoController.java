package com.maimob.server.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Controller;

import com.maimob.server.data.task.DataTask;
import com.maimob.server.db.entity.Dictionary;
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
		Cache.DicCatche(dao);
		
		
		
		
		
		
		if(dt == null)
		{
//			dt = new DataTask();
//			dt.start();
		}
		
	}
	
	
	public void createChannelTypeList()
	{
		List<Dictionary> dic3 = Cache.getDicList(3);
		List<Dictionary> dic4 = Cache.getDicList(4);
		List<Dictionary> dic5 = Cache.getDicList(5);
		
		Map<Long ,String> arc = new HashMap();
		for(Dictionary d : dic3)
		{
			arc.put(d.getId(), d.getName());
		}
		
		Map<Long ,String> type = new HashMap();
		for(Dictionary d : dic3)
		{
			arc.put(d.getId(), d.getName());
		}
		
		
		
		
		
		
		
	}
	
	
	
	
	
	

}

