package com.maimob.server.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

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
import com.maimob.server.db.entity.Operate_reportform_day;
import com.maimob.server.db.entity.Operate_reportform_month;
import com.maimob.server.db.entity.Proxy;
import com.maimob.server.db.entity.Reward;
import com.maimob.server.db.service.DaoService;
import com.maimob.server.db.service.SMSRecordService;
import com.maimob.server.protocol.BaseResponse;
import com.maimob.server.utils.AppTools;
import com.maimob.server.utils.Cache;
import com.maimob.server.utils.PWDUtils;
import com.maimob.server.utils.StringUtils;

@Controller
@RequestMapping("/pro")
public class ProxyController extends BaseController {

    //注入service
    @Autowired
    private SMSRecordService smsRecordService;
	
    @Autowired
    private DaoService dao;
    
    
    
    
    @RequestMapping(value = "/index", method = RequestMethod.GET,produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String index(HttpServletRequest request,HttpServletResponse response){
        
        return "thinks for visit";
        
    }
    
    public static void main(String[] args) {
		long id = System.currentTimeMillis();
		String pp = PWDUtils.encryptMD5AndBase64("123456");
		System.out.println(id+"  "+pp);
		
		
	}

    @CrossOrigin(origins="*",maxAge=3600)
    @RequestMapping(value = "/login", method = RequestMethod.POST,produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String login(HttpServletRequest request,HttpServletResponse response){

        logger.debug("login");

        BaseResponse baseResponse = new BaseResponse();

        String json = this.checkParameter(request);

        if(StringUtils.isStrEmpty(json)){
            baseResponse.setStatus(2);
            baseResponse.setStatusMsg("请求参数不合法");
            return JSONObject.toJSONString(baseResponse);
        }

        JSONObject jobj = JSONObject.parseObject(json);
        
        String mobileNo = jobj.getString("mobileNo");
        String pwd = jobj.getString("pwd");
        String md5Pwd = PWDUtils.encryptMD5AndBase64(pwd);
        
        
        int status = 1;
        String statusMsg ="";
        List<Proxy> proxyList = dao.findProxyByMobileNo(mobileNo);
        
        
        if(proxyList == null || proxyList.size() == 0){
            status = 1;
            statusMsg = "用户名或密码错误";
        }else{
        	Proxy proxy = proxyList.get(0);
        	String md5Pwd2 = PWDUtils.encryptMD5AndBase64(proxyList.get(0).getPwd());
        	
        	if(md5Pwd2.equals(md5Pwd))
        	{
        		proxy.setPwd(null);
                status = 0;
                proxy.setLoginDate(System.currentTimeMillis());
                setProxy(proxy);
                baseResponse.setMobileNo(proxy.getMobileno());
                baseResponse.setProxy(proxy);
                baseResponse.setSessionid(proxy.getId());
        	}
        	else
        	{
                status = 1;
                statusMsg = "用户名或密码错误";
        	}
        }
    	
        baseResponse.setStatus(status);
        baseResponse.setStatusMsg(statusMsg);
        String content = JSONObject.toJSONString(baseResponse);
        logger.debug("register content = {}",content);
        return content;
    }
    
    
    
    
      

    @CrossOrigin(origins="*",maxAge=3600)
    @RequestMapping(value = "/getChannel", method = RequestMethod.POST,produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getChannel(HttpServletRequest request,HttpServletResponse response){
        logger.debug("getChannel");
        BaseResponse baseResponse = new BaseResponse();

        String json = this.checkParameter(request);

        if(StringUtils.isStrEmpty(json)){
            baseResponse.setStatus(2);
            baseResponse.setStatusMsg("请求参数不合法");
            return JSONObject.toJSONString(baseResponse);
        }

        JSONObject jobj = JSONObject.parseObject(json);
        String proxyId = jobj.getString("sessionid");

        Proxy proxy = this.getProxy(proxyId);
        if(proxy == null)
        {
            baseResponse.setStatus(1);
            baseResponse.setStatusMsg("请重新登录");
            return JSONObject.toJSONString(baseResponse);
        }
        

        int first = 1;
        
        try {
            first = Integer.parseInt(jobj.getString("first"));
            baseResponse.setListSize(0+"");
            if(first==0)
            {
            	
                long listSize = dao.findChannelCouByProxyId(proxy.getId());
                baseResponse.setListSize(listSize+"");
            }
		} catch (Exception e) {
			// TODO: handle exception
		}

        List<Channel> channels = dao.findChannelByProxyId(proxy.getId());
        
        
        baseResponse.setChannelList(channels);
        baseResponse.setStatus(0);
        baseResponse.setStatusMsg("");
        String content = JSONObject.toJSONString(baseResponse);
        logger.debug("register content = {}",content);
        return content;
    }
    
    

    @CrossOrigin(origins="*",maxAge=3600)
    @RequestMapping(value = "/getChannelParameter", method = RequestMethod.POST,produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getChannelParameter(HttpServletRequest request,HttpServletResponse response){
        logger.debug("getChannelParameter");
        BaseResponse baseResponse = new BaseResponse();

        String json = this.checkParameter(request);

        if(StringUtils.isStrEmpty(json)){
            baseResponse.setStatus(2);
            baseResponse.setStatusMsg("请求参数不合法");
            return JSONObject.toJSONString(baseResponse);
        }

        JSONObject jobj = JSONObject.parseObject(json);
        String proxyId = jobj.getString("sessionid");

        Proxy proxy = this.getProxy(proxyId);
        if(proxy == null)
        {
            baseResponse.setStatus(1);
            baseResponse.setStatusMsg("请重新登录");
            return JSONObject.toJSONString(baseResponse);
        }
        



        List<Channel> channels = dao.findChannelByProxyId(proxy.getId());
    	
    	ArrayList<String> channelNameList = new ArrayList<String>();
    	ArrayList<String> channelNoList = new ArrayList<String>();
    	
    	for(int i = 0;i < channels.size();i++)
    	{
    		Channel channel = channels.get(i);
    		channelNameList.add(channel.getChannelName());
    		channelNoList.add(channel.getChannelNo());
    	}
    	
    	AppTools.removeDuplicate(channelNameList);
    	AppTools.removeDuplicate(channelNoList);
    	
        baseResponse.setChannelNameList(channelNameList);
        baseResponse.setChannelNoList(channelNoList);
        baseResponse.setStatus(0);
        baseResponse.setStatusMsg("");
        String content = JSONObject.toJSONString(baseResponse);
        logger.debug("register content = {}",content);
        return content;
    }
    
    

//    @CrossOrigin(origins="*",maxAge=3600)
//    @RequestMapping(value = "/getChannelValue", method = RequestMethod.POST,produces = "text/html;charset=UTF-8")
//    @ResponseBody
//    public String getChannelValue(HttpServletRequest request,HttpServletResponse response){
//        logger.debug("getChannelValue");
//        BaseResponse baseResponse = new BaseResponse();
//
//        String json = this.checkParameter(request);
//
//        if(StringUtils.isStrEmpty(json)){
//            baseResponse.setStatus(2);
//            baseResponse.setStatusMsg("请求参数不合法");
//            return JSONObject.toJSONString(baseResponse);
//        }
//
//        JSONObject jobj = JSONObject.parseObject(json);
//        String mobileNo = jobj.getString("sessionid");
//
//        Proxy proxy = this.getProxy(mobileNo);
//        if(proxy == null)
//        {
//            baseResponse.setStatus(1);
//            baseResponse.setStatusMsg("请重新登录");
//            return JSONObject.toJSONString(baseResponse);
//        }
//        StringBuffer where = new StringBuffer();
//        where.append(" 1 = 1 ");
//        String otheradminId = "";
//        if(!json.equals(""))
//        {
//        	
//            try {
//            	json = URLDecoder.decode(json, "utf-8");
//    		} catch (UnsupportedEncodingException e) {
//    			e.printStackTrace();
//    		}
//
//            JSONObject whereJson = JSONObject.parseObject(json);
//
//            otheradminId = whereJson.getString("adminId");
//            if(!StringUtils.isStrEmpty(otheradminId))
//            {
//            	where.append(" and adminId = "+otheradminId+" ");
//            }
//            
//        }
//
//    	ArrayList<String> adminIdList = new ArrayList<String>();
//
//        int level = admin.getLevel(); 
//        List<Channel> channels = null;
////        long []ids = new long[0];
//
//        if(level > 1)
//        {
//            if(level == 2)
//            {
//            	List<Admin> ads = dao.findAdminByHigherid(admin.getId());
////            	ids = new long[ads.size()+1];
//            	for(int i = 0;i < ads.size();i++)
//            	{
//            		Admin admin1 = ads.get(i);
////            		ids[i] = admin1.getId();
//            		adminIdList.add(admin1.getId()+","+admin1.getName()+","+admin1.getLevel());
//            	}
////            	ids[ids.length-1] = admin.getId();
//        		adminIdList.add(admin.getId()+","+admin.getName()+","+admin.getLevel());
//            }
//            else if(level == 3)
//            {
////            	ids = new long[1];
////            	ids[ids.length-1] = admin.getId();
//        		adminIdList.add(admin.getId()+","+admin.getName()+","+admin.getLevel());
//            }
//        }
//        else
//        {
//        	List<Admin> ads = dao.findAdminByDepartmentId(admin.getDepartmentId());
//        	for(int i = 0;i < ads.size();i++)
//        	{
//        		Admin admin1 = ads.get(i);
//        		adminIdList.add(admin1.getId()+","+admin1.getName()+","+admin1.getLevel());
//        	}
//        }
//        
//        
//        
//        Cache.DicCatche(dao);
//        List<Dictionary> dic1 = Cache.getDicList(1);
//        List<Dictionary> dic3 = Cache.getDicList(3);
//        List<Dictionary> dic4 = Cache.getDicList(4);
//        List<Dictionary> dic5 = Cache.getDicList(5);
//        List<Dictionary> dic6 = Cache.getDicList(6);
//        List<Dictionary> dic7 = Cache.getDicList(7);
//        List<Dictionary> dic8 = Cache.getDicList(8);
//        
//        
//        baseResponse.setChannelAttribute(dic3);
//        baseResponse.setChannelType(dic4);
//        baseResponse.setChannelSubdivision(dic5);
//        baseResponse.setAppList(dic1);
//        baseResponse.setCostingList(dic7);
//        baseResponse.setSettlementCycleList(dic6);
//        baseResponse.setRewardTypeList(dic8);
//        
//        
//        baseResponse.setAdminIdList(adminIdList);
//        baseResponse.setStatus(0);
//        baseResponse.setStatusMsg("");
//        String content = JSONObject.toJSONString(baseResponse);
//        logger.debug("register content = {}",content);
//        return content;
//    }
    

    @CrossOrigin(origins="*",maxAge=3600)
    @RequestMapping(value = "/getChannelAttribute", method = RequestMethod.POST,produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getChannelAttribute(HttpServletRequest request,HttpServletResponse response){
        logger.debug("getChannelAttribute");
        BaseResponse baseResponse = new BaseResponse();

        String json = this.checkParameter(request);

        if(StringUtils.isStrEmpty(json)){
            baseResponse.setStatus(2);
            baseResponse.setStatusMsg("请求参数不合法");
            return JSONObject.toJSONString(baseResponse);
        }

        JSONObject jobj = JSONObject.parseObject(json);
        String proxyId = jobj.getString("sessionid");

        Proxy proxy = this.getProxy(proxyId);
        if(proxy == null)
        {
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
        logger.debug("register content = {}",content);
        return content;
    }
    
 
   


    @CrossOrigin(origins="*",maxAge=3600)
    @RequestMapping(value = "/getChannelPermission", method = RequestMethod.POST,produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getChannelPermission(HttpServletRequest request,HttpServletResponse response){
        logger.debug("getChannelPermission");
        BaseResponse baseResponse = new BaseResponse();
        String json = this.checkParameter(request);

        if(StringUtils.isStrEmpty(json)){
            baseResponse.setStatus(2);
            baseResponse.setStatusMsg("请求参数不合法");
            return JSONObject.toJSONString(baseResponse);
        }
   

        JSONObject jobj = JSONObject.parseObject(json);
        String proxyId = jobj.getString("sessionid");

        Proxy proxy = this.getProxy(proxyId);
        if(proxy == null)
        {
            baseResponse.setStatus(1);
            baseResponse.setStatusMsg("请重新登录");
            return JSONObject.toJSONString(baseResponse);
        }
        
        
        ChannelPermission channelPermission = null;
        if(!json.equals(""))
        {
            try {
            	json = URLDecoder.decode(json, "utf-8");
    		} catch (UnsupportedEncodingException e) {
    			e.printStackTrace();
    		}

            JSONObject whereJson = JSONObject.parseObject(json);
            String id = whereJson.getString("permissionId");
            if(!StringUtils.isStrEmpty(id))
            {
            	channelPermission = dao.findChannelPermissionById(Long.parseLong(id));
            }

        }
        
        baseResponse.setChannelPermission(channelPermission);
        baseResponse.setStatus(0);
        baseResponse.setStatusMsg("");
        String content = JSONObject.toJSONString(baseResponse);
        logger.debug("register content = {}",content);
        return content;
    }
    
    

    @CrossOrigin(origins="*",maxAge=3600)
    @RequestMapping(value = "/checkChannelNo", method = RequestMethod.POST,produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String checkChannelNo(HttpServletRequest request,HttpServletResponse response){
        logger.debug("checkChannelNo");
        BaseResponse baseResponse = new BaseResponse();
        String json = this.checkParameter(request);

        if(StringUtils.isStrEmpty(json)){
            baseResponse.setStatus(2);
            baseResponse.setStatusMsg("请求参数不合法");
            return JSONObject.toJSONString(baseResponse);
        }


        JSONObject jobj = JSONObject.parseObject(json);
        String proxyId = jobj.getString("sessionid");

        Proxy proxy = this.getProxy(proxyId);
        if(proxy == null)
        {
            baseResponse.setStatus(1);
            baseResponse.setStatusMsg("请重新登录");
            return JSONObject.toJSONString(baseResponse);
        }

        String channelNo = jobj.getString("channelNo");
        
        long cou = dao.findCouByChannelNo(channelNo);
        if(cou == 0)
        {
            baseResponse.setStatus(0);
            baseResponse.setStatusMsg("渠道号可用");
        }
        else
        {
            baseResponse.setStatus(2);
            baseResponse.setStatusMsg("渠道号已经存在！");
        }
        
        
        String content = JSONObject.toJSONString(baseResponse);
        logger.debug("register content = {}",content);
        return content;
    }
    

    @CrossOrigin(origins="*",maxAge=3600)
    @RequestMapping(value = "/getReportform", method = RequestMethod.POST,produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getReportform(HttpServletRequest request,HttpServletResponse response){
        logger.debug("getReportform");
        BaseResponse baseResponse = new BaseResponse();
        String json = this.checkParameter(request);

        if(StringUtils.isStrEmpty(json)){
            baseResponse.setStatus(2);
            baseResponse.setStatusMsg("请求参数不合法");
            return JSONObject.toJSONString(baseResponse);
        }


        JSONObject jobj = JSONObject.parseObject(json);
        String proxyId = jobj.getString("sessionid");

        Proxy proxy = this.getProxy(proxyId);
        if(proxy == null)
        {
            baseResponse.setStatus(1);
            baseResponse.setStatusMsg("请重新登录");
            return JSONObject.toJSONString(baseResponse);
        }

        String dateType = "1";
        if(!json.equals(""))
        {
            try {
            	json = URLDecoder.decode(json, "utf-8");
    		} catch (UnsupportedEncodingException e) {
    			e.printStackTrace();
    		}
            JSONObject whereJson = JSONObject.parseObject(json);
            dateType = whereJson.getString("dateType"); 
        }

        List<Long> channelids = dao.findChannelIdByProxyId(proxy.getId());
        

        if(channelids.size() > 0)
        {

            int first = 1;
            
            try {
                first = Integer.parseInt(jobj.getString("first"));
    		} catch (Exception e) {
    			// TODO: handle exception
    		}
            if(first==0)
            {
                long listSize = dao.findFormCou(channelids,jobj,dateType);
                baseResponse.setListSize(listSize+"");
            }
            
            ChannelPermission channelPermission = dao.findChannelPermissionById(proxy.getId());
            
            
            if(dateType.equals("1"))
            {
            	List<Operate_reportform_day> reportforms = dao.findForm(channelids,jobj);
            	baseResponse.setReportforms_day(reportforms);
            	baseResponse.setChannelPermission(channelPermission);
            	deleteDayValue(reportforms, channelPermission);
            	
            }
            else
            {
            	List<Operate_reportform_month> reportforms = dao.findFormMonth(channelids,jobj);
            	baseResponse.setReportforms_month(reportforms);
            	baseResponse.setChannelPermission(channelPermission);
            	deleteMonthValue(reportforms, channelPermission);
            }
        	
        }
        else
        {
            baseResponse.setListSize("0");
        }
        
    	
        String content = JSONObject.toJSONString(baseResponse);
        logger.debug("register content = {}",content);
        return content;
    }
    
    
    private void deleteDayValue(List<Operate_reportform_day> od ,ChannelPermission channelPermission)
    {
    	for(int i = 0;i < od.size();i++)
    	{
    		if(channelPermission.getRegisterChartPermission() == 0)
    		{
    			od.get(i).setRegister(0);
    		}

    		if(channelPermission.getLoginChartPermission() == 0)
    		{
    			od.get(i).setActivation(0);;
    		}
    		if(channelPermission.getApplyChartPermission() == 0)
    		{
    			od.get(i).setUpload(0);
    		}
    		if(channelPermission.getLoanAcctChartPermission() == 0)
    		{
    			od.get(i).setAccount(0);
    		}
    		if(channelPermission.getCashNumCharPermission() == 0)
    		{
    			od.get(i).setLoan(0);
    		}
    		if(channelPermission.getFirstCashAmtChartPermission() == 0)
    		{
    			od.get(i).setFirstGetPer(0);
    			od.get(i).setFirstGetSum(0);
    		}

    		if(channelPermission.getTotalCashAmtChartPermission() == 0)
    		{
    			od.get(i).setChannelSum(0);
    		}
    		
    		
    		
    	}
    	
    	
    }
    

    private void deleteMonthValue(List<Operate_reportform_month> od ,ChannelPermission channelPermission)
    {
    	for(int i = 0;i < od.size();i++)
    	{
    		if(channelPermission.getRegisterChartPermission() == 0)
    		{
    			od.get(i).setRegister(0);
    		}

    		if(channelPermission.getLoginChartPermission() == 0)
    		{
    			od.get(i).setActivation(0);;
    		}
    		if(channelPermission.getApplyChartPermission() == 0)
    		{
    			od.get(i).setUpload(0);
    		}
    		if(channelPermission.getLoanAcctChartPermission() == 0)
    		{
    			od.get(i).setAccount(0);
    		}
    		if(channelPermission.getCashNumCharPermission() == 0)
    		{
    			od.get(i).setLoan(0);
    		}
    		if(channelPermission.getFirstCashAmtChartPermission() == 0)
    		{
    			od.get(i).setFirstGetPer(0);
    			od.get(i).setFirstGetSum(0);
    		}

    		if(channelPermission.getTotalCashAmtChartPermission() == 0)
    		{
    			od.get(i).setChannelSum(0);
    		}
    		
    		
    		
    	}
    	
    	
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    private void setProxy(Proxy proxy)
    {
    	proxy.setLoginDate(System.currentTimeMillis());
    	Cache.ProxyCache(dao);
    	Cache.updateProxyCatche(proxy);
    }
    

    private Proxy getProxy(String proxyId)
    {
    	Cache.ProxyCache(dao);
    	if(StringUtils.isStrEmpty(proxyId))
    		return null;
    	
    	Proxy proxy = Cache.getProxyCatche(Long.parseLong(proxyId));
    	if(proxy != null)
    	{

    		if(System.currentTimeMillis() - proxy.getLoginDate() > 7200000 )
    		{
    			proxy = null;
    		}
    		else
    		{
    			proxy.setLoginDate(System.currentTimeMillis());
    		}
    	}
    	return proxy;
    }
    
    
    
    
    
    
}
