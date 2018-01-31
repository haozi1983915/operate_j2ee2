package com.maimob.server.db.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.maimob.server.db.daoImpl.AdminDaoImpl;
import com.maimob.server.db.daoImpl.ChannelDaoImpl;
import com.maimob.server.db.daoImpl.ChannelPermissionDaoImpl;
import com.maimob.server.db.daoImpl.DaoWhere;
import com.maimob.server.db.daoImpl.DictionaryDaoImpl;
import com.maimob.server.db.daoImpl.OptimizationDaoImpl;
import com.maimob.server.db.daoImpl.ProxyDaoImpl;
import com.maimob.server.db.daoImpl.ReportformDaoImpl;
import com.maimob.server.db.daoImpl.ReportformMonthDaoImpl;
import com.maimob.server.db.daoImpl.RewardDaoImpl;
import com.maimob.server.db.entity.Admin;
import com.maimob.server.db.entity.Channel;
import com.maimob.server.db.entity.ChannelPermission;
import com.maimob.server.db.entity.Dictionary;
import com.maimob.server.db.entity.Operate_reportform_day;
import com.maimob.server.db.entity.Operate_reportform_month;
import com.maimob.server.db.entity.Optimization;
import com.maimob.server.db.entity.Proxy;
import com.maimob.server.db.entity.Reward;
import com.maimob.server.utils.Cache;
import com.maimob.server.utils.StringUtils;
 
@Service
public class DaoService {

    //注入UserDaoImpl
    @Autowired
    private AdminDaoImpl adminDaoImpl;
    
    @Autowired
    private ReportformDaoImpl reportformDaoImpl;
    
    @Autowired
    private OptimizationDaoImpl optimizationDaoImpl;
    @Autowired
    private ReportformMonthDaoImpl reportformMonthDaoImpl;
    @Autowired
    private ProxyDaoImpl proxyDaoImpl;

    @Autowired
    private ChannelDaoImpl channelDaoImpl;

    @Autowired
    private RewardDaoImpl rewardDaoImpl;

    @Autowired
    private DictionaryDaoImpl dictionaryDaoImpl;

    @Autowired
    private ChannelPermissionDaoImpl channelPermissionDaoImpl;
    
    public void saveAdmin(Admin admin){
        adminDaoImpl.saveOrUpdate(admin);
    }
    
    public void saveProxy(Proxy proxy){
        	if(proxy.getChannelPermission() != null)
        	channelPermissionDaoImpl.saveOrUpdate(proxy.getChannelPermission());
        	
        	proxyDaoImpl.saveOrUpdate(proxy); 
    }
    

    public void saveDictionary(Dictionary dic){
    	dictionaryDaoImpl.saveOrUpdate(dic);
    }
    

    public void saveChannel(Channel channel){

    	if(channel.getRewards() != null)
    	saveReward(channel.getRewards());
    	
    	channelDaoImpl.saveOrUpdate(channel);
    	
    	long cou = channelDaoImpl.findCouByProxyId(channel.getProxyId());
    	
    	long ProxyId = channel.getProxyId();
    	long permissionId = channel.getPermissionId();
    	String channelNo = "";
    	
    	if(channel.getLevel() == 1)
    	{
    		channelNo = channel.getChannelNo();
    	}
    	
    	proxyDaoImpl.Update(ProxyId, permissionId,cou,channelNo);
    	
    }
    
    public void saveReward(List<Reward> rewards){
    	for(int i = 0;i < rewards.size();i++)
    	{
    		rewards.get(i).setDate(System.currentTimeMillis());
        	rewardDaoImpl.saveOrUpdate(rewards.get(i));
    	}
    }
    
    public Proxy findProxyById(long id)
    {
    	List<Proxy> proxys = proxyDaoImpl.findAllById(id);
    	
    	if(proxys != null && proxys.size() > 0)
    	{
    		return proxys.get(0);
    	}
    	else{
    		return null;
    	}
    } 
    

    public long findCouByEmail(String email){
        return adminDaoImpl.findCouByEmail(email);
    }

    public long findCouByMobileNo(String mobileno){
        return proxyDaoImpl.findCouByMobileNo(mobileno);
    }

    public long findCouByChannelNo(String channelNo){
        return channelDaoImpl.findCouByChannelNo(channelNo);
    }


    public List<Admin> findAllAdmin(){
        return adminDaoImpl.findAll();
    }

    public List<Proxy> findAllProxy(){
        return proxyDaoImpl.findAll();
    }

    public List<Proxy> findAllProxy( int start,int maxCount){
        return proxyDaoImpl.findAll( start, maxCount);
    }
    public Admin findAdminById(String id){
        if(StringUtils.isStrEmpty(id)) return null;
        return adminDaoImpl.findSignalById(Admin.class, id);
    }

    public List<Admin> findAdminByName(String name){
        if(StringUtils.isStrEmpty(name)) return null;
        return adminDaoImpl.findAllByName( name);
    }

    public List<Admin> findAdminByEmail(String email){
        if(StringUtils.isStrEmpty(email)) return null;
        return adminDaoImpl.findAllByEmail( email);
    }
    

