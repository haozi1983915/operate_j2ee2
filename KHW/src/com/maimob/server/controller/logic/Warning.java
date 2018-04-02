package com.maimob.server.controller.logic;

import java.util.List;
import java.util.Map;

import com.maimob.server.importData.dao.OperateDao;
import com.maimob.server.utils.Mail;

public class Warning extends Thread{
	
	OperateDao od = new OperateDao();
	public void run() {
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
			if(mistime > 1800000) {
				Mail mail = new Mail();
				String text = "hi,张浩\n\n"+tableName+"表最后一次刷新时间是"+date+",已超过半小时没有刷新，请及时检查";
				mail.sendMailTest(text,arr);
			}
		}
	}
}
