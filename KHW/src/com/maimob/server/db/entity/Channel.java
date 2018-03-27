package com.maimob.server.db.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.maimob.server.utils.AppTools;
import com.maimob.server.utils.Cache;
import com.maimob.server.utils.StringUtils;

@Entity
@Table(name="operate_channel")
@DynamicUpdate(true)
@DynamicInsert(true)
public class Channel implements Serializable{

	public Channel() {
		// TODO Auto-generated constructor stub
	}

	public Channel(long id) {
		this.id = id;
	}
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Id @Column(name="id", nullable=false)
    //管理员ID
    private long id;
    
    @Column(name="proxyId")
    //渠道商id
    private long proxyId;
    
    @Column(name="channelName")
    //渠道名称
    private String channelName;

    @Column(name="pwd")
    //渠道密码
    private String pwd;
    
    @Column(name="channelNamePY")
    //渠道名称拼音
    private String channelNamePY;
    
	@Column(name="channel")
    //渠道编码
    private String channel;
    
    @Column(name="channelNo")
    //渠道号
    private String channelNo;
    
    @Column(name="level")
    //渠道级别
    private int level;
    
    @Column(name="higherChannelId")
    //上级渠道id
    private long higherChannelId;
    
    @Column(name="attribute")
    //渠道属性
    private long attribute;

    @Column(name="type")
    //渠道类别
    private long type;

    @Column(name="subdivision")
    //渠道细分
    private long subdivision;

    @Column(name="url")
    //推广链接
    private String url;
    
    @Column(name="rewardId")
    //分成方案id
    private long rewardId;

    @Column(name="adminId")
    //负责人id
    private long adminId;
    
    @Column(name="permissionId")
    //负责人id
    private long permissionId;

    @Column(name="appId")
    //appid
    private long appId;

    @Column(name="isCreate")
    //是否可创建子渠道
    private long isCreate;
    

    @Column(name="rewardTypeId")
    //appid
    private long rewardTypeId;

    @Column(name="rewardPrice")
    //是否可创建子渠道
    private String rewardPrice;
    
    @Column(name="status")
    //是否可创建子渠道
    private long status;
    
    

    @Column(name="optimizationId")
    //优化方案id
    private long optimizationId;
    
    @Column(name="synchronous")
    //是否同步0是1不是
    private int synchronous;
    
    @Column(name="optimization")
    //优化比例
    private int optimization;
    
    @Column(name="startDate")
    //优化开始时间
    private long startDate;
    
	//渠道权限
    @Transient
    private String adminName;
    
    @Transient
	private String rewardTypeName; 

    @Transient
	private boolean New; 

    //登录时间
    @Transient
    private long loginDate;
//一级渠道分成方式
    @Transient
	Map<String,List<Reward>> mainReward;
	
	
	
	
	
	public Map<String, List<Reward>> getMainReward() {
		return mainReward;
	}

	public void setMainReward(Map<String, List<Reward>> mainReward) {
		this.mainReward = mainReward;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public long getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(long loginDate) {
		this.loginDate = loginDate;
	}

	public int getOptimization() {
		return optimization;
	}

	public void setOptimization(int optimization) {
		this.optimization = optimization;
	}

      
	public long getStartDate() {
		return startDate;
	}

	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}

	public boolean isNew() {
		return New;
	}

	public void setNew(boolean new1) {
		New = new1;
	}

	public long getStatus() {
		return status;
	}

	public void setStatus(long status) {
		this.status = status;
	}

	public long getOptimizationId() {
		return optimizationId;
	}

	public void setOptimizationId(long optimizationId) {
		this.optimizationId = optimizationId;
	}

	public int getSynchronous() {
		return synchronous;
	}

	public void setSynchronous(int synchronous) {
		this.synchronous = synchronous;
	}

