package com.maimob.server.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.maimob.server.data.task.TaskLine;
import com.maimob.server.db.entity.Admin;
import com.maimob.server.db.entity.AdminPermission;
import com.maimob.server.db.entity.Channel;
import com.maimob.server.db.entity.ChannelPermission;
import com.maimob.server.db.entity.Dictionary;
import com.maimob.server.db.entity.Operate_reportform;
import com.maimob.server.db.entity.Operate_reportform_day;
import com.maimob.server.db.entity.Operate_reportform_month;
import com.maimob.server.db.entity.Optimization;
import com.maimob.server.db.entity.OptimizationTask;
import com.maimob.server.db.entity.Permission;
import com.maimob.server.db.entity.Proxy;
import com.maimob.server.db.entity.Reward;
import com.maimob.server.db.entity.UserPermission;
import com.maimob.server.db.service.DaoService;
import com.maimob.server.db.service.SMSRecordService;
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

		for (int i = 0; i < channels.size(); i++) {
			Channel channel = channels.get(i);
			if (channel.getLevel() == 1) {
				channelNameList.add(channel.getChannelName());
				channelNoList.add(channel.getChannel());
			} else if (channel.getLevel() == 2) {
				channelNameList.add("--" + channel.getChannelName());
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

		Cache.DicCatche(dao);
		List<Dictionary> dic3 = Cache.getDicList(3);
		List<Dictionary> dic4 = Cache.getDicList(4);
		List<Dictionary> dic5 = Cache.getDicList(5);

		baseResponse.setChannelAttribute(dic3);
		baseResponse.setChannelType(dic4);
		baseResponse.setChannelSubdivision(dic5);
		baseResponse.setStatus(0);
		baseResponse.setStatusMsg("");
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

		List<Reward> rewardList = null;
		if (!json.equals("")) {
			try {
				json = URLDecoder.decode(json, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			JSONObject whereJson = JSONObject.parseObject(json);
			String channelId = whereJson.getString("channelId");
			String id = whereJson.getString("rewardId");
			if (!StringUtils.isStrEmpty(id)) {
				rewardList = dao.findRewardById(Long.parseLong(id));
			} else if (!StringUtils.isStrEmpty(channelId)) {
				rewardList = dao.findRewardByChannelId(Long.parseLong(channelId));

				List<Reward> rewardList2 = new ArrayList<Reward>();
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
				rewardList = rewardList2;

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

		ChannelPermission channelPermission = null;
		if (!json.equals("")) {
			try {
				json = URLDecoder.decode(json, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			JSONObject whereJson = JSONObject.parseObject(json);
			String id = whereJson.getString("permissionId");
			if (!StringUtils.isStrEmpty(id)) {
				channelPermission = dao.findChannelPermissionById(Long.parseLong(id));
			}

		}

		baseResponse.setChannelPermission(channelPermission);
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

		Cache.channelCatche(dao);
		OperateDao od = new OperateDao();
		try {
			first = Integer.parseInt(jobj.getString("first"));

			List<Map<String, String>> reportforms1;
			if (first == 0) {
				
				reportforms1 = od.findSumFormDayOperate(null, jobj,"");
				List<Map<String, String>> ad = od.findAdminSumFormDayOperate(null, jobj,"");
				Map<String, String> or = reportforms1.get(0);
				or.put("adminName", ad.size()+"个负责人");
				reportforms1.addAll(ad);
				Cache.setOperate_reportformOperate(Long.parseLong(adminid), reportforms1);
				long listSize = od.findFormCou(null, null, jobj, dateType,"");
				baseResponse.setListSize(listSize + "");
			}
	        else
	        {
	        		reportforms1 = Cache.getOperate_reportformOperate(Long.parseLong(adminid));
	        }

	        Cache.setLastTime(Long.parseLong(adminid), System.currentTimeMillis());

			if (dateType.equals("1")) {
				List<Map<String, String>> reportforms = od.findFormOperate(null, null, jobj);
	        		reportforms.addAll(0, reportforms1);
				baseResponse.setReportforms_operate(reportforms);
			} else {
				List<Map<String, String>> reportforms = od.findFormMonthOperate(null, null, jobj);
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

		Cache.channelCatche(dao);
		OperateDao od = new OperateDao();
		try {
			first = Integer.parseInt(jobj.getString("first"));

			List<Map<String, String>> reportforms1;
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

	        Cache.setLastTime(Long.parseLong(adminid), System.currentTimeMillis());

			if (dateType.equals("1")) {
				List<Map<String, String>> reportforms = od.findFormOperateApp(null, null, jobj);
	        		reportforms.addAll(0, reportforms1);
				baseResponse.setReportforms_operate(reportforms);
			} else {
				List<Map<String, String>> reportforms = od.findFormMonthOperateApp(null, null, jobj);
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
		

		
        int channelflag = 0;
        int channelidflag = 0;
        int channeltypeflag = 0;
        int adminflag = 0;
        int h5flag = 0;
        int creditflag =0 ;
        int firstgetflag = 0;
        int secondgetflag =0;
        int outflag = 0;
		//都没有勾选时默认标志为0，下载的表格要移除相应字段
		if("[]".equals(arr.toString())) {
		}
		else {
			for(Object object : arr) {
				if("渠道".equals(object.toString())) {
					channelflag = 1;
				}
				if("渠道号".equals(object.toString())) {
					channelidflag = 1;
				}
				if("渠道分类".equals(object.toString())) {
					channeltypeflag = 1;
				}
				if("负责人".equals(object.toString())) {
					adminflag = 1;
				}
				if("H5".equals(object.toString())) {
					h5flag = 1;
				}
				if("额度".equals(object.toString())) {
					creditflag = 1;
				}
				if("首贷".equals(object.toString())) {
					firstgetflag = 1;
				}
				if("续贷".equals(object.toString())) {
					secondgetflag = 1;
				}
				if("外部".equals(object.toString())) {
					outflag = 1;
				}
			}
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

		Cache.channelCatche(dao);
		OperateDao od = new OperateDao();
		List<Map<String, String>> reportforms = null;
		try {
			first = Integer.parseInt(jobj.getString("first"));
			List<Map<String, String>> reportforms1;
			if (first == 0) {
				reportforms1 = od.findSumFormDayOperate(null, jobj,"");
				List<Map<String, String>> ad = od.findAdminSumFormDayOperate(null, jobj,"");
				Map<String, String> or = reportforms1.get(0);
				or.put("adminName", ad.size()+"个负责人");
				reportforms1.addAll(ad);
				Cache.setOperate_reportformOperate(Long.parseLong(adminid), reportforms1);
				long listSize = od.findFormCou(null, null, jobj, dateType,"");
				baseResponse.setListSize(listSize + "");
			}
			 else
		        {
		        		reportforms1 = Cache.getOperate_reportformOperate(Long.parseLong(adminid));
		        }

			//reportforms1中map这几个key没有值，默认为null，表格显示会错位，添加这几个key的value为空字符串""
			for(Map<String,String> map:reportforms1) {
				if(null == map.get("channelName")) {
					map.put("channelName", "");
				}
				if(null == map.get("channelType")) {
					map.put("channelType", "");
				}
				map.put("optimization", "");
				map.put("cost2", "");
				map.put("registerConversion", "");
				map.put("outUpload", "");
				map.put("outFirstGetSum", "");
			}
			
			if (dateType.equals("1")) {
				reportforms = od.findFormOperateAll(null, null, jobj);
				reportforms.addAll(0, reportforms1);
			} else {
				reportforms = od.findFormMonthOperateAll(null, null, jobj);
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
				map.put("registerConversion", "");
			}
			if(null == map.get("outFirstGetSum")) {
				map.put("outFirstGetSum", "");
			}
			if(null == map.get("cost2")) {
				map.put("cost2", "");
			}
			if(null == map.get("optimization")) {
				map.put("optimization", "");
			}
			
			for(String key:map.keySet()) {
				String value = map.get(key);
				if(value == null) {
					map.put(key, "");
				}
			}
		}

		List<String> listName = new ArrayList<>();
        listName.add("日期");
        listName.add("渠道");
        listName.add("渠道号");
        listName.add("渠道分类");
        listName.add("负责人");
        listName.add("H5点击");
        listName.add("H5注册");
        listName.add("H5激活转化(%)");
        listName.add("总注册数");
        listName.add("外部注册");
        listName.add("注册转化(%)");
        listName.add("激活");
        listName.add("进件");
        listName.add("外部进件");
        listName.add("进件转化(%)");
        listName.add("开户");
        listName.add("外部开户");
        listName.add("开户转化(%)");
        listName.add("提现");
        listName.add("提现转化(%)");
        listName.add("放款数");
        listName.add("授信总额");
        listName.add("人均批额");
        listName.add("首提人数");
        listName.add("外部首提");
        listName.add("首贷金额");
        listName.add("外部首贷金额");
        listName.add("首贷笔均");
        listName.add("续贷人数");
        listName.add("续放笔数");
        listName.add("续贷金额");
        listName.add("续贷笔均");
        listName.add("渠道提现总额");
        listName.add("外部渠道提现金额");
        listName.add("渠道笔均金额");
        listName.add("收入");
        listName.add("计算的成本");
        listName.add("导入的成本");
        listName.add("毛利");
        listName.add("毛利率(%)");
        listName.add("优化比例(%)");
        List<String> listId = new ArrayList<>();
        listId.add("date");           //时间
        listId.add("channelName");    //渠道
        listId.add("channel");        //渠道号
        listId.add("channelType");    //渠道分类
        listId.add("adminName");      //负责人
        listId.add("h5Click");        //h5点击
        listId.add("h5Register");     //h5注册
        listId.add("activationConversion");     //h5激活转化
        listId.add("register");       //注册数
        listId.add("outRegister");     //外部注册
        listId.add("registerConversion");     //注册转化
        listId.add("activation");     //激活 
        listId.add("upload");         //进件数
        listId.add("outUpload");         //外部进件
        listId.add("uploadConversion");      //进件转化
        listId.add("account");         //开户数 
        listId.add("outAccount");        //外部开户
        listId.add("accountConversion");      //开户转化
        listId.add("loan");      //提现
        listId.add("loanConversion");      //提现转化
        listId.add("loaner");            //放款数
        listId.add("credit");         //授信总额
        listId.add("perCapitaCredit"); //人均批额
        listId.add("firstGetPer");     //首提人数
        listId.add("outFirstGetPer");      //外部首提
        listId.add("firstGetSum");      //首贷金额
        listId.add("outFirstGetSum");   //外部首贷金额
        listId.add("firstPerCapitaCredit");       //首贷笔均
        listId.add("secondGetPer");       //续贷人数
        listId.add("secondGetPi");       //续贷笔数
        listId.add("secondGetSum");       //续贷金额
        listId.add("secondPerCapitaCredit");       //续贷笔均
        listId.add("channelSum");       //渠道提现总额
        listId.add("outChannelSum");        //外部渠道提现金额
        listId.add("channelCapitaCredit");       //渠道笔均金额
        listId.add("income");        //收入
        listId.add("cost");        //计算的成本
        listId.add("cost2");        //导入的成本
        listId.add("grossProfit");        //毛利  
        listId.add("grossProfitRate");        //毛利率
        listId.add("optimization");    //优化比例
        if(channelflag == 0) {
        	listName.remove("渠道");
        	listId.remove("channelName");   
        }
        if(channelidflag == 0) {
        	listName.remove("渠道号");
        	listId.remove("channel");    
        }
        if(channeltypeflag == 0) {
        	listName.remove("渠道分类");
        	listId.remove("channelType");  
        }
        if(adminflag == 0) {
        	listName.remove("负责人");
        	listId.remove("adminName"); 
        }
        if(h5flag == 0) {
        	 listName.remove("H5点击");
        	 listName.remove("H5注册");
//        	 listName.remove("优化比例(%)");
        	 listName.remove("激活");
        	 listName.remove("H5激活转化(%)");
        	 listId.remove("h5Click");        //h5点击
        	 listId.remove("h5Register");     //h5注册
//        	 listId.remove("optimization");    //优化比例
        	 listId.remove("activation");     //激活 
        	 listId.remove("activationConversion");     //h5激活转化
        }
        if(creditflag == 0) {
            listName.remove("授信总额");
            listName.remove("人均批额");
            listId.remove("credit");         //授信总额
            listId.remove("perCapitaCredit"); //人均批额
        }
        if(firstgetflag == 0) {
        	listName.remove("首提人数");
        	listName.remove("首贷金额");
        	listName.remove("外部首贷金额");   
        listName.remove("首贷笔均");
        listId.remove("firstGetPer");     //首提人数
        listId.remove("firstGetSum");      //首贷总额
        listId.remove("outFirstGetSum");   //外部首贷金额
        listId.remove("firstPerCapitaCredit");       //首贷笔均
        }
        if(secondgetflag == 0) {
        listName.remove("续贷人数");
        listName.remove("续放笔数");
        listName.remove("续贷金额");
        listName.remove("续贷笔均");
        listId.remove("secondGetPer");       //续贷人数
        listId.remove("secondGetPi");       //续放笔数
        listId.remove("secondGetSum");       //续贷金额
        listId.remove("secondPerCapitaCredit");       //续贷笔均
        }
        if(outflag == 0) {
        	listName.remove("外部注册");
        	listName.remove("外部进件");
        	listName.remove("外部开户");
        	listName.remove("外部首提");
        	listName.remove("外部渠道提现金额");
        	listId.remove("outRegister");     //外部注册
        	listId.remove("outUpload");       //外部进件
        	listId.remove("outAccount");        //外部开户
        	listId.remove("outFirstGetPer");      //外部首提
        	listId.remove("outChannelSum");        //外部渠道提现金额
        }

        ExportMapExcel exportExcelUtil = new ExportMapExcel();
        exportExcelUtil.exportExcelString("运营数据详情统计表",listName,listId,reportforms,response);

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
			
	        int channelflag = 0;
	        int channelidflag = 0;
	        int adminflag = 0;
	        
	      //都没有勾选时默认标志为0，下载的表格要移除相应字段
			if("[]".equals(arr.toString())) {
			}
			else {
				for(Object object : arr) {
					if("渠道".equals(object.toString())) {
						channelflag = 1;
					}
					if("渠道号".equals(object.toString())) {
						channelidflag = 1;
					}
//					if("渠道分类".equals(object.toString())) {
//						channeltypeflag = 1;
//					}
					if("负责人".equals(object.toString())) {
						adminflag = 1;
					}
//					if("H5".equals(object.toString())) {
//						h5flag = 1;
//					}
//					if("额度".equals(object.toString())) {
//						creditflag = 1;
//					}
//					if("首贷".equals(object.toString())) {
//						firstgetflag = 1;
//					}
//					if("续贷".equals(object.toString())) {
//						secondgetflag = 1;
//					}
//					if("外部".equals(object.toString())) {
//						outflag = 1;
//					}
				}
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

			Cache.channelCatche(dao);
			OperateDao od = new OperateDao();
			
			List<Map<String, String>> reportforms = null;
			try {
				first = Integer.parseInt(jobj.getString("first"));
				List<Map<String, String>> reportforms1;

				
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
		        
				
				for (Map<String, String> map : reportforms1) {
					if(map.get("channelName") == null) {
						map.put("channelName", "");
					}
					if(map.get("channelType") == null) {
						map.put("channelType", "");
					}
					if(map.get("adminName") == null) {
						map.put("adminName", "");
					}
					
					
				}

				
				if (dateType.equals("1")) {
					 reportforms = od.findFormOperateAppDown(null, null, jobj);
		        		reportforms.addAll(0, reportforms1);
					baseResponse.setReportforms_operate(reportforms);
				} else {
					reportforms = od.findFormMonthOperateAppDown(null, null, jobj);
					reportforms.addAll(0, reportforms1);
					baseResponse.setReportforms_operate(reportforms);
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				od.close();
			}
			
			List<String> listName = new ArrayList<>();
	        listName.add("日期");
	        listName.add("渠道");
	        listName.add("渠道号");
	        listName.add("渠道分类");
	        listName.add("负责人");
	        listName.add("APP注册");
	        listName.add("上传证件");
	        listName.add("传证转化(%)");
	        listName.add("绑卡");
	        listName.add("绑卡转化(%)");
	        listName.add("基本信息");
	        listName.add("信息转化(%)");
	        listName.add("联系人");
	        listName.add("联系人转化(%)");
	        listName.add("视频录制");
	        listName.add("视频转化(%)");
	        listName.add("进件中银");
	        listName.add("进件转化(%)");
	        listName.add("退件补件");
	        listName.add("开户成功");
	        listName.add("开户成功率(%)");
	        listName.add("开户总转化(%)");
	        listName.add("注册失败率(%)");
//	        listName.add("放款数");
//	        listName.add("授信总额");
//	        listName.add("人均批额");
//	        listName.add("首提人数");
//	        listName.add("外部首提");
//	        listName.add("首贷金额");
//	        listName.add("外部首贷金额");
//	        listName.add("首贷笔均");
//	        listName.add("续贷人数");
//	        listName.add("续放笔数");
//	        listName.add("续贷金额");
//	        listName.add("续贷笔均");
//	        listName.add("渠道提现总额");
//	        listName.add("外部渠道提现金额");
//	        listName.add("渠道笔均金额");
//	        listName.add("收入");
//	        listName.add("计算的成本");
//	        listName.add("导入的成本");
//	        listName.add("毛利");
//	        listName.add("毛利率(%)");
//	        listName.add("优化比例(%)");
	        List<String> listId = new ArrayList<>();
	        listId.add("date");           //时间
	        listId.add("channelName");    //渠道
	        listId.add("channel");        //渠道号
	        listId.add("channelType");    //渠道分类
	        listId.add("adminName");      //负责人
	        listId.add("register");        //APP注册
	        listId.add("idcard");     //上传证件
	        listId.add("idcardConversion");     //传证转化
	        listId.add("debitCard");       //绑卡
	        listId.add("debitCardConversion");     //绑卡转化
	        listId.add("homeJob");     //基本信息
	        listId.add("homeJobConversion");     //信息转化
	        listId.add("contacts");         //联系人
	        listId.add("contactsConversion");         //联系人转化
	        listId.add("vedio");      //视频录制
	        listId.add("vedioConversion");         //视频转化
	        listId.add("upload");        //进件中银
	        listId.add("uploadConversion");      //进件转化
	        listId.add("unaccount");      //退件补件
	        listId.add("account");      //开户成功
	        listId.add("accountConversion");            //开户成功率
	        listId.add("accountAllConversion");         //开户总转化
	        listId.add("lostConversion"); //注册失败率
//	        listId.add("firstGetPer");     //首提人数
//	        listId.add("outFirstGetPer");      //外部首提
//	        listId.add("firstGetSum");      //首贷金额
//	        listId.add("outFirstGetSum");   //外部首贷金额
//	        listId.add("firstPerCapitaCredit");       //首贷笔均
//	        listId.add("secondGetPer");       //续贷人数
//	        listId.add("secondGetPi");       //续贷笔数
//	        listId.add("secondGetSum");       //续贷金额
//	        listId.add("secondPerCapitaCredit");       //续贷笔均
//	        listId.add("channelSum");       //渠道提现总额
//	        listId.add("outChannelSum");        //外部渠道提现金额
//	        listId.add("channelCapitaCredit");       //渠道笔均金额
//	        listId.add("income");        //收入
//	        listId.add("cost");        //计算的成本
//	        listId.add("cost2");        //导入的成本
//	        listId.add("grossProfit");        //毛利  
//	        listId.add("grossProfitRate");        //毛利率
//	        listId.add("optimization");    //优化比例
	        if(channelflag == 0) {
	        	listName.remove("渠道");
	        	listId.remove("channelName");   
	        }
	        if(channelidflag == 0) {
	        	listName.remove("渠道号");
	        	listId.remove("channel");    
	        }
//	        if(channeltypeflag == 0) {
//	        	listName.remove("渠道分类");
//	        	listId.remove("channelType");  
//	        }
	        if(adminflag == 0) {
	        	listName.remove("负责人");
	        	listId.remove("adminName"); 
	        }
//	        if(h5flag == 0) {
//	        	 listName.remove("H5点击");
//	        	 listName.remove("H5注册");
//	        	 listName.remove("激活");
//	        	 listName.remove("H5激活转化(%)");
//	        	 listId.remove("h5Click");        //h5点击
//	        	 listId.remove("h5Register");     //h5注册
//	        	 listId.remove("activation");     //激活 
//	        	 listId.remove("activationConversion");     //h5激活转化
//	        }
//	        if(creditflag == 0) {
//	            listName.remove("授信总额");
//	            listName.remove("人均批额");
//	            listId.remove("credit");         //授信总额
//	            listId.remove("perCapitaCredit"); //人均批额
//	        }
//	        if(firstgetflag == 0) {
//	        	listName.remove("首提人数");
//	        	listName.remove("首贷金额");
//	        	listName.remove("外部首贷金额");   
//	        listName.remove("首贷笔均");
//	        listId.remove("firstGetPer");     //首提人数
//	        listId.remove("firstGetSum");      //首贷总额
//	        listId.remove("outFirstGetSum");   //外部首贷金额
//	        listId.remove("firstPerCapitaCredit");       //首贷笔均
//	        }
//	        if(secondgetflag == 0) {
//	        listName.remove("续贷人数");
//	        listName.remove("续放笔数");
//	        listName.remove("续贷金额");
//	        listName.remove("续贷笔均");
//	        listId.remove("secondGetPer");       //续贷人数
//	        listId.remove("secondGetPi");       //续放笔数
//	        listId.remove("secondGetSum");       //续贷金额
//	        listId.remove("secondPerCapitaCredit");       //续贷笔均
//	        }
//	        if(outflag == 0) {
//	        	listName.remove("外部注册");
//	        	listName.remove("外部进件");
//	        	listName.remove("外部开户");
//	        	listName.remove("外部首提");
//	        	listName.remove("外部渠道提现金额");
//	        	listId.remove("outRegister");     //外部注册
//	        	listId.remove("outUpload");       //外部进件
//	        	listId.remove("outAccount");        //外部开户
//	        	listId.remove("outFirstGetPer");      //外部首提
//	        	listId.remove("outChannelSum");        //外部渠道提现金额
//	        }

	        ExportMapExcel exportExcelUtil = new ExportMapExcel();
	        exportExcelUtil.exportExcelString("流程转化表",listName,listId,reportforms,response);

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
			
//			OperateDao od = new OperateDao();
//			od.
			
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
			List<Map<String, String>> pList = od.getAdminPermission(type,adminid,"0");
			od.close();
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
			long id = od.addAllAdminPermission(per);
			od.close();
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
			List<UserPermission> userPermission = od.getAllAdminPermission(sessionid,"0");
			od.close();
			
			BaseResponse baseResponse = new BaseResponse();
			baseResponse.setStatus(0);
			baseResponse.setUserPermission(userPermission);
			baseResponse.setStatusMsg("");
			return JSONObject.toJSONString(baseResponse);
			
		}
		
		
		
}
