package com.maimob.server.controller.logic;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.maimob.server.importData.dao.ConnectionState;
import com.maimob.server.importData.dao.OperateDao;
import com.maimob.server.utils.Mail;

public class Warning extends Thread{
	
	OperateDao od = new OperateDao();
	public void run() {
		
		Properties properties = new Properties();
		// 使用ClassLoader加载properties配置文件生成对应的输入流
		InputStream in = ConnectionState.class.getClassLoader().getResourceAsStream("config/hibernate/jdbc.properties");
		// 使用properties对象加载输入流
		try {
			properties.load(in);
			//获取key对应的value值
			String isWarning = properties.getProperty("isWarning");
			if(isWarning.equals("0"))
			return;
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		while(true) {
			try {
				sleep(1800000);
				sendWarning();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	//取数据库内各数据表的最新刷新时间和当前时间进行比较，超过半小时发送邮件提醒
	public void sendWarning() {
		String[] arr = new String[] {"hao.zhang@maimob.cn"};
		List<Map<String, String>> ordList = od.findTableDate();
		for (Map<String, String> map : ordList) {
			long mistime = Long.parseLong(map.get("mistime"));
			String date = map.get("recordTime");
			String tableName = map.get("tableName");
			if(mistime > 1800) {
				Mail mail = new Mail();
				String text = "hi,张浩\n\n    "+tableName+"表最后一次刷新时间是"+date+",已超过半小时没有刷新，请及时检查";
				mail.sendMailTest(text,arr);
			}
		}
	}
}
