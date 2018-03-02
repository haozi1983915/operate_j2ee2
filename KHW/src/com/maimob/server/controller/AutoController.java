package com.maimob.server.controller;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Controller;

import com.maimob.server.data.task.DataTask;

@Controller
public class AutoController  implements ApplicationListener<ContextRefreshedEvent>{

	public static DataTask dt = null;
	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		if(dt == null)
		{
//			dt = new DataTask();
//			dt.start();
		}
		
	}

}

