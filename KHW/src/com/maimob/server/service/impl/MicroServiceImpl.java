package com.maimob.server.service.impl;

import com.maimob.server.base.BasicRequest;
import com.maimob.server.db.daoImpl.OperateReportFormAppTodayImpl;
import com.maimob.server.db.entity.OperateReportFormAppToday;
import com.maimob.server.service.MicroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by yang on 2018/4/18.
 */
@Service
public class MicroServiceImpl implements MicroService {

    @Autowired
    private OperateReportFormAppTodayImpl dao;

    @Override
    public List<OperateReportFormAppToday> summary(BasicRequest request) {
        return dao.summary(request);
    }
}
