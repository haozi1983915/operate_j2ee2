package com.maimob.server.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.util.IOUtils;
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
import com.maimob.server.db.entity.Operate_reportform_day;
import com.maimob.server.db.entity.Operate_reportform_month;
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
		int status = 1;
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

				statusMsg = "联系人电话已经注册过！";
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

		String check = channel.check();
		if (check.equals("")) {

			try {
				dao.saveChannel(channel);
				baseResponse.setId(channel.getId());
				dao.updateProxy(channel);
				if (channel.getLevel() == 1) {
					dao.updateChannelType(channel.getRewardPrice(), channel.getRewardTypeId(), channel.getRewardId(),
							channel.getProxyId(), channel.getAttribute(), channel.getType(), channel.getSubdivision(),
							channel.getAdminId());
					Cache.channelCatche(dao, channel.getProxyId() + "");
				}

				statusMsg = "添加渠道商成功";
				status = 0;
			} catch (Exception e) {
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

		List<Long> ids = new ArrayList<Long>();
		if (level > 1) {
			List<Long> channels = null;

			if (level == 2) {
				List<Admin> ads = dao.findAdminByHigherid(admin.getId());
				for (int i = 0; i < ads.size(); i++) {
					ids.add(ads.get(i).getId());
				}
				ids.add(admin.getId());
			} else if (level == 3) {
				ids.add(admin.getId());
			}

			channels = dao.findProxyidByAdminids(ids);

			for (int i = 0; i < channels.size(); i++) {
				proxyids.add(channels.get(i));
			}

		}

		if (level == 1 || proxyids.size() > 0) {
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
		} else {
			baseResponse.setListSize("0");
		}

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

		if (level > 1) {
			List<Long> channels = null;
			if (level == 2) {
				List<Admin> ads = dao.findAdminByHigherid(admin.getId());
				for (int i = 0; i < ads.size(); i++) {
					ids.add(ads.get(i).getId());
				}
				ids.add(admin.getId());
			} else if (level == 3) {
				ids.add(admin.getId());
			}

			channels = dao.findProxyidByAdminids(ids);

			for (int i = 0; i < channels.size(); i++) {
				proxyids.add(channels.get(i));
			}

		}

		if (level == 1 || proxyids.size() > 0) {
			proxys = dao.findProxyNameByIds(proxyids, whereJson);
		}

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

		int first = 1;

		try {
			first = Integer.parseInt(jobj.getString("first"));
		} catch (Exception e) {
			// TODO: handle exception
		}
		List<Long> channelids = Cache.getChannelids(Long.parseLong(adminid));;

		int level = admin.getLevel();
		if(first == 0 || channelids == null)
		{
			
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

			channelids = dao.findChannelIdByAdminids(ids, jobj);
			Cache.setChannelids(Long.parseLong(adminid), channelids);
		}
		
		
		

		if (level == 1 || channelids.size() > 0) {

//			if (first == 0) {
//				long listSize = dao.findFormCou(channelids, jobj, dateType);
//				baseResponse.setListSize(listSize + "");
//			}


			OperateDao od = new OperateDao();
			List<Operate_reportform> reportforms1 = od.findSumFormDay(channelids, jobj);
			List<Operate_reportform> ad = od.findAdminSumFormDay(channelids, jobj);

//			List<Operate_reportform> pr = od.findProxySumFormDay(channelids, jobj);
			
			Operate_reportform or = reportforms1.get(0);
//			or.setChannelName(pr.size()+"个公司");
			or.setAdminName(ad.size()+"个负责人");
			reportforms1.addAll(ad);
//			reportforms1.addAll(pr);
			
			
//			baseResponse.setReportforms(reportforms1);
			
			baseResponse.setConversion(true);
	        if(first==0)
	        {
	            long listSize = dao.findFormCou(channelids, jobj, dateType);
	            baseResponse.setListSize(listSize+"");
	        }
	        
	        if(dateType.equals("1"))
	        {
	        	List<Operate_reportform_day> reportforms = dao.findForm(channelids,jobj);
	        	reportforms = AppTools.changeDay(reportforms1, reportforms);
	        	baseResponse.setReportforms_day(reportforms);
	        }
	        else
	        {
	        	List<Operate_reportform_month> reportforms = dao.findFormMonth(channelids,jobj);
	        	reportforms = AppTools.changeMonth(reportforms1, reportforms);
	        	baseResponse.setReportforms_month(reportforms);
	        }
			
			
			
//			if (dateType.equals("1")) {
//			} else {
////				List<Operate_reportform_month> reportforms = dao.findFormMonth(channelids, jobj);
////				baseResponse.setReportforms_month(reportforms);
//			}

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
    public String exportData(HttpServletRequest request,HttpServletResponse response) throws IOException{
        logger.debug("exportData");
        BaseResponse baseResponse = new BaseResponse();
        String json = this.checkParameter(request);
//        String json = this.check(request);
        String sessionid = request.getParameter("sessionid");
        if(StringUtils.isStrEmpty(json)){
            baseResponse.setStatus(2);
            baseResponse.setStatusMsg("请求参数不合法");
            return null;
        }


        JSONObject jobj = JSONObject.parseObject(json);
        String adminid = jobj.getString("sessionid");
//        String adminid = request.getParameter("sessionid");

        Admin admin = this.getAdmin(adminid);
        if(admin == null)
        {
            baseResponse.setStatus(1);
            baseResponse.setStatusMsg("请重新登录");
            return null;
        }

        String dateType = "1";
        String otheradminId = "";
        if(!json.equals(""))
        {
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
        List<Long> ids = new ArrayList<Long>();
        if(!StringUtils.isStrEmpty(otheradminId))
        {
        	
        }
        else
        {
            if(level > 1)
            {
                
                if(level == 2)
                {
                	List<Admin> ads = dao.findAdminByHigherid(admin.getId()); 
                	for(int i = 0;i < ads.size();i++)
                	{
                    	ids.add(ads.get(i).getId());
                	}
                	ids.add(admin.getId());
                }
                else if(level == 3)
                {
                	ids.add(admin.getId());
                }
            	
            	
            }
        }


        List<Long> channelids = dao.findChannelIdByAdminids(ids,jobj);
        
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
            listName.add("开户数");
            listName.add("放款数");
            listName.add("授信总额");
            listName.add("人均批额");
            listName.add("首提人数");
            listName.add("首贷总额");
            listName.add("渠道提现");
            List<String> listId = new ArrayList<>();
            listId.add("date");
            listId.add("channelName");
            listId.add("channelId");
            listId.add("channelType");
            listId.add("adminName");
            listId.add("h5Click");
            listId.add("h5Register");
            listId.add("activation");
            listId.add("register");
            listId.add("upload");
            listId.add("account");
            listId.add("loan");
            listId.add("credit");
            listId.add("perCapitaCredit");
            listId.add("firstGetPer");
            listId.add("firstGetSum");
            listId.add("channelSum");
            List<Map<String,Object>> listB = new ArrayList<>();
            
            String path =  IndexController.class.getResource("/").getFile().toString().replaceAll("WEB-INF/classes/", "upload/");
//            long currentTime = System.currentTimeMillis();
            Date day=new Date(); 
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss"); 
            String currentTime = df.format(day);
            String fname = "chanaldata" + currentTime + ".xls";
            String filename = path + fname;
            ExportMapExcel exportExcelUtil = new ExportMapExcel();
            if(dateType.equals("1"))
            {
                List<Operate_reportform_day> reportforms = dao.findFormDay(channelids,jobj);
                //baseResponse.setReportforms_day(reportforms);
                     
                for(Operate_reportform_day opdata:reportforms) {
                    Map<String,Object> map = new HashMap<>();
                    map.put("date", opdata.getDate());
                    map.put("channelName", opdata.getChannelName());
                    map.put("channelId", opdata.getChannelId());
                    map.put("channelType", opdata.getChannelType());
                    map.put("adminName", opdata.getAdminName());
                    map.put("h5Click", opdata.getH5Click());
                    map.put("h5Register", opdata.getH5Register());
                    map.put("activation", opdata.getActivation());
                    map.put("register", opdata.getRegister());
                    map.put("upload", opdata.getUpload());
                    map.put("account", opdata.getAccount());
                    map.put("loan", opdata.getLoan());
                    map.put("credit", opdata.getCredit());
                    map.put("perCapitaCredit", opdata.getPerCapitaCredit());
                    map.put("firstGetPer", opdata.getFirstGetPer());
                    map.put("firstGetSum", opdata.getFirstGetSum());
                    map.put("channelSum", opdata.getChannelSum());
                    listB.add(map);
                }
                exportExcelUtil.exportExcel("渠道数据报表",listName,listId,listB,filename);
            }
            else
            {
                List<Operate_reportform_month> reportforms = dao.findFormMon(channelids,jobj);
                 for(Operate_reportform_month opdata:reportforms) {
                        Map<String,Object> map = new HashMap<>();
                        map.put("date", opdata.getDate());
                        map.put("channelName", opdata.getChannelName());
                        map.put("channelId", opdata.getChannelId());
                        map.put("channelType", opdata.getChannelType());
                        map.put("adminName", opdata.getAdminName());
                        map.put("h5Click", opdata.getH5Click());
                        map.put("h5Register", opdata.getH5Register());
                        map.put("activation", opdata.getActivation());
                        map.put("register", opdata.getRegister());
                        map.put("upload", opdata.getUpload());
                        map.put("account", opdata.getAccount());
                        map.put("loan", opdata.getLoan());
                        map.put("credit", opdata.getCredit());
                        map.put("perCapitaCredit", opdata.getPerCapitaCredit());
                        map.put("firstGetPer", opdata.getFirstGetPer());
                        map.put("firstGetSum", opdata.getFirstGetSum());
                        map.put("channelSum", opdata.getChannelSum());
                        listB.add(map);
                    }
                exportExcelUtil.exportExcel("渠道数据报表",listName,listId,listB,filename);
            }
            return fname;
             
    }


    @CrossOrigin(origins="*",maxAge=3600)
    @RequestMapping(value = "/downloadData", method = RequestMethod.GET,produces = "text/html;charset=UTF-8")
    @ResponseBody
    public void downloadData1(HttpServletRequest request,HttpServletResponse response) throws IOException{  

        String path =  IndexController.class.getResource("/").getFile().toString().replaceAll("WEB-INF/classes/", "upload/");

        String fname = request.getParameter("fname");
        String filename = path + fname;
        response.setCharacterEncoding("UTF-8");
        //设置响应头，控制浏览器下载该文件
        response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(fname, "UTF-8"));
        //读取要下载的文件，保存到文件输入流
        FileInputStream in = new FileInputStream(filename);
        //创建输出流
        OutputStream out = response.getOutputStream();
        IOUtils.copy(in,out);
        //创建缓冲区
        // byte buffer[] = new byte[8192];
        // int len = 0;
        //循环将输入流中的内容读取到缓冲区当中
        // while((len=in.read(buffer))>0){
        //输出缓冲区的内容到浏览器，实现文件下载
        // out.write(buffer, 0, len);

        //关闭文件输入流
        in.close();
        //关闭输出流
        out.close();
        File file = new File(path + fname);
         if (file.exists() && file.isFile()) {
             if (file.delete()) {
                 System.out.println("删除文件成功！");
             }
         }
        
    }

    @RequestMapping(value = "/updataPwd", method = RequestMethod.POST)
	@CrossOrigin(origins="*",maxAge=3600)
	public void forgotPwd(HttpServletRequest request,HttpServletResponse response) {

		logger.debug("updataPwd");

//		String rootPath = this.getClass().getClassLoader().getResource("").getPath();
		//拼接修改密码链接     http://localhost:8080/operate/page/forgetPaw.html?参数
		String path = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/page/forgetPaw.html?email=";
		 String json = this.checkParameter(request);


       if(StringUtils.isStrEmpty(json)){
           return;
       }

       JSONObject jobj = JSONObject.parseObject(json);
       String email = jobj.getString("email");
       
            
       Admin admin = dao.findAdminByEmail(email).get(0);
       
		if (admin == null) {
			return ;
		}
	

		
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = sdf.format(d);

		long createurlTime = System.currentTimeMillis(); 

		String name = admin.getName();
//		email = "zhi.nie@maimob.cn";
		String[] array = new String[] {email};
		String text = "Hi "+ name +",\n\n我们在"+currentTime+"收到你重置后台账号密码的请求。如果不是你自己在重置密码，请忽略并删除本邮件。\n如果是你自己需要重置密码，请点击下方链接进行密码重置。（链接24小时有效）\n如果有其他问题，请与技术部联系。\n\n"+ path + array[0] + "&createurlTime=" + createurlTime;
//		String text = path + array[0] + "&createurlTime=" + createurlTime;
		Mail mail = new Mail();
		mail.sendMailTest(text,array);
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
		String path = "http://xfjr.ledaikuan.cn/systems/test/proxy/index.html";
		int n = dao.updatepwd(email, pwd);
	
		BaseResponse baseResponse = new BaseResponse();
		if(1 == n) {
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
	
	
}
