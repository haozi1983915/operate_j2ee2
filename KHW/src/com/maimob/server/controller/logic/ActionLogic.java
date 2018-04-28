package com.maimob.server.controller.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.maimob.server.base.BasicPage;
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


	public String getYamTimeAction(String json) {
		String check = this.CheckJson(json);
		if(!StringUtils.isStrEmpty(check))
			return check;

		JSONObject whereJson = JSONObject.parseObject(json);
		try {
			List<Map<String,String> > actionlist = od.getYamTimeAction(whereJson);
			baseResponse.setActionYamList(actionlist);
			baseResponse.setStatus(0);
			baseResponse.setStatusMsg("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String jsonstr = this.toJson();
		return jsonstr;
	}

    public String getPageTimeAction(String json) {
        String check = this.CheckJson(json);
        if(!StringUtils.isStrEmpty(check))
            return check;

        JSONObject whereJson = JSONObject.parseObject(json);
        try {
            List<Map<String,String>> actionlist = od.getPageTimeAction(whereJson);
            baseResponse.setActionYamList(actionlist);
            baseResponse.setStatus(0);
            baseResponse.setStatusMsg("");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String jsonstr = this.toJson();
        return jsonstr;
    }

	public String getPageErrorAction(String json) {
		String check = this.CheckJson(json);
		if(!StringUtils.isStrEmpty(check))
			return check;
		JSONObject whereJson = JSONObject.parseObject(json);
		try {
			BasicPage<Map<String,String> > actionlist = od.getErrorAction(whereJson);
			baseResponse.setListSize(actionlist.getPageSize()+" ");
			baseResponse.setActionYamList(actionlist.getList());
			baseResponse.setStatus(0);
			baseResponse.setStatusMsg("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String jsonstr = this.toJson();
		return jsonstr;
	}
	public String getPageErrorSearchAction(String json) {
		String check = this.CheckJson(json);
		if(!StringUtils.isStrEmpty(check))
			return check;
		List<Dictionary> appList = Cache.getDicList(1);
		baseResponse.setAppList(appList);
		HashMap<String,String> actionMap=new HashMap<>();
		actionMap.put("huizongPage","汇总页面");
		actionMap.put("jibenxingxiPage","基本信息页面");
		actionMap.put("loginPage","登陆页面");
		actionMap.put("registerPage","注册页面");
		actionMap.put("shenfenzhengPage","身份证页面");
		actionMap.put("yinhangkaPage","银行卡页面");
		baseResponse.setPageName(actionMap);
		String jsonstr = this.toJson();
		return jsonstr;
	}

}
