package com.maimob.server.base;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by yang on 2018/4/18.
 */
@ApiModel(value = "请求基类", description = "请求基类")
public class BasicRequest<Data> {

    @ApiModelProperty(value = "sessionId", required = true)
    private String sessionId;

    @ApiModelProperty(value = "开始时间", required = true)
    private String minDate;

    @ApiModelProperty(value = "结束时间", required = true)
    private String maxDate;

    @ApiModelProperty(value = "appId", required = false)
    private Short appId;

    @ApiModelProperty(value = "每页显示条数", required = false)
    private Integer pageSize;

    @ApiModelProperty(value = "页码", required = false)
    private Integer pageNo;

    private Data data;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getMinDate() {
        return minDate;
    }

    public void setMinDate(String minDate) {
        this.minDate = minDate;
    }

    public String getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(String maxDate) {
        this.maxDate = maxDate;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Integer getPageSize() {
        if(pageSize == null){
            pageSize = 100;
        }
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNo() {
        if(pageNo == 0){
            pageNo = 1;
        }
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Short getAppId() {
        return appId;
    }

    public void setAppId(Short appId) {
        this.appId = appId;
    }
}
