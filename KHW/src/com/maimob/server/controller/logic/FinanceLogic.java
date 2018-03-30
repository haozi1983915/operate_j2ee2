package com.maimob.server.controller.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.maimob.server.data.task.CreateBill;
import com.maimob.server.db.entity.Admin;
import com.maimob.server.db.entity.BalanceAccount;
import com.maimob.server.db.entity.Dictionary;
import com.maimob.server.db.entity.Proxy;
import com.maimob.server.db.entity.Reward;
import com.maimob.server.db.service.DaoService;
import com.maimob.server.utils.Cache;
import com.maimob.server.utils.StringUtils;

public class FinanceLogic extends Logic {

	private DaoService dao;
	
	public FinanceLogic(DaoService dao) {
		super(dao);
		this.dao = dao;
	}
	
	
	public String getBill(String json)
	{
		String check = this.CheckJson(json);
		if(!StringUtils.isStrEmpty(check))
			return check;

		JSONObject whereJson = JSONObject.parseObject(json); 
 
		try {
			
			List<Map<String,String>> billlist = od.getBill(whereJson,adminid);
			baseResponse.setBillList(billlist);
			baseResponse.setRefreshBill(od.hasBillStep(adminid));
			baseResponse.setStatus(0);
			baseResponse.setStatusMsg("");
		} catch (Exception e) {
			// TODO: handle exception
		} 
		return this.toJson();
	}
	
	public String updateBill(String json)
	{
		String check = this.CheckJson(json);
		if(!StringUtils.isStrEmpty(check))
			return check;

		JSONObject whereJson = JSONObject.parseObject(json); 
		String id = whereJson.getString("id");
		CreateBill cb = new CreateBill();
		cb.RefreshBill(id);

		return this.toJson();
	}

