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
import com.maimob.server.controller.logic.FinanceLogic;
import com.maimob.server.db.daoImpl.DaoWhere;
import com.maimob.server.db.entity.Admin;
import com.maimob.server.db.entity.AdminPermission;
import com.maimob.server.db.entity.BalanceAccount;
import com.maimob.server.db.entity.Channel;
import com.maimob.server.db.entity.ChannelPermission;
import com.maimob.server.db.entity.Dictionary;
import com.maimob.server.db.entity.Operate_business_kpi;
import com.maimob.server.db.entity.Operate_reportform;
import com.maimob.server.db.entity.OptimizationTask;
import com.maimob.server.db.entity.Permission;
import com.maimob.server.db.entity.Proxy;
import com.maimob.server.db.entity.Reward;
import com.maimob.server.db.entity.UserPermission;
import com.maimob.server.db.entity.operate_pay_company;
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
	@RequestMapping(value = "/addBalanceAccount", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String addBalanceAccount(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("addBalanceAccount");

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

		BalanceAccount balanceAccount = JSONObject.parseObject(json, BalanceAccount.class);
		balanceAccount.setUpdateAdmin(Long.parseLong(adminid));

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String updateTime = sdf.format(new Date());
		balanceAccount.setUpdateTime(updateTime);
		dao.saveBalanceAccount(balanceAccount);
		
		baseResponse.setStatus(0);
		baseResponse.setStatusMsg("");
		String content = JSONObject.toJSONString(baseResponse);
		logger.debug("register content = {}", content);
		return content;
	}
	

	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/getBalanceAccount", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getBalanceAccount(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("getBalanceAccount");

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

		List<BalanceAccount>balist = dao.findBalanceAccount(jobj);
		
		baseResponse.setBalanceAccountList(balist);
		baseResponse.setStatus(0);
		baseResponse.setStatusMsg("");
		String content = JSONObject.toJSONString(baseResponse);
		logger.debug("register content = {}", content);
		return content;
	}
	

	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/getBalanceAccountParameter", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getBalanceAccountParameter(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("getBalanceAccountParameter");

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
		
		List<BalanceAccount>balist = dao.findBalanceAccount(jobj);
		
		

		ArrayList<String> companyList = new ArrayList<String>();
		ArrayList<String> taxpayerNoList = new ArrayList<String>();
		ArrayList<String> accountNoList = new ArrayList<String>();

		for (int i = 0; i < balist.size(); i++) {
			BalanceAccount balanceAccount = balist.get(i);
			companyList.add(balanceAccount.getCompany());
			taxpayerNoList.add(balanceAccount.getTaxpayerNo());
			accountNoList.add(balanceAccount.getAccountNo());
			
		}

		baseResponse.setCompanyList(companyList);
		baseResponse.setTaxpayerNoList(taxpayerNoList);
		baseResponse.setAccountNoList(accountNoList);
		List<Dictionary> dic8 = Cache.getDicList(12);

		baseResponse.setBalanceAccountAttribute(dic8);
		
		
		baseResponse.setBalanceAccountList(balist);
		baseResponse.setStatus(0);
		baseResponse.setStatusMsg("");
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

		if (channel.getLevel() > 1 && channel.getRewardId() != 0) {
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
					
					Map<String, List<Reward>> mainReward = channel.getMainReward();
					 

					for (String appid : mainReward.keySet()) {
						List<Reward> rs = mainReward.get(appid);
						if(rs == null || rs.size() == 0)
						{
							continue;
						}
							
						List<Channel> cs = dao.findChannelByProxyId_appid(proxyId,appid);
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
			channelMap.put(channel.getChannelName(), "");
			channelMap.put(channel.getChannel(), "");

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
	@RequestMapping(value = "/getChannelNo", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getChannelNo(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("getChannelNo");
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


		String proxyid = jobj.getString("proxyid");
//		Proxy proxy = dao.findProxyById(Long.parseLong(proxyid));
		List<Channel> cs = dao.findMainChannel(Long.parseLong(proxyid));
		if(cs != null && cs.size() > 0)
		{
			baseResponse.setChannelNo(cs.get(0).getChannel());
			baseResponse.setStatus(0);
		}
		else
			baseResponse.setStatus(2);
		
		
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
			List<Admin> ads = null;
			if(adminid.equals("1517197192342"))
				ads = dao.findAllAdmin();
			else
				ads = dao.findAdminByDepartmentId(admin.getDepartmentId());
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
	@RequestMapping(value = "/getRewardTypeList", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getRewardTypeList(HttpServletRequest request){
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
		List<Dictionary> dic8 = Cache.getDicList(8);
		baseResponse.setRewardTypeList(dic8);
		baseResponse.setStatus(0);
		baseResponse.setStatusMsg("success");
		String content = JSONObject.toJSONString(baseResponse);
		logger.debug("getRewardTypeList content = {}", content);
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
		reportform = od.getReportform(null, jobj,"");
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

		if (level == 1 || proxyids.size() > 0) {} else {
			baseResponse.setListSize("0");
		}
		

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

		String appid = jobj.getString("appid");
		String channel = jobj.getString("channel");

		long cou = dao.findCouByChannel(channel,appid);
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
//			JSONObject whereJson = JSONObject.parseObject(json);
			otheradminId = jobj.getString("adminId");
			dateType = jobj.getString("dateType");
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
				List<Admin> ads = dao.findAdminByHigherid(admin.getId());//找到三级账户
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
				
				boolean isHj = DaoWhere.isHj(jobj);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String now = sdf.format(new Date());
				now += " 12:00:00";
				baseResponse.setConversion(true);
				List<Operate_reportform> reportforms1 = null;
		        if(first==0)
		        {

					Cache.channelCatche(dao);
					if(isHj)
					{
						reportforms1 = od.findSumFormDay(ids, jobj,now);
						List<Operate_reportform> ad = od.findAdminSumFormDay(ids, jobj,now);
						Operate_reportform or = reportforms1.get(0);
						or.setAdminName(ad.size()+"个负责人");
						reportforms1.addAll(ad);
						Cache.setOperate_reportform(Long.parseLong(adminid), reportforms1);
					}
		            long listSize = od.findFormCou(null,ids, jobj, dateType,now);
		            baseResponse.setListSize(listSize+"");
		        }
		        else
		        {
		        		if(isHj)
		        		reportforms1 = Cache.getOperate_reportform(Long.parseLong(adminid));
		        }
		        Cache.setLastTime(Long.parseLong(adminid), System.currentTimeMillis());
		        
		        String minDate = jobj.getString("minDate");
		        String maxDate = jobj.getString("maxDate");
		        String date = minDate;
		        if(!minDate.equals(maxDate)) {
		        	date = minDate + "~" + maxDate;
		        }
		        if(dateType.equals("1"))
		        {
			        	List<Operate_reportform> reportforms = od.findForm(null,ids,jobj,now);
			        	if(isHj && reportforms1 != null)
			        	{
				        	reportforms.addAll(0, reportforms1);
			        	}
			        	baseResponse.setReportforms_day(reportforms);
		        }
		        else if(dateType.equals("2")) {
		        	List<Operate_reportform> reportforms = od.findFormMonth(null,ids,jobj,now);
		        	if(isHj && reportforms1 != null)
		        	{
			        	reportforms.addAll(0, reportforms1);
		        	}
		        	baseResponse.setReportforms_month(reportforms);
		        }
		        else
		        {
		        	List<Operate_reportform> reportforms = od.findFormNothing(null,ids,jobj,now);
			        	for (Operate_reportform operate_reportform : reportforms) {
			        		operate_reportform.setDate(date);
						}
			        	if(isHj && reportforms1 != null)
			        	{
				        	reportforms.addAll(0, reportforms1);
			        	}
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
	@RequestMapping(value = "/channelTypeList", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String channelTypeList(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("updateCache");
		BaseResponse baseResponse = new BaseResponse();
		Cache.update(dao);

		baseResponse.setStatus(0);
		baseResponse.setStatusMsg("更新缓存成功");
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

		JSONArray arr = jobj.getJSONArray("tag");
		
        boolean allflag = false;
        boolean channelflag = false;
        boolean channeltypeflag = false;
        boolean adminflag = false;
        boolean h5flag = false;
        boolean creditflag =false ;
        if("[]".equals(arr.toString())) {
		}
		else {
			allflag = DaoWhere.ischose("总计", jobj);
			channelflag = DaoWhere.ischose("渠道号", jobj);
			channeltypeflag = DaoWhere.ischose("渠道分类", jobj);
			adminflag = DaoWhere.ischose("负责人", jobj);
			h5flag = DaoWhere.ischose("H5", jobj);
			creditflag = DaoWhere.ischose("额度", jobj);

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
//			JSONObject whereJson = JSONObject.parseObject(json);
			otheradminId = jobj.getString("adminId");
			dateType = jobj.getString("dateType");
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
				List<Admin> ads = dao.findAdminByHigherid(admin.getId());//找到三级账户
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
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String now = sdf.format(new Date());
				now += " 12:00:00";
				baseResponse.setConversion(true);
				List<Operate_reportform> reportforms1 = null;
		        if(first==0)
		        {
//
//					Cache.channelCatche(dao);
					if(allflag)
					{
						reportforms1 = od.findSumFormDay(ids, jobj,now);
						List<Operate_reportform> ad = od.findAdminSumFormDay(ids, jobj,now);
						Operate_reportform or = reportforms1.get(0);
						or.setAdminName(ad.size()+"个负责人");
						reportforms1.addAll(ad);
						for (Operate_reportform op : reportforms1) {
							if(op.getApp() == null) {
								op.setApp("");
							}
							if(op.getChannelType() == null) {
								op.setChannelType("");
							}
							if(op.getRewardType() == null) {
								op.setRewardType("");
							}
							
						}
						Cache.setOperate_reportform(Long.parseLong(adminid), reportforms1);
					}
		            long listSize = od.findFormCou(null,ids, jobj, dateType,now);
		            baseResponse.setListSize(listSize+"");
		        }
		        else
		        {
		        		if(allflag)
		        		reportforms1 = Cache.getOperate_reportform(Long.parseLong(adminid));
		        }
		        Cache.setLastTime(Long.parseLong(adminid), System.currentTimeMillis());
		        
		        String minDate = jobj.getString("minDate");
		        String maxDate = jobj.getString("maxDate");
		        String date = minDate;
		        if(!minDate.equals(maxDate)) {
		        	date = minDate + "~" + maxDate;
		        }
		        if(dateType.equals("1"))
		        {
			        	 reportforms = od.findFormDayAll(null,ids,jobj,now);
			        	if(allflag && reportforms1 != null)
			        	{
				        	reportforms.addAll(0, reportforms1);
			        	}
		        }
		        else if(dateType.equals("2")) {
		        	reportforms = od.findFormMon(null,ids,jobj,now);
		        	if(allflag && reportforms1 != null)
		        	{
			        	reportforms.addAll(0, reportforms1);
		        	}
		        }
		        else
		        {
		        	 reportforms = od.findFormMonthNothing(null,ids,jobj,now);
			        	for (Operate_reportform operate_reportform : reportforms) {
			        		operate_reportform.setDate(date);
						}
			        	if(allflag && reportforms1 != null)
			        	{
				        	reportforms.addAll(0, reportforms1);
			        	}
			        	baseResponse.setReportforms_month(reportforms);
		        }
		        for (Operate_reportform reportform : reportforms) {
		        	if(reportform.getRewardType() == null) {
		        		reportform.setRewardType("");
		        	}
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
         listName.add("APP");
         listName.add("渠道");
         listName.add("渠道号");
         listName.add("渠道分类");
         listName.add("分成方式");
         listName.add("负责人");
         listName.add("H5点击");
         listName.add("H5注册");
         listName.add("注册数");
         listName.add("激活");
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
         listId.add("app");
         listId.add("channelName");    //渠道
         listId.add("channel");        //渠道号
         listId.add("channelType");    //渠道分类
         listId.add("rewardType");
         listId.add("adminName");      //负责人
         listId.add("h5Click");        //h5点击
         listId.add("h5Register");     //h5注册
         listId.add("register");       //注册数
         listId.add("activation");     //激活 
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
         
//         if(allflag == 0) {
//	        	listName.remove("渠道");
//	        	listId.remove("channelName");   
//	        }
	        if(!channelflag) {
	        	listName.remove("渠道");
	        	listId.remove("channelName");  
	        	listName.remove("渠道号");
	        	listId.remove("channel");    
	        }
	        if(!channeltypeflag) {
	        	listName.remove("渠道分类");
	        	listId.remove("channelType");  
	        }
	        if(!adminflag) {
	        	listName.remove("负责人");
	        	listId.remove("adminName"); 
	        }
	        if(!h5flag) {
	        	 listName.remove("H5点击");
	        	 listName.remove("H5注册");
	        	 listId.remove("h5Click");        //h5点击
	        	 listId.remove("h5Register");     //h5注册

	        }
	        if(!creditflag) {
	            listName.remove("授信总额");
	            listName.remove("人均批额");
	            listId.remove("credit");         //授信总额
	            listId.remove("perCapitaCredit"); //人均批额
	        } 
  ExportMapExcel exportExcelUtil = new ExportMapExcel();
         for(Operate_reportform opdata:reportforms) {
                 Map<String,Object> map = new HashMap<>();
                 map.put("date", opdata.getDate());
                 map.put("app", opdata.getApp());
                 map.put("channelName", opdata.getChannelName());
                 map.put("channel", opdata.getChannel());
                 map.put("channelType", opdata.getChannelType());
                 map.put("rewardType", opdata.getRewardType());
                 map.put("adminName", opdata.getAdminName());
                 map.put("h5Click", opdata.getH5Click());
                 map.put("h5Register", opdata.getH5Register());
                 map.put("register", opdata.getRegister());
                 map.put("activation", opdata.getActivation());
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
		int type = 1;

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
//		String text = path + array[0] + "&createurlTime=" + createurlTime;
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
 		 
//		String email = request.getParameter("email");
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
//		 String path = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() 
//			+ request.getContextPath() + "/page/forgrtPaw.html?email=" + email +"&res=" + res + "&createurlTime" + ;
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
//		long currentTime = System.currentTimeMillis(); 
//		String path = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/page/index.html";
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
	
	

	@RequestMapping(value = "/datataskMsg", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String datataskMsg(HttpServletRequest request, HttpServletResponse response) {
		
//		if(AutoController.dt != null)
//		{
//			OperateDao od = new OperateDao();
//			
//			String msg = od.getTaskLog();
//			od.close();
//			
//			return AutoController.dt.getMsg()+"<br>"+msg;
//		}
//		else
//		{
//			return "任务未启动。";
//		}
		return "";
	}
	

	@RequestMapping(value = "/datatask", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String datatask(HttpServletRequest request, HttpServletResponse response) {
		
//		String date = this.checkParameter(request);
//		
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String now1 = sdf.format(new Date());
//		sdf = new SimpleDateFormat("yyyy-MM-dd");
//		String now = sdf.format(new Date());
//		
//		if(date.equals(""))
//			date = now;
//		Map ss = new HashMap();
//		ss.put("startDate", date);
//		ss.put("endDate", date);
//		ss.put("optimization", "-1"); 
//		
//		ss.put("id", "1517918294658");
//		ss.put("optimization", "-1");
//		ss.put("tableId", "30");
//		ss.put("adminId", "1516704387763");
//		System.out.println(date+"   "+now1);
//
//		OptimizationTask ot = new OptimizationTask(ss);
//		OperateData pd = new OperateData(ot);
//		pd.Statistics();
		

		return "任务完成。";

	}
	

	@RequestMapping(value = "/startdatatask", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String startdatatask(HttpServletRequest request, HttpServletResponse response) {
		 
//		if(AutoController.dt != null)
//		{
//			AutoController.dt.isrun = false;
//		}
//
//		AutoController.dt = new DataTask();
//		AutoController.dt.start();
		return "任务启动。";
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
		dic.setType(11);
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
		
		List<Dictionary> plist = dao.findDictionaryByType("11");
		
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
		per.setOpType(1);
		per.setUpdateTime(System.currentTimeMillis());
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
		List<Permission> pList = dao.findPermissionByType(Long.parseLong(type));;
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
		per.setOpType(1);
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
		
		List<Permission> ps = dao.findPermissionByName(name,"1");
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
		
		List<Permission> ps = dao.findPermissionByMeta(meta,"1");
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
		
		List<Dictionary> ps = dao.findDicByName("11", name);
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
		List<Map<String, String>> pList = od.getAdminPermission(type,adminid,"1");
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
		per.setOpType(1);
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
		List<UserPermission> userPermission = od.getAllAdminPermission(sessionid,"1");
		od.close();
		
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setStatus(0);
		baseResponse.setUserPermission(userPermission);
		baseResponse.setStatusMsg("");
		return JSONObject.toJSONString(baseResponse);
		
	}
	

	

	@RequestMapping(value = "/getFirstPage", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@CrossOrigin(origins="*",maxAge=3600)
	@ResponseBody
	public String getFirstPage(HttpServletRequest request,HttpServletResponse response) {
		String json = this.checkParameter(request);
		JSONObject jobj = JSONObject.parseObject(json);
		String sessionid = jobj.getString("sessionid");
		String adminid = jobj.getString("sessionid");

		BaseResponse baseResponse = new BaseResponse();
		Admin admin = this.getAdmin(adminid);
		if (admin == null) {
			baseResponse.setStatus(1);
			baseResponse.setStatusMsg("请重新登录");
			return JSONObject.toJSONString(baseResponse);
		}

		int level = admin.getLevel();
		List<Channel> channels = null;
		List<Long> ids = new ArrayList<Long>();
//		List<Admin> ads = new ArrayList<Admin>();
		List<Admin> adminList = new ArrayList<Admin>();//存放下级账户
		
		//不是一级账户  只能看到自己和自己下面的负责人的名字
		
		
		if (level == 3) 
			jobj.put("adminId", admin.getId());

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String now = sdf.format(new Date());
		now += " 12:00:00";
		OperateDao od = new OperateDao();
		List<Map<String, String>> firstPage = od.getFirstPage(jobj,now);
		od.close();
		
		baseResponse.setFirstPage(firstPage);
		baseResponse.setStatus(0);
		baseResponse.setStatusMsg("");
		return JSONObject.toJSONString(baseResponse);
		
	}
	

	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/getReportformByAdmin", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getReportformByAdmin(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("getReportformByAdmin");
		BaseResponse baseResponse = new BaseResponse();
		String json = this.checkParameter(request);

		if (StringUtils.isStrEmpty(json)) {
			baseResponse.setStatus(2);
			baseResponse.setStatusMsg("请求参数不合法");
			return JSONObject.toJSONString(baseResponse);
		}

		JSONObject jobj = JSONObject.parseObject(json);
		String adminid = jobj.getString("sessionid");
		String minDate = jobj.getString("minDate");
		String maxDate = jobj.getString("maxDate");
		String date = null;
		if(minDate.equals(maxDate)) {
			date = minDate;
		}
		else {
			date = minDate + "~" + maxDate;
		}
		
		Admin admin = this.getAdmin(adminid);//通过adminid获取负责人
		if (admin == null) {
			baseResponse.setStatus(1);
			baseResponse.setStatusMsg("请重新登录");
			return JSONObject.toJSONString(baseResponse);
		}

		Cache.channelCatche(dao);
		OperateDao od = new OperateDao();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String now = sdf.format(new Date());
			now += " 12:00:00";
			
			long registerall = od.findRegisterCouFormByDate(jobj);
			List<Map<String, String>> reportforms = od.findFormByAll(jobj,now);
			List<Map<String, String>> reportforms_admin = od.findFormByAdmin(jobj,now);
			
			for (String key : reportforms.get(0).keySet()) {
				String value = reportforms.get(0).get(key);
				if(null == value) {
					reportforms.get(0).put(key, "0");
				}
			}
//			System.out.println(reportforms_admin);
//			for (Map<String, String> map : reportforms_admin) {
//				for (String key : map.keySet()) {
//					String value = map.get(key);
//					if(null == value) {
//						map.put(key, "0");
//					}
//				}
//			}
			
			reportforms.get(0).put("date", "总计");
			reportforms.get(0).put("app", "-");
			reportforms.get(0).put("adminName", reportforms_admin.size()+"人");
			reportforms.addAll(reportforms_admin);
			if(reportforms_admin.toString() != "[]") {
				for (Map<String, String> map : reportforms) {
					long register = Long.parseLong(map.get("register"));
					String registerConversion = od.getBL(register,registerall);
					map.put("registerConversion",registerConversion);
					map.put("date", date);
				}
			}
			
			baseResponse.setReportforms_admin(reportforms);
			baseResponse.setStatus(0);

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
	@RequestMapping(value = "/getReportformByMainChannel", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getReportformByMainChannel(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("getReportformByMainChannel");
		BaseResponse baseResponse = new BaseResponse();
		String json = this.checkParameter(request);

		if (StringUtils.isStrEmpty(json)) {
			baseResponse.setStatus(2);
			baseResponse.setStatusMsg("请求参数不合法");
			return JSONObject.toJSONString(baseResponse);
		}

		JSONObject jobj = JSONObject.parseObject(json);
		String adminid = jobj.getString("sessionid");
		String minDate = jobj.getString("minDate");
		String maxDate = jobj.getString("maxDate");
		String date = null;
		if(minDate == maxDate) {
			date = minDate;
		}
		else {
			date = minDate + "~" + maxDate;
		}

		Admin admin = this.getAdmin(adminid);
		if (admin == null) {
			baseResponse.setStatus(1);
			baseResponse.setStatusMsg("请重新登录");
			return JSONObject.toJSONString(baseResponse);
		}

		Cache.channelCatche(dao);
		OperateDao od = new OperateDao();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String now = sdf.format(new Date());
			now += " 12:00:00";
			long registerall = od.findRegisterCouFormByDate(jobj);
			List<Map<String, String>> reportforms = od.findFormByAll(jobj,now);
			List<Map<String, String>> reportforms_admin = od.findFormByMainChannel(jobj,now);
			
			reportforms.get(0).put("date", "总计");
			reportforms.get(0).put("adminName", "-");
			reportforms.get(0).put("mainChannelName", "-");
			reportforms.get(0).put("mainChannel", "-");
			reportforms.get(0).put("channelTypeName", "-");
			reportforms.get(0).put("app", "-");

			reportforms.addAll(reportforms_admin);
			for (Map<String, String> map : reportforms) {
				long register = Long.parseLong(map.get("register"));
				String registerConversion = od.getBL(register,registerall);
				map.put("registerConversion",registerConversion);
				map.put("date", date);
			}
			
			baseResponse.setReportforms_admin(reportforms);
			baseResponse.setStatus(0);

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
	@RequestMapping(value = "/getReportformByChannel", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getReportformByChannel(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("getReportformByChannel");
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
		String minDate = jobj.getString("minDate");
		String maxDate = jobj.getString("maxDate");
		String date = null;
		if(minDate == maxDate) {
			date = minDate;
		}
		else {
			date = minDate + "~" + maxDate;
		}

		Cache.channelCatche(dao);
		OperateDao od = new OperateDao();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String now = sdf.format(new Date());
			now += " 12:00:00";
			long registerall = od.findRegisterCouFormByDate(jobj);
			List<Map<String, String>> reportforms = od.findFormByAll(jobj,now);
			if(reportforms.size() == 0)
			{
				baseResponse.setReportforms_admin(reportforms);
				baseResponse.setStatus(0);
			}
			else
			{

//				reportforms.get(0).put("registerConversion", "");
				List<Map<String, String>> reportforms_admin = od.findFormByChannel(jobj,now);
				reportforms.get(0).put("date", "总计");
				reportforms.get(0).put("registerConversion", "-");
				reportforms.get(0).put("adminName", "-");
				reportforms.get(0).put("mainChannelName", "-");
				reportforms.get(0).put("mainChannel", "-");
				reportforms.get(0).put("channelTypeName", "-");
				reportforms.get(0).put("app", "-"); 
				reportforms.addAll(reportforms_admin);
				for (Map<String, String> map : reportforms) {
					long register = Long.parseLong(map.get("register"));
					String registerConversion = od.getBL(register,registerall);
					map.put("registerConversion",registerConversion);
					map.put("date", date);
				}
				baseResponse.setReportforms_admin(reportforms);
				baseResponse.setStatus(0);
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
	
	/**
	 * 渠道数据统计表，通过sessionid获取其一级渠道名称、渠道号、渠道类型和负责人
	 * @author Astro
	 * @param request
	 * @param response
	 * @return
	 */
	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/getReportByAdmin", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getReportByAdmin(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("getReportByAdmin");
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

//		Cache.channelCatche(dao);
//		OperateDao od = new OperateDao();
//		List<Map<String, String>> reportforms = null;
//		try {
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			String now = sdf.format(new Date());
//			reportforms = od.findFormByAll(jobj,now);
//			List<Map<String, String>> reportforms_admin = od.findFormByMainChannel(jobj,now);
//			reportforms.get(0).put("registerConversion", "");
//			long registerall = Long.parseLong(reportforms.get(0).get("register"));
//			for (Map<String, String> map : reportforms_admin) {
//				long register = Long.parseLong(map.get("register"));
//				String registerConversion = od.getBL(register,registerall);
//				map.put("registerConversion",registerConversion);
//			}
//			reportforms.addAll(reportforms_admin);
			


//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			od.close();
//		}

		String otheradminId = "";
		if (!json.equals("")) {

			try {
				json = URLDecoder.decode(json, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

//			JSONObject whereJson = JSONObject.parseObject(json);

			otheradminId = jobj.getString("adminId");
//			if (!StringUtils.isStrEmpty(otheradminId)) {
//				where.append(" and adminId = " + otheradminId + " ");
//			}

		}
		

		int level = admin.getLevel();
		List<Channel> channels = null;
		List<Long> ids = new ArrayList<Long>();
//		List<Admin> ads = new ArrayList<Admin>();
		List<Admin> adminList = new ArrayList<Admin>();//存放下级账户
		if (!StringUtils.isStrEmpty(otheradminId)) {
			ids.add(Long.parseLong(otheradminId));
		} else {
			//不是一级账户  只能看到自己和自己下面的负责人的名字
			if (level > 1) {

				if (level == 2) {
					adminList = dao.findAdminByHigherid(admin.getId());//二级账户找到其下级账户
					for (int i = 0; i < adminList.size(); i++) {
						ids.add(adminList.get(i).getId());
						
					}
					ids.add(admin.getId());
				} else if (level == 3) {
					ids.add(admin.getId());//三级账户只能看到自己所管理的渠道
				}
				
				adminList.add(admin);//自己也要添加进去
			}
			//一级账户的时候能看到所有负责人的名字  再进行查询
			else {
				adminList = dao.findBusinessAdmin();
			}
		}
		
		
		

		channels = dao.findChannelByAdminids(ids, jobj);//一级账户能看到所有账户管理的渠道
		//找出渠道商id   proxyid
//		List<Long> proxyids = new ArrayList<Long>();
//		for (Channel channel : channels) {
//			proxyids.add(channel.getProxyId());
//		}

		//一级渠道名称
		ArrayList<String> channelNameList = new ArrayList<String>();
		//渠道号集合 包括一级渠道号和二级渠道号
		ArrayList<String> channelNoList = new ArrayList<String>();
		

		
		for (int i = 0; i < channels.size(); i++) {
			Channel channel = channels.get(i);
			if (channel.getLevel() == 1) {
				channelNameList.add(channel.getChannelName());
				channelNoList.add(channel.getChannel());
			} else if (channel.getLevel() == 2) {
//				channelNameList.add("--" + channel.getChannelName());
				channelNoList.add("--" + channel.getChannel());
			}

//			Admin admin1 = Cache.getAdminCatche(channel.getAdminId());
//			if (admin1 != null)
//				adminIdList.add(admin1.getId() + "," + admin1.getName());

		}

		// AppTools.removeDuplicate(channelNameList);
		// AppTools.removeDuplicate(channelNoList);
//		AppTools.removeDuplicate(adminIdList);
		//通过渠道商id 找出其中的一级渠道
//		channels = dao.findMainChannels(proxyids);
//		
//		for (Channel channel : channels) {
//			channelNameList.add(channel.getChannelName());
//			channelNoList.add(channel.getChannelNo());
//		}
		
		//渠道类别
		Cache.DicCatche(dao);
		List<Dictionary> dic1 = Cache.getDicList(1);
		List<Dictionary> dic3 = Cache.getDicList(3);
		List<Dictionary> dic4 = Cache.getDicList(4);
		List<Dictionary> dic5 = Cache.getDicList(5);

		baseResponse.setAppList(dic1);
		baseResponse.setChannelAttribute(dic3);
		baseResponse.setChannelType(dic4);
		baseResponse.setChannelSubdivision(dic5);
		baseResponse.setChannelTypeList(this.createChannelTypeList());
		baseResponse.setChannelNameList(channelNameList);
		baseResponse.setChannelNoList(channelNoList);
		baseResponse.setAdminList(adminList);
//		baseResponse.setReportforms_admin(reportforms);
		baseResponse.setStatus(0);
		baseResponse.setStatusMsg("");
		String content = JSONObject.toJSONString(baseResponse);
		logger.debug("register content = {}", content);
		return content;
	}
	
	
	public List createChannelTypeList()
	{
		List<Dictionary> dic3 = Cache.getDicList(3);
		List<Dictionary> dic4 = Cache.getDicList(4);
		List<Dictionary> dic5 = Cache.getDicList(5);
		
		Map<Long ,List<Map<String,String>>> typeList = new HashMap<Long ,List<Map<String,String>>>();
		for(Dictionary d : dic5)
		{
			long hid = d.getHigherId();
			Map<String,String> val = new HashMap<String,String>();
			val.put("value", d.getId()+"");
			val.put("label", d.getName());
			List<Map<String,String>> dlist = null;
			if(typeList.get(hid) == null)
			{
				dlist = new ArrayList<Map<String,String>>();
				typeList.put(hid, dlist);
			}
			else
			{
				dlist = typeList.get(hid);
			}

			dlist.add(val);
		}
		

//		{
//	          label: '换量--免费',
//	          options: [{
//	            value: '9',//要传的值
//	            label: '1:1注册',//要显示在页面上的名字
//	          }, {
//	            value: '8',//要传的值
//	            label: '1:1提现',//要显示在页面上的名字
//	          }]
//	        }, 
		
		Map<Long ,Dictionary> arc = new HashMap<Long ,Dictionary>();
		for(Dictionary d : dic3)
		{
			arc.put(d.getId(), d );
		}
		
		List channelTypeList = new ArrayList ();
		for(Dictionary d : dic4)
		{
			long hid = d.getHigherId();
			String arcName = arc.get(hid).getName(); 

			Map type = new HashMap ();
			Map<String,String> val = new HashMap<String,String>();
			val.put("label", arcName+"-"+d.getName());
			val.put("value", hid+","+d.getId());
			List<Map<String,String>> slist = typeList.get(d.getId());
			slist.add(0,val);
			type.put("label", arcName+"-"+d.getName());
			type.put("options", slist);
			channelTypeList.add(type);
		}
		
		return channelTypeList;
	}
	
	

	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/checkCompany", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String checkCompany(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("checkCompany");
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

		String company = jobj.getString("company");

		long cou = dao.findCouByCompany(company);
		if (cou == 0) {
			baseResponse.setStatus(0);
			baseResponse.setStatusMsg("公司名可用");
		} else {
			baseResponse.setStatus(2);
			baseResponse.setStatusMsg("公司名已经存在！");
		}

		String content = JSONObject.toJSONString(baseResponse);
		logger.debug("register content = {}", content);
		return content;
	}
	
	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/checkTaxpayerNo", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String checkTaxpayerNo(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("checkTaxpayerNo");
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

		String taxpayerNo = jobj.getString("taxpayerNo");

		long cou = dao.findCouByTaxpayerNo(taxpayerNo);
		if (cou == 0) {
			baseResponse.setStatus(0);
			baseResponse.setStatusMsg("纳税人识别号可用");
		} else {
			baseResponse.setStatus(2);
			baseResponse.setStatusMsg("纳税人识别号已经存在！");
		}

		String content = JSONObject.toJSONString(baseResponse);
		logger.debug("register content = {}", content);
		return content;
	}
	
	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/checkAccountNo", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String checkAccountNo(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("checkAccountNo");
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

		String accountNo = jobj.getString("accountNo");

		long cou = dao.findCouByAccountNo(accountNo);
		if (cou == 0) {
			baseResponse.setStatus(0);
			baseResponse.setStatusMsg("结算账户号码可用");
		} else {
			baseResponse.setStatus(2);
			baseResponse.setStatusMsg("结算账户号码已经存在！");
		}

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

//		Admin admin = this.getAdmin(adminid);
//		if (admin == null) {
//			baseResponse.setStatus(1);
//			baseResponse.setStatusMsg("请重新登录");
//			return JSONObject.toJSONString(baseResponse);
//		}

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

		List<Dictionary> dic1 = Cache.getDicList(1);
		baseResponse.setAppList(dic1);
		baseResponse.setPayCompanyList(payCompanyList);
		baseResponse.setStatus(0);
		baseResponse.setStatusMsg("");
		String content = JSONObject.toJSONString(baseResponse);
		logger.debug("register content = {}", content);
		return content;
	}
	

	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/addPayCompany", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String addPayCompany(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("addPayCompany");
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


		operate_pay_company opc = JSONObject.parseObject(json, operate_pay_company.class);
		
		dao.savePayConpany(opc);

		if(opc.getId()==0)
		{ 
			
			List<ChannelPermission> cplist = dao.findChannelPermissionByProxyId(opc.getProxyid()+"",opc.getAppid()+"");
			if(cplist == null || cplist.size() == 0)
			{
				ChannelPermission channelPermission = new ChannelPermission();
				channelPermission.setAppid(opc.getAppid());
				channelPermission.setProxyid(opc.getProxyid());
				dao.saveChannelPermission(channelPermission);
			}
		}
		
		baseResponse.setStatus(0);
		baseResponse.setStatusMsg("");
		String content = JSONObject.toJSONString(baseResponse);
		logger.debug("register content = {}", content);
		return content;
	}
	

	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/getPayCompanyParameter", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getPayCompanyParameter(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("getPayCompanyParameter");

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
		
		jobj.put("attributeId", "35");
		
		List<BalanceAccount>balist = dao.findBalanceAccount(jobj);

		List<Dictionary> dic13 = Cache.getDicList(13);

		List<Dictionary> dic1 = Cache.getDicList(1);
		baseResponse.setAppList(dic1);
		baseResponse.setPayList(dic13);
		baseResponse.setBalanceAccountList(balist);
		baseResponse.setStatus(0);
		baseResponse.setStatusMsg("");
		String content = JSONObject.toJSONString(baseResponse);
		logger.debug("register content = {}", content);
		return content;
	}
	
	
	
	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/exportDataByAdmin", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public void exportDataByAdmin(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("exportDataByAdmin");

		BaseResponse baseResponse = new BaseResponse();
		String json = this.checkParameter(request);

		if (StringUtils.isStrEmpty(json)) {
			baseResponse.setStatus(2);
			baseResponse.setStatusMsg("请求参数不合法");
			return ;
		}
		JSONObject jobj = JSONObject.parseObject(json);
		String adminid = jobj.getString("sessionid");
		
		String minDate = jobj.getString("minDate");
		String maxDate = jobj.getString("maxDate");
		String date = null;
		if(minDate.equals(maxDate)) {
			date = minDate;
		}
		else {
			date = minDate + "~" + maxDate;
		}
		Admin admin = this.getAdmin(adminid);
		if (admin == null) {
			baseResponse.setStatus(1);
			baseResponse.setStatusMsg("请重新登录");
			return ;
		}
		OperateDao od = new OperateDao();
		List<Map<String, String>> reportforms = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String now = sdf.format(new Date());
			now += " 12:00:00";
			
			long registerall = od.findRegisterCouFormByDate(jobj);
			reportforms = od.findFormByAll(jobj,now);
			List<Map<String, String>> reportforms_admin = od.findFormByAdmin(jobj,now);
			
			for (String key : reportforms.get(0).keySet()) {
				String value = reportforms.get(0).get(key);
				if(null == value) {
					reportforms.get(0).put(key, "0");
				}
			}
			reportforms.get(0).put("app", "-");
			reportforms.get(0).put("adminName", reportforms_admin.size()+"人");
			reportforms.addAll(reportforms_admin);
			if(reportforms_admin.toString() != "[]") {
				for (Map<String, String> map : reportforms) {
					long register = Long.parseLong(map.get("register"));
					String registerConversion = od.getBL(register,registerall);
					map.put("registerConversion",registerConversion);
					map.put("date", date);
				}
			}
			reportforms.get(0).put("date", "总计");
			

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			od.close();
		}

		List<String> listName = new ArrayList<>();
        listName.add("日期");
        listName.add("APP");
        listName.add("负责人");
        listName.add("注册数");
        listName.add("进件数");
        listName.add("首提人数");
        listName.add("首贷总额");
        listName.add("注册占比");
        
        List<String> listId = new ArrayList<>();
        listId.add("date");           //时间
        listId.add("app"); 
        listId.add("adminName");    
        listId.add("register"); 
        listId.add("upload"); 
        listId.add("firstGetPer");
        listId.add("firstGetSum"); 
        listId.add("registerConversion"); 

        ExportMapExcel exportExcelUtil = new ExportMapExcel();
        exportExcelUtil.exportExcelString("运营数据详情统计表",listName,listId,reportforms,response);
		
	} 
	
	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/exportDataByMainChannel", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public void exportDataByMainChannel(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("exportDataByMainChannel");
		BaseResponse baseResponse = new BaseResponse();
		String json = this.checkParameter(request);

		if (StringUtils.isStrEmpty(json)) {
			baseResponse.setStatus(2);
			baseResponse.setStatusMsg("请求参数不合法");
			return;
		}
		JSONObject jobj = JSONObject.parseObject(json);
		String adminid = jobj.getString("sessionid");
		String minDate = jobj.getString("minDate");
		String maxDate = jobj.getString("maxDate");
		String date = null;
		if(minDate.equals(maxDate)) {
			date = minDate;
		}
		else {
			date = minDate + "~" + maxDate;
		}
		Admin admin = this.getAdmin(adminid);
		if (admin == null) {
			baseResponse.setStatus(1);
			baseResponse.setStatusMsg("请重新登录");
			return;
		}
		OperateDao od = new OperateDao();
		List<Map<String, String>> reportforms = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String now = sdf.format(new Date());
			now += " 12:00:00";
			long registerall = od.findRegisterCouFormByDate(jobj);
			reportforms = od.findFormByAll(jobj,now);
			List<Map<String, String>> reportforms_admin = od.findFormByMainChannel(jobj,now);
			
			if(reportforms == null) {
				System.out.println("1");
				//查询不到数据怎么处理 
				return;
			}
			
//			reportforms.get(0).put("date", "总计");
			reportforms.get(0).put("adminName", "-");
			reportforms.get(0).put("mainChannelName", "-");
			reportforms.get(0).put("mainChannel", "-");
			reportforms.get(0).put("channelTypeName", "-");
			reportforms.get(0).put("app", "-");

			
			reportforms.addAll(reportforms_admin);
//			System.out.println(reportforms_admin);
			if(reportforms_admin.toString() != "[]") {
				for (Map<String, String> map : reportforms) {
//					for(int i = 1; i<reportforms.size();i++) {
					long register = Long.parseLong(map.get("register"));
					String registerConversion = od.getBL(register,registerall);
					map.put("registerConversion",registerConversion);
					map.put("date", date);
				}
				reportforms.get(0).put("date", "总计");
			}
			
			baseResponse.setReportforms_admin(reportforms);
			baseResponse.setStatus(0);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			od.close();
		}

		List<String> listName = new ArrayList<>();
        listName.add("日期");
        listName.add("APP");
        listName.add("负责人");
        listName.add("渠道");
        listName.add("渠道号");
        listName.add("渠道类型");
        listName.add("注册数");
        listName.add("进件数");
        listName.add("开户数");
        listName.add("首提人数");
        listName.add("首贷总额");
        listName.add("注册占比");
        
        List<String> listId = new ArrayList<>();
        listId.add("date");           //时间
        listId.add("app"); 
        listId.add("adminName");    
        listId.add("mainChannelName");       //渠道
        listId.add("mainChannel");      //渠道号
        listId.add("channelTypeName");     //渠道类型
        listId.add("register"); 
        listId.add("upload"); 
        listId.add("account");     //开户数
        listId.add("firstGetPer");
        listId.add("firstGetSum"); 
        listId.add("registerConversion"); 

        ExportMapExcel exportExcelUtil = new ExportMapExcel();
        exportExcelUtil.exportExcelString("运营数据详情统计表",listName,listId,reportforms,response);
	}
 
@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/exportDataByChannel", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public void exportDataByChannel(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("exportDataByChannel");
		BaseResponse baseResponse = new BaseResponse();
		String json = this.checkParameter(request);

		if (StringUtils.isStrEmpty(json)) {
			baseResponse.setStatus(2);
			baseResponse.setStatusMsg("请求参数不合法");
			return ;
		}

		JSONObject jobj = JSONObject.parseObject(json);
		String adminid = jobj.getString("sessionid");

		Admin admin = this.getAdmin(adminid);
		if (admin == null) {
			baseResponse.setStatus(1);
			baseResponse.setStatusMsg("请重新登录");
			return ;
		}
		String minDate = jobj.getString("minDate");
		String maxDate = jobj.getString("maxDate");
		String date = null;
		if(minDate.equals(maxDate)) {
			date = minDate;
		}
		else {
			date = minDate + "~" + maxDate;
		}

		Cache.channelCatche(dao);
		OperateDao od = new OperateDao();
		List<Map<String, String>> reportforms = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String now = sdf.format(new Date());
			now += " 12:00:00";
			long registerall = od.findRegisterCouFormByDate(jobj);
			reportforms = od.findFormByAll(jobj,now);
			if(reportforms.size() == 0)
			{
				baseResponse.setReportforms_admin(reportforms);
				baseResponse.setStatus(0);
			}
			else
			{

//				reportforms.get(0).put("registerConversion", "");
				List<Map<String, String>> reportforms_admin = od.findFormByChannel(jobj,now);
				
//				reportforms.get(0).put("registerConversion", "-");
				reportforms.get(0).put("adminName", "-");
				reportforms.get(0).put("channelName", "-");
				reportforms.get(0).put("channel", "-");
				reportforms.get(0).put("channelTypeName", "-");
				reportforms.get(0).put("app", "-"); 
				reportforms.addAll(reportforms_admin);
				if(reportforms_admin.toString() != "[]") {
					for (Map<String, String> map : reportforms) {
					long register = Long.parseLong(map.get("register"));
					String registerConversion = od.getBL(register,registerall);
					map.put("registerConversion",registerConversion);
					map.put("date", date);
					}
					reportforms.get(0).put("date", "总计");

				}
				baseResponse.setReportforms_admin(reportforms);
				baseResponse.setStatus(0);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			od.close();
		}
		
		List<String> listName = new ArrayList<>();
		  listName.add("日期");
	        listName.add("APP");
	        listName.add("负责人");
	        listName.add("渠道");
	        listName.add("渠道号");
	        listName.add("渠道类型");
	        listName.add("注册数");
	        listName.add("进件数");
	        listName.add("开户数");
	        listName.add("首提人数");
	        listName.add("首贷总额");
	        listName.add("注册占比");
	        
	        List<String> listId = new ArrayList<>();
	        listId.add("date");           //时间
	        listId.add("app"); 
	        listId.add("adminName");    
	        listId.add("channelName");       //渠道
	        listId.add("channel");      //渠道号
	        listId.add("channelTypeName");     //渠道类型
	        listId.add("register"); 
	        listId.add("upload"); 
	        listId.add("account");     //开户数
	        listId.add("firstGetPer");
	        listId.add("firstGetSum"); 
	        listId.add("registerConversion"); 

        ExportMapExcel exportExcelUtil = new ExportMapExcel();
        exportExcelUtil.exportExcelString("运营数据详情统计表",listName,listId,reportforms,response);

		
	}
 
/**
	 * kpi中心  业绩管理  发送负责人数据
	 * @param request
	 * @param response
	 */
	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/kpimanager", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String kpimanager(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("kpimanager");
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
		//		String id = jobj.getString("id");
		int level = admin.getLevel();
//		List<Long> ids = new ArrayList<Long>();
		List<Admin> adminList = new ArrayList<Admin>();
	
		if(level > 1) {
			if(level == 2) {
				adminList = dao.findAdminByHigherid(admin.getId());      //二级账户找到其下级账户
			}
			adminList.add(admin);
		}
		else {
			adminList = dao.findBusinessAdmin();
		}
		
//		if(id == null) {
//			for (Admin adm : adminList) {
//				ids.add(adm.getId());
//			}
//		}else {
//			ids.add(Long.parseLong(id));
//		}
		baseResponse.setAdminList(adminList);
		
//		Cache.channelCatche(dao);
//		OperateDao od = new OperateDao();
//		List<Map<String, String>> reportforms = od.findkpi(jobj,ids);
		
		baseResponse.setAdminList(adminList);
		baseResponse.setStatus(0);
		
		String content = JSONObject.toJSONString(baseResponse);
		logger.debug("register content = {}", content);
		return content;
		
		
	}
	/**
	 * 查询kpi  
	 * @param request
	 * @param response
	 * @return
	 */
	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/findkpi", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String findkpi(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("findkpi");
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
		JSONArray arr = jobj.getJSONArray("adminid");
		List<Long> ids = new ArrayList<Long>();

		if(arr.size() > 0) {
			for (Object id : arr) {
				ids.add(Long.parseLong(String.valueOf(id)));
			}
		}
		else {
			baseResponse.setStatus(1);
			baseResponse.setStatusMsg("请选择负责人");
			return JSONObject.toJSONString(baseResponse);
		}
		OperateDao od = new OperateDao();
		List<Map<String, String>> reportforms = od.findkpi(jobj,ids);
		
		baseResponse.setReportforms_admin(reportforms);
		baseResponse.setStatus(0);
		
		String content = JSONObject.toJSONString(baseResponse);
		logger.debug("register content = {}", content);
		return content;
		
		
	}

/**
	 * kpi查看详情
	 * @param request
	 * @param response
	 * @return
	 */
	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/kpidetail", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String kpidetail(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("kpidetail");
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
		List<Map<String, String>> reportforms = od.findkpidetail(jobj);

		
		baseResponse.setReportforms_admin(reportforms);
		baseResponse.setStatus(0);
		String content = JSONObject.toJSONString(baseResponse);
		logger.debug("register content = {}", content);
		return content;
	}
	
	/**
	 * kpi设定
	 * @param request
	 * @param response
	 * @return
	 */
	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/kpisetting", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String kpisetting(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("kpisetting");
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
		//将传过来的数据插入数据库
		JSONArray kpi = jobj.getJSONArray("KPI");
//		String kp = jobj.getString("KPI");
//		System.out.println(kpi);
		for (Object object : kpi) {
			JSONObject j = (JSONObject)object;
			Long adid = j.getLong("adminId");
			String month = j.getString("month");
			String kp = j.getString("KPI");
			Long kpinum = 0l;
			try {
				kpinum = Long.parseLong(kp);
			}
			catch(Exception e) {
				kpinum = 0l;
			}
			Operate_business_kpi operate_business_kpi = new Operate_business_kpi(adid,
					month,kpinum,Long.parseLong(adminid));
			dao.add(operate_business_kpi);
		}
//		Cache.channelCatche(dao);
//		dao.saveOperatekpi(operate_business_kpi);
//		OperateDao od = new OperateDao();
//		List<Map<String, String>> reportforms = od.findkpidetail(jobj);
		
//		baseResponse.setReportforms_admin(reportforms);
		baseResponse.setStatus(0);
		baseResponse.setStatusMsg("kpi新增成功！");
		String content = JSONObject.toJSONString(baseResponse);
		logger.debug("register content = {}", content);
		return content;
		
	}
	 
/**
	 * 业绩查询数据 可以是多个负责人
	 * @param request
	 * @param response
	 * @return
	 */
	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/findkpibymonth", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String findkpibymonth(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("findkpibymonth");
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
		
		
		JSONArray arr = jobj.getJSONArray("adminid");
		List<Long> ids = new ArrayList<Long>();

		if(arr.size() > 0) {
			for (Object id : arr) {
//				System.out.println(id);
				ids.add(Long.parseLong(String.valueOf(id)));
			}
		}
		else {
			baseResponse.setStatus(1);
			baseResponse.setStatusMsg("请选择负责人");
			return JSONObject.toJSONString(baseResponse);
		}
		OperateDao od = new OperateDao();
		//String adminid = jobj.getString("sessionid");
		JSONArray tag = jobj.getJSONArray("tag");
		
		 String minDate = jobj.getString("minDate");
		    String maxDate = jobj.getString("maxDate");
		    String date = minDate + "--" + maxDate;
		    if(minDate.equals(maxDate)) {
		    		date	 = minDate;
//		    		where1 = "where month = '" + minDate +  "'";
		    }
		List<Map<String, String>> reportforms = od.findkpiAll(jobj,ids);
		List<Map<String, String>>  kpireportforms = null;
		//根据条件查找数据
		if(tag.size() == 0) {
			kpireportforms = od.findkpiAll(jobj,ids);
			kpireportforms.get(0).put("month", date);
		}
		if(DaoWhere.ischose("分月查询", jobj) && !DaoWhere.ischose("负责人查询", jobj)) {
			kpireportforms = od.findkpibymonth(jobj,ids);
				
		}
			
		if(!DaoWhere.ischose("分月查询", jobj) && DaoWhere.ischose("负责人查询", jobj)) {
			reportforms.get(0).put("name", "-");
			kpireportforms = od.findkpibyAdmins(jobj,ids);
			for (Map<String, String> map : kpireportforms) {
				map.put("month", date);
			}
				
		}
		if(DaoWhere.ischose("分月查询", jobj) && DaoWhere.ischose("负责人查询", jobj)) {
			kpireportforms = od.findkpibyall(jobj,ids);
				
		}
		reportforms.addAll(kpireportforms);
		
		
		baseResponse.setReportforms_admin(reportforms);
		baseResponse.setStatus(0);
		String content = JSONObject.toJSONString(baseResponse);
		logger.debug("register content = {}", content);
		return content;
		
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
	
	
	
	
}
