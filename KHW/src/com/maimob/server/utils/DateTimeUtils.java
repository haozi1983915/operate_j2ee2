package com.maimob.server.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtils {

    /**
     * 获取当前时间
     * @return yyyyMMddHHmmss
     */
    public static String getCurrentDateTime(){
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        return df.format(new Date());
    }
    
    /**
     * 获取当前时间
     * @return yyyyMMddHHmmss
     */
    public static String getCurrentDateTime(long time){
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        return df.format(time);
    }
    
    /**
     * 获取当前精确到毫秒时间
     * @return yyyyMMddHHmmssSSS
     */
    public static String getMillisecondDateTime(){
        return new SimpleDateFormat("yyyyMMddHHmmssSSS") .format(new Date() );
    }
    
    /**
     * 获取当前时间
     * @return yyyyMMddHHmm
     */
    public static String getMinuteDateTime(){
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");//设置日期格式
        return df.format(new Date());
    }
    
    /**
     * 获取当前时间
     * @return yyyyMMddHHmm
     */
    public static String getDayDateTime(){
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//设置日期格式
        return df.format(new Date());
    }
    /**
     * 判断是否是同一天
     * @param time
     * @return
     */
    public static boolean isToDay(String time){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        SimpleDateFormat dayformat = new SimpleDateFormat("yyyyMMdd");//设置日期格式
        Date date;
        try {
            date = df.parse(time);
            String dayStr = dayformat.format(date);
            System.out.print("time ="+time+" dayStr ="+dayStr);
            return dayStr.equals(dayformat.format(new Date()));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
    
    public static String getDayFormat(String time){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        SimpleDateFormat dayformat = new SimpleDateFormat("yyyyMMdd");//设置日期格式
        
        Date date;
        try {
            date = df.parse(time);
            return dayformat.format(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static String formatToDbTime(String time){
        SimpleDateFormat dbdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//DB日期格式
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");//Calllog日期格式
        Date date;
        try {
            date = format.parse(time);
            return dbdf.format(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    
    public static String getTimeFormat(String time){
        SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss");//设置日期格式
        SimpleDateFormat timeformat = new SimpleDateFormat("HHmmss");//设置日期格式
        
        Date date;
        try {
            date = tf.parse(time);
            return timeformat.format(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    
    @SuppressWarnings("static-access")
    public static String getBeforeDayDateTime(int year ,int month ,int day,SimpleDateFormat sdf){
        Date dNow = new Date();   //当前时间
        Date dBefore = new Date();
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(dNow);//把当前时间赋给日历
        if(year != 0){
            calendar.add(calendar.YEAR, year);
        }
        if(month != 0){
            calendar.add(calendar.MONTH, month);  //设置为month月
        }
        if(day != 0){
            calendar.add(calendar.DAY_OF_MONTH, day);
        }
        dBefore = calendar.getTime();   //得到前3月的时间

        return sdf.format(dBefore);    //格式化前3月的时间
    }
    
    public static boolean hasOverDue(String time){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        Date date = null;
        try {
            date = df.parse(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        if(date!=null){
            return date.getTime() < getValidityTime();
        }else{
            return false;
        }
    }
    
    @SuppressWarnings("static-access")
    public static long getValidityTime(){
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(new Date());//把当前时间赋给日历
        calendar.add(calendar.MINUTE ,-30);
        return calendar.getTimeInMillis();
    }
    
	//获取7天前的日期
    /**
     * 
     * @param inputDate    日期
     * @param num          往前几天
     * @return             输入的日期往前几天的日期
     * @throws ParseException
     */
	public static String getDateBeforeOneWeek(String inputDate,int num) throws ParseException {
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date endDate =  dateFormat.parse(inputDate);
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(endDate);
		int inputDayOfYear = cal.get(Calendar.DAY_OF_YEAR);
		cal.set(Calendar.DAY_OF_YEAR , inputDayOfYear - num );
		return dateFormat.format(cal.getTime());
	
	}
	
	//获取本周一的日期
	/**
	 * 
	 * @param inputDate   日期
	 * @return            输入的日期所在周一的日期
	 * @throws ParseException
	 */
	public static String getThisWeekMonday(String inputDate) throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date endDate =  dateFormat.parse(inputDate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(endDate);
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);  
        if (1 == dayWeek) {  
            cal.add(Calendar.DAY_OF_MONTH, -1);  
        }  
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一  
        cal.setFirstDayOfWeek(Calendar.MONDAY);  
        // 获得当前日期是一个星期的第几天  
        int day = cal.get(Calendar.DAY_OF_WEEK);  
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值  
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);  
        return dateFormat.format(cal.getTime());  
	}
	
	//获取一定时间范围内的年月
	/**
	 * 
	 * @param inputDate  输入的日期
	 * @param num        规定日期往前推几个月
	 * @return           输入的日期的前几个月的年月    
	 * @throws ParseException
	 */
	public static String getYearMonth(String inputDate,int num) throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date endDate =  dateFormat.parse(inputDate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(endDate);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH )+1;
		if(num > 12) {
			year -= num / 12;
			num %= 12;
			if(month - num <= 0) {
				month = month - num + 12;
				year -= 1;
			}else {
				month -= num;
			}
		}else if(month - num <= 0) {
			month = month - num + 12;
			year -= 1;
			}else {
				month -= num;
			}
		if(month < 10) {
			return year + "-0" + month;
		}
		return year + "-" + month;
	}

    
}
