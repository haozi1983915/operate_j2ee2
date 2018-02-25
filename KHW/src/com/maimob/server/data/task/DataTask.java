package com.maimob.server.data.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.maimob.server.db.entity.OptimizationTask;

public class DataTask extends Thread {
	
	@Override
	public void run() {
		

		while(true)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String now = sdf.format(new Date());
			Map ss = new HashMap();
			ss.put("startDate", now);
			ss.put("endDate", now);
			ss.put("optimization", "-1"); 

			OptimizationTask ot = new OptimizationTask(ss);
			OperateData pd = new OperateData(ot);
			pd.Statistics();
			
			
			
			
			
			
			
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}
	
	
	
	

}
