package com.maimob.server.service.impl;

import com.maimob.server.base.BasicPage;
import com.maimob.server.base.BasicRequest;
import com.maimob.server.db.daoImpl.OperateActionIdcardDaoImpl;
import com.maimob.server.db.daoImpl.OperateActionSubmitImpl;
import com.maimob.server.db.daoImpl.OperateReportFormAppTodayImpl;
import com.maimob.server.db.entity.OperateActionIdcard;
import com.maimob.server.db.entity.OperateActionSubmit;
import com.maimob.server.db.entity.OperateReportFormAppToday;
import com.maimob.server.service.MicroService;
import com.wordnik.swagger.annotations.ApiModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yang on 2018/4/18.
 */
@Service
public class MicroServiceImpl implements MicroService {

    @Autowired
    private OperateReportFormAppTodayImpl dao;

    @Autowired
    private OperateActionIdcardDaoImpl operateActionIdcardDao;

    @Autowired
    private OperateActionSubmitImpl actionSubmitDao;

    @Override
    public List<OperateReportFormAppToday> summary(BasicPage<OperateReportFormAppToday> page, BasicRequest request) {
        return dao.summary(page, request);
    }

    @Override
    public List<OperateActionIdcard> applyRetention(BasicPage<OperateActionIdcard> page, BasicRequest request) {
        return operateActionIdcardDao.applyRetention(page, request);
    }

    @Override
    public List<OperateActionSubmit> openRetention(BasicPage<OperateActionSubmit> page, BasicRequest request) {
        return actionSubmitDao.applyRetention(page, request);
    }
}
