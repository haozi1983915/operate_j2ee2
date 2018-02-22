package com.maimob.server.data.task;

import java.util.List;
import java.util.Map;

import com.maimob.server.db.entity.OptimizationTask;
import com.maimob.server.db.service.DaoService;
import com.maimob.server.importData.dao.OperateDao;

public class ProxyDataTask extends Thread {
	
	
	
	boolean finish = true;
	public ProxyDataTask() {
		runTime = System.currentTimeMillis();
	}

	
	public OptimizationTask getOptimizationTask()
	{
		return nowot;
	}
	
	public boolean isrun;
	
	
	long runTime = 0;
	OptimizationTask nowot = null; 
	ProxyData pd = null;
	List<Map<String, String>> ts = null;
	OperateDao od = null;
	@Override
	public void run() {

		runTime = System.currentTimeMillis();
		while(true)
		{

			od = new OperateDao();
			try {

				runTime = System.currentTimeMillis();
				finish = false;
				ts = od.getAllTask();
				if(ts == null || ts.size() == 0)
				{
					finish = true;
					break;
				}
				Map<String, String> task = ts.get(0);
				
				nowot = new OptimizationTask(task);
				od.updateTask(nowot.getId(),1);

				pd = new ProxyData(nowot);
				pd.Statistics();

				nowot.setProgress(100);
				od.updateTask(nowot.getId(),2);
				
				
			} catch (Exception e) {
				e.printStackTrace();
				finish = true;
			}
			finally {
				od.close();
			}
			
		}
		
		finish = true;
	}
	
	
	
	
	

}
