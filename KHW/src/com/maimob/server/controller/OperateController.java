package com.maimob.server.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.maimob.server.db.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.maimob.server.controller.logic.ConclusionLogic;
import com.maimob.server.controller.logic.FinanceLogic;
import com.maimob.server.controller.logic.PartnerBillLogic;
import com.maimob.server.data.task.TaskLine;
import com.maimob.server.db.daoImpl.DaoWhere;

import com.maimob.server.db.service.DaoService;
import com.maimob.server.db.service.SMSRecordService;
import com.maimob.server.finance.FinanceTask;
import com.maimob.server.importData.dao.OperateDao;
import com.maimob.server.protocol.BaseResponse;
import com.maimob.server.utils.AppTools;
import com.maimob.server.utils.Cache;
import com.maimob.server.utils.ExportMapExcel;
import com.maimob.server.utils.Mail;
import com.maimob.server.utils.PWDUtils;
import com.maimob.server.utils.StringUtils;

@Controller
@RequestMapping("/op")
public class OperateController extends BaseController {

	// 注入service
	@Autowired
	private SMSRecordService smsRecordService;

	@Autowired
	private DaoService dao;

	@RequestMapping(value = "/index", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String index(HttpServletRequest request, HttpServletResponse response) {

		return "thinks for visit";

	}

	public static void main(String[] args) {
		long id = System.currentTimeMillis();
		String pp = PWDUtils.encryptMD5AndBase64("123456");
		System.out.println(id + "  " + pp);
	}

	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/addAdmin", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String addAdmin(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("addAdmin");

		BaseResponse baseResponse = new BaseResponse();

		String json = this.checkParameter(request);

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

		JSONObject jobj = JSONObject.parseObject(json);
		String adminid = jobj.getString("sessionid");

		Admin admin = this.getAdmin(adminid);
		if (admin == null) {
			baseResponse.setStatus(1);
			baseResponse.setStatusMsg("请重新登录");
			return JSONObject.toJSONString(baseResponse);
		}

		Admin otheradmin = JSONObject.parseObject(json, Admin.class);

		String statusMsg = "";
		int status = 1;
		String check = otheradmin.check();
		if (check.equals("")) {
			long id = otheradmin.getId();
			otheradmin.setDate(System.currentTimeMillis());
			dao.saveAdmin(otheradmin);

			Cache.updateAdminCatche(otheradmin);
			baseResponse.setId(id);
			statusMsg = "添加账号成功";
			status = 0;
		} else {
			statusMsg = check;
		}

		baseResponse.setStatus(status);
		baseResponse.setStatusMsg(statusMsg);
		String content = JSONObject.toJSONString(baseResponse);
		logger.debug("register content = {}", content);
		return content;
	}

	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String login(HttpServletRequest request, HttpServletResponse response) {

		logger.debug("login");

		BaseResponse baseResponse = new BaseResponse();

		String json = this.checkParameter(request);

		if (StringUtils.isStrEmpty(json)) {
			baseResponse.setStatus(2);
			baseResponse.setStatusMsg("请求参数不合法");
			return JSONObject.toJSONString(baseResponse);
		}

		JSONObject jobj = JSONObject.parseObject(json);

		String email = jobj.getString("email");
		String pwd = jobj.getString("pwd");
		String md5Pwd = PWDUtils.encryptMD5AndBase64(pwd);

		int status = 1;
		String statusMsg = "";
		List<Admin> as = dao.findAdminByEmail(email);

		if (as == null || as.size() == 0) {
			status = 1;
			statusMsg = "用户名或密码错误";
		} else {
			Admin admin = as.get(0);
			String md5Pwd2 = PWDUtils.encryptMD5AndBase64(as.get(0).getPwd());

			if (md5Pwd2.equals(md5Pwd)) {
				admin.setPwd(null);
				status = 0;
				admin.setLoginDate(System.currentTimeMillis());
				setAdmin(admin);
				baseResponse.setId(admin.getId());
				baseResponse.setAdmin(admin);
				baseResponse.setSessionid(admin.getId());
			} else {
				status = 1;
				statusMsg = "用户名或密码错误";
			}
		}

		baseResponse.setStatus(status);
		baseResponse.setStatusMsg(statusMsg);
		String content = JSONObject.toJSONString(baseResponse);
		logger.debug("register content = {}", content);
		return content;
	}

	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/getAdmin", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getAdmin(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("getAdmin");

		BaseResponse baseResponse = new BaseResponse();

		String json = this.checkParameter(request);

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

		if (!json.equals("")) {

			JSONObject jobj = JSONObject.parseObject(json);
			String adminid = jobj.getString("sessionid");

			Admin admin = this.getAdmin(adminid);
			if (admin == null) {
				baseResponse.setStatus(1);
				baseResponse.setStatusMsg("请重新登录");
				return JSONObject.toJSONString(baseResponse);
			}

			try {
				json = URLDecoder.decode(json, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			int first = Integer.parseInt(jobj.getString("first"));
			if (first == 0) {
				long listSize = dao.findAdminCou(json);
				baseResponse.setListSize(listSize + "");
			}

			List<Admin> admins = dao.findAdmin(json);
			baseResponse.setAdminList(admins);
		}

		baseResponse.setStatus(0);
		String content = JSONObject.toJSONString(baseResponse);
		logger.debug("register content = {}", content);
		return content;
	}

	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/getChannel", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getChannel(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("getChannel");
		BaseResponse baseResponse = new BaseResponse();

		String json = this.checkParameter(request);

		if (StringUtils.isStrEmpty(json)) {
			baseResponse.setStatus(2);
			baseResponse.setStatusMsg("请求参数不合法");
			return JSONObject.toJSONString(baseResponse);
		}

		Cache.channelCatche(dao);
		JSONObject jobj = JSONObject.parseObject(json);
		String adminid = jobj.getString("sessionid");

		Admin admin = this.getAdmin(adminid);
		if (admin == null) {
			baseResponse.setStatus(1);
			baseResponse.setStatusMsg("请重新登录");
			return JSONObject.toJSONString(baseResponse);
		}

		String proxyId = jobj.getString("proxyId");

		jobj.put("proxyId", proxyId);

		int level = admin.getLevel();
		List<Channel> channels = null;

		int first = 1;

		try {
			first = Integer.parseInt(jobj.getString("first"));
			baseResponse.setListSize(0 + "");
			if (first == 0) {
				long listSize = dao.findChannelCouByProxyId(jobj);
				baseResponse.setListSize(listSize + "");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		channels = dao.findChannelByProxyId(jobj);
		baseResponse.setChannelList(channels);
		baseResponse.setStatus(0);
		baseResponse.setStatusMsg("");
		String content = JSONObject.toJSONString(baseResponse);
		logger.debug("register content = {}", content);
		return content;
	}


	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/getAppList", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getAppList(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("getAppList");
		BaseResponse baseResponse = new BaseResponse();

		String json = this.checkParameter(request);

		if (StringUtils.isStrEmpty(json)) {
			baseResponse.setStatus(2);
			baseResponse.setStatusMsg("请求参数不合法");
			return JSONObject.toJSONString(baseResponse);
		}

		JSONObject jobj = JSONObject.parseObject(json);
		String adminid = jobj.getString("sessionid");

		Admin admin = this.getAdmin(adminid);
		if (admin == null) {
			baseResponse.setStatus(1);
			baseResponse.setStatusMsg("请重新登录");
			return JSONObject.toJSONString(baseResponse);
		}

		StringBuffer where = new StringBuffer();
		where.append(" 1 = 1 ");
		String otheradminId = "";
		if (!json.equals("")) {

			try {
				json = URLDecoder.decode(json, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			JSONObject whereJson = JSONObject.parseObject(json);

			otheradminId = whereJson.getString("adminId");
			if (!StringUtils.isStrEmpty(otheradminId)) {
				where.append(" and adminId = " + otheradminId + " ");
			}

		}

		Cache.DicCatche(dao);
		List<Dictionary> dic1 = Cache.getDicList(1);
		baseResponse.setAppList(dic1);
 
		baseResponse.setStatus(0);
		baseResponse.setStatusMsg("");
		String content = JSONObject.toJSONString(baseResponse);
		logger.debug("register content = {}", content);
		return content;
	}

	
	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/getChannelParameter", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getChannelParameter(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("getChannelParameter");
		BaseResponse baseResponse = new BaseResponse();

		String json = this.checkParameter(request);

		if (StringUtils.isStrEmpty(json)) {
			baseResponse.setStatus(2);
			baseResponse.setStatusMsg("请求参数不合法");
			return JSONObject.toJSONString(baseResponse);
		}

		JSONObject jobj = JSONObject.parseObject(json);
		String adminid = jobj.getString("sessionid");

		Admin admin = this.getAdmin(adminid);
		if (admin == null) {
			baseResponse.setStatus(1);
			baseResponse.setStatusMsg("请重新登录");
			return JSONObject.toJSONString(baseResponse);
		}

		Cache.channelCatche(dao);
		String proxyId = jobj.getString("proxyId");
		jobj.put("proxyId", proxyId);
		List<Channel> channels = dao.findChannelByProxyId(jobj);

		ArrayList<String> channelNameList = new ArrayList<String>();
		ArrayList<String> channelNoList = new ArrayList<String>();
		ArrayList<String> adminIdList = new ArrayList<String>();

		Map<String,String>  channelMap = new HashMap<String,String> ();
		for (int i = 0; i < channels.size(); i++) {
			Channel channel = channels.get(i);
			if (channel.getLevel() == 1) {
				if(channelMap.get(channel.getChannelName()) == null)
				channelNameList.add(channel.getChannelName());
				if(channelMap.get(channel.getChannel()) == null)
				channelNoList.add(channel.getChannel());
			} else if (channel.getLevel() == 2) {
				if(channelMap.get(channel.getChannelName()) == null)
				channelNameList.add("--" + channel.getChannelName());
				if(channelMap.get(channel.getChannel()) == null)
				channelNoList.add("--" + channel.getChannel());
			}

			Admin admin1 = Cache.getAdminCatche(channel.getAdminId());
			if (admin1 != null)
				adminIdList.add(admin1.getId() + "," + admin1.getName());

		}

		// AppTools.removeDuplicate(channelNameList);
		// AppTools.removeDuplicate(channelNoList);
		AppTools.removeDuplicate(adminIdList);
		baseResponse.setChannelNameList(channelNameList);
		baseResponse.setChannelNoList(channelNoList);
		baseResponse.setAdminIdList(adminIdList);
		baseResponse.setStatus(0);
		baseResponse.setStatusMsg("");
		String content = JSONObject.toJSONString(baseResponse);
		logger.debug("register content = {}", content);
		return content;
	}

	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/getChannelValue", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getChannelValue(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("getChannelValue");
		BaseResponse baseResponse = new BaseResponse();

		String json = this.checkParameter(request);

		if (StringUtils.isStrEmpty(json)) {
			baseResponse.setStatus(2);
			baseResponse.setStatusMsg("请求参数不合法");
			return JSONObject.toJSONString(baseResponse);
		}

		JSONObject jobj = JSONObject.parseObject(json);
		String adminid = jobj.getString("sessionid");

		Admin admin = this.getAdmin(adminid);
		if (admin == null) {
			baseResponse.setStatus(1);
			baseResponse.setStatusMsg("请重新登录");
			return JSONObject.toJSONString(baseResponse);
		}

		StringBuffer where = new StringBuffer();
		where.append(" 1 = 1 ");
		String otheradminId = "";
		if (!json.equals("")) {

			try {
				json = URLDecoder.decode(json, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			JSONObject whereJson = JSONObject.parseObject(json);

			otheradminId = whereJson.getString("adminId");
			if (!StringUtils.isStrEmpty(otheradminId)) {
				where.append(" and adminId = " + otheradminId + " ");
			}

		}

		ArrayList<String> adminIdList = new ArrayList<String>();

		int level = admin.getLevel();
		List<Channel> channels = null;
		// long []ids = new long[0];

		if (level > 1) {
			if (level == 2) {
				List<Admin> ads = dao.findAdminByHigherid(admin.getId());
				// ids = new long[ads.size()+1];
				for (int i = 0; i < ads.size(); i++) {
					Admin admin1 = ads.get(i);
					// ids[i] = admin1.getId();
					adminIdList.add(admin1.getId() + "," + admin1.getName() + "," + admin1.getLevel());
				}
				// ids[ids.length-1] = admin.getId();
				adminIdList.add(admin.getId() + "," + admin.getName() + "," + admin.getLevel());
			} else if (level == 3) {
				// ids = new long[1];
				// ids[ids.length-1] = admin.getId();
				adminIdList.add(admin.getId() + "," + admin.getName() + "," + admin.getLevel());
			}
		} else {
			List<Admin> ads = dao.findAdminByDepartmentId(admin.getDepartmentId());
			for (int i = 0; i < ads.size(); i++) {
				Admin admin1 = ads.get(i);
				adminIdList.add(admin1.getId() + "," + admin1.getName() + "," + admin1.getLevel());
			}
		}

		Cache.DicCatche(dao);
		List<Dictionary> dic1 = Cache.getDicList(1);
		List<Dictionary> dic3 = Cache.getDicList(3);
		List<Dictionary> dic4 = Cache.getDicList(4);
		List<Dictionary> dic5 = Cache.getDicList(5);
		List<Dictionary> dic6 = Cache.getDicList(6);
		List<Dictionary> dic7 = Cache.getDicList(7);
		List<Dictionary> dic8 = Cache.getDicList(8);

		baseResponse.setChannelAttribute(dic3);
		baseResponse.setChannelType(dic4);
		baseResponse.setChannelSubdivision(dic5);
		baseResponse.setAppList(dic1);
		baseResponse.setCostingList(dic7);
		baseResponse.setSettlementCycleList(dic6);
		baseResponse.setRewardTypeList(dic8);

		baseResponse.setAdminIdList(adminIdList);
		baseResponse.setStatus(0);
		baseResponse.setStatusMsg("");
		String content = JSONObject.toJSONString(baseResponse);
		logger.debug("register content = {}", content);
		return content;
	}

	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/setChannelAttribute", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String setChannelAttribute(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("setChannelAttribute");
		BaseResponse baseResponse = new BaseResponse();

		String json = this.checkParameter(request);

		if (StringUtils.isStrEmpty(json)) {
			baseResponse.setStatus(2);
			baseResponse.setStatusMsg("请求参数不合法");
			return JSONObject.toJSONString(baseResponse);
		}

		JSONObject jobj = JSONObject.parseObject(json);
		String adminid = jobj.getString("sessionid");

		Admin admin = this.getAdmin(adminid);
		if (admin == null) {
			baseResponse.setStatus(1);
			baseResponse.setStatusMsg("请重新登录");
			return JSONObject.toJSONString(baseResponse);
		}
		Cache.channelCatche(dao);
		String channelid = jobj.getString("channelId");
		String status = jobj.getString("status");
		dao.updateChannelStuts(Long.parseLong(channelid), Integer.parseInt(status));
        List<Channel> channels = dao.findChannelByChannelId(channelid);
        if(!CollectionUtils.isEmpty(channels)){
            Date date = new Date();
            for(Channel channel : channels){
                OperateChannelHistory channelHistory = new OperateChannelHistory();
                channelHistory.setChannelId(channel.getId());
                channelHistory.setUpdateBy(admin.getId());
                channelHistory.setUpdateDate(date);
                if("0".equals(status)){
                    channelHistory.setLog(admin.getName()+"禁用了"+channel.getChannelName());
                }else{
                    channelHistory.setLog(admin.getName()+"启用了"+channel.getChannelName());
                }
                dao.saveChannelHistory(channelHistory);
            }
        }

		String content = JSONObject.toJSONString(baseResponse);
		logger.debug("register content = {}", content);
		return content;
	}

	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/getChannelAttribute", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getChannelAttribute(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("getChannelAttribute");
		BaseResponse baseResponse = new BaseResponse();

		String json = this.checkParameter(request);

		if (StringUtils.isStrEmpty(json)) {
			baseResponse.setStatus(2);
			baseResponse.setStatusMsg("请求参数不合法");
			return JSONObject.toJSONString(baseResponse);
		}

		JSONObject jobj = JSONObject.parseObject(json);
		String adminid = jobj.getString("sessionid");

		Admin admin = this.getAdmin(adminid);
		if (admin == null) {
			baseResponse.setStatus(1);
			baseResponse.setStatusMsg("请重新登录");
			return JSONObject.toJSONString(baseResponse);
		}
        List<Map<String, String>> reportform = null;
        OperateDao od=new OperateDao();
		try {
	        reportform = od.getReportform(null, jobj,"");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			od.close();
		}  
        baseResponse.setReportform_mainChannel(reportform);
		Cache.DicCatche(dao);
		List<Dictionary> dic3 = Cache.getDicList(3);
		List<Dictionary> dic4 = Cache.getDicList(4);
		List<Dictionary> dic5 = Cache.getDicList(5);
		List<Dictionary> dic8 = Cache.getDicList(8);
		List<Dictionary> dic1 = Cache.getDicList(1);
		baseResponse.setAppList(dic1);
		baseResponse.setChannelAttribute(dic3);
		baseResponse.setChannelType(dic4);
		baseResponse.setChannelSubdivision(dic5);
		baseResponse.setRewardTypeList(dic8);
		baseResponse.setStatus(0);
		baseResponse.setStatusMsg("");
		od.close();
		String content = JSONObject.toJSONString(baseResponse);
		logger.debug("register content = {}", content);
		return content;
	}

	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/saveChannelAttribute", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String saveChannelAttribute(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("saveChannelAttribute");
		BaseResponse baseResponse = new BaseResponse();

		String json = this.checkParameter(request);

		if (StringUtils.isStrEmpty(json)) {
			baseResponse.setStatus(2);
			baseResponse.setStatusMsg("请求参数不合法");
			return JSONObject.toJSONString(baseResponse);
		}

		JSONObject jobj = JSONObject.parseObject(json);
		String adminid = jobj.getString("sessionid");

		Admin admin = this.getAdmin(adminid);
		if (admin == null) {
			baseResponse.setStatus(1);
			baseResponse.setStatusMsg("请重新登录");
			return JSONObject.toJSONString(baseResponse);
		}

		Dictionary dic = JSONObject.parseObject(json, Dictionary.class);
		dic.getId();
		String msg = "";
		if (StringUtils.isStrEmpty(dic.getName())) {
			msg = "名称不能为空";
		}

		if (dic.getType() != 3 && dic.getHigherId() == 0) {
			msg = "上层id不能为空";
		}

		if (dic.getType() == 0) {
			msg = "类别不能为空";
		}
		if (msg.equals("")) {
			dao.saveDictionary(dic);
			Cache.updateDicList(dic);
			baseResponse.setStatus(0);
			baseResponse.setStatusMsg("保存成功");
		} else {
			baseResponse.setStatus(1);
			baseResponse.setStatusMsg(msg);
		}

		String content = JSONObject.toJSONString(baseResponse);
		logger.debug("register content = {}", content);
		return content;
	}

	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/getProxy", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getProxy(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("getProxy");
		BaseResponse baseResponse = new BaseResponse();

