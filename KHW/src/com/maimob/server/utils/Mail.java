package com.maimob.server.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

public class Mail {
	
	private FreeMarkerConfigurer freeMarkerConfigurer; 
	private ApplicationContext ctx = null;
	private String rootPath = this.getClass().getClassLoader().getResource("").getPath();
	///Users/Astro/eclipse-workspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/operate/WEB-INF/classes/

	public Mail() {
//		logger.i("path = " + rootPath);
		ctx = new FileSystemXmlApplicationContext("/"+ rootPath +"config/spring/mailConfig.xml");
	}

	public void setFreeMarkerConfigurer(FreeMarkerConfigurer freeMarkerConfigurer) {
		this.freeMarkerConfigurer = freeMarkerConfigurer;
	}


	/**
	 * 发送简单邮件
	 */
	public void sendMail() {
		JavaMailSender sender = (JavaMailSender) ctx.getBean("mailSender");// 获取JavaMailSender
		SimpleMailMessage mail = new SimpleMailMessage();
		try {
			String[] array = new String[] {"zhi.nie@maimob.cn"};
			String from = "管理员<showcocent@163.com>";
			mail.setTo(array);// 接受者
			mail.setFrom(new String(from.getBytes("UTF8"),"ISO-8859-1"));
			mail.setSubject("重置密码");// 主题
			mail.setText("springMail 的简单发送测试");// 邮件内容
			sender.send(mail);
//			Log.i("发送完毕");
			System.out.println("发送完毕");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendMailTest(String text,String[] array) {
		JavaMailSender sender = (JavaMailSender) ctx.getBean("mailSender");// 获取JavaMailSender
		SimpleMailMessage mail = new SimpleMailMessage();
		try {

			String from = "Admin<showcocent@163.com>";
			mail.setTo(array);// 接受者
			mail.setFrom(new String(from.getBytes("UTF8"),"ISO-8859-1"));
			mail.setSubject("重置密码");// 主题
			mail.setText(text);// 邮件内容
			sender.send(mail);

			System.out.println("发送完毕");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 发送HTML邮件
	 */
	public String  send(String[] receivers, String subject, String content, boolean html) {
		JavaMailSender sender = (JavaMailSender) ctx.getBean("mailSender");// 获取JavaMailSender
		String from = "管理员<showcocent@163.com>";
		if(html){
	        //发送HTML格式的邮件         
	        //建立邮件信息，可发送HTML格式  
	        MimeMessage mimeMessage = sender.createMimeMessage(); //MimeMessage-->java的  
	        MimeMessageHelper messageHelper;
			try {
				messageHelper = new MimeMessageHelper(mimeMessage,true,"UTF-8");
		        //设置收件人，主题，内容  
		        messageHelper.setSubject(subject);  
				messageHelper.setFrom(from);
		        messageHelper.setTo(receivers);   
		        messageHelper.setText(content,true); //为true-->发送转义HTML  
		  
		        //senderMail.send(mimeMessage);  //这个是不带附件的   
		          
		        //发送带附件的  
//		        FileSystemResource file = new FileSystemResource(new File("E:\\DevelopmentSoft\\spring-framework-3.0.5.RELEASE\\docs\\javadoc-api\\index.html"));  
//		        messageHelper.addAttachment("index.html", file);  
		          
		        sender.send(mimeMessage); //这个是发送带附件的  
//		        Log.i("发送完毕");
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
//				Log.e("邮件发送失败 e = " + e.toString());
				e.printStackTrace();
			} // MimeMessageHelper-->spring的  不加后面两个参数会乱码  
	  
		}else{
			SimpleMailMessage mail = new SimpleMailMessage();
			try {
				mail.setTo(receivers);// 接受者
				mail.setFrom(from);
				mail.setSubject(subject);// 主题
				mail.setText(content);// 邮件内容
				sender.send(mail);
//				Log.i("发送完毕");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "发送完毕";
	}
	
	/**
	 * 修改密码密码邮件
	 * @param receivers
	 * @param userName
	 * @param validCode
	 * @return
	 */
	public String sendResetPwdMail(String[] receivers, String userName){
		// 通过指定模板名获取FreeMarker模板实例
		FreeMarkerConfigurer freeMarker = (FreeMarkerConfigurer) ctx.getBean("freeMarker");// 获取JavaMailSender
		this.setFreeMarkerConfigurer(freeMarker);
		Template template = null;
		try {
			template = freeMarkerConfigurer.getConfiguration().getTemplate("mail/resetPwdMail.html");
		} catch (TemplateNotFoundException e1) {
			// TODO Auto-generated catch block
//			Log.e("邮件发送失败 e31 = " + e1.toString());
			e1.printStackTrace();
		} catch (MalformedTemplateNameException e1) {
			// TODO Auto-generated catch block
//			Log.e("邮件发送失败 e32 = " + e1.toString());
			e1.printStackTrace();
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
//			Log.e("邮件发送失败 e33 = " + e1.toString());
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
//			Log.e("邮件发送失败 e34 = " + e1.toString());
			e1.printStackTrace();
		} 
		
		// FreeMarker通过Map传递动态数据
		Map<String, String> map = new HashMap<String, String>(); 
		map.put("name", userName); // 注意动态数据的key和模板标签中指定的属性相匹配
		map.put("link", "http://localhost:8080/operate/page/resetPwd.html" );
		map.put("date", DateTimeUtils.getMinuteDateTime());
		map.put("homeLink", "http://localhost:8080/operate/Index");                
		
		// 解析模板并替换动态数据，最终content将替换模板文件中的${content}标签。
		try {
			String htmlText = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
			return send(receivers, "重置密码", htmlText, true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return "发送失败";
	}
	
	/**
	 * 密码修改成功邮件
	 * @param receivers
	 * @param userName
	 * @param validCode
	 * @return
	 */
	public String sendPwdResetSuccessMail(String[] receivers, String userName){
		// 通过指定模板名获取FreeMarker模板实例
		FreeMarkerConfigurer freeMarker = (FreeMarkerConfigurer) ctx.getBean("freeMarker");// 获取JavaMailSender
		this.setFreeMarkerConfigurer(freeMarker);
		Template template = null;
		try {
			template = freeMarkerConfigurer.getConfiguration().getTemplate("mail/pwdResetSucessMail.html");
		} catch (TemplateNotFoundException e1) {
//			Log.e("邮件发送失败 e41 = " + e1.toString());
			e1.printStackTrace();
		} catch (MalformedTemplateNameException e1) {
//			Log.e("邮件发送失败 e41 = " + e1.toString());
			e1.printStackTrace();
		} catch (ParseException e1) {
//			Log.e("邮件发送失败 e41 = " + e1.toString());
			e1.printStackTrace();
		} catch (IOException e1) {
//			Log.e("邮件发送失败 e41 = " + e1.toString());
			e1.printStackTrace();
		} 
		
		// FreeMarker通过Map传递动态数据
		Map<String, String> map = new HashMap<String, String>(); 
		map.put("name", userName); // 注意动态数据的key和模板标签中指定的属性相匹配
		map.put("link", "http://localhost:8080/operate/index");
		map.put("currentTime", DateTimeUtils.getMillisecondDateTime());
		map.put("date", DateTimeUtils.getMinuteDateTime());
//		map.put("homeLink", Constants.getAdminRootUrl());
		
		// 解析模板并替换动态数据，最终content将替换模板文件中的${content}标签。
		try {
			String htmlText = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
			return send(receivers, "密码设置成功", htmlText, true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			Log.e("邮件发送失败 e51 = " + e.toString());
			e.printStackTrace();
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
//			Log.e("邮件发送失败 e51 = " + e.toString());
			e.printStackTrace();
		}
		
//		Log.e("邮件发送失败或超时");
		return "发送失败";
	}
}
