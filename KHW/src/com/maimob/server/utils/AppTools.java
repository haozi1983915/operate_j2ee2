package com.maimob.server.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.maimob.server.data.task.TaskLine;
import com.maimob.server.db.entity.Admin;
import com.maimob.server.db.entity.Operate_reportform;
import com.maimob.server.db.entity.Operate_reportform_day;
import com.maimob.server.db.entity.Operate_reportform_month;

public class AppTools {
	
	public static boolean istest = true; 
	
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
	
	public static TaskLine taskLine = new TaskLine();
	
	
	public static void main(String[] args) {
		
		ArrayList<String> arlList = new ArrayList<String>();
		arlList.add("1111");
		arlList.add("1121");
		arlList.add("1111");
		removeDuplicate(arlList);
		System.out.println(arlList.size());
		
	}
	
	public static boolean isProxy(String no)
	{
		boolean isProxy = true;
		try {
			long no1 = Long.parseLong(no);
		} catch (Exception e) {
			isProxy = false;
		}
		
		return isProxy;
	}

	public static String next(String queryTime) {
		try {

			long time = stringToLong(queryTime, "yyyy-MM-dd");
			 
			time += 3600000l * 24l;

			queryTime = longToString(time, "yyyy-MM-dd");

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return queryTime;
	}

	public static long stringToLong(String strTime, String formatType)   {
		try {
			Date date = stringToDate(strTime, formatType); // String类型转成date类型
			if (date == null) {
				return 0;
			} else {
				long currentTime = dateToLong(date); // date类型转成long类型
				return currentTime;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public static String dateToString(Date data, String formatType) {
		return new SimpleDateFormat(formatType).format(data);
	}

	public static long dateToLong(Date date) {
		return date.getTime();
	}

	// long类型转换为String类型
	// currentTime要转换的long类型的时间
	// formatType要转换的string类型的时间格式
	public static String longToString(long currentTime, String formatType) throws ParseException {
		Date date = longToDate(currentTime, formatType); // long类型转成Date类型
		String strTime = dateToString(date, formatType); // date类型转成String
		return strTime;
	}
	public static Date longToDate(long currentTime, String formatType) throws ParseException {
		Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
		String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
		Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
		return date;
	}

	public static Date stringToDate(String strTime, String formatType) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(formatType);
		Date date = null;
		date = formatter.parse(strTime);
		return date;
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
    	String gs = "yyyy-MM-dd";
    	if(start.length()==7)
    	{
    		gs = "yyyy-MM";
    	}
    		
    	 SimpleDateFormat sdf=new SimpleDateFormat(gs);
         Date d1=sdf.parse(start);  
         Date d2=sdf.parse(end);  
    	
        Calendar cal = Calendar.getInstance();
        cal.setTime(d1);
        long time1 = cal.getTimeInMillis();
        cal.setTime(d2);
        long time2 = cal.getTimeInMillis();
        between_days=(time2-time1)/(1000*3600*24);
        
        if(gs.equals("yyyy-MM"))
        {
            int maxDate = getCurrentMonthLastDay(end);
            between_days += maxDate;
        }
        else
        {
        	between_days+=1;
        }
        
		} catch (Exception e) {
			// TODO: handle exception
		}
       return (int)between_days;           
    }    
    /** 
     * 取得当月天数 
     * */  
    public static int getCurrentMonthLastDay(String date)  
    {  
        
//        String[] ds = date.split("-");
//        
//        Calendar c = Calendar.getInstance();  
//        c.set(Calendar.YEAR, Integer.parseInt(ds[0])); // 2010年  
//        c.set(Calendar.MONTH, Integer.parseInt(ds[1])-1); // 6 月  
//        System.out.println("------------" + c.get(Calendar.YEAR) + "年" + (c.get(Calendar.MONTH) + 1) + "月的天数和周数-------------");  
//        System.out.println("天数：" + c.getActualMaximum(Calendar.DAY_OF_MONTH));  
//        System.out.println("周数：" + c.getActualMaximum(Calendar.WEEK_OF_MONTH));  
//        int maxDate = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");  
        
        Calendar calendar = Calendar.getInstance();  
        try {
			calendar.setTime(sdf.parse(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        int maxDate = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);  
        
        
        return maxDate;  
    }  
    
    /**
     * 
     * @param dateType 1 昨天，2今天，3本周，4本月，5上月
     * @return
     */
    public static String[] getPrevious(String dateType)
    {
    		String formatType = "yyyy-MM-dd";
		String[] dates = null; 
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(formatType);
			String today = sdf.format(new Date());

			long time = dateToLong(new Date());
			
			switch (dateType) {
			case "1":
				dates = new String[2]; 
				time -= 3600000l*24;
				String yesterday = longToString(time, formatType);
				dates[0] = yesterday;
				dates[1] = yesterday;
				break;

			case "2":
				dates = new String[2]; 
				dates[0] = today;
				dates[1] = today;
				break;
				
			case "3":

				today = longToString(time, formatType);
				dates = new String[2]; 
				Calendar cal = Calendar.getInstance();
			    cal.setTime(new Date());
			    int w = cal.get(Calendar.DAY_OF_WEEK);
			    
			    int c = w-2;
			    if(w==1)
			    		c = 6;

				time -= 3600000l*24*c;
				
				String Monday = longToString(time, formatType);
				dates[0] = Monday;
				dates[1] = today;
				break;

			case "4":
				today = longToString(time, formatType);
				dates = new String[2]; 
				
				sdf = new SimpleDateFormat("dd");
				String day = sdf.format(new Date());
				c = Integer.parseInt(day)-1;
				
				time -= 3600000l*24*c;
				
				String one = longToString(time, formatType);
				dates[0] = one;
				dates[1] = today;
				break;

			case "5":
				today = longToString(time, formatType);
				dates = new String[2]; 
				
				sdf = new SimpleDateFormat("MM");
				String MM = sdf.format(new Date());
				int m = Integer.parseInt(MM)-1;
				if(m==0)
					m = 12;

				sdf = new SimpleDateFormat("yyyy");
				String y = sdf.format(new Date());
				
				dates[0] = y+"-"+m+"-01";
				dates[1] = y+"-"+m+"-31";;
				break;
			default:
				break;
			}
			

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return dates;
	}
    
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;

        return weekDays[w];
    }
    
	
//	public static List<Operate_reportform_day> changeDay(List<Operate_reportform> l1 ,List<Operate_reportform_day> l2 )
//	{
//		List<Operate_reportform_day> l3 = new ArrayList<Operate_reportform_day>();
//		for(int i = 0;i < l1.size();i++)
//		{
//			Operate_reportform or = l1.get(i);
//			
//			Operate_reportform_day ord = new Operate_reportform_day(or);
//			l3.add(ord);
//			
//		}
//		l3.addAll(l2);
//		for(Operate_reportform_day ord:l3)
//		{
//			double register = ord.getRegister();
//			double upload = ord.getUpload();
//			double account = ord.getAccount();
//			double loan = ord.getLoan();
//			
//			if(register == 0)
//				register = 1;
//			if(upload == 0)
//				upload = 1;
//			if(account == 0)
//				account = 1;
//			if(loan == 0)
//				loan = 1;
//			
//			String uploadC = ((upload/register)*100)+"";
//			
//			if(uploadC.contains("."))
//				uploadC = uploadC.substring(0, uploadC.indexOf("."));
//
//			String accountC = ((account/upload)*100)+"";
//			
//			if(accountC.contains("."))
//				accountC = accountC.substring(0, accountC.indexOf("."));
//
//			String loanC = ((loan/account)*100)+"";
//			
//			if(loanC.contains("."))
//				loanC = loanC.substring(0, loanC.indexOf("."));
//			
//			ord.setUploadConversion(uploadC);
//			ord.setAccountConversion(accountC);
//			ord.setLoanConversion(loanC);
//
//			
//		}
//		return l3;
//	}

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
		for(Operate_reportform_month ord:l3)
		{
			double register = ord.getRegister();
			double upload = ord.getUpload();
			double account = ord.getAccount();
			double loan = ord.getLoan();
			
			if(register == 0)
				register = 1;
			if(upload == 0)
				upload = 1;
			if(account == 0)
				account = 1;
			if(loan == 0)
				loan = 1;
			
			String uploadC = ((upload/register)*100)+"";
			
			if(uploadC.contains("."))
				uploadC = uploadC.substring(0, uploadC.indexOf("."));

			String accountC = ((account/upload)*100)+"";
			
			if(accountC.contains("."))
				accountC = accountC.substring(0, accountC.indexOf("."));

			String loanC = ((loan/account)*100)+"";
			
			if(loanC.contains("."))
				loanC = loanC.substring(0, loanC.indexOf("."));
			
			ord.setUploadConversion(uploadC);
			ord.setAccountConversion(accountC);
			ord.setLoanConversion(loanC);

			
		}
		return l3;
	}
	
	
	

}
