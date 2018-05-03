package com.maimob.server.controller.logic;

import java.util.*;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.maimob.server.base.BasicPage;
import com.maimob.server.data.task.CreateBill;
import com.maimob.server.db.entity.Admin;
import com.maimob.server.db.entity.BalanceAccount;
import com.maimob.server.db.entity.Dictionary;
import com.maimob.server.db.entity.Proxy;
import com.maimob.server.db.entity.Reward;
import com.maimob.server.db.service.DaoService;
import com.maimob.server.importData.dao.OperateDao;
import com.maimob.server.utils.Cache;
import com.maimob.server.utils.ExportMapExcel;
import com.maimob.server.utils.StringUtils;

import javax.servlet.http.HttpServletResponse;

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
		JSONObject whereJson = JSONObject.parseObject(json);
		List<Map<String,String>> actionlist = od.getErrorSearchAction(whereJson);
		for (Map<String, String> map : actionlist) {
			for (String key : map.keySet()) {
			if (key.equals("page_name")){
				String value=map.get(key);
            if (value.equals("huizongPage"))actionMap.put("huizongPage","汇总页面");
            if (value.equals("jibenxingxiPage"))actionMap.put("jibenxingxiPage","基本信息页面");
            if (value.equals("loginPage"))actionMap.put("loginPage","登陆页面");
            if (value.equals("registerPage"))actionMap.put("registerPage","注册页面");
            if (value.equals("shenfenzhengPage"))actionMap.put("shenfenzhengPage","身份证页面");
            if (value.equals("yinhangkaPage"))actionMap.put("yinhangkaPage","银行卡页面");
			}
			}
		}
		baseResponse.setPageName(actionMap);
		String jsonstr = this.toJson();
		return jsonstr;
	}

	public void exportErrorPage(String json, HttpServletResponse response) {
		String check = this.CheckJson(json);
		if(!StringUtils.isStrEmpty(check))
			return ;
		JSONObject whereJson = JSONObject.parseObject(json);
		OperateDao od = new OperateDao();
		BasicPage<Map<String,String>> basicPage = od.getErrorAction(whereJson);
		List<Map<String,String>> reportforms = basicPage.getList();
			for (Map<String, String> map : reportforms) {
				for (String key : map.keySet()) {
					if (map.get(key) == null) {
						map.put(key, "' '");
					}
					if (key.equals("page_name")){
						String value=map.get(key);
						if (value.equals("huizongPage"))
							map.put(key,"汇总页");
						else if (value.equals("jibenxingxiPage"))
							map.put(key,"基本信息页");
						else if (value.equals("loginPage"))
							map.put(key,"登陆页");
						else  if (value.equals("registerPage"))
							map.put(key,"注册页");
						else if (value.equals("shenfenzhengPage"))
							map.put(key,"身份证页");
						else if (value.equals("yinhangkaPage"))
							map.put(key,"银行卡页");
					}
				}
			}
		if (reportforms.size()==0){
			return;
		}
		JSONArray arr = whereJson.getJSONArray("tag");
		List<String> listName = new ArrayList<>();
		listName.add("日期");
		listName.add("页面");
		listName.add("错误类型");
		listName.add("版本");
		listName.add("机型");
		listName.add("系统");
		listName.add("合计");

		List<String> listId = new ArrayList<>();
	    listId.add("date");
	    listId.add("page_name");
	    listId.add("error");
	    listId.add("app_version_name");
	    listId.add("model");
	    listId.add("platform");
	    listId.add("cou");
		ExportMapExcel exportExcelUtil = new ExportMapExcel();
		exportExcelUtil.exportExcelString("返回分析报表",listName,listId,reportforms,response);
	}

    public String getUserAction(String json) {
        String check = this.CheckJson(json);
        if(!StringUtils.isStrEmpty(check))
            return check;
        JSONObject whereJson = JSONObject.parseObject(json);
        try {
            BasicPage<Map<String,String> > actionlist = od.getUsersAction(whereJson);
            baseResponse.setBasicPage(actionlist);
            baseResponse.setStatus(0);
            baseResponse.setStatusMsg("");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String jsonstr = this.toJson();
        return jsonstr;
    }

	public String getUsersActionParam(String json) {
		String check = this.CheckJson(json);
		if(!StringUtils.isStrEmpty(check))
			return check;
		JSONObject whereJson = JSONObject.parseObject(json);
		try {
			BasicPage<Map<String,String> > actionlist = od.getUsersActionParam(whereJson);
			baseResponse.setBasicPage(actionlist);
			baseResponse.setStatus(0);
			baseResponse.setStatusMsg("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String jsonstr = this.toJson();
		return jsonstr;
	}

	public void exportUserAction(String json, HttpServletResponse response) {
		String check = this.CheckJson(json);
		if(!StringUtils.isStrEmpty(check))
			return ;
		JSONObject whereJson = JSONObject.parseObject(json);
		OperateDao od = new OperateDao();
		BasicPage<Map<String,String>> basicPage = od.getUsersAction(whereJson);
		List<Map<String,String>> reportforms = basicPage.getList();
		for (Map<String, String> map : reportforms) {
			for (String key : map.keySet()) {
				if (map.get(key) == null) {
					map.put(key, "");
				}
			}
		}
		if (reportforms.size()==0){
			return;
		}
		JSONArray arr = whereJson.getJSONArray("tag");
		List<String> listName = new ArrayList<>();
		listName.add("日期");
		listName.add("新注册");
		listName.add("类别");

		List<String> listId = new ArrayList<>();
		listId.add("date");
		listId.add("cou");
		listId.add("name");
		ExportMapExcel exportExcelUtil = new ExportMapExcel();
		exportExcelUtil.exportExcelString("用户分析报表",listName,listId,reportforms,response);
	}
}