		String json = this.checkParameter(request);

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
		String adminid = whereJson.getString("sessionid");

		Admin admin = this.getAdmin(adminid);
		if (admin == null) {
			baseResponse.setStatus(1);
			baseResponse.setStatusMsg("请重新登录");
			return JSONObject.toJSONString(baseResponse);
		}

		List<Proxy> proxys = null;

		int first = 1;

		try {
			first = Integer.parseInt(whereJson.getString("first"));
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (first == 0) {
			long listSize = dao.findProxyCou(whereJson);
			baseResponse.setListSize(listSize + "");
		}

		proxys = dao.findAllProxy(whereJson);

		baseResponse.setProxyList(proxys);
		baseResponse.setStatus(0);
		baseResponse.setStatusMsg("");
		String content = JSONObject.toJSONString(baseResponse);
		logger.debug("register content = {}", content);
		return content;
	}

	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/getProxyNameList", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getProxyNameList(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("getProxyNameList");
		BaseResponse baseResponse = new BaseResponse();

		String json = this.checkParameter(request);

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
		String adminid = whereJson.getString("sessionid");

		Admin admin = this.getAdmin(adminid);
		if (admin == null) {
			baseResponse.setStatus(1);
			baseResponse.setStatusMsg("请重新登录");
			return JSONObject.toJSONString(baseResponse);
		}

		List<Proxy> proxys = dao.findProxyName();

		baseResponse.setProxyList(proxys);
		baseResponse.setStatus(0);
		baseResponse.setStatusMsg("");
		String content = JSONObject.toJSONString(baseResponse);
		logger.debug("register content = {}", content);
		return content;
	}

	
	
	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/getOptimization", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getOptimization(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("getOptimization");
		BaseResponse baseResponse = new BaseResponse();

		String json = this.checkParameter(request);

		if (StringUtils.isStrEmpty(json)) {
			baseResponse.setStatus(2);
			baseResponse.setStatusMsg("请求参数不合法");
			return JSONObject.toJSONString(baseResponse);
		}

		JSONObject jobj = JSONObject.parseObject(json);
		String adminid = jobj.getString("sessionid");

		Admin admin = this.getAdmin(adminid);
		if (admin == null) {
			baseResponse.setStatus(1);
			baseResponse.setStatusMsg("请重新登录");
			return JSONObject.toJSONString(baseResponse);
		}

		List<Optimization> optimizationList = null;
		if (!json.equals("")) {
			try {
				json = URLDecoder.decode(json, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			JSONObject whereJson = JSONObject.parseObject(json);
			String channelId = whereJson.getString("channelId");
			String id = whereJson.getString("optimizationId");
			if (!StringUtils.isStrEmpty(id)) {
				optimizationList = dao.findAllOptimizationById(id);
			} else if (!StringUtils.isStrEmpty(channelId)) {
				optimizationList = dao.findAllOptimizationByChannelId(channelId);
			}

		}

		baseResponse.setOptimizationList(optimizationList);
		;
		baseResponse.setStatus(0);
		baseResponse.setStatusMsg("");
		String content = JSONObject.toJSONString(baseResponse);
		logger.debug("register content = {}", content);
		return content;
	}

	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/addOptimization", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String addOptimization(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("addOptimization");
		BaseResponse baseResponse = new BaseResponse();

		String json = this.checkParameter(request);

		if (StringUtils.isStrEmpty(json)) {
			baseResponse.setStatus(2);
			baseResponse.setStatusMsg("请求参数不合法");
			return JSONObject.toJSONString(baseResponse);
		}

		JSONObject jobj = JSONObject.parseObject(json);
		String adminid = jobj.getString("sessionid");
		String channelId = jobj.getString("channelId");
		String synchronous = jobj.getString("synchronous");
		String proxyId = jobj.getString("proxyId");
		String linkage = jobj.getString("linkage");

		Admin admin = this.getAdmin(adminid);
		if (admin == null) {
			baseResponse.setStatus(1);
			baseResponse.setStatusMsg("请重新登录");
			return JSONObject.toJSONString(baseResponse);
		}
		Optimization optimization = JSONObject.parseObject(json, Optimization.class);

		String statusMsg = "";
		int status = 2;

		boolean issave = true;
		if (optimization.getId() != 0) {
			List<Optimization> oplist1 = dao.findAllOptimizationByChannelIdLast(channelId);
			if (oplist1.size() > 0) {
				Optimization op = oplist1.get(0);

				if (op.getStartDate() == optimization.getStartDate()
						&& op.getOptimization() == optimization.getOptimization()) {
					optimization = op;
					issave = false;
				}

			}

		}

		long optimizationid = optimization.getId();

		if (issave) {
			dao.saveOptimization(optimization);
			long channelid = Long.parseLong(channelId);
			dao.updateChannelSynchronous(channelid, Integer.parseInt(synchronous));
			dao.updateChannelOptimization_startDate(optimizationid, channelid, optimization.getOptimization(),
					optimization.getStartDate());
		}
		Cache.channelCatche(dao);
		if (linkage.equals("1")) {
			List<Channel> cs = dao.findChannelByProxyId(proxyId);
			for (int i = 0; i < cs.size(); i++) {
				Channel channel = cs.get(i);
				long otherChannelid = channel.getId();

				long channelid = Long.parseLong(channelId);
				if(otherChannelid == channelid)
					continue;
				
				
				optimization.setChannelId(otherChannelid);
				optimization.setId(0);
				long otherOptimizationid = optimization.getId();
				dao.saveOptimization(optimization);
				dao.updateChannelOptimization_startDate(otherOptimizationid, otherChannelid,
						optimization.getOptimization(), optimization.getStartDate());
			}

		}

		baseResponse.setStatus(0);
		baseResponse.setStatusMsg("");
		String content = JSONObject.toJSONString(baseResponse);
		logger.debug("register content = {}", content);
		return content;
	}
 
	
	

	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/getReward", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getReward(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("getReward");
		BaseResponse baseResponse = new BaseResponse();

		String json = this.checkParameter(request);

		if (StringUtils.isStrEmpty(json)) {
			baseResponse.setStatus(2);
			baseResponse.setStatusMsg("请求参数不合法");
			return JSONObject.toJSONString(baseResponse);
		}

		JSONObject jobj = JSONObject.parseObject(json);
		String adminid = jobj.getString("sessionid");

		Admin admin = this.getAdmin(adminid);
		if (admin == null) {
			baseResponse.setStatus(1);
			baseResponse.setStatusMsg("请重新登录");
			return JSONObject.toJSONString(baseResponse);
		}