    public List<Proxy> findProxyByMobileNo(String mobileNo){
        if(StringUtils.isStrEmpty(mobileNo)) return null;
        return proxyDaoImpl.findAllByMobileNo(mobileNo);
    }
    
    

    public List<Admin> findAdminByLevel_departmentId(long level,long departmentId){
        return adminDaoImpl.findAllByLevel_departmentId(level, departmentId);
    }
    


    public long findAdminCou(String json){
        if(StringUtils.isStrEmpty(json)) return 0;

        
        return adminDaoImpl.findCouByParameter(DaoWhere.getAdminWhere(json,0)[0]);
    }

    public List<Admin> findAdmin(String json){
        if(StringUtils.isStrEmpty(json)) return null;
        String[] where = DaoWhere.getAdminWhere(json,1);
        return adminDaoImpl.findAllByParameter(where[0],Integer.parseInt(where[1]),Integer.parseInt(where[2]));
    }
    
    
    
    public List<Admin> findAdminByHigherid(long higherid){
        return adminDaoImpl.findAllByHigherid(higherid);
    }
    

    public List<Admin> findAdminByDepartmentId(long departmentId){
        return adminDaoImpl.findAllByDepartmentId(departmentId);
    }
    
    
    public long findChannelCouByAdminids(List<Long> adminids,JSONObject jobj){
    	String[] where = DaoWhere.getChannelWhere(jobj,0);
        return channelDaoImpl.findCouByParameter(adminids,where[0]);
    }
    

    public long findChannelCouByProxyId(long proxyId){
        return channelDaoImpl.findCouByProxyId(proxyId);
    }
    

    public List<Channel> findChannelByProxyId(long proxyId){
    	List<Channel> channels = channelDaoImpl.findByProxyId(proxyId);
    	for(Channel channel:channels)
    		channel.getAdminName();
        return channels;
    }
    
    
    public List<Channel> findChannelByAdminids(List<Long> adminids,JSONObject jobj){

    	String[] where = DaoWhere.getChannelWhere(jobj,1);
    	List<Channel> channels = channelDaoImpl.findByAdminids(adminids,where[0],Integer.parseInt(where[1]),Integer.parseInt(where[2]));
    	for(Channel channel:channels)
    		channel.getAdminName();
        return channels;
    }
    

    public List<Long> findChannelIdByAdminids(List<Long> adminids,JSONObject jobj){

    	String[] where = DaoWhere.getChannelWhere(jobj,1);
    	List<Long> channelids = channelDaoImpl.findIdByAdminids(adminids,where[0]);
        return channelids;
    }

    public List<Long> findChannelIdByProxyId(long proxyId){

    	List<Long> channelids = channelDaoImpl.findIdByProxyId(proxyId);
        return channelids;
    }
    
    
    
    public List<Channel> findMainChannel(long proxyid){
        return channelDaoImpl.findMainChannel(proxyid);
    }
    
    
    
    public List<Long> findProxyidByAdminids(List<Long> adminids){
        if(adminids.size() == 0) return null;
        return channelDaoImpl.findProxyidByAdminIds(adminids);
    }

    public long findProxyCouByIds(List<Long> proxyids,JSONObject jobj){
        return proxyDaoImpl.findCouByParameter(proxyids,DaoWhere.getProxyWhere(jobj,0)[0]);
    }

    public long findProxyCou(){
        return proxyDaoImpl.findCou();
    }

    public List<Proxy> findProxyByIds(List<Long> proxyids,JSONObject jobj){
    	
    	String[] where = DaoWhere.getProxyWhere(jobj,1);
    	
        return proxyDaoImpl.findByIds(proxyids, where[0],Integer.parseInt(where[1]),Integer.parseInt(where[2]));
    }
    

    public List<Proxy> findProxyNameByIds(List<Long> proxyids,JSONObject jobj){
    	
    	String[] where = DaoWhere.getProxyWhere(jobj,1);
    	
        return proxyDaoImpl.findNameByIds(proxyids, where[0]);
    }

    public List<Proxy> findProxyName(){
        return proxyDaoImpl.findName();
    }

    
    

    public List<Channel> findAllChannel(String where){
        return channelDaoImpl.findAll(where);
    }
    
    public ChannelPermission findChannelPermissionDaoImplById(long id){
        return channelPermissionDaoImpl.findById(id);
    }
    
    public void saveProxy(ChannelPermission channelPermission){
    	channelPermissionDaoImpl.saveOrUpdate(channelPermission);
    }
    

    public List<Reward> findRewardById(long id){
        return rewardDaoImpl.findById(id);
    }
    
    public List<Reward> findRewardByChannelId(long channelId){
        return rewardDaoImpl.findByChannelId(channelId);
    }


    public ChannelPermission findChannelPermissionById(long id){
        return channelPermissionDaoImpl.findById(id);
    }
    

