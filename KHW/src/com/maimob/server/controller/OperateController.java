package com.maimob.server.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
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

import com.alibaba.fastjson.JSONObject;
import com.maimob.server.data.task.TaskLine;
import com.maimob.server.db.entity.Admin;
import com.maimob.server.db.entity.Channel;
import com.maimob.server.db.entity.ChannelPermission;
import com.maimob.server.db.entity.Dictionary;
import com.maimob.server.db.entity.Operate_reportform;
import com.maimob.server.db.entity.Operate_reportform_day;
import com.maimob.server.db.entity.Operate_reportform_month;
import com.maimob.server.db.entity.Optimization;
import com.maimob.server.db.entity.OptimizationTask;
import com.maimob.server.db.entity.Proxy;
import com.maimob.server.db.entity.Reward;
import com.maimob.server.db.service.DaoService;
import com.maimob.server.db.service.SMSRecordService;
import com.maimob.server.importData.dao.OperateDao;
import com.maimob.server.protocol.BaseResponse;
import com.maimob.server.utils.AppTools;
import com.maimob.server.utils.Cache;
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
	@RequestMapping(value = "/checkChannelNo", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String checkChannelNo(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("checkChannelNo");
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

		String channelNo = jobj.getString("channelNo");

		long cou = dao.findCouByChannelNo(channelNo);
		if (cou == 0) {
			baseResponse.setStatus(0);
			baseResponse.setStatusMsg("渠道号可用");
		} else {
			baseResponse.setStatus(2);
			baseResponse.setStatusMsg("渠道号已经存在！");
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

			if (first == 0) {
				long listSize = od.findFormCou(null, null, jobj, dateType);
				baseResponse.setListSize(listSize + "");
			}

			if (dateType.equals("1")) {
				List<Map<String, String>> reportforms = od.findFormOperate(null, null, jobj);
				baseResponse.setReportforms_operate(reportforms);
			} else {
				List<Map<String, String>> reportforms = od.findFormMonthOperate(null, null, jobj);
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

}