//		List<List<Reward>> rewardList = new ArrayList<List<Reward>>();
		List<Reward> rewardList = new ArrayList<Reward>();
		if (!json.equals("")) {
			try {
				json = URLDecoder.decode(json, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			JSONObject whereJson = JSONObject.parseObject(json);
			String channelId = whereJson.getString("channelId");
			String appid = whereJson.getString("appid");
			String id = whereJson.getString("rewardId");
			if (!StringUtils.isStrEmpty(id)) {
				rewardList = dao.findRewardById(Long.parseLong(id));
			} else if (!StringUtils.isStrEmpty(channelId)) {
				if(!StringUtils.isStrEmpty(appid))
				{
					String[] appids = appid.split(",");
					for(String app:appids)
					{
						List<Reward> rewardList2 = getReward(dao.findRewardByChannelId(Long.parseLong(channelId),Long.parseLong(app)));
						rewardList.addAll(rewardList2);
					}
				}
				else
				{
					rewardList = getRewardHistory(dao.findRewardByChannelId(Long.parseLong(channelId)));
					
				}
				

			}

		}

		baseResponse.setRewardList(rewardList);
		baseResponse.setStatus(0);
		baseResponse.setStatusMsg("");
		String content = JSONObject.toJSONString(baseResponse);
		logger.debug("register content = {}", content);
		return content;
	}

	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/getPayCompany", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getPayCompany(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("getPayCompany");
		BaseResponse baseResponse = new BaseResponse();
		String json = this.checkParameter(request);

		if (StringUtils.isStrEmpty(json)) {
			baseResponse.setStatus(2);
			baseResponse.setStatusMsg("请求参数不合法");
			return JSONObject.toJSONString(baseResponse);
		}

		JSONObject jobj = JSONObject.parseObject(json);
		String adminid = jobj.getString("sessionid");

		Admin admin = this.getAdmin(adminid);
		if (admin == null) {
			baseResponse.setStatus(1);
			baseResponse.setStatusMsg("请重新登录");
			return JSONObject.toJSONString(baseResponse);
		}

		 List<operate_pay_company> payCompanyList = null;
		if (!json.equals("")) {
			try {
				json = URLDecoder.decode(json, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			JSONObject whereJson = JSONObject.parseObject(json);
			String proxyid = whereJson.getString("proxyid");
			if (!StringUtils.isStrEmpty(proxyid)) {
				payCompanyList = dao.findPayCompanyByProxyId(proxyid);
			}
		}

		baseResponse.setPayCompanyList(payCompanyList);
		baseResponse.setStatus(0);
		baseResponse.setStatusMsg("");
		String content = JSONObject.toJSONString(baseResponse);
		logger.debug("register content = {}", content);
		return content;
	}
 
	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/getRewardMain", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getRewardMain(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("getRewardMain");
		BaseResponse baseResponse = new BaseResponse();

		String json = this.checkParameter(request);

		if (StringUtils.isStrEmpty(json)) {
			baseResponse.setStatus(2);
			baseResponse.setStatusMsg("请求参数不合法");
			return JSONObject.toJSONString(baseResponse);
		}

		JSONObject jobj = JSONObject.parseObject(json);
		String adminid = jobj.getString("sessionid");

		Admin admin = this.getAdmin(adminid);
		if (admin == null) {
			baseResponse.setStatus(1);
			baseResponse.setStatusMsg("请重新登录");
			return JSONObject.toJSONString(baseResponse);
		}

		Map<String,List<Reward>> mainReward = new HashMap<String,List<Reward>>();
//		List<Reward> rewardList = new ArrayList<Reward>();
		if (!json.equals("")) {
			try {
				json = URLDecoder.decode(json, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			JSONObject whereJson = JSONObject.parseObject(json);
			String channelId = whereJson.getString("channelId");
			String appid = whereJson.getString("appid");
			
			if (!StringUtils.isStrEmpty(channelId)) {
				if(!StringUtils.isStrEmpty(appid))
				{
					String[] appids = appid.split(",");
					for(String app:appids)
					{
						List<Reward> rewardList2 = getRewardMain(dao.findRewardByChannelId(Long.parseLong(channelId),Long.parseLong(app)));
						mainReward.put(app, rewardList2);
					}
				}
			}
		}

		baseResponse.setMainReward(mainReward);
		baseResponse.setStatus(0);
		baseResponse.setStatusMsg("");
		String content = JSONObject.toJSONString(baseResponse);
		logger.debug("register content = {}", content);
		return content;
	}

	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/addRewardMain", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String addRewardMain(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("addRewardMain");
		BaseResponse baseResponse = new BaseResponse();

		String json = this.checkParameter(request);

		if (StringUtils.isStrEmpty(json)) {
			baseResponse.setStatus(2);
			baseResponse.setStatusMsg("请求参数不合法");
			return JSONObject.toJSONString(baseResponse);
		}

		JSONObject jobj = JSONObject.parseObject(json);
		String adminid = jobj.getString("sessionid");

		Admin admin = this.getAdmin(adminid);
		if (admin == null) {
			baseResponse.setStatus(1);
			baseResponse.setStatusMsg("请重新登录");
			return JSONObject.toJSONString(baseResponse);
		}

		Channel channel = JSONObject.parseObject(json, Channel.class);
		Map<String,List<Reward>> mainReward = channel.getMainReward();
		for (String key : mainReward.keySet()) {
			List<Reward> Rewards = mainReward.get(key);
			 dao.saveRewardMain(Rewards);
		}
		
		baseResponse.setStatus(0);
		baseResponse.setStatusMsg("");
		String content = JSONObject.toJSONString(baseResponse);
		logger.debug("register content = {}", content);
		return content;
	}
	

	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/getMainChannel", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getMainChannel(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("getMainChannel");
		BaseResponse baseResponse = new BaseResponse();

		String json = this.checkParameter(request);

		if (StringUtils.isStrEmpty(json)) {
			baseResponse.setStatus(2);
			baseResponse.setStatusMsg("请求参数不合法");
			return JSONObject.toJSONString(baseResponse);
		}

		JSONObject jobj = JSONObject.parseObject(json);
		String adminid = jobj.getString("sessionid");

		Admin admin = this.getAdmin(adminid);
		if (admin == null) {
			baseResponse.setStatus(1);
			baseResponse.setStatusMsg("请重新登录");
			return JSONObject.toJSONString(baseResponse);
		}
		

		String proxyid = jobj.getString("proxyid");
		List<Channel> cs = dao.findMainByProxyId(proxyid);
		String mainChannel = "";
		if(cs != null && cs.size()>0)
			mainChannel = cs.get(0).getChannel();
		baseResponse.setMainChannel(mainChannel);
		baseResponse.setStatus(0);
		baseResponse.setStatusMsg("");
		String content = JSONObject.toJSONString(baseResponse);
		logger.debug("register content = {}", content);
		return content;
	}
	
	
	
	
	
	private List<Reward> getRewardHistory(List<Reward> rewardList )
	{
		List<Reward> rewardList2 = new ArrayList<Reward>();
		if(rewardList != null)
		{
			for (int j = 0; j < rewardList.size(); j++) {
				Reward reward = rewardList.get(j);
				Admin admin1 = Cache.getAdminCatche(reward.getAdminId());
				if (admin1 != null)
					reward.setAdminName(admin1.getName());

				Admin UpdateAdmin = Cache.getAdminCatche(reward.getUpdateAdminId());
				if (UpdateAdmin != null)
					reward.setUpdateAdminName(UpdateAdmin.getName());

				rewardList2.add(reward);
				String rewardPrice = "";
				long rewardTypeId = reward.getTypeId();

				if (rewardTypeId == 26) {
					rewardPrice += reward.getMax() + "/" + reward.getPrice() + "元";
					reward.setRewardPrice(rewardPrice);
				} else {
					rewardPrice += reward.getMax() + "/" + reward.getPrice() + "%";
					reward.setRewardPrice(rewardPrice);
				}

				for (int i = j + 1; i < rewardList.size(); i++) {
					Reward reward1 = rewardList.get(i);
					if (reward.getId() == reward1.getId()) {
						rewardPrice += ",";

						if (rewardTypeId == 26) {
							rewardPrice += reward1.getMax() + "/" + reward1.getPrice() + "元";
							reward.setRewardPrice(rewardPrice);
						} else {
							rewardPrice += reward1.getMax() + "/" + reward1.getPrice() + "%";
							reward.setRewardPrice(rewardPrice);
						}

					} else {
						break;
					}

					j = i;

				}
			}
		}
		
		return rewardList2;
	}
	

	private List<Reward> getReward(List<Reward> rewardList )
	{
		List<Reward> rewardList2 = new ArrayList<Reward>();
		if(rewardList != null)
		{
			for (int j = 0; j < rewardList.size(); j++) {
				Reward reward = rewardList.get(j);
				Admin admin1 = Cache.getAdminCatche(reward.getAdminId());
				if (admin1 != null)
					reward.setAdminName(admin1.getName());

				Admin UpdateAdmin = Cache.getAdminCatche(reward.getUpdateAdminId());
				if (UpdateAdmin != null)
					reward.setUpdateAdminName(UpdateAdmin.getName());

				rewardList2.add(reward);
				String rewardPrice = "";
				long rewardTypeId = reward.getTypeId();

				if (rewardTypeId == 26) {
					rewardPrice += reward.getMax() + "/" + reward.getPrice() + "元";
					reward.setRewardPrice(rewardPrice);
				} else {
					rewardPrice += reward.getMax() + "/" + reward.getPrice() + "%";
					reward.setRewardPrice(rewardPrice);
				}

				for (int i = j + 1; i < rewardList.size(); i++) {
					Reward reward1 = rewardList.get(i);
					if (reward.getId() == reward1.getId()) {
						rewardPrice += ",";

						if (rewardTypeId == 26) {
							rewardPrice += reward1.getMax() + "/" + reward1.getPrice() + "元";
							reward.setRewardPrice(rewardPrice);
						} else {
							rewardPrice += reward1.getMax() + "/" + reward1.getPrice() + "%";
							reward.setRewardPrice(rewardPrice);
						}

					} else {
						return rewardList2;
					}

					j = i;

				}
			}
		}
		
		return rewardList2;
	}
	

	private List<Reward> getRewardMain(List<Reward> rewardList )
	{
		List<Reward> rewardList2 = new ArrayList<Reward>();
		if(rewardList != null)
		{
			for (int j = 0; j < rewardList.size(); j++) {
				Reward reward = rewardList.get(j);
				if(rewardList2.size() > 0 && rewardList2.get(j-1).getId() != reward.getId())
				{
					break;
				}
				rewardList2.add(reward);
			}
		}
		return rewardList2;
	}
	
	
	

	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/getChannelPermission", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getChannelPermission(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("getChannelPermission");
		BaseResponse baseResponse = new BaseResponse();
		String json = this.checkParameter(request);

		if (StringUtils.isStrEmpty(json)) {
			baseResponse.setStatus(2);
			baseResponse.setStatusMsg("请求参数不合法");
			return JSONObject.toJSONString(baseResponse);
		}

		JSONObject jobj = JSONObject.parseObject(json);
		String adminid = jobj.getString("sessionid");

		Admin admin = this.getAdmin(adminid);
		if (admin == null) {
			baseResponse.setStatus(1);
			baseResponse.setStatusMsg("请重新登录");
			return JSONObject.toJSONString(baseResponse);
		}

		List<ChannelPermission> channelPermissions = null;
		if (!json.equals("")) {
			try {
				json = URLDecoder.decode(json, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			JSONObject whereJson = JSONObject.parseObject(json);
			String proxyid = whereJson.getString("proxyid");
			String appids = whereJson.getString("appid");
			if (!StringUtils.isStrEmpty(proxyid)) {
				if(!StringUtils.isStrEmpty(appids))
				{
					channelPermissions = dao.findChannelPermissionByProxyId_appidList(proxyid,appids);
				}
				else
				{
					channelPermissions = dao.findChannelPermissionByProxyId(proxyid);
				}
				
			}

		}

		baseResponse.setChannelPermissionList(channelPermissions);
		baseResponse.setStatus(0);
		baseResponse.setStatusMsg("");
		String content = JSONObject.toJSONString(baseResponse);
		logger.debug("register content = {}", content);
		return content;
	}
	
	

	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/AdminParameter", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String AdminParameter(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("AdminParameter");
		BaseResponse baseResponse = new BaseResponse();
		String json = this.checkParameter(request);

		if (StringUtils.isStrEmpty(json)) {
			baseResponse.setStatus(2);
			baseResponse.setStatusMsg("请求参数不合法");
			return JSONObject.toJSONString(baseResponse);
		}

		JSONObject jobj = JSONObject.parseObject(json);
		String adminid = jobj.getString("sessionid");

		Admin admin = this.getAdmin(adminid);
		if (admin == null) {
			baseResponse.setStatus(1);
			baseResponse.setStatusMsg("请重新登录");
			return JSONObject.toJSONString(baseResponse);
		}

		List<Admin> admins = dao.findAdminByLevel_departmentId(admin.getLevel(), admin.getDepartmentId());
		baseResponse.setAdminList(admins);
		baseResponse.setStatus(0);
		baseResponse.setStatusMsg("");
		String content = JSONObject.toJSONString(baseResponse);
		logger.debug("register content = {}", content);
		return content;
	}

	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/checkEmail", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String checkEmail(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("checkEmail");
		BaseResponse baseResponse = new BaseResponse();
		String json = this.checkParameter(request);

		if (StringUtils.isStrEmpty(json)) {
			baseResponse.setStatus(2);
			baseResponse.setStatusMsg("请求参数不合法");
			return JSONObject.toJSONString(baseResponse);
		}

		JSONObject jobj = JSONObject.parseObject(json);
		String adminid = jobj.getString("sessionid");

		Admin admin = this.getAdmin(adminid);
		if (admin == null) {
			baseResponse.setStatus(1);
			baseResponse.setStatusMsg("请重新登录");
			return JSONObject.toJSONString(baseResponse);
		}

		String email = jobj.getString("email");

		long cou = dao.findCouByEmail(email);
		if (cou == 0) {
			baseResponse.setStatus(0);
			baseResponse.setStatusMsg("email可用");
		} else {
			baseResponse.setStatus(2);
			baseResponse.setStatusMsg("email已经存在！");
		}

		String content = JSONObject.toJSONString(baseResponse);
		logger.debug("register content = {}", content);
		return content;
	}

	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/checkMobileNo", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String checkMobileNo(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("checkMobileNo");
		BaseResponse baseResponse = new BaseResponse();
		String json = this.checkParameter(request);

		if (StringUtils.isStrEmpty(json)) {
			baseResponse.setStatus(2);
			baseResponse.setStatusMsg("请求参数不合法");
			return JSONObject.toJSONString(baseResponse);
		}

		JSONObject jobj = JSONObject.parseObject(json);
		String adminid = jobj.getString("sessionid");

		Admin admin = this.getAdmin(adminid);
		if (admin == null) {
			baseResponse.setStatus(1);
			baseResponse.setStatusMsg("请重新登录");
			return JSONObject.toJSONString(baseResponse);
		}

		String mobileNo = jobj.getString("mobileNo");

		long cou = dao.findCouByMobileNo(mobileNo);
		if (cou == 0) {
			baseResponse.setStatus(0);
			baseResponse.setStatusMsg("电话可用");
		} else {
			baseResponse.setStatus(2);
			baseResponse.setStatusMsg("联系人电话已经存在！");
		}

		String content = JSONObject.toJSONString(baseResponse);
		logger.debug("register content = {}", content);
		return content;
	}

	 

	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/getReportform", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getReportform(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("getReportform");
		BaseResponse baseResponse = new BaseResponse();
		String json = this.checkParameter(request);

		if (StringUtils.isStrEmpty(json)) {
			baseResponse.setStatus(2);
			baseResponse.setStatusMsg("请求参数不合法");
			return JSONObject.toJSONString(baseResponse);
		}

		JSONObject jobj = JSONObject.parseObject(json);
		String adminid = jobj.getString("sessionid");

		Admin admin = this.getAdmin(adminid);
		if (admin == null) {
			baseResponse.setStatus(1);
			baseResponse.setStatusMsg("请重新登录");
			return JSONObject.toJSONString(baseResponse);
		}

		String dateType = "1";
		String otheradminId = "";
		if (!json.equals("")) {
			try {
				json = URLDecoder.decode(json, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			JSONObject whereJson = JSONObject.parseObject(json);
			otheradminId = whereJson.getString("adminId");
			dateType = whereJson.getString("dateType");
		}

		int level = admin.getLevel();
		List<Channel> channels = null;

		int first = 1;

		String minDate = jobj.getString("minDate");
		String maxDate = jobj.getString("maxDate");
		String date = null;
		if(minDate.equals(maxDate)) {
			date = minDate;
		}
		else {
			date = minDate + "~" + maxDate;
		}
		String str = null;
		// 从数据字典获取 数据详情表 包含的所有字段
		List<Dictionary> list = dao.findDictionaryByType("22");
		List<String> strs = new ArrayList<String>();
		Cache.channelCatche(dao);
		OperateDao od = new OperateDao();
		List<List<String>> lists = new ArrayList<List<String>>();
		try {
			first = Integer.parseInt(jobj.getString("first"));

			String appId = jobj.getString("appId");
			// 从配置表读取 数据详情表配置的表头字段
			String sql = "select columns from operate_app_table where type = 22 and system = 1 and appId = " + appId;
			str = od.Query(sql).get(0).get("columns");
			// 通过 数据字段 获取表头对应的英文名称
			strs = od.getNamePy(list, str);
			boolean isHj = DaoWhere.isHj(jobj);
			List<Map<String, String>> reportforms1 = null;
			if (first == 0) {
				if(isHj)
				{
					reportforms1 = od.findSumFormDayOperate(null, jobj,"");
					List<Map<String, String>> ad = od.findAdminSumFormDayOperate(null, jobj,"");
					Map<String, String> or = reportforms1.get(0);
					or.put("adminName", ad.size()+"个负责人");
					reportforms1.addAll(ad);
					Cache.setOperate_reportformOperate(Long.parseLong(adminid), reportforms1);
				}
				
				long listSize = od.findFormCou(null, null, jobj, dateType,"");
				baseResponse.setListSize(listSize + "");
			}
	        else
	        {
        			if(isHj)
	        		reportforms1 = Cache.getOperate_reportformOperate(Long.parseLong(adminid));
	        }

	        Cache.setLastTime(Long.parseLong(adminid), System.currentTimeMillis());

	        List<Map<String, String>> reportforms = null;
			if (dateType.equals("1")) {
				reportforms = od.findFormOperate(null, null, jobj);
			} else if (dateType.equals("2")){
				reportforms = od.findFormMonthOperate(null, null, jobj);
			} else {
				reportforms = od.findFormNothing(null, null, jobj);
				for (Map<String, String> map : reportforms) {
					map.put("date", date);
				}
			}
			if(isHj && reportforms1 != null)
        	{
	        	reportforms.addAll(0, reportforms1);
        	}
			for (Map<String, String> map : reportforms) {
				if(null == map.get("registerConversion")) {
					map.put("registerConversion", "0");
				}
				if("" == map.get("grossProfitRate")) {
					map.put("grossProfitRate", "0");
				}
				if(null == map.get("optimization")) {
					map.put("optimization", "0");
				}
			}
//			lists.add(Arrays.asList(str.split(",")));
			//对数据按表头配置排序 发送给前端
			for (Map<String, String> map : reportforms) {
				List<String> l = new ArrayList<String>();
				for (String string : strs) {
					l.add(map.get(string));
					}
				lists.add(l);
			}
			List<String> tablHead = Arrays.asList(str.split(","));
			baseResponse.setProxyNameList(tablHead);    // 表头
			baseResponse.setDatas(lists);         // 具体数据

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			od.close();
		}
		

		String content = JSONObject.toJSONString(baseResponse);
		logger.debug("register content = {}", content);
		return content;
	}
	

	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/getReportformAPP", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getReportformAPP(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("getReportformAPP");
		BaseResponse baseResponse = new BaseResponse();
		String json = this.checkParameter(request);

		if (StringUtils.isStrEmpty(json)) {
			baseResponse.setStatus(2);
			baseResponse.setStatusMsg("请求参数不合法");
			return JSONObject.toJSONString(baseResponse);
		}

		JSONObject jobj = JSONObject.parseObject(json);
		String adminid = jobj.getString("sessionid");

		Admin admin = this.getAdmin(adminid);
		if (admin == null) {
			baseResponse.setStatus(1);
			baseResponse.setStatusMsg("请重新登录");
			return JSONObject.toJSONString(baseResponse);
		}

		String dateType = "1";
		String otheradminId = "";
		if (!json.equals("")) {
			try {
				json = URLDecoder.decode(json, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			JSONObject whereJson = JSONObject.parseObject(json);
			otheradminId = whereJson.getString("adminId");
			dateType = whereJson.getString("dateType");
		}

		int level = admin.getLevel();
		List<Channel> channels = null;

		int first = 1;
		
		String minDate = jobj.getString("minDate");
		String maxDate = jobj.getString("maxDate");
		String date = null;
		if(minDate.equals(maxDate)) {
			date = minDate;
		}
		else {
			date = minDate + "~" + maxDate;
		}
		
		String str = null;
		//数据库读取数据详情表包含的所有表头
		List<Dictionary> list = dao.findDictionaryByType("21");
		List<String> strs = new ArrayList<String>();
		List<List<String>> lists = new ArrayList<List<String>>();
		Cache.channelCatche(dao);
		OperateDao od = new OperateDao();
		try {
			first = Integer.parseInt(jobj.getString("first"));
			String appId = jobj.getString("appId");
			//从数据库读取表头配置   type=21 表示流程转化表
			String sql = "select columns from operate_app_table where system = 1 and type = 21 and appId = " + appId;
			str = od.Query(sql).get(0).get("columns");
			// 获取表头对应的英文名称
			strs = od.getNamePy(list, str);
		
			boolean isHj = DaoWhere.isHj(jobj);
			List<Map<String, String>> reportforms1 = null;
			if (first == 0) {
				if(isHj) {
				reportforms1 = od.findSumFormDayOperateAPP(null, jobj,"");
				List<Map<String, String>> ad = od.findAdminSumFormDayOperateApp(null, jobj,"");
				Map<String, String> or = reportforms1.get(0);
				or.put("adminName", ad.size()+"个负责人");
				reportforms1.addAll(ad);
				Cache.setOperate_reportformOperate(Long.parseLong(adminid), reportforms1);
				}
				long listSize = od.findFormCouApp(null, null, jobj, dateType,"");
				baseResponse.setListSize(listSize + "");
			}
	        else
	        {
	        	if(isHj) {
	        		reportforms1 = Cache.getOperate_reportformOperate(Long.parseLong(adminid));
	        	}
	        }

	        Cache.setLastTime(Long.parseLong(adminid), System.currentTimeMillis());

	        List<Map<String, String>> reportforms = null;
			if (dateType.equals("1")) {
				reportforms = od.findFormOperateApp(null, null, jobj);
			} else if (dateType.equals("2")) {
				reportforms = od.findFormMonthOperateApp(null, null, jobj);
			} else {
				reportforms = od.findFormMonthOperateAppNothing(null, null, jobj);
				for (Map<String, String> map : reportforms) {
					map.put("date", date);
				}
			}
			if(isHj) {
				reportforms.addAll(0, reportforms1);
			}
			//将数据按表头顺序排序
			for (Map<String, String> map : reportforms) {
				List<String> l = new ArrayList<String>();
				for (String string : strs) {
					l.add(map.get(string));
					}
				lists.add(l);
			}
			List<String> tablHead = Arrays.asList(str.split(","));
			baseResponse.setProxyNameList(tablHead);    // 表头
			baseResponse.setDatas(lists); 

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			od.close();
		}

		String content = JSONObject.toJSONString(baseResponse);
		logger.debug("register content = {}", content);
		return content;
	}

	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/addOptimizationTask", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String addOptimizationTask(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("addOptimizationTask");
		BaseResponse baseResponse = new BaseResponse();
		String json = this.checkParameter(request);

		if (StringUtils.isStrEmpty(json)) {
			baseResponse.setStatus(2);
			baseResponse.setStatusMsg("请求参数不合法");
			return JSONObject.toJSONString(baseResponse);
		}

		JSONObject jobj = JSONObject.parseObject(json);
		String adminid = jobj.getString("sessionid");

		Admin admin = this.getAdmin(adminid);
		if (admin == null) {
			baseResponse.setStatus(1);
			baseResponse.setStatusMsg("请重新登录");
			return JSONObject.toJSONString(baseResponse);
		}

		OptimizationTask optimizationTask = JSONObject.parseObject(json, OptimizationTask.class);
		dao.saveOptimizationTask(optimizationTask);
		baseResponse.setStatus(0);
		String content = JSONObject.toJSONString(baseResponse);
		logger.debug("register content = {}", content);
		return content;
	}

	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/deleteOptimizationTask", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String deleteOptimizationTask(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("deleteOptimizationTask");
		BaseResponse baseResponse = new BaseResponse();
		String json = this.checkParameter(request);

		if (StringUtils.isStrEmpty(json)) {
			baseResponse.setStatus(2);
			baseResponse.setStatusMsg("请求参数不合法");
			return JSONObject.toJSONString(baseResponse);
		}

		JSONObject jobj = JSONObject.parseObject(json);
		String adminid = jobj.getString("sessionid");

		Admin admin = this.getAdmin(adminid);
		if (admin == null) {
			baseResponse.setStatus(1);
			baseResponse.setStatusMsg("请重新登录");
			return JSONObject.toJSONString(baseResponse);
		}

		String ids = jobj.getString("id");

		String[] idlist = ids.split(",");

		OptimizationTask ot = TaskLine.getRunOptimizationTask();

		for (int i = 0; i < idlist.length; i++) {
			long id = 0;
			try {
				id = Long.parseLong(idlist[i]);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (ot != null && ot.getId() == id) {

			} else
				dao.deleteOptimizationTask(id + "");
		}

		baseResponse.setStatus(0);
		String content = JSONObject.toJSONString(baseResponse);
		logger.debug("register content = {}", content);
		return content;
	}

	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/startOptimizationTask", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String startOptimizationTask(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("startOptimizationTask");
		BaseResponse baseResponse = new BaseResponse();
		String json = this.checkParameter(request);

		if (StringUtils.isStrEmpty(json)) {
			baseResponse.setStatus(2);
			baseResponse.setStatusMsg("请求参数不合法");
			return JSONObject.toJSONString(baseResponse);
		}

		JSONObject jobj = JSONObject.parseObject(json);
		String adminid = jobj.getString("sessionid");

		Admin admin = this.getAdmin(adminid);
		if (admin == null) {
			baseResponse.setStatus(1);
			baseResponse.setStatusMsg("请重新登录");
			return JSONObject.toJSONString(baseResponse);
		}

		int rs = TaskLine.startProxyDataTask();
		if (rs == 0) {
			baseResponse.setStatus(0);
			baseResponse.setStatusMsg("开始重跑数据。");
		} else if (rs == 1) {
			baseResponse.setStatus(2);
			baseResponse.setStatusMsg("重跑数据已经开始了。");
		}

		String content = JSONObject.toJSONString(baseResponse);
		logger.debug("register content = {}", content);
		return content;
	}

	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/getOptimizationTaskParameter", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getOptimizationTaskParameter(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("getOptimizationTaskParameter");
		BaseResponse baseResponse = new BaseResponse();

		Cache.AdminCatche(dao);
		String json = this.checkParameter(request);

		if (StringUtils.isStrEmpty(json)) {
			baseResponse.setStatus(2);
			baseResponse.setStatusMsg("请求参数不合法");
			return JSONObject.toJSONString(baseResponse);
		}

		JSONObject jobj = JSONObject.parseObject(json);
		String adminid = jobj.getString("sessionid");

		Admin admin = this.getAdmin(adminid);
		if (admin == null) {
			baseResponse.setStatus(1);
			baseResponse.setStatusMsg("请重新登录");
			return JSONObject.toJSONString(baseResponse);
		}

		StringBuffer where = new StringBuffer();
		where.append(" 1 = 1 ");
		String otheradminId = "";
		if (!json.equals("")) {

			try {
				json = URLDecoder.decode(json, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			JSONObject whereJson = JSONObject.parseObject(json);

			otheradminId = whereJson.getString("adminId");
			if (!StringUtils.isStrEmpty(otheradminId)) {
				where.append(" and adminId = " + otheradminId + " ");
			}

		}

		List<Channel> channels = dao.findAllChannel("");

		ArrayList<String> channelNoList = new ArrayList<String>();

		for (int i = 0; i < channels.size(); i++) {
			Channel channel = channels.get(i);
			channelNoList.add(channel.getId() + "," + channel.getChannel() + "," + channel.getChannelName());
		}
		Cache.DicCatche(dao);
		List<Dictionary> dic9 = Cache.getDicList(9);

		baseResponse.setChannelNoList(channelNoList);
		baseResponse.setFromTypeList(dic9);
		baseResponse.setStatus(0);
		baseResponse.setStatusMsg("");
		String content = JSONObject.toJSONString(baseResponse);
		logger.debug("register content = {}", content);
		return content;
	}

	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/getOptimizationTask", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getOptimizationTask(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("getOptimizationTask");
		BaseResponse baseResponse = new BaseResponse();
		String json = this.checkParameter(request);

		if (StringUtils.isStrEmpty(json)) {
			baseResponse.setStatus(2);
			baseResponse.setStatusMsg("请求参数不合法");
			return JSONObject.toJSONString(baseResponse);
		}

		JSONObject jobj = JSONObject.parseObject(json);
		String adminid = jobj.getString("sessionid");

		Admin admin = this.getAdmin(adminid);
		if (admin == null) {
			baseResponse.setStatus(1);
			baseResponse.setStatusMsg("请重新登录");
			return JSONObject.toJSONString(baseResponse);
		}
		List<OptimizationTask> taskList = null;

		String queryType = jobj.getString("queryType");
		if (StringUtils.isStrEmpty(queryType)) {
			taskList = dao.findByNoFinishOptimizationTask();

			OptimizationTask ot = TaskLine.getRunOptimizationTask();
			for (int i = 0; i < taskList.size(); i++) {
				if (ot == null) {
					taskList.get(i).setStatus(0);
				} else {
					if (taskList.get(i).getId() == ot.getId()) {
						ot.setChannelName(taskList.get(i).getChannelName());
						ot.setComment(taskList.get(i).getComment());
						taskList.set(i, ot);

					} else {
						taskList.get(i).setStatus(0);

					}
				}
			}

		} else
			taskList = dao.findByAllOptimizationTask(jobj);

		baseResponse.setOptimizationTaskList(taskList);
		baseResponse.setStatus(0);
		String content = JSONObject.toJSONString(baseResponse);
		logger.debug("register content = {}", content);
		return content;
	}
	
	

	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/addCost", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String addCost(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("addCost");
		BaseResponse baseResponse = new BaseResponse();
		String json = this.checkParameter(request);

		if (StringUtils.isStrEmpty(json)) {
			baseResponse.setStatus(2);
			baseResponse.setStatusMsg("请求参数不合法");
			return JSONObject.toJSONString(baseResponse);
		}

		JSONObject jobj = JSONObject.parseObject(json);
		String adminid = jobj.getString("sessionid");

		Admin admin = this.getAdmin(adminid);
		if (admin == null) {
			baseResponse.setStatus(1);
			baseResponse.setStatusMsg("请重新登录");
			return JSONObject.toJSONString(baseResponse);
		}

		

		Cache.channelCatche(dao);
		
		OperateDao od = new OperateDao();
		try {
			od.updateCost(jobj);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			od.close();
		}

		
		baseResponse.setStatus(0);
		String content = JSONObject.toJSONString(baseResponse);
		logger.debug("register content = {}", content);
		return content;
	}
	
	

	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/getCost", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getCost(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("getCost");
		BaseResponse baseResponse = new BaseResponse();
		String json = this.checkParameter(request);

		if (StringUtils.isStrEmpty(json)) {
			baseResponse.setStatus(2);
			baseResponse.setStatusMsg("请求参数不合法");
			return JSONObject.toJSONString(baseResponse);
		}

		JSONObject jobj = JSONObject.parseObject(json);
		String adminid = jobj.getString("sessionid");

		Admin admin = this.getAdmin(adminid);
		if (admin == null) {
			baseResponse.setStatus(1);
			baseResponse.setStatusMsg("请重新登录");
			return JSONObject.toJSONString(baseResponse);
		}
		List<OptimizationTask> taskList = null;

		

		Cache.channelCatche(dao);
		
		OperateDao od = new OperateDao(); 
  
		try {
			int first = Integer.parseInt(jobj.getString("first"));

			if (first == 0) {
				int listSize = od.getCostCou(jobj);
				baseResponse.setListSize(listSize + "");
			}
			List<Map<String,String>> dl = od.getCost(jobj);

			baseResponse.setCostList(dl);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			od.close();
		}
		
		

	
		baseResponse.setStatus(0);
		String content = JSONObject.toJSONString(baseResponse);
		logger.debug("register content = {}", content);
		return content;
	}
	

	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/deleteCost", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String deleteCost(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("deleteCost");
		BaseResponse baseResponse = new BaseResponse();
		String json = this.checkParameter(request);

		if (StringUtils.isStrEmpty(json)) {
			baseResponse.setStatus(2);
			baseResponse.setStatusMsg("请求参数不合法");
			return JSONObject.toJSONString(baseResponse);
		}

		JSONObject jobj = JSONObject.parseObject(json);
		String adminid = jobj.getString("sessionid");

		Admin admin = this.getAdmin(adminid);
		if (admin == null) {
			baseResponse.setStatus(1);
			baseResponse.setStatusMsg("请重新登录");
			return JSONObject.toJSONString(baseResponse);
		}
		List<OptimizationTask> taskList = null;

		

		Cache.channelCatche(dao);
		
		OperateDao od = new OperateDao(); 
  
		try {
			od.deleteCost(jobj);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			od.close();
		}
		
		

	
		baseResponse.setStatus(0);
		String content = JSONObject.toJSONString(baseResponse);
		logger.debug("register content = {}", content);
		return content;
	}
	
	

	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/getRunOptimizationTask", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getRunOptimizationTask(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("getRunOptimizationTask");
		BaseResponse baseResponse = new BaseResponse();
		String json = this.checkParameter(request);

		if (StringUtils.isStrEmpty(json)) {
			baseResponse.setStatus(2);
			baseResponse.setStatusMsg("请求参数不合法");
			return JSONObject.toJSONString(baseResponse);
		}

		JSONObject jobj = JSONObject.parseObject(json);
		String adminid = jobj.getString("sessionid");

		Admin admin = this.getAdmin(adminid);
		if (admin == null) {
			baseResponse.setStatus(1);
			baseResponse.setStatusMsg("请重新登录");
			return JSONObject.toJSONString(baseResponse);
		}

		OptimizationTask ot = TaskLine.getRunOptimizationTask();
		baseResponse.setRunOptimizationTask(ot);

		baseResponse.setStatus(0);
		String content = JSONObject.toJSONString(baseResponse);
		logger.debug("register content = {}", content);
		return content;
	}

	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/getTableType", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getTableType(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("getTableType");
		BaseResponse baseResponse = new BaseResponse();
		String json = this.checkParameter(request);

		if (StringUtils.isStrEmpty(json)) {
			baseResponse.setStatus(2);
			baseResponse.setStatusMsg("请求参数不合法");
			return JSONObject.toJSONString(baseResponse);
		}

		JSONObject jobj = JSONObject.parseObject(json);
		String adminid = jobj.getString("sessionid");

		Admin admin = this.getAdmin(adminid);
		if (admin == null) {
			baseResponse.setStatus(1);
			baseResponse.setStatusMsg("请重新登录");
			return JSONObject.toJSONString(baseResponse);
		}
		List<OptimizationTask> taskList = null;

		Cache.DicCatche(dao);
		List<Dictionary> dic9 = Cache.getDicList(9);

		baseResponse.setFromTypeList(dic9);
		baseResponse.setStatus(0);
		baseResponse.setStatusMsg("");

		baseResponse.setOptimizationTaskList(taskList);
		baseResponse.setStatus(0);
		String content = JSONObject.toJSONString(baseResponse);
		logger.debug("register content = {}", content);
		return content;
	}

	private void setAdmin(Admin admin) {
		admin.setLoginDate(System.currentTimeMillis());
		Cache.AdminCatche(dao);
		Cache.updateAdminCatche(admin);
	}

	private Admin getAdmin(String adminid) {
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
	
	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/exportData", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public void exportData(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("exportData");
		BaseResponse baseResponse = new BaseResponse();
		String json = this.checkParameter(request);

		if (StringUtils.isStrEmpty(json)) {
			baseResponse.setStatus(2);
			baseResponse.setStatusMsg("请求参数不合法");
			return ;
		}

		JSONObject jobj = JSONObject.parseObject(json);
		String adminid = jobj.getString("sessionid");
		//根据勾选框选中与否显示相应的字段
		JSONArray arr = jobj.getJSONArray("tag");
		

	        boolean allflag = false;
	        boolean channelflag = false;
	        boolean channeltypeflag = false;
	        boolean adminflag = false;
	        boolean h5flag = false;
	        boolean creditflag = false;
	        boolean firstgetflag = false;
	        boolean secondgetflag = false;
	        boolean outflag = false;
		//都没有勾选时默认标志为0，下载的表格要移除相应字段
		if("[]".equals(arr.toString())) {
		}
		else {
			allflag = DaoWhere.ischose("总计", jobj);
			channelflag = DaoWhere.ischose("渠道号", jobj);
			channeltypeflag = DaoWhere.ischose("渠道分类", jobj);
			adminflag = DaoWhere.ischose("负责人", jobj);
			h5flag = DaoWhere.ischose("H5", jobj);
			creditflag = DaoWhere.ischose("额度", jobj);
			firstgetflag = DaoWhere.ischose("首贷", jobj);
			secondgetflag = DaoWhere.ischose("续贷", jobj);
			outflag = DaoWhere.ischose("外部", jobj);

		}
        

		Admin admin = this.getAdmin(adminid);
		if (admin == null) {
			baseResponse.setStatus(1);
			baseResponse.setStatusMsg("请重新登录");
			return ;
		}

		String dateType = "1";
		String otheradminId = "";
		if (!json.equals("")) {
			try {
				json = URLDecoder.decode(json, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			JSONObject whereJson = JSONObject.parseObject(json);
			otheradminId = whereJson.getString("adminId");
			dateType = whereJson.getString("dateType");
		}

		int level = admin.getLevel();
		List<Channel> channels = null;

		int first = 1;

		String minDate = jobj.getString("minDate");
		String maxDate = jobj.getString("maxDate");
		String date = null;
		if(minDate.equals(maxDate)) {
			date = minDate;
		}
		else {
			date = minDate + "~" + maxDate;
		}
		String str = null;
		//获取数据字典内数据详情表表头
		List<Dictionary> list = dao.findDictionaryByType("22");
		List<String> strs = new ArrayList<String>();
//		Cache.channelCatche(dao);
		OperateDao od = new OperateDao();
		List<Map<String, String>> reportforms = null;
//		String str = null;
		try {
			first = Integer.parseInt(jobj.getString("first"));
			String appId = jobj.getString("appId");
			// 获取配置的表 表头以及对应的英文名称
			String sql = "select columns from operate_app_table where system = 1 and type = 22 and appId = " + appId;
			str = od.Query(sql).get(0).get("columns");

			strs = od.getNamePy(list, str);
			List<Map<String, String>> reportforms1 = null;
			if (first == 0) {
				if(allflag)
				{
				reportforms1 = od.findSumFormDayOperate(null, jobj,"");
				List<Map<String, String>> ad = od.findAdminSumFormDayOperate(null, jobj,"");
				Map<String, String> or = reportforms1.get(0);
				or.put("adminName", ad.size()+"个负责人");
				reportforms1.addAll(ad);
				Cache.setOperate_reportformOperate(Long.parseLong(adminid), reportforms1);
				}
				long listSize = od.findFormCou(null, null, jobj, dateType,"");
				baseResponse.setListSize(listSize + "");
			}
			 else
		        {
				 if(allflag) {
		        		reportforms1 = Cache.getOperate_reportformOperate(Long.parseLong(adminid));
				 }
		     }

			if (dateType.equals("1")) {
				reportforms = od.findFormOperateAll(null, null, jobj);
			} else if(dateType.equals("2")) {
				reportforms = od.findFormMonthOperateAll(null, null, jobj);
			}else {
				reportforms = od.findFormMonthOperateAllNothing(null, null, jobj);
				for (Map<String, String> map : reportforms) {
					map.put("date", date);
				}
			}
			if(allflag) {
				//reportforms1中map这几个key没有值，默认为null，表格显示会错位，添加这几个key的value为空字符串""
				for(Map<String,String> map:reportforms1) {
					if(map.get("app") == null) {
						map.put("app","");
					}
					if(null == map.get("channelName")) {
						map.put("channelName", "");
					}
					if(null == map.get("channelType")) {
						map.put("channelType", "");
					}
					if(null == map.get("registerConversion")) {
						map.put("registerConversion", "0");
					}
					if(null == map.get("optimization")) {
						map.put("optimization", "0");
					}
					if(map.get("rewardType") == null) {
						map.put("rewardType", "");
					}
				}
				reportforms.addAll(0, reportforms1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			od.close();
		}

		//map的value值为null时，表格会错位，将null设为空字符串""
		for(Map<String,String> map:reportforms) {
			if(null == map.get("registerConversion")) {
				map.put("registerConversion", "0");
			}
//			if(null == map.get("outFirstGetSum")) {
//				map.put("outFirstGetSum", "");
//			}
//			if(null == map.get("cost2")) {
//				map.put("cost2", "0.0");
//			}
			if("" == map.get("grossProfitRate")) {
				map.put("grossProfitRate", "0");
			}
			if(null == map.get("optimization")) {
				map.put("optimization", "0");
			}
			if(map.get("rewardType") == null) {
				map.put("rewardType", "");
			}
			
//			for(String key:map.keySet()) {
//				String value = map.get(key);
//				if(value == null) {
//					map.put(key, "");
//				}
//			}
			map.put("activationConversion", map.get("activationConversion")+"%");
			map.put("registerConversion", map.get("registerConversion")+"%");
			map.put("uploadConversion", map.get("uploadConversion")+"%");
			map.put("accountConversion", map.get("accountConversion")+"%");
			map.put("loanConversion", map.get("loanConversion")+"%");
			map.put("grossProfitRate", map.get("grossProfitRate")+"%");
			map.put("optimization", map.get("optimization")+"%");
		}

		List<String> listName =  Arrays.asList(str.split(","));
        if(!channelflag) {
        	listName.remove("渠道");
        	strs.remove("channelName");   
        	listName.remove("渠道号");
        	strs.remove("channel");  
        }
//        if(channelidflag == 0) {
//        	listName.remove("渠道号");
//        	listId.remove("channel");    
//        }
        if(!channeltypeflag) {
        	listName.remove("渠道分类");
        	 listName.remove("分成方式");
        	 strs.remove("channelType");  
        	 strs.remove("rewardType");  
        }
        if(!adminflag) {
        	listName.remove("负责人");
        	strs.remove("adminName"); 
        }
        if(!h5flag) {
        	 listName.remove("H5点击");
        	 listName.remove("H5注册");
//        	 listName.remove("优化比例(%)");
        	 listName.remove("激活");
        	 listName.remove("H5激活转化(%)");
        	 strs.remove("h5Click");        //h5点击
        	 strs.remove("h5Register");     //h5注册
//        	 listId.remove("optimization");    //优化比例
        	 strs.remove("activation");     //激活 
        	 strs.remove("activationConversion");     //h5激活转化
        }
        if(!creditflag) {
            listName.remove("授信总额");
            listName.remove("人均批额");
            strs.remove("credit");         //授信总额
            strs.remove("perCapitaCredit"); //人均批额
        }
        if(!firstgetflag) {
        	listName.remove("首提人数");
        	listName.remove("首贷金额");
        	listName.remove("外部首贷金额");   
        listName.remove("首贷笔均");
        strs.remove("firstGetPer");     //首提人数
        strs.remove("firstGetSum");      //首贷总额
        strs.remove("outFirstGetSum");   //外部首贷金额
        strs.remove("firstPerCapitaCredit");       //首贷笔均
        }
        if(!secondgetflag) {
        listName.remove("续贷人数");
        listName.remove("续放笔数");
        listName.remove("续贷金额");
        listName.remove("续贷笔均");
        strs.remove("secondGetPer");       //续贷人数
        strs.remove("secondGetPi");       //续放笔数
        strs.remove("secondGetSum");       //续贷金额
        strs.remove("secondPerCapitaCredit");       //续贷笔均
        }
        if(!outflag) {
        	listName.remove("外部注册");
        	listName.remove("外部进件");
        	listName.remove("外部开户");
        	listName.remove("外部首提");
        	listName.remove("外部渠道提现金额");
        	strs.remove("outRegister");     //外部注册
        	strs.remove("outUpload");       //外部进件
        	strs.remove("outAccount");        //外部开户
        	strs.remove("outFirstGetPer");      //外部首提
        	strs.remove("outChannelSum");        //外部渠道提现金额
        }

        ExportMapExcel exportExcelUtil = new ExportMapExcel();
        exportExcelUtil.exportExcelString("运营数据详情统计表",listName,strs,reportforms,response);

	}

	 @RequestMapping(value = "/updataPwd", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
	    @ResponseBody
		public String updataPwd(HttpServletRequest request,HttpServletResponse response) {

			logger.debug("updataPwd");
			BaseResponse baseResponse = new BaseResponse();
//			String rootPath = this.getClass().getClassLoader().getResource("").getPath();
			//拼接修改密码链接     http://localhost:8080/operate/page/forgetPaw.html?参数
			
			 String json = this.checkParameter(request);


	       if(StringUtils.isStrEmpty(json)){
	    	   baseResponse.setStatusMsg("请输入邮箱账号！");
	    	   return JSONObject.toJSONString(baseResponse);
	       }

	       JSONObject jobj = JSONObject.parseObject(json);
	       String email = jobj.getString("email");
	       
	            
	       List<Admin> admins = dao.findAdminByEmail(email);
	       
	       if(admins.size() == 0) {
			 baseResponse.setStatusMsg("没有这个用户，请确认邮箱输入正确！");
		    	 return JSONObject.toJSONString(baseResponse);
			}
		

			
			Date d = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentTime = sdf.format(d);
			int type = 2;

			long createurlTime = System.currentTimeMillis(); 

			String name = admins.get(0).getName();
			int flag = admins.get(0).getFlag();
			String[] array = new String[] {email};

			String path = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() 
			+ request.getContextPath() + "/page/forgetPaw.html?email=" + array[0] + "&createurlTime="
			+ createurlTime + "&flag=" + flag + "&type=" + type;
			
			
			String text = "Hi "+ name +",\n\n我们在"+currentTime+"收到你重置后台账号密码的请求。"
					+ "如果不是你自己在重置密码，请忽略并删除本邮件。\n如果是你自己需要重置密码，请点击下方链接进行密码重置。"
					+ "（链接24小时有效）\n如果有其他问题，请与技术部联系。\n\n"+ path ;
//			String text = path + array[0] + "&createurlTime=" + createurlTime;
			Mail mail = new Mail();
			mail.sendMailTest(text,array);
			baseResponse.setStatusMsg("重置密码邮件已发送，请注意查收！");
	   	    return JSONObject.toJSONString(baseResponse);
			}

	 	@RequestMapping(value = "/checkflag", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
	 	@ResponseBody
		public String checkflag(HttpServletRequest request,HttpServletResponse response) {
		 
	 		logger.debug("checkflag");
	 		
	 		BaseResponse baseResponse = new BaseResponse();
		 
	 		 String json = this.checkParameter(request);
	 		JSONObject jobj = JSONObject.parseObject(json);
		       String email = jobj.getString("email");
	 		 
//			String email = request.getParameter("email");
			int flag = Integer.parseInt(jobj.getString("flag"));
			long createurlTime = Long.parseLong(jobj.getString("createurlTime"));
			long currentTime = System.currentTimeMillis(); 
			int type = Integer.parseInt(jobj.getString("type"));
			
			
			
			 List<Admin> admins = dao.findAdminByEmail(email);
			 int random = (int)(Math.random()*10);
			 int res = 0;
			 if(flag == admins.get(0).getFlag() && (currentTime - createurlTime < 86400000)) {
				 flag += random;
				 dao.updateflag(email, flag);
				 baseResponse.setFinish(true);
				 baseResponse.setStatus(type);
				 return JSONObject.toJSONString(baseResponse);
			 }
//			 String path = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() 
//				+ request.getContextPath() + "/page/forgrtPaw.html?email=" + email +"&res=" + res + "&createurlTime" + ;
			 baseResponse.setFinish(false);
			 baseResponse.setStatusMsg("链接已失效，请重新申请");
			return JSONObject.toJSONString(baseResponse);
			
	 }

		@RequestMapping(value = "/changePwd", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
		@ResponseBody
		public String changePwd(HttpServletRequest request,HttpServletResponse response) {
		
			String json = this.checkParameter(request);

			JSONObject jobj = JSONObject.parseObject(json);
			String email = jobj.getString("email");

			String pwd = jobj.getString("password");
			int type = Integer.parseInt(jobj.getString("type"));
			
			Date d = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentTime = sdf.format(d);
//			long currentTime = System.currentTimeMillis(); 
//			String path = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/page/index.html";
			String path = "";
			if(type == 1) {
				path = "http://ledaikuan.cn/systems/proxy/index.html";
			}
			else {
				path = "http://ledaikuan.cn/systems/operation/index.html";
			}
			int n = dao.updatepwd(email, pwd);
			List<Admin> admins = dao.findAdminByEmail(email);
			
			String text = "Hi "+ admins.get(0).getName() +",\n\n    你的密码在"+currentTime+"被重置。"
					+ "如果非本人操作，请尽快联系管理员\n\n						消费金融技术部";
			String[] array = new String[] {email};
			Mail mail = new Mail();
			
			BaseResponse baseResponse = new BaseResponse();
			
			if(1 == n) {
				mail.sendMailTest(text,array);
				baseResponse.setStatus(0);
				baseResponse.setStatusMsg("密码修改成功，请重新登录");
				baseResponse.setListSize(path);
				return JSONObject.toJSONString(baseResponse);
			}else {
				baseResponse.setStatus(1);
				baseResponse.setStatusMsg("密码修改失败，请联系管理员！");
				return JSONObject.toJSONString(baseResponse);
			}
		}
		
		@CrossOrigin(origins = "*", maxAge = 3600)
		@RequestMapping(value = "/exportDataApp", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@ResponseBody
		public String exportDataApp(HttpServletRequest request, HttpServletResponse response) {
			logger.debug("exportDataApp");
			BaseResponse baseResponse = new BaseResponse();
			String json = this.checkParameter(request);

			if (StringUtils.isStrEmpty(json)) {
				baseResponse.setStatus(2);
				baseResponse.setStatusMsg("请求参数不合法");
				return JSONObject.toJSONString(baseResponse);
			}

			JSONObject jobj = JSONObject.parseObject(json);
			String adminid = jobj.getString("sessionid");
			
			JSONArray arr = jobj.getJSONArray("tag");
			
			  boolean allflag = false;
		      boolean channelflag = false;
		      boolean adminflag = false;
		      //都没有勾选时默认标志为0，下载的表格要移除相应字段
		  	if("[]".equals(arr.toString())) {
			}
			else {
		        allflag = DaoWhere.ischose("总计", jobj);
				channelflag = DaoWhere.ischose("渠道信息", jobj);
				adminflag = DaoWhere.ischose("负责人", jobj);
			}
	        
	    
	
			Admin admin = this.getAdmin(adminid);
			if (admin == null) {
				baseResponse.setStatus(1);
				baseResponse.setStatusMsg("请重新登录");
				return JSONObject.toJSONString(baseResponse);
			}

			String dateType = "1";
			String otheradminId = "";
			if (!json.equals("")) {
				try {
					json = URLDecoder.decode(json, "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				JSONObject whereJson = JSONObject.parseObject(json);
				otheradminId = whereJson.getString("adminId");
				dateType = whereJson.getString("dateType");
			}

			int level = admin.getLevel();
			List<Channel> channels = null;

			int first = 1;
			
			String minDate = jobj.getString("minDate");
			String maxDate = jobj.getString("maxDate");
			String date = null;
			if(minDate.equals(maxDate)) {
				date = minDate;
			}
			else {
				date = minDate + "~" + maxDate;
			}

			String str = null;
			// 获取数据字典内流程转化表的表头字段
			List<Dictionary> list = dao.findDictionaryByType("21");
			List<String> strs = new ArrayList<String>();
			
			Cache.channelCatche(dao);
			OperateDao od = new OperateDao();
			
			List<Map<String, String>> reportforms = null;
			try {
				first = Integer.parseInt(jobj.getString("first"));
				List<Map<String, String>> reportforms1 = null;
				String appId = jobj.getString("appId");
				// 获取配置的表头字段 以及对应的英文名称
				String sql = "select columns from operate_app_table where system = 1 and type = 21 and appId = " + appId;
				str = od.Query(sql).get(0).get("columns");
				strs = od.getNamePy(list, str);
				
				if (first == 0) {
					
						reportforms1 = od.findSumFormDayOperateAPP(null, jobj,"");
						List<Map<String, String>> ad = od.findAdminSumFormDayOperateApp(null, jobj,"");
						Map<String, String> or = reportforms1.get(0);
						or.put("adminName", ad.size()+"个负责人");
						reportforms1.addAll(ad);
						Cache.setOperate_reportformOperate(Long.parseLong(adminid), reportforms1);
				
						long listSize = od.findFormCouApp(null, null, jobj, dateType,"");
						baseResponse.setListSize(listSize + "");
				}
		        else
		        {
		        		reportforms1 = Cache.getOperate_reportformOperate(Long.parseLong(adminid));
		        }

					if (dateType.equals("1")) {
						reportforms = od.findFormOperateAppDown(null, null, jobj);
					} else if (dateType.equals("2")) {
						reportforms = od.findFormMonthOperateAppDown(null, null, jobj);
					} else {
						reportforms = od.findFormMonthOperateAppDownNothing(null, null, jobj);
						for (Map<String, String> map : reportforms) {
							map.put("date", date);
						}
					}

//				if (dateType.equals("1")) {
//					 reportforms = od.findFormOperateAppDown(null, null, jobj);
//					baseResponse.setReportforms_operate(reportforms);
//				} else {
//					reportforms = od.findFormMonthOperateAppDown(null, null, jobj);
//					reportforms.addAll(0, reportforms1);
//					baseResponse.setReportforms_operate(reportforms);
//				}
				for (Map<String, String> map : reportforms) {
					if(null == map.get("app")) {
						map.put("app", "");
					}
				}
				if(allflag) {
					
						for(Map<String,String> map:reportforms1) {
							if(map.get("app") == null) {
								map.put("app","");
							}
							if(null == map.get("channelName")) {
								map.put("channelName", "");
							}
							if(null == map.get("channelType")) {
								map.put("channelType", "");
							}
						}
		        	
					reportforms.addAll(0, reportforms1);
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				od.close();
			}
			
			for (Map<String, String> map : reportforms) {
				map.put("loginConversion", map.get("loginConversion")+"%");
				map.put("idcardConversion", map.get("idcardConversion")+"%");
				map.put("debitCardConversion", map.get("debitCardConversion")+"%");
				map.put("homeJobConversion", map.get("homeJobConversion")+"%");
				map.put("contactsConversion", map.get("contactsConversion")+"%");
				map.put("vedioConversion", map.get("vedioConversion")+"%");
				map.put("uploadConversion", map.get("uploadConversion")+"%");
				map.put("accountConversion", map.get("accountConversion")+"%");
				map.put("accountAllConversion", map.get("accountAllConversion")+"%");
				map.put("lostConversion", map.get("lostConversion")+"%");
			}
			List<String> listName = Arrays.asList(str.split(","));

	        if(!channelflag) {
	        	listName.remove("渠道");
	        	strs.remove("channelName");   
	        	listName.remove("渠道号");
	        	strs.remove("channel");    
	        	listName.remove("渠道分类");
	        	strs.remove("channelType"); 
	        }
	        if(!adminflag) {
	        	listName.remove("负责人");
	        	strs.remove("adminName"); 
	        }


	        ExportMapExcel exportExcelUtil = new ExportMapExcel();
	        exportExcelUtil.exportExcelString("流程转化表",listName,strs,reportforms,response);

			String content = JSONObject.toJSONString(baseResponse);
			logger.debug("register content = {}", content);
			return content;
		}
		
		@RequestMapping(value = "/addPermissionType", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
		@ResponseBody
		public String addPermissionType(HttpServletRequest request,HttpServletResponse response) {
			String json = this.checkParameter(request);
			JSONObject jobj = JSONObject.parseObject(json);
			String adminid = jobj.getString("sessionid");
			
			Dictionary dic = JSONObject.parseObject(json, Dictionary.class);
			dic.setId(AppTools.getId());
			dic.setUpdateAdminId(Long.parseLong(adminid));
			dic.setCreatAdminId(Long.parseLong(adminid));
			dic.setUpdateTime(System.currentTimeMillis());
			dic.setType(10);
			dao.saveDictionary(dic);
			BaseResponse baseResponse = new BaseResponse();
			baseResponse.setStatus(0);
			baseResponse.setStatusMsg("");
			return JSONObject.toJSONString(baseResponse);
			
		}
		
		@RequestMapping(value = "/getPermissionType", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
		@ResponseBody
		public String getPermissionType(HttpServletRequest request,HttpServletResponse response) {
			String json = this.checkParameter(request);
			JSONObject jobj = JSONObject.parseObject(json);
			String adminid = jobj.getString("sessionid");
			
			List<Dictionary> plist = dao.findDictionaryByType("10");
			
			BaseResponse baseResponse = new BaseResponse();
			baseResponse.setPermissionTypeList(plist);
			baseResponse.setStatus(0);
			baseResponse.setStatusMsg("");
			return JSONObject.toJSONString(baseResponse);
			
		}	

		@RequestMapping(value = "/addPermission", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
		@ResponseBody
		public String addPermission(HttpServletRequest request,HttpServletResponse response) {
			String json = this.checkParameter(request);
			JSONObject jobj = JSONObject.parseObject(json);
			String adminid = jobj.getString("sessionid");
			
			Permission per = JSONObject.parseObject(json, Permission.class);
			
			per.setUpdateAdminId(Long.parseLong(adminid));
			per.setUpdateTime(System.currentTimeMillis());
			per.setOpType(0);
			dao.savePermission(per);
			BaseResponse baseResponse = new BaseResponse();
			baseResponse.setStatus(0);
			baseResponse.setStatusMsg("");
			return JSONObject.toJSONString(baseResponse);
			
		}

		@RequestMapping(value = "/getPermission", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
		@ResponseBody
		public String getPermission(HttpServletRequest request,HttpServletResponse response) {
			String json = this.checkParameter(request);
			JSONObject jobj = JSONObject.parseObject(json);
			String adminid = jobj.getString("sessionid");
			String type = jobj.getString("type");
			
			List<Permission> pList = dao.findPermissionByType(Long.parseLong(type));
			BaseResponse baseResponse = new BaseResponse();
			baseResponse.setPermissions(pList);
			baseResponse.setStatus(0);
			baseResponse.setStatusMsg("");
			return JSONObject.toJSONString(baseResponse);
			
		}
		
		@RequestMapping(value = "/updatePermission", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
		@ResponseBody
		public String updatePermission(HttpServletRequest request,HttpServletResponse response) {
			String json = this.checkParameter(request);
			JSONObject jobj = JSONObject.parseObject(json);
			String adminid = jobj.getString("sessionid");
			
			Permission per = JSONObject.parseObject(json, Permission.class);
			per.setOpType(0);
			per.setUpdateAdminId(Long.parseLong(adminid));
			per.setUpdateTime(System.currentTimeMillis());
			dao.savePermission(per);
			BaseResponse baseResponse = new BaseResponse();
			baseResponse.setStatus(0);
			baseResponse.setStatusMsg("");
			return JSONObject.toJSONString(baseResponse);
			
		}
		

		@RequestMapping(value = "/checkPermission", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
		@ResponseBody
		public String checkPermission(HttpServletRequest request,HttpServletResponse response) {
			String json = this.checkParameter(request);
			JSONObject jobj = JSONObject.parseObject(json);
			String adminid = jobj.getString("sessionid");
			String name = jobj.getString("name");

			BaseResponse baseResponse = new BaseResponse();
			
			List<Permission> ps = dao.findPermissionByName(name,"0");
			if(ps.size()==0)
			{
				baseResponse.setStatus(0);
				baseResponse.setStatusMsg("功能名称可用");
			}
			else
			{
				baseResponse.setStatus(2);
				baseResponse.setStatusMsg("功能名称重复");
			}
			return JSONObject.toJSONString(baseResponse);
			
		}
		

		@RequestMapping(value = "/checkMeta", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
		@ResponseBody
		public String checkMeta(HttpServletRequest request,HttpServletResponse response) {
			String json = this.checkParameter(request);
			JSONObject jobj = JSONObject.parseObject(json);
			String adminid = jobj.getString("sessionid");
			String meta = jobj.getString("meta");

			BaseResponse baseResponse = new BaseResponse();
			
			List<Permission> ps = dao.findPermissionByMeta(meta,"0");
			if(ps.size()==0)
			{
				baseResponse.setStatus(0);
				baseResponse.setStatusMsg("meta可用");
			}
			else
			{
				baseResponse.setStatus(2);
				baseResponse.setStatusMsg("meta重复");
			}
			return JSONObject.toJSONString(baseResponse);
			
		}
		

		@RequestMapping(value = "/checkPermissionType", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
		@ResponseBody
		public String checkPermissionType(HttpServletRequest request,HttpServletResponse response) {
			String json = this.checkParameter(request);
			JSONObject jobj = JSONObject.parseObject(json);
			String adminid = jobj.getString("sessionid");
			String name = jobj.getString("name");

			BaseResponse baseResponse = new BaseResponse();
			
			List<Dictionary> ps = dao.findDicByName("10", name);
			if(ps.size()==0)
			{
				baseResponse.setStatus(0);
				baseResponse.setStatusMsg("功能组名称可用");
			}
			else
			{
				baseResponse.setStatus(2);
				baseResponse.setStatusMsg("功能名称重复");
			}
			return JSONObject.toJSONString(baseResponse);
			
		}


		@RequestMapping(value = "/getAdminPermission", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
		@ResponseBody
		public String getAdminPermission(HttpServletRequest request,HttpServletResponse response) {
			String json = this.checkParameter(request);
			JSONObject jobj = JSONObject.parseObject(json);
			String sessionid = jobj.getString("sessionid");

			String adminid = jobj.getString("adminid");

			String type = jobj.getString("type");
			OperateDao od = new OperateDao();
			List<Map<String, String>> pList = null;
			try {
				pList = od.getAdminPermission(type,adminid,"0");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				od.close();
			} 
			BaseResponse baseResponse = new BaseResponse();
			baseResponse.setAdminPermissionList(pList);
			baseResponse.setStatus(0);
			baseResponse.setStatusMsg("");
			return JSONObject.toJSONString(baseResponse);
			
		}
		

		@RequestMapping(value = "/addAdminPermission", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
		@ResponseBody
		public String addAdminPermission(HttpServletRequest request,HttpServletResponse response) {
			String json = this.checkParameter(request);
			JSONObject jobj = JSONObject.parseObject(json);
			String sessionid = jobj.getString("sessionid");

			AdminPermission per = JSONObject.parseObject(json, AdminPermission.class);

			per.setUpdateAdminId(Long.parseLong(sessionid));
			per.setOpType(0);
			per.setUpdateTime(System.currentTimeMillis());
			
			OperateDao od = new OperateDao();
			long id = 0;
			try {
				 id = od.addAllAdminPermission(per);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				od.close();
			}  
			BaseResponse baseResponse = new BaseResponse();
			if(id > 0)
			{
				baseResponse.setStatus(0);
				baseResponse.setId(id);
			}
			else
			{
				baseResponse.setStatus(2);
			}
			baseResponse.setStatusMsg("");
			return JSONObject.toJSONString(baseResponse);
			
		}

		@RequestMapping(value = "/userPermission", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
		@ResponseBody
		public String userPermission(HttpServletRequest request,HttpServletResponse response) {
			String json = this.checkParameter(request);
			JSONObject jobj = JSONObject.parseObject(json);
			String sessionid = jobj.getString("sessionid");

			OperateDao od = new OperateDao();
			List<UserPermission> userPermission = null;

			try {
				userPermission = od.getAllAdminPermission(sessionid,"0");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				od.close();
			}  
			
			BaseResponse baseResponse = new BaseResponse();
			baseResponse.setStatus(0);
			baseResponse.setUserPermission(userPermission);
			baseResponse.setStatusMsg("");
			String jsonstr = JSONObject.toJSONString(baseResponse);
			return jsonstr;
			
		}
		
		/**
		 * 按逻辑查询
		 */
		@CrossOrigin(origins = "*", maxAge = 3600)
		@RequestMapping(value = "/getReportformLogic", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@ResponseBody
		public String getReportformLogic(HttpServletRequest request, HttpServletResponse response) {
			logger.debug("getReportformLogic");
			BaseResponse baseResponse = new BaseResponse();
			String json = this.checkParameter(request);

			if (StringUtils.isStrEmpty(json)) {
				baseResponse.setStatus(2);
				baseResponse.setStatusMsg("请求参数不合法");
				return JSONObject.toJSONString(baseResponse);
			}

			JSONObject jobj = JSONObject.parseObject(json);
			String adminid = jobj.getString("sessionid");

			Admin admin = this.getAdmin(adminid);
			if (admin == null) {
				baseResponse.setStatus(1);
				baseResponse.setStatusMsg("请重新登录");
				return JSONObject.toJSONString(baseResponse);
			}

			String dateType = "1";
			String otheradminId = "";
			if (!json.equals("")) {
				try {
					json = URLDecoder.decode(json, "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				JSONObject whereJson = JSONObject.parseObject(json);
				otheradminId = whereJson.getString("adminId");
				dateType = whereJson.getString("dateType");
			}

			int level = admin.getLevel();
			List<Channel> channels = null;

			int first = 1;

			Cache.channelCatche(dao);
			OperateDao od = new OperateDao();
			try {
				first = Integer.parseInt(jobj.getString("first"));

				List<Map<String, String>> reportforms1;
				if (first == 0) {
					
					reportforms1 = od.findSumFormDayOperateLogic(null, jobj,"");
					List<Map<String, String>> ad = od.findAdminSumFormDayOperateLogic(null, jobj,"");
					Map<String, String> or = reportforms1.get(0);
					or.put("adminName", ad.size()+"个负责人");
					reportforms1.addAll(ad);
					Cache.setOperate_reportformOperate(Long.parseLong(adminid), reportforms1);
					long listSize = od.findFormCouLogic(null, null, jobj, dateType,"");
					baseResponse.setListSize(listSize + "");
				}
		        else
		        {
		        		reportforms1 = Cache.getOperate_reportformOperate(Long.parseLong(adminid));
		        }

		        Cache.setLastTime(Long.parseLong(adminid), System.currentTimeMillis());

				if (dateType.equals("1")) {
					List<Map<String, String>> reportforms = od.findFormOperateLogic(null, null, jobj);
		        		reportforms.addAll(0, reportforms1);
					baseResponse.setReportforms_operate(reportforms);
				} else {
					List<Map<String, String>> reportforms = od.findFormMonthOperateLogic(null, null, jobj);
					reportforms.addAll(0, reportforms1);
					baseResponse.setReportforms_operate(reportforms);
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				od.close();
			}

			String content = JSONObject.toJSONString(baseResponse);
			logger.debug("register content = {}", content);
			return content;
		}
		
		
		@RequestMapping(value = "/yesterdayData", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
		@ResponseBody
		public String yesterdayData(HttpServletRequest request,HttpServletResponse response) {
			String json = this.checkParameter(request);
			JSONObject jobj = JSONObject.parseObject(json);
			String sessionid = jobj.getString("sessionid");

			OperateDao od = new OperateDao();
			List<UserPermission> userPermission = null;

			try {
				userPermission = od.getAllAdminPermission(sessionid,"0");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				od.close();
			}  
			
			BaseResponse baseResponse = new BaseResponse();
			baseResponse.setStatus(0);
			baseResponse.setUserPermission(userPermission);
			baseResponse.setStatusMsg("");
			return JSONObject.toJSONString(baseResponse);
			
		}
		
		@RequestMapping(value = "/getBill", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
		@ResponseBody
		public String getBill(HttpServletRequest request,HttpServletResponse response) {
			String json = this.checkParameter(request);
			FinanceLogic logic = new FinanceLogic(dao);
			return logic.getBill(json);
			
		}

		@RequestMapping(value = "/updateBillStatus", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
		@ResponseBody
		public String updateBillStatus(HttpServletRequest request,HttpServletResponse response) {
			String json = this.checkParameter(request);
			FinanceLogic logic = new FinanceLogic(dao);
			return logic.updateBillStatus(json);
			
		}

		//批量审核
	 @RequestMapping(value = "/batchUpdateBill", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	 @CrossOrigin(origins="*",maxAge=3600)
	 @ResponseBody
	 public String batchUpdateBillStatus(HttpServletRequest request,HttpServletResponse response) {
		String json = this.checkParameter(request);
		FinanceLogic logic = new FinanceLogic(dao);
		return logic.batchUpdateBillStatus(json);

	}


		@RequestMapping(value = "/updateBill", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
		@ResponseBody
		public String updateBill(HttpServletRequest request,HttpServletResponse response) {
			String json = this.checkParameter(request);
			FinanceLogic logic = new FinanceLogic(dao);
			return logic.updateBill(json);
			
		}

		@RequestMapping(value = "/getBillParameter", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
		@ResponseBody
		public String getBillParameter(HttpServletRequest request,HttpServletResponse response) {
			String json = this.checkParameter(request);
			FinanceLogic logic = new FinanceLogic(dao);
			return logic.getBillParameter(json);
			
		}

		@RequestMapping(value = "/getBillDetails", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
		@ResponseBody
		public String getBillDetails(HttpServletRequest request,HttpServletResponse response) {
			String json = this.checkParameter(request);
			FinanceLogic logic = new FinanceLogic(dao);
			return logic.getBillDetails(json);
			
		}
		
		/**
		 * 合作方管理、数据中心  运营成本数据管理 及按条件查询
		 * @param request
		 * @param response
		 * @return  所有合作方公司名称
		 */
		@RequestMapping(value = "/partnerManager", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
		@ResponseBody
		public String partnerManager(HttpServletRequest request,HttpServletResponse response) {
			
			logger.debug("partnerManager");
			BaseResponse baseResponse = new BaseResponse();
			String json = this.checkParameter(request);

			if (StringUtils.isStrEmpty(json)) {
				baseResponse.setStatus(2);
				baseResponse.setStatusMsg("请求参数不合法");
				return JSONObject.toJSONString(baseResponse);
			}

			JSONObject jobj = JSONObject.parseObject(json);
			String adminid = jobj.getString("sessionid");

			Admin admin = this.getAdmin(adminid);
			if (admin == null) {
				baseResponse.setStatus(1);
				baseResponse.setStatusMsg("请重新登录");
				return JSONObject.toJSONString(baseResponse);
			}
			
			OperateDao od = new OperateDao();
			List<Map<String, String>> partnerDetail = null;

			try {
				partnerDetail = od.findPartnerDetail(jobj);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				od.close();
			}  
			baseResponse.setReportforms_admin(partnerDetail);
			
			List<Dictionary> appList = dao.findDictionaryByType("1");
			Dictionary app = new Dictionary();
			app.setName("贷款超市");
			app.setId(60);
			appList.add(app);
			baseResponse.setAppList(appList);     //产品名称
			
			List<Dictionary> dic19 = Cache.getDicList(19); //获取发票内容
			baseResponse.setInvoiceContentList(dic19);
			
			baseResponse.setStatus(0);
			baseResponse.setStatusMsg("获取数据成功");
			return JSONObject.toJSONString(baseResponse);
		}


		/**
		 * 获取我方公司信息
		 * @param request
		 * @param response
		 * @return
		 */
		@RequestMapping(value = "/getOurCompany", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
		@ResponseBody
		public String getOurCompany(HttpServletRequest request,HttpServletResponse response) {
		
			logger.debug("getOurCompany");
			BaseResponse baseResponse = new BaseResponse();
			String json = this.checkParameter(request);

			if (StringUtils.isStrEmpty(json)) {
				baseResponse.setStatus(2);
				baseResponse.setStatusMsg("请求参数不合法");
				return JSONObject.toJSONString(baseResponse);
			}

			JSONObject jobj = JSONObject.parseObject(json);
			String adminid = jobj.getString("sessionid");

			Admin admin = this.getAdmin(adminid);
			if (admin == null) {
				baseResponse.setStatus(1);
				baseResponse.setStatusMsg("请重新登录");
				return JSONObject.toJSONString(baseResponse);
			}
			
			OperateDao od = new OperateDao();
			List<Map<String, String>> accounts = null;
			try {
				accounts = od.findBalanceAccountDetail(jobj);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				od.close();
			}  
			
			baseResponse.setReportforms_admin(accounts);
			
			List<Dictionary> dic1 = Cache.getDicList(19); //获取发票内容
			baseResponse.setAppList(dic1);
			
			baseResponse.setStatus(0);
			baseResponse.setStatusMsg("获取数据成功");
			return JSONObject.toJSONString(baseResponse);
		}
		
		/**
		 * 合作方添加和修改
		 * @param request
		 * @param response
		 * @return
		 */
		@RequestMapping(value = "/addOrUpdatePartner", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
		@ResponseBody
		public String addOrUpdatePartner(HttpServletRequest request,HttpServletResponse response) {
		
			logger.debug("addOrUpdatePartner");
			BaseResponse baseResponse = new BaseResponse();
			String json = this.checkParameter(request);

			if (StringUtils.isStrEmpty(json)) {
				baseResponse.setStatus(2);
				baseResponse.setStatusMsg("请求参数不合法");
				return JSONObject.toJSONString(baseResponse);
			}

			JSONObject jobj = JSONObject.parseObject(json);
			String adminid = jobj.getString("sessionid");

			Admin admin = this.getAdmin(adminid);
			if (admin == null) {
				baseResponse.setStatus(1);
				baseResponse.setStatusMsg("请重新登录");
				return JSONObject.toJSONString(baseResponse);
			}
			String ruleForm = jobj.getString("ruleForm");
			Partner partner =  JSONObject.parseObject(ruleForm, Partner.class);
			
			int infoStatus = Integer.parseInt(jobj.getString("infoStatus"));
			
			if(infoStatus == 0) {

				dao.addPartner(partner);
			}
			else {
				dao.updatePartner(partner);
			}
			
			baseResponse.setStatus(0);
			baseResponse.setStatusMsg("添加数据成功");
			return JSONObject.toJSONString(baseResponse);
		}
		
		/**
		 * 删除合作方
		 * @param request
		 * @param response
		 * @return
		 */
		@RequestMapping(value = "/deletePartner", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
		@ResponseBody
		public String deletePartner(HttpServletRequest request,HttpServletResponse response) {
		
			logger.debug("deletePartner");
			BaseResponse baseResponse = new BaseResponse();
			String json = this.checkParameter(request);

			if (StringUtils.isStrEmpty(json)) {
				baseResponse.setStatus(2);
				baseResponse.setStatusMsg("请求参数不合法");
				return JSONObject.toJSONString(baseResponse);
			}

			JSONObject jobj = JSONObject.parseObject(json);
			String adminid = jobj.getString("sessionid");

			Admin admin = this.getAdmin(adminid);
			if (admin == null) {
				baseResponse.setStatus(1);
				baseResponse.setStatusMsg("请重新登录");
				return JSONObject.toJSONString(baseResponse);
			}
			
			String ruleForm = jobj.getString("ruleForm");
			Partner partner =  JSONObject.parseObject(ruleForm, Partner.class);

			
			dao.deletePartner(partner);
			
			baseResponse.setStatus(0);
			baseResponse.setStatusMsg("删除数据成功");
			return JSONObject.toJSONString(baseResponse);
		}
			
		
		/**
		 * 运营成本查询
		 * @param request
		 * @param response
		 * @return
		 */
		@RequestMapping(value = "/getOperateCosting", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
		@ResponseBody
		public String getOperateCosting(HttpServletRequest request,HttpServletResponse response) {
		
			logger.debug("getOperateCosting");
			BaseResponse baseResponse = new BaseResponse();
			String json = this.checkParameter(request);

			if (StringUtils.isStrEmpty(json)) {
				baseResponse.setStatus(2);
				baseResponse.setStatusMsg("请求参数不合法");
				return JSONObject.toJSONString(baseResponse);
			}

			JSONObject jobj = JSONObject.parseObject(json);
			String adminid = jobj.getString("sessionid");

			Admin admin = this.getAdmin(adminid);
			if (admin == null) {
				baseResponse.setStatus(1);
				baseResponse.setStatusMsg("请重新登录");
				return JSONObject.toJSONString(baseResponse);
			}
			
			OperateDao od = new OperateDao();
			List<Map<String, String>> costs = null;

			try {
				costs = od.findCostDetail(jobj);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				od.close();
			}  
			baseResponse.setReportforms_admin(costs);
			
			baseResponse.setStatus(0);
			baseResponse.setStatusMsg("获取数据成功");
			return JSONObject.toJSONString(baseResponse);
			
		}
		
		/**
		 * 运营成本删除、批量删除
		 * @param request
		 * @param response
		 * @return
		 */
		@RequestMapping(value = "/deleteCosting", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
		@ResponseBody
		public String deleteCosting(HttpServletRequest request,HttpServletResponse response) {
		
			logger.debug("deleteCosting");
			BaseResponse baseResponse = new BaseResponse();
			String json = this.checkParameter(request);

			if (StringUtils.isStrEmpty(json)) {
				baseResponse.setStatus(2);
				baseResponse.setStatusMsg("请求参数不合法");
				return JSONObject.toJSONString(baseResponse);
			}

			JSONObject jobj = JSONObject.parseObject(json);
			String adminid = jobj.getString("sessionid");

			Admin admin = this.getAdmin(adminid);
			if (admin == null) {
				baseResponse.setStatus(1);
				baseResponse.setStatusMsg("请重新登录");
				return JSONObject.toJSONString(baseResponse);
			}
			
			JSONArray ruleForm = jobj.getJSONArray("ruleForm");
			for (Object object : ruleForm) {
				OperateCost operateCost =  JSONObject.parseObject(object.toString(), OperateCost.class);
				dao.deleteOperateCost(operateCost);
			}
			
			baseResponse.setStatus(0);
			baseResponse.setStatusMsg("删除数据成功");
			return JSONObject.toJSONString(baseResponse);
			
		}
		
		/**
		 * 运营成本添加
		 * @param request
		 * @param response
		 * @return
		 */
		@RequestMapping(value = "/addOrUpdateCosting", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
		@ResponseBody
		public String addOrUpdateCosting(HttpServletRequest request,HttpServletResponse response) {
		
			logger.debug("addOrUpdateCosting");
			BaseResponse baseResponse = new BaseResponse();
			String json = this.checkParameter(request);

			if (StringUtils.isStrEmpty(json)) {
				baseResponse.setStatus(2);
				baseResponse.setStatusMsg("请求参数不合法");
				return JSONObject.toJSONString(baseResponse);
			}

			JSONObject jobj = JSONObject.parseObject(json);
			String adminid = jobj.getString("sessionid");

			Admin admin = this.getAdmin(adminid);
			if (admin == null) {
				baseResponse.setStatus(1);
				baseResponse.setStatusMsg("请重新登录");
				return JSONObject.toJSONString(baseResponse);
			}
			
			JSONArray ruleForm = jobj.getJSONArray("ruleForm");
			for (Object object : ruleForm) {
				
				OperateCost operateCost =  JSONObject.parseObject(object.toString(), OperateCost.class);
				
				dao.addOperateCost(operateCost);
			}
			
			
			baseResponse.setStatus(0);
			return JSONObject.toJSONString(baseResponse);
			
		}
		//修改运营成本的同步状态
		@RequestMapping(value = "/updateCosting", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
		@ResponseBody
		public String updateCosting(HttpServletRequest request,HttpServletResponse response) {
			String json = this.checkParameter(request);
			PartnerBillLogic logic = new PartnerBillLogic(dao);
			return logic.updateCosting(json);
		}
		
		/**
		 * 获取合作方账单查询需要的相关参数
		 * @param request
		 * @param response
		 * @return
		 */
		@RequestMapping(value = "/getPartnerBillPara", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
		@ResponseBody
		public String getPartnerBillPara(HttpServletRequest request,HttpServletResponse response) {
			
			String json = this.checkParameter(request);
			PartnerBillLogic logic = new PartnerBillLogic(dao);
			return logic.getBillParameter(json);
			
		}
		/**
		 * 合作方账单
		 * @param request
		 * @param response
		 * @param  json 包含的字段
		 * @return 账单List
		 */
		@RequestMapping(value = "/getPartnerBill", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
		@ResponseBody
		public String getPartnerBill(HttpServletRequest request,HttpServletResponse response) {
			
			String json = this.checkParameter(request);
			PartnerBillLogic logic = new PartnerBillLogic(dao);
			return logic.getPartnerBill(json);
			
		}
		
		/**
		 * 下载账单
		 * @param request
		 * @param response
		 * @return
		 */
		@RequestMapping(value = "/exportPartnerBillData", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
		@ResponseBody
		public String exportPartnerBillData(HttpServletRequest request,HttpServletResponse response) {
			
			BaseResponse baseResponse = new BaseResponse();
			String json = this.checkParameter(request);

			if (StringUtils.isStrEmpty(json)) {
				baseResponse.setStatus(2);
				baseResponse.setStatusMsg("请求参数不合法");
				return JSONObject.toJSONString(baseResponse);
			}

			JSONObject jobj = JSONObject.parseObject(json);
			String adminid = jobj.getString("sessionid");

			Admin admin = this.getAdmin(adminid);
			if (admin == null) {
				baseResponse.setStatus(1);
				baseResponse.setStatusMsg("请重新登录");
				return JSONObject.toJSONString(baseResponse);
			}
			
			JSONObject whereJson = JSONObject.parseObject(json); 
			
			OperateDao od = new OperateDao();
			List<Map<String, String>> partnerBills = null;

			try {
				partnerBills = od.findPartnerBillDetail(whereJson);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				od.close();
			}  
			for (Map<String, String> map : partnerBills) {
				String month = map.get("month");
				String companyId = map.get("companyId");
				String ourCompanyId = map.get("ourCompanyId");
				List<BalanceAccount> balanceAccount = dao.findBalanceAccountById(ourCompanyId);
				List<Partner> partnerList = dao.findAllById(companyId);
				map.put("partner",  JSONObject.toJSONString(partnerList.get(0)));
				map.put("balanceAccount", JSONObject.toJSONString(balanceAccount.get(0)));
				List<Map<String, String>> billDetail = od.findBillDetail(month,companyId);
				map.put("billDetail", JSONObject.toJSONString(billDetail));
			}
			baseResponse.setReportforms_admin(partnerBills);
			baseResponse.setStatus(0);
			return JSONObject.toJSONString(baseResponse);
		}
		
		/**
		 * 修改账单
		 * @param request
		 * @param response
		 * @return
		 */
		@RequestMapping(value = "/updatePartnerBill", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
		@ResponseBody
		public String updatePartnerBill(HttpServletRequest request,HttpServletResponse response) {
			
			String json = this.checkParameter(request);
			PartnerBillLogic logic = new PartnerBillLogic(dao);
			return logic.updatePartnerBill(json);
		}


		@CrossOrigin(origins = "*", maxAge = 3600)
		@RequestMapping(value = "/updateAllStatus", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@ResponseBody
		public String updateAllStatus(HttpServletRequest request){
			BaseResponse baseResponse = new BaseResponse();
			String json = this.checkParameter(request);
			if (StringUtils.isStrEmpty(json)) {
				baseResponse.setStatus(2);
				baseResponse.setStatusMsg("请求参数不合法");
				return JSONObject.toJSONString(baseResponse);
			}
			JSONObject jobj = JSONObject.parseObject(json);
			String adminid = jobj.getString("sessionid");

			Admin admin = this.getAdmin(adminid);
			if (admin == null) {
				baseResponse.setStatus(1);
				baseResponse.setStatusMsg("请重新登录");
				return JSONObject.toJSONString(baseResponse);
			}

			String proxyId = jobj.getString("proxyId");
			if(StringUtils.isStrEmpty(proxyId)){
				baseResponse.setStatus(1);
				baseResponse.setStatusMsg("请重新登录");
				return JSONObject.toJSONString(baseResponse);
			}
			if(dao.updateAllStatusByProxyId(Long.valueOf(proxyId)) > 0){
                List<Channel> channels = dao.findChannelByProxyId(proxyId);
                if(!CollectionUtils.isEmpty(channels)){
                    Date date = new Date();
                    for(Channel channel : channels){
                        OperateChannelHistory channelHistory = new OperateChannelHistory();
                        channelHistory.setChannelId(channel.getId());
                        channelHistory.setUpdateBy(admin.getId());
                        channelHistory.setUpdateDate(date);
                        channelHistory.setLog(admin.getName()+"禁用了"+channel.getChannelName());
                        dao.saveChannelHistory(channelHistory);
                    }
                }


                return BaseController.success();
			}else{
				return BaseController.fail();
			}


		}

    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/getChannelHistory", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getChannelHistory(HttpServletRequest request){
        BaseResponse baseResponse = new BaseResponse();
        String json = this.checkParameter(request);
        if (StringUtils.isStrEmpty(json)) {
            baseResponse.setStatus(2);
            baseResponse.setStatusMsg("请求参数不合法");
            return JSONObject.toJSONString(baseResponse);
        }
        JSONObject jobj = JSONObject.parseObject(json);
        String adminid = jobj.getString("sessionid");

        Admin admin = this.getAdmin(adminid);
        if (admin == null) {
            baseResponse.setStatus(1);
            baseResponse.setStatusMsg("请重新登录");
            return JSONObject.toJSONString(baseResponse);
        }
        String channel = jobj.getString("channelId");
        OperateChannelHistory channelHistory = new OperateChannelHistory();
        channelHistory.setChannelId(Long.valueOf(channel));
        List<OperateChannelHistory> results = dao.findChannelHistory(channelHistory);
        baseResponse.setStatus(0);
        baseResponse.setStatusMsg("success");
        baseResponse.setChannelHistories(results);
        return JSONObject.toJSONString(baseResponse);


    }

		
		//总结中心  日报 传递app列表
		@RequestMapping(value = "/getDataDailyPara", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
		@ResponseBody
		public String getDataDaily(HttpServletRequest request,HttpServletResponse response) {
			
			String json = this.checkParameter(request);
			ConclusionLogic logic = new ConclusionLogic(dao);
			return logic.getDataDailyPara(json);
		}
		
		//获取日报数据
		@RequestMapping(value = "/getConclusionData", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
		@ResponseBody
		public String getConclusionData(HttpServletRequest request,HttpServletResponse response) {
			
			String json = this.checkParameter(request);
			ConclusionLogic logic = new ConclusionLogic(dao);
			return logic.getConclusionData(json);
		}
		
		//获取bd、市场曲线数据
		@RequestMapping(value = "/getBdCurveData", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
		@ResponseBody
		public String getBdCurveData(HttpServletRequest request,HttpServletResponse response) {
			
			String json = this.checkParameter(request);
			ConclusionLogic logic = new ConclusionLogic(dao);
			return logic.getBdCurveData(json);
		}
		
		//获取市场曲线数据
		@RequestMapping(value = "/getMarketCurveData", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
		@ResponseBody
		public String getMarketCurveData(HttpServletRequest request,HttpServletResponse response) {
			
			String json = this.checkParameter(request);
			ConclusionLogic logic = new ConclusionLogic(dao);
			return logic.getMarketCurveData(json);
		}
		
		//获取评论
		@RequestMapping(value = "/getCommentList", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
		@ResponseBody
		public String getCommentList(HttpServletRequest request,HttpServletResponse response) {
			
			String json = this.checkParameter(request);
			ConclusionLogic logic = new ConclusionLogic(dao);
			return logic.getCommentList(json);
		}
		//添加评论
		@RequestMapping(value = "/addComment", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
		@ResponseBody
		public String addComment(HttpServletRequest request,HttpServletResponse response) {
			
			String json = this.checkParameter(request);
			ConclusionLogic logic = new ConclusionLogic(dao);
			return logic.addComment(json);
		}
		
		//日报历史
		@RequestMapping(value = "/dailyHistory", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
		@ResponseBody
		public String dailyHistory(HttpServletRequest request,HttpServletResponse response) {
			
			String json = this.checkParameter(request);
			ConclusionLogic logic = new ConclusionLogic(dao);
			return logic.dailyHistory(json);
		}
		
		
		
		//日报历史的运营和续贷数据
		@RequestMapping(value = "/getOperateAndSecondData", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
		@ResponseBody
		public String getOperateAndSecondData(HttpServletRequest request,HttpServletResponse response) {
			
			String json = this.checkParameter(request);
			ConclusionLogic logic = new ConclusionLogic(dao);
			return logic.getOperateAndSecondData(json);
		}
		
		//日报历史bd和市场数据
		@RequestMapping(value = "/getBdAndMarketData", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
		@ResponseBody
		public String getBdAndMarketData(HttpServletRequest request,HttpServletResponse response) {

			String json = this.checkParameter(request);
			ConclusionLogic logic = new ConclusionLogic(dao);
			return logic.getBdAndMarketData(json);
		}
		//日报历史 运营数据下载
//		@RequestMapping(value = "/getBdAndMarketData", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
//		@CrossOrigin(origins="*",maxAge=3600)
//		@ResponseBody
//		public String exportBdAndMarketData(HttpServletRequest request,HttpServletResponse response) {
//
//			String json = this.checkParameter(request);
//			ConclusionLogic logic = new ConclusionLogic(dao);
//			return logic.exportBdAndMarketData(json);
//		}
		
		//日报历史bd和市场数据详情
		@RequestMapping(value = "/getBdAndMarketDeatailData", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
		@ResponseBody
		public String getBdAndMarketDeatailData(HttpServletRequest request,HttpServletResponse response) {

			String json = this.checkParameter(request);
			ConclusionLogic logic = new ConclusionLogic(dao);
			return logic.getBdAndMarketDeatailData(json);
		}

		//业绩查询的参数
		@RequestMapping(value = "/getPerformancePara", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
		@ResponseBody
		public String getPerformancePara(HttpServletRequest request,HttpServletResponse response) {

			String json = this.checkParameter(request);
			ConclusionLogic logic = new ConclusionLogic(dao);
			return logic.getPerformancePara(json);
		}
		
		//业绩查询通过渠道商id获取渠道的参数
		@RequestMapping(value = "/getChannels", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
		@ResponseBody
		public String getChannels(HttpServletRequest request,HttpServletResponse response) {

			String json = this.checkParameter(request);
			ConclusionLogic logic = new ConclusionLogic(dao);
			return logic.getChannels(json);
		}
		
		//业绩查询
		@RequestMapping(value = "/getPerformance", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
		@ResponseBody
		public String getPerformance(HttpServletRequest request,HttpServletResponse response) {

			String json = this.checkParameter(request);
			ConclusionLogic logic = new ConclusionLogic(dao);
			return logic.getPerformance(json);
		}

		//业绩下载
		@RequestMapping(value = "/exportPerformance", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
		@ResponseBody
		public void exportPerformance(HttpServletRequest request,HttpServletResponse response) {

			String json = this.checkParameter(request);
			ConclusionLogic logic = new ConclusionLogic(dao);
			logic.exportPerformance(json,response);
		}
		
		//下载运营日报数据
		@RequestMapping(value = "/exportOperateData", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
		@ResponseBody
		public void exportOperateData(HttpServletRequest request,HttpServletResponse response) {

			String json = this.checkParameter(request);
			ConclusionLogic logic = new ConclusionLogic(dao);
			 logic.exportOperateData(json,response);
		}
		
		//下载bd日报数据
		@RequestMapping(value = "/exportBdData", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
		@ResponseBody
		public void exportBdData(HttpServletRequest request,HttpServletResponse response) {

			String json = this.checkParameter(request);
			ConclusionLogic logic = new ConclusionLogic(dao);
			logic.exportBdData(json,response);
		}

		//下载续贷日报数据
		@RequestMapping(value = "/exportSecondData", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
		@ResponseBody
		public void exportSecondData(HttpServletRequest request,HttpServletResponse response) {

			String json = this.checkParameter(request);
			ConclusionLogic logic = new ConclusionLogic(dao);
			logic.exportSecondData(json,response);
		}
		
		//下载市场日报数据
		@RequestMapping(value = "/exportMarketData", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
		@ResponseBody
		public void exportMarketData(HttpServletRequest request,HttpServletResponse response) {

			String json = this.checkParameter(request);
			ConclusionLogic logic = new ConclusionLogic(dao);
			logic.exportMarketData(json,response);
		}

		//下载bd和市场  历史日报
		@RequestMapping(value = "/exportgetBdAndMarketData", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
		@ResponseBody
		public void exportgetBdAndMarketData(HttpServletRequest request,HttpServletResponse response) {

			String json = this.checkParameter(request);
			ConclusionLogic logic = new ConclusionLogic(dao);
			logic.exportgetBdAndMarketData(json,response);
		}

		//下载运营和续贷  日报历史
		@RequestMapping(value = "/exportOperateAndSecondData", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
		@ResponseBody
		public void exportOperateAndSecondData(HttpServletRequest request,HttpServletResponse response) {

			String json = this.checkParameter(request);
			ConclusionLogic logic = new ConclusionLogic(dao);
			logic.exportOperateAndSecondData(json,response);
		}
		
		//下载bd和市场  日报历史 详情
		@RequestMapping(value = "/exportBdAndMarketDeatailData", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
		@ResponseBody
		public void exportBdAndMarketDeatailData(HttpServletRequest request,HttpServletResponse response) {

			String json = this.checkParameter(request);
			ConclusionLogic logic = new ConclusionLogic(dao);
			logic.exportBdAndMarketDeatailData(json,response);
 		}
		

		//上传财务信息
		@RequestMapping(value = "/FinanceTask", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
		@ResponseBody
		public String FinanceTask(HttpServletRequest request,HttpServletResponse response) {
			String json = this.checkParameter(request);
			FinanceLogic logic = new FinanceLogic(dao);
			return logic.FinanceTask(json);
			
 		}
		
		// 相关产品 报表 表头配置
		@RequestMapping(value = "/getTableColumns", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
		@ResponseBody
		public String getTableColumns(HttpServletRequest request,HttpServletResponse response) {
			String json = this.checkParameter(request);
			PartnerBillLogic logic = new PartnerBillLogic(dao);
			return logic.getTableColumns(json);
			
 		}
		
		//保存相关产品  报表 表头的配置
		@RequestMapping(value = "/updateTableColumns", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@CrossOrigin(origins="*",maxAge=3600)
		@ResponseBody
		public String updateTableColumns(HttpServletRequest request,HttpServletResponse response) {
			String json = this.checkParameter(request);
			PartnerBillLogic logic = new PartnerBillLogic(dao);
			return logic.updateTableColumns(json);
			
 		}
		
		
		
}


























