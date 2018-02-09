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
		for(Operate_reportform_day ord:l3)
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
