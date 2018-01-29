package com.maimob.server.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.maimob.server.db.entity.Admin;

public class AppTools {
	
	public static Map<String ,Admin> userMap = new HashMap<String ,Admin>();
	private static long sid = 0;
	public synchronized static long getId()
	{
		long id = System.currentTimeMillis();
		
		while(sid == id)
		{
			id = System.currentTimeMillis();
		}	
		sid = id;
		return id;
	}
	
	
	public static void main(String[] args) {
		
		ArrayList<String> arlList = new ArrayList<String>();
		arlList.add("1111");
		arlList.add("1121");
		arlList.add("1111");
		removeDuplicate(arlList);
		System.out.println(arlList.size());
		
	}
	
	public static void removeDuplicate(ArrayList<String> arlList)      
	{      
		HashSet h = new HashSet(arlList);      
		arlList.clear();      
		arlList.addAll(h);      
	}    
	

	public static String getTime(long time)      
	{      
		Date nowTime=new Date(); 
		if(time > 0)
		{
			nowTime=new Date(time); 
		}

		SimpleDateFormat time1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return time1.format(nowTime);
		
	}    
	
 
	
	
	

}
