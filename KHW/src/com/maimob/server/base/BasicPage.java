package com.maimob.server.base;

import com.maimob.server.db.entity.OperateReportFormAppToday;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

/**
 * Created by yang on 2018/4/19.
 */
public class BasicPage<Data> {

    private List<Data> list;

 private HashMap<String,String> appid;
    private HashMap<String,String> actionMap;

    public HashMap<String, String> getActionMap() {
        return actionMap;
    }

    public void setActionMap(HashMap<String, String> actionMap) {
        this.actionMap = actionMap;
    }

    public HashMap<String, String> getAppid() {
        return appid;
    }

    public void setAppid(HashMap<String, String> appid) {
        this.appid = appid;
    }

    private int currentPage;

    private int pageSize;

    private int totalPage;
    private int totalRecords;
    private List<Data> appVersionName;
    private List<Data> platform;

    public List<Data> getAppVersionName() {
        return appVersionName;
    }

    public void setAppVersionName(List<Data> appVersionName) {
        this.appVersionName = appVersionName;
    }

    public List<Data> getPlatform() {
        return platform;
    }

    public void setPlatform(List<Data> platform) {
        this.platform = platform;
    }

    public BasicPage(){}

    public BasicPage(int pageNo, int pageSize){
        this.currentPage = pageNo;
        this.pageSize = pageSize;
    }

    public List<Data> getList() {
        return list;
    }

    public void setList(List<Data> list) {
        this.list = list;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalRecords % pageSize == 0 ? totalRecords/pageSize : totalRecords/pageSize + 1;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

}
