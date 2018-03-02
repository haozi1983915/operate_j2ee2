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

import com.alibaba.fastjson.JSONObject;
import com.maimob.server.db.entity.Admin;
import com.maimob.server.db.entity.Channel;
import com.maimob.server.db.entity.ChannelPermission;
import com.maimob.server.db.entity.Dictionary;
import com.maimob.server.db.entity.Operate_reportform;
import com.maimob.server.db.entity.Proxy;
import com.maimob.server.db.entity.Reward;
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
@RequestMapping("/Index")
public class IndexController extends BaseController {

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
			// String adminid = jobj.getString("sessionid");
			//
			// Admin admin = this.getAdmin(adminid);
			// if(admin == null)
			// {
			// baseResponse.setStatus(1);
			// baseResponse.setStatusMsg("请重新登录");
			// return JSONObject.toJSONString(baseResponse);
			// }

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
	@RequestMapping(value = "/addProxy", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String addProxy(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("addProxy");
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

		Proxy proxy = JSONObject.parseObject(json, Proxy.class);

		String statusMsg = "";
		int status = 0;
		String check = proxy.check();
		if (check.equals("")) {
			long id = proxy.getId();

			try {
				dao.saveProxy(proxy);
				baseResponse.setId(id);
				statusMsg = "添加渠道商成功";
				status = 0;
			} catch (Exception e) {
				String msg = e.getMessage();

				statusMsg = "保存失败";
				status = 2;
				System.out.println(msg);
			}

		} else {
			status = 2;
			statusMsg = check;
		}

		baseResponse.setStatus(status);
		baseResponse.setStatusMsg(statusMsg);
		String content = JSONObject.toJSONString(baseResponse);
		logger.debug("register content = {}", content);
		return content;
	}

	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/addChannel", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String addChannel(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("addChannel");
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

		String linkage = jobj.getString("linkage");
		Channel channel = JSONObject.parseObject(json, Channel.class);

		String statusMsg = "";
		int status = 2;

		if (channel.getRewardId() != 0) {
			List<Reward> rewardlist1 = dao.findRewardById(channel.getRewardId());
			List<Reward> rewardlist2 = channel.getRewards();
			
			if(rewardlist1.size() != rewardlist2.size())
			{
				channel.setRewardId(0);
			}
			else
			{

				for (int i = 0; i < rewardlist1.size(); i++) {
					rewardlist1.get(i).setUpdateAdminId(0);
					if (rewardlist2.get(i) != null)
						rewardlist2.get(i).setUpdateAdminId(0);
				}

				String content1 = JSONObject.toJSONString(rewardlist1);
				String content2 = JSONObject.toJSONString(rewardlist2);

				channel = JSONObject.parseObject(json, Channel.class);
				if (!content1.equals(content2)) {

					channel.setRewardId(0);
				}

			}
			
		}

		if (channel.getRewards() != null && channel.getRewards().size() > 0)
			channel.setAdminId(channel.getRewards().get(0).getAdminId());
		channel.setStatus(1);
		String proxyId = jobj.getString("proxyId");
		String check = channel.check();
		if (check.equals("")) {

			try {
				dao.saveChannel(channel);
				baseResponse.setId(channel.getId());
				dao.updateProxy(channel);
				

				if (linkage != null && linkage.equals("1")) {
					List<Reward> rs = channel.getRewards();
					List<Channel> cs = dao.findChannelByProxyId(proxyId);
					for (int i = 0; i < cs.size(); i++) {
						
						Channel otherChannel = cs.get(i);
						if(otherChannel.getLevel() == 1)
							continue;
						
						otherChannel.setRewardId(0);
						for(int j = 0;j < rs.size();j++)
						{
							rs.get(j).setChannelId(otherChannel.getId());
							rs.get(j).setId(0);
						}
						otherChannel.setRewards(rs);
						
						otherChannel.setAttribute(channel.getAttribute());
						otherChannel.setType(channel.getType());
						otherChannel.setSubdivision(channel.getSubdivision());
						otherChannel.setAdminId(channel.getAdminId());
						otherChannel.setRewardTypeId(channel.getRewardTypeId());
						
						String check1 = otherChannel.check();
						if (check1.equals("")) {
							
							
							dao.saveChannel(otherChannel);
							
						}
						
					}

				}

				
				
//				if (channel.getLevel() == 1) {
//					dao.updateChannelType(channel.getRewardPrice(), channel.getRewardTypeId(), channel.getRewardId(),
//							channel.getProxyId(), channel.getAttribute(), channel.getType(), channel.getSubdivision(),
//							channel.getAdminId());
//					Cache.channelCatche(dao, channel.getProxyId() + "");
//				}

				statusMsg = "添加渠道商成功";
				status = 0;
			} catch (Exception e) {
				e.printStackTrace();
				String msg = e.getMessage();

				statusMsg = "渠道号重复！";
				status = 2;
				System.out.println(msg);
			}

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

		JSONObject jobj = JSONObject.parseObject(json);
		String adminid = jobj.getString("sessionid");

		Admin admin = this.getAdmin(adminid);
		if (admin == null) {
			baseResponse.setStatus(1);
			baseResponse.setStatusMsg("请重新登录");
			return JSONObject.toJSONString(baseResponse);
		}

		String otheradminId = "";
		if (!json.equals("")) {
			try {
				json = URLDecoder.decode(json, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			JSONObject whereJson = JSONObject.parseObject(json);
			otheradminId = whereJson.getString("adminId");
		}

		int level = admin.getLevel();
		List<Channel> channels = null;
		List<Long> ids = new ArrayList<Long>();
		if (!StringUtils.isStrEmpty(otheradminId)) {

		} else {
			if (level > 1) {

				if (level == 2) {
					List<Admin> ads = dao.findAdminByHigherid(admin.getId());
					for (int i = 0; i < ads.size(); i++) {
						ids.add(ads.get(i).getId());
					}
					ids.add(admin.getId());
				} else if (level == 3) {
					ids.add(admin.getId());
				}

			}
		}
		if (ids.size() > 0) {
			ids.add(0l);
		}

		int first = 1;

		try {
			first = Integer.parseInt(jobj.getString("first"));
			baseResponse.setListSize(0 + "");
			if (first == 0) {

				long listSize = dao.findChannelCouByAdminids(ids, jobj);
				baseResponse.setListSize(listSize + "");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		channels = dao.findChannelByAdminids(ids, jobj);

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

		int level = admin.getLevel();
		List<Channel> channels = null;
		List<Long> ids = new ArrayList<Long>();
		if (!StringUtils.isStrEmpty(otheradminId)) {
			ids.add(Long.parseLong(otheradminId));
		} else {
			if (level > 1) {

				if (level == 2) {
					List<Admin> ads = dao.findAdminByHigherid(admin.getId());
					for (int i = 0; i < ads.size(); i++) {
						ids.add(ads.get(i).getId());
					}
					ids.add(admin.getId());
				} else if (level == 3) {
					ids.add(admin.getId());
				}
			}
		}

		channels = dao.findChannelByAdminids(ids, jobj);

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
		
		String channelid = jobj.getString("channelId");
		String status = jobj.getString("status");
		dao.updateChannelStuts(Long.parseLong(channelid), Integer.parseInt(status));
		

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

		List<Long> proxyids = new ArrayList<Long>();
		int level = admin.getLevel();
		List<Proxy> proxys = null;

//		List<Long> ids = new ArrayList<Long>();
//		if (level > 1) {
//			List<Long> channels = null;
//
//			if (level == 2) {
//				List<Admin> ads = dao.findAdminByHigherid(admin.getId());
//				for (int i = 0; i < ads.size(); i++) {
//					ids.add(ads.get(i).getId());
//				}
//				ids.add(admin.getId());
//			} else if (level == 3) {
//				ids.add(admin.getId());
//			}
//
//			channels = dao.findProxyidByAdminids(ids);
//
//			for (int i = 0; i < channels.size(); i++) {
//				proxyids.add(channels.get(i));
//			}
//
//		}

//		if (level == 1 || proxyids.size() > 0) {} else {
//			baseResponse.setListSize("0");
//		}
		

		int first = 1;

		try {
			first = Integer.parseInt(whereJson.getString("first"));
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (first == 0) {
			long listSize = dao.findProxyCouByIds(proxyids, whereJson);
			baseResponse.setListSize(listSize + "");
		}

		proxys = dao.findProxyByIds(proxyids, whereJson);
	
		

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

		List<Long> proxyids = new ArrayList<Long>();
		int level = admin.getLevel();
		List<Proxy> proxys = null;

		List<Long> ids = new ArrayList<Long>();

//		if (level > 1) {
//			List<Long> channels = null;
//			if (level == 2) {
//				List<Admin> ads = dao.findAdminByHigherid(admin.getId());
//				for (int i = 0; i < ads.size(); i++) {
//					ids.add(ads.get(i).getId());
//				}
//				ids.add(admin.getId());
//			} else if (level == 3) {
//				ids.add(admin.getId());
//			}
//
//			channels = dao.findProxyidByAdminids(ids);
//
//			for (int i = 0; i < channels.size(); i++) {
//				proxyids.add(channels.get(i));
//			}
//
//		}
//
//		if (level == 1 || proxyids.size() > 0) {
//			proxys = dao.findProxyNameByIds(proxyids, whereJson);
//		}
		proxys = dao.findProxyNameByIds(proxyids, whereJson);

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
	@RequestMapping(value = "/checkChannel", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String checkChannel(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("checkChannel");
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

		String channel = jobj.getString("channel");

		long cou = dao.findCouByChannel(channel);
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

		int first = 1;

		try {
			first = Integer.parseInt(jobj.getString("first"));
		} catch (Exception e) {
			// TODO: handle exception
		}
//		List<Long> channelids = Cache.getChannelids(Long.parseLong(adminid));;

		int level = admin.getLevel();
		List<Long> ids = new ArrayList<Long>();
		
		if (!StringUtils.isStrEmpty(otheradminId)) {
			ids.add(Long.parseLong(otheradminId));
		}
		else	 if(first == 0  && level > 1 )
		{
			if (level == 2) {
				List<Admin> ads = dao.findAdminByHigherid(admin.getId());
				for (int i = 0; i < ads.size(); i++) {
					ids.add(ads.get(i).getId());
				}
				ids.add(admin.getId());
			} else if (level == 3) {
				ids.add(admin.getId());
			}
			Cache.setAdminids(admin.getId(),ids);
		}
		
		ids = Cache.getAdminids(admin.getId());
		
		if (level == 1 || ids.size() > 0) {
			OperateDao od = new OperateDao();
			
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String now = sdf.format(new Date());
				baseResponse.setConversion(true);
				List<Operate_reportform> reportforms1;
		        if(first==0)
		        {

					Cache.channelCatche(dao);
					reportforms1 = od.findSumFormDay(ids, jobj,now);
					List<Operate_reportform> ad = od.findAdminSumFormDay(ids, jobj,now);
					Operate_reportform or = reportforms1.get(0);
					or.setAdminName(ad.size()+"个负责人");
					reportforms1.addAll(ad);
					Cache.setOperate_reportform(Long.parseLong(adminid), reportforms1);
		            long listSize = od.findFormCou(null,ids, jobj, dateType,now);
		            baseResponse.setListSize(listSize+"");
		        }
		        else
		        {
		        		reportforms1 = Cache.getOperate_reportform(Long.parseLong(adminid));
		        }
		        
		        if(dateType.equals("1"))
		        {
			        	List<Operate_reportform> reportforms = od.findForm(null,ids,jobj,now);
			        	reportforms.addAll(0, reportforms1);
			        	baseResponse.setReportforms_day(reportforms);
		        }
		        else
		        {
			        	List<Operate_reportform> reportforms = od.findFormMonth(null,ids,jobj,now);
			        	reportforms.addAll(0, reportforms1);
			        	baseResponse.setReportforms_month(reportforms);
		        }
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				od.close();
			}
			
			
			

		} else {
			baseResponse.setListSize("0");
		}

		String content = JSONObject.toJSONString(baseResponse);
		logger.debug("register content = {}", content);
		return content;
	}
	
	



	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/updateCache", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String updateCache(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("updateCache");
		BaseResponse baseResponse = new BaseResponse();
		Cache.update(dao);

		baseResponse.setStatus(0);
		baseResponse.setStatusMsg("更新缓存成功");
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

	
	@CrossOrigin(origins="*",maxAge=3600)
    @RequestMapping(value = "/exportData", method = RequestMethod.POST,produces = "text/html;charset=UTF-8")
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

		String arr = jobj.getString("tag");
		
        int channelflag = 0;
        int channelidflag = 0;
        int channeltypeflag = 0;
        int adminflag = 0;
        int h5flag = 0;
        int creditflag =0 ;
        if("[]".equals(arr)) {
		}
		else {
			String[] strs = arr.substring(1, arr.length()-1).split(",");
			for(int i = 0;i < strs.length;i++) {
				strs[i] = strs[i].substring(1,strs[i].length()-1);
				if("渠道".equals(strs[i])) {
					channelflag = 1;
				}
				if("渠道号".equals(strs[i])) {
					channelidflag = 1;
				}
				if("渠道分类".equals(strs[i])) {
					channeltypeflag = 1;
				}
				if("负责人".equals(strs[i])) {
					adminflag = 1;
				}
				if("H5".equals(strs[i])) {
					h5flag = 1;
				}
				if("额度".equals(strs[i])) {
					creditflag = 1;
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

		int first = 1;

		try {
			first = Integer.parseInt(jobj.getString("first"));
		} catch (Exception e) {
			// TODO: handle exception
		}
//		List<Long> channelids = Cache.getChannelids(Long.parseLong(adminid));;

		int level = admin.getLevel();
		List<Long> ids = new ArrayList<Long>();
		
		if (!StringUtils.isStrEmpty(otheradminId)) {
			ids.add(Long.parseLong(otheradminId));
		}
		else	 if(first == 0  && level > 1 )
		{
			if (level == 2) {
				List<Admin> ads = dao.findAdminByHigherid(admin.getId());
				for (int i = 0; i < ads.size(); i++) {
					ids.add(ads.get(i).getId());
				}
				ids.add(admin.getId());
			} else if (level == 3) {
				ids.add(admin.getId());
			}
			Cache.setAdminids(admin.getId(),ids);
		}
		
		ids = Cache.getAdminids(admin.getId());
		
	 	List<Operate_reportform> reportforms = null;
		if (level == 1 || ids.size() > 0) {
			OperateDao od = new OperateDao();
			
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String now = sdf.format(new Date());
				baseResponse.setConversion(true);
				List<Operate_reportform> reportforms1;
		        if(first==0)
		        {

					Cache.channelCatche(dao);
					reportforms1 = od.findSumFormDay(ids, jobj,now);
					List<Operate_reportform> ad = od.findAdminSumFormDay(ids, jobj,now);
					Operate_reportform or = reportforms1.get(0);
					or.setAdminName(ad.size()+"个负责人");
					reportforms1.addAll(ad);
					Cache.setOperate_reportform(Long.parseLong(adminid), reportforms1);
		            long listSize = od.findFormCou(null,ids, jobj, dateType,now);
		            baseResponse.setListSize(listSize+"");
		        }
		        else
		        {
		        		reportforms1 = Cache.getOperate_reportform(Long.parseLong(adminid));
		        }
		        for(Operate_reportform opdata:reportforms1) {
		        		if(opdata.getChannelType() == null) {
		        			opdata.setChannelType("");
		        		}
		        }
		        if(dateType.equals("1"))
		        {
			        	 reportforms = od.findFormDay(null,ids,jobj,now);
			        	reportforms.addAll(0, reportforms1);
//			        	baseResponse.setReportforms_day(reportforms);
		        }
		        else
		        {
			        reportforms = od.findFormMon(null,ids,jobj,now);
			        	reportforms.addAll(0, reportforms1);
//			        	baseResponse.setReportforms_month(reportforms);
		        }
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				od.close();
			}
			
		} else {
			baseResponse.setListSize("0");
		}

         List<String> listName = new ArrayList<>();
         listName.add("时间");
         listName.add("渠道");
         listName.add("渠道号");
         listName.add("渠道分类");
         listName.add("负责人");
         listName.add("H5点击");
         listName.add("H5注册");
         listName.add("激活");
         listName.add("注册数");
         listName.add("进件数");
         listName.add("进件转化率%");
         listName.add("开户数");
         listName.add("开户转化率%");
         listName.add("放款数");
         listName.add("放款转化率%");
         listName.add("授信总额");
         listName.add("人均批额");
         listName.add("首提人数");
         listName.add("首贷总额");
         listName.add("渠道提现总额");
         List<String> listId = new ArrayList<>();
         listId.add("date");           //时间
         listId.add("channelName");    //渠道
         listId.add("channel");        //渠道号
         listId.add("channelType");    //渠道分类
         listId.add("adminName");      //负责人
         listId.add("h5Click");        //h5点击
         listId.add("h5Register");     //h5注册
         listId.add("activation");     //激活 
         listId.add("register");       //注册数
         listId.add("upload");         //进件数
         listId.add("uploadConversion");   //进件转化率
         listId.add("account");         //开户数
         listId.add("accountConversion");   //开户转化率
         listId.add("loan");            //放款数
         listId.add("loanConversion");   //放款转化率
         listId.add("credit");         //授信总额
         listId.add("perCapitaCredit"); //人均批额
         listId.add("firstGetPer");     //首提人数
         listId.add("firstGetSum");      //首贷总额
         listId.add("channelSum");       //渠道体现总额
         List<Map<String,Object>> listB = new ArrayList<>();
         
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
	        	 listId.remove("h5Click");        //h5点击
	        	 listId.remove("h5Register");     //h5注册

	        }
	        if(creditflag == 0) {
	            listName.remove("授信总额");
	            listName.remove("人均批额");
	            listId.remove("credit");         //授信总额
	            listId.remove("perCapitaCredit"); //人均批额
	        }
         ExportMapExcel exportExcelUtil = new ExportMapExcel();
         for(Operate_reportform opdata:reportforms) {
                 Map<String,Object> map = new HashMap<>();
                 map.put("date", opdata.getDate());
                 map.put("channelName", opdata.getChannelName());
                 map.put("channel", opdata.getChannel());
                 map.put("channelType", opdata.getChannelType());
                 map.put("adminName", opdata.getAdminName());
                 map.put("h5Click", opdata.getH5Click());
                 map.put("h5Register", opdata.getH5Register());
                 map.put("activation", opdata.getActivation());
                 map.put("register", opdata.getRegister());
                 map.put("upload", opdata.getUpload());
                 map.put("uploadConversion", opdata.getUploadConversion());
                 map.put("account", opdata.getAccount());
                 map.put("accountConversion", opdata.getAccountConversion());
                 map.put("loan", opdata.getLoan());
                 map.put("loanConversion", opdata.getLoanConversion());
                 map.put("credit", opdata.getCredit());
                 map.put("perCapitaCredit", opdata.getPerCapitaCredit());
                 map.put("firstGetPer", opdata.getFirstGetPer());
                 map.put("firstGetSum", opdata.getFirstGetSum());
                 map.put("channelSum", opdata.getChannelSum());
                 listB.add(map);
             }
                exportExcelUtil.exportExcel("渠道数据报表",listName,listId,listB,response);
	}




    @RequestMapping(value = "/updataPwd", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@CrossOrigin(origins="*",maxAge=3600)
    @ResponseBody
	public String updataPwd(HttpServletRequest request,HttpServletResponse response) {

		logger.debug("updataPwd");
		BaseResponse baseResponse = new BaseResponse();
//		String rootPath = this.getClass().getClassLoader().getResource("").getPath();
		//拼接修改密码链接     http://localhost:8080/operate/page/forgetPaw.html?参数
		String path = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/page/forgetPaw.html?email=";
		 String json = this.checkParameter(request);


       if(StringUtils.isStrEmpty(json)){
    	   baseResponse.setStatusMsg("请输入正确邮箱账号！");
    	   return JSONObject.toJSONString(baseResponse);
       }

       JSONObject jobj = JSONObject.parseObject(json);
       String email = jobj.getString("email");
       
            
       Admin admin = dao.findAdminByEmail(email).get(0);
       
		if (admin == null) {
		 baseResponse.setStatusMsg("没有这个用户，请确认邮箱输入正确！");
	    	 return JSONObject.toJSONString(baseResponse);
		}
	

		
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = sdf.format(d);
		int flag = 1;

		long createurlTime = System.currentTimeMillis(); 

		String name = admin.getName();

		String[] array = new String[] {email};
		String text = "Hi "+ name +",\n\n我们在"+currentTime+"收到你重置后台账号密码的请求。"
				+ "如果不是你自己在重置密码，请忽略并删除本邮件。\n如果是你自己需要重置密码，请点击下方链接进行密码重置。"
				+ "（链接24小时有效）\n如果有其他问题，请与技术部联系。\n\n"+ path + array[0] + "&createurlTime=" + createurlTime + "&flag=" + flag;
//		String text = path + array[0] + "&createurlTime=" + createurlTime;
		Mail mail = new Mail();
		mail.sendMailTest(text,array);
		baseResponse.setStatusMsg("重置密码邮件已发送，请注意查收！");
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
//		String path = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/page/index.html";
		String path = "http://xfjr.ledaikuan.cn/systems/proxy/index.html";
		int n = dao.updatepwd(email, pwd);
	
		BaseResponse baseResponse = new BaseResponse();
		if(1 == n) {
			baseResponse.setStatus(0);
			baseResponse.setStatusMsg("密码修改成功，页面正在跳转，请重新登录");
			baseResponse.setListSize(path);
			return JSONObject.toJSONString(baseResponse);
		}else {
			baseResponse.setStatus(1);
			baseResponse.setStatusMsg("密码修改失败，请联系管理员！");
			return JSONObject.toJSONString(baseResponse);
		}
	}
	
	
}