	public String updateBillStatus(String json)
	{
		String check = this.CheckJson(json);
		if(!StringUtils.isStrEmpty(check))
			return check;

		JSONObject whereJson = JSONObject.parseObject(json); 
 
		try {

			String status = whereJson.getString("status");
			String billid = whereJson.getString("id");
			whereJson.remove("status");
			List<Map<String,String>> billlist = od.getBill(whereJson,adminid);

			if(billlist != null && billlist.size() > 0)
			{
				Map<String,String> bill = billlist.get(0);
				int billStatus = 43;
				int score = Integer.parseInt(bill.get("score"));
				if(bill.get("isUpdate").equals("1"))
				{
					int oldStep = Integer.parseInt(bill.get("step"));
					if(status.equals("40"))
					{
						billStatus = 45;
						int newStep = oldStep-1;
						int newScore = stepToScore(newStep);
						od.UpdatetBillStatus(billid, adminid, status, oldStep, newStep, newScore,billStatus);
					}
					else if(status.equals("39"))
					{
						billStatus = 44;
						score++;
						int newStep = this.scoreToStep(score);
						if(newStep == 4)
						{
							billStatus = 46;
						}
						else if(newStep == 5)
						{
							billStatus = 47;
						}
						
						od.UpdatetBillStatus(billid, adminid, status, oldStep, newStep, score,billStatus);
						
					}

					baseResponse.setStatus(0);
					baseResponse.setStatusMsg("账单修改完成");
				}
				else
				{

					baseResponse.setStatus(2);
					baseResponse.setStatusMsg("你没有权限操作账单");
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return this.toJson();
	}
	
	public int stepToScore(int step)
	{
		int score = 0;
		if(step == 1)
			score = 0;
		else if(step == 2)
			score = 1;
		else if(step == 3)
			score = 2;
		else if(step == 4)
			score = 4;
		else if(step == 5)
			score = 5;
		return score;
	}
	

	public int scoreToStep(int score)
	{
		int step = 0;
		if(score == 0)
			step = 1;
		else if(score == 1)
			step = 2;
		else if(score == 2)
			step = 3;
		else if(score == 4)
			step = 4;
		else if(score == 5)
			step = 5;
		return step;
	}
	
	

	public String getBillParameter(String json)
	{
		String check = this.CheckJson(json);
		if(!StringUtils.isStrEmpty(check))
			return check;

		JSONObject whereJson = JSONObject.parseObject(json); 
 
		try {

			String adminid = whereJson.getString("sessionid");
			Admin admin = this.getAdmin(adminid);
			admin.getDate();
			if(admin.getLevel() == 3)
			{
				whereJson.put("adminId", admin.getId());
			}

			List<Map<String,String>> billlist = od.getBillParameter(whereJson);
 
			List<String> proxyNameList = new ArrayList<String>();
			List<String> mainChannelNameList = new ArrayList<String>();
			List<Map<String,String>> adminList = new ArrayList<Map<String,String>>();
			
			Map<String,String>  billMap = new HashMap<String,String> ();
			for (int i = 0; i < billlist.size(); i++) {
				Map<String,String> bill = billlist.get(i);

//				String sql = "SELECT mainChannelName,proxyName,adminid,(select name from operate_admin a where a.id = b.adminid) adminname FROM db_operate.operate_bill b "+where[0];
				if(billMap.get(bill.get("mainChannelName")) == null)
					mainChannelNameList.add(bill.get("mainChannelName"));
				
				if(billMap.get(bill.get("proxyName")) == null)
					proxyNameList.add(bill.get("proxyName"));

				if(billMap.get(bill.get("adminid")) == null)
				{
					Map<String,String>adminmap = new HashMap<String,String>();
					adminmap.put("adminId", bill.get("adminid"));
					adminmap.put("adminname", bill.get("adminname"));
					adminList.add(adminmap);
					
				}
				
				billMap.put(bill.get("mainChannelName"), "");
				billMap.put(bill.get("proxyName"), "");
				billMap.put(bill.get("adminid"), "");
				baseResponse.setMainChannelNameList(mainChannelNameList);
				baseResponse.setProxyNameList(proxyNameList);
				baseResponse.setBillAdminList(adminList);
			}

			List<Dictionary> dic1 = Cache.getDicList(1);
			baseResponse.setAppList(dic1);
			List<Dictionary> dic16 = Cache.getDicList(16);
			baseResponse.setBillStatusList(dic16);

			List<Dictionary> dic14 = Cache.getDicList(14);
			baseResponse.setBillAdminStatusList(dic14);
			List<Dictionary> dic15 = Cache.getDicList(15);
			baseResponse.setBillAdminLastStatusList(dic15);
			whereJson.put("attributeId", "35");
			List<BalanceAccount>balist = dao.findBalanceAccount(whereJson);
			baseResponse.setBalanceAccountList(balist);
			
			baseResponse.setStatus(0);
			
			
		} catch (Exception e) {
			// TODO: handle exception
		} 
		return this.toJson();
	}
	
	

	public String getBillDetails(String json)
	{
		String check = this.CheckJson(json);
		if(!StringUtils.isStrEmpty(check))
			return check;

		JSONObject whereJson = JSONObject.parseObject(json);

		try {
			List<Map<String,String>> billlist = od.getBill(whereJson,adminid);
			if(billlist != null && billlist.size() > 0)
			{
				Map<String,String> bill = billlist.get(0);
				
				Proxy proxy = dao.findProxyById(Long.parseLong(bill.get("proxyId")));
				
				baseResponse.setProxy(proxy);
				baseResponse.setBill(bill);
				List<BalanceAccount> balist = dao.findBalanceAccountById(bill.get("payCompanyId"));
				baseResponse.setBalanceAccountList(balist);
				
				baseResponse.setBillDetails(this.getBillDetails(bill.get("mainChannelName"),bill.get("mainChannel"),bill.get("month"), bill.get("proxyId"), bill.get("appId")));
				
				List<Map<String,String>> billStepList = od.getBillStep(bill.get("id"));
				baseResponse.setBillStepList(billStepList);
			}
			
			
			baseResponse.setStatus(0);
			baseResponse.setStatusMsg("请重新登录");
		} catch (Exception e) {
			// TODO: handle exception
		} 
		return this.toJson();
	}
	
	
	
	public List<Map<String,String>> getBillDetails(String mainChannelName,String mainChannel,String month,String proxyid,String appid)
	{
		List<Map<String,String>> billDetaillist = null;
		try {
			billDetaillist = od.getBillDetail(month, proxyid, appid);
			Map<String,String> billDetaiMain = new HashMap<String,String>();
			double costSum = 0;
			Map<String,Double> rewardSum = new HashMap<String,Double>();
			Map<String,String> rewardPrice = new HashMap<String,String>();
			if(billDetaillist != null && billDetaillist.size() > 0)
			{
				for(int i = 0;i < billDetaillist.size();i++)
				{
					Map<String,String> billDetai = billDetaillist.get(i);
					String cost2 = billDetai.get("cost2");
					double cost = Double.parseDouble(cost2);
					costSum += cost;
					String rewardId = billDetai.get("rewardId");
					String outFirstGetPer = billDetai.get("outFirstGetPer");
					String outRegister = billDetai.get("outRegister");
					String outFirstGetSum = billDetai.get("outFirstGetSum");
					String outAccount = billDetai.get("outAccount");
					String outUpload = billDetai.get("outUpload");
					
					String[] reward = this.getCost(outFirstGetPer, outRegister, outFirstGetSum, outAccount, outUpload, rewardId);
					billDetai.put("rewardType", reward[0]);
					billDetai.put("rewardSum", reward[1]+" "+ reward[2]);
					double num = Double.parseDouble(reward[2]);
					if(rewardSum.get(reward[1]) == null)
						rewardSum.put(reward[1], 0d);
					rewardSum.put(reward[1], rewardSum.get(reward[1])+num);
					String price = getRewardHistory(Long.parseLong(rewardId));
					
					if(rewardPrice.get(reward[0] +" "+price) == null)
						rewardPrice.put(reward[0] +" "+price, "");
					
					billDetai.put("price", price);
					
				}
			}
			billDetaiMain.put("channelName", mainChannelName);
			billDetaiMain.put("channel", mainChannel);
			String rewardSumStr = "";
			for (Map.Entry<String,Double> entry : rewardSum.entrySet()) { 
				rewardSumStr += entry.getKey() +" "+entry.getValue()+"\n";
			}

			String rewardPriceStr = "";
			for (Map.Entry<String,String> entry : rewardPrice.entrySet()) { 
				rewardPriceStr += entry.getKey()+"\n";
			}
			
			billDetaiMain.put("price", rewardPriceStr);
			billDetaiMain.put("rewardSum", rewardSumStr);
			billDetaiMain.put("month", month);
			billDetaiMain.put("cost2", costSum+"");
			
			billDetaillist.add(0, billDetaiMain);
			 
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return billDetaillist;
	}
	
	
	
	
	
	public String[] getCost(String outFirstGetPer, String outRegister, String outFirstGetSum, 
			String outAccount, String outUpload,
			String rewardid) {
		String rewardStr[] = new String[3];
		List<Map<String, String>> rs = od.getReward(rewardid);
		if (rs != null && rs.size() > 0) {
			Map<String, String> r = rs.get(0);
			String typeId = r.get("typeId");
			float price = Float.parseFloat(r.get("price"));
			// '26', 'CPA 单价'
			// '27', 'CPS 首提'
			// '28', 'CPS 比例'
			// '29', 'CPS 开户'
			// '33', 'CPA 进件'

			// cpa单价 按注册
			// cps首提 按首提人数
			// cps比例 按首提金额
			// cps开户 按授信人数

			double cost = 0;
			
			
			String num = "";
			if (typeId.equals("26")) {
				num = outRegister;
				rewardStr[0] = "CPA单价";
				rewardStr[1] = "注册用户 ";
				rewardStr[2] =  num;
			} else if (typeId.equals("27")) {
				num = outFirstGetPer;
				rewardStr[0] = "CPS首提";
				rewardStr[1] = "首提用户 ";
				rewardStr[2] =  num;
			} else if (typeId.equals("28")) {
				num = outFirstGetSum;
				rewardStr[0] = "CPS比例";
				rewardStr[1] = "首提总额 ";
				rewardStr[2] =  num;
			}
			else if (typeId.equals("29")) {
				num = outAccount;
				rewardStr[0] = "CPS开户";
				rewardStr[1] = "开户用户";
				rewardStr[2] =  num;
			} else if (typeId.equals("33")) {
				num = outUpload;
				rewardStr[0] = "CPA进件";
				rewardStr[1] = "进件用户";
				rewardStr[2] =  num;
			}
 
		}

		return rewardStr;
	}
	
	
	
	
	

	private String getRewardHistory(long id)
	{
		String rewardPrice = "";
		List<Reward> rewardList = dao.findRewardById(id);
		List<Reward> rewardList2 = new ArrayList<Reward>();
		if(rewardList != null)
		{
			for (int j = 0; j < rewardList.size(); j++) {
				Reward reward = rewardList.get(j);
				Admin admin1 = Cache.getAdminCatche(reward.getAdminId());
				if (admin1 != null)
					reward.setAdminName(admin1.getName());

				Admin UpdateAdmin = Cache.getAdminCatche(reward.getUpdateAdminId());
				if (UpdateAdmin != null)
					reward.setUpdateAdminName(UpdateAdmin.getName());

				rewardList2.add(reward);
				long rewardTypeId = reward.getTypeId();

				if (rewardTypeId == 26) {
					rewardPrice += reward.getMax() + "/" + reward.getPrice() + "元";
					reward.setRewardPrice(rewardPrice);
				} else {
					rewardPrice += reward.getMax() + "/" + reward.getPrice() + "%";
					reward.setRewardPrice(rewardPrice);
				}

				for (int i = j + 1; i < rewardList.size(); i++) {
					Reward reward1 = rewardList.get(i);
					if (reward.getId() == reward1.getId()) {
						rewardPrice += ",";

						if (rewardTypeId == 26) {
							rewardPrice += reward1.getMax() + "/" + reward1.getPrice() + "元";
							reward.setRewardPrice(rewardPrice);
						} else {
							rewardPrice += reward1.getMax() + "/" + reward1.getPrice() + "%";
							reward.setRewardPrice(rewardPrice);
						}

					} else {
						return rewardPrice;
					}

					j = i;

				}
			}
		}
		
		
		return rewardPrice;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
