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
import com.maimob.server.utils.PWDUtils;
import com.maimob.server.utils.StringUtils;

@Controller
@RequestMapping("/pro")
public class ProxyController extends BaseController {

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

		String mobileNo = jobj.getString("mobileNo");
		String pwd = jobj.getString("pwd");
		String md5Pwd = PWDUtils.encryptMD5AndBase64(pwd);
		long no = 0;
		
		boolean isproxy = AppTools.isProxy(mobileNo);

		Cache.AdminCatche(dao);
		int status = 1;
		String statusMsg = "";
		if(!isproxy)
		{
			List<Channel> channelList = dao.findChannelByChannel(mobileNo);

			if (channelList == null || channelList.size() == 0) {
				status = 1;
				statusMsg = "用户名或密码错误";
			} else {
				Channel channel = channelList.get(0);
				String md5Pwd2 = PWDUtils.encryptMD5AndBase64(channelList.get(0).getPwd());

				if (md5Pwd2.equals(md5Pwd)) {
					Channel c = new Channel();
					c.setChannel(channel.getChannel());
					
					status = 0;
					c.setLoginDate(System.currentTimeMillis());
					setChannel(channel);
					baseResponse.setChannel(channel);
					baseResponse.setSessionid(mobileNo);
				} else {
					status = 1;
					statusMsg = "用户名或密码错误";
				}
			}
		}
		else
		{
			List<Proxy> proxyList = dao.findProxyByMobileNo(mobileNo);


			if (proxyList == null || proxyList.size() == 0) {
				status = 1;
				statusMsg = "用户名或密码错误";
			} else {
				Proxy proxy = proxyList.get(0);
				String md5Pwd2 = PWDUtils.encryptMD5AndBase64(proxyList.get(0).getPwd());

				if (md5Pwd2.equals(md5Pwd)) {
					proxy.setPwd(null);
					status = 0;
					proxy.setLoginDate(System.currentTimeMillis());
					setProxy(proxy);
					baseResponse.setMobileNo(proxy.getMobileno());
					baseResponse.setProxy(proxy);
					baseResponse.setSessionid(proxy.getId());
				} else {
					status = 1;
					statusMsg = "用户名或密码错误";
				}
			}
			
		}
		baseResponse.setStatus(status);
		baseResponse.setStatusMsg(statusMsg);
		String content = JSONObject.toJSONString(baseResponse);
		logger.debug("register content = {}", content);
		return content;
	}
	

	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/Autologin", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String Autologin(HttpServletRequest request, HttpServletResponse response) {

		logger.debug("Autologin");

		BaseResponse baseResponse = new BaseResponse();

		String json = this.checkParameter(request);

		if (StringUtils.isStrEmpty(json)) {
			baseResponse.setStatus(2);
			baseResponse.setStatusMsg("请求参数不合法");
			return JSONObject.toJSONString(baseResponse);
		}

		JSONObject jobj = JSONObject.parseObject(json);

		String adminid = jobj.getString("admin");
		String id = jobj.getString("id");
		String pwd = jobj.getString("pwd"); 
		long no = 0;
		

		Admin admin = this.getAdmin(adminid);
		if (admin == null) {
			baseResponse.setStatus(1);
			baseResponse.setStatusMsg("请重新登录");
			return JSONObject.toJSONString(baseResponse);
		}
		
		
		
		boolean isproxy = AppTools.isProxy(id);

		Cache.AdminCatche(dao);
		int status = 1;
		String statusMsg = "";
		if(!isproxy)
		{
			List<Channel> channelList = dao.findChannelByChannel(id);

			if (channelList == null || channelList.size() == 0) {
				status = 1;
				statusMsg = "不存在";
			} else {
				Channel channel = channelList.get(0);
				
				{
					Channel c = new Channel();
					c.setChannel(channel.getChannel());
					
					status = 0;
					c.setLoginDate(System.currentTimeMillis());
					setChannel(channel);
					baseResponse.setChannel(channel);
					baseResponse.setSessionid(id);
				}  
			}
		}
		else
		{
			Proxy proxy = dao.findProxyById(Long.parseLong(id));


			if (proxy == null) {
				status = 1;
				statusMsg = "用户不存在";
			} else {
				{
					proxy.setPwd(null);
					status = 0;
					proxy.setLoginDate(System.currentTimeMillis());
					setProxy(proxy);
					baseResponse.setMobileNo(proxy.getMobileno());
					baseResponse.setProxy(proxy);
					baseResponse.setSessionid(proxy.getId());
				} 
			}
			
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
		String proxyId = jobj.getString("sessionid");

		Proxy proxy = this.getProxy(proxyId);
		if (proxy == null) {
			baseResponse.setStatus(1);
			baseResponse.setStatusMsg("请重新登录");
			return JSONObject.toJSONString(baseResponse);
		}
		Cache.channelCatche(dao);
		Channel channel = JSONObject.parseObject(json, Channel.class);

		String statusMsg = "";
		int status = 2;

		String check = channel.check2();
		if (check.equals("")) {
			try {
				channel.getId();
				if (channel.isNew()) {
					dao.saveChannel(channel);
					dao.updateProxy(channel);
				} else {
					dao.updateChannelName(channel.getId(), channel.getChannelName());
				}
				baseResponse.setId(channel.getId());
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
		String proxyId = jobj.getString("sessionid");

		Proxy proxy = this.getProxy(proxyId);
		if (proxy == null) {
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
		String proxyId = jobj.getString("sessionid");

		Proxy proxy = this.getProxy(proxyId);
		if (proxy == null) {
			baseResponse.setStatus(1);
			baseResponse.setStatusMsg("请重新登录");
			return JSONObject.toJSONString(baseResponse);
		}

		jobj.put("proxyId", proxyId);
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

		List<Channel> channels = dao.findChannelByProxyId(jobj);

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
		String proxyId = jobj.getString("sessionid");

		Proxy proxy = this.getProxy(proxyId);
		if (proxy == null) {
			baseResponse.setStatus(1);
			baseResponse.setStatusMsg("请重新登录");
			return JSONObject.toJSONString(baseResponse);
		}

		jobj.put("proxyId", proxyId);

		List<Channel> channels = dao.findChannelByProxyId(jobj);

		ArrayList<String> channelNameList = new ArrayList<String>();
		ArrayList<String> channelNoList = new ArrayList<String>();

		for (int i = 0; i < channels.size(); i++) {
			Channel channel = channels.get(i);
			if (channel.getLevel() == 1) {
				channelNameList.add(channel.getChannelName());
				channelNoList.add(channel.getChannel());
			} else if (channel.getLevel() == 2) {
				channelNameList.add("--" + channel.getChannelName());
				channelNoList.add("--" + channel.getChannel());
			}

		}

		// AppTools.removeDuplicate(channelNameList);
		// AppTools.removeDuplicate(channelNoList);

		baseResponse.setChannelNameList(channelNameList);
		baseResponse.setChannelNoList(channelNoList);
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
		String mobileNo = jobj.getString("sessionid");

		Proxy proxy = this.getProxy(mobileNo);
		if (proxy == null) {
			baseResponse.setStatus(1);
			baseResponse.setStatusMsg("请重新登录");
			return JSONObject.toJSONString(baseResponse);
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
		String proxyId = jobj.getString("sessionid");

		Proxy proxy = this.getProxy(proxyId);
		if (proxy == null) {
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
		String proxyId = jobj.getString("sessionid");

		Proxy proxy = this.getProxy(proxyId);
		if (proxy == null) {
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
		String proxyId = jobj.getString("sessionid");

		boolean isproxy = AppTools.isProxy(proxyId);
		long proxyid2 = 0;
		long channelPermissionid = 0;
		if(isproxy )
		{
			Proxy proxy = dao.findProxyById(Long.parseLong(proxyId)); 
			channelPermissionid = proxy.getPermissionId();
			if (proxy == null) {
				baseResponse.setStatus(1);
				baseResponse.setStatusMsg("请重新登录");
				return JSONObject.toJSONString(baseResponse);
			}
			proxyid2 = proxy.getId();
		}
		else
		{

			List<Channel> channelList = dao.findChannelByChannel(proxyId);

			if (channelList == null || channelList.size() == 0) {
				baseResponse.setStatus(1);
				baseResponse.setStatusMsg("请重新登录");
				return JSONObject.toJSONString(baseResponse);
			} else {
				Channel channel = channelList.get(0);

				proxyid2 = channel.getProxyId();
				Proxy proxy = dao.findProxyById(proxyid2); 
				channelPermissionid = proxy.getPermissionId();
				jobj.put("channel", proxyId);
			}

		}
		
		
		String dateType = jobj.getString("dateType");
		List<Long> channelids = dao.findChannelIdByProxyId(proxyid2, jobj);
		
		
		
		Cache.channelCatche(dao);
		if (channelids.size() > 0) {

			int first = 1;
			OperateDao od = new OperateDao();
			try {

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String now = sdf.format(new Date());
				first = Integer.parseInt(jobj.getString("first"));
				if (first == 0) {
					long listSize = od.findFormCou(channelids,null, jobj, dateType,now);
					baseResponse.setListSize(listSize + "");
				}

				ChannelPermission channelPermission = dao.findChannelPermissionById(channelPermissionid);

				if (dateType.equals("1")) {
					List<Operate_reportform> reportforms = od.findForm(channelids,null,jobj,now);
					baseResponse.setReportforms_day(reportforms);
					baseResponse.setChannelPermission(channelPermission);
					deleteDayValue(reportforms, channelPermission);

				} else {
					List<Operate_reportform> reportforms = od.findFormMonth(channelids,null,jobj,now);
					baseResponse.setReportforms_month(reportforms);
					baseResponse.setChannelPermission(channelPermission);
					deleteDayValue(reportforms, channelPermission);
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
		String proxyId = jobj.getString("sessionid");

		Proxy proxy = this.getProxy(proxyId);
		if (proxy == null) {
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
//	注册数         注册用户数
//	登录数         激活用户数
//	申请数         进件用户数
//	授信人数       开户用户数
//	放款人数       首提用户数
//	首次提现总金额 首贷总额
	private void deleteDayValue(List<Operate_reportform> od, ChannelPermission channelPermission) {
		if (channelPermission == null)
			return;
		
		for (int i = 0; i < od.size(); i++) {
			
			if (channelPermission.getRegisterChartPermission() == 0) {
				od.get(i).setRegister(0);
			}

			if (channelPermission.getLoginChartPermission() == 0) {
				od.get(i).setActivation(0);
			}
			if (channelPermission.getApplyChartPermission() == 0) {
				od.get(i).setUpload(0);
			}
			if (channelPermission.getLoanAcctChartPermission() == 0) {
				od.get(i).setAccount(0);
			}
			if (channelPermission.getCashNumCharPermission() == 0) {
				od.get(i).setFirstGetPer(0);
			}
			if (channelPermission.getFirstCashAmtChartPermission() == 0) {
				od.get(i).setFirstGetSum(0);
			}

//			if (channelPermission.getTotalCashAmtChartPermission() == 0) {
//				
//			}
			od.get(i).setH5Click(0);
			od.get(i).setH5Register(0);
			od.get(i).setAdminName("");

			od.get(i).setChannelSum(0);
			
		}

	}
	

	private void setChannel(Channel channel) {
		channel.setLoginDate(System.currentTimeMillis());
		Cache.channelCatche(dao);
		Cache.AdminCatche(dao);
		Cache.updateChannelCatche(channel);
	}

	private Channel getChannel(String channel) {
		Cache.channelCatche(dao);
		if (StringUtils.isStrEmpty(channel))
			return null;

		Channel channel1 = Cache.getChannelCatche(channel);
		if (channel1 != null) {

			if (System.currentTimeMillis() - channel1.getLoginDate() > 7200000) {
				channel1 = null;
			} else {
				channel1.setLoginDate(System.currentTimeMillis());
			}
		}
		return channel1;
	}
	

	private void setProxy(Proxy proxy) {
		proxy.setLoginDate(System.currentTimeMillis());
		Cache.ProxyCache(dao);
		Cache.AdminCatche(dao);
		Cache.updateProxyCatche(proxy);
	}

	private Proxy getProxy(String proxyId) {
		Cache.ProxyCache(dao);
		if (StringUtils.isStrEmpty(proxyId))
			return null;

		Proxy proxy = Cache.getProxyCatche(Long.parseLong(proxyId));
		if (proxy != null) {

			if (System.currentTimeMillis() - proxy.getLoginDate() > 7200000) {
				proxy = null;
			} else {
				proxy.setLoginDate(System.currentTimeMillis());
			}
		}
		return proxy;
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
		String proxyId = jobj.getString("sessionid");
	

//		Proxy proxy = this.getProxy(proxyId);
//		if (proxy == null) {
//			baseResponse.setStatus(1);
//			baseResponse.setStatusMsg("请重新登录");
//			return ;
//		}
//
//		String dateType = jobj.getString("dateType");
//		List<Long> channelids = dao.findChannelIdByProxyId(proxy.getId(), jobj);
//
//		Cache.channelCatche(dao);
//		List<Operate_reportform> reportforms = null;
//		ChannelPermission channelPermission = dao.findChannelPermissionById(proxy.getId());
//		if (channelids.size() > 0) {
//
//			int first = 1;
//			OperateDao od = new OperateDao();
//			try {
//
//				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//				String now = sdf.format(new Date());
//				first = Integer.parseInt(jobj.getString("first"));
//				if (first == 0) {
//					long listSize = od.findFormCou(channelids,null, jobj, dateType,now);
//					baseResponse.setListSize(listSize + "");
//				}
//
//
//				if (dateType.equals("1")) {
//					reportforms = od.findFormAll(channelids,null,jobj,now);
//					deleteDayValue(reportforms, channelPermission);
//
//				} else {
//					reportforms = od.findFormMonthAll(channelids,null,jobj,now);
//					deleteDayValue(reportforms, channelPermission);
//				}
//				
//			}
//			catch (Exception e) {
//				e.printStackTrace();
//			}
//			finally {
//				od.close();
//			}
//			
//			
//
//		} else {
//			baseResponse.setListSize("0");
//		}

		boolean isproxy = AppTools.isProxy(proxyId);
		long proxyid2 = 0;
		long channelPermissionid = 0;
		if(isproxy )
		{
			Proxy proxy = dao.findProxyById(Long.parseLong(proxyId)); 
			channelPermissionid = proxy.getPermissionId();
			if (proxy == null) {
				baseResponse.setStatus(1);
				baseResponse.setStatusMsg("请重新登录");
				return ;
			}
			proxyid2 = proxy.getId();
		}
		else
		{

			List<Channel> channelList = dao.findChannelByChannel(proxyId);

			if (channelList == null || channelList.size() == 0) {
				baseResponse.setStatus(1);
				baseResponse.setStatusMsg("请重新登录");
				return ;
			} else {
				Channel channel = channelList.get(0);

				proxyid2 = channel.getProxyId();
				Proxy proxy = dao.findProxyById(proxyid2); 
				channelPermissionid = proxy.getPermissionId();
				jobj.put("channel", proxyId);
			}

		}
		
		
		String dateType = jobj.getString("dateType");
		List<Long> channelids = dao.findChannelIdByProxyId(proxyid2, jobj);
		
		
		List<Operate_reportform> reportforms = null;
		ChannelPermission channelPermission = dao.findChannelPermissionById(channelPermissionid);
		Cache.channelCatche(dao);
		if (channelids.size() > 0) {

			int first = 1;
			OperateDao od = new OperateDao();
			try {

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String now = sdf.format(new Date());
				first = Integer.parseInt(jobj.getString("first"));
				if (first == 0) {
					long listSize = od.findFormCou(channelids,null, jobj, dateType,now);
					baseResponse.setListSize(listSize + "");
				}

				

				if (dateType.equals("1")) {
					reportforms = od.findForm(channelids,null,jobj,now);
					baseResponse.setReportforms_day(reportforms);
					baseResponse.setChannelPermission(channelPermission);
					deleteDayValue(reportforms, channelPermission);

				} else {
					reportforms = od.findFormMonth(channelids,null,jobj,now);
					baseResponse.setReportforms_month(reportforms);
					baseResponse.setChannelPermission(channelPermission);
					deleteDayValue(reportforms, channelPermission);
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
        listName.add("注册数");
        listName.add("激活");
        listName.add("进件数");
        listName.add("开户数");
        listName.add("首提人数");
        listName.add("首贷总额");
        List<String> listId = new ArrayList<>();
        listId.add("date");           //时间
        listId.add("channelName");    //渠道
        listId.add("channel");        //渠道号
        listId.add("register");       //注册数
        listId.add("activation");     //激活 
        listId.add("upload");         //进件数
        listId.add("account");         //开户数  
        listId.add("firstGetPer");     //首提人数
        listId.add("firstGetSum");      //首贷总额
        List<Map<String,Object>> listB = new ArrayList<>();
        
        if (channelPermission.getRegisterChartPermission() == 0) {
			listName.remove("注册数");
			listId.remove("register");
		}

		if (channelPermission.getLoginChartPermission() == 0) {
			listName.remove("激活");
			listId.remove("activation");
		}
		if (channelPermission.getApplyChartPermission() == 0) {
			listName.remove("进件数");
			listId.remove("upload");
		}
		if (channelPermission.getLoanAcctChartPermission() == 0) {
			listName.remove("开户数");
			listId.remove("account");
		}
		if (channelPermission.getCashNumCharPermission() == 0) {
			listName.remove("首提人数");
			listId.remove("firstGetPer");
		}
		if (channelPermission.getFirstCashAmtChartPermission() == 0) {
			listName.remove("首贷总额");
			listId.remove("firstGetSum");
		}


        

        ExportMapExcel exportExcelUtil = new ExportMapExcel();
        for(Operate_reportform opdata:reportforms) {
                Map<String,Object> map = new HashMap<>();
                map.put("date", opdata.getDate());
                map.put("channelName", opdata.getChannelName());
                map.put("channel", opdata.getChannel());
                map.put("activation", opdata.getActivation());
                map.put("register", opdata.getRegister());
                map.put("upload", opdata.getUpload());
                map.put("account", opdata.getAccount());
                map.put("firstGetPer", opdata.getFirstGetPer());
                map.put("firstGetSum", opdata.getFirstGetSum());
                if (channelPermission.getRegisterChartPermission() == 0) {
                	map.remove("register");
                }

                if (channelPermission.getLoginChartPermission() == 0) {
                	map.remove("activation");
                }
                if (channelPermission.getApplyChartPermission() == 0) {
                	map.remove("upload");

                }
                if (channelPermission.getLoanAcctChartPermission() == 0) {
                	map.remove("account");
                }
                if (channelPermission.getCashNumCharPermission() == 0) {
                	map.remove("firstGetPer");
                }
                if (channelPermission.getFirstCashAmtChartPermission() == 0) {
                	map.remove("firstGetSum");
                }

                listB.add(map);
        }
        exportExcelUtil.exportExcel("渠道数据详情报表",listName,listId,listB,response);

	
	}
	
  

}