    public List<Dictionary> findAllDictionary(){
        return dictionaryDaoImpl.findAll();
    }
    
    

    
    public long findFormCou(List<Long> channelids,JSONObject jobj ,String dateType){

    	String[] where = DaoWhere.getFromWhereForFrom(jobj,0);
    	
    	if(dateType.equals("1"))
    	{
            return reportformDaoImpl.findCouByParameter(channelids,where[0]);
    	}
    	else
    	{
            return reportformMonthDaoImpl.findCouByParameter(channelids,where[0]);
    		
    	}
    	
    }
    

    
    public long findFormCou(JSONObject jobj ,String dateType){

    	String[] where = DaoWhere.getFromWhereForFrom(jobj,0);
    	
    	if(dateType.equals("1"))
    	{
            return reportformDaoImpl.findCouByParameter(where[0]);
    	}
    	else
    	{
            return reportformMonthDaoImpl.findCouByParameter(where[0]);
    		
    	}
    	
    }
    
    
    

    
    
    public List<Operate_reportform_day> findForm(JSONObject jobj){
    	String[] where = DaoWhere.getFromWhereForFrom(jobj,1);
    	List<Operate_reportform_day> Operate_reportform_days = reportformDaoImpl.findByChannelids(where[0],Integer.parseInt(where[1]),Integer.parseInt(where[2]));
    	insertOperate_reportform_day(Operate_reportform_days);
        return Operate_reportform_days;
    }
    
    public void insertOperate_reportform_day(List<Operate_reportform_day> Operate_reportform_days)
    {
    	Cache.channelCatche(this);
    	for(int i = 0;i < Operate_reportform_days.size();i++)
    	{
    		Operate_reportform_day rf = Operate_reportform_days.get(i);
    		Channel channel = Cache.getChannelCatche(rf.getChannelId());
    		if(channel != null)
    		{
        		rf.setChannelName(channel.getChannelName());
        		Admin admin = Cache.getAdminCatche(channel.getAdminId());
        		if(admin != null)
        		rf.setAdminName(admin.getName());
        		String type = "";
        		Dictionary dic = Cache.getDic(channel.getAttribute());
        		if(dic != null)
        			type+=dic.getName().substring(0,1)+" ";
        		
        		dic = Cache.getDic(channel.getType());
        		if(dic != null)
        			type+=dic.getName()+" ";

        		dic = Cache.getDic(channel.getSubdivision());
        		if(dic != null)
        			type+=dic.getName()+" ";
        		
        		rf.setChannelType(type);
    			
    		}
    	}
    }
    
    
    public List<Operate_reportform_day> findForm(List<Long> channelids,JSONObject jobj){
    	String[] where = DaoWhere.getFromWhereForFrom(jobj,1);
    	List<Operate_reportform_day> Operate_reportform_days = reportformDaoImpl.findByChannelids(channelids,where[0],Integer.parseInt(where[1]),Integer.parseInt(where[2]));
    	insertOperate_reportform_day(Operate_reportform_days);
        return Operate_reportform_days;
    }
    
    

    public List<Operate_reportform_month> findFormMonth(List<Long> channelids,JSONObject jobj){
    	String[] where = DaoWhere.getFromWhereForFrom(jobj,1);
    	List<Operate_reportform_month> Operate_reportform_months = reportformMonthDaoImpl.findByChannelids(channelids,where[0],Integer.parseInt(where[1]),Integer.parseInt(where[2]));
    	insertOperate_reportform_month(Operate_reportform_months);
        return Operate_reportform_months;
    }

    public List<Operate_reportform_month> findFormMonth(JSONObject jobj){

    	String[] where = DaoWhere.getFromWhereForFrom(jobj,1);
    	List<Operate_reportform_month> Operate_reportform_months = reportformMonthDaoImpl.findByChannelids(where[0],Integer.parseInt(where[1]),Integer.parseInt(where[2]));
    	insertOperate_reportform_month(Operate_reportform_months);
        return Operate_reportform_months;
    }

    public void insertOperate_reportform_month(List<Operate_reportform_month> Operate_reportform_months)
    {
    	Cache.channelCatche(this);
		for(int i = 0;i < Operate_reportform_months.size();i++)
		{
			Operate_reportform_month rf = Operate_reportform_months.get(i);
			Channel channel = Cache.getChannelCatche(rf.getChannelId());
			if(channel != null)
			{
	    		rf.setChannelName(channel.getChannelName());
	    		Admin admin = Cache.getAdminCatche(channel.getAdminId());
	    		if(admin != null)
	    		rf.setAdminName(admin.getName());
	    		String type = "";
	    		Dictionary dic = Cache.getDic(channel.getAttribute());
	    		if(dic != null)
	    			type+=dic.getName().substring(0,1)+" ";
	    		
	    		dic = Cache.getDic(channel.getType());
	    		if(dic != null)
	    			type+=dic.getName()+" ";
	
	    		dic = Cache.getDic(channel.getSubdivision());
	    		if(dic != null)
	    			type+=dic.getName()+" ";
	    		
	    		rf.setChannelType(type);
				
			}
		}
	}
    
    

    

    public List<Optimization> findAllOptimizationByChannelId(String ChannelId){
        return optimizationDaoImpl.findByChannelId(ChannelId);
    }

    public List<Optimization> findAllOptimizationById(String Id){
        return optimizationDaoImpl.findById(Id);
    }
    
    
    
}





















