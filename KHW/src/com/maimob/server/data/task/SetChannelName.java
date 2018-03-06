package com.maimob.server.data.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.maimob.server.importData.dao.OperateDao;

public class SetChannelName {

	public static void main(String[] args) {

		OperateDao od = new OperateDao();

		try {
			Map<String, Map<String, String>> channels = new HashMap<String, Map<String, String>>();

			String sql1 = " select * from operate_channel   ";

			List<Map<String, String>> channelList = od.Query(sql1);
			for(int i = 0;i < channelList.size();i++)
			{
				Map<String, String> channelobj = channelList.get(i); 

				String channelId = channelobj.get("id");
				
				String channelName = channelobj.get("channelName");
				String channel = channelobj.get("channel");
				String attribute = channelobj.get("attribute");
				String type = channelobj.get("type");
				String subdivision = channelobj.get("subdivision");
				String adminid = channelobj.get("adminId");
				String proxyid = channelobj.get("proxyId");
				if(adminid==null)
					adminid = "0";
				if(!channelId.equals("0"))
				{
					String up = "update operate_reportform set proxyid="+proxyid+", channelId="+channelId+",channel='"+channel+"',adminid='"+adminid+"', channelName='"+channelName+"',channelAttribute='"+attribute+"',channelType='"+type+"',subdivision='"+subdivision+"'"
							+ "  where channel='"+channel+"'  ";
					int sl = od.Update(up);
					if(sl == 0)
						System.out.println(i+"   "+channelId+"   "+channel);
					
					

//					String up = "update operate_reportform_app set proxyid="+proxyid+" "
//							+ "  where channel='"+channel+"'  ";
//					int sl = od.Update(up);
//					if(sl == 0)
//						System.out.println(i+"   "+channelId+"   "+channel);
					
				}
				
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		
		

	}

}
