package com.maimob.server.importData.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.maimob.server.importData.dao.LoansDao;
import com.maimob.server.importData.dao.OperateDao;

public class InportChannel {

	LoansDao ld = new LoansDao();
	OperateDao od = new OperateDao();

	public static void main(String[] args) {
		InportChannel ic = new InportChannel();
		ic.inputChannel();
//		ic.upputChannel();
	}

	public void inputChannel() {

		String sql = "select * from loans_proxy order by channelCompany,channelLevel asc";

		// String sql2 = "insert into operate_proxy values()";

		try {

			String sql1 = "SELECT parentId, count(1) cou FROM db_loans.loans_proxy group by parentId;";
			List<Map<String, String>> parentIdlist = ld.Query(sql1);
			Map<String, String> p_c = new HashMap<String, String>();
			for (int i = 0; i < parentIdlist.size(); i++) {
				String parentId = parentIdlist.get(i).get("parentId");
				String cou = parentIdlist.get(i).get("cou");
				p_c.put(parentId, cou);
			}

			List<Map<String, String>> proxyList = ld.Query(sql);
			for (int i = 0; i < proxyList.size(); i++) {
				Map<String, String> proxy = proxyList.get(i);

				String id = proxy.get("id");
				String parentId = proxy.get("parentId");

				String permissionId = id;

				if (parentId.equals("1")) {
					String registerChartPermission = proxy.get("registerChartPermission");
					String loginChartPermission = proxy.get("loginChartPermission");
					String applyChartPermission = proxy.get("applyChartPermission");
					String loanAcctChartPermission = proxy.get("loanAcctChartPermission");
					String cashNumCharPermission = proxy.get("cashNumCharPermission");
					String firstCashAmtChartPermission = proxy.get("firstCashAmtChartPermission");
					String totalCashAmtChartPermission = proxy.get("totalCashAmtChartPermission");
					String sql2 = "insert into operate_channel_permission (id,registerChartPermission,loginChartPermission,applyChartPermission,loanAcctChartPermission,cashNumCharPermission,firstCashAmtChartPermission,totalCashAmtChartPermission)"
							+ "values(" + id + "," + registerChartPermission + "," + loginChartPermission + ","
							+ applyChartPermission + "," + loanAcctChartPermission + "," + cashNumCharPermission + ","
							+ firstCashAmtChartPermission + "," + totalCashAmtChartPermission + ")";

					od.Update(sql2);

					String company = proxy.get("channelCompany");
					String contacts = proxy.get("channelContactName");
					String mobileno = proxy.get("channelContactMobile");
					String email = proxy.get("channelConatctEmail");
					String pwd = proxy.get("channelAccountPwd");
					String createTime = proxy.get("createTime");

					String channelTag = proxy.get("channelTag");
					

					int channelCou = 0;
					String cc = p_c.get(id);
					if (cc != null && !cc.equals("")) {
						channelCou = Integer.parseInt(cc);
						channelCou--;
					}

					String sql3 = "insert into operate_proxy (id,company,contacts,mobileno,email,pwd,createTime,channelCou,permissionId,channelNo)"
							+ "values(" + id + ",'" + company + "','" + contacts + "'," + mobileno + ",'" + email
							+ "','" + pwd + "','" + createTime + "'," + channelCou + "," + permissionId + ",'" + channelTag + "');";

					od.Update(sql3);
				} else {
					permissionId = parentId;
				}

				String level = proxy.get("channelLevel");
				String channelName = proxy.get("channelCNDes");
				String url = proxy.get("channelDes");
				String channel = proxy.get("channel");
				String channelNo = proxy.get("channelTag");
				String appid = "1";
				String isCreate = proxy.get("createAcctPermission");
				String status = proxy.get("status");
				String discount = proxy.get("discount");
				String adminId = "1516587173233";

				String sql3 = "insert into operate_channel (id,proxyId,channelName,channel,channelNo,level,higherChannelId,isCreate,url,adminId,permissionId,appId,status,discount)"
						+ "values(" + id + "," + parentId + ",'" + channelName + "','" + channel + "','" + channelNo
						+ "'," + level + "," + parentId + "," + isCreate + ",'" + url + "'," + adminId + ","
						+ permissionId + "," + appid + "," + status + "," + discount + ");";
				
				od.Update(sql3);

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void upputChannel()
	{
		String path = "/Users/zhanghao/Downloads/proxy.txt";
				

		StringBuilder result = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(path)));// 构造一个BufferedReader类来读取文件
			String s = null;
			int i = 0;
			while ((s = br.readLine()) != null) {// 使用readLine方法，一次读一行
				
				String sql = "insert into loans_proxy (id,parentId,channelLevel,channel,channelTag,channelCNDes,channelDes,fuzzySearch,channelCompany,"
						+ "channelContactName,channelContactMobile,channelConatctEmail,channelAccountPwd,createAcctPermission,manageChannelPermission"
						+ ",registerChartPermission,loginChartPermission,applyChartPermission,loanAcctChartPermission,cashNumCharPermission,firstCashAmtChartPermission,totalCashAmtChartPermission"
						+ ",createTime,status,discount)"
						+ "values("+s+")";
				
				ld.Update(sql);
				System.out.println((i++ +"  ")+s);
				
				
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		
		
	}

	/**
	 * 读取txt文件的内容
	 * 
	 * @param file
	 *            想要读取的文件对象
	 * @return 返回文件内容
	 */
	public static String txt2String(File file) {
		StringBuilder result = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));// 构造一个BufferedReader类来读取文件
			String s = null;
			while ((s = br.readLine()) != null) {// 使用readLine方法，一次读一行
				result.append(System.lineSeparator() + s);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}

}
