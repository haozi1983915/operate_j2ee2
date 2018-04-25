package com.maimob.server.controller.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.maimob.server.data.task.CreateBill;
import com.maimob.server.db.entity.Admin;
import com.maimob.server.db.entity.BalanceAccount;
import com.maimob.server.db.entity.Dictionary;
import com.maimob.server.db.entity.Proxy;
import com.maimob.server.db.entity.Reward;
import com.maimob.server.db.service.DaoService;
import com.maimob.server.utils.Cache;
import com.maimob.server.utils.StringUtils;

public class ActionLogic extends Logic {

	private DaoService dao;
	
	public ActionLogic(DaoService dao) {
		super(dao);
		this.dao = dao;
	}

	public ActionLogic( ) {
		super(null); 
	}
	
	
	public String getAction(String json)
	{
		String check = this.CheckJson(json);
		if(!StringUtils.isStrEmpty(check))
			return check;

		JSONObject whereJson = JSONObject.parseObject(json); 
 
		try {
			 Map<String,Map<String,String> > actionlist = od.getUserAction(whereJson);
			baseResponse.setActionList(actionlist);
			baseResponse.setStatus(0);
			baseResponse.setStatusMsg("");
		} catch (Exception e) {
			e.printStackTrace();
		} 
		String jsonstr = this.toJson();
		return jsonstr;
	}
	 
	
	
	
	
	
	
	
	
	

}
