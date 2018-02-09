package com.maimob.server.data.task;

import java.util.List;

import com.maimob.server.db.entity.OptimizationTask;
import com.maimob.server.db.service.DaoService;

public class TaskLine {
	
	static ProxyDataTask proxyDataTask;
	
	//0,任务开始。1 已经有任务在运行
	public static OptimizationTask getRunOptimizationTask()
	{
		if(proxyDataTask != null)
		{
			if(!proxyDataTask.finish)
				return proxyDataTask.getOptimizationTask();
			else
			{
				return null;
			}
		}
		else
			return null;
	}

	public synchronized static int startProxyDataTask()
	{
		if(proxyDataTask == null || proxyDataTask.finish == true )
		{
			proxyDataTask = new ProxyDataTask();
			proxyDataTask.start();
			return 0;
		}
		else
		{
			return 1;
		}
	}
	
	public boolean isProxyTaskRun()
	{
		if(this.proxyDataTask == null || this.proxyDataTask.finish == false)
			return false;
		else
			return true;
		
	}
	
	

}
