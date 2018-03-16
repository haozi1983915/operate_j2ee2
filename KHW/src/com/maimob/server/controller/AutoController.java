package com.maimob.server.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONObject;
import com.maimob.server.data.task.DataTask;
import com.maimob.server.db.entity.Dictionary;
import com.maimob.server.db.service.DaoService;
import com.maimob.server.protocol.BaseResponse;
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
		
//		List m = createChannelTypeList();
//
//		BaseResponse baseResponse = new BaseResponse();
//		baseResponse.setChannelTypeList(m);
//
//		String content = JSONObject.toJSONString(baseResponse);
//		System.out.println(content);
	}
	 
	
	public List createChannelTypeList()
	{
		List<Dictionary> dic3 = Cache.getDicList(3);
		List<Dictionary> dic4 = Cache.getDicList(4);
		List<Dictionary> dic5 = Cache.getDicList(5);
		
		Map<Long ,List<Map<String,String>>> typeList = new HashMap<Long ,List<Map<String,String>>>();
		for(Dictionary d : dic5)
		{
			long hid = d.getHigherId();
			Map<String,String> val = new HashMap<String,String>();
			val.put("value", d.getId()+"");
			val.put("label", d.getName());
			List<Map<String,String>> dlist = null;
			if(typeList.get(hid) == null)
			{
				dlist = new ArrayList<Map<String,String>>();
				typeList.put(hid, dlist);
			}
			else
			{
				dlist = typeList.get(hid);
			}

			dlist.add(val);
		}
		

//		{
//	          label: '换量--免费',
//	          options: [{
//	            value: '9',//要传的值
//	            label: '1:1注册',//要显示在页面上的名字
//	          }, {
//	            value: '8',//要传的值
//	            label: '1:1提现',//要显示在页面上的名字
//	          }]
//	        }, 
		
		Map<Long ,Dictionary> arc = new HashMap<Long ,Dictionary>();
		for(Dictionary d : dic3)
		{
			arc.put(d.getId(), d );
		}
		
		List channelTypeList = new ArrayList ();
		for(Dictionary d : dic4)
		{
			long hid = d.getHigherId();
			String arcName = arc.get(hid).getName(); 

			Map type = new HashMap ();
			Map<String,String> val = new HashMap<String,String>();
			val.put("label", arcName+"-"+d.getName());
			val.put("value", hid+","+d.getId());
			List<Map<String,String>> slist = typeList.get(d.getId());
			slist.add(0,val);
			type.put("label", arcName+"-"+d.getName());
			type.put("options", slist);
			channelTypeList.add(type);
		}
		
		return channelTypeList;
	}
	
	
	
	
	
	

}

