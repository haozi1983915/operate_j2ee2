package com.maimob.server.controller.logic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class log {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		log.saveAsFileWriter("34r");
	}

	private static String savefile = "C:/workspace/";
//	private static String savefile = "/Users/zhanghao/Downloads/电话注册/";
	
	public static String saveAsFileWriter(String content) {
		
		FileWriter fwriter = null;
		try {
			File f = new File(savefile);
			if(!f.exists())
				f.mkdirs();
			String path = savefile+"aa.txt";
			f = new File(path);
			if(!f.exists())
				f.createNewFile();
			fwriter = new FileWriter(path);
			fwriter.write(content);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				fwriter.flush();
				fwriter.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return "ok";
	}

}
