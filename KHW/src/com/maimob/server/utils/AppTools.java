package com.maimob.server.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.maimob.server.db.entity.Admin;
import com.maimob.server.db.entity.Operate_reportform;
import com.maimob.server.db.entity.Operate_reportform_day;
import com.maimob.server.db.entity.Operate_reportform_month;

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
	


    public static int daysBetween(String start ,String end)
    {    
    	long between_days=0;
    	try {
    	 SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
         Date d1=sdf.parse(start);  
         Date d2=sdf.parse(end);  
    	
        Calendar cal = Calendar.getInstance();
        cal.setTime(d1);
        long time1 = cal.getTimeInMillis();
        cal.setTime(d2);
        long time2 = cal.getTimeInMillis();
        between_days=(time2-time1)/(1000*3600*24);
		} catch (Exception e) {
			// TODO: handle exception
		}
       return Integer.parseInt(String.valueOf(between_days))+1;           
    }    
	
	public static List<Operate_reportform_day> changeDay(List<Operate_reportform> l1 ,List<Operate_reportform_day> l2 )
	{
		List<Operate_reportform_day> l3 = new ArrayList<Operate_reportform_day>();
		for(int i = 0;i < l1.size();i++)
		{
			Operate_reportform or = l1.get(i);
			
			Operate_reportform_day ord = new Operate_reportform_day(or);
			l3.add(ord);
			
		}
		l3.addAll(l2);
		return l3;
	}

	public static List<Operate_reportform_month> changeMonth(List<Operate_reportform> l1 ,List<Operate_reportform_month> l2 )
	{
		List<Operate_reportform_month> l3 = new ArrayList<Operate_reportform_month>();
		for(int i = 0;i < l1.size();i++)
		{
			Operate_reportform or = l1.get(i);
			
			Operate_reportform_month ord = new Operate_reportform_month(or);
			l3.add(ord);
			
		}
		l3.addAll(l2);
		return l3;
	}
	
	
	

}
