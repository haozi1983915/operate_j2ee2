package com.maimob.server.service;

import com.maimob.server.base.BasicPage;
import com.maimob.server.base.BasicRequest;
import com.maimob.server.db.dto.OperateActionDto;
import com.maimob.server.db.dto.OperateLoanDto;
import com.maimob.server.db.entity.OperateActionIdcard;
import com.maimob.server.db.entity.OperateActionLoan;
import com.maimob.server.db.entity.OperateActionSubmit;
import com.maimob.server.db.entity.OperateReportFormAppToday;

import java.util.List;

/**
 * Created by yang on 2018/4/18.
 */
public interface MicroService {

    List<OperateReportFormAppToday> summary(BasicPage<OperateReportFormAppToday> page, BasicRequest request);

    List<OperateActionIdcard> applyRetention(BasicPage<OperateActionIdcard> page, BasicRequest request);

    List<OperateActionSubmit> openRetention(BasicPage<OperateActionSubmit> page, BasicRequest request);

    List<OperateActionLoan> loanRetention(BasicPage<OperateActionLoan> page, BasicRequest request);

    List<OperateActionDto> openDistribution(BasicPage<OperateActionDto> page, BasicRequest request);

    List<OperateLoanDto> loanDistribution(BasicPage<OperateLoanDto> page, BasicRequest request);
}