	public String getAdminName() {
    	
    	if(adminName == null)
    	{
    		this.adminId = adminId;
    		if(Cache.getAdminCatche(adminId) != null)
    		this.adminName = Cache.getAdminCatche(adminId).getName();
    		else
    			this.adminName = "未知";
    	}
    	
		return adminName;
	}


	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}


	public String getRewardTypeName() {
		return rewardTypeName;
	}


	public void setRewardTypeName(String rewardTypeName) {
		this.rewardTypeName = rewardTypeName;
	}


	public long getRewardTypeId() {
		return rewardTypeId;
	}


	public void setRewardTypeId(long rewardTypeId) {
		this.rewardTypeId = rewardTypeId;
	}


	public String getRewardPrice() {
		return rewardPrice;
	}


	public void setRewardPrice(String rewardPrice) {
		this.rewardPrice = rewardPrice;
	}


	public String getChannelNamePY() {
		return channelNamePY;
	}


	public void setChannelNamePY(String channelNamePY) {
		this.channelNamePY = channelNamePY;
	}


	public long getAppId() {
		return appId;
	}


	public void setAppId(long appId) {
		this.appId = appId;
	}


	public long getIsCreate() {
		return isCreate;
	}


	public void setIsCreate(long isCreate) {
		this.isCreate = isCreate;
	}


    @Transient
	private List<Reward> rewards; 


	public List<Reward> getRewards() {
		return rewards;
	}


	public void setRewards(List<Reward> rewards) {
		this.rewards = rewards;
		if(rewards != null && rewards.size() > 0 )
		{
			if(rewards.get(0).getId() != 0)
				this.rewardId = rewards.get(0).getId();

			if(rewards.get(0).getAdminId() != 0)
			{
				this.adminId = rewards.get(0).getAdminId();
			}
		}
	}



    public String checkRewards()
    {
    	boolean newReward = false;
    	if(this.rewardId == 0)
    	{
    		this.rewardId = AppTools.getId();
    		newReward = true;
    	}
    	
    	for(int i = 0;i < rewards.size();i++)
    	{
    		Reward reward = rewards.get(i);
    		reward.setNew(newReward);
    		String msg = "";
    		if(this.getAttribute() == 8)
        	msg = reward.check2(this.rewardId,this.id);
    		else
    		msg = reward.check(this.rewardId,this.id);
    		
    		
    		if(!msg.equals(""))
    			return msg;
    	}

    	if(rewards.size() == 0)
			return "没有渠道分成方式";
    	
    	
		return "";
    }
    
    
    public String check()
    {
    	this.getId();
    	if(this.level == 2)
    	{
        	String msg = checkRewards();
        	
        	if(!StringUtils.isStrEmpty(msg))
        	{
        		return msg;
        	}

        	Reward reward = rewards.get(0);
        	
        	this.rewardTypeId = reward.getTypeId();
        	this.rewardPrice = "";
        	if(rewards.size() == 1)
        	{
        		if(rewardTypeId == 28)
        		{
        			this.rewardPrice = reward.getPrice()+"%";
        		}
        		else
        		{
        			this.rewardPrice = reward.getPrice()+"元";
        		}
        	}
        	else
        	{
        		for(int i = 0;i < rewards.size();i++)
        		{
        			Reward reward1 = rewards.get(i);

        			if(i > 0)
        				this.rewardPrice += ",";
        			
            		if(rewardTypeId == 28)
            		{
            			this.rewardPrice += reward1.getMax()+"/"+reward1.getPrice()+"%";
            		}
            		else
            		{
            			this.rewardPrice += reward1.getMax()+"/"+reward1.getPrice()+"元";
            		}
        		}
        		
        	}
        	
        	
    	}
    	
    	
    	
    	
    	
    	if(this.proxyId == 0)
    	{
    		return "渠道商不能为空";
    	}
    	else if(this.channelName == null || "".equals(this.channelName))
    	{
    		return "渠道名称不能为空";
    	}
    	else if(this.channel == null || "".equals(this.channel))
    	{
    		return "渠道编码不能为空";
    	}
    	else if(this.channelNo == null || "".equals(this.channelNo))
    	{
    		return "渠道号不能为空";
    	}
    	else if(this.level == 0)
    	{
    		return "渠道级别不能为空";
    	}
//    	else if(this.level == 2 || higherChannelId == 0)
//    	{
//    		return "上级渠道不能为空";
//    	}
    	else if(this.attribute == 0)
    	{
    		return "渠道属性不能为空";
    	}
    	else if(this.type == 0)
    	{
    		return "渠道类别不能为空";
    	}
    	else if(this.subdivision == 0)
    	{
    		return "渠道细分不能为空";
    	}
    	else if(this.url == null || "".equals(this.url))
    	{
    		return "推广链接不能为空";
    	}
    	else if(this.level == 2 && this.rewardId == 0)
    	{
    		return "分成方案不能为空";
    	}
    	
    	
    	
    	
    	return "";
    }
    
     public String check2()
     {

     	String msg = "";
    	 
     	if(this.proxyId == 0)
     	{
     		return "渠道商不能为空";
     	}
     	else if(this.channelName == null || "".equals(this.channelName))
     	{
     		return "渠道名称不能为空";
     	}
     	else if(this.channel == null || "".equals(this.channel))
     	{
     		return "渠道编码不能为空";
     	}
     	else if(this.channelNo == null || "".equals(this.channelNo))
     	{
     		return "渠道号不能为空";
     	}
     	else if(this.level == 0)
     	{
     		return "渠道级别不能为空";
     	}
     	
     	return msg;
    	 
     }


	public long getPermissionId() {
		return permissionId;
	}


	public void setPermissionId(long permissionId) {
		this.permissionId = permissionId;
	}


	public long getAdminId() {
		return adminId;
	}

	public void setAdminId(long adminId) {
		this.adminId = adminId;
		this.adminName = Cache.getAdminCatche(adminId).getName();
	}


	public long getId() {
		if(id == 0)
		{
			this.New = true;
			id = AppTools.getId();
		}
		return id;
	}


	public long getProxyId() {
		return proxyId;
	}


	public void setProxyId(long proxyId) {
		this.proxyId = proxyId;
	}


	public String getChannelName() {
		return channelName;
	}


	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}


	public String getChannel() {
		return channel;
	}


	public void setChannel(String channel) {
		this.channel = channel;
	}


	public String getChannelNo() {
		return channelNo;
	}


	public void setChannelNo(String channelNo) {
		this.channelNo = channelNo;
	}


	public int getLevel() {
		return level;
	}


	public void setLevel(int level) {
		this.level = level;
	}


	public long getHigherChannelId() {
		return higherChannelId;
	}


	public void setHigherChannelId(long higherChannelId) {
		this.higherChannelId = higherChannelId;
	}


	public long getAttribute() {
		return attribute;
	}


	public void setAttribute(long attribute) {
		this.attribute = attribute;
	}


	public long getType() {
		return type;
	}


	public void setType(long type) {
		this.type = type;
	}


	public long getSubdivision() {
		return subdivision;
	}


	public void setSubdivision(long subdivision) {
		this.subdivision = subdivision;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public long getRewardId() {
		return rewardId;
	}


	public void setRewardId(long rewardId) {
		this.rewardId = rewardId;
	}


	public void setId(long id) {
		this.id = id;
	}

 

}
