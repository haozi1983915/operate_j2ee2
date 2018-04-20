package com.maimob.server.base;

import com.maimob.server.db.entity.OperateReportFormAppToday;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by yang on 2018/4/19.
 */
public class BasicPage<Data> {

    private List<Data> list;

    private int currentPage;

    private int pageSize;

    private int totalPage;

    private int totalRecords;

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
