package com.maimob.server.data.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.maimob.server.importData.dao.OperateDao;

public class Optimization {
	
	public static void main(String[] args) {
		
		Optimization.getOptimizationList(0);
		
		
	}
	

	public static Map<Long , List<Optimization>> getOptimizationList(long channelId)
	{

		Map<Long , List<Optimization>> ops = new HashMap<Long , List<Optimization>>();
		OperateDao od = new OperateDao();
		try {

			
			String sql = " select * from operate_optimization order by channelId,startDate,id asc ";
			
			if(channelId != 0)
				sql = " select * from operate_optimization where channelId = "+channelId+"  order by channelId,startDate,id asc ";
			
			List<Map<String, String>> optimizationList = od.Query(sql);
			long nowId = 0;
			List<Optimization> nowChannelOp = null;
			for(int i = 0;i < optimizationList.size();i++)
			{
				
				Map<String, String> optimization1 = optimizationList.get(i);
				long channelid = Long.parseLong(optimization1.get("channelId"));
				
				if(channelid != nowId)
				{
					nowChannelOp = new ArrayList<Optimization>();
					ops.put(channelid, nowChannelOp);
					nowId = channelid;
				}
				Optimization op = new Optimization();
				

				long startTime = Long.parseLong(optimization1.get("startDate"));

				int optimization = Integer.parseInt(optimization1.get("optimization"));
				long endTime = 0;
				if(optimizationList.size() > i+1)
				{
					for(int j = i+1;j < optimizationList.size();j++)
					{
						if(optimizationList.size() > j)
						{
							Map<String, String> nextChannel = optimizationList.get(j);
							
							long nextChannelid = Long.parseLong(nextChannel.get("channelId"));

							long nextStartTime = Long.parseLong(nextChannel.get("startDate"));
							int nextOptimization = Integer.parseInt(nextChannel.get("optimization"));
							
							if(channelid == nextChannelid)
							{
								if(startTime == nextStartTime)
								{
									optimization = nextOptimization;
									i = j;
								}
								else
								{
									endTime = nextStartTime;
									break;
								}
							}
							else
							{
								endTime = Long.MAX_VALUE;
								break;
							}
							
						}
						else
						{
							endTime = Long.MAX_VALUE;
						}
					}
				}
				else
				{
					endTime = Long.MAX_VALUE;
				}
				
				op.channelId = channelid;
				op.startTime = startTime;
				op.endTime = endTime;
				op.optimization = optimization;
				nowChannelOp.add(op);
				
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			od.close();
		}
		
		return ops;
	}
	
	long channelId = 0;
	long startTime = 0;
	long endTime = 0;
	int optimization = 0;
	
	public Optimization() {}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
