package com.maimob.server.controller.logic;

import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.maimob.server.base.BasicPage;
import com.maimob.server.data.task.CreateBill;
import com.maimob.server.db.entity.*;
import com.maimob.server.db.entity.Dictionary;
import com.maimob.server.db.service.DaoService;
import com.maimob.server.importData.dao.OperateDao;
import com.maimob.server.utils.Cache;
import com.maimob.server.utils.ExportMapExcel;
import com.maimob.server.utils.StringUtils;

import javax.management.Query;
import javax.servlet.http.HttpServletResponse;

public class ActionLogic extends Logic {

    private DaoService dao;

    public ActionLogic(DaoService dao) {
        super(dao);
        this.dao = dao;
    }

    public ActionLogic() {
        super(null);
    }


    public String getAction(String json) {
        String check = this.CheckJson(json);
        if (!StringUtils.isStrEmpty(check))
            return check;

        JSONObject whereJson = JSONObject.parseObject(json);

        try {
            Map<String, Map<String, String>> actionlist = od.getUserAction(whereJson);
            baseResponse.setActionList(actionlist);
            baseResponse.setStatus(0);
            baseResponse.setStatusMsg("");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String jsonstr = this.toJson();
        return jsonstr;
    }


    public String getYamTimeAction(String json) {
        String check = this.CheckJson(json);
        if (!StringUtils.isStrEmpty(check))
            return check;

        JSONObject whereJson = JSONObject.parseObject(json);
        try {
            List<Map<String, String>> actionlist = od.getYamTimeAction(whereJson);
            baseResponse.setActionYamList(actionlist);
            baseResponse.setStatus(0);
            baseResponse.setStatusMsg("");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String jsonstr = this.toJson();
        return jsonstr;
    }

    public String getPageTimeAction(String json) {
        String check = this.CheckJson(json);
        if (!StringUtils.isStrEmpty(check))
            return check;

        JSONObject whereJson = JSONObject.parseObject(json);
        try {
            List<Map<String, String>> actionlist = od.getPageTimeAction(whereJson);
            baseResponse.setActionYamList(actionlist);
            baseResponse.setStatus(0);
            baseResponse.setStatusMsg("");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String jsonstr = this.toJson();
        return jsonstr;
    }

    public String getPageErrorAction(String json) {
        String check = this.CheckJson(json);
        if (!StringUtils.isStrEmpty(check))
            return check;
        JSONObject whereJson = JSONObject.parseObject(json);
        try {
            BasicPage<Map<String, String>> actionlist = od.getErrorAction(whereJson);
            baseResponse.setListSize(actionlist.getPageSize() + " ");
            baseResponse.setActionYamList(actionlist.getList());
            baseResponse.setStatus(0);
            baseResponse.setStatusMsg("");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String jsonstr = this.toJson();
        return jsonstr;
    }

    public String getPageErrorSearchAction(String json) {
        String check = this.CheckJson(json);
        if (!StringUtils.isStrEmpty(check))
            return check;
        List<Dictionary> appList = Cache.getDicList(1);
        baseResponse.setAppList(appList);
        HashMap<String, String> actionMap = new HashMap<>();
        JSONObject whereJson = JSONObject.parseObject(json);
        BasicPage<Map<String, String>> basicPage = od.getErrorSearchAction(whereJson);
        List<Map<String, String>> actionlist = basicPage.getList();
        int i = 0;
        for (Map<String, String> map : actionlist) {
            for (String key : map.keySet()) {
                if (key.equals("page_name")) {
                    String value = map.get(key);
                    if (value.equals("huizongPage"))
                        actionMap.put("汇总页", value);
                    else if (value.equals("jibenxingxiPage"))
                        actionMap.put("基本信息页", value);
                    else if (value.equals("loginPage"))
                        actionMap.put("登陆页", value);
                    else if (value.equals("registerPage"))
                        actionMap.put("注册页", value);
                    else if (value.equals("shenfenzhengPage"))
                        actionMap.put("身份证页", value);
                    else if (value.equals("yinhangkaPage"))
                        actionMap.put("银行卡页", value);
                }
            }
            i++;
        }
        basicPage.setList(null);
        basicPage.setActionMap(actionMap);
        baseResponse.setBasicPage(basicPage);
        String jsonstr = this.toJson();
        return jsonstr;
    }

    public void exportErrorPage(String json, HttpServletResponse response) {
        String check = this.CheckJson(json);
        if (!StringUtils.isStrEmpty(check))
            return;
        JSONObject whereJson = JSONObject.parseObject(json);
        OperateDao od = new OperateDao();
        BasicPage<Map<String, String>> basicPage = od.getErrorAction(whereJson);
        List<Map<String, String>> reportforms = basicPage.getList();
        for (Map<String, String> map : reportforms) {
            for (String key : map.keySet()) {
                if (map.get(key) == null) {
                    map.put(key, "' '");
                }
                if (key.equals("page_name")) {
                    String value = map.get(key);
                    if (value.equals("huizongPage"))
                        map.put(key, "汇总页");
                    else if (value.equals("jibenxingxiPage"))
                        map.put(key, "基本信息页");
                    else if (value.equals("loginPage"))
                        map.put(key, "登陆页");
                    else if (value.equals("registerPage"))
                        map.put(key, "注册页");
                    else if (value.equals("shenfenzhengPage"))
                        map.put(key, "身份证页");
                    else if (value.equals("yinhangkaPage"))
                        map.put(key, "银行卡页");
                }
            }
        }
        if (reportforms.size() == 0) {
            return;
        }
        JSONArray arr = whereJson.getJSONArray("tag");
        List<String> listName = new ArrayList<>();
        listName.add("日期");
        listName.add("页面");
        listName.add("错误类型");
        listName.add("版本");
        listName.add("机型");
        listName.add("系统");
        listName.add("合计");

        List<String> listId = new ArrayList<>();
        listId.add("date");
        listId.add("page_name");
        listId.add("error");
        listId.add("app_version_name");
        listId.add("model");
        listId.add("platform");
        listId.add("cou");
        ExportMapExcel exportExcelUtil = new ExportMapExcel();
        exportExcelUtil.exportExcelString("返回分析报表", listName, listId, reportforms, response);
    }

    public String getUserAction(String json) {
        String check = this.CheckJson(json);
        if (!StringUtils.isStrEmpty(check))
            return check;
        JSONObject whereJson = JSONObject.parseObject(json);
        try {
            BasicPage<Map<String, String>> actionlist = od.getUsersAction(whereJson);
            baseResponse.setBasicPage(actionlist);
            baseResponse.setStatus(0);
            baseResponse.setStatusMsg("");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String jsonstr = this.toJson();
        return jsonstr;
    }

    public String getUsersActionParam(String json) {
        String check = this.CheckJson(json);
        if (!StringUtils.isStrEmpty(check))
            return check;
        JSONObject whereJson = JSONObject.parseObject(json);
        try {
            BasicPage<Map<String, String>> actionlist = od.getUsersActionParam(whereJson);
            baseResponse.setBasicPage(actionlist);
            baseResponse.setStatus(0);
            baseResponse.setStatusMsg("");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String jsonstr = this.toJson();
        return jsonstr;
    }

    public void exportUserAction(String json, HttpServletResponse response) {
        String check = this.CheckJson(json);
        if (!StringUtils.isStrEmpty(check))
            return;
        JSONObject whereJson = JSONObject.parseObject(json);
        OperateDao od = new OperateDao();
        BasicPage<Map<String, String>> basicPage = od.getUsersAction(whereJson);
        List<Map<String, String>> reportforms = basicPage.getList();
        for (Map<String, String> map : reportforms) {
            for (String key : map.keySet()) {
                if (map.get(key) == null) {
                    map.put(key, "");
                }
            }
        }
        if (reportforms.size() == 0) {
            return;
        }
        JSONArray arr = whereJson.getJSONArray("tag");
        List<String> listName = new ArrayList<>();
        listName.add("日期");
        listName.add("新注册");
        listName.add("类别");

        List<String> listId = new ArrayList<>();
        listId.add("date");
        listId.add("cou");
        listId.add("name");
        ExportMapExcel exportExcelUtil = new ExportMapExcel();
        exportExcelUtil.exportExcelString("用户分析报表", listName, listId, reportforms, response);
    }
    public void saveChannelAdmin(long adminId,long channelId,String startDate){
        String hasTodaySql = "select * from operate_channel_admin where channelId="+channelId+" and startDate=" + "'" + startDate + "'";
        try {
            List<Map<String, String>> flag = od.Query(hasTodaySql);
            if (!flag.isEmpty()) {
                String updateToday = "update operate_channel_admin o set o.adminId=" + adminId + " where o.id=" + flag.get(0).get("id");
                od.Update(updateToday);
            } else {
                String getEndDate = "select  * from operate_channel_admin o where channelId =" + channelId + " and o.startDate >" + "'" + startDate + "'" + " order by startDate asc limit 1";
                String updateEnd = "select  * from operate_channel_admin o where channelId =" + channelId + " and o.startDate <" + "'" + startDate + "'" + " order by endDate asc limit 1";
                List<Map<String, String>> later = od.Query(getEndDate);
                List<Map<String, String>> early = od.Query(updateEnd);
                StringBuffer sb = new StringBuffer();
                sb.append("insert into operate_channel_admin(adminId,channelId,startDate");
                String endDate = "";
                if (!later.isEmpty()) {
                    endDate = later.get(0).get("startDate");
                    sb.append(",endDate");
                }
                sb.append(" ) values (" + adminId + "," + channelId + "," + "'" + startDate + "'");
                if (!later.isEmpty()) {
                    sb.append("," + "'" + endDate + "'");
                }
                sb.append(" )");
                String insertSQL = sb.toString();
                od.Update(insertSQL);
                if (!early.isEmpty()) {
                    int ocaId = Integer.parseInt(early.get(0).get("id"));
                    String updateEarly = "update operate_channel_admin o set endDate=" + "'" + startDate + "'" + " where o.id=" + ocaId;
                    od.Update(updateEarly);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public String addChannelAdmin(String json) {
        String check = this.CheckJson(json);
        if (!StringUtils.isStrEmpty(check)) {
            return check;
        }
        JSONObject whereJson = JSONObject.parseObject(json);
        long adminId = Long.parseLong(whereJson.getString("adminId"));
        
        String cid = whereJson.getString("channelId");
        if(StringUtils.isStrEmpty(cid))
        {
            List<ChannelAdmin> channelAdminList = new ArrayList<>();
            ChannelAdmin channelAdmin = new ChannelAdmin();
            channelAdmin.setAdminId(adminId);
            channelAdminList.add(channelAdmin);
            baseResponse.setChannelAdmins(channelAdminList);
        }
        else
        {
        	long channelId = Long.parseLong(whereJson.getString("channelId"));
            String startDate = whereJson.getString("startDate");
            String sessionId = whereJson.getString("sessionId");
            saveChannelAdmin(adminId,channelId,startDate);
            //若该渠道是一级渠道，将下发所有负责人
            String channelLevel="select * from operate_channel o where o.id="+channelId;
            try {
                List<Map<String, String>> channel = od.Query(channelLevel);
                int level= Integer.parseInt(channel.get(0).get("level"));
                long proxyId= Long.parseLong(channel.get(0).get("proxyId"));
                if (level==1){
                    //一级渠道需要更改其二级渠道所有负责人
                    String secondSql="select * from operate_channel o where o.proxyId ="+proxyId +" and o.level = 2";
                    List<Map<String, String>> query = od.Query(secondSql);
                    for (Map<String,String> map:query) {
                        long channels = Long.parseLong(map.get("id"));
                        saveChannelAdmin(adminId, channels, startDate);
                        //更新channel
                        LocalDate now = LocalDate.now();
                        String nowdate="'"+now+"'";
                        String queryAdminId="select * from operate_channel_admin o where o.channelId="+channelId+" and o.startDate <="+nowdate+" and ( o.endDate >"+nowdate +" or o.endDate is NULL )order by startDate DESC limit 1";
                        List<Map<String, String>> querys = od.Query(queryAdminId);
                        long newAdminId = Long.parseLong(querys.get(0).get("adminId"));
                        String updateChannel="update operate_channel o set o.adminId=" + newAdminId + " where o.id =" + channels;
                        od.Update(updateChannel);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //返回当前负责人
            long adminId1 = 0;
            Date currentTime = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = formatter.format(currentTime);
            String getnowAdminSql = "select * from operate_channel_admin o where channelId = " + channelId + " and o.startDate <=" + "'" + dateString + "'" + " and  ( endDate IS NULL or o.endDate >" + "'" + dateString + "'" + " )";
            try {
                List<Map<String, String>> nowadmin = od.Query(getnowAdminSql);
                if (!nowadmin.isEmpty()) {
                    adminId1 = Long.parseLong(nowadmin.get(0).get("adminId"));
                    List<ChannelAdmin> channelAdminList = new ArrayList<>();
                    ChannelAdmin channelAdmin = new ChannelAdmin();
                    channelAdmin.setAdminId(adminId1);
                    channelAdminList.add(channelAdmin);
                    baseResponse.setChannelAdmins(channelAdminList);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //更新该渠道的adminId
            String channelSql = "update operate_channel o set o.adminId=" + adminId1 + " where o.id =" + channelId;
            try {
                od.Update(channelSql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        
        String jsonstr = this.toJson();
        return jsonstr;
    }

    public String getChannelAdmin(String json) {
        String check = this.CheckJson(json);
        if (!StringUtils.isStrEmpty(check)) {
            return check;
        }
        JSONObject whereJson = JSONObject.parseObject(json);
        long channelId = Long.parseLong(whereJson.getString("channelId"));
        String queryChannelAdmin = "select *,(select b.name from operate_admin b where b.id=a.adminId) name from operate_channel_admin a where channelId =" + channelId+ " order by startDate";
        try {
            List<Map<String, String>> ChannelAdminList = od.Query(queryChannelAdmin);
            List<ChannelAdmin> channelAdminList = new ArrayList<>();
            for (Map<String, String> map : ChannelAdminList) {
                ChannelAdmin channelAdmin = new ChannelAdmin();
                channelAdmin.setAdminId(Long.valueOf(map.get("adminId")));
//                String sql = "select * from operate_admin where id=" + channelAdmin.getAdminId();
//                List<Map<String, String>> admin = od.Query(sql);
                channelAdmin.setAdminName(map.get("name"));
                channelAdmin.setChannelId(Long.valueOf(map.get("channelId")));
                channelAdmin.setStartDate(map.get("startDate"));
                channelAdmin.setEndDate(map.get("endDate"));
                channelAdminList.add(channelAdmin);
            }
            baseResponse.setChannelAdmins(channelAdminList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String jsonstr = this.toJson();
        return jsonstr;
    }
    public String batchSetChannelAdmin(String json) {
        String check = this.CheckJson(json);
        if (!StringUtils.isStrEmpty(check)) {
            return check;
        }
        JSONObject whereJson = JSONObject.parseObject(json);
        long adminOffer=Long.parseLong(whereJson.getString("adminOffer"));
        long proxyId=Long.parseLong(whereJson.getString("proxyId"));
        long adminAcc=Long.parseLong(whereJson.getString("adminAcc"));
        String startDate=whereJson.getString("startDate");
        String sql="select * from operate_channel_admin where adminId="+adminOffer;
        if (proxyId!=0){
            sql +=" and channelId IN (SELECT id from operate_channel where proxyId="+proxyId+")";
        }
        try {
            List<Map<String, String>> ChannelAdminList = od.Query(sql);
            for (Map<String, String> map : ChannelAdminList) {
                long channelId= Long.parseLong(map.get("channelId"));
                saveChannelAdmin(adminAcc,channelId,startDate);
                //更新channel
                LocalDate now = LocalDate.now();
                String nowdate="'"+now+"'";
                String queryAdminId="select * from operate_channel_admin o where o.channelId="+channelId+" and o.startDate <="+nowdate+" and ( o.endDate >"+nowdate +" or o.endDate is NULL )order by startDate DESC limit 1";
                List<Map<String, String>> query = od.Query(queryAdminId);
                long newAdminId = Long.parseLong(query.get(0).get("adminId"));
                String updateChannel="update operate_channel o set o.adminId=" + newAdminId + " where o.id =" + channelId;
                od.Update(updateChannel);
                baseResponse.setStatusMsg("success");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            baseResponse.setStatusMsg("failed");
        }
        String jsonstr = this.toJson();
        return jsonstr;
    }

    public String getAdminProxy(String json) {
        String check = this.CheckJson(json);
        if (!StringUtils.isStrEmpty(check)) {
            return check;
        }
        List<Admin> adminList = dao.findAllAdmin();
        baseResponse.setAdminList(adminList);
        List<Proxy> proxyList = dao.findAllProxy();
        baseResponse.setProxyList(proxyList);
        String jsonstr = this.toJson();
        return jsonstr;
    }
}
