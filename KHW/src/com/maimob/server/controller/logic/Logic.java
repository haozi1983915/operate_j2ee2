package com.maimob.server.controller.logic;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.alibaba.fastjson.JSONObject;
import com.maimob.server.db.entity.Admin;
import com.maimob.server.db.service.DaoService;
import com.maimob.server.importData.dao.OperateDao;
import com.maimob.server.protocol.BaseResponse;
import com.maimob.server.utils.Cache;
import com.maimob.server.utils.StringUtils;

public class Logic {

	BaseResponse baseResponse = new BaseResponse();
	OperateDao od = new OperateDao();
	public Logic(DaoService dao) {
		this.dao = dao;
	}
	private DaoService dao;
	 void setAdmin(Admin admin) {
		admin.setLoginDate(System.currentTimeMillis());
		Cache.AdminCatche(dao);
		Cache.updateAdminCatche(admin);
	}

	public String toJson()
	{
		this.close();
		return JSONObject.toJSONString(baseResponse);
	}
	public void close()
	{
		od.close();
	}

	 Admin getAdmin(String adminid) {
		Cache.AdminCatche(dao);
		if (StringUtils.isStrEmpty(adminid))
			return null;

		Admin admin = Cache.getAdminCatche(Long.parseLong(adminid));
		if (admin != null) {

			if (System.currentTimeMillis() - admin.getLoginDate() > 7200000) {
				admin = null;
			} else {
				admin.setLoginDate(System.currentTimeMillis());
			}
		}
		return admin;
	}
	 String adminid = "";
	public String CheckJson(String json)
	{
		BaseResponse baseResponse = new BaseResponse();
		if (StringUtils.isStrEmpty(json)) {
			baseResponse.setStatus(2);
			baseResponse.setStatusMsg("请求参数不合法");
			return JSONObject.toJSONString(baseResponse);
		}

		try {
			json = URLDecoder.decode(json, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		JSONObject whereJson = JSONObject.parseObject(json);
		adminid = whereJson.getString("sessionid");

		Admin admin = this.getAdmin(adminid);
		if (admin == null) {
			baseResponse.setStatus(1);
			baseResponse.setStatusMsg("请重新登录");
			return JSONObject.toJSONString(baseResponse);
		}
		return "";
	}
	
	
	


}
