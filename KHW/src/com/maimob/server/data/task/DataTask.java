package com.maimob.server.data.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.maimob.server.db.entity.OptimizationTask;
import com.maimob.server.importData.dao.OperateDao;

public class DataTask extends Thread {
	
	public static void main(String[] args) {
		DataTask dt = new DataTask();
		dt.start();
	}
	
	
	@Override
	public void run() {
		

		while(true)
		{
			try {

				
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String now1 = sdf.format(new Date());
				  sdf = new SimpleDateFormat("yyyy-MM-dd");
				String now = sdf.format(new Date());
				Map ss = new HashMap();
				ss.put("startDate", now);
				ss.put("endDate", now);
				ss.put("optimization", "-1"); 
				

				ss.put("id", "1517918294658");
//				ss.put("channel", "duorong_cesyy");
				ss.put("optimization", "-1");
				ss.put("tableId", "30");
				ss.put("adminId", "1516704387763");
				System.out.println(now+"   "+now1);

				OptimizationTask ot = new OptimizationTask(ss);
				OperateData pd = new OperateData(ot);
				pd.Statistics();
				
				
				sleep(180000);
			} catch (Exception e) {
				e.printStackTrace();
			} 
			
			
			
			
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}
	
	
	
	

}
