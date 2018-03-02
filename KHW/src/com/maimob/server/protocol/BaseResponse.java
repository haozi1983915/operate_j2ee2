package com.maimob.server.protocol;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

public class BaseResponse {

    protected int status;

    protected String statusMsg;
    
    protected String sessionid;
    
    protected long id;
    
    private List<Proxy> proxyList;
    
    private List<Channel> channelList;
    
    private List<Reward> rewardList;
    

    private List<Admin> adminList;
    private ChannelPermission channelPermission = null;
    
    private Admin admin; 
    

    private Channel channel;
    private Proxy proxy;
    
    private String listSize; 

    private List<Operate_reportform> reportforms_day;
    private List<Operate_reportform> reportforms_month;


    private List<Operate_reportform> reportforms;
    
    private List<Map<String, String>> reportforms_operate;
    
    
    
	ArrayList<String> channelNameList;
	ArrayList<String> channelNoList;
	ArrayList<String> adminIdList;
    
	List<Dictionary> channelAttribute; 
	List<Dictionary> channelType; 
	List<Dictionary> channelSubdivision; 

	List<Dictionary> appList; 

	List<Dictionary> costingList;
	List<Dictionary> settlementCycleList;
	List<Optimization> optimizationList;

	List<Dictionary> RewardTypeList;

	List<Dictionary> fromTypeList;
	
	List<OptimizationTask> optimizationTaskList;

    boolean conversion = false;
    
	long mobileNo=-1;
	
	boolean finish = false;
	
	OptimizationTask runOptimizationTask;
	List<Map<String,String>> costList;
	
	
	public Channel getChannel() {
		return channel;
	}
	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	public List<Map<String, String>> getCostList() {
		return costList;
	}
	public void setCostList(List<Map<String, String>> costList) {
		this.costList = costList;
	}
	public List<Map<String, String>> getReportforms_operate() {
		return reportforms_operate;
	}
	public void setReportforms_operate(List<Map<String, String>> reportforms_operate) {
		this.reportforms_operate = reportforms_operate;
	}
	public OptimizationTask getRunOptimizationTask() {
		return runOptimizationTask;
	}
	public void setRunOptimizationTask(OptimizationTask runOptimizationTask) {
		this.runOptimizationTask = runOptimizationTask;
	}
	public List<OptimizationTask> getOptimizationTaskList() {
		return optimizationTaskList;
	}
	public void setOptimizationTaskList(List<OptimizationTask> optimizationTaskList) {
		this.optimizationTaskList = optimizationTaskList;
	}
	public boolean isFinish() {
		return finish;
	}
	public void setFinish(boolean finish) {
		this.finish = finish;
	}
	public List<Dictionary> getFromTypeList() {
		return fromTypeList;
	}
	public void setFromTypeList(List<Dictionary> fromTypeList) {
		this.fromTypeList = fromTypeList;
	}
	public boolean isConversion() {
		return conversion;
	}
	public void setConversion(boolean conversion) {
		this.conversion = conversion;
	}
	public List<Operate_reportform> getReportforms() {
		return reportforms;
	}
	public void setReportforms(List<Operate_reportform> reportforms) {
		this.reportforms = reportforms;
	}
	public List<Operate_reportform> getReportforms_day() {
		return reportforms_day;
	}
	public void setReportforms_day(List<Operate_reportform> reportforms_day) {
		this.reportforms_day = reportforms_day;
	}
	public List<Operate_reportform> getReportforms_month() {
		return reportforms_month;
	}
	public void setReportforms_month(List<Operate_reportform> reportforms_month) {
		this.reportforms_month = reportforms_month;
	}
	public List<Optimization> getOptimizationList() {
		return optimizationList;
	}
	public void setOptimizationList(List<Optimization> optimizationList) {
		this.optimizationList = optimizationList;
	}
	public long getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(long mobileNo) {
		this.mobileNo = mobileNo;
	}
	public Proxy getProxy() {
		return proxy;
	}
	public void setProxy(Proxy proxy) {
		this.proxy = proxy;
	}
	
	public List<Dictionary> getRewardTypeList() {
		return RewardTypeList;
	}
	public void setRewardTypeList(List<Dictionary> rewardTypeList) {
		RewardTypeList = rewardTypeList;
	}
	public List<Dictionary> getCostingList() {
		return costingList;
	}
	public void setCostingList(List<Dictionary> costingList) {
		this.costingList = costingList;
	}
	public List<Dictionary> getSettlementCycleList() {
		return settlementCycleList;
	}
	public void setSettlementCycleList(List<Dictionary> settlementCycleList) {
		this.settlementCycleList = settlementCycleList;
	}
	public List<Dictionary> getAppList() {
		return appList;
	}
	public void setAppList(List<Dictionary> appList) {
		this.appList = appList;
	}
	public List<Dictionary> getChannelAttribute() {
		return channelAttribute;
	}
	public void setChannelAttribute(List<Dictionary> channelAttribute) {
		this.channelAttribute = channelAttribute;
	}
	public List<Dictionary> getChannelType() {
		return channelType;
	}
	public void setChannelType(List<Dictionary> channelType) {
		this.channelType = channelType;
	}
	public List<Dictionary> getChannelSubdivision() {
		return channelSubdivision;
	}
	public void setChannelSubdivision(List<Dictionary> channelSubdivision) {
		this.channelSubdivision = channelSubdivision;
	}
	public ArrayList<String> getChannelNameList() {
		return channelNameList;
	}
	public void setChannelNameList(ArrayList<String> channelNameList) {
		this.channelNameList = channelNameList;
	}
	public ArrayList<String> getChannelNoList() {
		return channelNoList;
	}
	public void setChannelNoList(ArrayList<String> channelNoList) {
		this.channelNoList = channelNoList;
	}
	public ArrayList<String> getAdminIdList() {
		return adminIdList;
	}
	public void setAdminIdList(ArrayList<String> adminIdList) {
		this.adminIdList = adminIdList;
	}
	public String getListSize() {
		return listSize;
	}
	public void setListSize(String listSize) {
		this.listSize = listSize;
	}
	
	public String getSessionid() {
		return sessionid;
	}
	public void setSessionid(long sessionid) {
		this.sessionid = sessionid+"";
	} 
	public void setSessionid(String sessionid) {
		this.sessionid = sessionid+"";
	} 
	public List<Admin> getAdminList() {
		return adminList;
	}
	public void setAdminList(List<Admin> adminList) {
		this.adminList = adminList;
	}
	public Admin getAdmin() {
		return admin;
	}
	public void setAdmin(Admin admin) {
		admin.setPwd(null);
		this.admin = admin;
	}
	public ChannelPermission getChannelPermission() {
		return channelPermission;
	}
	public void setChannelPermission(ChannelPermission channelPermission) {
		this.channelPermission = channelPermission;
	}
	public List<Reward> getRewardList() {
		return rewardList;
	}
	public void setRewardList(List<Reward> rewardList) {
		this.rewardList = rewardList;
	}
	public List<Proxy> getProxyList() {
		return proxyList;
	}
	public void setProxyList(List<Proxy> proxyList) {
		this.proxyList = proxyList;
	}
	public List<Channel> getChannelList() {
		return channelList;
	}
	public void setChannelList(List<Channel> channelList) {
		this.channelList = channelList;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    
    public String getStatusMsg() {
        return statusMsg;
    }
    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    
    public BaseResponse(){}
    
}
