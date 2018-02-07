package com.maimob.server.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;

import com.maimob.server.db.entity.Admin;
import com.maimob.server.db.entity.Channel;
import com.maimob.server.db.entity.Dictionary;
import com.maimob.server.db.entity.Operate_reportform_day;
import com.maimob.server.db.entity.Operate_reportform_month;
import com.maimob.server.db.entity.Proxy;
import com.maimob.server.db.service.DaoService;

public class Cache {
	
	

	private static Map<Long, Admin> AdminCache;
	/**
	 * 数据缓存
	 * @param dao
	 * @param type 0 admin,
	 */
	public synchronized static void AdminCatche(DaoService dao)
	{
		if(AdminCache == null)
		{
			AdminCache = new HashMap<Long, Admin>();
			List<Admin>admins = dao.findAllAdmin();
			for(int i = 0;i < admins.size();i++)
			{
				AdminCache.put(admins.get(i).getId(), admins.get(i));
			}
		}
		
	}
	
	public static void updateAdminCatche(Admin admin)
	{
		AdminCache.put(admin.getId(), admin);
	}

	public static Admin getAdminCatche(long id)
	{
		return AdminCache.get(id);
	}
	
	

	private static Map<Long, Proxy> ProxyCache;
	/**
	 * 数据缓存
	 * @param dao
	 * @param type 0 admin,
	 */
	public synchronized static void ProxyCache(DaoService dao)
	{
		if(ProxyCache == null)
		{
			ProxyCache = new HashMap<Long, Proxy>();
			List<Proxy>proxys = dao.findAllProxy();
			for(int i = 0;i < proxys.size();i++)
			{
				ProxyCache.put(proxys.get(i).getId(), proxys.get(i));
			}
		}
	}
	
	public static void updateProxyCatche(Proxy proxy)
	{
		ProxyCache.put(proxy.getId(), proxy);
	}

	public static Proxy getProxyCatche(Long id)
	{
		return ProxyCache.get(id);
	}
	
	public synchronized static void update(DaoService dao)
	{
		DicCache = null;
		DicTypeCache = null;
		DicCatche(dao);
		AdminCache = null;
		AdminCatche(dao);
		ProxyCache = null;
		ProxyCache(dao);

		channelCatche = null;
		
		channelCatche(dao);
		
		
	}
	

	private static Map<Long, Dictionary> DicCache;
	private static Map<Integer, List<Dictionary>> DicTypeCache;
	
	public synchronized static void DicCatche(DaoService dao)
	{
		if(DicCache == null)
		{
			DicCache = new HashMap<Long, Dictionary>();
			DicTypeCache = new HashMap<Integer, List<Dictionary>>();
			List<Dictionary> diclist = dao.findAllDictionary();
			for(int i = 0;i < diclist.size();i++)
			{
				Dictionary dic = diclist.get(i);
				dic.init();
				DicCache.put(dic.getId(), dic);
				
				if(DicTypeCache.get(dic.getType()) != null)
					DicTypeCache.get(dic.getType()).add(dic);
				else
				{
					DicTypeCache.put(dic.getType(), new ArrayList<Dictionary>());
					DicTypeCache.get(dic.getType()).add(dic);
				}
			}
		}
		
	}

	private static Map<Long, Channel> channelCatche;
	
	public synchronized static void channelCatche(DaoService dao)
	{
		if(channelCatche == null)
		{
			channelCatche = new HashMap<Long, Channel>();
			List<Channel> channellist = dao.findAllChannel("");
			for(int i = 0;i < channellist.size();i++)
			{
				Channel channel = channellist.get(i);
				channelCatche.put(channel.getId(), channel);
				
			}
		}
	}
	

	public synchronized static void channelCatche(DaoService dao,String proxyId)
	{
		if(channelCatche == null)
		{
			channelCatche = new HashMap<Long, Channel>();
			List<Channel> channellist = dao.findAllChannel("");
			for(int i = 0;i < channellist.size();i++)
			{
				Channel channel = channellist.get(i);
				channelCatche.put(channel.getId(), channel);
				
			}
		}
		else
		{
			List<Channel> channellist = dao.findAllChannel(" proxyId = "+proxyId);
			for(int i = 0;i < channellist.size();i++)
			{
				Channel channel = channellist.get(i);
				channelCatche.put(channel.getId(), channel);
			}
		}
	}
	
