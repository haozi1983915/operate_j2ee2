package com.maimob.server.importData.dao;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.maimob.server.base.BasicPage;
import com.maimob.server.db.daoImpl.DaoWhere;
import com.maimob.server.db.entity.Admin;
import com.maimob.server.db.entity.AdminPermission;
import com.maimob.server.db.entity.Channel;
import com.maimob.server.db.entity.Dictionary;
import com.maimob.server.db.entity.Operate_reportform;
import com.maimob.server.db.entity.UserPermission;
import com.maimob.server.utils.AppTools;
import com.maimob.server.utils.Cache;
import com.maimob.server.utils.DateTimeUtils;
import com.maimob.server.utils.StringUtils;
import com.mysql.cj.x.json.JsonArray;

import freemarker.template.utility.StringUtil;

public class SysDao extends Dao {

	public static void main(String[] args) {
		 System.out.println("36010219830915".substring(6,10));;
	}
	
	public SysDao() {

		super("system");

	}
	
	

	public void updateCost(JSONObject jobj)
	{
		
//		List<Dictionary> applist = Cache.getDicList(1);
//		Map<String,Long> apps = new HashMap<String,Long>();
//		for(Dictionary dic:applist)
//		{
//			dic.getName();
//			apps.put(dic.getName(), dic.getId());
//		}

		JSONArray data = jobj.getJSONArray("fileData");

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		String update = df.format(new Date());
		
		JSONArray errordata = new JSONArray();
		for(int i = 0;i < data.size();i++)
		{
			JSONObject d = (JSONObject) data.get(i);
			
			String channel = d.getString("channel");
			String date = d.getString("date");
			String cost = d.getString("cost");
			String appid = d.getString("appid");
//			String appid = "0";
//			if(!StringUtils.isStrEmpty(app))
//			{
//				appid= apps.get(app)+"";
//			}
			
			
			if(cost == null)
				cost = "0";
			else
			{
				cost = cost.replaceAll("¥", "");
				cost = cost.replaceAll(",", "");
			}
			
			Channel c = Cache.getChannelCatche(channel);
			String channelName = "";
			long id = 0;
			if(c != null)
			{
				id = c.getId();
				channelName = c.getChannelName();
			}
			else
			{
				errordata.add(d);
				
			}

			try {
				if(!cost.equals("0") && !StringUtils.isStrEmpty(appid))
				{
					
					//保存最后一次优化比例
					String sql2 = "update operate_data_log set channelId= "+id+",cost="+cost+", channel='"+channel+"',channelName='"+channelName+"',updateTime='"+update+"'  where channel= '"+channel+"' and date = '"+date+"' and appid="+appid+" ";
					int yx = this.Update(sql2);
					if(yx==0)
					{
						sql2 = "insert into operate_data_log(cost,channelId,date,channel,channelName,updateTime,appid) values("+cost+" ,"+id+" , '"+date+"', '"+channel+"', '"+channelName+"', '"+update+"',"+appid+") ";
						yx = this.Update(sql2);
					}
					 sql2 = "update operate_reportform set cost2="+cost+"   where channel= '"+channel+"' and date = '"+date+"' and appid="+appid+" ";
					 yx = this.Update(sql2);
				}
					
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
		}
	
		//return errordata;
	}
	
	
	
	
	
	
	
	
	
	
	
	

}





























