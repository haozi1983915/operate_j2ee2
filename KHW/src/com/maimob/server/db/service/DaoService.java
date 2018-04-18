package com.maimob.server.db.service;

import java.text.ParseException;
import java.util.List;

import com.maimob.server.db.daoImpl.*;
import com.maimob.server.db.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.maimob.server.importData.dao.OperateDao;
import com.maimob.server.utils.AppTools;
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
    private PermissionDaoImpl permissionDaoImpl;
    @Autowired
    private AdminPermissionDaoImpl adminPermissionDaoImpl;

    @Autowired
    private OptimizationTaskDaoImpl optimizationTaskDaoImpl;

    @Autowired
    private BalanceAccountDaoImpl balanceAccountDaoImpl;
    
    @Autowired
    private ChannelPermissionDaoImpl channelPermissionDaoImpl;

    @Autowired
    private PayCompanyDaoImpl payCompanyDaoImpl;

    //合作方实体对象
    @Autowired
    private PartnerDaoImpl partnerDaoImpl;
    
  //运营成本实体对象
    @Autowired
    private OperateCostDaoImpl operateCostDaoImpl;
    
    //kpi实体对象
    @Autowired
    private OperatekpiDaoImpl operatekpiDaoImpl;

    @Autowired
    private ChannelHistoryImpl channelHistoryImpl;
    
    public void addOperateCost(OperateCost operateCost){
    	operateCostDaoImpl.saveOrUpdate(operateCost);
    }
   
    public void deleteOperateCost(OperateCost operateCost){
    	operateCostDaoImpl.delete(operateCost);
    }
    public void addPartner(Partner partner){
    	partnerDaoImpl.saveOrUpdate(partner);
    }
    public void updatePartner(Partner partner){
    	partnerDaoImpl.update(partner);
    }
    
    public void saveOptimizationTask(OptimizationTask optimizationTask){
    	optimizationTaskDaoImpl.save(optimizationTask);
    }


    //保存kpi
    public void add(Operate_business_kpi operate_business_kpi){
    	operatekpiDaoImpl.save(operate_business_kpi);
    }
   
    public void deletePartner(Partner partner){
    	partnerDaoImpl.delete(partner);
    }
    
    public void deleteOptimizationTask(String id){
    	optimizationTaskDaoImpl.delete(id);
    }
    
    public void getOptimizationTaskByListid(String listid){
    	optimizationTaskDaoImpl.delete(listid);
    }
    

    public void saveBalanceAccount(BalanceAccount balanceAccount){
    		balanceAccountDaoImpl.saveOrUpdate(balanceAccount);
    }
    public void saveAdmin(Admin admin){
        adminDaoImpl.saveOrUpdate(admin);
    }
    
    public void saveProxy(Proxy proxy){
        	if(proxy.getChannelPermissionList() != null)
        	{
        		for(int i = 0;i < proxy.getChannelPermissionList().size();i++)
        		{
        			if(proxy.getChannelPermissionList().get(i).getId()==0)
        				channelPermissionDaoImpl.save(proxy.getChannelPermissionList().get(i));
        			else
                    	channelPermissionDaoImpl.update(proxy.getChannelPermissionList().get(i));
        			
        			
        		}
        		
        		
        	}
        	
        	proxyDaoImpl.saveOrUpdate(proxy); 
    }

    public void saveChannelPermission(ChannelPermission channelPermission){
    	if(channelPermission.getId()==0)
			channelPermissionDaoImpl.save(channelPermission);
		else
        	channelPermissionDaoImpl.update(channelPermission);
    }
    

    public void saveDictionary(Dictionary dic){
    		dictionaryDaoImpl.saveOrUpdate(dic);
		Cache.updateDicList(dic);
    }

    public void savePermission(Permission per){
    		permissionDaoImpl.saveOrUpdate(per);
    }
    

    public List<Permission> findPermissionByName(String name,String opType){
    		return permissionDaoImpl.findPermissionByName(name,opType);
    }
    public List<Permission> findPermissionByMeta(String meta,String opType){
		return permissionDaoImpl.findPermissionByMeta(meta,opType);
}

    public List<Dictionary> findDicByName(String type,String name){
    		return dictionaryDaoImpl.findByTypeName(type,name);
    }
    
    
    public List<Permission> findPermissionByType(long type){
    		List<Permission> aps =  permissionDaoImpl.findPermissionByType(type);
    		 return aps;
    }
 
    

    public List<AdminPermission> findAdminPermissionByType(String adminid,String type,String opType){
    		List<AdminPermission> aps = adminPermissionDaoImpl.findPermissionByType(adminid, type, opType);
    	 	return aps;
    }

    public void saveAdminPermission(AdminPermission per){
    		adminPermissionDaoImpl.saveOrUpdate(per);
    }

    public void saveChannel(Channel channel){
    	long rewardid = 0;
    	if(channel.getRewards() != null)
    		rewardid = saveReward(channel.getRewards());
    	
    	channel.setRewardId(rewardid);
    	if(channel.isNew())
    	{
        	channelDaoImpl.save(channel);
    	}
    	else
    	{
        	channelDaoImpl.update(channel);
    		
    	}
    	Cache.channelCatche(this);
		Cache.updateChannelCatche(channel);
    }
    
    public void updateProxy(Channel channel)
    {
    	long ProxyId = channel.getProxyId();
    	long cou = channelDaoImpl.findCouByProxyId(ProxyId);
    	
    	String channelNo = "";
    	
    	if(channel.getLevel() == 1)
    	{
    		channelNo = channel.getChannelNo();
    	}
    	
    	proxyDaoImpl.Update(ProxyId,cou,channelNo);
    }
    
    public long saveReward(List<Reward> rewards){
    	long id = AppTools.getId();
    	long date = System.currentTimeMillis();
		try {
			String now = AppTools.longToString(date, "yyyy-MM-dd");
			long date1 = AppTools.stringToLong(now, "yyyy-MM-dd");
			OperateDao od = new OperateDao();
			od.deleteReward(date1, rewards.get(0).getChannelId());
			od.close();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
    	for(int i = 0;i < rewards.size();i++)
    	{
    		rewards.get(i).setId(id);
    		rewards.get(i).setDate(System.currentTimeMillis());
			rewardDaoImpl.save(rewards.get(i));
    	}
    	return id;
    }
    

    public void saveRewardMain(List<Reward> rewards){
    	
    	long id = AppTools.getId();
    	if(rewards != null)
    	{
        	for(int i = 0;i < rewards.size();i++)
        	{
        		Reward reward = rewards.get(i);
        		reward.setDate(System.currentTimeMillis());
        		
        		
        		if(reward.getId() == 0)
        		{
        			reward.setId(id); 
        		} 

        		rewardDaoImpl.saveOrUpdate(reward);
        	}
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

    public long findCouByCompany(String company){
        return balanceAccountDaoImpl.findCouByCompany(company);
    }

    public long findCouByTaxpayerNo(String TaxpayerNo){
        return balanceAccountDaoImpl.findCouByTaxpayerNo(TaxpayerNo);
    }

    public long findCouByAccountNo(String AccountNo){
        return balanceAccountDaoImpl.findCouByAccountNo(AccountNo);
    }


    public long findCouByEmail(String email){
        return adminDaoImpl.findCouByEmail(email);
    }

    public long findCouByMobileNo(String mobileno){
        return proxyDaoImpl.findCouByMobileNo(mobileno);
    }

    public long findCouByChannel(String channel,String appid){
        return channelDaoImpl.findCouByChannel(channel,appid);
    }


    public List<Admin> findAllAdmin(){
        return adminDaoImpl.findAll();
    }
    
    //查询商务账号
    public List<Admin> findBusinessAdmin(){
        return adminDaoImpl.findBusinessAdmin();
    }
    

    public List<Proxy> findAllProxy(){
        return proxyDaoImpl.findAll();
    }

    public List<Proxy> findAllProxy(JSONObject jobj){

    	String[] where = DaoWhere.getProxyWhere(jobj,1);
        return proxyDaoImpl.findAll(where[0],Integer.parseInt(where[1]),Integer.parseInt(where[2]));
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

    public List<BalanceAccount> findBalanceAccount(JSONObject jobj){
    		String[] where = DaoWhere.getBalanceAccountWhere(jobj);
        
        	List<BalanceAccount>  bl = balanceAccountDaoImpl.findAll(where[0]);
     	for(BalanceAccount balanceAccount:bl)
     		balanceAccount.getUpdateAdminName();
     	return bl;
    }

    public List<BalanceAccount> findAllBalanceAccount(){
    	return balanceAccountDaoImpl.findAllBalanceAccount();
    }

    public List<BalanceAccount> findBalanceAccountById(String id){
        
        	List<BalanceAccount>  bl = balanceAccountDaoImpl.findById(id);
     	for(BalanceAccount balanceAccount:bl)
     		balanceAccount.getUpdateAdminName();
     	
     	return bl;
    }

    public List<BalanceAccount> findBalanceAccountById(JSONObject jobj){
    	return balanceAccountDaoImpl.findBalanceAccountById(jobj);
    }
    

    public long findChannelCouByProxyId(JSONObject jobj){
    	String[] where = DaoWhere.getChannelWhere(jobj,0);
        return channelDaoImpl.findCouByProxyId(where[0]);
    }
    
    

    public List<Channel> findMainByProxyId(String proxyid){
    	List<Channel> channels = channelDaoImpl.findMainByProxyId(proxyid);
    	for(Channel channel:channels)
    		channel.getAdminName();
        return channels;
    }
    

    public List<Channel> findChannelByProxyId(JSONObject jobj){
    	String[] where = DaoWhere.getChannelWhere(jobj,1);
    	List<Channel> channels = channelDaoImpl.findByProxyId(where[0],Integer.parseInt(where[1]),Integer.parseInt(where[2]));
    	for(Channel channel:channels)
    		channel.getAdminName();
        return channels;
    }

    public List<Channel> findChannelByProxyId(String proxyid){
    	List<Channel> channels = channelDaoImpl.findByProxyId(proxyid);
    	for(Channel channel:channels)
    		channel.getAdminName();
        return channels;
    }
    

    public List<Channel> findChannelByProxyId_appid(String proxyid,String appid){
    	List<Channel> channels = channelDaoImpl.findByProxyId_appid(proxyid, appid);
    	for(Channel channel:channels)
    		channel.getAdminName();
        return channels;
    }
    

    public List<Channel> findChannelByChannel(String channel){
    	List<Channel> channels = channelDaoImpl.findByChannel(channel);
        return channels;
    }
    
    
    public List<Channel> findChannelByAdminids(List<Long> adminids,JSONObject jobj){

    	String[] where = DaoWhere.getChannelWhere(jobj,1);
    	List<Channel> channels = channelDaoImpl.findByAdminids(adminids,where[0],Integer.parseInt(where[1]),Integer.parseInt(where[2]));
    	for(Channel channel:channels)
    		channel.getAdminName();
        return channels;
    }
    
    public void updateChannelOptimizationId(long id,long optimizationId){
    	channelDaoImpl.UpdateOptimization(optimizationId, id);
    }

    public void updateChannelOptimization_startDate(long optimizationId,long id,int optimization,long startDate){
    	channelDaoImpl.UpdateOptimization_startDate(optimizationId,optimization, startDate,id);
    }

    public void updateChannelStuts(long id,int status){
    	channelDaoImpl.UpdateStuts(status, id);
    }

    public void updateChannelSynchronous(long id,int synchronous){
    	channelDaoImpl.UpdateSynchronous(synchronous, id);
    }

    public void updateChannelName(long id,String channelName,String pwd){
    	channelDaoImpl.UpdateChannelName(channelName,pwd, id);
    }
    

    public void updateChannelType(String rewardPrice , long rewardTypeId,long rewardId,long proxyId,long attribute, long type, long subdivision,long adminId){
    	channelDaoImpl.UpdateType(rewardPrice, rewardTypeId,rewardId,attribute, type, subdivision, proxyId,adminId);
    }

    public List<Long> findChannelIdByAdminids(List<Long> adminids,JSONObject jobj){

    	String[] where = DaoWhere.getChannelWhere(jobj,1);
    	List<Long> channelids = channelDaoImpl.findIdByAdminids(adminids,where[0]);
        return channelids;
    }

    public List<Long> findChannelIdByProxyId(long proxyId,JSONObject jobj){

    	String[] where = DaoWhere.getChannelWhere(jobj,1);
    	List<Long> channelids = channelDaoImpl.findIdByProxyId(proxyId,where[0]);
        return channelids;
    }
    
    
    
    public List<Channel> findMainChannel(long proxyid){
        return channelDaoImpl.findMainChannel(proxyid);
    }
    
    public List<Channel> findMainChannels(List<Long> proxyid){
        return channelDaoImpl.findMainChannels(proxyid);
    }
    
    public List<Long> findProxyidByAdminids(List<Long> adminids){
        if(adminids.size() == 0) return null;
        return channelDaoImpl.findProxyidByAdminIds(adminids);
    }

    public long findProxyCouByIds(List<Long> proxyids,JSONObject jobj){
        return proxyDaoImpl.findCouByParameter(proxyids,DaoWhere.getProxyWhere(jobj,0)[0]);
    }

    public long findProxyCou(JSONObject jobj){
        return proxyDaoImpl.findCou(DaoWhere.getProxyWhere(jobj,0)[0]);
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

    public List<Reward> findRewardByChannelId(long channelId,long appid){
        return rewardDaoImpl.findByChannelId(channelId,appid);
    }

    public ChannelPermission findChannelPermissionById(long id){
        return channelPermissionDaoImpl.findById(id);
    }

    public ChannelPermission findChannelPermissionByProxyId_appid(String ProxyId,String appid){
    		List<ChannelPermission> cps =	channelPermissionDaoImpl.findByProxyId(ProxyId, appid);
    		if(cps != null && cps.size() > 0)
    			return cps.get(0);
        return  null;
    }
    

    public List<operate_pay_company> findPayCompanyByProxyId(String proxyId){
        return payCompanyDaoImpl.findAll(proxyId);
    } 

    public void savePayConpany(operate_pay_company opc){
         payCompanyDaoImpl.saveOrUpdate(opc);
    } 
    

    public List<ChannelPermission> findChannelPermissionByProxyId(String proxyId,String appid){
        return channelPermissionDaoImpl.findByProxyId(proxyId,appid);
    } 
    public List<ChannelPermission> findChannelPermissionByProxyId(String proxyId){
        return channelPermissionDaoImpl.findByProxyId(proxyId);
    } 
    

    public List<ChannelPermission> findChannelPermissionByProxyId_appidList(String proxyId,String appidList){
        return channelPermissionDaoImpl.findByProxyId_appidList(proxyId,appidList);
    } 
    
    
    public List<Dictionary> findAllDictionary(){
        return dictionaryDaoImpl.findAll();
    }

    public List<Dictionary> findDictionaryByType(String type){
        return dictionaryDaoImpl.findByType(type);
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
    

    public Operate_reportform_day findOperate_reportform_day_Sum(List<Long> channelids,JSONObject jobj ,String dateType){
    	String[] where = DaoWhere.getFromWhereForFrom(jobj,0);
		List<Operate_reportform_day> od = reportformDaoImpl.findSumByChannelids(channelids,where[0]);
		return od.get(0);
    }
    

    public Operate_reportform_month findOperate_reportform_month_Sum(List<Long> channelids,JSONObject jobj ,String dateType){
    	String[] where = DaoWhere.getFromWhereForFrom(jobj,0);
		List<Operate_reportform_month> od = reportformMonthDaoImpl.findSumByChannelids(channelids,where[0]);
		return od.get(0);
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

    
    public List<Operate_reportform_day> findFormDay(List<Long> channelids,JSONObject jobj){
    	String[] where = DaoWhere.getFromWhereForFrom(jobj,1);
    	List<Operate_reportform_day> Operate_reportform_days = reportformDaoImpl.findByChannelids(channelids,where[0],Integer.parseInt(where[1]),Integer.parseInt(where[2]));
    	insertOperate_reportform_day(Operate_reportform_days);
        return Operate_reportform_days;
    }
    

    public List<Operate_reportform_day> findSumFormDay(List<Long> channelids,JSONObject jobj){
    	String[] where = DaoWhere.getFromWhereForFrom(jobj,1);
    	List<Operate_reportform_day> Operate_reportform_days = reportformDaoImpl.findSumByChannelids(channelids,where[0]);
    	insertOperate_reportform_day(Operate_reportform_days);
        return Operate_reportform_days;
    }
    

    public List<Operate_reportform_day> findAdminSumFormDay(List<Long> channelids,JSONObject jobj){
    	String[] where = DaoWhere.getFromWhereForFrom(jobj,1);
    	List<Operate_reportform_day> Operate_reportform_days = reportformDaoImpl.findAdminSumByChannelids(channelids,where[0]);
    	insertOperate_reportform_day(Operate_reportform_days);
        return Operate_reportform_days;
    }
    
    
    
    
    

    public List<Operate_reportform_month> findFormMonth(List<Long> channelids,JSONObject jobj){
    	String[] where = DaoWhere.getFromWhereForFrom(jobj,1);
    	List<Operate_reportform_month> Operate_reportform_months = reportformMonthDaoImpl.findByChannelids(channelids,where[0],Integer.parseInt(where[1]),Integer.parseInt(where[2]));
    	insertOperate_reportform_month(Operate_reportform_months);
        return Operate_reportform_months;
    }
    

    public List<Operate_reportform_month> findFormMon(List<Long> channelids,JSONObject jobj){
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
    
    

    public List<Optimization> findAllOptimizationByChannelIdLast(String ChannelId){
        
        
        List<Optimization> ops = optimizationDaoImpl.findByChannelIdLast(ChannelId);
    	
    	
        return ops;
    }
    

    public List<Optimization> findAllOptimizationByChannelId(String ChannelId){
        
        
        List<Optimization> ops = optimizationDaoImpl.findByChannelId(ChannelId);
    	
		for (int i = 0; i < ops.size(); i++) {
			Optimization op = ops.get(i);

			Admin admin = Cache.getAdminCatche(op.getAdminId());
			if (admin != null)
				op.setAdminName(admin.getName());
		}
    	
    	
        return ops;
    }

    public List<Optimization> findAllOptimizationById(String Id){
    	
    	List<Optimization> ops = optimizationDaoImpl.findById(Id);
    	
    	for(int i = 0;i < ops.size();i++)
    	{
    		Optimization op = ops.get(i);

    		Admin admin = Cache.getAdminCatche(op.getAdminId());
    		if(admin != null)
    			op.setAdminName(admin.getName());
    	}
    	
    	
        return ops;
    }
    

    public void saveOptimization(Optimization optimization){
        optimizationDaoImpl.saveOrUpdate(optimization);
    }
//    public List<OptimizationTask> findByNoFinish(){
    public List<OptimizationTask> findByNoFinishOptimizationTask()
    {
    	List<OptimizationTask> ots = optimizationTaskDaoImpl.findByNoFinish();

    	for(int i = 0;i < ots.size();i++)
    	{
    		OptimizationTask op = ots.get(i);

    		Admin admin = Cache.getAdminCatche(op.getAdminId());
    		if(admin != null)
    			op.setAdminName(admin.getName());
    	}
    	return ots;
    	
    }
    

    public List<OptimizationTask> findByAllOptimizationTask(JSONObject jobj)
    {
    	
        String[] where = DaoWhere.getOptimizationTaskWhere(jobj);
    	List<OptimizationTask> ots = optimizationTaskDaoImpl.findByAll(where[0]);
    	for(int i = 0;i < ots.size();i++)
    	{
    		OptimizationTask op = ots.get(i);
    		Admin admin = Cache.getAdminCatche(op.getAdminId());
    		if(admin != null)
    			op.setAdminName(admin.getName());
    	}
    	return ots;
    }
    

	   public int updatepwd(String email,String pwd) {
 	return adminDaoImpl.updatePwd(email, pwd);
 }
    
	   public int updateflag(String email,int flag) {
 	return adminDaoImpl.updateflag(email, flag);
 }
	   
	   public List<Partner> findAllPartners(){
		   return partnerDaoImpl.findAll();
	   }
	   
	   public List<Partner> findAllById(String id){
		   return partnerDaoImpl.findAllById(id);
	   }

        public int updateAllStatusByProxyId(Long proxyId) {
            return channelDaoImpl.updateAllStatusByProxyId(proxyId);
        }

        public void saveChannelHistory(OperateChannelHistory channelHistory) {
            channelHistoryImpl.saveChannelHistory(channelHistory);
        }

    public List<OperateChannelHistory> findChannelHistory(OperateChannelHistory channelHistory) {
        return channelHistoryImpl.findAll();
    }
}