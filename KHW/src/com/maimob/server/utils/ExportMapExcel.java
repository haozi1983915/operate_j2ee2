package com.maimob.server.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;


public class ExportMapExcel {

	public void exportExcel(String title, List<String> headersName, List<String> headersId,
            List<Map<String, Object>> dtoList,String path) {
			/*
			（一）表头--标题栏
			*/
			Map<Integer, String> headersNameMap = new HashMap<>();
			int key = 0;
			for (int i = 0; i < headersName.size(); i++) {
				if (!headersName.get(i).equals(null)) {
					headersNameMap.put(key, headersName.get(i));
					key++;
				}
			}
			/*
			（二）字段---标题的字段
			*/
			Map<Integer, String> titleFieldMap = new HashMap<>();
			int value = 0;
			for (int i = 0; i < headersId.size(); i++) {
				if (!headersId.get(i).equals(null)) {
					titleFieldMap.put(value, headersId.get(i));
					value++;
				}
			}

	/*
    （三）声明一个工作薄：包括构建工作簿、表格、样式
*/
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet(title);
			sheet.setDefaultColumnWidth((short) 15);
			// 生成一个样式
			HSSFCellStyle style = wb.createCellStyle();
			HSSFRow row = sheet.createRow(0);
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			HSSFCell cell;
			Collection c = headersNameMap.values();//拿到表格所有标题的value的集合
			Iterator<String> headersNameIt = c.iterator();//表格标题的迭代器
			/*
    （四）导出数据：包括导出标题栏以及内容栏
			 */
			//根据选择的字段生成表头--标题
			short size = 0;
			while (headersNameIt.hasNext()) {
				cell = row.createCell(size);
				cell.setCellValue(headersNameIt.next().toString());
				cell.setCellStyle(style);
				size++;
			}
			//表格一行的字段的集合，以便拿到迭代器
	        Collection zdC = titleFieldMap.values();
	        Iterator<Map<String, Object>> titleFieldIt = dtoList.iterator();//总记录的迭代器
	        int zdRow = 1;//真正的数据记录的列序号
	        while (titleFieldIt.hasNext()) {//记录的迭代器，遍历总记录
	            Map<String, Object> mapTemp = titleFieldIt.next();//拿到一条记录
	                row = sheet.createRow(zdRow);
	                zdRow++;
	                int zdCell = 0;
	                Iterator<String> zdIt = zdC.iterator();//一条记录的字段的集合的迭代器
	                while (zdIt.hasNext()) {
	                    String tempField =zdIt.next();//字段的暂存
	                    if (mapTemp.get(tempField) != null) {
	                        row.createCell((short) zdCell).setCellValue(String.valueOf(mapTemp.get(tempField)));//写进excel对象
	                        zdCell++;
	                    }
	                }
	        }
	        try {
	            FileOutputStream exportXls = new FileOutputStream(path);
//	            getServletContext().getRealPath("/");
//	         	OutputStream exportXls = response.getOutputStream();  
//	            response.setContentType("application/ms-excel;charset=UTF-8");  
//	            response.setHeader("Content-Disposition", "attachment;filename="  
//	                    .concat(String.valueOf(URLEncoder.encode(fileName + ".xls", "UTF-8")))); 
	            wb.write(exportXls);
	            exportXls.close();
	            System.out.println("导出成功!");
	        } catch (FileNotFoundException e) {
	            System.out.println("导出失败!");
	            e.printStackTrace();
	        } catch (IOException e) {
	            System.out.println("导出失败!");
	            e.printStackTrace();
	        }
	}
}
