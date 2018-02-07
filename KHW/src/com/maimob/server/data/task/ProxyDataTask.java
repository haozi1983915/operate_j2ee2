package com.maimob.server.data.task;

import java.util.List;
import java.util.Map;

import com.maimob.server.db.entity.OptimizationTask;
import com.maimob.server.db.service.DaoService;
import com.maimob.server.importData.dao.OperateDao;

public class ProxyDataTask extends Thread {
	
	
	
	boolean finish = false;
	public ProxyDataTask() {
		
	}

	
	public OptimizationTask getOptimizationTask()
	{
		return nowot;
	}
	
	OptimizationTask nowot = null; 
	@Override
	public void run() {

		while(true)
		{

			try {

				OperateDao od = new OperateDao();
				List<Map<String, String>> ts = od.getAllTask();
				if(ts.size() == 0)
				{
					break;
				}
				Map<String, String> task = ts.get(0);
				
				nowot = new OptimizationTask(task);
				od.updateTask(nowot.getId(),1);

				ProxyData pd = new ProxyData(nowot);
				pd.Statistics();

				nowot.setProgress(100);
				od.updateTask(nowot.getId(),2);
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		finish = true;
	}
	
	
	
	
	

}
