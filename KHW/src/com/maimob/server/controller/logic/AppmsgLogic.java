package com.maimob.server.controller.logic;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.maimob.server.db.entity.Dictionary;
import com.maimob.server.db.service.DaoService;
import com.maimob.server.finance.FinanceTask;
import com.maimob.server.utils.Cache;
import com.maimob.server.utils.StringUtils;

public class AppmsgLogic extends Logic {

	private DaoService dao;
	
	public AppmsgLogic(DaoService dao) {
		super(dao);
		this.dao = dao;
	}

	public String getMsgway(String json)
	{
		String check = this.CheckJson(json);
		if(!StringUtils.isStrEmpty(check))
			return check;

		JSONObject whereJson = JSONObject.parseObject(json); 
 
		try {
			List<Map<String,String>> waylist = od.getMsgway();
			this.baseResponse.setMsgwaylist(waylist);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		String jsonstr = this.toJson();
		return jsonstr;
		
	}
	

	public String getMsg(String json)
	{
		String check = this.CheckJson(json);
		if(!StringUtils.isStrEmpty(check))
			return check;

		JSONObject whereJson = JSONObject.parseObject(json); 
 
		try {
			List<Map<String,String>> msglist = od.getMsg();
			this.baseResponse.setMsglist(msglist);

			List<Dictionary> dic24 = Cache.getDicList(24);
			this.baseResponse.setSendTypeList(dic24);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String jsonstr = this.toJson();
		return jsonstr;
		
	}
	

	public String saveMsg(String json)
	{
		String check = this.CheckJson(json);
		if(!StringUtils.isStrEmpty(check))
			return check;

		JSONObject whereJson = JSONObject.parseObject(json); 
 
		try {
			String type = whereJson.getString("type");
			String time = whereJson.getString("time");
			String content = whereJson.getString("content");
			od.saveMsg(type,time,content);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		String jsonstr = this.toJson();
		return jsonstr;
	}
	

	public String saveMsgway(String json)
	{
		String check = this.CheckJson(json);
		if(!StringUtils.isStrEmpty(check))
			return check;

		JSONObject whereJson = JSONObject.parseObject(json); 
 
		try {
			String type = whereJson.getString("type");
			String time = whereJson.getString("time");
			String wayid = whereJson.getString("wayid");
			od.saveMsgway(type,time,wayid);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		String jsonstr = this.toJson();
		return jsonstr;
		
	}

	public String saveAllMsgway(String json)
	{
		String check = this.CheckJson(json);
		if(!StringUtils.isStrEmpty(check))
			return check;

		JSONObject whereJson = JSONObject.parseObject(json); 
 
		try {
			String id = whereJson.getString("wayid");
			od.saveAllMsgway(id);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		String jsonstr = this.toJson();
		return jsonstr;
		
	}
	
	

	public String getMsgList(String json)
	{
		String check = this.CheckJson(json);
		if(!StringUtils.isStrEmpty(check))
			return check;

		JSONObject whereJson = JSONObject.parseObject(json); 
 
		try {
			String mindate = whereJson.getString("mindate");
			String maxdate = whereJson.getString("maxdate");
			List<Map<String,String>> msglist = od.getMsgList(mindate,maxdate);
			this.baseResponse.setMsglist(msglist);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		String jsonstr = this.toJson();
		return jsonstr;
		
	}
	
	
	
	
	

}
