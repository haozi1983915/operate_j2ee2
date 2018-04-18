package com.maimob.server.protocol;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.maimob.server.db.entity.*;

public class BaseResponse {

    int isRefreshBill = 0;
    protected int status;

    protected String statusMsg;
    
    protected String sessionid;
    
    protected long id;
    
    private List<Proxy> proxyList;
    
    private List<Channel> channelList;
    
    private List<Reward> rewardList; 
	
	Map<String,List<Reward>> mainReward;
	
    List<BalanceAccount> balanceAccountList;

    private List<Admin> adminList;

    private List<ChannelPermission> channelPermissionList;
    
    private Admin admin; 
    List<operate_pay_company> payCompanyList;
    
    private Channel channel;
    private Proxy proxy;
    
    private String listSize; 

    private List<Operate_reportform> reportforms_day;
    private List<Operate_reportform> reportforms_month;


    private List<Operate_reportform> reportforms;
    
    private List<Map<String, String>> reportforms_operate;

    private List<Permission> permissions;

    private List<Map<String, String>> adminPermissionList;

    private List<Map<String, String>> reportforms_admin;
    protected String channelNo;
	ArrayList<String> channelNameList;
	ArrayList<String> channelNoList;
	ArrayList<String> adminIdList;
    
	List<Dictionary> channelAttribute; 
	List<Dictionary> channelType; 
	List<Dictionary> channelSubdivision; 
	List<Dictionary> permissionTypeList; 
	List<Dictionary> appList; 
	List<Dictionary> payList; 
	List<Dictionary> billStatusList;  
	List<Dictionary> invoiceContentList;
 
	List<Dictionary> billAdminStatusList;  
	List<Dictionary> billAdminLastStatusList;
	List<Dictionary> balanceAccountAttribute; 
	List<Dictionary> costingList;
	List<Dictionary> settlementCycleList;
	List<Optimization> optimizationList;

	List<Partner> partnerList;
	List<Dictionary> RewardTypeList;

	List<Dictionary> fromTypeList;
	List<OptimizationTask> optimizationTaskList;
	List<OperateChannelHistory> channelHistories;
	List<Map<String, String>> firstPage;
    boolean conversion = false;
	long mobileNo=-1;
	
	boolean finish = false;
	
	String mainChannel;
	OptimizationTask runOptimizationTask;
	List<Map<String,String>> costList;
	
	List<UserPermission> userPermission;
	List channelTypeList;
	ChannelPermission channelPermission;
	ArrayList<String> companyList;
	ArrayList<String> taxpayerNoList;
	ArrayList<String> accountNoList;
	List<Map<String,String>> billList;
	
	Map<String,String> bill;
	List<String> proxyNameList;
	List<String> mainChannelNameList;
	List<Map<String,String>> billAdminList;
	List<Map<String,String>> billDetails;
	List<Map<String,String>> billStepList;
	
	
	 
	public List<Partner> getPartnerList() {
		return partnerList;
	}
	public void setPartnerList(List<Partner> partnerList) {
		this.partnerList = partnerList;
	}
	public int getIsRefreshBill() {
		return isRefreshBill;
	}
	public void setIsRefreshBill(int isRefreshBill) {
		this.isRefreshBill = isRefreshBill;
	}
	public List<Dictionary> getBillAdminLastStatusList() {
		return billAdminLastStatusList;
	}
	public void setBillAdminLastStatusList(List<Dictionary> billAdminLastStatusList) {
		this.billAdminLastStatusList = billAdminLastStatusList;
	}
	public List<Dictionary> getBillAdminStatusList() {
		return billAdminStatusList;
	}
	public void setBillAdminStatusList(List<Dictionary> billAdminStatusList) {
		this.billAdminStatusList = billAdminStatusList;
	}
	public List<Map<String, String>> getBillStepList() {
		return billStepList;
	}
	public void setBillStepList(List<Map<String, String>> billStepList) {
		this.billStepList = billStepList;
	}
	public List<Map<String, String>> getBillDetails() {
		return billDetails;
	}
	public void setBillDetails(List<Map<String, String>> billDetails) {
		this.billDetails = billDetails;
	}
	public Map<String, String> getBill() {
		return bill;
	}
	public void setBill(Map<String, String> bill) {
		this.bill = bill;
	}
	public List<Dictionary> getBillStatusList() {
		return billStatusList;
	}
	public void setBillStatusList(List<Dictionary> billStatusList) {
		this.billStatusList = billStatusList;
	}
	public List<String> getProxyNameList() {
		return proxyNameList;
	}
	public void setProxyNameList(List<String> proxyNameList) {
		this.proxyNameList = proxyNameList;
	}
	public List<String> getMainChannelNameList() {
		return mainChannelNameList;
	}
	public void setMainChannelNameList(List<String> mainChannelNameList) {
		this.mainChannelNameList = mainChannelNameList;
	}
	public List<Map<String, String>> getBillAdminList() {
		return billAdminList;
	}
	public void setBillAdminList(List<Map<String, String>> billAdminList) {
		this.billAdminList = billAdminList;
	}
	public List<operate_pay_company> getPayCompanyList() {
		return payCompanyList;
	}
	public void setPayCompanyList(List<operate_pay_company> payCompanyList) {
		this.payCompanyList = payCompanyList;
	}

 
	public List<Map<String, String>> getBillList() {
		return billList;
	}
	public void setBillList(List<Map<String, String>> billList) {
		this.billList = billList;
	}
	public String getMainChannel() {
		return mainChannel;
	}
	public void setMainChannel(String mainChannel) {
		this.mainChannel = mainChannel;
	}
	public Map<String, List<Reward>> getMainReward() {
		return mainReward;
	}
	public void setMainReward(Map<String, List<Reward>> mainReward) {
		this.mainReward = mainReward;
	}
	public List<Dictionary> getPayList() {
		return payList;
	} 
	public void setPayList(List<Dictionary> payList) {
		this.payList = payList;
	}

