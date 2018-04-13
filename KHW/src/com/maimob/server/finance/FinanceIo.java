package com.maimob.server.finance;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.maimob.server.db.entity.Channel;

public class FinanceIo extends Thread {

	public void upload()
	{
		
		
		
		
		
	}
	
	

	String sign = "7bdfb4f2397b762f89ce048c2a7fd5d9";
	String testsign = "7c92935cdb88c36d4a73aec1d34d4bfd";
	String line_id = "3";
//	sign	字符串	每个接口必传参数，用于校验用户身份
//	name	字符串	开收票公司名称
//	line_id	整数	api/get_line_id接口获取的line_id
	
	String MainUrl = "http://fms.maimob.net";
//	String testMainUrl = "http://fmstest.maimob.net";
	String u_invice = MainUrl+"/api/get_invoice_title_id";

	String u_customer = MainUrl+"/api/get_customer_id";
	String u_supplier = MainUrl+"/api/get_supplier_id";
	String u_income = MainUrl+"/api/set_income_info";
	String u_cost = MainUrl+"/api/set_cost_info";
	public WebResult set_income_info(String invoice_title_id,String customer_id,String service_name,String belong_period,String money)
	{
		String incomeurl = u_income+"?sign="+sign+"&invoice_title_id="+invoice_title_id+"&customer_id="+customer_id+"&service_name="+service_name+"&belong_period="+belong_period+"&money="+money+"&line_id="+line_id;
		String result = sendGet(incomeurl);
		WebResult wr = null;
		try {
			wr = JSONObject.parseObject(result, WebResult.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wr;
	}
	
	public String getIdUrl(String name)
	{
		String Idurl = "?sign="+sign+"&name="+name+"&line_id="+line_id;
		return Idurl;
	}

	

	public WebResult set_cost_info(String invoice_title_id,String supplier_id,String service_name,String belong_period,String money,String type)
	{
		String costurl = u_cost+"?sign="+sign+"&invoice_title_id="+invoice_title_id+"&supplier_id="+supplier_id+"&service_name="+service_name+"&belong_period="+belong_period+"&money="+money+"&line_id="+line_id+"&type="+type;
		String result = sendGet(costurl);
		WebResult wr = JSONObject.parseObject(result, WebResult.class);
		return wr;
	}
	
//	sign	字符串	每个接口必传参数，用于校验用户身份
//	line_id	整数	api/get_line_id接口获取的line_id
//	supplier_id
//	name	字符串	供应商公司全称
//	duty_paragraph	字符串	税号
//	phone	字符串	联系电话
//	addr	字符串	地址
//	open_bank	字符串	开户银行
//	bank_account	字符串	银行账号
//	remark	字符串	备注
//	status	整数	1-可用，0-禁用
	

	String u_set_supplier = MainUrl+"/api/set_supplier";
	public String set_supplier(List<Map<String,String>> suppliers)
	{
		String supplier_id = "";
		if(suppliers != null && suppliers.size() > 0)
		{
			Map<String,String> sup = suppliers.get(0);
			String proxyId = sup.get("proxyId");
			String name = sup.get("company");
			String duty_paragraph = sup.get("dutyParagraph");
			String phone = sup.get("phone");
			String open_bank = sup.get("bank");
			String addr = sup.get("address");
			String bank_account = sup.get("bankAccount");
			String remark = "";
			String status = "1";
			supplier_id = sup.get("supplier_id");
			if(supplier_id == null || supplier_id.equals(""))
				supplier_id = "0";

			try {
				name = java.net.URLEncoder.encode(name, "utf-8");
				open_bank = java.net.URLEncoder.encode(open_bank, "utf-8");
				addr = java.net.URLEncoder.encode(addr, "utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String ssurl = u_set_supplier+"?sign="+sign+"&line_id="+line_id+"&supplier_id="+supplier_id+"&name="+name+"&duty_paragraph="+duty_paragraph+"&phone="+phone+"&addr="+addr
					+"&open_bank="+open_bank+"&bank_account="+bank_account+"&remark="+remark+"&status="+status;
			String result = sendGet(ssurl);
			WebResult wr = JSONObject.parseObject(result, WebResult.class);
			supplier_id = wr.getSupplier_id();
			
			
			
		}
		
		
		return supplier_id;
		
	}
	
	
	
	/**
	 * 开票公司id
	 * @param name
	 * @return
	 */
	public String get_invoice_title_id(String name)
	{
		String url  = u_invice+getIdUrl(name);
		WebResult wr = getId(url);
		if(wr != null && wr.getInvoice_title_id() != null)
			return wr.getInvoice_title_id();
		else
			return "";
	}
	
	public String getIdByType(String name,String type)
	{

		try {
			name = java.net.URLEncoder.encode(name, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String id = "";
		switch (type) {
		case "invoice_title_id":
			id = get_invoice_title_id(name);
			break;

		case "customer_id":
			id = get_customer_id(name);
			break;

		case "supplier_id":
			id = get_supplier_id(name);
			break;

		default:
			break;
		}
		
		return id;
	}
	

	/**
	 * 获取客户id
	 * @param name
	 * @return
	 */
	public String get_customer_id(String name)
	{
		String url  = u_customer+getIdUrl(name);
		WebResult wr = getId(url);
		if(wr != null && wr.getCustomer_id() != null)
			return wr.getCustomer_id();
		else
			return "";
	}
	
	

	/**
	 * 获取供应商id
	 * @param name
	 * @return
	 */
	public String get_supplier_id(String name)
	{
		String url  = u_supplier+getIdUrl(name);
		WebResult wr = getId(url);
		if(wr != null && wr.getSupplier_id() != null)
			return wr.getSupplier_id();
		else
			return "";
	}
	
	
	public WebResult getId(String url)
	{
		String result = sendGet(url);
		WebResult wr = JSONObject.parseObject(result, WebResult.class);
		return wr;
	}
	
	public static void main(String[] args) {
		
		FinanceIo fi = new FinanceIo();
		
		String result = fi.get_invoice_title_id("上海麦广网络科技有限公司");
		
		
		
		System.out.println(result);
		
	} 
	/**
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public String sendGet(String url) {
        String result = "";
        BufferedReader in = null;
        try { 
        	
        	
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "dddddd");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
//            Map<String, List<String>> map = connection.getHeaderFields();
//            // 遍历所有的响应头字段
//            for (String key : map.keySet()) {
//                System.out.println(key + "--->" + map.get(key));
//            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(),"utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    } 
	
	
}
 



