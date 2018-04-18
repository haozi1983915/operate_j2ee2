package com.maimob.server.service;

import com.maimob.server.base.BasicRequest;
import com.maimob.server.db.entity.OperateReportFormAppToday;

import java.util.List;

/**
 * Created by yang on 2018/4/18.
 */
public interface MicroService {

    List<OperateReportFormAppToday> summary(BasicRequest request);
}