	public ChannelPermission getChannelPermission() {
		return channelPermission;
	}
	public void setChannelPermission(ChannelPermission channelPermission) {
		this.channelPermission = channelPermission;
	}
	public List<ChannelPermission> getChannelPermissionList() {
		return channelPermissionList;
	}
	public void setChannelPermissionList(List<ChannelPermission> channelPermissionList) {
		this.channelPermissionList = channelPermissionList;
	}
	public ArrayList<String> getCompanyList() {
		return companyList;
	}
	public void setCompanyList(ArrayList<String> companyList) {
		this.companyList = companyList;
	}
	public ArrayList<String> getTaxpayerNoList() {
		return taxpayerNoList;
	}
	public void setTaxpayerNoList(ArrayList<String> taxpayerNoList) {
		this.taxpayerNoList = taxpayerNoList;
	}
	public ArrayList<String> getAccountNoList() {
		return accountNoList;
	}
	public void setAccountNoList(ArrayList<String> accountNoList) {
		this.accountNoList = accountNoList;
	}
	public List<Dictionary> getBalanceAccountAttribute() {
		return balanceAccountAttribute;
	}
	public void setBalanceAccountAttribute(List<Dictionary> balanceAccountAttribute) {
		this.balanceAccountAttribute = balanceAccountAttribute;
	}
	public List<BalanceAccount> getBalanceAccountList() {
		return balanceAccountList;
	}
	public void setBalanceAccountList(List<BalanceAccount> balanceAccountList) {
		this.balanceAccountList = balanceAccountList;
	}
	public List getChannelTypeList() {
		return channelTypeList;
	}
	public void setChannelTypeList(List channelTypeList) {
		this.channelTypeList = channelTypeList;
	}
	public List<Map<String, String>> getReportforms_admin() {
		return reportforms_admin;
	}
	public void setReportforms_admin(List<Map<String, String>> reportforms_admin) {
		this.reportforms_admin = reportforms_admin;
	}
	public List<Map<String, String>> getFirstPage() {
		return firstPage;
	}
	public void setFirstPage(List<Map<String, String>> firstPage) {
		this.firstPage = firstPage;
	}
	public List<UserPermission> getUserPermission() {
		return userPermission;
	}
	public void setUserPermission(List<UserPermission> userPermission) {
		this.userPermission = userPermission;
	}
	public List<Map<String, String>> getAdminPermissionList() {
		return adminPermissionList;
	}
	public void setAdminPermissionList(List<Map<String, String>> adminPermissionList) {
		this.adminPermissionList = adminPermissionList;
	}
	public List<Dictionary> getPermissionTypeList() {
		return permissionTypeList;
	}
	public void setPermissionTypeList(List<Dictionary> permissionTypeList) {
		this.permissionTypeList = permissionTypeList;
	}
	public List<Permission> getPermissions() {
		return permissions;
	}
	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}
	public String getChannelNo() {
		return channelNo;
	}
	public void setChannelNo(String channelNo) {
		this.channelNo = channelNo;
	}
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
    
    
   
	public List<Dictionary> getInvoiceContentList() {
		return invoiceContentList;
	}

	public List<OperateChannelHistory> getChannelHistories() {
		return channelHistories;
	}

	public void setChannelHistories(List<OperateChannelHistory> channelHistories) {
		this.channelHistories = channelHistories;
	}

	public void setInvoiceContentList(List<Dictionary> invoiceContentList) {
		this.invoiceContentList = invoiceContentList;
	}
	public BaseResponse(){}
    
}
