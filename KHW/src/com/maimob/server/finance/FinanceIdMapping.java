package com.maimob.server.finance;

import java.util.HashMap;
import java.util.Map;

import com.maimob.server.importData.dao.OperateDao;
import com.maimob.server.utils.StringUtils;

public class FinanceIdMapping extends FinanceIo {
	
//	line_id	整数	api/get_line_id接口获取的line_id
//	invoice_title_id	整数	api/get_invoice_title_id接口获取的invoice_title_id
//	customer_id	整数	api/get_customer_id接口获取的customer_id
//	service_name	字符串	服务名称，该字段具有唯一性，实时对接的核心所在，所以各个业务部门技术需要与业务人员进行沟通，设计该字段如何拼接，同一个服务名称，多次请求该API，会以最后一次提交的数据作为最终数据
	
	public String getLineid()
	{
		return "3";
	}
	OperateDao od = new OperateDao();
	
	public String getId(String name,String type)
	{
		String id = IdMap.get(name+"_"+type);
		if(id == null)
		{
			  id = od.getFinanceId(name, type);
			
			if(StringUtils.isStrEmpty(id))
			{
				id = this.getIdByType(name, type);
			}
			
			IdMap.put(name+"_"+type, id);
		}
		return id;
	}
	
	Map<String,String> IdMap = new HashMap<String,String>();
	
	
//	sign	字符串	每个接口必传参数，用于校验用户身份
//	line_id	整数	api/get_line_id接口获取的line_id
//	invoice_title_id	整数	api/get_invoice_title_id接口获取的invoice_title_id
//	customer_id	整数	api/get_customer_id接口获取的customer_id
//	service_name	字符串	服务名称，该字段具有唯一性，实时对接的核心所在，所以各个业务部门技术需要与业务人员进行沟通，设计该字段如何拼接，同一个服务名称，多次请求该API，会以最后一次提交的数据作为最终数据
//	belong_period	字符串	该数据的归属期间，如有疑问与业务人员进行沟通。格式如：2018-03。
//	money	浮点数	单位元，该数据对应的金额。需要注意的是该金额是累加金额，各个业务部门自行处理该金额的累计情况，传递给财务系统的是最终金额。
	
	
	public String set_income(String invoice_title,String customer,String service_name,String belong_period,String money)
	{
		String invoice_title_id = this.getId(invoice_title, "invoice_title_id");
		String customer_id = this.getId(customer, "customer_id");
		
		return this.set_income_info(invoice_title_id, customer_id, service_name, belong_period, money);
		
	}
	
	

	public String set_cost(String invoice_title,String supplier,String service_name,String belong_period,String money,String type)
	{
		String invoice_title_id = this.getId(invoice_title, "invoice_title_id");
		String supplier_id = this.getId(supplier, "supplier_id");
		
		return this.set_cost_info(invoice_title_id, supplier_id, service_name, belong_period, money,type);
		
	}
	
	
	
	
	
	
	
	
	
	

}
