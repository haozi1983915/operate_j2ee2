package com.maimob.server.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.maimob.server.db.entity.Admin;
import com.maimob.server.db.entity.Channel;
import com.maimob.server.db.entity.Dictionary;
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
	public static void updateChannelCatche(Channel channel)
	{
		channelCatche.put(channel.getId(), channel);;
	}

	public static Channel getChannelCatche(long id)
	{
		return channelCatche.get(id);
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
	
	
	
	
	
	
	
	
	

}