	public static void updateChannelCatche(Channel channel)
	{
		channelCatche.put(channel.getId(), channel);;
	}

	public static Channel getChannelCatche(long id)
	{
		return channelCatche.get(id);
	}

	public static void updateChannelStuts(long id,int status)
	{
		Channel channel = channelCatche.get(id);
		channel.setStatus(status);
	}

	public static void updateChannelSynchronous(long id,int synchronous)
	{
		Channel channel = channelCatche.get(id);
		channel.setSynchronous(synchronous);;
	}
	public static void updateOptimization(long id,long optimizationId)
	{
		Channel channel = channelCatche.get(id);
		channel.setOptimizationId(optimizationId);
	}
	public static void updateOptimization_startDate(long id,int optimization,long startDate)
	{
		Channel channel = channelCatche.get(id);
		channel.setOptimization(optimization);
		channel.setStartDate(startDate);
	}
	

	public static void updateChannelName(long id,String channelName)
	{
		Channel channel = channelCatche.get(id);
		channel.setChannelName(channelName);
	}
 


    private long attribute;

    @Column(name="type")
    //渠道类别
    private long type;

    @Column(name="subdivision")
	public static void updateChannelType(long id,long attribute,long type,long subdivision)
	{
		Channel channel = channelCatche.get(id);
		channel.setAttribute(attribute);
		channel.setType(type);
		channel.setSubdivision(subdivision);
	}
	
	
	
	public static Dictionary getDic(long dicid)
	{
		Dictionary dic = null;
		if(DicCache != null)
		{
			dic = DicCache.get(dicid);
		}
		
		return dic;
	}
	
	

	public static List<Dictionary> getDicList(int type)
	{
		List<Dictionary> diclist = null;
		if(DicTypeCache != null)
		{
			diclist = DicTypeCache.get(type);
		}
		return diclist;
	}
	
	

	public static void updateDicList(Dictionary dic)
	{
		if(DicTypeCache != null)
		{
			dic.init();
			DicCache.put(dic.getId(), dic);
			if(DicTypeCache.get(dic.getType()) != null)
			{
				 List<Dictionary> dics = DicTypeCache.get(dic.getType());
				boolean isadd = true;
				for(int i = 0;i < dics.size();i++)
				{
					if(dics.get(i).getId() == dic.getId())
					{
						dics.set(i, dic);
						isadd = false;
						break;
					}
					
				}
				if(isadd)
					dics.add(dic);
				
			}
			else
			{
				DicTypeCache.put(dic.getType(), new ArrayList<Dictionary>());
				DicTypeCache.get(dic.getType()).add(dic);
			}
		}
		
	}
	

	private static Map<Long, UserCache> userCache = new HashMap<Long, UserCache>();
	
	public static  void setChannelids(long id,List<Long> channelids)
	{
		get(id).channelids = channelids;
	}

	public static  void setOperate_reportform_day(long id,Operate_reportform_day od)
	{
		get(id).od = od;
	}

	public static  void setOperate_reportform_month(long id,Operate_reportform_month om)
	{
		get(id).om = om;
	}
	

	public static  List<Long> getChannelids(long id)
	{
		return get(id).channelids;
	}

	public static  Operate_reportform_day getOperate_reportform_day(long id)
	{
		return get(id).od;
	}

	public static  Operate_reportform_month getOperate_reportform_month(long id)
	{
		return get(id).om;
	}

	

	private static  UserCache get(long id)
	{
		UserCache uc = userCache.get(id);
		if(uc == null)
		{
			uc = new UserCache();
			userCache.put(uc.id, uc);
		}
		return uc;
	}
	

	public void setUserCache(UserCache uc)
	{
		userCache.put(uc.id, uc);
	}

	
	
	public UserCache  getUserCache(long id)
	{
		return userCache.get(id);
	}
	

}
